package com.example.chatexample3

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.R.menu
import android.support.annotation.NonNull
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task


class MainActivity : AppCompatActivity() {

    private val SIGN_IN_REQUEST_CODE: Int = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Start sign in/sign up activity
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .build(),
                SIGN_IN_REQUEST_CODE
            );
        } else {
            // User is already signed in. Therefore, display
            // a welcome Toast
            Toast.makeText(this,
                "Welcome " + FirebaseAuth.getInstance()
                    ?.getCurrentUser()
                    ?.getDisplayName(),
                Toast.LENGTH_LONG)
                .show();

            // Load chat room contents
            displayChatMessages();
        }
    }

    private fun displayChatMessages() {

    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(
                    this,
                    "Successfully signed in. Welcome!",
                    Toast.LENGTH_LONG
                )
                    .show()
                displayChatMessages()
            } else {
                Toast.makeText(
                    this,
                    "We couldn't sign you in. Please try again later.",
                    Toast.LENGTH_LONG
                )
                    .show()

                // Close the app
                finish()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === R.id.menu_sign_out) {
            AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(object : OnCompleteListener<Void> {
                    override fun onComplete(task: Task<Void>) {
                        Toast.makeText(
                            this@MainActivity,
                            "You have been signed out.",
                            Toast.LENGTH_LONG
                        )
                            .show()

                        // Close activity
                        finish()
                    }
                })
        }
        return true
    }
}
