package content.minigame.mta.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class GraveyardGuardianDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        player("Hello.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "What do I have to do in this room?",
                    "What are the rewards?",
                    "Got any tips that may help me?",
                    "Thanks, bye!",
                ).also {
                    stage++
                }

            1 ->
                when (buttonId) {
                    1 -> player("What do I have to do in this room?").also { stage++ }
                    2 -> player("What are the rewards?").also { stage = 5 }
                    3 -> player("Got any tips that may help me?").also { stage = 7 }
                    4 -> player("Thanks, bye!").also { stage = END_DIALOGUE }
                }
            2 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Have you noticed all the bones around the room? These",
                    "are teleported here from all over Keldagrim to help",
                    "clean up the landscape of countless bones left behind",
                    "from combat. What better use for these bones than to",
                ).also {
                    stage++
                }
            3 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "convert them to nutritious fruit to be eaten by you",
                    "mortals? You have to use your Bones to Bananas spell",
                    "to convert the bones and then  place them in the holes",
                    "on the walls to earn Graveyard Pizazz Points.",
                ).also {
                    stage++
                }
            4 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Unluckily for you, your health will constantly decrease",
                    "from getting hit by these dropping bones so you will",
                    "probably want to eat some of the bananas yourself to",
                    "increase your stay here.",
                ).also {
                    stage =
                        0
                }
            5 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "You will get experience from casting your bones to",
                    "bananas spell and you will get Graveyard Pizazz Points",
                    "when you put the bananas though the wall.",
                    "Occasionally you will also be credited with a runestone",
                ).also {
                    stage++
                }
            6 -> npc(FaceAnim.OLD_NORMAL, "to help you in your future spell casting.").also { stage = 0 }
            7 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Different bones will provide you with different numbers",
                    "of bananas, so try to find the best type. Collect enough",
                    "points and you will be able to buy a 'Bones to Peaches'",
                    "spell from my fellow guardian above the entrance hall.",
                ).also {
                    stage++
                }
            8 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "This spell can be used here just like the bones to",
                    "bananas spell, except this spell will give you even more",
                    "experience and the peaches will restore more health!",
                ).also {
                    stage++
                }
            9 -> player("I see.").also { stage++ }
            10 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Oh, and a word of warning: should you decide to leave",
                    "this room by a method other than the exit portals, you",
                    "will be teleported to the entrance and have any items",
                    "that you picked up in the room removed.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GRAVEYARD_GUARDIAN_3101)
    }
}
