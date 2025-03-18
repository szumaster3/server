package content.region.kandarin.quest.itwatchtower.dialogue

import core.api.addItemOrDrop
import core.api.inInventory
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.world.map.RegionManager
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class OgDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        if (getQuestStage(player, Quests.WATCHTOWER) >= 2) {
            npc(FaceAnim.OLD_DEFAULT, "Why you here, little t'ing?")
        } else if (!isQuestComplete(player, Quests.WATCHTOWER)) {
            sendMessage(player, "The ogre has nothing to say at the moment.")
        } else {
            sendMessage(player, "The ogre is not interested in you anymore.")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("I seek entrance to the city of ogres.", "I have come to kill you.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player("I seek entrance to the city of ogres.").also { stage++ }
                    2 -> player("I have come to kill you!").also { stage = 7 }
                }
            2 -> npc(FaceAnim.OLD_DEFAULT, "You got no business there!").also { stage++ }
            3 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Just a minute... Maybe if you did somet'ing for me, I",
                    "might help you get in.",
                ).also {
                    stage++
                }
            4 -> player("What can I do to help an ogre?").also { stage++ }
            5 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "South-east of here der is more ogres - the name of the",
                    "chieftain is Toban. He stole gold from me and I want it",
                    "back!",
                ).also {
                    stage++
                }
            6 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Here is a key to the chest it's in. If you bring it here,",
                    "I may reward you...",
                ).also {
                    end()
                    player.questRepository.setStageNonmonotonic(player.questRepository.forIndex(131), 3)
                    if (!inInventory(player, Items.TOBANS_KEY_2378)) {
                        addItemOrDrop(player, Items.TOBANS_KEY_2378)
                    }
                    stage = END_DIALOGUE
                }
            7 -> npc(FaceAnim.OLD_DEFAULT, "Kill me, eh? You shall be crushed. Guards!").also { stage++ }
            8 -> {
                end()
                RegionManager.getLocalNpcs(player)
                if (npc.id == NPCs.OGRE_115) npc.attack(player)
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.OG_853)
}
