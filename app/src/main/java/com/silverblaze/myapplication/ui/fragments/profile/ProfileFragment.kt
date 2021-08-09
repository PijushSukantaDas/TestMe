package com.silverblaze.myapplication.ui.fragments.profile

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
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.silverblaze.myapplication.R
import com.silverblaze.myapplication.data.models.Profile
import com.silverblaze.myapplication.databinding.FragmentProfileBinding
import com.silverblaze.myapplication.utils.Preference
import com.silverblaze.myapplication.utils.Status
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    lateinit var binding : FragmentProfileBinding
    private val viewModel : ProfileViewModel by viewModels()


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
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val intent = Intent().extras

        var profileId = Preference(requireContext()).getString("id")

        getProfile(profileId)

        Toast.makeText(requireContext(),profileId,Toast.LENGTH_SHORT).show()


        viewModel.profileData.observe(viewLifecycleOwner, Observer {
            val response = it.body()
            if(response?.meta?.status == 200){
                binding.name.setText(response.response.user_data.name)
                Toast.makeText(requireContext(),response.response.user_data.name,Toast.LENGTH_SHORT).show()
                setProfileDetails(response)
            }else{

            }
        })

    }

    private fun getProfile(profileId: String?) {
        viewModel.profile(Integer.parseInt(profileId))
    }


    private fun setProfileDetails(data: Profile?) {
        data?.let {
            CoroutineScope(Dispatchers.Main).launch {
                Preference(requireContext()).addString("id", it.response.user_data.id.toString())
                Preference(requireContext()).addString("email", it.response.user_data.email)
                Preference(requireContext()).addString("password", it.response.user_data.password)
                Preference(requireContext()).addString("image", it.response.user_data.profile_image)
                Preference(requireContext()).addString("name", it.response.user_data.name)
                Preference(requireContext()).addString("phone", it.response.user_data.phone)
                Preference(requireContext()).addString("latitude", it.response.user_data.latitude)
                Preference(requireContext()).addString("longitude", it.response.user_data.longitude)
                Preference(requireContext()).addString("created", it.response.user_data.created_at)

                viewModel.name.value = it.response.user_data.name
                viewModel.mobile.value = it.response.user_data.phone
                viewModel.email.value = it.response.user_data.email
                viewModel.password.value = it.response.user_data.password
                viewModel.created.value = it.response.user_data.created_at
                viewModel.image.value = it.response.user_data.profile_image

                Picasso.get()
                    .load(it.response.user_data.profile_image)
                    .fit()
                    .centerCrop()
                    .into(binding.profileImage)


            }


        }
    }


}