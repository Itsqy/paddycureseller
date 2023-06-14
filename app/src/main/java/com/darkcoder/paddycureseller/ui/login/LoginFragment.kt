package com.darkcoder.paddycureseller.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.darkcoder.paddycure.utils.Utils
import com.darkcoder.paddycureseller.utils.ViewModelFactory
import com.darkcoder.paddycureseller.MainActivity
import com.darkcoder.paddycureseller.R
import com.darkcoder.paddycureseller.data.viewmodel.LoginViewModel
import com.darkcoder.paddycureseller.databinding.FragmentLoginBinding
import com.darkcoder.paddycureseller.utils.UserPreferences


val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding
    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory(UserPreferences.getInstance(requireContext().dataStore))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.layoutLogin?.let { Utils().setupUI(it, requireActivity()) }
        val welcome = "Welcome to " + "<br>" + " Paddy<font color = #ECF87F>Cure</font>"
        showToast()
        binding?.apply {
            loginViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                if (isLoading) {
                    loadingLottie?.visibility = View.VISIBLE
                    bgLoading?.visibility = View.VISIBLE
                } else {
                    loadingLottie?.visibility = View.INVISIBLE
                    bgLoading?.visibility = View.INVISIBLE
                }
            }
            tvWelcome?.text = Html.fromHtml(welcome)
            btnLogin.setOnClickListener {
                loginViewModel.login(
                    edtEmailLogin.text.toString(),
                    edtPassLogin.text.toString()
                )
            }
            tvToRegister?.setOnClickListener {
                view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            edtPassLogin.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    setUpCustomView()
                }

                override fun afterTextChanged(s: Editable?) {
                    setUpCustomView()
                }
            })
            edtEmailLogin.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    setUpCustomView()
                }

                override fun afterTextChanged(s: Editable?) {
                    setUpCustomView()

                }
            })
        }

    }


    private fun showToast() {
        loginViewModel.showStatus.observe(requireActivity()) { status ->
            if (status == true) {
                loginViewModel.showMessage.observe(requireActivity()) { msg ->
                    Toast.makeText(requireContext(), "$msg successfully login", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    requireActivity().finish()
                }
            } else {
                loginViewModel.showMessage.observe(requireActivity()) { msg ->
                    Toast.makeText(requireContext(), "$msg", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }

    private fun setUpCustomView() {
        binding?.apply {
            val pass = edtPassLogin.text.toString()
            val email = edtEmailLogin.text.toString().trim()
            Log.d("edtPass", email)
            btnLogin.isEnabled =
                pass != null && email != null && pass.length >= 8 && Patterns.EMAIL_ADDRESS.matcher(
                    email
                ).matches()
        }
    }

}