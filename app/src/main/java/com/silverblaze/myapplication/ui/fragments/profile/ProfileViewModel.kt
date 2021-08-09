package com.silverblaze.myapplication.ui.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silverblaze.myapplication.data.models.Login
import com.silverblaze.myapplication.data.models.Profile
import com.silverblaze.myapplication.domain.ProfileUseCase
import com.silverblaze.myapplication.utils.Resource
import com.squareup.picasso.Picasso
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase
) : ViewModel() {

    var name = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var mobile = MutableLiveData<String>()
    var latitude = MutableLiveData<String>()
    var longitude = MutableLiveData<String>()
    var image = MutableLiveData<String>()
    var created = MutableLiveData<String>()

    private val _profileData = MutableLiveData<Response<Profile>>()
    val profileData : LiveData<Response<Profile>> = _profileData

//    private val _profile = MutableStateFlow<Resource<Profile>>(Resource.loading(null))
//    val profile : StateFlow<Profile> = _profile


    fun profile(id : Int) {
        viewModelScope.launch(Dispatchers.Main){
            _profileData.value = profileUseCase.profile(id)
        }
    }

    fun created(): Date? {
        if(!created.value.isNullOrEmpty()){
            val format = "dd/MM/yy hh:mm aaa"
            val simpleDateFormat = SimpleDateFormat(format)
            val date = simpleDateFormat.parse(created.value)

            return  date
        }
        return null
    }


}