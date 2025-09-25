package core.cache

import com.displee.cache.CacheLibrary
import core.cache.def.impl.LinkedScripts
import core.cache.def.impl.ScriptArgs

class InterfacePacking(private val cacheLibrary: CacheLibrary) {

    fun packInterfaces() {
        val cachePacking = CachePacking(cacheLibrary)

        val scripts = LinkedScripts().apply {
            onMouseOver = ScriptArgs(1357, arrayOf(-2147483645, 100))
            onMouseLeave = ScriptArgs(1501, arrayOf(-2147483645, 49938533))
            onOptionClick = ScriptArgs(1722, arrayOf(-2147483645, -2147483643, 1433, 1431, 10))
            onMouseRepeat = ScriptArgs(38, arrayOf(-2147483645, 49938533, "Switch to primary/secondary bank", 25, 150))
        }
        cachePacking.addGraphicComponent(
            def = 762,
            index = 103,
            x = 332,
            y = 287,
            width = 35,
            height = 35,
            overlay = 49938493,
            spriteId = 1431,
            scripts = scripts
        )
    }
}
