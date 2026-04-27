package com.example.myapplication.main.base.notification

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

sealed interface NotificationCommand {
    data class Navigate(val route: String) : NotificationCommand
    data class OpenLink(val url: String) : NotificationCommand
}

object PendingNotificationRoute {

    private val _commands = MutableSharedFlow<NotificationCommand>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val commands: SharedFlow<NotificationCommand> = _commands.asSharedFlow()

    fun emit(command: NotificationCommand) {
        _commands.tryEmit(command)
    }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    fun clear() {
        _commands.resetReplayCache()
    }
}
