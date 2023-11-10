package com.example.core.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.R
import com.example.core.model.network.BaseResponse
import com.example.core.model.network.ErrorResponse
import com.example.core.utils.SingleLiveEvent
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineExceptionHandler
import okhttp3.ResponseBody
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
open class ErrorNetworkResponse(
    var status: Int?,
    var message: String?
)
abstract class BaseViewModel : ViewModel() {

    var messageError = SingleLiveEvent<Any>()
    var isLoading = SingleLiveEvent<Boolean>()

    val handler = CoroutineExceptionHandler { asas, throwable ->
        onError(throwable)
    }

    fun onError(
        throwable: Throwable,
        cb: ((result: ErrorNetworkResponse) -> Unit?)? = null
    ) {
        if (false) {
            messageError.postValue(R.string.not_connected_internet)
        } else if (throwable is HttpException) {
            var errorBody: String? = null
            try {
                errorBody = throwable.response()!!.errorBody()!!.string()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            var errorNetworkResponse: ErrorNetworkResponse? = null
            try {
                errorNetworkResponse =
                    Gson().fromJson(
                        errorBody,
                        ErrorNetworkResponse::class.java
                    )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (errorNetworkResponse != null) {
                if (errorNetworkResponse.status == 401) {
                    cb?.invoke(errorNetworkResponse)
                } else {
                    messageError.postValue(
                        errorNetworkResponse.message ?: "${errorNetworkResponse.status} "
                    )
                }
            } else {
                messageError.postValue(throwable.message)
            }
        } else {
            messageError.postValue("Some thing went wrong")
        }

    }

    inline fun <reified T : BaseResponse> handleError(
        responseBody: ResponseBody
    ): T? {
        var errorBody = ""
        try {
            errorBody = responseBody.string()
        } catch (e: Exception) {
            e.printStackTrace()
        }
//        val typeResponse =
//            object : TypeToken<T>() {}.type
        var response: T? = null
        try {
            response =
                Gson().fromJson(
                    errorBody,
                    T::class.java
                )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response

    }

}