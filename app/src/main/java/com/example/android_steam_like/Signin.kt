package com.example.android_steam_like

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.android_steam_like.entities.User
import com.example.android_steam_like.entities.UserSignupBody
import com.example.android_steam_like.utils.CustomSteamAPI
import com.example.android_steam_like.utils.GenericAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Signin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin)
        supportActionBar?.hide()

//        supportFragmentManager.beginTransaction()
//            .replace(R.id.signin_container, SigninFragment())
//            .commit()
    }
}

class SigninFragment: Fragment() {
    private var usernameInput: EditText? = null
    private var emailInput: EditText? = null
    private var passwordInput: EditText? = null
    private var passwordVerifInput: EditText? = null
    private var email: String = ""
    private var password: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(requireContext()).inflate(R.layout.signin_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDataFromIntent(view)
        setSigninListener(view)

    }

    private fun setDataFromIntent (view: View) {
        this.usernameInput = view.findViewById(R.id.username)
        this.emailInput = view.findViewById(R.id.user_email)
        this.passwordInput = view.findViewById(R.id.user_password)
        this.passwordVerifInput = view.findViewById(R.id.user_password_verification)
//            if (this.intent.hasExtra("email")) {
//                this.email = this.intent.getStringExtra("email")!!
//            }
//            if (this.intent.hasExtra("password"))
//                this.password = this.intent.getStringExtra("password")!!
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
//            intent = Intent(this, Home::class.java)
//            startActivity(intent)
    }

    private fun setSigninListener (view: View) {
        view.findViewById<Button>(R.id.signin_button).setOnClickListener {
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