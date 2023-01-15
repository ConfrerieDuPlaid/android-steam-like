package com.example.android_steam_like

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.android_steam_like.entities.User

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        setNavLinks()
        setLoginListener()
    }

    private fun login (res: String) {
        intent = Intent(this, Home::class.java)
        startActivity(intent)
    }

    private fun failLogin (code: Int) {
        println("Identifiants invalides !")
    }

    private fun setLoginListener () {
        findViewById<Button>(R.id.login_button).setOnClickListener {
            val email = findViewById<EditText>(R.id.user_email).text.toString()
            val password = findViewById<EditText>(R.id.user_password).text.toString()

            if (email != "" && password != "") {
                User.login(email, password, this::login, this::failLogin)
            }
        }
    }

    private fun setNavLinks () {
        findViewById<Button>(R.id.new_account_button).setOnClickListener {
            intent = Intent(this, Signin::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.forgotten_password_button).setOnClickListener {
            intent = Intent(this, ForgottenPassword::class.java)
            startActivity(intent)
        }
    }
}