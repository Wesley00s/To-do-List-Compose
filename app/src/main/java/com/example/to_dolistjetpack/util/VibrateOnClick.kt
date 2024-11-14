package com.example.to_dolistjetpack.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.VibratorManager

fun vibrateOnClick(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        val vibrator = vibratorManager.defaultVibrator

        if (vibrator.hasVibrator()) {
            val effect = VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(effect)
        }
    }
}