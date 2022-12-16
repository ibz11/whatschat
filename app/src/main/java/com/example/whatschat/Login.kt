package com.example.whatschat

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import android.net.ConnectivityManager

class Login : AppCompatActivity() {
    private lateinit var emailtxt:EditText
    private lateinit var passwordtxt:EditText
    private lateinit var signup_btn:Button
    private lateinit var login_btn:Button
    private lateinit var mAuth:FirebaseAuth

    val CHANNEL_ID="ChannelId"
    val CHANNEL_NAME="channel_name"
    val NOTIFICATION_ID=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth=FirebaseAuth.getInstance()


        login_btn =findViewById(R.id.login_btn)
        signup_btn =findViewById(R.id.signup_btn)
        passwordtxt =findViewById(R.id.password)
        emailtxt=findViewById(R.id.email)






        createNotificationChannel()
        val intent= Intent(this,MainActivity::class.java)
        val pendingIntent= TaskStackBuilder.create(this).run{
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notification= NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle("New Message!")
            .setContentText("Welcome Back!")
            .setSmallIcon(R.drawable.logo)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .build()
        val notificationManager= NotificationManagerCompat.from(this)
        login_btn.setOnClickListener{
            val email=emailtxt.text.toString()
            val password=passwordtxt.text.toString()
            login(email,password)
            notificationManager.notify(NOTIFICATION_ID,notification)
        }
        signup_btn.setOnClickListener{
         val signupintent=Intent(this,Signup::class.java)
            startActivity(signupintent)
        }

    }

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val Homeintent=Intent(this@Login,MainActivity::class.java)
                    finish()
                    startActivity(Homeintent)
                } else {

                    Toast.makeText(this@Login,"Wrong password or email",Toast.LENGTH_SHORT).show()

                }
            }
    }
    fun createNotificationChannel() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES .O){
            val channel = NotificationChannel(CHANNEL_ID,CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH)
            channel.lightColor = Color.GREEN
            channel.enableLights(true)

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
}}