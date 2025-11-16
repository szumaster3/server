package core.cache.misc

import core.cache.bzip2.BZip2Decompressor
import core.cache.gzip.GZipDecompressor
import java.nio.ByteBuffer
import java.util.zip.CRC32

class ContainersInformation(packedData: ByteArray) {

    val data: ByteArray = packedData.copyOf()
    val informationContainer: Container = Container()
    var protocol: Int = 0
        private set

    var revision: Int = 0
        private set

    lateinit var containersIndexes: IntArray
        private set

    lateinit var containers: Array<FilesContainer>
        private set

    var filesNamed: Boolean = false
        private set

    var whirpool: Boolean = false
        private set

    init {
        informationContainer.version =
            ((packedData[packedData.size - 2].toInt() shl 8) and 0xff00) +
                    (packedData[packedData.size - 1].toInt() and 0xff)
        val crc32 = CRC32()
        crc32.update(packedData)
        informationContainer.crc = crc32.value.toInt()
        decodeContainersInformation(unpackCacheContainer(packedData) ?: ByteArray(0))
    }

    companion object {
        fun unpackCacheContainer(packedData: ByteArray): ByteArray? {
            val buffer = ByteBuffer.wrap(packedData)
            val compression = buffer.get().toInt() and 0xFF
            val containerSize = buffer.int
            if (containerSize < 0 || containerSize > 5_000_000) return null

            if (compression == 0) {
                val unpacked = ByteArray(containerSize)
                buffer.get(unpacked)
                return unpacked
            }

            val decompressedSize = buffer.int
            if (decompressedSize < 0 || decompressedSize > 20_000_000) return null

            val decompressedData = ByteArray(decompressedSize)
            when (compression) {
                1 -> BZip2Decompressor.decompress(decompressedData, packedData, containerSize, 9)
                else -> GZipDecompressor.decompress(buffer, decompressedData)
            }
            return decompressedData
        }
    }

    private fun decodeContainersInformation(data: ByteArray) {
        val buffer = ByteBuffer.wrap(data)
        protocol = buffer.get().toInt() and 0xFF
        if (protocol !in 5..6) throw RuntimeException("Unsupported protocol: $protocol")
        revision = if (protocol < 6) 0 else buffer.int
        val hash = buffer.get().toInt() and 0xFF
        filesNamed = (hash and 0x1) != 0
        whirpool = (hash and 0x2) != 0

        containersIndexes = IntArray(buffer.short.toInt() and 0xFFFF)
        var lastIndex = -1
        for (i in containersIndexes.indices) {
            containersIndexes[i] =
                (buffer.short.toInt() and 0xFFFF) + if (i == 0) 0 else containersIndexes[i - 1]
            if (containersIndexes[i] > lastIndex) lastIndex = containersIndexes[i]
        }

        containers = Array(lastIndex + 1) { FilesContainer() }
        for (i in containersIndexes.indices) containers[containersIndexes[i]] = FilesContainer()

        if (filesNamed) {
            for (i in containersIndexes.indices) {
                containers[containersIndexes[i]].nameHash = buffer.int
            }
        }

        var filesHashes: Array<ByteArray>? = null
        if (whirpool) {
            filesHashes = Array(containers.size) { ByteArray(64) }
            for (i in containersIndexes.indices) {
                buffer.get(filesHashes[containersIndexes[i]]!!)
            }
        }

        for (i in containersIndexes.indices) containers[containersIndexes[i]].crc = buffer.int
        for (i in containersIndexes.indices) containers[containersIndexes[i]].version = buffer.int
        for (i in containersIndexes.indices) {
            val fc = containers[containersIndexes[i]]
            fc.filesIndexes = IntArray(buffer.short.toInt() and 0xFFFF)
        }

        for (i in containersIndexes.indices) {
            val fc = containers[containersIndexes[i]]
            var lastFileIndex = -1
            for (j in fc.filesIndexes.indices) {
                fc.filesIndexes[j] =
                    (buffer.short.toInt() and 0xFFFF) + if (j == 0) 0 else fc.filesIndexes[j - 1]
                if (fc.filesIndexes[j] > lastFileIndex) lastFileIndex = fc.filesIndexes[j]
            }
            fc.files = Array(lastFileIndex + 1) { Container() }
        }

        if (whirpool) {
            for (i in containersIndexes.indices) {
                val fc = containers[containersIndexes[i]]
                for (j in fc.filesIndexes.indices) {
                    fc.files[fc.filesIndexes[j]].version =
                        filesHashes!![containersIndexes[i]][fc.filesIndexes[j]].toInt()
                }
            }
        }

        if (filesNamed) {
            for (i in containersIndexes.indices) {
                val fc = containers[containersIndexes[i]]
                for (j in fc.filesIndexes.indices) {
                    fc.files[fc.filesIndexes[j]].nameHash = buffer.int
                }
            }
        }
    }
}
