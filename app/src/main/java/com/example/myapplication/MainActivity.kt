package com.example.myapplication

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var button:Button = findViewById(R.id.button)
        button.setOnClickListener {
            (it as Button).text = "Нажата"
        }
    }
}