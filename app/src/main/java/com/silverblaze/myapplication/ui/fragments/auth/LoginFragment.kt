package com.silverblaze.myapplication.ui.fragments.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.silverblaze.myapplication.R
import com.silverblaze.myapplication.data.models.Login
import com.silverblaze.myapplication.databinding.FragmentLoginBinding
import com.silverblaze.myapplication.ui.activity.AppActivity
import com.silverblaze.myapplication.utils.EventObserver
import com.silverblaze.myapplication.utils.Preference
import com.silverblaze.myapplication.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    lateinit var binding : FragmentLoginBinding
    private val viewModel : AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.signingOption)
            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.signingOption)
        }

        binding.goToSignUp.setOnClickListener {
            findNavController().navigate(R.id.registrationFragment)
        }

        binding.loginBtn.setOnClickListener {
            setData()
            if(viewModel.loginValidation()){
                viewModel.login()
                login()
            }else{
                showErrorMessage()
            }
        }
    }

    private fun login() {
        lifecycleScope.launch {
            viewModel.login.collect {
                when(it.status){
                    Status.SUCCESS->{
                        val response = it.data?.meta?.status?:0
                        if (response == 200){
                            storeLoginInfo(it.data)

                        }else if (response == 100){
                            Toast.makeText(requireContext(),it.data?.response?.message?:"${it.message}",Toast.LENGTH_SHORT).show()
                        }

                    }
                    Status.LOADING->{
                        Toast.makeText(requireContext(),"Loading",Toast.LENGTH_SHORT).show()
                    }
                    Status.ERROR->{
                        val response = it.data?.meta?.status?:0
                        if (response != 200) {
                            Toast.makeText(requireContext(), it.data?.response?.message?:"${it.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun storeLoginInfo(data: Login?) {
        data?.let {
            Preference(requireContext()).addString("id", it.response.user.id.toString())
            Preference(requireContext()).addString("email", it.response.user.email)
            Preference(requireContext()).addString("password", it.response.user.password)
            Preference(requireContext()).addString("image", it.response.user.profile_image)
            Preference(requireContext()).addString("name", it.response.user.name)
            Preference(requireContext()).addString("phone", it.response.user.phone)
            Preference(requireContext()).addString("latitude", it.response.user.latitude)
            Preference(requireContext()).addString("longitude", it.response.user.longitude)
            Preference(requireContext()).addString("created", it.response.user.created_at)

            val intent = Intent(requireContext(), AppActivity::class.java)
            intent.putExtra("id",it.response.user.id)

            startActivity(intent)


        }
    }

    private fun showErrorMessage() {
        viewModel.errorMessage.observe(viewLifecycleOwner, EventObserver{
            Toast.makeText(requireContext(),it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun setData() {
        viewModel.email.value = binding.emailField.text.toString()
        viewModel.password.value = binding.passwordField.text.toString()
    }

}