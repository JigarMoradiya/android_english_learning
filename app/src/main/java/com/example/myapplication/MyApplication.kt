package com.example.myapplication

import android.app.Activity
import android.app.Application
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.annotation.NonNull
import com.example.myapplication.data.model.NotificationData
import com.example.myapplication.main.base.notification.NotificationCommand
import com.example.myapplication.main.base.notification.NotificationRouteMapper
import com.example.myapplication.main.base.notification.PendingNotificationRoute
import com.example.myapplication.utils.AppConstants
import com.google.firebase.crashlytics.internal.common.CommonUtils
import com.google.gson.Gson
import com.onesignal.OneSignal
import com.onesignal.OneSignal.InAppMessages
import com.onesignal.OneSignal.Location
import com.onesignal.OneSignal.Notifications
import com.onesignal.OneSignal.User
import com.onesignal.debug.LogLevel
import com.onesignal.inAppMessages.IInAppMessageClickEvent
import com.onesignal.inAppMessages.IInAppMessageClickListener
import com.onesignal.inAppMessages.IInAppMessageDidDismissEvent
import com.onesignal.inAppMessages.IInAppMessageDidDisplayEvent
import com.onesignal.inAppMessages.IInAppMessageLifecycleListener
import com.onesignal.inAppMessages.IInAppMessageWillDismissEvent
import com.onesignal.inAppMessages.IInAppMessageWillDisplayEvent
import com.onesignal.notifications.INotificationClickEvent
import com.onesignal.notifications.INotificationClickListener
import com.onesignal.notifications.INotificationLifecycleListener
import com.onesignal.notifications.INotificationWillDisplayEvent
import com.onesignal.user.state.IUserStateObserver
import com.onesignal.user.state.UserChangedState
import dagger.hilt.android.HiltAndroidApp
import kotlin.jvm.java

@HiltAndroidApp
class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        oneSignal()
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
            }
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })

    }

    private fun oneSignal() {
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.Debug.logLevel = LogLevel.VERBOSE
        // OneSignal Initialization
        OneSignal.initWithContext(this, AppConstants.ONESIGNAL_KEY)

        InAppMessages.addLifecycleListener(object : IInAppMessageLifecycleListener {
            override fun onWillDisplay(@NonNull event: IInAppMessageWillDisplayEvent) {
            }

            override fun onDidDisplay(@NonNull event: IInAppMessageDidDisplayEvent) {
            }

            override fun onWillDismiss(@NonNull event: IInAppMessageWillDismissEvent) {
            }

            override fun onDidDismiss(@NonNull event: IInAppMessageDidDismissEvent) {
            }
        })

        InAppMessages.addClickListener(object : IInAppMessageClickListener {
            override fun onClick(event: IInAppMessageClickEvent) {
            }
        })

        Notifications.addClickListener(object : INotificationClickListener {
            override fun onClick(event: INotificationClickEvent) {
                val additionalData = event.notification.additionalData ?: return
                val notification = runCatching {
                    Gson().fromJson(additionalData.toString(), NotificationData::class.java)
                }.getOrNull() ?: return

                when {
                    notification.type == "link" && notification.link.isNotEmpty() -> {
                        PendingNotificationRoute.emit(NotificationCommand.OpenLink(notification.link))
                    }
                    notification.type.isNotEmpty() -> {
                        NotificationRouteMapper.routeFromType(notification.type)?.let { route ->
                            PendingNotificationRoute.emit(NotificationCommand.Navigate(route))
                        }
                    }
                }
            }
        })

        Notifications.addForegroundLifecycleListener(object : INotificationLifecycleListener {
            override fun onWillDisplay(@NonNull event: INotificationWillDisplayEvent) {
                val notification = event.notification
                val data = notification.additionalData

                //Prevent OneSignal from displaying the notification immediately on return. Spin
                //up a new thread to mimic some asynchronous behavior, when the async behavior (which
                //takes 2 seconds) completes, then the notification can be displayed.
                event.preventDefault()
                val r = Runnable {
                    try {
                        Thread.sleep(2000)
                    } catch (ignored: InterruptedException) {
                    }
                    notification.display()
                }
                val t = Thread(r)
                t.start()
            }
        })

        User.addObserver(object : IUserStateObserver {
            override fun onUserStateChange(@NonNull state: UserChangedState) {
                val currentUserState = state.current
            }
        })

        InAppMessages.paused = true
        Location.isShared = false

    }
}