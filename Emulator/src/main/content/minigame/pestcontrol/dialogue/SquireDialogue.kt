package content.minigame.pestcontrol.dialogue

import content.global.travel.ship.CharterShip
import core.api.playJingle
import core.api.sendDialogueLines
import core.api.sendNPCDialogueLines
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld.settings
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Squire dialogue.
 */
@Initializable
class SquireDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        if (args.size == 3) {
            val type = args[1] as Int
            when (type) {
                0 -> {
                    playJingle(player, 144)
                    sendNPCDialogueLines(player, NPCs.SQUIRE_3781, FaceAnim.HALF_GUILTY, false, "The Void Knight was killed, another of our Order has", "fallen and that Island is lost.").also { stage = END_DIALOGUE }
                }

                1 -> {
                    val points = args[2] as String
                    playJingle(player, 145)
                    sendNPCDialogueLines(player, NPCs.SQUIRE_3781, FaceAnim.HAPPY, false, "Congratulations! You managed to destroy all the portals!", "We've awarded you $points Void Knight Commendation", "points. Please also accept these coins as a reward.").also { stage = 17 }
                }

                else -> {
                    playJingle(player, 145)
                    sendNPCDialogueLines(player, NPCs.SQUIRE_3781, FaceAnim.HALF_GUILTY, false, "Congratulations! You managed to destroy all the portals!", "However, you did not succeed in reaching the required", "amount of damage dealt we cannot grant you a reward.").also { stage = END_DIALOGUE }
                }
            }
            return true
        }

        npc = args[0] as NPC
        if (args.size == 2) {
            npc(FaceAnim.HALF_GUILTY, "Be quick, we're under attack!")
            stage = 13
            return true
        }
        npc(FaceAnim.HALF_GUILTY, "Hi, how can I help you?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                if (npc.id == NPCs.SQUIRE_3781) {
                    options("I'd like to go back to Port Sarim please.", "I'm fine thanks.").also { stage++ }
                } else {
                    options("Who are you?", "Where does this ship go?", "I'd like to go to your outpost.", "I'm fine thanks.").also { stage = 2 }
                }
            }

            1 -> when (buttonId) {
                1 -> player("I'd like to go back to Port Sarim please.").also { stage = 3 }
                2 -> player("I'm fine thanks.").also { stage = END_DIALOGUE }

            }

            2 -> when (buttonId) {
                1 -> player(FaceAnim.HALF_GUILTY, "Who are you?").also { stage = 5 }
                2 -> player(FaceAnim.HALF_GUILTY, "Where does this ship go?").also { stage = 8 }
                3 -> player(FaceAnim.HALF_GUILTY, "I'd like to go to your outpost.").also { stage = 11 }
                4 -> player(FaceAnim.HALF_GUILTY, "I'm fine thanks.").also { stage = END_DIALOGUE }
            }

            3 -> npc("Ok, but please come back soon and help us.").also { stage++ }
            4 -> {
                end()
                CharterShip.PEST_TO_PORT_SARIM.sail(player)
            }

            5 -> npc(FaceAnim.HALF_GUILTY, "I'm a Squire for the Void Knights.").also { stage++ }
            6 -> npc(FaceAnim.HALF_GUILTY, "The who?").also { stage++ }
            7 -> npc(FaceAnim.HALF_GUILTY, "The Void Knights, they are great warriors of balance", "who do Guthix's work here in " + settings!!.name + ".").also { stage = END_DIALOGUE }
            8 -> npc(FaceAnim.HALF_GUILTY, "To the Void Knight outpost. It's a small island just off", "Karamja.").also { stage++ }
            9 -> options("I'd like to go to your outpost.", "That's nice.").also { stage++ }
            10 -> when (buttonId) {
                1 -> player(FaceAnim.HALF_GUILTY, "I'd like to go to your outpost.").also { stage++ }
                2 -> player(FaceAnim.HALF_GUILTY, "That's nice.").also { stage = END_DIALOGUE }
            }
            11 -> npc(FaceAnim.HALF_GUILTY, "Certainly, right this way.").also { stage++ }
            12 -> {
                end()
                CharterShip.PORT_SARIM_TO_PEST_CONTROL.sail(player)
            }
            13 -> options("What's going on?", "How do I repair things?", "I want to leave.", "I'd better get back to it then.").also { stage++ }
            14 -> when (buttonId) {
                1 -> player("What's going on?").also { stage++ }
                2 -> player("How do I repair things?").also { stage = 16 }
                3 -> player("I want to leave.").also { stage = 18 }
                4 -> player("I'd better get back to it then.").also { stage = END_DIALOGUE }

            }
            15 -> npc("This island is being invaded by outsiders and the Void", "Knight over there is using a ritual to unsummon their", "portals. We must defend the Void Knight at all costs.", "however if you get an opening you can destroy the portals.").also { stage = END_DIALOGUE }
            16 -> npc("There are trees on the island. You'll need to chop them", "down for logs and use a hammer to repair the defences.", "Be careful tough, the trees here don't grow back very", "fast so your consts are limited!").also { stage = END_DIALOGUE }
            17 -> sendDialogueLines(player, BLUE + "You now have " + RED + "" + player.getSavedData().activityData.pestPoints + "" + BLUE + " Void Knight Commendation points!</col>", "You can speak to a Void Knight to exchange your points for", "rewards.").also { stage = END_DIALOGUE }
            18 -> npc("Away you go then, the lander will take you back.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SQUIRE_3781, NPCs.SQUIRE_3790)
}
