package content.region.misthalin.quest.phoenixgang.dialogue

import content.region.misthalin.quest.phoenixgang.ShieldofArrav
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import org.rs.consts.NPCs
import org.rs.consts.Quests

class WeaponsMasterDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.SHIELD_OF_ARRAV)
        when (quest!!.getStage(player)) {
            else -> {
                if (args.size > 1) {
                    npc("Stop! Thief!")
                    stage = 600
                    return true
                }
                player("Hello.")
                stage = 0
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (quest!!.getStage(player)) {
            else ->
                when (stage) {
                    600 -> {
                        end()
                        npc.properties.combatPulse.attack(player)
                    }

                    0 ->
                        stage =
                            if (ShieldofArrav.isPhoenix(player)) {
                                npc("Hello fellow Phoenix! What are you after?")
                                1
                            } else {
                                npc(
                                    "Hey! Who are you? I'm gonna teach you not to stick",
                                    "your nose where it don't belong!",
                                )
                                50
                            }

                    50 -> {
                        end()
                        npc.properties.combatPulse.attack(player)
                    }

                    1 -> {
                        options("I'm after a weapon or two.", "I'm looking for treasure.")
                        stage = 2
                    }

                    2 ->
                        when (buttonId) {
                            1 -> {
                                player("I'm after a weapon or two.")
                                stage = 10
                            }

                            2 -> {
                                player("I'm looking for treasure.")
                                stage = 20
                            }
                        }

                    10 -> {
                        npc("No problem. Feel free to look around.")
                        stage = 11
                    }

                    11 -> end()
                    20 -> {
                        npc(
                            "Aren't we all? We've not got any up here. Go mug",
                            "someone somewhere if you want some treasure.",
                        )
                        stage = 21
                    }

                    21 -> end()
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return WeaponsMasterDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.WEAPONSMASTER_643)
    }
}
