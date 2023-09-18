package com.example.baseproject.util

import android.util.Patterns

fun String.isValidEmailInput() = Patterns.EMAIL_ADDRESS.matcher(this).matches()
