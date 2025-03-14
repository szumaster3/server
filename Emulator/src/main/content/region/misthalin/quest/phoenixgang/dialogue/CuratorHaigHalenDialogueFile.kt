package content.region.misthalin.quest.phoenixgang.dialogue

import content.region.misthalin.quest.phoenixgang.ShieldofArrav
import core.api.sendItemDialogue
import core.game.dialogue.DialogueFile
import core.game.node.entity.npc.NPC
import org.rs.consts.NPCs

class CuratorHaigHalenDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.CURATOR_HAIG_HALEN_646)
        val player = player!!
        if (player.inventory.containsItem(ShieldofArrav.PHOENIX_SHIELD) ||
            player.inventory.containsItem(ShieldofArrav.BLACKARM_SHIELD)
        ) {
            when (stage) {
                0 -> player("I have half the shield of Arrav here. Can I get a", "reward?").also { stage++ }
                1 ->
                    npc(
                        "The Shield of Arrav! Goodness, the Museum has been",
                        "searching for that for years! The late King Roald II",
                        "offered a reward for it years ago!",
                    ).also {
                        stage++
                    }
                2 -> player("Well, I'm here to claim it.").also { stage++ }
                3 -> npc("Let me have a look at it first.").also { stage++ }
                4 ->
                    sendItemDialogue(
                        player,
                        ShieldofArrav.getShield(player),
                        "The curator peers at the shield.",
                    ).also { stage++ }
                5 -> npc("This is incredible!").also { stage++ }
                6 -> npc("That shield has been missing for over twenty-five years!").also { stage++ }
                7 ->
                    npc(
                        "Leave the shield here with me and I'll write you out a",
                        "certificate saying that you have returned the shield, so",
                        "that you can claim your reward from the King.",
                    ).also {
                        stage++
                    }
                8 -> player("Can I have two certificates please?").also { stage++ }
                9 -> npc("Yes, certainly. Please hand over the shield.").also { stage++ }
                10 ->
                    sendItemDialogue(
                        player,
                        ShieldofArrav.getShield(player),
                        "You hand over the shield half.",
                    ).also { stage++ }
                11 -> {
                    val shield = ShieldofArrav.getShield(player)
                    val certificate =
                        if (shield ==
                            ShieldofArrav.BLACKARM_SHIELD
                        ) {
                            ShieldofArrav.BLACKARM_CERTIFICATE
                        } else {
                            ShieldofArrav.PHOENIX_CERTIFICATE
                        }
                    if (player.inventory.remove(shield)) {
                        player.inventory.add(certificate)
                        interpreter!!.sendItemMessage(certificate, "The curator writes out two half-certificates.")
                        stage = 12
                    }
                }
            }
            return
        }
        when (stage) {
            0, 12 ->
                npc(
                    "Of course you won't actually be able to claim the",
                    "reward with only half the reward certificate...",
                ).also {
                    stage++
                }
            13 ->
                player(
                    "What? I went through a lot of trouble to get that shield",
                    "piece and now you tell me it was for nothing? That's",
                    "not very fair!",
                ).also {
                    stage++
                }
            14 ->
                npc(
                    "Well, if you were to get me the other half of the shield,",
                    "I could give you the other half of the reward certificate.",
                    "It's rumoured to be in the possession of the infamous",
                    "Blackarm Gang, beyond that I can't help you.",
                ).also {
                    stage++
                }
            15 -> player("Okay, I'll see what I can do.").also { stage++ }
            16 -> end()
        }
    }
}
