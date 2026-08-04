package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Customer
import com.example.data.model.PrinterConfig
import com.example.data.model.SalesTransaction
import com.example.ui.theme.TelebirrGoldAccent
import com.example.ui.theme.TelebirrGoldLight
import com.example.ui.theme.TelebirrGreenPrimary
import com.example.ui.theme.TelebirrGreenSecondary
import com.example.viewmodel.AppLanguage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class AirtimePricingItem(
    val denomination: Int,
    val faceValue: Double,
    val netPrice: Double,
    val discountText: String
)

@Composable
fun CustomerDashboardScreen(
    customer: Customer,
    transactions: List<SalesTransaction>,
    printerConfig: PrinterConfig?,
    language: AppLanguage,
    onPurchaseAndPrint: (denomination: Int) -> Unit,
    onOpenPrinterSettings: () -> Unit
) {
    val airtimeRates = listOf(
        AirtimePricingItem(100, 100.0, 95.00, "5.00 ETB (5% OFF)"),
        AirtimePricingItem(50, 50.0, 47.50, "2.50 ETB (5% OFF)"),
        AirtimePricingItem(25, 25.0, 23.75, "1.25 ETB (5% OFF)"),
        AirtimePricingItem(15, 15.0, 13.85, "1.15 ETB (7% OFF)"),
        AirtimePricingItem(10, 10.0, 9.50, "0.50 ETB (5% OFF)"),
        AirtimePricingItem(5, 5.0, 4.75, "0.25 ETB (5% OFF)")
    )

    val customerTxs = transactions.filter { it.customerId == customer.id }
    val sdf = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Customer Welcome & Live Balance Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("customer_balance_card"),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(TelebirrGreenPrimary, TelebirrGreenSecondary)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "User",
                                        tint = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = customer.name,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = customer.phone,
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.8f)
                                    )
                                }
                            }

                            // Active Status Badge
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = TelebirrGoldAccent
                            ) {
                                Text(
                                    text = if (language == AppLanguage.AMHARIC) "ንቁ አካውንት" else "Active Account",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Balance Big Display
                        Text(
                            text = if (language == AppLanguage.AMHARIC) "አጠቃላይ ቀሪ ሂሳብ (Balance)" else "Available Account Balance",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "%.2f".format(customer.balance),
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ETB (ብር)",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TelebirrGoldLight,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Bluetooth Printer Status Strip
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenPrinterSettings() }
                    .testTag("printer_status_card"),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Bluetooth,
                            contentDescription = "Bluetooth",
                            tint = TelebirrGreenPrimary
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = printerConfig?.printerName ?: "Bluetooth Thermal POS Printer",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = if (language == AppLanguage.AMHARIC) "ለመቀየር ወይም ለመፈተሽ እዚህ ይጫኑ" else "Tap to pair or view printer setup",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.Print,
                        contentDescription = "Printer Settings",
                        tint = TelebirrGreenPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Section Title: Buy & Print Airtime
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocalOffer, contentDescription = "Airtime", tint = TelebirrGreenPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == AppLanguage.AMHARIC) "ካርድ ይግዙ እና ያትሙ (Airtime Pricing)" else "Dynamic Airtime Voucher Printing",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = if (language == AppLanguage.AMHARIC)
                    "የቅናሽ ስሌቶች አውቶማቲክ ይሰላሉ (Automatic 5% Commission Applied)"
                else
                    "Select voucher denomination to buy & auto-print directly on thermal paper",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        // Grid of Airtime Denominations
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                airtimeRates.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowItems.forEach { rate ->
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("buy_card_${rate.denomination}"),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    // Denomination Badge
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(TelebirrGreenPrimary)
                                            .padding(vertical = 6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${rate.denomination} ETB",
                                            color = Color.White,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 20.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Pricing & Discount details
                                    Text(
                                        text = if (language == AppLanguage.AMHARIC) "የሚከፈለው: ${"%.2f".format(rate.netPrice)} ብር" else "Net Cost: ${"%.2f".format(rate.netPrice)} ETB",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = TelebirrGreenPrimary
                                    )
                                    Text(
                                        text = "ቅናሽ (Save): ${rate.discountText}",
                                        fontSize = 10.sp,
                                        color = Color(0xFF2E7D32),
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Button(
                                        onClick = { onPurchaseAndPrint(rate.denomination) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(38.dp)
                                            .testTag("purchase_button_${rate.denomination}"),
                                        colors = ButtonDefaults.buttonColors(containerColor = TelebirrGreenPrimary),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Print,
                                            contentDescription = "Print",
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (language == AppLanguage.AMHARIC) "ግዛ እና አትም" else "Buy & Print",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Recent Printed Vouchers Section
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.History, contentDescription = "History", tint = TelebirrGreenPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == AppLanguage.AMHARIC) "የቅርብ ጊዜ የተበተኑ ካርዶች ታሪክ" else "Recent Printed Cards Log",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (customerTxs.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (language == AppLanguage.AMHARIC) "እስካሁን ምንም የተበተነ ካርድ የለም።" else "No printed vouchers yet.",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            items(customerTxs.take(10)) { tx ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Receipt,
                                contentDescription = "Receipt",
                                tint = TelebirrGreenPrimary
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "${tx.denomination} ETB Airtime Voucher",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "PIN: *805*${tx.pin}#",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TelebirrGreenPrimary
                                )
                                Text(
                                    text = "SN: ${tx.serialNumber} | ${sdf.format(Date(tx.timestamp))}",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Text(
                            text = "${"%.2f".format(tx.netPrice)} ETB",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
