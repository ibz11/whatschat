package com.example.whatschat

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Signup : AppCompatActivity() {
    private lateinit var nametxt: EditText
    private lateinit var emailtxt: EditText
    private lateinit var passwordtxt: EditText
    private lateinit var signup_btn: Button
    private lateinit var mDbRef:DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    val CHANNEL_ID="ChannelId"
    val CHANNEL_NAME="channel_name"
    val NOTIFICATION_ID=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        mAuth=FirebaseAuth.getInstance()

        nametxt =findViewById(R.id.nametxt)
         signup_btn =findViewById(R.id.signup_btn)
        passwordtxt =findViewById(R.id.password)
         emailtxt=findViewById(R.id.email)
          supportActionBar?.hide()

        createNotificationChannel()
        val intent= Intent(this,MainActivity::class.java)
        val pendingIntent= TaskStackBuilder.create(this).run{

            addNextIntentWithParentStack(intent)

            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notification= NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle("New notification!")
            .setContentText("Thank you signing up.Happy Chatting!!")
            .setSmallIcon(R.drawable.logo)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .build()
        val notificationManager= NotificationManagerCompat.from(this)

        signup_btn.setOnClickListener{
            val name=nametxt.text.toString()
            val email=emailtxt.text.toString() 
            val password=passwordtxt.text.toString()
            signup(name,email,password)
          notificationManager.notify(NOTIFICATION_ID,notification)
        }
    }

    private fun signup(name:String,email: String, password: String) {

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserToDatabase(name,email,mAuth.currentUser?.uid!!)
                    Toast.makeText(this@Signup,"Your Account has been made successfuly",Toast.LENGTH_SHORT).show()
                val Homeintent=Intent(this@Signup,MainActivity::class.java)
               finish()
                    startActivity(Homeintent)
                }
                else {
                  Toast.makeText(this@Signup,"Error",Toast.LENGTH_SHORT).show()
                }
            }

    }
private fun addUserToDatabase(name:String,email: String,uid:String){
    mDbRef = FirebaseDatabase.getInstance().getReference()
    mDbRef.child("user").child(uid).setValue(User(name,email,uid))
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