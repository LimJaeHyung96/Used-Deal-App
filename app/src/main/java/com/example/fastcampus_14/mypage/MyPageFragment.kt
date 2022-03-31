package com.example.fastcampus_14.mypage

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.fastcampus_14.R
import com.example.fastcampus_14.databinding.FragmentMyPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MyPageFragment : Fragment(R.layout.fragment_my_page) {

    private var binding: FragmentMyPageBinding? = null
    private val auth : FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentMyPageBinding = FragmentMyPageBinding.bind(view)
        binding = fragmentMyPageBinding

        fragmentMyPageBinding.signInOutButton.setOnClickListener {
            activity?.let{ activity ->
                binding?.let { binding ->
                    val email = binding.emailEditText.text.toString()
                    val password = binding.passwordEditText.text.toString()

                    if (auth.currentUser == null) {
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(activity) { task ->
                                if(task.isSuccessful){
                                    successSignIn()
                                } else {
                                    Toast.makeText(context,"이메일 또는 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        auth.signOut()
                        binding.emailEditText.text.clear()
                        binding.emailEditText.isEnabled = true
                        binding.passwordEditText.text.clear()
                        binding.passwordEditText.isEnabled = true

                        binding.signInOutButton.text = "로그인"
                        binding.signInOutButton.isEnabled = false
                        binding.signUpButton.isEnabled = false
                    }
                }
            }
        }

        fragmentMyPageBinding.signUpButton.setOnClickListener {
            binding?.let { binding ->
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()){
                        if(it.isSuccessful){
                            Toast.makeText(context,"회원가입에 성공했습니다. 로그인 버튼을 눌러 이용해주세요.",Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context,"회원가입에 실패했습니다.",Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        fragmentMyPageBinding.emailEditText.addTextChangedListener {
            binding?.let { binding ->
                val enable = binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
                binding.signInOutButton.isEnabled = enable
                binding.signUpButton.isEnabled = enable
            }
        }

        fragmentMyPageBinding.passwordEditText.addTextChangedListener {
            binding?.let { binding ->
                val enable = binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
                binding.signInOutButton.isEnabled = enable
                binding.signUpButton.isEnabled = enable
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if(auth.currentUser == null) {
            binding?.let { binding ->
                binding.emailEditText.text.clear()
                binding.emailEditText.isEnabled = true
                binding.passwordEditText.text.clear()
                binding.passwordEditText.isEnabled = true
                binding.signInOutButton.text = "로그인"
                binding.signInOutButton.isEnabled = false
                binding.signUpButton.isEnabled = false
            }
        } else {
            binding?.let { binding ->
                binding.emailEditText.setText(auth.currentUser!!.email)
                binding.emailEditText.isEnabled = false
                binding.passwordEditText.setText("********")
                binding.passwordEditText.isEnabled = false
                binding.signInOutButton.text = "로그아웃"
                binding.signInOutButton.isEnabled = true
                binding.signUpButton.isEnabled = false
            }
        }
    }

    private fun successSignIn() {
        if(auth.currentUser == null){
            Toast.makeText(context,"로그인에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        binding?.let { binding ->
            binding.emailEditText.isEnabled = false
            binding.passwordEditText.isEnabled = false
            binding.signUpButton.isEnabled = false
            binding.signInOutButton.text = "로그아웃"
        }
    }
}