package core.game.world.objectparser

import core.ServerConstants
import core.api.StartupListener
import core.game.node.scenery.Scenery
import core.game.world.map.build.LandscapeParser
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
                            var dir = 1
                            when (rawDir) {
                                "n" -> dir = 1
                                "ne" -> dir = 2
                                "nw" -> dir = 0
                                "w" -> dir = 3
                                "e" -> dir = 4
                                "sw" -> dir = 5
                                "se" -> dir = 7
                                "s" -> dir = 6
                            }
                            LandscapeParser.addScenery(
                                Scenery(
                                    id,
                                    x,
                                    y,
                                    z,
                                    objType,
                                    dir,
                                ),
                            )
                        }

                        "remove" -> {
                            val id = parseElement.getAttribute("id").toInt()
                            val x = parseElement.getAttribute("x").toInt()
                            val y = parseElement.getAttribute("y").toInt()
                            val z = parseElement.getAttribute("z").toInt()
                            val objType = 10
                            LandscapeParser.removeScenery(
                                Scenery(
                                    id,
                                    x,
                                    y,
                                    z,
                                ),
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun startup() {
        parseObjects()
    }
}
