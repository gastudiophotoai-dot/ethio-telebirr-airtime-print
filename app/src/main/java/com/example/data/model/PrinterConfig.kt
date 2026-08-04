package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "printer_config")
data class PrinterConfig(
    @PrimaryKey val id: Int = 1,
    val printerName: String = "POS Thermal Printer",
    val printerAddress: String = "00:11:22:33:44:55",
    val paperWidthMm: Int = 58,
    val autoPrintEnabled: Boolean = true,
    val agentName: String = "Ethio Telebirr Express Agent",
    val agentCode: String = "TB-AG8820"
)
