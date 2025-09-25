package core.cache

import com.displee.cache.CacheLibrary

class InterfacePacking(private val cacheLibrary: CacheLibrary) {

    fun packInterfaces() {
        val cachePacking = CachePacking(cacheLibrary)

        cachePacking.addGraphicComponent(
            def = 762,
            x = 332,
            y = 287,
            width = 35,
            height = 35,
            index = 103,
            overlay = -1,
            spriteId = 0,
        )
    }
}
