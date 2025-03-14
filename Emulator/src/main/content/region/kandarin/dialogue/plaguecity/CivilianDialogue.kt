package content.region.kandarin.dialogue.plaguecity

import content.region.kandarin.quest.elena.handlers.PlagueCityUtils
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class CivilianDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (PlagueCityUtils.hasFullMournerGear(player)) {
            player("Hello.")
            stage = 20
            return true
        }
        when (npc.id) {
            NPCs.CIVILIAN_786 -> player("Hi there.")
            else -> playerl(FaceAnim.FRIENDLY, "Hello there.").also { stage = 15 }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.FRIENDLY, "Good day to you, traveller.").also { stage++ }
            1 -> playerl(FaceAnim.FRIENDLY, "What are you up to?").also { stage++ }
            2 -> npcl(FaceAnim.FRIENDLY, "Chasing mice as usual! It's all I seem to do nowadays.").also { stage++ }
            3 -> playerl(FaceAnim.FRIENDLY, "You must waste a lot of time?").also { stage++ }
            4 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Yes, but what can you do? It's not like there's many cats around here!",
                ).also { stage++ }
            5 -> {
                when {
                    PlagueCityUtils.hasAnKitten(player) -> {
                        playerl(FaceAnim.HAPPY, "I have a kitten that I could sell.")
                        stage++
                    }
                    PlagueCityUtils.hasAnCat(player) -> {
                        playerl(FaceAnim.HAPPY, "I have a cat that I could sell.")
                        stage = 9
                    }
                    inInventory(player, Items.WITCHS_CAT_1491) -> {
                        player(FaceAnim.FRIENDLY, "I have a cat...look!")
                        stage = 21
                    }
                    else -> {
                        playerl(FaceAnim.NEUTRAL, "No, you're right, you don't see many around.")
                        stage = END_DIALOGUE
                    }
                }
            }
            6 -> npcl(FaceAnim.HAPPY, "Really, let's have a look.").also { stage++ }
            7 -> sendDialogue(player, "You present the kitten.").also { stage++ }
            8 ->
                npcl(FaceAnim.LAUGH, "Hah! that little thing won't catch any mice. I need a fully grown cat.").also {
                    stage =
                        END_DIALOGUE
                }
            9 -> npcl(FaceAnim.FRIENDLY, "You don't say. Is that it?").also { stage++ }
            10 -> player("Say hello to a real mouse-killer!").also { stage++ }
            11 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Hmmm, not bad, not bad at all. Looks like it's a lively one.",
                ).also { stage++ }
            12 -> playerl(FaceAnim.THINKING, "Erm...kind of...").also { stage++ }
            13 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I don't have much in the way of money, but I do have these!",
                ).also { stage++ }
            14 -> {
                end()
                val item = PlagueCityUtils.grownCatItemIds
                for (i in item) {
                    if (player.inventory.remove(i)) {
                        player.familiarManager.removeDetails(i.idHash)
                        sendItemDialogue(player, Items.DEATH_RUNE_560, "The peasant shows you a sack of death runes.")
                        addItem(player, Items.DEATH_RUNE_560, 100)
                    }
                }
            }
            15 -> npcl(FaceAnim.FRIENDLY, "I'm a bit busy to talk right now, sorry.").also { stage++ }
            16 -> playerl(FaceAnim.FRIENDLY, "Why? What are you doing?").also { stage++ }
            17 -> npcl(FaceAnim.FRIENDLY, "Trying to kill these mice! What I really need is a cat!").also { stage++ }
            18 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "No, you're right, you don't see many around.",
                ).also { stage = END_DIALOGUE }
            19 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "If you Mourners really wanna help, why don't you do something about these mice?!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            20 ->
                npcl(
                    FaceAnim.HALF_ASKING,
                    "If you Mourners really wanna help, why don't you do something about these mice?!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            21 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Hmmm...doesn't look like it's seen daylight in years. That's not going to catch any mice!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CIVILIAN_785, NPCs.CIVILIAN_786, NPCs.CIVILIAN_787)
    }
}
