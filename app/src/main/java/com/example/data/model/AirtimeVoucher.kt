package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "airtime_vouchers")
data class AirtimeVoucher(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val denomination: Int, // 100, 50, 25, 15, 10, 5
    val pin: String,
    val serialNumber: String,
    val faceValue: Double,
    val discountPrice: Double,
    val isUsed: Boolean = false,
    val usedByCustomerId: Long? = null,
    val usedByCustomerPhone: String? = null,
    val printedTimestamp: Long? = null
)
