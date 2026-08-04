package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AirtimeVoucher
import com.example.data.model.Customer
import com.example.data.model.PrinterConfig
import com.example.data.model.SalesTransaction
import com.example.ui.theme.TelebirrGoldAccent
import com.example.ui.theme.TelebirrGreenPrimary
import com.example.viewmodel.AppLanguage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminDashboardScreen(
    customers: List<Customer>,
    vouchers: List<AirtimeVoucher>,
    transactions: List<SalesTransaction>,
    printerConfig: PrinterConfig?,
    totalSales: Double,
    totalDiscounts: Double,
    language: AppLanguage,
    onOpenAddCustomer: () -> Unit,
    onOpenTopUp: (Customer) -> Unit,
    onOpenBatchAddVouchers: () -> Unit,
    onOpenPrinterSettings: () -> Unit
) {
    var adminTab by remember { mutableIntStateOf(0) } // 0 = Customers, 1 = Stock, 2 = Reports
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Admin Analytics Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("admin_analytics_card"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
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
                                .background(TelebirrGreenPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PointOfSale,
                                contentDescription = "POS Admin",
                                tint = TelebirrGoldAccent,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.AMHARIC) "የአስተዳዳሪ ዳሽቦርድ" else "Agent Admin Dashboard",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = TelebirrGreenPrimary
                    ) {
                        Text(
                            text = if (language == AppLanguage.AMHARIC) "የተፈቀደ ኤጀንት" else "Verified Agent",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Total Sales Metric
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = if (language == AppLanguage.AMHARIC) "ጠቅላላ ሽያጭ" else "Total Sales",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${"%.2f".format(totalSales)} ETB",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = TelebirrGreenPrimary
                            )
                        }
                    }

                    // Total Cards Sold Metric
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = if (language == AppLanguage.AMHARIC) "የተሸጡ ካርዶች" else "Cards Sold",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${transactions.size} Cards",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Total Customers Metric
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = if (language == AppLanguage.AMHARIC) "ደንበኞች" else "Customers",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${customers.size} Users",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Navigation Tabs for Admin
        TabRow(
            selectedTabIndex = adminTab,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
        ) {
            Tab(
                selected = adminTab == 0,
                onClick = { adminTab = 0 },
                modifier = Modifier.testTag("admin_customers_tab")
            ) {
                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Group, contentDescription = "Customers", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = if (language == AppLanguage.AMHARIC) "ደንበኞች" else "Customers", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Tab(
                selected = adminTab == 1,
                onClick = { adminTab = 1 },
                modifier = Modifier.testTag("admin_stock_tab")
            ) {
                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Inventory, contentDescription = "Stock", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = if (language == AppLanguage.AMHARIC) "ክምችት" else "Stock", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Tab(
                selected = adminTab == 2,
                onClick = { adminTab = 2 },
                modifier = Modifier.testTag("admin_reports_tab")
            ) {
                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ReceiptLong, contentDescription = "Reports", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = if (language == AppLanguage.AMHARIC) "ሪፖርት" else "Reports", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (adminTab) {
            0 -> {
                // Customer Management Tab
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == AppLanguage.AMHARIC) "የተመዘገቡ ደንበኞች ዝርዝር" else "Registered Customers List",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    Button(
                        onClick = onOpenAddCustomer,
                        colors = ButtonDefaults.buttonColors(containerColor = TelebirrGreenPrimary),
                        modifier = Modifier.testTag("add_customer_fab")
                    ) {
                        Icon(Icons.Default.PersonAdd, contentDescription = "Add Customer", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (language == AppLanguage.AMHARIC) "አዲስ ደንበኛ" else "Add Customer", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(customers) { cust ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("customer_item_${cust.id}"),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(text = cust.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                        Text(text = "📞 ${cust.phone} | ✉️ ${cust.email.ifBlank { "N/A" }}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(text = "🔑 4-Digit PIN: ${cust.pin}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TelebirrGreenPrimary)
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "${"%.2f".format(cust.balance)} ETB",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 16.sp,
                                            color = TelebirrGreenPrimary
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        OutlinedButton(
                                            onClick = { onOpenTopUp(cust) },
                                            modifier = Modifier.testTag("topup_button_${cust.id}")
                                        ) {
                                            Icon(Icons.Default.AddCard, contentDescription = "Deposit", modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(if (language == AppLanguage.AMHARIC) "ብር ሙላ" else "Deposit", fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // Stock Inventory Tab
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == AppLanguage.AMHARIC) "የካርዶች ክምችት (Stock Inventory)" else "Airtime Voucher Stock",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    Button(
                        onClick = onOpenBatchAddVouchers,
                        colors = ButtonDefaults.buttonColors(containerColor = TelebirrGreenPrimary),
                        modifier = Modifier.testTag("batch_add_vouchers_button")
                    ) {
                        Icon(Icons.Default.AddCircle, contentDescription = "Add Stock", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (language == AppLanguage.AMHARIC) "ካርድ ጫን" else "Add Stock", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                val denoms = listOf(100, 50, 25, 15, 10, 5)
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(denoms) { denom ->
                        val availableCount = vouchers.count { it.denomination == denom && !it.isUsed }
                        val usedCount = vouchers.count { it.denomination == denom && it.isUsed }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(TelebirrGreenPrimary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "$denom", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(text = "ባለ $denom ብር ካርድ", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                        Text(text = "የተሸጠ: $usedCount | የቀረ: $availableCount", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (availableCount > 0) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                                ) {
                                    Text(
                                        text = if (availableCount > 0) "$availableCount ዝግጁ (Ready)" else "ካርድ አልቋል (Empty)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (availableCount > 0) Color(0xFF2E7D32) else Color(0xFFC62828),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // Reports Tab
                Text(
                    text = if (language == AppLanguage.AMHARIC) "የኤጀንት ሽያጭ ሪፖርቶች" else "Agent Sales & Transactions History",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(transactions) { tx ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
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
                                Column {
                                    Text(text = "${tx.customerName} (${tx.customerPhone})", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(text = "Denom: ${tx.denomination} ETB | Net: ${"%.2f".format(tx.netPrice)} ETB", fontSize = 11.sp, color = TelebirrGreenPrimary)
                                    Text(text = "SN: ${tx.serialNumber} | Date: ${sdf.format(Date(tx.timestamp))}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }

                                Text(
                                    text = "PIN: *805*${tx.pin}#",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TelebirrGreenPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
