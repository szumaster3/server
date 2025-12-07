package core.cache

import core.ServerConstants
import core.api.log
import core.cache.def.impl.*
import core.tools.Log
import core.tools.SystemLogger.logCache
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.ByteBuffer

object Cache {
    private lateinit var cacheFileManagers: Array<CacheFileManager?>
    private var referenceFile: CacheFile? = null

    @JvmStatic
    @Throws(Throwable::class)
    fun init(path: String = ServerConstants.CACHE_PATH!!, writable: Boolean = false) {
        logCache("Cache initialization started [readOnly=${!writable}].")
        val mode = if (writable) "rw" else "r"
        val buffer = ByteArray(520)

        val idx255 = File(path, "main_file_cache.idx255")
        val dat2 = File(path, "main_file_cache.dat2")

        if (!idx255.exists() || !dat2.exists()) {
            throw FileNotFoundException("Cache files not found in: $path")
        }

        val idx255File = RandomAccessFile(idx255, mode)
        val dat2File = RandomAccessFile(dat2, mode)

        referenceFile = CacheFile(255, idx255File, dat2File, 500_000, buffer)

        val length = (idx255File.length() / 6).toInt()
        cacheFileManagers = arrayOfNulls(length)

        for (i in 0 until length) {
            val idxFile = File(path, "main_file_cache.idx$i")
            if (idxFile.exists() && idxFile.length() > 0) {
                try {
                    val raf = RandomAccessFile(idxFile, mode)
                    val cacheFile = CacheFile(i, raf, dat2File, 1_000_000, buffer)
                    val manager = CacheFileManager(cacheFile, true)
                    if (manager.data == null) {
                        cacheFileManagers[i] = null
                        log(Cache::class.java, Log.ERR, "Error loading cache index $i: no information.")
                    } else {
                        cacheFileManagers[i] = manager
                    }
                } catch (e: IOException) {
                    log(Cache::class.java, Log.ERR, "Failed to load cache index $i: ${e.message}")
                    cacheFileManagers[i] = null
                }
            }
        }

        try {
            ItemDefinition.parse()
            SceneryDefinition.parse()
        } catch (t: Throwable) {
            log(Cache::class.java, Log.WARN, "Definition parsing failed: ${t.message}")
        }

        log(Cache::class.java, Log.FINE, "Cache initialization complete.")
    }

    @JvmStatic
    fun getIndexes(): Array<CacheFileManager?> = cacheFileManagers

    @JvmStatic
    fun getReferenceFile(): CacheFile? = referenceFile

    @JvmStatic
    fun getArchiveData(index: Int, archive: Int, priority: Boolean, encryptionValue: Int): ByteBuffer? {
        val data = if (index == 255) referenceFile?.getContainerData(archive)
        else cacheFileManagers.getOrNull(index)?.cacheFile?.getContainerData(archive)

        if (data == null || data.isEmpty()) {
            log(Cache::class.java, Log.ERR, "Invalid JS-5 request - $index, $archive, $priority, $encryptionValue!")
            return null
        }

        val compression = data[0].toInt() and 0xFF
        val length = ((data[1].toInt() and 0xFF) shl 24) or
                ((data[2].toInt() and 0xFF) shl 16) or
                ((data[3].toInt() and 0xFF) shl 8) or
                (data[4].toInt() and 0xFF)

        var settings = compression
        if (!priority) settings = settings or 0x80

        var realLength = if (compression != 0) length + 4 else length
        if (index != 255 && compression != 0 && data.size - length == 9) realLength += 2

        val estimate = (realLength + 5) + (realLength / 512) + 10
        val bufferOut = ByteBuffer.allocate(estimate)
        bufferOut.put(index.toByte())
        bufferOut.putShort(archive.toShort())
        bufferOut.put(settings.toByte())
        bufferOut.putInt(length)

        for (i in 5 until realLength + 5) {
            if (bufferOut.position() % 512 == 0) bufferOut.put(255.toByte())
            bufferOut.put(if (i < data.size) data[i] else 0)
        }

        if (encryptionValue != 0) {
            for (i in 0 until bufferOut.position()) {
                bufferOut.put(i, bufferOut.get(i).toInt().xor(encryptionValue).toByte())
            }
        }

        bufferOut.flip()
        return bufferOut
    }

    @JvmStatic
    fun generateReferenceData(): ByteArray {
        val buf = ByteBuffer.allocate(cacheFileManagers.size * 8)
        for ((index, manager) in cacheFileManagers.withIndex()) {
            if (manager?.data == null) {
                buf.putInt(if (index == 24) 609_698_396 else 0)
                buf.putInt(0)
            } else {
                buf.putInt(manager.data.informationContainer.crc)
                buf.putInt(manager.data.revision)
            }
        }
        return buf.array()
    }

    @JvmStatic fun getInterfaceDefinitionsComponentsSize(interfaceId: Int) = getIndexes()[3]?.getFilesSize(interfaceId) ?: 0
    @JvmStatic fun getInterfaceDefinitionsSize() = getIndexes()[3]?.containersSize ?: 0
    @JvmStatic fun getNPCDefinitionsSize(): Int {
        val lastContainerId = getIndexes()[18]?.containersSize?.minus(1) ?: 0
        return lastContainerId * 128 + (getIndexes()[18]?.getFilesSize(lastContainerId) ?: 0)
    }
    @JvmStatic fun getGraphicDefinitionsSize(): Int {
        val lastContainerId = getIndexes()[21]?.containersSize?.minus(1) ?: 0
        return lastContainerId * 256 + (getIndexes()[21]?.getFilesSize(lastContainerId) ?: 0)
    }
    @JvmStatic fun getAnimationDefinitionsSize(): Int {
        val lastContainerId = getIndexes()[20]?.containersSize?.minus(1) ?: 0
        return lastContainerId * 128 + (getIndexes()[20]?.getFilesSize(lastContainerId) ?: 0)
    }
    @JvmStatic fun getObjectDefinitionsSize(): Int {
        val lastContainerId = getIndexes()[16]?.containersSize?.minus(1) ?: 0
        return lastContainerId * 256 + (getIndexes()[16]?.getFilesSize(lastContainerId) ?: 0)
    }
    @JvmStatic fun getItemDefinitionsSize(): Int {
        val lastContainerId = getIndexes()[19]?.containersSize?.minus(1) ?: 0
        return lastContainerId * 256 + (getIndexes()[19]?.getFilesSize(lastContainerId) ?: 0)
    }

    @JvmStatic
    fun getRenderAnimationDefinitionsSize(): Int {
        val idx = 2
        val containerId = 32
        return getIndexes()[idx]?.getFilesSize(containerId) ?: 0
    }
}
