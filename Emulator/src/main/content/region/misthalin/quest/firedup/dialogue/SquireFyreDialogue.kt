package content.region.misthalin.quest.firedup.dialogue

import content.minigame.allfiredup.handlers.BeaconState
import core.api.setVarbit
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class SquireFyreDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = (args[0] as NPC).getShownNPC(player)
        val qstage = player.questRepository.getQuest(Quests.ALL_FIRED_UP).getStage(player)
        when (qstage) {
            40 ->
                player(
                    "Hi there. I'm helping Blaze and King Roald test the",
                    "beacon network. Can you see it from here? Blaze said",
                    "you have pretty sharp eyes.",
                ).also { stage = 100 }

            else -> npc("Carry on, friend.").also { stage = 1000 }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            100 ->
                npc(
                    "Of course I can see it. I haven't spent my entire life",
                    "practising my seeing skills for nothing! I'm happy to",
                    "report that the fire near Blaze is burning brightly.",
                ).also { stage++ }

            101 ->
                player(
                    "Terrific! Blaze has asked me to light this fire as well, so",
                    "he can see how things look from his vantage point.",
                ).also { stage++ }

            102 ->
                npc("Be my guest!").also {
                    stage++
                    player.questRepository.getQuest(Quests.ALL_FIRED_UP).setStage(player, 50)
                    setVarbit(
                        player,
                        5146,
                        BeaconState.DYING.ordinal,
                    )
                }

            103 ->
                options(
                    "How do I light the beacon?",
                    "I suppose you don't have any logs I could have?",
                    "Okay, thanks.",
                ).also { stage++ }

            104 ->
                when (buttonId) {
                    1 -> player("How do I light the beacon?").also { stage = 110 }
                    2 -> player("I suppose you don't have any logs I could have?").also { stage = 120 }
                    3 -> player("Okay, thanks").also { stage = 1000 }
                }

            110 -> npc("Put in 20 logs of the same kind, and then light it.").also { stage = 1000 }
            120 -> npc("No, I do not.").also { stage = 1000 }
            1000 -> end()
        }

        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SQUIRE_FYRE_8042)
    }
}
