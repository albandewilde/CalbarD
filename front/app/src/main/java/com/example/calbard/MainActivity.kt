package com.example.calbard

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {
    var savedNickname: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button : Button = findViewById(R.id.validateButton)
        var pseudo : EditText = findViewById(R.id.pseudoInput)
        val insertPseudoText : TextView = findViewById(R.id.insertPseudo)
        val pref: SharedPreferences = getSharedPreferences("Infos", MODE_PRIVATE)

        if( pref.contains("Nickname") ) {
            pseudo.setText(pref.getString("Nickname", ""))
        }

        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("3712", "test", importance).apply {
            description = "Ceci est une notification"
        }
        val notificationManager: NotificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
        button.setOnClickListener() {
            if(!pseudo.text.isEmpty()) {
                val editor: SharedPreferences.Editor = pref.edit()
                editor.putString("Nickname", pseudo.text.toString())
                editor.apply()

                goToAllPictures(pseudo.text.toString())
            } else {
                Toast.makeText(this, R.string.pseudo_error, Toast.LENGTH_LONG).show()
                insertPseudoText.setTextColor(Color.RED)
            }
        }
    }

    fun goToAllPictures(pseudo: String) {
        this.sendNotification()
        val intent = Intent(this, MainPage::class.java)
        intent.putExtra("Pseudo", pseudo)
        startActivity(intent)
    }

    fun sendNotification() {
        var titleContent = "Titre Notif"
        var messageContent = "Test"

        var builder =
            NotificationCompat.Builder(this, "3712")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(titleContent)
                .setContentText(messageContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
    }
}