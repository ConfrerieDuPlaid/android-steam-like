package com.example.android_steam_like

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.android_steam_like.entities.RecPassword
import com.example.android_steam_like.entities.ResPassword
import com.example.android_steam_like.utils.CustomSteamAPI
import com.example.android_steam_like.utils.GenericAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        displayRequestTokenInputs()
        setTokenRequestListener()
        setResetPasswordListener()
    }

    private fun setToken (res: RecPassword) {
        this.tokenInput?.setText(res.token, TextView.BufferType.EDITABLE)
        displayPasswordResetInputs()
    }

    private fun setTokenRequestListener () {
        findViewById<Button>(R.id.request_token_button).setOnClickListener {
            email = emailInput?.text.toString()
            if (email != "") {
                GlobalScope.launch(Dispatchers.Main) {
                    GenericAPI.call(CustomSteamAPI.NetworkRequest::requestToken, email, ::setToken)
                }
            }
        }
    }

    private fun setResetPasswordListener () {
        findViewById<Button>(R.id.reset_password_button).setOnClickListener {
            this.password = this.passwordInput?.text.toString()
            val token = this.tokenInput?.text.toString()
            val passwordVerification = this.passwordVerifInput?.text.toString()
            if (token != "" && this.password != "" && passwordVerification == this.password) {
                val body = ResPassword(token, password)
                GlobalScope.launch(Dispatchers.Main) {
                    GenericAPI.call(CustomSteamAPI.NetworkRequest::resetPassword, body, ::backToLogin)
                }
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

    private fun displayRequestTokenInputs () {
        this.tokenInput?.visibility = View.INVISIBLE
        this.passwordInput?.visibility = View.INVISIBLE
        this.passwordVerifInput?.visibility = View.INVISIBLE
        findViewById<Button>(R.id.reset_password_button).visibility = View.INVISIBLE
    }

    private fun displayPasswordResetInputs () {
        this.emailInput?.visibility = View.INVISIBLE
        findViewById<Button>(R.id.request_token_button).visibility = View.INVISIBLE
        this.tokenInput?.visibility = View.VISIBLE
        this.passwordInput?.visibility = View.VISIBLE
        this.passwordVerifInput?.visibility = View.VISIBLE
        findViewById<Button>(R.id.reset_password_button).visibility = View.VISIBLE
    }

    private fun backToLogin (res: Int) {
        if (res == 1) {
            intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }
}