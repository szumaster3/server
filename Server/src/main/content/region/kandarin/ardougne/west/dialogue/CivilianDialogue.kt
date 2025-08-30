package content.region.kandarin.ardougne.west.dialogue

import content.global.skill.construction.decoration.costumeroom.Storable
import content.global.skill.construction.decoration.costumeroom.Storable.Companion.hasStorableEquipped
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

val cats = intArrayOf(
    Items.PET_CAT_1561,
    Items.PET_CAT_1562,
    Items.PET_CAT_1563,
    Items.PET_CAT_1564,
    Items.PET_CAT_1565,
    Items.PET_CAT_1566,
    Items.OVERGROWN_CAT_1567,
    Items.OVERGROWN_CAT_1568,
    Items.OVERGROWN_CAT_1569,
    Items.OVERGROWN_CAT_1570,
    Items.OVERGROWN_CAT_1571,
    Items.OVERGROWN_CAT_1572,
    Items.LAZY_CAT_6549,
    Items.LAZY_CAT_6550,
    Items.LAZY_CAT_6551,
    Items.LAZY_CAT_6552,
    Items.LAZY_CAT_6553,
    Items.LAZY_CAT_6554,
    Items.WILY_CAT_6555,
    Items.WILY_CAT_6556,
    Items.WILY_CAT_6557,
    Items.WILY_CAT_6558,
    Items.WILY_CAT_6559,
    Items.WILY_CAT_6560
)

@Initializable
class CivilianDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if(!hasStorableEquipped(player, Storable.MournerGear)) {
            player("Hello.")
            stage = 20
        }
        when (npc.id) {
            NPCs.CIVILIAN_785 -> playerl(FaceAnim.FRIENDLY, "Hello there.")
            NPCs.CIVILIAN_786 -> player("Hi there.")
            NPCs.CIVILIAN_787 -> playerl(FaceAnim.FRIENDLY, "Hello there.")
            else -> return false
        }
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                when (npc.id) {
                    NPCs.CIVILIAN_785 -> npcl(FaceAnim.FRIENDLY, "Oh hello, I'm sorry, I'm a bit worn out.")
                    NPCs.CIVILIAN_786 -> npcl(FaceAnim.FRIENDLY, "Good day to you traveller.")
                    NPCs.CIVILIAN_787 -> npcl(FaceAnim.FRIENDLY, "I'm a bit busy to talk right now, sorry.")
                }
                stage++
            }
            1 -> {
                when (npc.id) {
                    NPCs.CIVILIAN_785 -> playerl(FaceAnim.FRIENDLY, "Busy day?")
                    NPCs.CIVILIAN_786 -> playerl(FaceAnim.FRIENDLY, "What are you up to?")
                    NPCs.CIVILIAN_787 -> playerl(FaceAnim.FRIENDLY, "Why? What are you doing?")
                }
                stage++
            }
            2 -> {
                when (npc.id) {
                    NPCs.CIVILIAN_785 -> npcl(FaceAnim.FRIENDLY, "Oh, It's those mice! They're everywhere! What I really need is a cat. But they're hard to come by nowadays.")
                    NPCs.CIVILIAN_786 -> npcl(FaceAnim.FRIENDLY, "Chasing mice as usual! It's all I seem to do nowadays.")
                    NPCs.CIVILIAN_787 -> npcl(FaceAnim.FRIENDLY, "Trying to kill these mice! What I really need is a cat!")
                }
                stage++
            }
            3 -> {
                val hasCat = cats.any { inInventory(player, it) }
                when {
                    hasCat -> {
                        playerl(FaceAnim.HAPPY, "I have a cat that I could sell.")
                        stage = 10
                    }
                    inInventory(player, Items.WITCHS_CAT_1491) -> {
                        player(FaceAnim.FRIENDLY, "I have a cat...look!")
                        stage = 20
                    }
                    else -> {
                        playerl(FaceAnim.NEUTRAL, "No, you're right, you don't see many around.")
                        stage = END_DIALOGUE
                    }
                }
            }
            10 -> npcl(FaceAnim.FRIENDLY, "You don't say, is that it?").also { stage++ }
            11 -> player("Say hello to a real mouse-killer!").also { stage++ }
            12 -> npcl(FaceAnim.FRIENDLY, "Hmmm, not bad, not bad at all. Looks like it's a lively one.").also { stage++ }
            13 -> playerl(FaceAnim.THINKING, "Erm...kind of...").also { stage++ }
            14 -> npcl(FaceAnim.FRIENDLY, "I don't have much in the way of money, but I do have these!").also { stage++ }
            15 -> {
                cats.forEachIndexed { index, catItem ->
                    if (removeItem(player, catItem)) {
                        player.familiarManager.removeDetails(index)
                        addItem(player, Items.DEATH_RUNE_560, 100)
                        sendItemDialogue(player, Items.DEATH_RUNE_560, "The peasant shows you a sack of death runes.")
                        stage = END_DIALOGUE
                        return true
                    }
                }
                stage = END_DIALOGUE
            }
            20 -> npcl(FaceAnim.NEUTRAL, "Hmmm...doesn't look like it's seen daylight in years. That's not going to catch any mice!").also { stage = END_DIALOGUE }
            21 -> npcl(FaceAnim.HALF_ASKING, "If you Mourners really wanna help, why don't you do something about these mice?!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(
        NPCs.CIVILIAN_785,
        NPCs.CIVILIAN_786,
        NPCs.CIVILIAN_787
    )
}
