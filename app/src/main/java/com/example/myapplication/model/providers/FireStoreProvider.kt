package com.example.myapplication.model.providers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.model.NoAuthException
import com.example.myapplication.model.Note
import com.example.myapplication.model.NoteResult
import com.example.myapplication.model.User
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val NOTES_COLLECTION = "notes"
private const val USERS_COLLECTION = "users"

class FireStoreProvider(
        private val firebaseAuth: FirebaseAuth,
        private val db: FirebaseFirestore
) : RemoteDataProvider {

    companion object {
        private val TAG = "${FireStoreProvider::class.java.simpleName} :"
    }

    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser

    override suspend fun subscribeToAllNotes(): ReceiveChannel<NoteResult> =
            Channel<NoteResult>(Channel.CONFLATED).apply {
                var registration: ListenerRegistration? = null
                try {
                    registration = getUserNotesCollections()
                            .addSnapshotListener { snapshot, e ->
                                val value = e?.let {
                                    NoteResult.Error(it)
                                } ?: snapshot?.let {
                                    val notes = it.documents.map {
                                        it.toObject(Note::class.java)
                                    }
                                    NoteResult.Success(it)
                                }
                                value?.let { offer(it) }
                            }
                } catch (e: Throwable) {
                    offer(NoteResult.Error(e))
                }
                invokeOnClose { registration?.remove() }
            }

    override suspend fun getNoteById(id: String): Note =
            suspendCoroutine { continuation ->
                try {
                    getUserNotesCollections().document(id)
                            .get()
                            .addOnSuccessListener { snapshot ->
                                continuation.resume(snapshot.toObject(Note::class.java)!!)
                            }
                            .addOnFailureListener { exception ->
                                continuation.resumeWithException(exception)
                            }
                } catch (e: Throwable) {
                    continuation.resumeWithException(e)
                }
            }

    override suspend fun saveNote(note: Note): Note =
            suspendCoroutine { continuation ->
                try {
                    getUserNotesCollections().document(note.id)
                            .set(note).addOnSuccessListener {
                                continuation.resume(note)
                            }.addOnFailureListener {
                                OnFailureListener { exception ->
                                    continuation.resumeWithException(exception)
                                }
                            }
                } catch (e: Throwable) {
                    continuation.resumeWithException(e)
                }
            }

    override suspend fun getCurrentUser(): User? =
            suspendCoroutine { continuation ->
                currentUser?.let {
                    continuation.resume(User(
                            it.displayName ?: "",
                            it.email ?: ""
                    ))
                } ?: continuation.resume(null)
            }

    private fun getUserNotesCollections() = currentUser?.let { firebaseUser ->
        db.collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()

    override suspend fun deleteNote(noteId: String): Note? =
            suspendCoroutine { contination ->
                try {
                    getUserNotesCollections()
                            .document(noteId)
                            .delete()
                            .addOnSuccessListener { contination.resume(null) }
                            .addOnFailureListener { contination.resumeWithException(it) }
                } catch (e: Throwable) {
                    contination.resumeWithException(e)
                }
            }
}