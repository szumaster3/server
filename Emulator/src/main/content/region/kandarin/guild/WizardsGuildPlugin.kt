package content.region.kandarin.guild

import content.global.travel.EssenceTeleport
import content.region.feldiphills.quest.zogre.dialogue.ZavisticRarveDefaultDialogue
import content.region.feldiphills.quest.zogre.dialogue.ZavisticRarveDialogueFiles
import core.api.*
import core.api.interaction.openNpcShop
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.global.Skillcape
import core.game.global.action.ClimbActionHandler
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Scenery

class WizardsGuildPlugin : InteractionListener {

    companion object {
        val MAGIC_DOOR = intArrayOf(Scenery.MAGIC_GUILD_DOOR_1600, Scenery.MAGIC_GUILD_DOOR_1601)
        val GATE = intArrayOf(Scenery.GATE_2154, Scenery.GATE_2155)
    }

    override fun defineListeners() {

        /*
         * Handles magic doors.
         */

        on(MAGIC_DOOR, IntType.SCENERY, "open") { player, node ->
            if (getDynLevel(player, Skills.MAGIC) < 66) {
                sendPlayerDialogue(player, "You need a Magic level of at least 66 to enter.")
                return@on true
            }
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            return@on true
        }

        /*
         * Handles basement of the Wizards' Guild interaction with gates.
         */

        on(GATE, IntType.SCENERY, "open") { player, _ ->
            sendNPCDialogue(
                player,
                NPCs.WIZARD_FRUMSCONE_460,
                "You can't attack the Zombies in the room, my Zombies are for magic target practice only and should be attacked from the other side of the fence.",
            )
            return@on true
        }

        /*
         * Handles staircase.
         */

        on(Scenery.STAIRCASE_1722, IntType.SCENERY, "climb-up") { player, node ->
            if (node.location == Location(2590, 3089, 0)) {
                ClimbActionHandler.climb(player, null, Location.create(2591, 3092, 1))
            } else {
                ClimbActionHandler.climbLadder(player, node.asScenery(), "climb-up")
            }
            return@on true
        }

        /*
         * Handling the essence teleport inside guild.
         */

        on(NPCs.WIZARD_DISTENTOR_462, IntType.NPC, "teleport") { player, node ->
            if (!isQuestComplete(player, Quests.RUNE_MYSTERIES)) {
                sendMessage(player, "You need to have completed the Rune Mysteries Quest to use this feature.")
                return@on false
            }

            EssenceTeleport.teleport((node as NPC), player)
            return@on true
        }

        /*
         * Handling talking to npc around guild.
         */

        on(NPCs.PROFESSOR_IMBLEWYN_4586, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, ProfessorImblewynDialogue(), node)
            return@on true
        }

        on(NPCs.ROBE_STORE_OWNER_1658, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, RobeStoreOwnerDialogue(), node)
            return@on true
        }

        on(NPCs.WIZARD_DISTENTOR_462, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, WizardDistentorDialogue(), node)
            return@on true
        }

        on(NPCs.WIZARD_FRUMSCONE_460, IntType.NPC, "talk-to") { player, node ->
            sendNPCDialogue(player, node.id, "Do you like my magic Zombies? Feel free to kill them, there's plenty more where these came from!", FaceAnim.HALF_GUILTY)
            return@on true
        }
    }
}

private class ProfessorImblewynDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> player("I didn't realise gnomes were interested in magic.").also { stage++ }
            1 -> npc(FaceAnim.OLD_NORMAL, "Gnomes are interested in everything, lad.").also { stage++ }
            2 -> player("Of course.").also { stage = END_DIALOGUE }
        }
    }
}

private class RobeStoreOwnerDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> if (Skillcape.isMaster(player!!, Skills.MAGIC)) {
                options("Ask about Skillcape.", "Something else").also { stage = 4 }
            } else {
                npc("Welcome to the Magic Guild Store. Would you like to", "buy some magic supplies?").also { stage++ }
            }

            1 -> options("Yes please.", "No thank you.").also { stage++ }
            2 -> when (buttonID) {
                1 -> player("Yes please.").also { stage++ }
                2 -> player("No thank you.").also { stage = END_DIALOGUE }
            }

            3 -> {
                end()
                openNpcShop(player!!, NPCs.ROBE_STORE_OWNER_1658)
            }

            4 -> when (buttonID) {
                1 -> player("Can I buy a Skillcape of Magic?").also { stage++ }
                2 -> npc("Welcome to the Magic Guild Store. Would you like to", "buy some magic supplies?").also { stage = 1 }
            }
            5 -> npc("Certainly! Right when you give me 99000 coins.").also { stage++ }
            6 -> options("Okay, here you go.", "No, thanks.").also { stage++ }
            7 -> when (buttonID) {
                1 -> player("Okay, here you go.").also { stage++ }
                2 -> player("No, thanks.").also { stage = END_DIALOGUE }
            }

            8 -> if (Skillcape.purchase(player!!, Skills.MAGIC)) {
                npc("There you go! Enjoy.").also { stage = END_DIALOGUE }
            }
        }
    }
}

private class WizardDistentorDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npc("Welcome to the Magicians' Guild!").also { stage++ }
            1 -> player("Hello there.").also { stage++ }
            2 -> npc("What can I do for you?").also { stage++ }
            3 -> if (!isQuestComplete(player!!, Quests.RUNE_MYSTERIES)) {
                player("Nothing thanks, I'm just looking around.").also { stage = 5 }
            } else {
                options("Nothing thanks, I'm just looking around.", "Can you teleport me to Rune Essence?").also { stage++ }
            }
            4 -> when (buttonID) {
                1 -> player("Nothing thanks, I'm just looking around.").also { stage++ }
                2 -> player("Can you teleport me to the Rune Essence?").also { stage = 6 }
            }

            5 -> npc("That's fine with me.").also { stage = END_DIALOGUE }
            6 -> {
                end()
                EssenceTeleport.teleport(npc!!, player!!)
            }
        }
    }
}

@Initializable
private class ZavisticRarveDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        if (isQuestComplete(player, Quests.ZOGRE_FLESH_EATERS)) {
            openDialogue(player, ZavisticRarveDefaultDialogue())
        } else {
            openDialogue(player, ZavisticRarveDialogueFiles())
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.ZAVISTIC_RARVE_2059)
}