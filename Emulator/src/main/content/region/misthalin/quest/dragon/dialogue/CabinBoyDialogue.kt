package content.region.misthalin.quest.dragon.dialogue

import core.api.sendNPCDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.plugin.Initializable
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class CabinBoyDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.DRAGON_SLAYER)
        when (quest!!.getStage(player)) {
            20 -> {
                npc("Ahoy! Whay d'ye think of yer ship then?")
                stage = 0
            }

            40, 30 -> {
                sendNPCDialogue(player, 918, "Splice the mainsail!")
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
            40, 30 ->
                when (stage) {
                    0 -> {
                        npc("Aye aye, cap'n!")
                        stage = 1
                    }

                    1 -> end()
                }

            20 ->
                when (stage) {
                    0 -> {
                        player("Can you sail this ship to Crandor?")
                        stage = 1
                    }

                    1 -> {
                        npc("Not me, sir! I'm just an 'umble cabin boy. You'll need", "a proper cap'n.")
                        stage = 2
                    }
                    2 -> {
                        player("Where can I find a captain?")
                        stage = 3
                    }
                    3 -> {
                        npc(
                            "The cap'ns round 'ere seem to be a mite scared of",
                            "Crandor. I ask 'em why and they just say it was afore",
                            "my time,",
                        )
                        stage = 4
                    }
                    4 -> {
                        npc(
                            "but there is one cap'n I reckon might 'elp. I 'eard",
                            "there's a retired 'un who lives in Draynor Village who's",
                            "so desperate to sail again 'ed' take any job.",
                        )
                        stage = 5
                    }
                    5 -> {
                        npc("I can't remember 'is name, but 'e lives in Draynor", "Village an' makes rope.")
                        stage = 6
                    }

                    6 -> end()
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CABIN_BOY_JENKINS_6085)
    }
}
