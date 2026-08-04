package com.example.data.repository

import com.example.data.dao.AirtimeVoucherDao
import com.example.data.dao.CustomerDao
import com.example.data.dao.PrinterConfigDao
import com.example.data.dao.SalesTransactionDao
import com.example.data.model.AirtimeVoucher
import com.example.data.model.Customer
import com.example.data.model.PrinterConfig
import com.example.data.model.SalesTransaction
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random

class TelebirrRepository(
    private val customerDao: CustomerDao,
    private val voucherDao: AirtimeVoucherDao,
    private val transactionDao: SalesTransactionDao,
    private val printerConfigDao: PrinterConfigDao
) {
    val allCustomers: Flow<List<Customer>> = customerDao.getAllCustomers()
    val allVouchers: Flow<List<AirtimeVoucher>> = voucherDao.getAllVouchers()
    val allTransactions: Flow<List<SalesTransaction>> = transactionDao.getAllTransactions()
    val printerConfig: Flow<PrinterConfig?> = printerConfigDao.getPrinterConfig()
    val totalSalesAmount: Flow<Double?> = transactionDao.getTotalSalesAmount()
    val totalDiscountsGiven: Flow<Double?> = transactionDao.getTotalDiscountsGiven()

    fun getCustomerTransactions(customerId: Long): Flow<List<SalesTransaction>> {
        return transactionDao.getTransactionsByCustomer(customerId)
    }

    fun getAvailableVoucherCount(denomination: Int): Flow<Int> {
        return voucherDao.getAvailableCount(denomination)
    }

    suspend fun getCustomerByPhone(phone: String): Customer? {
        return customerDao.getCustomerByPhone(phone.trim())
    }

    suspend fun registerCustomer(
        name: String,
        phone: String,
        email: String,
        initialBalance: Double = 0.0,
        customPin: String? = null
    ): Result<Pair<Customer, String>> {
        val existing = customerDao.getCustomerByPhone(phone.trim())
        if (existing != null) {
            return Result.failure(Exception("Customer with phone $phone already exists!"))
        }

        val generatedPin = customPin?.takeIf { it.length == 4 }
            ?: Random.nextInt(1000, 9999).toString()

        val newCustomer = Customer(
            name = name.trim(),
            phone = phone.trim(),
            email = email.trim(),
            pin = generatedPin,
            balance = initialBalance
        )

        val id = customerDao.insertCustomer(newCustomer)
        val created = newCustomer.copy(id = id)
        return Result.success(Pair(created, generatedPin))
    }

    suspend fun topUpCustomerBalance(customerId: Long, amount: Double): Result<Unit> {
        if (amount <= 0) {
            return Result.failure(Exception("Top up amount must be greater than zero!"))
        }
        customerDao.topUpBalance(customerId, amount)
        return Result.success(Unit)
    }

    /**
     * Forget Password / Reset PIN requirement:
     * Resets customer PIN to a new 4-digit PIN while keeping previous balance 100% intact!
     */
    suspend fun resetCustomerPin(phone: String): Result<String> {
        val cleanPhone = phone.trim()
        val customer = customerDao.getCustomerByPhone(cleanPhone)
            ?: return Result.failure(Exception("No registered customer found for phone: $cleanPhone"))

        val newPin = Random.nextInt(1000, 9999).toString()
        val rowsUpdated = customerDao.resetCustomerPin(cleanPhone, newPin)
        return if (rowsUpdated > 0) {
            Result.success(newPin)
        } else {
            Result.failure(Exception("Failed to reset PIN. Please try again."))
        }
    }

    /**
     * Discount Pricing rules:
     * 100 ETB -> 95.00 ETB
     * 50 ETB  -> 47.50 ETB
     * 25 ETB  -> 23.75 ETB
     * 15 ETB  -> 13.85 ETB
     * 10 ETB  -> 9.50 ETB
     * 5 ETB   -> 4.75 ETB
     */
    fun calculateNetPrice(denomination: Int): Double {
        return when (denomination) {
            100 -> 95.00
            50 -> 47.50
            25 -> 23.75
            15 -> 13.85
            10 -> 9.50
            5 -> 4.75
            else -> denomination * 0.95
        }
    }

    suspend fun purchaseAndPrintVoucher(
        customer: Customer,
        denomination: Int
    ): Result<Pair<SalesTransaction, AirtimeVoucher>> {
        val faceValue = denomination.toDouble()
        val netPrice = calculateNetPrice(denomination)
        val discountAmount = faceValue - netPrice

        // Verify balance
        val currentCustomer = customerDao.getCustomerById(customer.id)
            ?: return Result.failure(Exception("Customer account not found"))

        if (currentCustomer.balance < netPrice) {
            return Result.failure(
                Exception("Insufficient balance! Required: ${"%.2f".format(netPrice)} ETB, Available: ${"%.2f".format(currentCustomer.balance)} ETB")
            )
        }

        // Fetch or auto-generate voucher from inventory
        var voucher = voucherDao.getAvailableVoucher(denomination)
        if (voucher == null) {
            // Auto-generate fresh voucher for this denomination if stock is empty
            val newPin = (10000000000000..99999999999999).random().toString()
            val newSerial = "1029" + (10000000..99999999).random().toString()
            val newVoucher = AirtimeVoucher(
                denomination = denomination,
                pin = newPin,
                serialNumber = newSerial,
                faceValue = faceValue,
                discountPrice = netPrice
            )
            val voucherId = voucherDao.insertVoucher(newVoucher)
            voucher = newVoucher.copy(id = voucherId)
        }

        // Deduct balance atomically
        val rowsAffected = customerDao.deductBalance(currentCustomer.id, netPrice)
        if (rowsAffected == 0) {
            return Result.failure(Exception("Transaction failed due to balance verification conflict."))
        }

        // Mark voucher as used
        val now = System.currentTimeMillis()
        val updatedVoucher = voucher.copy(
            isUsed = true,
            usedByCustomerId = currentCustomer.id,
            usedByCustomerPhone = currentCustomer.phone,
            printedTimestamp = now
        )
        voucherDao.updateVoucher(updatedVoucher)

        // Log Sales Transaction
        val transaction = SalesTransaction(
            customerId = currentCustomer.id,
            customerName = currentCustomer.name,
            customerPhone = currentCustomer.phone,
            denomination = denomination,
            faceValue = faceValue,
            netPrice = netPrice,
            discountAmount = discountAmount,
            serialNumber = updatedVoucher.serialNumber,
            pin = updatedVoucher.pin,
            timestamp = now,
            printerStatus = "PRINTED"
        )
        val txId = transactionDao.insertTransaction(transaction)

        return Result.success(Pair(transaction.copy(id = txId), updatedVoucher))
    }

    suspend fun batchAddVouchers(denomination: Int, count: Int): Result<Int> {
        val discountPrice = calculateNetPrice(denomination)
        val list = mutableListOf<AirtimeVoucher>()
        repeat(count) {
            val pin = (10000000000000..99999999999999).random().toString()
            val serial = "1029" + (10000000..99999999).random().toString()
            list.add(
                AirtimeVoucher(
                    denomination = denomination,
                    pin = pin,
                    serialNumber = serial,
                    faceValue = denomination.toDouble(),
                    discountPrice = discountPrice
                )
            )
        }
        voucherDao.insertVouchers(list)
        return Result.success(count)
    }

    suspend fun updatePrinterConfig(config: PrinterConfig) {
        printerConfigDao.savePrinterConfig(config)
    }
}
