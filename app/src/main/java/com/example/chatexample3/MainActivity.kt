package com.example.chatexample3

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //testFirebase(getString(R.string.test_mail), getString(R.string.test_pass))
        checkAuthentification(getString(R.string.test_mail), getString(R.string.test_pass))
    }

    fun testFirebase(emailAddress: String, password: String) {
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(emailAddress, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // User registered successfully
                    Log.d("TEST", task.toString());
                }
            }
    }

    private fun checkAuthentification(emailAddress: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            // Already signed in
            // Do nothing
        } else {
            auth.signInWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // User signed in successfully
                    }
                }
        }
    }

    private fun testSignOut() {
        FirebaseAuth.getInstance().signOut()
    }
}
