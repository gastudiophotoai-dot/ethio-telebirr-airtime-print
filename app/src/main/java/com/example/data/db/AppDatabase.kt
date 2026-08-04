package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.AirtimeVoucherDao
import com.example.data.dao.CustomerDao
import com.example.data.dao.PrinterConfigDao
import com.example.data.dao.SalesTransactionDao
import com.example.data.model.AirtimeVoucher
import com.example.data.model.Customer
import com.example.data.model.PrinterConfig
import com.example.data.model.SalesTransaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

@Database(
    entities = [
        Customer::class,
        AirtimeVoucher::class,
        SalesTransaction::class,
        PrinterConfig::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun customerDao(): CustomerDao
    abstract fun airtimeVoucherDao(): AirtimeVoucherDao
    abstract fun salesTransactionDao(): SalesTransactionDao
    abstract fun printerConfigDao(): PrinterConfigDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "telebirr_airtime_db"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        seedInitialData(database)
                    }
                }
            }

            private suspend fun seedInitialData(database: AppDatabase) {
                // Seed initial customers
                val customerDao = database.customerDao()
                customerDao.insertCustomer(
                    Customer(
                        name = "አበበ ቢቂላ (Abebe Bikila)",
                        phone = "0911223344",
                        email = "abebe@example.com",
                        pin = "1234",
                        balance = 500.00
                    )
                )
                customerDao.insertCustomer(
                    Customer(
                        name = "አስቴር አወቀ (Aster Aweke)",
                        phone = "0922334455",
                        email = "aster@example.com",
                        pin = "5678",
                        balance = 250.00
                    )
                )
                customerDao.insertCustomer(
                    Customer(
                        name = "ዳዊት ፅጌ (Dawit Tsige)",
                        phone = "0933445566",
                        email = "dawit@example.com",
                        pin = "9988",
                        balance = 1000.00
                    )
                )

                // Seed initial vouchers stock
                val voucherDao = database.airtimeVoucherDao()
                val denominations = listOf(
                    100 to 95.00,
                    50 to 47.50,
                    25 to 23.75,
                    15 to 13.85,
                    10 to 9.50,
                    5 to 4.75
                )

                val vouchersToInsert = mutableListOf<AirtimeVoucher>()
                for ((denom, discountPrice) in denominations) {
                    repeat(5) { i ->
                        val randomPin = (10000000000000..99999999999999).random().toString()
                        val randomSerial = "1029" + (10000000..99999999).random().toString()
                        vouchersToInsert.add(
                            AirtimeVoucher(
                                denomination = denom,
                                pin = randomPin,
                                serialNumber = randomSerial,
                                faceValue = denom.toDouble(),
                                discountPrice = discountPrice
                            )
                        )
                    }
                }
                voucherDao.insertVouchers(vouchersToInsert)

                // Seed default printer configuration
                val printerDao = database.printerConfigDao()
                printerDao.savePrinterConfig(
                    PrinterConfig(
                        id = 1,
                        printerName = "Ethio POS Thermal Printer (58mm)",
                        printerAddress = "00:11:22:AA:BB:CC",
                        paperWidthMm = 58,
                        autoPrintEnabled = true,
                        agentName = "Ethio Telebirr Official Agent",
                        agentCode = "TB-AG0091"
                    )
                )
            }
        }
    }
}
