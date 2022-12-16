package com.example.whatschat

import android.app.ProgressDialog
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.example.whatschat.databinding.ActivityRetrieveBinding
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class RetrieveActivity : AppCompatActivity() {
    lateinit var binding: ActivityRetrieveBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRetrieveBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_retrieve)

        binding.retrieve.setOnClickListener{
            var progressDialog= ProgressDialog(this)
            progressDialog.setMessage("Image is being retrieved")
            progressDialog.setCancelable(false)
            progressDialog.show()


            val imageName=binding.imageEt.text.toString()
            var storageRef=FirebaseStorage.getInstance().reference.child("images/$imageName.jpg")

            //val storageRef= FirebaseStorage.getInstance().getReference("images/$imageName.jpg")

            val localfile= File.createTempFile("tempImage","jpg")

            storageRef.getFile(localfile).addOnSuccessListener {
                if (progressDialog.isShowing)progressDialog.dismiss()
                val bitmap= BitmapFactory.decodeFile(localfile.absolutePath)

                binding.image.setImageBitmap(bitmap)


            }.addOnFailureListener{


                if (progressDialog.isShowing)progressDialog.dismiss()
                Toast.makeText(this@RetrieveActivity,"Failed to retrieve Image", Toast.LENGTH_SHORT).show()
            }
        }
    }
}