package content.minigame.mta.dialogue

import content.minigame.mta.handlers.ProgressHat
import core.api.anyInEquipment
import core.api.anyInInventory
import core.api.hasAnItem
import core.api.sendDialogueOptions
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.splitLines
import core.game.global.action.DropListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class ProgressHatDialogue(
    player: Player? = null,
) : Dialogue(player) {
    var progressHat: Item? = null
    val randomGreetings =
        arrayOf(
            "Hello... Mr Progress Hat?",
            "Erm, excuse me Mr Progress Hat?",
            "Mr Progress Hat... sir?",
            "Mr Progress Hat? Hello?",
        )
    private val responses =
        listOf(
            listOf(
                "That's me, why are you bothering me human?",
                "Sorry, but do you think you could tell me my Pizazz Points?",
            ),
            listOf("Can't you see I'm busy?", "But you're just a hat? Can you tell me my Pizazz Point totals?"),
            listOf("What do you want?", "Do you think you could tell me my Pizazz Points?"),
            listOf("I suppose you want to know your Pizazz Points.", "That would be nice, yes."),
        )

    override fun open(vararg args: Any): Boolean {
        if (args.size > 1) {
            progressHat = args[0] as Item
            npc(FaceAnim.OLD_NORMAL, "How dare you destroy me? You'll lose your Pizazz Points!").also { stage = 4 }
            return true
        }
        player(randomGreetings.random())
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val randomIndex = (0..3).random()
        val response = responses[randomIndex]
        when (stage) {
            0 -> npc(FaceAnim.OLD_NORMAL, *splitLines(response[0])).also { stage = 1 }
            1 -> player(FaceAnim.HALF_ASKING, *splitLines(response[1])).also { stage = 2 }
            2 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Ok, I suppose it's my job. You have:",
                    getPoints(0).toString() + " Telekinetic, " + getPoints(1) + " Alchemist,",
                    getPoints(2).toString() + " Enchantment, and " + getPoints(3) + " Graveyard Pizazz",
                    "Points.",
                ).also { stage++ }

            3 -> player("Thank you!").also { stage = END_DIALOGUE }
            4 -> sendDialogueOptions(player, "Destroy Hat?", "Yes", "No").also { stage++ }
            5 ->
                when (buttonId) {
                    1 -> {
                        val hasHat = hasAnItem(player, *ProgressHat.hats).container != null
                        if (!hasHat ||
                            !anyInEquipment(player, *ProgressHat.hats) ||
                            anyInInventory(player, *ProgressHat.hats)
                        ) {
                            end()
                            return true
                        }
                        DropListener.drop(player, Item(ProgressHat.hats.size))
                    }

                    2 -> npc(FaceAnim.OLD_NORMAL, "I think so too!").also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    fun getPoints(index: Int): Int = player.getSavedData().activityData.getPizazzPoints(index)

    override fun getIds(): IntArray = intArrayOf(NPCs.PIZZAZ_HAT_3096)
}
