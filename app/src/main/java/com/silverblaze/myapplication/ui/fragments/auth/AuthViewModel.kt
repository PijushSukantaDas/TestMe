package com.silverblaze.myapplication.ui.fragments.auth

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silverblaze.myapplication.data.models.Login
import com.silverblaze.myapplication.data.models.NewUser
import com.silverblaze.myapplication.data.models.SingUp
import com.silverblaze.myapplication.domain.AuthUseCase
import com.silverblaze.myapplication.utils.Event
import com.silverblaze.myapplication.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel()  {

    var name = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var mobile = MutableLiveData<String>()
    var isMale = MutableLiveData<Boolean>()
    var isFemale = MutableLiveData<Boolean>()
    var latitude = MutableLiveData<String>()
    var longitude = MutableLiveData<String>()
    var imagePath = MutableLiveData<String>()
    var uri : Uri? = null

    var propertyImagePart: MultipartBody.Part? = null

    private val _signUp = MutableStateFlow<Resource<SingUp>>(Resource.loading(null))
    val signUp : StateFlow<Resource<SingUp>> = _signUp

    private val _newUser= MutableStateFlow<Resource<NewUser>>(Resource.loading(null))
    val newUser : StateFlow<Resource<NewUser>> = _newUser

    private val _login = MutableStateFlow<Resource<Login>>(Resource.loading(null))
    val login : StateFlow<Resource<Login>> = _login

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>>
        get() = _errorMessage


    fun validation() : Boolean {
        return when{
            name.value.isNullOrEmpty()->{
                _errorMessage.value = Event("Name is Required")
                true
            }
            email.value.isNullOrEmpty()->{
                _errorMessage.value = Event("Email is Required")
                true
            }
            password.value.isNullOrEmpty()->{
                _errorMessage.value = Event("Password is Required")
                true
            }
            mobile.value.isNullOrEmpty() || (mobile.value.toString().length<11) ->{
                _errorMessage.value = Event("Enter Valid Mobile Number")
                true
            }
            else->{
                true
            }
        }

    }

    fun loginValidation() : Boolean {
        return when{
            email.value.isNullOrEmpty()->{
                _errorMessage.value = Event("Email is Required")
                true
            }
            password.value.isNullOrEmpty()->{
                _errorMessage.value = Event("Password is Required")
                true
            }
            else->{
                true
            }
        }

    }



    fun signUp(file : File) {
        viewModelScope.launch {
            authUseCase.signUp(
                name.value?:"",
                email.value?:"",
                mobile.value?:"",
                latitude.value?:"",
                longitude.value?:"",
                password.value?:"",
                file
            ).catch { e->
                _signUp.value = Resource.error(null,e.toString())
            }.collect {
                _signUp.value = it
            }
        }
    }

    fun addNewUser(file : File) {
        viewModelScope.launch {
            authUseCase.addNewUser(
                name.value?:"",
                email.value?:"",
                mobile.value?:"",
                latitude.value?:"",
                longitude.value?:"",
                password.value?:"",
                file
            ).catch { e->
                _newUser.value = Resource.error(null,e.toString())
            }.collect {
                _newUser.value = it
            }
        }
    }


    fun login(){
        viewModelScope.launch {
            authUseCase.login(
                email.value?:"",
                password.value?:""
            ).catch {e->
                _login.value = Resource.error(null,e.toString())
            }.collect {
                _login.value = it
            }
        }
    }


}


