package com.example.arogyanidhi.ui.schemes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arogyanidhi.data.local.DocumentDao
import com.example.arogyanidhi.data.local.DocumentEntity
import com.example.arogyanidhi.domain.model.Scheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SchemeDetailViewModel @Inject constructor(
    private val documentDao: DocumentDao
) : ViewModel() {

    private val _scheme = MutableStateFlow<Scheme?>(null)
    val scheme: StateFlow<Scheme?> = _scheme.asStateFlow()

    private val _documents = MutableStateFlow<List<DocumentEntity>>(emptyList())
    val documents: StateFlow<List<DocumentEntity>> = _documents.asStateFlow()

    fun loadScheme(schemeId: String) {
        val schemeData = SchemeRepository.getSchemeById(schemeId)
        _scheme.value = schemeData
        schemeData?.let { loadDocuments(it) }
    }

    private fun loadDocuments(scheme: Scheme) {
        viewModelScope.launch {
            documentDao.getDocumentsForScheme(scheme.id).collect { docs ->
                if (docs.isEmpty()) {
                    scheme.documentsRequired.forEachIndexed { i, docName ->
                        documentDao.insertDocument(
                            DocumentEntity(id = "${scheme.id}_$i", schemeId = scheme.id, name = docName)
                        )
                    }
                } else {
                    _documents.value = docs
                }
            }
        }
    }

    fun toggleDocument(document: DocumentEntity) {
        viewModelScope.launch {
            documentDao.updateDocument(document.copy(isReady = !document.isReady))
        }
    }
}
