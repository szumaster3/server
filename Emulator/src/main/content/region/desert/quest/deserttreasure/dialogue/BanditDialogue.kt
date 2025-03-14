package content.region.desert.quest.deserttreasure.dialogue

import core.api.quest.getQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class BanditDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        when {
            getQuestStage(player, Quests.DESERT_TREASURE) == 100 -> {
                npc(
                    FaceAnim.HAPPY,
                    "So you're the one who freed Azzanadra from his prison? Thank you, kind " +
                        if (!player.isMale) "lady" else "sir" + "!",
                )
                stage = 1
            }

            getQuestStage(player, Quests.DESERT_TREASURE) in 1..15 -> {
                npc(FaceAnim.HAPPY, "What do you want " + if (!player.isMale) "lass" else "lad" + "?")
                stage = 2
            }

            else -> {
                npc(FaceAnim.ANGRY, "Get out of this village. You are not welcome here.")
                stage = 1
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 -> end()
            2 ->
                options(
                    "I'm here on an archaeological expedition for the Museum of Varrock. I believe there may be some interesting artefacts in the area.",
                    "What do you want from me?",
                ).also { stage++ }

            3 -> {
                when (buttonId) {
                    1 -> {
                        val randomResponse = (0..4).random()
                        when (randomResponse) {
                            0 ->
                                npc(
                                    "You are a crazy " +
                                        if (!player.isMale) {
                                            "woman"
                                        } else {
                                            "man" +
                                                ". The only thing you will find out here in the desert is your death."
                                        },
                                )
                            1 ->
                                npc(
                                    "I have no interest in the world that betrayed my people. Search where you will, you will find nothing.",
                                )
                            2 ->
                                npc(
                                    "The gods forsake us, and drove us to this place. Anything of worth has been long gone.",
                                )
                            3 ->
                                npc(
                                    "I'm sure there are many secrets buried beneath the sands here. The thing about this being a desert, is that they're likely to stay that way.",
                                )
                            4 -> npc("Do I look like I care who you are or where you came from?")
                        }
                        stage = 1
                    }

                    2 -> {
                        npc("Do I look like I care who you are or where you came from?")
                        stage = 1
                    }
                }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return BanditDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BANDIT_1926)
    }
}
