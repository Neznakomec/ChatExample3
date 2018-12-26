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
import com.google.firebase.database.FirebaseDatabase
import android.widget.EditText
import android.support.design.widget.FloatingActionButton
import android.text.format.DateFormat
import android.view.View
import android.widget.ListView
import com.firebase.ui.database.FirebaseListAdapter
import android.widget.TextView






class MainActivity : AppCompatActivity() {

    private val SIGN_IN_REQUEST_CODE: Int = 100

    private var adapter: FirebaseListAdapter<ChatMessage>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

        //
        val fab = findViewById<View>(R.id.fab) as FloatingActionButton

        fab.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val input = findViewById<View>(R.id.input) as EditText

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                FirebaseDatabase.getInstance()
                    .reference
                    .push()
                    .setValue(
                        ChatMessage(
                            input.text.toString(),
                            FirebaseAuth.getInstance()
                                .currentUser!!
                                .displayName
                        )
                    )

                // Clear the input
                input.setText("")
            }
        })
    }

    private fun displayChatMessages() {
        val listOfMessages = findViewById<View>(R.id.list_of_messages) as ListView

        adapter = object : FirebaseListAdapter<ChatMessage>(
            this, ChatMessage::class.java,
            R.layout.message, FirebaseDatabase.getInstance().reference
        ) {
            override fun populateView(v: View, model: ChatMessage, position: Int) {
                // Get references to the views of message.xml
                val messageText = v.findViewById<View>(R.id.message_text) as TextView
                val messageUser = v.findViewById<View>(R.id.message_user) as TextView
                val messageTime = v.findViewById<View>(R.id.message_time) as TextView

                // Set their text
                messageText.text = model.messageText
                messageUser.text = model.messageUser

                // Format the date before showing it
                messageTime.setText(
                    DateFormat.format(
                        "dd-MM-yyyy (HH:mm:ss)",
                        model.messageTime
                    )
                )
            }
        }

        listOfMessages.setAdapter(adapter)
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
