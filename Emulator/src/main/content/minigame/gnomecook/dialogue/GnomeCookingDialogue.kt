package content.minigame.gnomecook.dialogue

import content.minigame.gnomecook.plugin.*
import content.minigame.gnomecook.plugin.GnomeCookingTipper.getTip
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import core.tools.colorize
import org.rs.consts.Items

class GnomeCookingDialogue(val job: GnomeCookingTask) : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> {
                val neededItem = player!!.getAttribute<Item>("$GC_BASE_ATTRIBUTE:$GC_NEEDED_ITEM", null)
                if (neededItem != null && player!!.inventory.containsItem(neededItem)) {
                    sendNPCDialogue(player!!, job.npc_id, "Thank you!", FaceAnim.OLD_HAPPY)
                    removeItem(player!!, neededItem)
                    player!!.inventory.add(getTip(job.level))
                    removeAttribute(player!!, "$GC_BASE_ATTRIBUTE:$GC_JOB_ORDINAL")
                    removeAttribute(player!!, "$GC_BASE_ATTRIBUTE:$GC_NEEDED_ITEM")
                    var curPoints = player!!.getAttribute("$GC_BASE_ATTRIBUTE:$GC_POINTS", 0)
                    curPoints += 3
                    if (curPoints == 12) {
                        addItem(player!!, Items.REWARD_TOKEN_9474, 1)
                        sendMessage(
                            player!!,
                            colorize("%RYou have been granted a food delivery token. Use it to have food delivered."),
                        )
                    } else if (curPoints % 12 == 0) {
                        var curRedeems = getAttribute(player!!, "$GC_BASE_ATTRIBUTE:$GC_REDEEMABLE_FOOD", 0)
                        setAttribute(
                            player!!,
                            "/save:$GC_BASE_ATTRIBUTE:$GC_REDEEMABLE_FOOD",
                            if (curRedeems != 10) {
                                ++curRedeems
                            } else {
                                curRedeems
                            },
                        )
                        sendMessage(player!!, colorize("%RYou have been granted a single food delivery charge."))
                    }
                    setAttribute(player!!, "/save:$GC_BASE_ATTRIBUTE:$GC_POINTS", curPoints)
                } else {
                    sendNPCDialogue(player!!, job.npc_id, "Where's my food?!", FaceAnim.ANGRY)
                }
                stage = END_DIALOGUE
            }
        }
    }
}
