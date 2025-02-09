package com.example.nfcapp

data class NfcCard(
    val id: String = "",
    val cardData: String = "",
    val location: String = "",
    val timestamp: Long = 0
)