package com.darkcoder.paddycureseller.ui.register

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.darkcoder.paddycure.utils.Utils
import com.darkcoder.paddycureseller.R
import com.darkcoder.paddycureseller.data.viewmodel.RegisterViewModel
import com.darkcoder.paddycureseller.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding?.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding?.apply {
            layoutRegister?.let { Utils().setupUI(it, requireActivity()) }
            registerViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                if (isLoading) {
                    loadingLottie?.visibility = View.VISIBLE
                    bgLoading?.visibility = View.VISIBLE
                } else {
                    loadingLottie?.visibility = View.INVISIBLE
                    bgLoading?.visibility = View.INVISIBLE
                }
            }
            val join = "Let's Join With" +
                    "<br>" + " Paddy<font color = #ECF87F>Cure</font>"
            tvWelcome?.text = Html.fromHtml(join)
            tvToLogin.setOnClickListener {
                view.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
            edtPassRegister.addTextChangedListener(object : TextWatcher {
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
            edtEmailRegister.addTextChangedListener(object : TextWatcher {
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
            btnRegister.setOnClickListener {
                registerViewModel.register(
                    edtUsername.text.toString(),
                    edtEmailRegister.text.toString(),
                    edtPassRegister.text.toString()
                )
            }


        }

        showToast()


    }

    private fun showToast() {
        registerViewModel.showStatus.observe(requireActivity()) { status ->
            if (status == true) {
                registerViewModel.showMessage.observe(requireActivity()) { msg ->
                    Toast.makeText(requireContext(), "$msg", Toast.LENGTH_SHORT).show()
                    val modal = AlertDialog.Builder(requireContext())
                    modal.setTitle("Login Berhasil")
                    modal.setMessage("$msg berhasil\nmendaftar silahkan untuk login ")
                    modal.setPositiveButton("login") { dialog: DialogInterface, which: Int ->
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }

                    val dialog: AlertDialog = modal.create()
                    dialog.show()
                }
            } else {
                registerViewModel.showMessage.observe(requireActivity()) { msg ->
                    Toast.makeText(requireContext(), "$msg", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun setUpCustomView() {
        binding?.apply {
            val pass = edtPassRegister.text.toString()
            val email = edtEmailRegister.text.toString().trim()
            Log.d("edtPass", email)
            btnRegister.isEnabled =
                pass != null && email != null && pass.length >= 8 && Patterns.EMAIL_ADDRESS.matcher(
                    email
                ).matches()
        }
    }

}