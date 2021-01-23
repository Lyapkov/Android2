package com.example.myapplication.model

object Repository {

    private val notes: List<Note>

    init {
        notes = listOf(
                Note(
                        "Первая",
                        "1111111",
                        0xfff0692.toInt()
                ),
                Note(
                        "Вторая",
                        "2222222",
                        0xfff0692.toInt()
                ),
                Note(
                        "Третья",
                        "333333",
                        0xfff0692.toInt()
                ),
                Note(
                        "Четвертая",
                        "444444",
                        0xfff0692.toInt()
                ),
                Note(
                        "Пятая",
                        "555555",
                        0xfff0692.toInt()
                )
        )
    }

    fun getNotes(): List<Note> = notes
}