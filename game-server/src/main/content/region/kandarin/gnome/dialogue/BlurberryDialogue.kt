package content.region.kandarin.gnome.dialogue

import content.minigame.gnomecook.plugin.GC_BASE_ATTRIBUTE
import content.minigame.gnomecook.plugin.GC_TUT_FIN
import content.minigame.gnomecook.plugin.GC_TUT_PROG
import core.api.inInventory
import core.api.sendDialogue
import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Represents the Blurberry (barman) dialogue.
 */
@Initializable
class BlurberryDialogue(player: Player? = null) : Dialogue(player) {

    private var tutorialProgress = -1
    private var tutorialComplete = false

    override fun open(vararg args: Any?): Boolean {
        tutorialComplete = player.getAttribute("$GC_BASE_ATTRIBUTE:$GC_TUT_FIN", false)
        tutorialProgress = player.getAttribute("$GC_BASE_ATTRIBUTE:$GC_TUT_PROG", -1)

        if (tutorialProgress == 18) {
            npc(FaceAnim.OLD_NORMAL, "Yes, you, Aluft said you would be coming.").also { stage = 0 }
            return true
        }

        if (tutorialComplete) {
            npc(FaceAnim.OLD_NORMAL, "I do hope you're enjoying your work!").also { stage = END_DIALOGUE }
            return true
        }

        npc(FaceAnim.OLD_NORMAL, "Hello, have you made that drink for me?")
        stage = tutorialProgress
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> player("Yes! What is it I need to do?").also { stage++ }
            1 -> npc(FaceAnim.OLD_NORMAL, "Well, I'd like to show you how to make", "a drink.").also { stage++ }
            2 -> player("Woah, sounds like fun!").also { stage++ }
            3 -> npc(FaceAnim.OLD_NORMAL, "Alright, then, let's get to it!").also { stage++ }
            4 -> npc(FaceAnim.OLD_NORMAL, "I want you to make me a fruit blast. Simple drink!").also { stage++ }
            5 -> npc(FaceAnim.OLD_NORMAL, "Here's everything you need.").also { stage++ }
            6 -> {
                end()
                val items = arrayOf(Item(Items.KNIFE_946), Item(Items.COCKTAIL_SHAKER_2025), Item(Items.COCKTAIL_GLASS_2026), Item(Items.PINEAPPLE_2114), Item(Items.LEMON_2102, 2), Item(Items.ORANGE_2108))
                if (player.inventory.hasSpaceFor(*items)) {
                    player.inventory.add(*items)
                    setAttribute(player, "/save:$GC_BASE_ATTRIBUTE:$GC_TUT_PROG", 20)
                } else {
                    sendDialogue(player, "You don't have enough room for the items.")
                }
            }

            20 ->
                if (inInventory(player, Items.FRUIT_BLAST_9514)) {
                    player("Yes, yes I have! Here you go.").also { stage++ }
                } else {
                    player("No I have not.").also { stage = END_DIALOGUE }
                }

            21 -> {
                player.inventory.remove(Item(Items.FRUIT_BLAST_9514))
                sendDialogue(player, "You hand over the fruit blast.")
                setAttribute(player, "/save:$GC_BASE_ATTRIBUTE:$GC_TUT_PROG", 22)
                stage++
            }

            22 -> npc(FaceAnim.OLD_NORMAL, "Excellent, I think you're ready to go on the job!").also { stage++ }
            23 -> {
                end()
                npc(FaceAnim.OLD_NORMAL, "Go back and speak with Aluft now.")
                setAttribute(player, "/save:$GC_BASE_ATTRIBUTE:$GC_TUT_FIN", true)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = BlurberryDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.BLURBERRY_848)
}