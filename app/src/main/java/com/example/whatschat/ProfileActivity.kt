package com.example.whatschat

import android.R.attr
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.whatschat.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class ProfileActivity : AppCompatActivity() {
    private lateinit var home: ImageView
    private lateinit var profile: ImageView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    lateinit var binding:ActivityProfileBinding
    lateinit var ImageUri:Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_profile)
        binding=ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        home=findViewById(R.id.home)
        profile=findViewById(R.id.profile)
        var select: Button =findViewById(R.id.select)
        var upload: Button =findViewById(R.id.upload)
     /* var view: Button =findViewById(R.id.viewimage)

        view.setOnClickListener{
            var intent=Intent(this@ProfileActivity,RetrieveActivity::class.java)

            startActivity(intent)
        }
*/
        //BottomNavigation
        binding.home.setOnClickListener{
            val intent= Intent(this@ProfileActivity,MainActivity::class.java)
            startActivity(intent)
        }
        binding.profile.setOnClickListener {
            val intent = Intent(this@ProfileActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.select.setOnClickListener{
            selectImage()
        }

        binding.upload.setOnClickListener{
            uploadImage()
        }
    }

    private fun uploadImage() {
   var progressDialog=ProgressDialog(this)
        progressDialog.setMessage("Upload image")
        progressDialog.show()

        val formatter= SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now=Date()
        val fileName=formatter.format(now)
        val storageRef= FirebaseStorage.getInstance().getReference("images/$fileName")

        storageRef.putFile(ImageUri).addOnSuccessListener {

        binding.image.setImageURI(null)
            Toast.makeText(this@ProfileActivity,"Image is Uploaded",Toast.LENGTH_SHORT).show()
            if (progressDialog.isShowing)progressDialog.dismiss()

        }.addOnFailureListener{
            if (progressDialog.isShowing)progressDialog.dismiss()
            Toast.makeText(this@ProfileActivity,"Failed to upload",Toast.LENGTH_SHORT).show()

        }
    }

    private fun selectImage() {
      val intent=Intent()
        intent.type="image/*"
        intent.action=Intent.ACTION_GET_CONTENT

        startActivityForResult(intent,100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
if (requestCode==100 && resultCode ==RESULT_OK){
ImageUri=data?.data!!
    binding.image.setImageURI(ImageUri)
}

    }
}