package com.example.android_steam_like

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.android_steam_like.entities.User

class ForgottenPassword : AppCompatActivity() {
    private var emailInput: EditText? = null
    private var passwordInput: EditText? = null
    private var passwordVerifInput: EditText? = null
    private var tokenInput: EditText? = null
    private var email: String = ""
    private var password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgotten_password)
        supportActionBar?.hide()

        setDataFromIntent()
        displayOnlyResetInputs()
        setTokenRequestListener()
    }

    private fun setToken (res: String) {
        println(res)
    }

    private fun setTokenRequestListener () {
        findViewById<Button>(R.id.request_token_button).setOnClickListener {
            email = emailInput?.text.toString()
            if (email != "") {
                println("sendRequest")
                User.requestToken(email, this::setToken)
            }
        }
    }

    private fun setDataFromIntent () {
        this.emailInput = findViewById(R.id.user_email)
        this.passwordInput = findViewById(R.id.user_password)
        this.passwordVerifInput = findViewById(R.id.user_password_verification)
        this.tokenInput = findViewById(R.id.token)
        if (this.intent.hasExtra("email")) {
            this.email = this.intent.getStringExtra("email")!!
        }
        if (this.email != "") {
            this.emailInput?.setText(email, TextView.BufferType.EDITABLE)
        }
    }

    private fun displayOnlyResetInputs () {
        this.tokenInput?.visibility = View.INVISIBLE
        this.passwordInput?.visibility = View.INVISIBLE
        this.passwordVerifInput?.visibility = View.INVISIBLE
        findViewById<Button>(R.id.reset_password_button).visibility = View.INVISIBLE
    }

    private fun displayOnlyNewPasswordInputs () {
        this.emailInput?.visibility = View.INVISIBLE
        findViewById<Button>(R.id.request_token_button).visibility = View.INVISIBLE
        this.tokenInput?.visibility = View.VISIBLE
        this.passwordInput?.visibility = View.VISIBLE
        this.passwordVerifInput?.visibility = View.VISIBLE
        findViewById<Button>(R.id.reset_password_button).visibility = View.VISIBLE

    }
}