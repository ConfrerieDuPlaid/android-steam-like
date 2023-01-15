package com.example.android_steam_like

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.android_steam_like.entities.User
import org.json.JSONObject

class Login : AppCompatActivity() {
    private var email: String = ""
    private var password: String = ""

    //todo : retirer la navbar
    //todo : message d'erreur on login fail
    //todo : surligner champs vides onclick login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        supportActionBar?.hide()

        setNavLinks()
        setLoginListener()
        setInputListeners()
    }

    private fun login (res: String) {
        User.setInstanceFromJson(JSONObject(res))
        intent = Intent(this, Home::class.java)
        startActivity(intent)
    }

    private fun failLogin (code: Int) {
        println("Identifiants invalides !")
    }

    private fun setLoginListener () {
        findViewById<Button>(R.id.login_button).setOnClickListener {
            if (this.email != "" && this.password != "") {
                User.login(email, password, this::login, this::failLogin)
            }
        }
    }

    private fun setInputListeners () {
        findViewById<EditText>(R.id.user_email).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                email = s.toString()
            }
        })
        findViewById<EditText>(R.id.user_password).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                password = s.toString()
            }
        })
    }

    private fun setNavLinks () {
        findViewById<Button>(R.id.new_account_button).setOnClickListener {
            intent = Intent(this, Signin::class.java)
            if (email != "") intent.putExtra("email", email)
            if (password != "") intent.putExtra("password", password)
            startActivity(intent)
        }
        findViewById<Button>(R.id.forgotten_password_button).setOnClickListener {
            intent = Intent(this, ForgottenPassword::class.java)
            if (email != "") intent.putExtra("email", email)
            startActivity(intent)
        }
    }
}