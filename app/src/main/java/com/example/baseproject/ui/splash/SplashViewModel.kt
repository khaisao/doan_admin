package com.example.baseproject.ui.splash

import android.os.Handler
import android.os.Looper
import com.example.core.base.BaseViewModel
import com.example.core.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : BaseViewModel() {

    val actionSPlash = SingleLiveEvent<SplashActionState>()

    private val handler = Handler(Looper.getMainLooper())

    init {
        handler.postDelayed({
            actionSPlash.value = SplashActionState.Finish
        }, 1000)
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(null)
        super.onCleared()
    }
}

sealed class SplashActionState {
    object Finish : SplashActionState()
}