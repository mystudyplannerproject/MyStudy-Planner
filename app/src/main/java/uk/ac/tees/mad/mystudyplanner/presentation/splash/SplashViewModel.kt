package uk.ac.tees.mad.mystudyplanner.presentation.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private val _destination = MutableStateFlow<SplashDestination?>(null)
    val destination: StateFlow<SplashDestination?> = _destination

    init {
        initializeApp()
    }

    private fun initializeApp() {
        viewModelScope.launch {

            delay(2000)
            val user = FirebaseAuth.getInstance().currentUser

            _destination.value =
                if (user != null) SplashDestination.Home
                else SplashDestination.Auth
        }
    }
}

enum class SplashDestination {
    Home,
    Auth
}