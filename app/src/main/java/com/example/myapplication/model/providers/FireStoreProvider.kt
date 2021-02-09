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

    override fun subscribeToAllNotes(): LiveData<NoteResult> =
            MutableLiveData<NoteResult>().apply {
                try {
                    getUserNotesCollections().addSnapshotListener { snapshot, error ->
                        value = error?.let { NoteResult.Error(error) }
                                ?: snapshot?.let { it ->
                                    val notes = it.documents.map { it.toObject(Note::class.java) }
                                    NoteResult.Success(notes)
                                }
                    }
                } catch (e: Throwable) {
                    value = NoteResult.Error(e)
                }
            }


    override fun getNoteById(id: String): LiveData<NoteResult> =
            MutableLiveData<NoteResult>().apply {
                try {
                    getUserNotesCollections().document(id)
                            .get()
                            .addOnSuccessListener { snapshot ->
                                value = NoteResult.Success(snapshot.toObject(Note::class.java))
                            }
                            .addOnFailureListener { exception ->
                                value = NoteResult.Error(exception)
                            }
                } catch (e: Throwable) {
                    value = NoteResult.Error(e)
                }
            }


    override fun saveNote(note: Note): LiveData<NoteResult> =
            MutableLiveData<NoteResult>().apply {
                try {
                    getUserNotesCollections().document(note.id)
                            .set(note).addOnSuccessListener {
                                Log.d(TAG, "Note $note is saved")
                                value = NoteResult.Success(note)
                            }.addOnFailureListener {
                                OnFailureListener { exception ->
                                    Log.d(TAG, "Error saving note $note, message: ${exception.message}")
                                    value = NoteResult.Error(exception)
                                }
                            }
                } catch (e: Throwable) {
                    value = NoteResult.Error(e)
                }
            }

    override fun getCurrentUser(): LiveData<User?> =
            MutableLiveData<User?>().apply {
                value = currentUser?.let {
                    User(
                            it.displayName ?: "",
                            it.email ?: ""
                    )
                }
            }

    private fun getUserNotesCollections() = currentUser?.let { firebaseUser ->
        db.collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()

    override fun deleteNote(noteId: String): LiveData<NoteResult> =
            MutableLiveData<NoteResult>().apply {
                try {
                    getUserNotesCollections()
                            .document(noteId)
                            .delete()
                            .addOnSuccessListener { value = NoteResult.Success(null) }
                            .addOnFailureListener { throw it }
                } catch (e: Throwable) {
                    value = NoteResult.Error(e)
                }
            }
}