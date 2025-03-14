package content.region.kandarin.quest.grandtree.handlers

import content.global.handlers.iface.ScrollLine
import content.region.kandarin.quest.grandtree.dialogue.FemiCartDialogueFile
import core.api.*
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.tools.GREEN
import core.tools.RED
import org.rs.consts.Items

object TheGrandTreeUtils {
    const val TWIG_0 = "/save:grandtree:twig:0"
    const val TWIG_1 = "/save:grandtree:twig:1"
    const val TWIG_2 = "/save:grandtree:twig:2"
    const val TWIG_3 = "/save:grandtree:twig:3"

    const val DRACONIA_ROCK = "grandtree:rock"

    const val FEMI_HELP_TRUE = "/save:grandtree:free_gate"
    const val FEMI_TALK = "/save:grandtree:femi_talked"

    val ROOTS_LOCATION =
        arrayOf(
            Location(2467, 9896, 0),
            Location(2468, 9890, 0),
            Location(2465, 9891, 0),
            Location(2465, 9891, 0),
            Location(2473, 9897, 0),
        )

    val KARAMJA_GATE = intArrayOf(2438, 2439)
    const val HAZELMERE_SCROLL = Items.HAZELMERES_SCROLL_786
    const val LUMBER_ORDER_SCROLL = Items.LUMBER_ORDER_787
    const val INVASION_PLANS_SCROLL = Items.INVASION_PLANS_794

    val HAZELMERE_SCROLL_CONTENT =
        arrayOf(
            "<col=FFF900>Es lemanto meso pro eis prit ta Cinqo mond.</col>",
            "<col=FFF900>Mi lovos ta lemanto Daconia arpos</col>",
            "<col=FFF900>et Daconia arpos eto meriz ta priw!</col>",
        )

    val LUMBER_ORDER_SCROLL_CONTENT =
        arrayOf(
            RED + "Karamja shipyard                                ",
            "Order                                          ",
            "Order for 30 Karamja battleships                ",
            "Lumber required: 2000                         ",
            "Troop capacity: 300                             ",
        )

    val INVASION_PLANS_SCROLL_CONTENT =
        arrayOf(
            ScrollLine("${RED}Invasion", 3),
            ScrollLine("Troops board three fleets of battleships at Karamja.", 5),
            ScrollLine("${GREEN}Fleet 1", 6),
            ScrollLine("Attacks Misthalin from the south.", 7),
            ScrollLine("${GREEN}Fleet 2", 8),
            ScrollLine("Groups at Crandor and attacks Asgarnia from the west.", 9),
            ScrollLine("${GREEN}Fleet 3", 10),
            ScrollLine("Sails north to attack Kandarin from south, reinforced", 11),
            ScrollLine("by gnome foot soldiers leaving gnome stronghold.", 12),
            ScrollLine("${RED}Take no prisoners!", 14),
        )

    fun sneakIn(player: Player) {
        lock(player, 1000)
        lockInteractions(player, 1000)
        player.walkingQueue.reset()
        GameWorld.Pulser.submit(
            object : Pulse() {
                var counter = 0

                override fun pulse(): Boolean {
                    when (counter++) {
                        0 -> {
                            player.walkingQueue.addPath(2461, 3379, true)
                        }

                        3 -> {
                            player.faceLocation(Location(2458, 3379, 0))
                            animate(player, 828)
                        }

                        5 -> {
                            teleport(player, Location(2458, 3408, 0))
                            openDialogue(player, FemiCartDialogueFile())
                            unlock(player)
                        }
                    }
                    return false
                }
            },
        )
    }
}
