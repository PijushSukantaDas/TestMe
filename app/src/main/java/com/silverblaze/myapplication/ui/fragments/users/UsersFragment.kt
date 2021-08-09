package com.silverblaze.myapplication.ui.fragments.users

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.silverblaze.myapplication.R
import com.silverblaze.myapplication.databinding.FragmentUsersBinding
import com.silverblaze.myapplication.ui.fragments.users.adapter.UsersAdapter
import com.silverblaze.myapplication.utils.Resource
import com.silverblaze.myapplication.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class UsersFragment : Fragment() {

    lateinit var binding : FragmentUsersBinding
    private lateinit var adapter : UsersAdapter
    private val viewModel : UsersViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUsersBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        viewModel.usersList()
        setUserList()

        binding.addUserBtn.setOnClickListener {
            findNavController().navigate(R.id.addUsersFragment)
        }

    }

    private fun setUserList() {
        lifecycleScope.launchWhenStarted {
            viewModel.users.collect {

                when(it.status){
                    Status.SUCCESS->{
                        var response = it.data
                        adapter = UsersAdapter(response?.response?.user_list?: listOf())
                        binding.recyclerView.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }
                    Status.LOADING->{
                        Toast.makeText(requireContext(),"Loading",Toast.LENGTH_SHORT).show()
                    }
                    Status.ERROR->{
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun initAdapter() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = UsersAdapter(arrayListOf())
        binding.recyclerView.adapter = adapter
    }


}