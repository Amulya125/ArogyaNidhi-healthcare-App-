package com.example.arogyanidhi.data.repository

import com.example.arogyanidhi.domain.model.UserProfile
import com.example.arogyanidhi.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : UserRepository {

    override fun getUserProfile(uid: String): Flow<UserProfile?> = callbackFlow {
        val docRef = firestore.collection("users").document(uid)
        val listener = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) { trySend(null); return@addSnapshotListener }
            val profile = snapshot?.toObject(UserProfile::class.java)
            trySend(profile)
        }
        awaitClose { listener.remove() }
    }

    override suspend fun saveUserProfile(userProfile: UserProfile): Result<Unit> {
        return try {
            firestore.collection("users").document(userProfile.uid).set(userProfile).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadProfileImage(uid: String, imageBytes: ByteArray): Result<String> {
        return try {
            val ref = storage.reference.child("profile_images/$uid.jpg")
            ref.putBytes(imageBytes).await()
            val url = ref.downloadUrl.await().toString()
            Result.success(url)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
