package com.example.android_steam_like

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.android_steam_like.entities.User
import com.example.android_steam_like.entities.UserSignupBody
import com.example.android_steam_like.utils.CustomSteamAPI
import com.example.android_steam_like.utils.GenericAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Signin : AppCompatActivity() {
    private var usernameInput: EditText? = null
    private var emailInput: EditText? = null
    private var passwordInput: EditText? = null
    private var passwordVerifInput: EditText? = null
    private var email: String = ""
    private var password: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin)
        supportActionBar?.hide()

        setDataFromIntent()
        setSigninListener()
    }

    private fun setDataFromIntent () {
        this.usernameInput = findViewById(R.id.username)
        this.emailInput = findViewById(R.id.user_email)
        this.passwordInput = findViewById(R.id.user_password)
        this.passwordVerifInput = findViewById(R.id.user_password_verification)
        if (this.intent.hasExtra("email")) {
            this.email = this.intent.getStringExtra("email")!!
        }
        if (this.intent.hasExtra("password"))
            this.password = this.intent.getStringExtra("password")!!
        if (this.email != "") {
            this.emailInput?.setText(email, TextView.BufferType.EDITABLE)
        }
        if (this.password != "") {
            this.passwordInput?.setText(password, TextView.BufferType.EDITABLE)
            this.passwordVerifInput?.setText(password, TextView.BufferType.EDITABLE)
        }
    }

    private fun signin (res: User) {
        User.setInstance(res)
        intent = Intent(this, Home::class.java)
        startActivity(intent)
    }

    private fun setSigninListener () {
        findViewById<Button>(R.id.signin_button).setOnClickListener {
            val username = usernameInput?.text.toString()
            email = emailInput?.text.toString()
            password = passwordInput?.text.toString()
            val passwordVerif = passwordVerifInput?.text.toString()

            if (username != "" && email != "" && password != "" && password == passwordVerif) {
                val userCredentials = UserSignupBody(username, email, password)
                GlobalScope.launch(Dispatchers.Main) {
                    GenericAPI.call(CustomSteamAPI.NetworkRequest::signup, userCredentials, ::signin)
                }
            }
        }
    }
}