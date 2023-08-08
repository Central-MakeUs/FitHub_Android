package com.proteam.fithub.data.remote.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.proteam.fithub.R
import com.proteam.fithub.presentation.ui.FitHub
import com.proteam.fithub.presentation.ui.splash.SplashActivity
import java.net.URL

open class FirebaseMessagingService : FirebaseMessagingService() {

    private val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val title = message.data["title"]
        val body = message.data["body"]
        for((k, v) in message.data) {
            Log.e("----", "onMessageReceived: $k / $v", )
        }
        if(!(this.application as FitHub).isForeground) {
            sendNotificationWhenBackground(title, body, message.data)
        }
        /*when((this.application as FitHub).isForeground) {
            //true -> sendNotificationWhenForeground(title, body, message.data)
            else ->
        }*/
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("----", "onNewToken: $token")
    }

    private fun sendNotificationWhenForeground(title : String?, text : String?, data : Map<String, String>) {
        Log.e("----", "sendNotificationWhenForeground: ${(this.application as FitHub).isForeground}", )
        /*val intent = Intent(this, if(data["targetView"] == "ARTICLE") BoardDetailActivity::class.java else ExerciseCertificateDetailActivity::class.java).setType(data["targetPK"])
        val pendingIntent = PendingIntent.getActivity(this, (System.currentTimeMillis()).toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify((System.currentTimeMillis()).toInt(), notificationBuilder(title, text, data["targetImage"], pendingIntent).build()) */
    }

    private fun sendNotificationWhenBackground(title: String?, text: String?, data: Map<String, String>) {
        Log.e("----", "sendNotificationWhenBackground: ${(this.application as FitHub).isForeground}", )
        val intent = Intent(this, SplashActivity::class.java)
        intent.putExtra("AlarmPK", data.getValue("targetNotification"))
        intent.putExtra("View", data.getValue("targetView"))
        intent.putExtra("PK", data.getValue("targetPK"))
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, (System.currentTimeMillis()).toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify((System.currentTimeMillis()).toInt(), if(data["targetView"] == "RECORD") notificationBuilderWithPicture(title, text, data["targetImage"], pendingIntent).build() else notificationBuilderWithoutPicture(title, text, pendingIntent).build())
    }

    private fun notificationBuilderWithPicture(title : String?, content : String?, thumbnail : String?, pendingIntent : PendingIntent) : NotificationCompat.Builder {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setAutoCancel(true)
            .setSound(defaultSound)
            .setContentText(content)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentIntent(pendingIntent)
            .setLargeIcon(BitmapFactory.decodeStream(URL(thumbnail).openConnection().getInputStream()))
    }

    private fun notificationBuilderWithoutPicture(title : String?, content : String?, pendingIntent : PendingIntent) : NotificationCompat.Builder {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setAutoCancel(true)
            .setSound(defaultSound)
            .setContentText(content)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentIntent(pendingIntent)
    }

    companion object {
        private const val CHANNEL_NAME = "핏허브 알림"
        private const val CHANNEL_ID = "FITHUB_CHANNEL_NAME"

    }

}