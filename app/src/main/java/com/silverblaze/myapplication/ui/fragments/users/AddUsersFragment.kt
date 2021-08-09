package com.silverblaze.myapplication.ui.fragments.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.silverblaze.myapplication.R
import com.silverblaze.myapplication.databinding.FragmentAddUsersBinding
import com.silverblaze.myapplication.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddUsersFragment : Fragment() {
    lateinit var binding : FragmentAddUsersBinding
    private val viewModel : UsersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddUsersBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveBtn.setOnClickListener {
            if(viewModel.validation()){

            }else{
               showErrorMessage()
            }

        }
    }

    private fun showErrorMessage() {
        viewModel.errorMessage.observe(viewLifecycleOwner,EventObserver{
            Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
        })
    }

}