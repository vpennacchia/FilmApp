package com.example.filmapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmapp.dataFirebase.Injection
import com.example.filmapp.dataFirebase.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import com.example.filmapp.dataFirebase.Result
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest

class AuthViewModel : ViewModel() {
    private val userRepository: UserRepository
    private val firebaseAuth = FirebaseAuth.getInstance()

    private val _currentUser = MutableLiveData<FirebaseUser?>()
    val currentUser: LiveData<FirebaseUser?> get() = _currentUser

    private val _authResult = MutableLiveData<Result<Boolean>>()
    val authResult: LiveData<Result<Boolean>> get() = _authResult

    init {
        userRepository = UserRepository(
            firebaseAuth,
            Injection.instance()
        )

        _currentUser.value = firebaseAuth.currentUser
    }

    fun signUp(email: String, password: String, firstName: String, lastName: String) {
        viewModelScope.launch {
            val result = userRepository.signUp(email, password, firstName, lastName)
            _authResult.postValue(result)

            val user = FirebaseAuth.getInstance().currentUser
            user?.let {
                val profileUpdates = userProfileChangeRequest {
                    displayName = "$firstName $lastName"
                }
                it.updateProfile(profileUpdates).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("AuthViewModel", "Profilo aggiornato con successo")
                    } else {
                        Log.e("AuthViewModel", "Errore nell'aggiornamento del profilo")
                    }
                }
            }

        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authResult.value = userRepository.login(email, password)
            _currentUser.postValue(firebaseAuth.currentUser)  // Aggiorna l'utente autenticato
        }
    }
}
