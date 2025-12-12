package content.minigame.mage_arena.dialogue

import content.data.GodType
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Chamber Guardian dialogue.
 */
@Initializable
class ChamberGuardianDialogue(player: Player? = null) : Dialogue(player) {

    private var godType: GodType? = null

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (player.getSavedData().activityData.hasReceivedKolodionReward()) {
            player(FaceAnim.FRIENDLY, "Hello again.")
            return true
        } else if (player.getSavedData().activityData.hasKilledKolodion()) {
            npc(FaceAnim.HALF_ASKING, "Hello adventurer, have you made your choice?")
            return true
        }
        npc(FaceAnim.ANNOYED, "YOU SHOULD NOT BE IN HERE!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        if (player.getSavedData().activityData.hasReceivedKolodionReward()) {
            when (stage) {
                0 -> npc(FaceAnim.FRIENDLY, "Hello adventurer, are you looking for another staff?").also { stage++ }
                1 -> options("What do you have to offer?", "No thanks.").also { stage++ }
                2 -> when (buttonId) {
                    1 -> end().also { openNpcShop(player, NPCs.CHAMBER_GUARDIAN_904) }
                    2 -> player(FaceAnim.NEUTRAL, "No thanks.").also { stage++ }
                }
                3 -> npc(FaceAnim.FRIENDLY, "Well let me know if you need one.").also { stage = END_DIALOGUE }
            }
            return true
        } else if (player.getSavedData().activityData.hasKilledKolodion()) {
            when (stage) {
                0 -> {
                    godType = GodType.getCape(player)
                    if (godType == null) {
                        player(FaceAnim.HALF_GUILTY, "Sorry, I'm still looking.").also { stage = END_DIALOGUE }
                    } else {
                        player(FaceAnim.NEUTRAL, "I have.").also { stage++ }
                    }
                }
                1 -> npc(FaceAnim.FRIENDLY, "Good, good, I hope you have chosen well. I will now", "present you with a magic staff. This, along with the", "cape awarded to you by your chosen god, are all the", "weapons and armour you will need here.").also { stage++ }
                2 -> {
                    end()
                    if (!hasSpaceFor(player, godType!!.staffId.asItem())) {
                        player(FaceAnim.HALF_GUILTY, "Sorry, I don't have enough inventory space.")
                        return true
                    }
                    godType?.let { god ->
                        if (inEquipmentOrInventory(player, god.capeId)) {
                            addItem(player, god.staffId, 1)
                            player.getSavedData().activityData.kolodionStage = 3
                            sendItemDialogue(player, god.staffId, "The guardian hands you an ornate magic staff.")
                        }
                    }
                }
            }
            return true
        } else {
            end()
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.CHAMBER_GUARDIAN_904)
}
