package core.cache.secure

import java.nio.ByteBuffer

/**
 * XTEA encryption & decryption utilities.
 *
 * @author Emperor
 */
object XTEACryption {
    private const val DELTA: Int = -1640531527
    private const val SUM: Int = -957401312
    private const val NUM_ROUNDS: Int = 32
    private const val GOLDEN_RATIO = -0x61c88647

    /**
     * Decrypts a byte array using XTEA.
     *
     * @param buffer Data to decrypt.
     * @param key Key (must have 4 ints).
     * @param start Start index (default 0).
     */
    @JvmStatic
    fun decipher(
        buffer: ByteArray,
        key: IntArray,
        start: Int = 0,
    ) {
        if (key.size != 4) {
            throw IllegalArgumentException("Key must contain exactly 4 integers.")
        }

        val numQuads = buffer.size / 8
        for (i in 0 until numQuads) {
            var sum = GOLDEN_RATIO * NUM_ROUNDS
            var v0 = getInt(buffer, start + i * 8)
            var v1 = getInt(buffer, start + i * 8 + 4)
            for (j in 0 until NUM_ROUNDS) {
                v1 -= (v0 shl 4 xor v0.ushr(5)) + v0 xor sum + key[sum.ushr(11) and 3]
                sum -= GOLDEN_RATIO
                v0 -= (v1 shl 4 xor v1.ushr(5)) + v1 xor sum + key[sum and 3]
            }
            putInt(buffer, start + i * 8, v0)
            putInt(buffer, start + i * 8 + 4, v1)
        }
    }

    /**
     * Encrypts a byte array using XTEA.
     *
     * @param buffer Data to encrypt.
     * @param start Start index.
     * @param end End index (exclusive).
     * @param key Key (must have 4 ints).
     */
    @JvmStatic
    fun encipher(
        buffer: ByteArray,
        start: Int,
        end: Int,
        key: IntArray,
    ) {
        if (key.size != 4) {
            throw IllegalArgumentException("Key must contain exactly 4 integers.")
        }

        val numQuads = (end - start) / 8
        for (i in 0 until numQuads) {
            var sum = 0
            var v0 = getInt(buffer, start + i * 8)
            var v1 = getInt(buffer, start + i * 8 + 4)
            for (j in 0 until NUM_ROUNDS) {
                v0 += (v1 shl 4 xor v1.ushr(5)) + v1 xor sum + key[sum and 3]
                sum += GOLDEN_RATIO
                v1 += (v0 shl 4 xor v0.ushr(5)) + v0 xor sum + key[sum.ushr(11) and 3]
            }
            putInt(buffer, start + i * 8, v0)
            putInt(buffer, start + i * 8 + 4, v1)
        }
    }

    /**
     * Gets an int from a byte array at index.
     */
    private fun getInt(buffer: ByteArray, index: Int): Int =
        (buffer[index].toInt() and 0xff shl 24) or
                (buffer[index + 1].toInt() and 0xff shl 16) or
                (buffer[index + 2].toInt() and 0xff shl 8) or
                (buffer[index + 3].toInt() and 0xff)

    /**
     * Puts an int into a byte array at index.
     */
    private fun putInt(buffer: ByteArray, index: Int, value: Int) {
        buffer[index] = (value shr 24).toByte()
        buffer[index + 1] = (value shr 16).toByte()
        buffer[index + 2] = (value shr 8).toByte()
        buffer[index + 3] = value.toByte()
    }

    /**
     * Decrypts a ByteBuffer.
     */
    @JvmStatic
    fun decrypt(keys: IntArray, buffer: ByteBuffer): ByteBuffer =
        decrypt(keys, buffer, buffer.position(), buffer.limit())

    /**
     * Decrypts a ByteBuffer from offset to length.
     */
    @JvmStatic
    fun decrypt(
        keys: IntArray,
        buffer: ByteBuffer,
        offset: Int,
        length: Int,
    ): ByteBuffer {
        val numBlocks = (length - offset) / 8
        val block = IntArray(2)
        for (i in 0 until numBlocks) {
            val index = i * 8 + offset
            block[0] = buffer.getInt(index)
            block[1] = buffer.getInt(index + 4)
            decipher(keys, block)
            buffer.putInt(index, block[0])
            buffer.putInt(index + 4, block[1])
        }
        return buffer
    }

    /**
     * Deciphers a 2-int block with keys.
     */
    private fun decipher(keys: IntArray, block: IntArray) {
        var sum: Long = SUM.toLong()
        for (i in 0 until NUM_ROUNDS) {
            block[1] -= (keys[((sum and 0x1933L) ushr 11).toInt()] + sum xor (block[0] + (block[0] shl 4 xor (block[0] ushr 5))).toLong()).toInt()
            sum -= DELTA.toLong()
            block[0] -= (((block[1] shl 4 xor (block[1] ushr 5)) + block[1]).toLong() xor keys[(sum and 0x3L).toInt()] + sum).toInt()
        }
    }

    /**
     * Encrypts a ByteBuffer.
     */
    @JvmStatic
    fun encrypt(keys: IntArray, buffer: ByteBuffer) {
        encrypt(keys, buffer, buffer.position(), buffer.limit())
    }

    /**
     * Encrypts a ByteBuffer from offset to length.
     */
    @JvmStatic
    fun encrypt(
        keys: IntArray,
        buffer: ByteBuffer,
        offset: Int,
        length: Int,
    ) {
        val numBlocks = (length - offset) / 8
        val block = IntArray(2)
        for (i in 0 until numBlocks) {
            val index = i * 8 + offset
            block[0] = buffer.getInt(index)
            block[1] = buffer.getInt(index + 4)
            encipher(keys, block)
            buffer.putInt(index, block[0])
            buffer.putInt(index + 4, block[1])
        }
    }

    /**
     * Enciphers a 2-int block with keys.
     */
    private fun encipher(keys: IntArray, block: IntArray) {
        var sum: Long = 0
        for (i in 0 until NUM_ROUNDS) {
            block[0] += (((block[1] shl 4 xor (block[1] ushr 5)) + block[1]).toLong() xor keys[(sum and 0x3L).toInt()] + sum).toInt()
            sum += DELTA.toLong()
            block[1] += (keys[((sum and 0x1933L) ushr 11).toInt()] + sum xor (block[0] + (block[0] shl 4 xor (block[0] ushr 5))).toLong()).toInt()
        }
    }
}
