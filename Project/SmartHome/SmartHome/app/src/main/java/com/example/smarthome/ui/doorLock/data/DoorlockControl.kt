package com.example.smarthome.ui.doorLock.data

import com.google.gson.annotations.SerializedName

data class DoorlockControl(
    @SerializedName("doorlockcontrol")
    val control: Boolean
)
