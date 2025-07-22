package content.minigame.allfiredup.dialogue

import core.api.addItem
import core.api.getAttribute
import core.api.removeAttribute
import core.game.dialogue.DialogueFile
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Items

/**
 * Represents the King Roald (All Fired Up quest) dialogue.
 */
class KingRoaldAFUDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            START_DIALOGUE -> npc("Did what?").also { stage++ }
            1 -> {
                if (getAttribute(player!!, "afu-mini:adze", false)) {
                    player("I lit all fourteen beacons at once!")
                } else if (getAttribute(player!!, "afu-mini:gloves", false)) {
                    player("I lit ten beacons at once!")
                } else if (getAttribute(player!!, "afu-mini:ring", false)) {
                    player("I lit six beacons at once!")
                }
                stage++
            }

            2 -> {
                npc("Oh, wonderful! Here is your reward then.")
                if (getAttribute(player!!, "afu-mini:adze", false)) {
                    if (addItem(player!!, Items.INFERNO_ADZE_13661)) {
                        removeAttribute(player!!, "afu-mini:adze")
                    }
                }
                if (getAttribute(player!!, "afu-mini:gloves", false)) {
                    if (addItem(player!!, Items.FLAME_GLOVES_13660)) {
                        removeAttribute(player!!, "afu-mini:gloves")
                    }
                }
                if (getAttribute(player!!, "afu-mini:ring", false)) {
                    if (addItem(player!!, Items.RING_OF_FIRE_13659)) {
                        removeAttribute(player!!, "afu-mini:ring")
                    }
                }
                stage = END_DIALOGUE
            }

            END_DIALOGUE -> end()
        }
    }
}
