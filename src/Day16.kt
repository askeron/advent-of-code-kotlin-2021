fun main() {
    fun part1(input: String): Int {
        return BitStream.ofHex(input)
            .parseAndGetBitsLeft()
            .also { assertEquals(0, it.bitsLeft.toInt()) }
            .packet
            .getSubPacketsFlattened()
            .sumOf { it.version }
    }

    fun part2(input: String): Long {
        return BitStream.ofHex(input)
            .parseAndGetBitsLeft()
            .also { assertEquals(0, it.bitsLeft.toInt()) }
            .packet.value()
    }

    fun parseInput(input: List<String>) = input[0]

    val input = parseInput(readInput("Day16"))
    println(BitStream.ofHex("D2FE28").parseAndGetBitsLeft())
    println(BitStream.ofHex("38006F45291200").parseAndGetBitsLeft())
    println(BitStream.ofHex("EE00D40C823060").parseAndGetBitsLeft())
    assertEquals(16, part1("8A004A801A8002F478"))
    assertEquals(12, part1("620080001611562C8802118E34"))
    assertEquals(23, part1("C0015000016115A2E0802F182340"))
    assertEquals(31, part1("A0016C880162017C3686B18A3D4780"))
    println(part1(input))
    assertEquals(3L, part2("C200B40A82"))
    assertEquals(54L, part2("04005AC33890"))
    assertEquals(7L, part2("880086C3E88112"))
    assertEquals(9L, part2("CE00C43D881120"))
    assertEquals(1L, part2("D8005AC2A8F0"))
    assertEquals(0L, part2("F600BC2D8F"))
    assertEquals(0L, part2("9C005AC2F8F0"))
    assertEquals(1L, part2("9C0141080250320F1802104A08"))
    println(part2(input))
}

private sealed class Packet(val version: Int, val packetId: Int, val subPackets: List<Packet>) {
    fun getSubPacketsFlattened(): List<Packet> = listOf(this).plus(subPackets.flatMap { it.getSubPacketsFlattened() })
    fun value(): Long = when (this) {
        is LiteralPacket -> literal
        is OperatorPacket -> when (packetId) {
            0 -> subPackets.sumOf { it.value() }
            1 -> subPackets.map { it.value() }.reduce { acc, l -> acc * l }
            2 -> subPackets.minOf { it.value() }
            3 -> subPackets.maxOf { it.value() }
            5 -> subPackets.map { it.value() }.let { it[0] > it[1] }.toLong()
            6 -> subPackets.map { it.value() }.let { it[0] < it[1] }.toLong()
            7 -> subPackets.map { it.value() }.let { it[0] == it[1] }.toLong()
            else -> error("unknown packetId $packetId")
        }
    }
}
private class LiteralPacket(version: Int, packetId: Int, val literal: Long): Packet(version, packetId, emptyList()) {
    override fun toString() = "LiteralPacket(version = $version, packetId = $packetId, literal = $literal)"
}
private class OperatorPacket(version: Int, packetId: Int, subPackets: List<Packet>): Packet(version, packetId, subPackets) {
    override fun toString() = "OperatorPacket(version = $version, packetId = $packetId, subPackets = $subPackets)"
}

private class BitStream(bitsInput: List<Boolean>) {
    private val bits = bitsInput.toMutableList()
    fun removeBits(n: Int) = (1..n).map { bits.removeAt(0) }

    data class ParseResult(val packet: Packet, val bitsLeft: List<Boolean>)

    fun parseAndGetBitsLeft(): ParseResult {
        val version = removeBits(3).toInt()
        val packetId = removeBits(3).toInt()
        if (packetId == 4) {
            val literalParts = mutableListOf<List<Boolean>>()
            do {
                val literalBits = removeBits(5)
                literalParts += literalBits.drop(1)
            } while (literalBits.first())
            return ParseResult(LiteralPacket(version, packetId, literalParts.flatten().toLong()), bits.toList())
        } else {
            if (removeBits(1)[0]) {
                val subPacketCount = removeBits(11).toInt()
                var bitsLeft = bits.toList()
                val subPackets = mutableListOf<Packet>()
                repeat(subPacketCount) {
                    BitStream(bitsLeft).parseAndGetBitsLeft().also {
                        subPackets += it.packet
                        bitsLeft = it.bitsLeft
                    }
                }
                return ParseResult(OperatorPacket(version, packetId, subPackets), bitsLeft.toList())
            } else {
                val subPacketsLength = removeBits(15).toInt()
                var bitsLeft = removeBits(subPacketsLength)
                val subPackets = mutableListOf<Packet>()
                while (bitsLeft.isNotEmpty()) {
                    BitStream(bitsLeft).parseAndGetBitsLeft().also {
                        subPackets += it.packet
                        bitsLeft = it.bitsLeft
                    }
                }
                return ParseResult(OperatorPacket(version, packetId, subPackets), bits.toList())
            }
        }
    }

    companion object {
        fun ofHex(hexString: String): BitStream = BitStream(hexString.toCharArray()
            .map { it.toString().toInt(16) }
            .flatMap { listOf(
                it.getBit(3),
                it.getBit(2),
                it.getBit(1),
                it.getBit(0),
            ) })
    }
}

private fun Boolean.toInt() = if (this) 1 else 0
private fun Boolean.toLong() = toInt().toLong()
private fun List<Boolean>.toInt() = toLong().toInt()
private fun List<Boolean>.toLong() = reversed().mapIndexed { index, b -> b.toInt().toLong() shl index }.sum()
private fun Int.getBit(position: Int) = (this shr position) and 1 == 1