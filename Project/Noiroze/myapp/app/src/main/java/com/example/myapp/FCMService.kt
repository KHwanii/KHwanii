package com.example.myapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat

import com.example.myapp.login.LoginActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_channel"
const val channelName = "com.example.myapp"

class FCMService : FirebaseMessagingService() {

    fun generateNotification(title: String, detail: String) {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)  // 해당 플래그 설정 시, 해당 액티비티가 최상위로 올라오고, 나머지는 종료

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
        // 다른 앱에 권한을 부여하여 특정 작업을 수행하도록 허용하는 토큰.

        val notiSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)  // 알람 사운드 설정
        var builder : NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.mipmap.noiroze_app_icon)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            .setSound(notiSound)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title, detail))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0, builder.build())
    }  // generateNotification 함수

    fun getRemoteView(title : String, detail : String) : RemoteViews {
        val remoteView = RemoteViews("com.example.myapp", R.layout.notification)    // 사용자 정의 알림 레이아웃 생성
        remoteView.setTextViewText(R.id.Noti_Title, title)
        remoteView.setTextViewText(R.id.Noti_Detail, detail)
        remoteView.setImageViewResource(R.id.app_logo, R.mipmap.noiroze_app_icon)   // 사용자 정의 레이아웃과 연결

        return remoteView
    }  // getRemoteView 함수

    override fun onMessageReceived(message: RemoteMessage) {
        if(message.getNotification() != null) {
            generateNotification(message.notification!!.title!!, message.notification!!.body!!)
        }
    }  // onMessageReceived 함수
}