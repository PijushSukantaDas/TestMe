package com.silverblaze.myapplication.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.silverblaze.myapplication.R
import com.silverblaze.myapplication.ui.activity.AppActivity
import com.silverblaze.myapplication.utils.Preference
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val background = object : Thread(){
            override fun run() {
                try{
                    sleep(2000)
                    val email = Preference(requireContext()).getString("email")
                    val password = Preference(requireContext()).getString("password")

                    if(!email.isNullOrEmpty() && !password.isNullOrEmpty()){
                        startActivity(Intent(requireContext(), AppActivity::class.java))
                    }else{
                        findNavController().navigate(R.id.action_splashFragment_to_signingOption)
                    }

                }catch (e : Exception){
                    e.printStackTrace()
                }
            }

        }

        background.start()
    }
}