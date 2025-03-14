package content.region.asgarnia.dialogue.portsarim

import content.global.travel.charter.Charter
import core.api.finishDiaryTask
import core.api.hasDiaryTaskComplete
import core.api.sendDialogue
import core.cache.def.impl.ItemDefinition
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class MonkOfEntranaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (npc.id in intArrayOf(NPCs.MONK_OF_ENTRANA_2730, NPCs.MONK_OF_ENTRANA_658, NPCs.MONK_OF_ENTRANA_2731)) {
            npc(FaceAnim.HALF_ASKING, "Do you wish to leave holy Entrana?").also { stage = 25 }
            return true
        }
        npc(
            FaceAnim.HALF_GUILTY,
            "Do you seek passage to holy Entrana? If so, you must",
            "leave your weaponry and armour behind. This is",
            "Saradomin's will.",
        ).also {
            stage =
                0
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("No, not right now.", "Yes, okay, I'm ready to go.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player("No, not right now.").also { stage = 10 }
                    2 -> player("Yes, okay, I'm ready to go.").also { stage = 20 }
                }

            10 -> npc("Very well.").also { stage = END_DIALOGUE }
            20 -> npc("Very well. One moment please.").also { stage++ }
            21 -> sendDialogue(player!!, "The monk quickly searches you.").also { stage++ }
            22 -> {
                if (!ItemDefinition.canEnterEntrana(player)) {
                    npc(
                        FaceAnim.ANNOYED,
                        "NO WEAPONS OR ARMOUR are permitted on holy",
                        "Entrana AT ALL. We will not allow you to travel there",
                        "in breach of mighty Saradomin's edict.",
                    ).also {
                        stage =
                            23
                    }
                    return true
                }
                npc("All is satisfactory. You may board the boat now.").also { stage = 24 }
            }

            23 ->
                npc(
                    "Do not try and decieve us again. Come back when you",
                    "have liad down your Zamorakian instruments of death.",
                ).also {
                    stage =
                        END_DIALOGUE
                }

            24 -> {
                end()
                Charter.PORT_SARIM_TO_ENTRANA.sail(player)
                if (!hasDiaryTaskComplete(player, DiaryType.FALADOR, 0, 14)) {
                    finishDiaryTask(player, DiaryType.FALADOR, 0, 14)
                }
            }

            25 -> options("Yes, I'm ready to go.", "Not just yet.").also { stage++ }
            26 ->
                when (buttonId) {
                    1 -> player("Yes, I'm ready to go.").also { stage++ }
                    2 -> player("Not just yet.").also { stage = END_DIALOGUE }
                }

            27 -> npc("Okay, let's board...").also { stage++ }
            28 -> {
                end()
                Charter.ENTRANA_TO_PORT_SARIM.sail(player)
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.MONK_OF_ENTRANA_657,
            NPCs.MONK_OF_ENTRANA_658,
            NPCs.MONK_OF_ENTRANA_2728,
            NPCs.MONK_OF_ENTRANA_2729,
            NPCs.MONK_OF_ENTRANA_2730,
            NPCs.MONK_OF_ENTRANA_2731,
        )
    }
}
