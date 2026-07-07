package com.ntech.smsreader

data class SmsRecord(
    val id: Long,
    val sender: String,
    val body: String,
    val receivedAt: Long
)
