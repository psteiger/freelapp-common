package com.freelapp.common.application.ktx

import kotlinx.coroutines.channels.SendChannel

fun <E> SendChannel<E>.tryOffer(element: E): Boolean =
    runCatching { offer(element) }.getOrDefault(false)