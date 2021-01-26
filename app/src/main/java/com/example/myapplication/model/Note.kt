package com.example.myapplication.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Note(
        val id: String,
        val title: String,
        val note: String,
        val color: Color = Color.WHITE,
        val lastChanged: Date = Date()
) : Parcelable {

    companion object {
        fun createNewNote(title: String, body: String): Note = Note(generateId(), title, body)

        fun generateId(): String {
            return UUID.randomUUID().toString()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Note

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

enum class Color() {
    WHITE,
    YELLOW,
    GREEN,
    BLUE,
    RED,
    VIOLET,
    PINK
}