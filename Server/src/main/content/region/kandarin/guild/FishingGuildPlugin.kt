package content.region.kandarin.guild

import core.api.getDynLevel
import core.api.openNpcShop
import core.api.sendNPCDialogue
import core.api.withinDistance
import core.cache.def.impl.SceneryDefinition
import core.game.dialogue.Dialogue
import core.game.global.Skillcape
import core.game.global.action.DoorActionHandler
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.plugin.ClassScanner
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.END_DIALOGUE
import shared.consts.NPCs

@Initializable
class FishingGuildPlugin : OptionHandler() {

    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(2025).handlers["option:open"] = this
        ClassScanner.definePlugin(MasterFisherDialogue())
        ClassScanner.definePlugin(RoacheyDialogue())
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        when (option) {
            "open" -> when (node.id) {
                2025 -> {
                    if (getDynLevel(player, Skills.FISHING) < 68 && withinDistance(player, Location(2611, 3394, 0))) {
                        sendNPCDialogue(player, NPCs.MASTER_FISHER_308, "Hello, I'm afraid only the top fishers are allowed to use our premier fishing facilities.")
                        return true
                    }
                    DoorActionHandler.handleAutowalkDoor(player, node as Scenery)
                }
            }
        }
        return true
    }

    /**
     * Represents the Roachey dialogue.
     */
    inner class RoacheyDialogue(player: Player? = null) : Dialogue(player) {

        override fun open(vararg args: Any?): Boolean {
            npc("Would you like to buy some Fishing equipment or sell", "some fish?")
            return true
        }

        override fun handle(interfaceId: Int, buttonId: Int): Boolean {
            when (stage) {
                0 -> options("Yes, please.", "No, thank you.").also { stage++ }
                1 -> when (buttonId) {
                    1 -> player("Yes, please.").also { stage++ }
                    2 -> end()
                }
                2 -> end().also { openNpcShop(player, NPCs.ROACHEY_592) }
            }
            return true
        }

        override fun newInstance(player: Player?): Dialogue = RoacheyDialogue(player)

        override fun getIds(): IntArray = intArrayOf(NPCs.ROACHEY_592)
    }


    /**
     * Represents the Master Fisher dialogue.
     */
    inner class MasterFisherDialogue(player: Player? = null) : Dialogue(player) {

        override fun open(vararg args: Any?): Boolean {
            if (!Skillcape.isMaster(player, Skills.FISHING)) {
                npc("Hello, I'm afraid only the top fishers are allowed to use our", "premier fishing facilities.")
            } else {
                npc("Hello, only the top fishers are allowed to use our", "premier fishing facilities and you seem to meet the", "criteria. Enjoy!")
            }
            stage = 0
            return true
        }

        override fun handle(interfaceId: Int, buttonId: Int): Boolean {
            when (stage) {
                0 -> if (Skillcape.isMaster(player, Skills.FISHING)) {
                    player("Can I buy a Skillcape of Fishing?").also { stage = 3 }
                } else {
                    player("Can you tell me about that Skillcape you're wearing?").also { stage++ }
                }
                1 -> npc("I'm happy to, my friend. This beautiful cape was", "presented to me in recognition of my skills and", "experience as a fisherman and I was asked to be the", "head of this guild at the same time. As the best").also { stage++ }
                2 -> npc("fisherman in the guild it is my duty to control who has", "access to the guild and to say who can buy similar", "Skillcapes.").also { stage = END_DIALOGUE }
                3 -> npc("Certainly! Right when you pay me 99000 coins.").also { stage++ }
                4 -> options("Okay, here you go.", "No, thanks.").also { stage++ }
                5 -> when (buttonId) {
                    1 -> player("Okay, here you go.").also { stage++ }
                    2 -> player("No, thanks.").also { stage = END_DIALOGUE }
                }

                6 -> {
                    if (Skillcape.purchase(player, Skills.FISHING)) {
                        npc("There you go! Enjoy.").also { stage = END_DIALOGUE }
                    }
                }
            }
            return true
        }

        override fun newInstance(player: Player?): Dialogue = MasterFisherDialogue(player)

        override fun getIds(): IntArray = intArrayOf(NPCs.MASTER_FISHER_308)
    }
}
