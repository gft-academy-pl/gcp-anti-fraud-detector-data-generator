package com.gft.academy.frauddetector.generator

import java.io.File
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Stream


class App {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {


            val dataStream: Stream<String> = TradeDataGenerator().generate(1000)
                    .map({ i -> i.second.toCSV() })

            val path = "trades.csv"
            var file = File(path)
            print("Output File: ${file.absolutePath}")

            if (file.exists()) {
                file.delete()
            }
            file.createNewFile()
            val get = Paths.get(path)
            PrintWriter(Files.newBufferedWriter(
                    get)).use { pw -> dataStream.forEach { data -> pw.println(data) } }

        }
    }
}