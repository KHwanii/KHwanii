package com.example.myapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

import com.example.myapp.login.LoginActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FCMService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("토큰확인", "FCM Token : $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("메세지확인", "FCM Message : ${message.data}")
        Log.d("메세지확인", "FCM Notification: Title - ${message.notification?.title}, Body - ${message.notification?.body}")

        val intent = Intent(applicationContext, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.action = "Noiroze_Push_Notification"


        // Check if message contains a data payload.
        message.data.isNotEmpty().let {
            // Handle the message here
            // For this example, we'll just show a notification with the message content
            showNotification(message.notification?.title, message.notification?.body)
        }
    } // onMessageReceived 함수

    private fun showNotification(title: String?, body: String?) {
        val channelId = "your_channel_id"
        val channelName = "your_channel_name"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.mipmap.noiroze_app_icon) // 아이콘 변경
            .build()

        notificationManager.notify(0, notification)
    }
}