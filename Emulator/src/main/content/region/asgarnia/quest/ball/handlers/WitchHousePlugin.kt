package content.region.asgarnia.quest.ball.handlers

import core.api.*
import core.game.interaction.NodeUsageEvent
import core.game.interaction.OptionHandler
import core.game.interaction.UseWithHandler
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class WitchHousePlugin : OptionHandler() {
    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        return true
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        definePlugin(WitchHouseUseWithHandler())
        definePlugin(MouseNPC())
        return this
    }

    class WitchHouseUseWithHandler : UseWithHandler(Items.CHEESE_1985) {
        override fun newInstance(arg: Any?): Plugin<Any> {
            addHandler(org.rs.consts.Scenery.MOUSE_HOLE_15518, OBJECT_TYPE, this)
            addHandler(
                NPCs.MOUSE_901,
                NPC_TYPE,
                object : UseWithHandler(Items.MAGNET_2410) {
                    override fun newInstance(arg: Any?): Plugin<Any> {
                        addHandler(NPCs.MOUSE_901, NPC_TYPE, this)
                        return this
                    }

                    override fun handle(event: NodeUsageEvent): Boolean {
                        val player = event.player
                        val usedItem = event.usedItem
                        val npc = event.usedWith as NPC
                        checkNotNull(usedItem)
                        if (usedItem.id == Items.MAGNET_2410 && npc.id == NPCs.MOUSE_901 && player.getAttribute<Any?>("mouse_out") != null) {
                            sendDialogueLines(
                                player,
                                "You attach a magnet to the mouse's harness. The mouse finishes",
                                "the cheese and runs back into its hole. You hear some odd noises",
                                "from inside the walls. There is a strange whirring noise from above",
                                "the door frame.",
                            )
                            removeAttribute(player, "mouse_out")
                            if (removeItem(player, Items.MAGNET_2410, Container.INVENTORY)) setAttribute(
                                player, "attached_magnet", true
                            )
                        }
                        return true
                    }
                },
            )
            return this
        }

        override fun handle(event: NodeUsageEvent): Boolean {
            val player = event.player
            val used = event.usedItem
            val scenery = event.usedWith as Scenery
            checkNotNull(used)
            if (player.getAttribute<Any?>("mouse_out") != null && used.id == Items.CHEESE_1985 && scenery.id == 15518) {
                sendDialogue(player, "You can't do this right now.")
            }
            if (used.id == Items.CHEESE_1985 && scenery.id == org.rs.consts.Scenery.MOUSE_HOLE_15518 && player.getAttribute<Any?>(
                    "mouse_out"
                ) == null
            ) {
                if (removeItem(player, Items.CHEESE_1985, Container.INVENTORY)) sendDialogue(
                    player, "A mouse runs out of the hole."
                )
                val mouse = NPC.create(NPCs.MOUSE_901, Location.create(2903, 3466, 0)) as MouseNPC
                mouse.player = player
                mouse.isRespawn = false
                mouse.isWalks = true
                mouse.init()
                mouse.faceLocation(Location(2903, 3465, 0))
                setAttribute(player, "mouse_out", true)
            }
            return true
        }
    }

}
