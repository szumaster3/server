package core.game.world.objectparser

import core.ServerConstants
import core.api.StartupListener
import core.api.log
import core.game.node.scenery.Scenery
import core.game.world.map.build.LandscapeParser
import core.tools.Log
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class ObjectParser : StartupListener {

    fun parseObjects() {
        if (ServerConstants.OBJECT_PARSER_PATH == null) return
        val f = File(ServerConstants.OBJECT_PARSER_PATH)
        if (!f.exists()) {
            return
        }

        try {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val doc = builder.parse(f)

            val parseList = doc.getElementsByTagName("ObjectAction")

            var added = 0
            var removed = 0

            for (i in 0 until parseList.length) {
                val parseNode = parseList.item(i)
                if (parseNode.nodeType == Node.ELEMENT_NODE) {
                    val parseElement = parseNode as Element
                    val type = parseElement.getAttribute("mode")
                    when (type) {
                        "add" -> {
                            val id = parseElement.getAttribute("id").toInt()
                            val x = parseElement.getAttribute("x").toInt()
                            val y = parseElement.getAttribute("y").toInt()
                            val z = parseElement.getAttribute("z").toInt()
                            var objType = 10
                            if (parseElement.hasAttribute("type")) {
                                objType = parseElement.getAttribute("type").toInt()
                            }
                            val rawDir = parseElement.getAttribute("direction")
                            val dir = when (rawDir) {
                                "n"  -> 1
                                "ne" -> 2
                                "nw" -> 0
                                "w"  -> 3
                                "e"  -> 4
                                "sw" -> 5
                                "se" -> 7
                                "s"  -> 6
                                else -> 1
                            }
                            added++
                            LandscapeParser.addScenery(Scenery(id, x, y, z, objType, dir))
                        }
                        "remove" -> {
                            val id = parseElement.getAttribute("id").toInt()
                            val x = parseElement.getAttribute("x").toInt()
                            val y = parseElement.getAttribute("y").toInt()
                            val z = parseElement.getAttribute("z").toInt()
                            var objType = 10
                            if (parseElement.hasAttribute("type")) {
                                objType = parseElement.getAttribute("type").toInt()
                            }
                            removed++
                            LandscapeParser.removeScenery(Scenery(id, x, y, z, objType, 0))
                        }
                    }
                }
            }

            log(this::class.java, Log.INFO, "Object parser: added=$added, removed=$removed.")

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun startup() {
        parseObjects()
    }
}