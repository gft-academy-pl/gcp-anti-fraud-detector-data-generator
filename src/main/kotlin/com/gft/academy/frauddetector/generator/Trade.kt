package com.gft.academy.frauddetector.generator

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class Trade(
        val trade: String,
        val client: String,
        val account: String,
        val clearingBroker: String,
        val executingBroker: String,
        val amount: Int,
        val currency: String,
        val region: String,
        val price: BigDecimal,
        val tax: BigDecimal,
        val value: BigDecimal,
        val fee: BigDecimal,
        val status: String,
        val version: Int,
        val securityId: Int,
        val securityIdType: Int,
        val tradeReferenceNumber: Int,
        val tradeDate: LocalDate = LocalDate.now().minusDays(1L),
        val settleDate: LocalDate = LocalDate.now(),
        val createdBy: String,
        val createdOn: LocalDateTime = LocalDateTime.now()
) {
    fun toCSV(): String {
        return "$trade,$client,$account,$clearingBroker,$executingBroker,$amount,$currency,$region,$price,$tax,$value,$fee,$status,$version,$securityId,$securityIdType,$tradeReferenceNumber,$tradeDate,$settleDate,$createdBy,$createdOn"
    }
}

