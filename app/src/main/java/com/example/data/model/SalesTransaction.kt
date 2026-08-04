package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sales_transactions")
data class SalesTransaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val customerId: Long,
    val customerName: String,
    val customerPhone: String,
    val denomination: Int,
    val faceValue: Double,
    val netPrice: Double,
    val discountAmount: Double,
    val serialNumber: String,
    val pin: String,
    val timestamp: Long = System.currentTimeMillis(),
    val printerStatus: String = "PRINTED"
)
