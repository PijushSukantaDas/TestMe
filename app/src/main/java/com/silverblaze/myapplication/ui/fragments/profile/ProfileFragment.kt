package com.silverblaze.myapplication.ui.fragments.profile

import android.content.ContentValues.TAG
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
import com.here.sdk.core.GeoCoordinates

import com.here.sdk.mapviewlite.MapScene

import com.here.sdk.mapviewlite.MapStyle
import com.here.sdk.mapviewlite.MapMarkerImageStyle

import com.here.sdk.mapviewlite.MapMarker

import android.R

import com.here.sdk.mapviewlite.MapImageFactory

import com.here.sdk.mapviewlite.MapImage
import com.silverblaze.myapplication.utils.formatDateToString
import com.silverblaze.myapplication.utils.formatStringToDate
import java.text.SimpleDateFormat
import java.util.*


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


        binding.mapView.onCreate(savedInstanceState)
        loadMapScene()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val intent = Intent().extras

        var profileId = Preference(requireContext()).getString("id")

        getProfile(profileId)

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

    fun formatStringToDate(date : String): Date {
        val format = "dd/MM/yyyy hh:mm aaa"
        val simpleDateFormat = SimpleDateFormat(format)
        val dateString = simpleDateFormat.format(date)
        val date = simpleDateFormat.parse(date)

        return  date
    }

    private fun loadMapScene() {
        var lat = Preference(requireContext()).getString("latitude")
        var long = Preference(requireContext()).getString("longitude")
        // Load a scene from the SDK to render the map with a map style.
        val coordinates = GeoCoordinates(lat?.toDouble()?:00.0, long?.toDouble()?:0.0)

        binding.mapView.mapScene.loadScene(
            MapStyle.NORMAL_DAY,
            MapScene.LoadSceneCallback { errorCode ->
                if (errorCode == null) {

                    binding.mapView.getCamera().setTarget(coordinates)
                    binding.mapView.getCamera().setZoomLevel(15.0)

                } else {
                    Log.d(TAG, "onLoadScene failed: $errorCode")
                }
            })

    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }
}