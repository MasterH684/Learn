package com.example.learn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Hierboven staat 'boilerplate' hieronder staan de 2 knoppen geinitialiseerd.
        val buttonA: Button = findViewById(R.id.buttonA)
        buttonA.setOnClickListener {
            // de tweede parameter geeft de nieuwe java classe naam weer geladen moet worden.
            val intent = Intent(this, PushUps::class.java)
            startActivity(intent)
        }
        val buttonB: Button = findViewById(R.id.Statistics)
        buttonB.setOnClickListener {
            // de tweede parameter geeft de nieuwe java classe naam weer geladen moet worden.
            val intent = Intent(this, subMenuB::class.java)
            startActivity(intent)
        }
    }
}
