package com.khaipv.attendance.notification

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.*
import com.example.core.pref.RxPreferences
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.khaipv.attendance.R
import com.khaipv.attendance.container.MainActivity
import com.khaipv.attendance.util.BundleKey
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AppFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var rxPreferences: RxPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.tag(TAG).d("FCM data: ${remoteMessage.data}")

        showNotification(this, remoteMessage)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(context: Context, remoteMessage: RemoteMessage) {
        // notify channel
        val channelId = getString(R.string.default_notification_channel_id)

        // notify sound
        val defaultSoundUri = RingtoneManager.getDefaultUri(
            RingtoneManager.TYPE_NOTIFICATION
        )

        val coursePerCycleId = remoteMessage.data["coursePerCycleId"]
        val className = remoteMessage.data["className"]

        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(BundleKey.COURSE_PER_CYCLE_ID, coursePerCycleId)

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder =
            NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText("You didn't checkin: $className")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        notificationBuilder.setVibrate(longArrayOf(100, 100))

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        val channel = NotificationChannel(
            channelId,
            resources.getString(R.string.app_name),
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.setShowBadge(true)
        notificationManager.createNotificationChannel(channel)
        notificationBuilder.priority = NotificationManager.IMPORTANCE_HIGH
        notificationBuilder.setChannelId(channelId)

        if (!rxPreferences.getUserName().isNullOrEmpty() && !rxPreferences.getPassword()
                .isNullOrEmpty() && rxPreferences.getRole() == 1
        ) {
            notificationManager.notify(
                System.currentTimeMillis().toInt(),
                notificationBuilder.build()
            )
        }
    }


    companion object {
        private const val TAG = "AppFirebaseMessagingService"
    }
}
