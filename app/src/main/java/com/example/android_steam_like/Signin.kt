package com.example.android_steam_like

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android_steam_like.databinding.SigninFragmentBinding
import com.example.android_steam_like.entities.User
import com.example.android_steam_like.entities.UserSignupBody
import com.example.android_steam_like.utils.CustomSteamAPI
import com.example.android_steam_like.utils.GenericAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SigninFragment: Fragment() {
    private lateinit var binding: SigninFragmentBinding
    private var email: String = ""
    private var password: String = ""

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
        binding = SigninFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDataFromIntent()
        setSigninListener()
    }

    private fun setDataFromIntent () {
        this.email = arguments?.getString("email").toString()
        this.password = arguments?.getString("password").toString()
        if (this.email != "") {
            binding.userEmail.setText(email, TextView.BufferType.EDITABLE)
        }
        if (this.password != "") {
            binding.userPassword.setText(password, TextView.BufferType.EDITABLE)
            binding.userPasswordVerification.setText(password, TextView.BufferType.EDITABLE)
        }
    }

    private fun signin (res: User) {
        User.setInstance(res)
        findNavController().navigate(R.id.home2)
    }

    private fun setSigninListener () {
        binding.signinButton.setOnClickListener {
            val username = binding.username.text.toString()
            email = binding.userEmail.text.toString()
            password = binding.userPassword.text.toString()
            val passwordVerif = binding.userPasswordVerification.text.toString()

            if (username != "" && email != "" && password != "" && password == passwordVerif) {
                val userCredentials = UserSignupBody(username, email, password)
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        GenericAPI.call(CustomSteamAPI.NetworkRequest::signup, userCredentials, ::signin)
                        binding.errors.visibility = View.GONE
                    }catch (e: Exception){
                        binding.errors.visibility = View.VISIBLE
                        binding.errors.text = e.message
                    }
                }
            }
        }
    }
}