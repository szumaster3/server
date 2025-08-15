package content.region.morytania.phas.dialogue

import content.region.morytania.phas.quest.ahoy.dialogue.GhostVillagerDialogueFile
import content.region.morytania.phas.quest.ahoy.plugin.GhostsAhoyUtils
import core.api.getAttribute
import core.api.inEquipment
import core.api.openDialogue
import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Ghost Villager dialogue.
 */
@Initializable
class GhostVillagerDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> {
                if (!inEquipment(player, Items.GHOSTSPEAK_AMULET_552)) {
                    npc("Woooo wooo wooooo woooo").also { stage = 1 }
                    return true
                }
                if (getAttribute(player!!, GhostsAhoyUtils.petitionstart, false)) {
                    end()
                    openDialogue(player, GhostVillagerDialogueFile())
                    return true
                }
                if (inEquipment(player!!, Items.BEDSHEET_4284)) {
                    npc("Why are you wearing that bedsheet?", "If you're trying to pretend to be one of us, you're", "not fooling anybody - you're not even green!")
                    stage = END_DIALOGUE
                    return true
                }
                if (inEquipment(player, Items.BEDSHEET_4285)) {
                    end()
                    openDialogue(player, GhostVillagerDialogueFile())
                } else {
                    when ((0..4).random()) {
                        0 -> npc("This cold wind blows right through you, doesn't it?").also { stage = END_DIALOGUE }
                        1 -> npc("We do not talk to the warm-bloods.").also { stage = END_DIALOGUE }
                        2 -> npc("What do you want, mortal?").also { stage = END_DIALOGUE }
                        3 -> npc("Why did we have to listen to that maniacal priest?").also { stage = END_DIALOGUE }
                        4 -> npc("Worship the Ectofuntus all you want, but", "don't bother us, human.").also { stage = END_DIALOGUE }
                    }
                }
            }

            1 -> sendDialogue(player, "You cannot understand the ghost.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = GhostVillagerDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.GHOST_VILLAGER_1697)
}
