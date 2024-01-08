package com.khaipv.attendance.container

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.core.base.activity.BaseActivityNotRequireViewModel
import com.example.core.network.connectivity.NetworkConnectionManager
import com.example.core.pref.RxPreferences
import com.example.core.utils.setLanguage
import com.example.core.utils.toastMessage
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.kbyai.facesdk.FaceSDK
import com.khaipv.attendance.R
import com.khaipv.attendance.databinding.ActivityMainBinding
import com.khaipv.attendance.navigation.AppNavigation
import com.khaipv.attendance.util.BundleKey
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseActivityNotRequireViewModel<ActivityMainBinding>() {

    @Inject
    lateinit var appNavigation: AppNavigation

    private val TAG = "MainActivity"

    @Inject
    lateinit var networkConnectionManager: NetworkConnectionManager

    @Inject
    lateinit var rxPreferences: RxPreferences

    override val layoutId = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var ret = FaceSDK.setActivation(
            "d0q76AKm3+kzUaoVX0WnW7l8YAApMbMGPC7t1qLYktAAPBiCZbWEZD8yICNh6fLbbvKby5nHtz4T\n" +
                    "uePOdFydIXqBJS+WNYhZra+qQxSmsvR8jF/zzjMVbFlZeDMd9zbO+jA5pXkk38GP5znVRWToBRrS\n" +
                    "Ui9vuyGjvgL/esu3hAqD9Wez/8gg+Qd8zo4sVhVzIfgip//kbM1GbhK7iCNTun3AZPn0GkdwGFQY\n" +
                    "NlBTeUDEhW5RPrDNU+/LctoSySEEM/hyZaqKRK4Jz3qaG5nXxwtAXvSPWA8mVC4wogIu8l8cl01k\n" +
                    "ldvdMzF20c+aR1l9KgygMdGQTQkJQc5caMqHlg=="
        )

        if (ret == FaceSDK.SDK_SUCCESS) {
            ret = FaceSDK.init(assets)
        } else {
            ret =
                FaceSDK.setActivation("A9/DTjBqzv4yozfPbEfCDmtCTJirR3E2TrXLaetUzAeCozb3H7cIIR4KcNOQZdHdw08sxPLwQzMH\njkLpuu5SNne+rVaBZA8LeOq84ZEXl1av+T29m2DVW0tiqlMoaMdVcbg7Xf8bsNTcMvMabTNMN/2+\nbfAt5qTWYKFYQq77hXpZS9UDgw/YCJB8jIMzQu1zglG+xce5ZjsWSc7ZI7rGkNh32956MeVSFAd5\nn9v+SoVnjslBvIR9XRcUEVu4rvwFFKbQyxxBNTl2A6trT/wspZQLdfrIEiaCtbjmOQP8aYTmd8v9\na3yIO5zlY3u2MM4IP1SGGbZD/LAbHYTyveUDZw==")
            if (ret == FaceSDK.SDK_SUCCESS) {
                ret = FaceSDK.init(assets)
            }
        }
        if (ret != FaceSDK.SDK_SUCCESS) {
            when (ret) {
                FaceSDK.SDK_LICENSE_KEY_ERROR -> {
                    toastMessage("Invalid license!")
                }

                FaceSDK.SDK_LICENSE_APPID_ERROR -> {
                    toastMessage("Invalid error!")
                }

                FaceSDK.SDK_LICENSE_EXPIRED -> {
                    toastMessage("License expired!")
                }

                FaceSDK.SDK_NO_ACTIVATED -> {
                    toastMessage("No activated!")
                }

                FaceSDK.SDK_INIT_ERROR -> {
                    toastMessage("Init error!")
                }
            }
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment
        appNavigation.bind(navHostFragment.navController)

        lifecycleScope.launch {
            val language = rxPreferences.getLanguage().first()
            language?.let { setLanguage(it) }
        }

        networkConnectionManager.isNetworkConnectedFlow
            .onEach {
                if (it) {
                    Timber.tag(TAG).d("onCreate: Network connected")
                } else {
                    Timber.tag(TAG).d("onCreate: Network disconnected")
                }
            }
            .launchIn(lifecycleScope)

        handlerCoursePerCycleId(intent, false)
    }

    override fun onStart() {
        super.onStart()
        networkConnectionManager.startListenNetworkState()
    }

    override fun onStop() {
        super.onStop()
        networkConnectionManager.stopListenNetworkState()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handlerCoursePerCycleId(intent, true)
    }

    private fun handlerCoursePerCycleId(intent: Intent?, isAppRunning: Boolean) {
        val coursePerCycleId = intent?.getStringExtra(BundleKey.COURSE_PER_CYCLE_ID)
        val bundle = Bundle()
        if (coursePerCycleId != null) {
            bundle.putInt(BundleKey.COURSE_PER_CYCLE_ID, coursePerCycleId.toInt())
            try {
                if (isAppRunning) {
                    appNavigation.openStudentTopToDetailScheduleStudent(bundle)
                } else {
                    appNavigation.openGlobalSplashToDetailAttendance(bundle)
                }
            } catch (e: Exception) {
                toastMessage("Something went wrong, try again")
            }
        }
    }
}