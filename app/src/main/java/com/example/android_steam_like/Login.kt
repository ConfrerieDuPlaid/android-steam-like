package com.example.android_steam_like

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        setNavLinks()
        setLoginListener()
        setInputListeners()
    }

    private fun login (res: User) {
        User.setInstance(res)
        findNavController().navigate(R.id.home2)
    }

    private fun setLoginListener () {
        binding.loginButton.setOnClickListener {
            if (email != "" && password != "") {
                val userCredentials = UserCredentials(email, password)
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        GenericAPI.call(CustomSteamAPI.NetworkRequest::login, userCredentials, ::login)
                        binding.errors.visibility = View.GONE
                    }catch (e: Exception){
                        binding.errors.visibility = View.VISIBLE
                        binding.errors.text = e.message
                    }
                }
            } else {
                setWarningInputOutlines()
            }
        }
    }

    private fun setWarningInputOutlines () {
        if (email == "") {
            binding.userEmail.setBackgroundResource(R.drawable.warning_outline)
        }
        if (password == ""){
            binding.userPassword.setBackgroundResource(R.drawable.warning_outline)
        }
    }

    private fun setInputListeners () {
        binding.userEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                email = s.toString()
            }
        })
        binding.userPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                password = s.toString()
            }
        })
    }

    private fun setNavLinks () {
        binding.newAccountButton.setOnClickListener {
            findNavController().navigate(
                R.id.signinFragment,
                bundleOf("email" to email, "password" to password)
            )
        }
        binding.forgottenPasswordButton.setOnClickListener {
            findNavController().navigate(
                R.id.forgottenPassword,
                bundleOf("email" to email)
            )
        }
    }
}