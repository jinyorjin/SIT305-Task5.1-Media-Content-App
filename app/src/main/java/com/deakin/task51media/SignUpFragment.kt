package com.deakin.task51media

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.deakin.task51media.data.AppDatabase
import com.deakin.task51media.data.User
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etFullName = view.findViewById<EditText>(R.id.etFullName)
        val etNewUsername = view.findViewById<EditText>(R.id.etNewUsername)
        val etNewPassword = view.findViewById<EditText>(R.id.etNewPassword)
        val etConfirmPassword = view.findViewById<EditText>(R.id.etConfirmPassword)
        val btnCreateAccount = view.findViewById<Button>(R.id.btnCreateAccount)
        val btnBackToLogin = view.findViewById<Button>(R.id.btnBackToLogin)

        val db = AppDatabase.getDatabase(requireContext())
        val userDao = db.userDao()

        btnCreateAccount.setOnClickListener {
            val fullName = etFullName.text.toString().trim()
            val username = etNewUsername.text.toString().trim()
            val password = etNewPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val existingUser = userDao.getUserByUsername(username)

                    if (existingUser != null) {
                        Toast.makeText(requireContext(), "Username already exists", Toast.LENGTH_SHORT).show()
                    } else {
                        userDao.insertUser(
                            User(
                                fullName = fullName,
                                username = username,
                                password = password
                            )
                        )

                        Toast.makeText(requireContext(), "Account created successfully", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.popBackStack()
                    }
                } catch (e: Exception) {
                    Log.e("SignUpFragment", "Signup error", e)
                    Toast.makeText(requireContext(), "Signup failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        btnBackToLogin.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}