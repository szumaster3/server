package content.region.asgarnia.dialogue.burthope

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Represents the Gamfred dialoue.
 */
@Initializable
class GamfredDialoue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (args.size > 1) {
            player(FaceAnim.ASKING, "May I have a shield please?").also { stage = 13 }
            return true
        }
        npc(
            FaceAnim.CHILD_NORMAL,
            "Ello there. I'm Gamfred, the engineer in this here guild.",
            "Have you seen my catapult?",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "That's not a catapult, it's a large crossbow.",
                    "Yes, beautiful piece of engineering.",
                    "No, where is it?",
                    "May I claim my tokens please?",
                    "Bye!",
                ).also {
                    stage++
                }
            1 ->
                when (buttonId) {
                    1 -> player("That's not a catapult, it's a large crossbow.").also { stage++ }
                    2 -> player("Yes, beautiful piece of engineering.").also { stage = 4 }
                    3 -> player("No, where is it?").also { stage = 15 }
                    4 -> {
                        end()
                        openDialogue(player, "wg:claim-tokens", npc.id)
                    }
                    5 -> player("Bye!").also { stage = 16 }
                }
            2 ->
                npc(
                    FaceAnim.CHILD_SAD,
                    "WHAT!? I'll have you know that is the finest piece of",
                    "dwarven engineering for miles around! How DARE you",
                    "insult my work!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            3 -> end()
            4 ->
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Nice to meet someone who appreciates fine work, have",
                    "you tried it out yet?",
                ).also {
                    stage++
                }
            5 -> options("Yes", "No, how do I do that?").also { stage++ }
            6 ->
                when (buttonId) {
                    1 -> player("Yes.").also { stage = 7 }
                    2 -> player("No, how do I do that?").also { stage = 17 }
                }
            7 -> npc(FaceAnim.CHILD_NORMAL, "What did you think?").also { stage++ }
            8 ->
                options(
                    "It was ok I guess.",
                    "It was fun!",
                    "I didn't like it.",
                    "May i have a shield please?",
                ).also { stage++ }
            9 ->
                when (buttonId) {
                    1 -> player("It was ok I guess.").also { stage++ }
                    2 -> player("It was fun!").also { stage = 11 }
                    3 -> player("I didn't like it.").also { stage = 12 }
                    4 -> player("May i have a shield please?").also { stage = 13 }
                }
            10 -> npc(FaceAnim.CHILD_NORMAL, "Well I guess not everyone will like it.").also { stage = END_DIALOGUE }
            11 ->
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Glad to hear it. Try it again sometime. We have more",
                    "tests to run.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            12 ->
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Well I guess not everyone will like it. But give it another",
                    "chance before you go.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            13 -> {
                if (hasAnItem(player, Items.DEFENSIVE_SHIELD_8856).container != null) {
                    npc(FaceAnim.CHILD_NORMAL, "Silly muffin, you have one already!").also { stage = END_DIALOGUE }
                } else {
                    npc(FaceAnim.CHILD_NORMAL, "of course!").also { stage = 14 }
                }
            }
            14 -> {
                if (hasSpaceFor(player, Item(Items.DEFENSIVE_SHIELD_8856, 1))) {
                    addItem(player, Items.DEFENSIVE_SHIELD_8856)
                    sendItemDialogue(player, Items.DEFENSIVE_SHIELD_8856, "The dwarf hands you a large shield.").also {
                        stage =
                            END_DIALOGUE
                    }
                } else {
                    npc(FaceAnim.CHILD_NORMAL, "Muffin make some room in your inventory first!").also {
                        stage =
                            END_DIALOGUE
                    }
                }
            }
            15 ->
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Are ye blind lad? Tis over there in the next room with me",
                    "assistant working it!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            16 ->
                npc(FaceAnim.CHILD_NORMAL, "Come back soon! My catapult needs more test subjects.").also {
                    stage =
                        END_DIALOGUE
                }
            17 ->
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Well ye take the big defence shield in both hands and",
                    "watch the catapult. My assistant will fire different things",
                    "at you and you need to defend against them. To see",
                    "what might be coming your way and wich defensive",
                ).also {
                    stage++
                }
            18 -> npc(FaceAnim.CHILD_NORMAL, "mode to choose, see the poster on the wall.").also { stage++ }
            19 -> options("May I have a shield please?", "Sounds boring.").also { stage++ }
            20 ->
                when (buttonId) {
                    1 -> player("May I have a shield please?").also { stage = 13 }
                    2 -> player("Sounds boring.").also { stage = 21 }
                }
            21 -> npc(FaceAnim.CHILD_NORMAL, "Your loss...").also { stage = 3 }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GAMFRED_4287)
    }
}
