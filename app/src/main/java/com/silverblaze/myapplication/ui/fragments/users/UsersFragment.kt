package com.silverblaze.myapplication.ui.fragments.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.silverblaze.myapplication.R
import com.silverblaze.myapplication.data.models.User
import com.silverblaze.myapplication.databinding.DialogDeleteLayoutBinding
import com.silverblaze.myapplication.databinding.FragmentUsersBinding
import com.silverblaze.myapplication.ui.fragments.users.adapter.UserListener
import com.silverblaze.myapplication.ui.fragments.users.adapter.UsersAdapter
import com.silverblaze.myapplication.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class UsersFragment : Fragment(), UserListener {

    lateinit var binding : FragmentUsersBinding
    private lateinit var adapter : UsersAdapter
    private val viewModel : UsersViewModel by viewModels()



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

        binding.search.addTextChangedListener {
            if(viewModel.list.value?.size?:0 >0){
                adapter = UsersAdapter(viewModel.getSearchList(it),this)
                binding.recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }

    }

    private fun setUserList() {
        lifecycleScope.launchWhenStarted {
            viewModel.users.collect {

                when(it.status){
                    Status.SUCCESS->{
                        var response = it.data
                        viewModel.list.value = response?.response?.user_list?: listOf()
                        adapter = UsersAdapter(response?.response?.user_list?: listOf(),this@UsersFragment)
                        binding.recyclerView.adapter = adapter
                        adapter.notifyDataSetChanged()

                        binding.totalUsersTv.setText(response?.response?.user_list?.size?.toString())
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
        adapter = UsersAdapter(arrayListOf(),this)
        binding.recyclerView.adapter = adapter
    }

    override fun deleteUser(user: User, position : Int) {
        val dialog = MaterialDialog(requireContext())

        val dialogBinding  = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.dialog_delete_layout,
            null,
            false
        ) as  DialogDeleteLayoutBinding

        dialogBinding.viewModel = viewModel
        dialog.setContentView(dialogBinding.root)

        dialog.show()

        dialogBinding.cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.deleteBtn.setOnClickListener {

            viewModel.deleteUser(user.id)

            viewModel.deleteUser.observe(viewLifecycleOwner, Observer {
                val response = it.body()
                if(response?.meta?.status == 200){
                    dialog.dismiss()
                    findNavController().navigate(R.id.usersFragment)
                    Toast.makeText(requireContext(),response.response.message,Toast.LENGTH_SHORT).show()
                }else{
                    dialog.dismiss()
                    Toast.makeText(requireContext(),response?.response?.message,Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


}