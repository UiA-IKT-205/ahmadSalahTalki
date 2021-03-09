package com.example.superpiano

import android.content.DialogInterface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.superpiano.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*

public class MainActivity : AppCompatActivity() {

    private val tag:String ="Superpiano:MainActivity"

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth:FirebaseAuth

    private lateinit var Piano:PianoLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        signInAnonymously()

        Piano = supportFragmentManager.findFragmentById(binding.piano.id) as PianoLayout

        Piano.onsave = {
            this.upload(it)
        }
    }
    private fun upload(file: Uri){
        Log.d(tag, "upload file $file")
        val ref = FirebaseStorage.getInstance().reference.child("melodies/${file.lastPathSegment}")
        val uploadTask = ref.putFile(file)

        uploadTask.addOnSuccessListener {
            Log.d(tag,"Saved file to firebase ${it.toString()}")
        }.addOnFailureListener{
            Log.e(tag,"Error saving file to firebase",it)
        }
    }
    // denne funksjonen skal gi oss mulighet til å loge inn som ukjent bruker
    private fun signInAnonymously(){
        auth.signInAnonymously().addOnSuccessListener {
            Log.d(tag,"Login success ${it.user.toString()}")
        }.addOnFailureListener{
            Log.e(tag,"Login failed",it)
        }
}

}