package com.example.android_steam_like

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android_steam_like.databinding.ForgottenPasswordBinding
import com.example.android_steam_like.entities.RecPassword
import com.example.android_steam_like.entities.ResPassword
import com.example.android_steam_like.utils.CustomSteamAPI
import com.example.android_steam_like.utils.GenericAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.example.android_steam_like.entities.User
import org.json.JSONObject

class ForgottenPassword : Fragment() {
    private lateinit var binding: ForgottenPasswordBinding
    private var token: String = ""
    private var email: String = ""
    private var password: String = ""
    private var passwordVerification: String = ""

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ForgottenPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDataFromIntent()
        displayRequestTokenInputs()
        setTokenRequestListener()
        setResetPasswordListener()
    }

    private fun setToken (res: RecPassword) {
        binding.token.setText(res.token, TextView.BufferType.EDITABLE)
        displayPasswordResetInputs()
    }

    private fun setTokenRequestListener () {
        binding.requestTokenButton.setOnClickListener {
            email = binding.userEmail.text.toString()
            if (email != "") {
                GlobalScope.launch(Dispatchers.Main) {
                    GenericAPI.call(CustomSteamAPI.NetworkRequest::requestToken, email, ::setToken)
                }
            } else {
                binding.userEmail.setBackgroundResource(R.drawable.warning_outline)
            }
        }
    }

    private fun setResetPasswordListener () {
        binding.resetPasswordButton.setOnClickListener {
            password = binding.userPassword.text.toString()
            token = binding.token.text.toString()
            passwordVerification = binding.userPasswordVerification.text.toString()
            if (token != "" && password != "" && passwordVerification == password) {
                val body = ResPassword(token, password)
                GlobalScope.launch(Dispatchers.Main) {
                    GenericAPI.call(CustomSteamAPI.NetworkRequest::resetPassword, body, ::backToLogin)
                }
            } else {
                setWarningInputOutlinesForPasswordReset()
            }
        }
    }

    private fun setWarningInputOutlinesForPasswordReset () {
        if (token == "") {
            binding.token.setBackgroundResource(R.drawable.warning_outline)
        }
        if (password == ""){
            binding.userPassword.setBackgroundResource(R.drawable.warning_outline)
        }
        if (passwordVerification == ""){
            binding.userPasswordVerification.setBackgroundResource(R.drawable.warning_outline)
        }
    }

    private fun setDataFromIntent () {
        email = arguments?.getString("email").toString()
        if (email != "") {
            binding.userEmail.setText(email, TextView.BufferType.EDITABLE)
        }
    }

    private fun displayRequestTokenInputs () {
        binding.token.visibility = View.INVISIBLE
        binding.userPassword.visibility = View.INVISIBLE
        binding.userPasswordVerification.visibility = View.INVISIBLE
        binding.resetPasswordButton.visibility = View.INVISIBLE
    }

    private fun displayPasswordResetInputs () {
        binding.userEmail.visibility = View.INVISIBLE
        binding.requestTokenButton.visibility = View.INVISIBLE
        binding.token.visibility = View.VISIBLE
        binding.userPassword.visibility = View.VISIBLE
        binding.userPasswordVerification.visibility = View.VISIBLE
        binding.resetPasswordButton.visibility = View.VISIBLE
    }

    private fun backToLogin (res: Int) {
        if (res == 1) {
            findNavController().navigate(
                R.id.loginFragment
            )
        }
    }
}