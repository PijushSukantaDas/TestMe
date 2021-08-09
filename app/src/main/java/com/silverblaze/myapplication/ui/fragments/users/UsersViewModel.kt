package com.silverblaze.myapplication.ui.fragments.users

import androidx.lifecycle.*
import com.silverblaze.myapplication.data.models.Delete
import com.silverblaze.myapplication.data.models.Profile
import com.silverblaze.myapplication.data.models.User
import com.silverblaze.myapplication.data.models.Users
import com.silverblaze.myapplication.domain.ProfileUseCase
import com.silverblaze.myapplication.utils.Event
import com.silverblaze.myapplication.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(private val profileUseCase: ProfileUseCase): ViewModel(){
    var name = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var mobile = MutableLiveData<String>()
    var isMale = MutableLiveData<Boolean>()
    var isFemale = MutableLiveData<Boolean>()
    var latitude = MutableLiveData<String>()
    var longitude = MutableLiveData<String>()

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>>
        get() = _errorMessage

    private val _users = MutableStateFlow<Resource<Users>>(Resource.loading(null))
    val users : StateFlow<Resource<Users>>  = _users

    private val _deleteUser = MutableLiveData<Response<Delete>>()
    val deleteUser : LiveData<Response<Delete>> = _deleteUser

    fun usersList() {
        viewModelScope.launch {
           profileUseCase.getUserList().catch { e->
               _users.value = Resource.error(null,e.toString())
           }.collect {
               _users.value = it
           }
        }
    }

    fun validation() : Boolean{
        when{
            name.value.isNullOrEmpty() ->{
                Event("Name is Required")
                return false
            }
            email.value.isNullOrEmpty()->{
                Event("Email is Required")
                return false
            }
            password.value.isNullOrEmpty()->{
                Event("Password is Required")
                return false
            }
            mobile.value.isNullOrEmpty() || (mobile.value.toString().length<11) ->{
                Event("Enter Valid Mobile Number")
                return false
            }
            else->{
                return true
            }
        }

        return false
    }

    fun deleteUser(id : Int) {
        viewModelScope.launch(Dispatchers.Main){
            _deleteUser.value = profileUseCase.delete(id)
        }
    }

}