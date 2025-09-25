package core.cache

import com.displee.cache.CacheLibrary
import com.displee.cache.index.Index
import com.displee.cache.index.archive.Archive
import core.api.log
import core.cache.def.impl.ComponentType
import core.cache.def.impl.IfaceDefinition
import core.tools.Log

class CachePacking(private val cache: CacheLibrary) {

    fun addGraphicComponent(
        def: Int,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        index: Int,
        overlay: Int = -1,
        spriteId: Int = -1,
        ops: Array<String?>? = emptyArray()
    ): IfaceDefinition {
        val root = IfaceDefinition.forId(def) ?: error("Interface $def not found")

        val newSprite = IfaceDefinition().apply {
            this.id = (def shl 16) + index
            this.parent = def
            this.version = 3
            this.type = ComponentType.SPRITE

            this.baseX = x
            this.baseY = y
            this.baseWidth = width
            this.baseHeight = height

            this.overlayer = overlay
            this.spriteId = spriteId
            this.activeSpriteId = spriteId
            this.spriteTiling = false
            this.hasAlpha = false
            this.alpha = 0
            this.outlineThickness = 0
            this.shadowColor = 0
            this.hFlip = false
            this.vFlip = false
        }

        val currentChildren = root.children ?: arrayOfNulls<IfaceDefinition>(index + 1)
        val updatedChildren = Array(maxOf(currentChildren.size, index + 1)) { i ->
            currentChildren.getOrNull(i)
        }
        updatedChildren[index] = newSprite
        root.children = updatedChildren

        log(this.javaClass, Log.INFO, "New sprite added: baseWidth=${newSprite.baseWidth}, baseHeight=${newSprite.baseHeight}, spriteId=${newSprite.spriteId}")

        saveComponent(def, index, newSprite)
        return newSprite
    }

    /**
     * Encodes and writes a single child component into the cache.
     *
     * @param ifaceId The id of the parent interface.
     * @param childIndex The child index within the interface.
     * @param def The [IfaceDefinition] to save.
     */
    private fun saveComponent(ifaceId: Int, childIndex: Int, def: IfaceDefinition) {
        val encodedBytes = IfaceDefinition.encode(def)

        val index: Index = cache.index(CacheIndex.COMPONENTS.id)
        val archive: Archive = index.archive(ifaceId) ?: index.add(ifaceId)

        archive.add(childIndex, encodedBytes, overwrite = true)
        index.update()
        cache.update()

        log(
            this.javaClass,
            Log.INFO,
            "Saved child component: interface=$ifaceId, index=$childIndex, type=${def.type}, spriteId=${def.spriteId}"
        )
    }
}
