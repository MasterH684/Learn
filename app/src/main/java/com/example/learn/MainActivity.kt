package com.example.learn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonA: Button = findViewById(R.id.buttonA)
        buttonA.setOnClickListener {
            val intent = Intent(this, PushUps::class.java)
            startActivity(intent)
        }
        val buttonB: Button = findViewById(R.id.buttonB)
        buttonB.setOnClickListener {
            val intent = Intent(this, subMenuB::class.java)
            startActivity(intent)
        }
    }
}
