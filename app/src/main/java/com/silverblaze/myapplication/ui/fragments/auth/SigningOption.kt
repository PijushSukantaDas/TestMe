package com.silverblaze.myapplication.ui.fragments.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.silverblaze.myapplication.R
import com.silverblaze.myapplication.databinding.FragmentSigningOptionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SigningOption : Fragment() {

    lateinit var binding : FragmentSigningOptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finishAffinity()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSigningOptionBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginBtn.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
        binding.registerBtn.setOnClickListener {
            findNavController().navigate(R.id.registrationFragment)
        }

    }

}