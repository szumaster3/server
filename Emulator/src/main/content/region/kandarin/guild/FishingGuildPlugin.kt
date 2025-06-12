package content.region.kandarin.guild

import core.api.getDynLevel
import core.api.interaction.openNpcShop
import core.api.openDialogue
import core.api.sendNPCDialogue
import core.api.withinDistance
import core.game.dialogue.DialogueFile
import core.game.global.Skillcape
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

class FishingGuildPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles opening the fishing guild door.
         */

        on(2025, IntType.SCENERY, "open") { player, node ->
            val restricted = getDynLevel(player, Skills.FISHING) < 68 && withinDistance(player, Location(2611, 3394, 0))
            if (restricted) {
                sendNPCDialogue(player, NPCs.MASTER_FISHER_308, "Hello, I'm afraid only the top fishers are allowed to use our premier fishing facilities.")
            } else {
                DoorActionHandler.handleAutowalkDoor(player, node as Scenery)
            }
            return@on true
        }

        /*
         * Handles talking to npc inside guild.
         */

        on(NPCs.MASTER_FISHER_308, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, MasterFisherDialogue(), node)
            return@on true
        }

        on(NPCs.ROACHEY_592, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, RoacheyDialogue(), node)
            return@on true
        }
    }
}

private class MasterFisherDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> if (!Skillcape.isMaster(player!!, Skills.FISHING)) {
                npc("Hello, I'm afraid only the top fishers are allowed to use our", "premier fishing facilities.").also { stage++ }
            } else {
                npc("Hello, only the top fishers are allowed to use our", "premier fishing facilities and you seem to meet the", "criteria. Enjoy!").also { stage++ }
            }
            1 -> if (Skillcape.isMaster(player!!, Skills.FISHING)) {
                player("Can I buy a Skillcape of Fishing?").also { stage = 4 }
            } else {
                player("Can you tell me about that Skillcape you're wearing?").also { stage++ }
            }
            2 -> npc("I'm happy to, my friend. This beautiful cape was", "presented to me in recognition of my skills and", "experience as a fisherman and I was asked to be the", "head of this guild at the same time. As the best").also { stage++ }
            3 -> npc("fisherman in the guild it is my duty to control who has", "access to the guild and to say who can buy similar", "Skillcapes.").also { stage = END_DIALOGUE }
            4 -> npc("Certainly! Right when you pay me 99000 coins.").also { stage++ }
            5 -> options("Okay, here you go.", "No, thanks.").also { stage++ }
            6 -> when (buttonID) {
                1 -> player("Okay, here you go.").also { stage++ }
                2 -> player("No, thanks.").also { stage = END_DIALOGUE }
            }
            7 -> if (Skillcape.purchase(player!!, Skills.FISHING)) {
                npc("There you go! Enjoy.").also { stage = END_DIALOGUE }
            }
        }
    }

}

private class RoacheyDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npc("Would you like to buy some Fishing equipment or sell", "some fish?").also { stage++ }
            1 -> options("Yes, please.", "No, thank you.").also { stage++ }
            2 -> when (buttonID) {
                1 -> player("Yes, please.").also { stage++ }
                2 -> end()
            }
            3 -> end().also { openNpcShop(player!!, NPCs.ROACHEY_592) }
        }
    }
}