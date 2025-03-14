package content.region.kandarin.quest.itwatchtower.dialogue

import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.RegionManager
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class TobanDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val questStage = getQuestStage(player, Quests.WATCHTOWER)
        if (questStage == 4) {
            npc(FaceAnim.OLD_NEUTRAL, "What do you want, small t'ing?")
            return true
        }
        if (questStage == 5) {
            npc(FaceAnim.OLD_NEUTRAL, "Hahaha! Small t'ing returns. Did you bring the dragon", "bone?").also {
                stage =
                    10
            }
            return true
        }
        if (!isQuestComplete(player, Quests.WATCHTOWER)) {
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
            0 -> options("I seek entrance to the city of ogres.", "Die, creature!").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player("I seek entrance to the city of ogres.").also { stage += 2 }
                    2 -> player("Die, creature!").also { stage++ }
                }
            2 ->
                npcl(FaceAnim.OLD_NEUTRAL, "Hahaha! It t'inks it's a match for Toban, does it?").also {
                    end()
                    RegionManager.getLocalNpcs(player)
                    if (npc.id == NPCs.OGRE_115) npc.attack(player)
                }
            3 -> npc(FaceAnim.OLD_NEUTRAL, "Hahaha! You'll never get in there.").also { stage++ }
            4 -> player("I'll find a way, trust me.").also { stage++ }
            5 -> npc(FaceAnim.OLD_NEUTRAL, "Bold words for a t'ing so small.").also { stage++ }
            6 -> options("I could do something for you...", "Die, creature!").also { stage++ }
            7 ->
                when (buttonId) {
                    1 -> player("I could do something for you...").also { stage++ }
                    2 -> player("Die, creature!").also { stage = 2 }
                }
            8 ->
                npc(
                    FaceAnim.OLD_NEUTRAL,
                    "Hahaha! This creature t'inks it can help me! I would eat",
                    "you now, but for your punt size.",
                ).also {
                    stage++
                }
            9 ->
                npc(
                    FaceAnim.OLD_NEUTRAL,
                    "Prove to me your might: bring me the bones of an",
                    "adult dragon to chew on, and I'll eat them, not you.",
                ).also {
                    player.questRepository.setStageNonmonotonic(player.questRepository.forIndex(131), 5)
                    stage = END_DIALOGUE
                }
            10 -> {
                if (freeSlots(player) == 0 && inInventory(player, Items.DRAGON_BONES_536)) {
                    npcl(FaceAnim.OLD_NEUTRAL, "Hahaha! Small t'ing has done it.").also { stage = 11 }
                    return true
                }
                if (!removeItem(player, Items.DRAGON_BONES_536)) {
                    player("I have nothing for you.").also { stage = 12 }
                } else {
                    player("When I say I will get something, I get it!").also { stage = 13 }
                }
            }
            11 ->
                sendDialogue(
                    player,
                    "Toban is glad, but you can't hold his reward; you'd best put somet'ing down.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            12 -> npc(FaceAnim.OLD_NEUTRAL, "Then you shall get nothing from me!").also { stage = END_DIALOGUE }
            13 ->
                npc(
                    FaceAnim.OLD_NEUTRAL,
                    "Hahaha! Small t'ing has done it. Toban is glad - take",
                    "this...",
                ).also { stage++ }
            14 -> {
                sendItemDialogue(player, Items.RELIC_PART_3_2375, "The ogre gives you part of a statue.")
                player.questRepository.setStageNonmonotonic(player.questRepository.forIndex(131), 6)
                addItem(player, Items.RELIC_PART_3_2375)
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return TobanDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TOBAN_855)
    }
}
