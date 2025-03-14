package content.region.kandarin.quest.mcannon.dialogue

import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import org.rs.consts.NPCs
import org.rs.consts.Quests

class LollkDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.DWARF_CANNON)
        when (quest!!.getStage(player)) {
            40 -> npc("Thank the heavens, you saved me!", "I thought I'd be goblin lunch for sure!")
            else -> sendMessage(player, "The dwarf doesn't seem interested in talking to you.")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (quest!!.getStage(player)) {
            40 ->
                when (stage) {
                    0 -> player("Are you ok?").also { stage++ }
                    1 -> npc("I think so, I'd better run off home.").also { stage++ }
                    2 -> player("That's right, you get going. I'll catch up.").also { stage++ }
                    3 -> npc("Thanks again, brave adventurer.").also { stage++ }
                    4 -> {
                        npc.isInvisible = true
                        end()
                    }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LOLLK_207)
    }
}
