package com.gft.academy.frauddetector.generator

import java.io.File
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import java.util.stream.IntStream
import java.util.stream.Stream

class TradeDataGenerator {

    val randomizer = Randomizer()

    fun generate(numOfRecords: Int = 1000000, numOfFrauds: Int = 0): Stream<Pair<Int, Trade>> {
        return IntStream.range(numOfFrauds, numOfRecords)
                .mapToObj({ i -> Pair(i, generateSingle(i, numOfRecords)) })

    }

    private fun generateSingle(i: Int, numOfRecords: Int): Trade {

        val priceNetto = randomizer.sampleDecimal(BigDecimal.ZERO, BigDecimal.valueOf(100))
        val amount = randomizer.sampleInt(100)
        val tax = randomizer.sampleDecimal(BigDecimal.ZERO, priceNetto.multiply(amount.toBigDecimal()).divide(BigDecimal.valueOf(2)))
        val price = Amount(priceNetto, amount, tax)
        val timestampOffsetMiliseconds: Long = (24 * 60 * 60 * i / numOfRecords).toLong()
        val created = LocalDate.now().atStartOfDay().plusSeconds(timestampOffsetMiliseconds)
        var out = Trade(
                trade = randomizer.random("BS", "SL", "SS"),
                client = randomizer.random(readFile("COMPANY_DATA.csv")),
                account = randomizer.sampleString(),
                clearingBroker = randomizer.random(readFile("BROKER_DATA.csv")),
                executingBroker = randomizer.random(readFile("BROKER_DATA.csv")),
                amount = price.amount,
                currency = randomizer.random(readFile("CURRENCY_DATA.csv")),
                region = randomizer.random(readFile("REGION_DATA.csv")),
                price = price.price,
                tax = price.tax,
                value = price.value,
                fee = randomizer.sampleDecimal(BigDecimal.ZERO, price.value.multiply(BigDecimal.valueOf(0.5))),
                status = randomizer.random(readFile("STATUS_DATA.csv")),
                version = randomizer.sampleInt(),
                securityId = randomizer.sampleInt(),
                securityIdType = randomizer.sampleInt(),
                tradeReferenceNumber = randomizer.sampleInt(),
                createdBy = randomizer.sampleString(),
                createdOn = created
        )

        return out
    }


    private val cache: HashMap<String, List<String>> = HashMap()

    fun readFile(fileName: String): List<String> {
        if (!cache.containsKey(fileName)) {
            val filePath = javaClass.classLoader.getResource(fileName).file
            cache[fileName] = File(filePath).readLines()
        }
        return cache.getOrDefault(fileName, emptyList())
    }

}

data class Amount(val price: BigDecimal, val amount: Int, val tax: BigDecimal) {
    val value get() = price.multiply(BigDecimal.valueOf(amount.toLong())).plus(tax)
}

class Randomizer {

    var random = Random()

    fun random(vararg input: String): String {
        return input[random.nextInt(input.size)]
    }

    fun random(input: Collection<String>): String {
        return input.elementAt(random.nextInt(input.size))
    }

    fun sampleString(): String {
        return UUID.randomUUID().toString()
    }

    fun sampleDecimal(): BigDecimal {
        return BigDecimal.valueOf(random.nextDouble())
    }

    fun sampleDecimal(min: BigDecimal, max: BigDecimal): BigDecimal {
        val randomBigDecimal = min.add(BigDecimal(Math.random()).multiply(max.subtract(min)))
        return randomBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP)
    }


    fun sampleInt(max: Int = Integer.MAX_VALUE): Int {
        return random.nextInt(max)
    }
}
