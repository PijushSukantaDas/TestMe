package com.silverblaze.myapplication.ui.fragments.auth

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.silverblaze.myapplication.R
import com.silverblaze.myapplication.data.models.SingUp
import com.silverblaze.myapplication.databinding.FragmentRegistrationBinding
import com.silverblaze.myapplication.ui.activity.AppActivity
import com.silverblaze.myapplication.utils.EventObserver
import com.silverblaze.myapplication.utils.Preference
import com.silverblaze.myapplication.utils.Status
import com.silverblaze.myapplication.utils.getFileName
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@AndroidEntryPoint
class RegistrationFragment : Fragment() {
    lateinit var binding : FragmentRegistrationBinding
    private val viewModel : AuthViewModel by viewModels()
    private var imageUri : Uri? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.signingOption)
            }
        })
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        getLatLong()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.signingOption)
        }

        binding.goToLogin.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

        binding.profileImage.setOnClickListener {
            permissionAccess()
            chooseImageGallery()
        }

        if(binding.male.isChecked){
            binding.female.isChecked = false
        }else if(binding.female.isChecked){
            binding.male.isChecked = false
        }

        binding.signUpBtn.setOnClickListener {
            if(viewModel.validation() && (binding.male.isChecked || binding.female.isChecked)){
                getLatLong()
                setData()
                registerUser()
            }else if(!binding.male.isChecked || !binding.female.isChecked){
                Toast.makeText(requireContext(),"Please Select Gender", Toast.LENGTH_SHORT).show()
            }
            else{
                showErrorMessage()
            }
        }
    }

    private fun setData() {
        viewModel.name.value = binding.nameField.text.toString()
        viewModel.email.value = binding.emailField.text.toString()
        viewModel.password.value = binding.passwordField.text.toString()
        viewModel.mobile.value = binding.mobileField.text.toString()
    }

    private fun registerUser() {
        viewModel.signUp(createFile())

        lifecycleScope.launch {
            viewModel.signUp.collect {
                when(it.status){
                    Status.SUCCESS->{
                        val response = it.data?.meta?.status?:0
                        if (response == 200){
                            Toast.makeText(requireContext(),"Registered",Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.loginFragment)
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
                            Log.i("Error Message", it.message.toString())
                        }
                    }
                }
            }
        }
    }



    private fun showErrorMessage() {
        viewModel.errorMessage.observe(viewLifecycleOwner, EventObserver{
            Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else{
                    Toast.makeText(requireContext(),"Permission denied", Toast.LENGTH_SHORT).show()
                    permissionAccess()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PERMISSION_CODE && resultCode == Activity.RESULT_OK){

            Log.i("imageUri","${data?.data}")
            imageUri = data?.data
//            dialogBinding.foodImageView.setImageURI(data?.data)
            setImage(imageUri)


            viewModel.imagePath.value = imageUri?.path
            Toast.makeText(requireContext(),imageUri?.path, Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(),"$resultCode", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun permissionAccess() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                // You can use the API that requires the permission.

            }
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA)->{

                }

            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)->{
                    getLatLong()
                }
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)->{

            }
            else -> {
                // You can directly ask for the permission.
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),
                    PERMISSION_CODE)
            }
        }
    }

    private fun getLatLong() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),PERMISSION_CODE)
        }else{
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    // Got last known location. In some rare situations this can be null.
                    viewModel.latitude.value = location?.latitude.toString()
                    viewModel.longitude.value = location?.longitude.toString()
                }
        }

    }

    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PERMISSION_CODE)
    }

    private fun setImage(imageUri: Uri?) {
        imageUri?.let {
            Picasso.get()
                .load(it)
                .fit()
                .centerCrop()
                .into(binding.profileImage)
        }

    }

    private fun createFile(): File {

        var parseFileDir = requireContext().contentResolver.openFileDescriptor(imageUri!!,"r",null)
        var file = File(requireContext().cacheDir,requireContext().contentResolver.getFileName(imageUri!!))
        val inputStream = FileInputStream(parseFileDir?.fileDescriptor)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        return file
    }

    companion object {
        private val IMAGE_CHOOSE = 1000;
        val PERMISSION_CODE = 1001;
    }

}