package core.cache

import com.displee.cache.CacheLibrary
import com.displee.cache.index.Index
import com.displee.cache.index.archive.Archive
import core.api.log
import core.cache.def.impl.ComponentType
import core.cache.def.impl.IfaceDefinition
import core.cache.def.impl.LinkedScripts
import core.tools.Log

class CachePacking(private val cache: CacheLibrary) {

    fun addGraphicComponent(
        def: Int,
        index: Int,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        overlay: Int = -1,
        spriteId: Int = -1,
        scripts: LinkedScripts? = null,
        alpha: Int = 0,
        outline: Int = 0,
        hFlip: Boolean = false,
        vFlip: Boolean = false,
        spriteTiling: Boolean = false,
        shadowColor: Int = 0
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
            this.spriteTiling = spriteTiling
            this.hasAlpha = alpha > 0
            this.alpha = alpha
            this.outlineThickness = outline
            this.shadowColor = shadowColor
            this.hFlip = hFlip
            this.vFlip = vFlip
            this.scripts = scripts
        }

        val currentChildren = root.children ?: arrayOfNulls<IfaceDefinition>(index + 1)
        val updatedChildren = Array(maxOf(currentChildren.size, index + 1)) { i ->
            currentChildren.getOrNull(i)
        }
        updatedChildren[index] = newSprite
        root.children = updatedChildren
        saveComponent(def, index, newSprite)
        return newSprite
    }

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
