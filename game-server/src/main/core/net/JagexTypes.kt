package core.net

import java.nio.ByteBuffer

private val BIT_MASK = IntArray(32) { (1 shl it) - 1 }
private val bitPositions = java.util.WeakHashMap<ByteBuffer, Int>()

/**
 * G1s - gets a signed byte from the buffer
 * @return value as Int
 */
fun ByteBuffer.g1s(): Int = get().toInt()

/**
 * G2s - gets a signed short from the buffer
 * @return value as Int
 */
fun ByteBuffer.g2s(): Int = short.toInt()

/**
 * Reads a null-terminated string.
 * @return the string read.
 */
fun ByteBuffer.gjstr(): String {
    val sb = StringBuilder()
    while (true) {
        val b = get()
        if (b.toInt() == 0) break
        sb.append(b.toInt().toChar())
    }
    return sb.toString()
}

/**
 * Puts a byte to the buffer.
 * @param value the value to put.
 */
fun ByteBuffer.p1(value: Int) {
    put(value.toByte())
}

/**
 * Puts a byte (value + 128).
 * @param value the value to put.
 */
fun ByteBuffer.p1add(value: Int) {
    put((value + 128).toByte())
}

/**
 * Puts a byte (128 - value).
 * @param value the value to put.
 */
fun ByteBuffer.p1sub(value: Int) {
    put((128 - value).toByte())
}

/**
 * Puts a negated byte (-value).
 * @param value the value to put.
 */
fun ByteBuffer.p1neg(value: Int) {
    put((-value).toByte())
}

/**
 * Puts a 2-byte short (big-endian).
 * @param value the value to put.
 */
fun ByteBuffer.p2(value: Int) {
    put((value shr 8).toByte())
    put(value.toByte())
}

/**
 * Puts a 2-byte short (big-endian), second byte +128.
 * @param value the value to put.
 */
fun ByteBuffer.p2add(value: Int) {
    put((value shr 8).toByte())
    put((value + 128).toByte())
}

/**
 * Puts a 2-byte short (little-endian).
 * @param value the value to put.
 */
fun ByteBuffer.ip2(value: Int) {
    put(value.toByte())
    put((value shr 8).toByte())
}

/**
 * Puts a 2-byte short (little-endian), first byte +128.
 * @param value the value to put.
 */
fun ByteBuffer.ip2add(value: Int) {
    put((value + 128).toByte())
    put((value shr 8).toByte())
}

/**
 * Puts a 3-byte int (big-endian).
 * @param value the value to put.
 */
fun ByteBuffer.p3(value: Int) {
    put((value shr 16).toByte())
    put((value shr 8).toByte())
    put(value.toByte())
}

/**
 * Puts a 3-byte int (little-endian).
 * @param value the value to put.
 */
fun ByteBuffer.ip3(value: Int) {
    put(value.toByte())
    put((value shr 8).toByte())
    put((value shr 16).toByte())
}

/**
 * Puts a 4-byte int (big-endian).
 * @param value the value to put.
 */
fun ByteBuffer.p4(value: Int) {
    put((value shr 24).toByte())
    put((value shr 16).toByte())
    put((value shr 8).toByte())
    put(value.toByte())
}

/**
 * Puts a 4-byte int (little-endian).
 * @param value the value to put.
 */
fun ByteBuffer.ip4(value: Int) {
    put(value.toByte())
    put((value shr 8).toByte())
    put((value shr 16).toByte())
    put((value shr 24).toByte())
}

/**
 * Puts a 4-byte int in mixed order [2][3][0][1].
 * @param value the value to put.
 */
fun ByteBuffer.mp4(value: Int) {
    put((value shr 16).toByte())
    put((value shr 24).toByte())
    put(value.toByte())
    put((value shr 8).toByte())
}

/**
 * Puts a 4-byte int in mixed order [1][0][3][2].
 * @param value the value to put.
 */
fun ByteBuffer.imp4(value: Int) {
    put((value shr 8).toByte())
    put(value.toByte())
    put((value shr 24).toByte())
    put((value shr 16).toByte())
}

/**
 * Puts an 8-byte long (big-endian).
 * @param value the value to put.
 */
fun ByteBuffer.p8(value: Long) {
    put((value shr 56).toByte())
    put((value shr 48).toByte())
    put((value shr 40).toByte())
    put((value shr 32).toByte())
    put((value shr 24).toByte())
    put((value shr 16).toByte())
    put((value shr 8).toByte())
    put(value.toByte())
}

/**
 * Puts a variable-length int using varint encoding.
 * @param value the value to put.
 */
fun ByteBuffer.pVarInt(value: Int) {
    var v = value
    if ((v and 0x7F.inv()) != 0) {
        if ((v and 0x3FFF.inv()) != 0) {
            if ((v and 0x1FFFFF.inv()) != 0) {
                if ((v and (-268435456).inv()) != 0) {
                    p1((v shr 28) or 0x80)
                }
                p1((v shr 21) or 0x80)
            }
            p1((v shr 14) or 0x80)
        }
        p1((v shr 7) or 0x80)
    }
    p1(v and 0x7F)
}

/**
 * Puts a size byte at position relative to buffer.
 * @param length the length to write.
 */
fun ByteBuffer.psize(length: Int) {
    put(position() - length - 1, length.toByte())
}

/**
 * Puts a size byte + 128 at position relative to buffer.
 * @param length the length to write.
 */
fun ByteBuffer.psizeadd(length: Int) {
    put(position() - length - 1, (length + 128).toByte())
}

/**
 * Reads an unsigned byte.
 * @return the unsigned byte as Int.
 */
fun ByteBuffer.g1(): Int = get().toInt() and 0xFF

/**
 * Reads a signed byte.
 * @return the signed byte as Int.
 */
fun ByteBuffer.g1b(): Int = get().toInt()

/**
 * Reads a byte and subtracts 128.
 * @return the result as Int.
 */
fun ByteBuffer.g1add(): Int = (get().toInt() - 128) and 0xFF

/**
 * Reads a negated byte.
 * @return the negated byte as Int.
 */
fun ByteBuffer.g1neg(): Int = -(get().toInt() and 0xFF)

/**
 * Reads a byte and returns 128 - byte.
 * @return the result as Int.
 */
fun ByteBuffer.g1sub(): Int = (128 - (get().toInt() and 0xFF)) and 0xFF

/**
 * Reads an unsigned short (big-endian).
 * @return the unsigned short as Int.
 */
fun ByteBuffer.g2(): Int = ((get().toInt() and 0xFF) shl 8) or (get().toInt() and 0xFF)

/**
 * Reads a short where second byte is subtracted by 128.
 * @return the result as Int.
 */
fun ByteBuffer.g2add(): Int = ((get().toInt() and 0xFF) shl 8) or ((get().toInt() - 128) and 0xFF)

/**
 * Reads a signed short (big-endian).
 * @return the signed short as Int.
 */
fun ByteBuffer.g2b(): Int {
    val value = ((get().toInt() and 0xFF) shl 8) or (get().toInt() and 0xFF)
    return if (value > 32767) value - 0x10000 else value
}

/**
 * Reads an unsigned short (little-endian).
 * @return the unsigned short as Int.
 */
fun ByteBuffer.ig2(): Int = (get().toInt() and 0xFF) or ((get().toInt() and 0xFF) shl 8)

/**
 * Reads a short (little-endian) with first byte subtracted by 128.
 * @return the result as Int.
 */
fun ByteBuffer.ig2add(): Int = ((get().toInt() - 128) and 0xFF) or ((get().toInt() and 0xFF) shl 8)

/**
 * Reads a 3-byte unsigned int (big-endian).
 * @return the unsigned int as Int.
 */
fun ByteBuffer.g3(): Int = ((get().toInt() and 0xFF) shl 16) or
        ((get().toInt() and 0xFF) shl 8) or
        (get().toInt() and 0xFF)

/**
 * Reads a 3-byte unsigned int (little-endian).
 * @return the unsigned int as Int.
 */
fun ByteBuffer.ig3(): Int = (get().toInt() and 0xFF) or
        ((get().toInt() and 0xFF) shl 8) or
        ((get().toInt() and 0xFF) shl 16)

/**
 * Reads a 4-byte int (big-endian).
 * @return the int value.
 */
fun ByteBuffer.g4(): Int = ((get().toInt() and 0xFF) shl 24) or
        ((get().toInt() and 0xFF) shl 16) or
        ((get().toInt() and 0xFF) shl 8) or
        (get().toInt() and 0xFF)

/**
 * Reads a 4-byte int (little-endian).
 * @return the int value.
 */
fun ByteBuffer.ig4(): Int = (get().toInt() and 0xFF) or
        ((get().toInt() and 0xFF) shl 8) or
        ((get().toInt() and 0xFF) shl 16) or
        ((get().toInt() and 0xFF) shl 24)

/**
 * Reads a 4-byte int in mixed order [2][3][0][1].
 * @return the int value.
 */
fun ByteBuffer.m4(): Int = ((get().toInt() and 0xFF) shl 16) or
        ((get().toInt() and 0xFF) shl 24) or
        (get().toInt() and 0xFF) or
        ((get().toInt() and 0xFF) shl 8)

/**
 * Reads a 4-byte int in mixed order [1][0][3][2].
 * @return the int value.
 */
fun ByteBuffer.im4(): Int = ((get().toInt() and 0xFF) shl 8) or
        (get().toInt() and 0xFF) or
        ((get().toInt() and 0xFF) shl 24) or
        ((get().toInt() and 0xFF) shl 16)

/**
 * Puts a specified number of bits from a value into the buffer.
 *
 * @param numBitsInput number of bits to write
 * @param valueInput bits to write
 * @return this buffer
 */
fun ByteBuffer.putBits(numBitsInput: Int, valueInput: Int): ByteBuffer {
    var numBits = numBitsInput
    var value = valueInput

    var bitPos = bitPositions[this] ?: 0
    var bytePos = bitPos shr 3
    var bitOffset = 8 - (bitPos and 7)
    bitPos += numBits
    bitPositions[this] = bitPos

    while (numBits > bitOffset) {
        val b = get(bytePos).toInt() and 0xFF
        val cleared = b and BIT_MASK[bitOffset].inv()
        val toWrite = (value shr (numBits - bitOffset)) and BIT_MASK[bitOffset]
        put(bytePos, (cleared or toWrite).toByte())
        bytePos++
        numBits -= bitOffset
        bitOffset = 8
    }

    val b = get(bytePos).toInt() and 0xFF
    if (numBits == bitOffset) {
        val cleared = b and BIT_MASK[bitOffset].inv()
        val toWrite = value and BIT_MASK[bitOffset]
        put(bytePos, (cleared or toWrite).toByte())
    } else {
        val cleared = b and (BIT_MASK[numBits] shl (bitOffset - numBits)).inv()
        val toWrite = (value and BIT_MASK[numBits]) shl (bitOffset - numBits)
        put(bytePos, (cleared or toWrite).toByte())
    }
    return this
}

/**
 * Reads bits.
 * @param numBits number of bits to read.
 * @return bits read as Int.
 */
fun ByteBuffer.readBits(numBits: Int): Int {
    var value = 0
    var bitsLeft = numBits
    var bitPos = bitPositions[this] ?: 0

    while (bitsLeft > 0) {
        val bytePos = bitPos shr 3
        val bitOffset = 8 - (bitPos and 7)
        val bitsToRead = if (bitsLeft < bitOffset) bitsLeft else bitOffset
        val mask = BIT_MASK[bitsToRead]

        val currentByte = get(bytePos).toInt() and 0xFF
        val shifted = currentByte shr (bitOffset - bitsToRead)
        value = (value shl bitsToRead) or (shifted and mask)

        bitPos += bitsToRead
        bitsLeft -= bitsToRead
    }

    bitPositions[this] = bitPos
    position((bitPos + 7) shr 3)
    return value
}

/**
 * Puts a null-terminated string.
 * @param value the string to write.
 */
fun ByteBuffer.pjstr(value: String) {
    for (ch in value) {
        put(ch.code.toByte())
    }
    put(0)
}

/**
 * Switches the buffer to bit access mode.
 */
fun ByteBuffer.bitAccess(): ByteBuffer {
    bitPositions[this] = position() * 8
    return this
}

/**
 * Switches the buffer back to byte access mode.
 */
fun ByteBuffer.byteAccess(): ByteBuffer {
    val pos = bitPositions[this] ?: 0
    position((pos + 7) / 8)
    return this
}

/**
 * Finishes bit access.
 */
fun ByteBuffer.finishBitAccess() {
    val pos = bitPositions[this] ?: 0
    position((pos + 7) shr 3)
}

/**
 * Writes an int using smart encoding (1 or 2 bytes depending on value).
 * @param value The value to write.
 */
fun ByteBuffer.psmarts(value: Int) {
    when (value) {
        in 0 until 128 -> this.p1(value)
        in 0 until 0x8000 -> this.p2(value + 0x8000)
        else -> throw IllegalArgumentException("smart out of range: $value")
    }
}

/**
 * Writes bytes in reverse order.
 * @param data The byte array to write.
 * @param start Start index in the array.
 * @param length Number of bytes to write.
 */
fun ByteBuffer.putReverse(data: ByteArray, start: Int, length: Int) {
    for (i in (start + length - 1) downTo start) {
        this.put(data[i])
    }
}

/**
 * Writes a byte with A-type transformation (value + 128 mod 256).
 *
 * @param value The byte value to transform and write.
 */
fun ByteBuffer.putA(value: Byte) {
    this.put((value.toInt() + 128).toByte())
}

/**
 * Writes bytes in reverse order with A-type transformation.
 *
 * @param data The byte array to write.
 * @param start Start index in the array.
 * @param length Number of bytes to write.
 */
fun ByteBuffer.putReverseA(data: ByteArray, start: Int, length: Int) {
    for (i in (start + length - 1) downTo start) {
        putA(data[i])
    }
}