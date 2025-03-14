package content.region.desert.dialogue.alkharid

import content.global.skill.agility.AgilityHandler
import core.api.sendDialogueLines
import core.api.sendItemDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.path.Pathfinder
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class ShantayGuardDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        if (args[0] != null && args[0] is NPC) {
            npc = (args[0] as NPC)
        }
        if (args.size == 2) {
            npc("Can I see your Desert Pass please?")
            stage = 13
            return true
        }
        if (npc != null && npc.id != 837) {
            npc(FaceAnim.HALF_GUILTY, "Hello there!")
            stage = 0
        } else {
            npc("Go talk to Shantay. I'm on duty and I don't have time", "to talk to the likes of you!")
            stage = 100
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.HALF_GUILTY, "What can I do for you?").also { stage++ }
            1 -> options("I'd like to go into the desert please.", "Nothing thanks.").also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "I'd like to go into the desert please.").also { stage = 10 }
                    2 -> end()
                }

            10 -> npc(FaceAnim.HALF_GUILTY, "Of course!").also { stage++ }
            11 ->
                if (!player.inventory.containsItem(PASS)) {
                    npc(
                        "You need a Shantay pass to get through this gate. See Shantay, he will sell you one for a very reasonable price.",
                    ).also {
                        stage = END_DIALOGUE
                    }
                } else {
                    npc("Can I see your Desert Pass please?").also { stage = 13 }
                }

            13 ->
                if (!player.inventory.containsItem(PASS)) {
                    player("Sorry, I don't have one with me.").also { stage = END_DIALOGUE }
                } else {
                    sendItemDialogue(player, PASS, "You hand over a Shantay Pass.").also { stage++ }
                }

            14 -> player("Sure, here you go!").also { stage++ }
            15 ->
                if (player.inventory.remove(PASS)) {
                    val dest =
                        if (player.location.y < 3304) Location.create(3303, 3117, 0) else Location.create(3305, 3117, 0)
                    Pathfinder.find(player, dest).walk(player)
                    end()
                    player.lock()
                    Pulser.submit(
                        object : Pulse(1, player) {
                            override fun pulse(): Boolean {
                                if (player.location == dest) {
                                    player.unlock()
                                    handleShantayPass(player)
                                    return true
                                }
                                return false
                            }
                        },
                    )
                }

            100 ->
                sendDialogueLines(
                    player!!,
                    "The guard seems quite bad tempered, probably from having to wear",
                    "heavy armour in this intense heat.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return ShantayGuardDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SHANTAY_GUARD_837, NPCs.SHANTAY_GUARD_838)
    }

    companion object {
        private val PASS = Item(Items.SHANTAY_PASS_1854)

        fun handleShantayPass(player: Player) {
            AgilityHandler.walk(
                player,
                0,
                player.location,
                player.location.transform(0, if (player.location.y > 3116) -2 else 2, 0),
                null,
                0.0,
                null,
            )
        }
    }
}
