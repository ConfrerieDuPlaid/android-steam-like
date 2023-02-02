package com.example.android_steam_like

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android_steam_like.databinding.LoginFragmentBinding
import com.example.android_steam_like.entities.User
import com.example.android_steam_like.entities.UserCredentials
import com.example.android_steam_like.utils.CustomSteamAPI
import com.example.android_steam_like.utils.GenericAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Login : AppCompatActivity() {
    //todo : message d'erreur on login fail
    //todo : surligner champs vides onclick login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        supportActionBar?.hide()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, LoginFragment())
            .commit()
    }
}

class LoginFragment: Fragment () {
    private lateinit var binding: LoginFragmentBinding
    private var email: String = ""
    private var password: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNavLinks(view)
        setLoginListener(view)
        setInputListeners(view)
    }

    private fun login (res: User) {
        User.setInstance(res)
        findNavController().navigate(R.id.home2)
//            LoginFragment
//            intent = Intent(this, Home::class.java)
//            startActivity(intent)
    }

    private fun setLoginListener (view: View) {
        view.findViewById<Button>(R.id.login_button).setOnClickListener {
            if (this.email != "" && this.password != "") {
                val userCredentials = UserCredentials(email, password)
                GlobalScope.launch(Dispatchers.Main) {
                    GenericAPI.call(CustomSteamAPI.NetworkRequest::login, userCredentials, ::login)
                }
            }
        }
    }

    private fun setInputListeners (view: View) {
        view.findViewById<EditText>(R.id.user_email).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                email = s.toString()
            }
        })
        view.findViewById<EditText>(R.id.user_password).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                password = s.toString()
            }
        })
    }

    private fun setNavLinks (view: View) {
        view.findViewById<Button>(R.id.new_account_button).setOnClickListener {
            findNavController().navigate(
                R.id.signinFragment,
                bundleOf("email" to email, "password" to password)
            )
        }
        view.findViewById<Button>(R.id.forgotten_password_button).setOnClickListener {
            findNavController().navigate(
                R.id.forgottenPassword,
                bundleOf("email" to email)
            )
//                intent = Intent(this, ForgottenPassword::class.java)
//                if (email != "") intent.putExtra("email", email)
//                startActivity(intent)
        }
    }
}