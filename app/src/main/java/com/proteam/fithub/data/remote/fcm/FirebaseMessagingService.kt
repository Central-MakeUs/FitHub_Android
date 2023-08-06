package com.proteam.fithub.data.remote.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.proteam.fithub.R
import com.proteam.fithub.presentation.ui.FitHub
import com.proteam.fithub.presentation.ui.main.MainActivity
import com.proteam.fithub.presentation.ui.splash.SplashActivity

open class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val title = message.data["title"]
        val body = message.data["body"]
        sendNoti(title, body, message.data)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("----", "onNewToken: $token")
    }

    private fun sendNoti(title: String?, text: String?, data: Map<String, String>) {
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val intent = Intent(this, if((this.application as FitHub).isForeground) MainActivity::class.java else SplashActivity::class.java)
        intent.putExtra("View", data.getValue("targetView"))
        intent.putExtra("PK", data.getValue("targetPK"))
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setAutoCancel(true)
            .setSound(defaultSound)
            .setContentText(text)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_logo)
            .setLargeIcon(resources.getDrawable(R.drawable.ic_logo, null).toBitmap())
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH,
            )

            channel.enableLights(true)
            channel.enableVibration(true)

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                channel
            )
        }
    }

    companion object {
        private const val CHANNEL_NAME = "핏허브 알림"
        private const val CHANNEL_ID = "FITHUB_CHANNEL_NAME"

    }

}