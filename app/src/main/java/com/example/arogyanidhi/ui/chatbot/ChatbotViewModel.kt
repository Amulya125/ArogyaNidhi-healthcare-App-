package com.example.arogyanidhi.ui.chatbot

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arogyanidhi.domain.model.ChatMessage
import com.example.arogyanidhi.network.Content
import com.example.arogyanidhi.network.GeminiRequest
import com.example.arogyanidhi.network.Part
import com.example.arogyanidhi.network.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    private val retrofitClient: RetrofitClient
) : ViewModel() {

    private val API_KEY = "AIzaSyAcJbolb6mvHFU_TVgXiERe5TLS6_DGT70"

    private val _messages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                "Namaste! I am ArogyaBot. How can I help you today?",
                isUser = false
            )
        )
    )

    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun sendMessage(userText: String) {

        if (userText.isBlank()) return

        _messages.value =
            _messages.value + ChatMessage(
                userText,
                isUser = true
            )

        _isLoading.value = true

        viewModelScope.launch {

            try {

                val prompt = """
You are ArogyaBot, a friendly healthcare assistant for Indian government health schemes.

Rules:
- Give short and clear answers
- Help users understand health schemes
- Mention required documents when needed
- Explain eligibility simply
- Be polite and helpful

User Question:
$userText
"""

                val request = GeminiRequest(
                    contents = listOf(
                        Content(
                            parts = listOf(
                                Part(prompt)
                            )
                        )
                    )
                )

                Log.d("CHATBOT_DEBUG", "Sending request to Gemini")

                val response =
                    retrofitClient.geminiService.generateContent(
                        apiKey = API_KEY,
                        request = request
                    )

                Log.d("CHATBOT_DEBUG", "Response received")

                val reply =
                    response.candidates
                        ?.firstOrNull()
                        ?.content
                        ?.parts
                        ?.firstOrNull()
                        ?.text
                        ?: "No response generated."

                _messages.value =
                    _messages.value + ChatMessage(
                        reply,
                        isUser = false
                    )

            } catch (e: Exception) {

                Log.e("CHATBOT_DEBUG", "ERROR", e)

                val errorMessage = when {

                    e.message?.contains("429") == true ->
                        "Gemini API limit reached. Please wait 1 minute and try again."

                    e.message?.contains("401") == true ->
                        "Invalid Gemini API key."

                    e.message?.contains("403") == true ->
                        "Gemini access denied."

                    else ->
                        "Unable to connect to AI service."
                }

                _messages.value =
                    _messages.value + ChatMessage(
                        errorMessage,
                        isUser = false
                    )
            } finally {

                _isLoading.value = false
            }
        }
    }
}