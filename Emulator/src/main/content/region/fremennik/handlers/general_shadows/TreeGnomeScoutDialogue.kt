package content.region.fremennik.handlers.general_shadows

import core.api.*
import core.api.quest.hasRequirement
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class TreeGnomeScoutDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        if (!inEquipment(player, Items.GHOSTSPEAK_AMULET_552)) {
            npc("Whoooo wooo Whooooooooo.").also { stage = END_DIALOGUE }
            return true
        }
        if (getAttribute(player, GeneralShadowUtils.GS_COMPLETE, false)) {
            player("Hello again.").also { stage = 100 }
            return true
        }
        if (getAttribute(player, GeneralShadowUtils.GS_PROGRESS, 0) == 3) {
            player("Hello there! General Khazard sent me.")
            return true
        }
        sendDialogue(player, "The Scout is too busy to talk.").also { stage = END_DIALOGUE }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc("What news, skinbag?").also { stage++ }
            1 -> player("Hey, no need to be rude.").also { stage++ }
            2 -> npc("Your smell disgusts me, so make it quick.").also { stage++ }
            3 ->
                player(
                    "Khazard says: 'The planets are nearly alignment; we",
                    "will meet in the place of half light and ice soon. Beware",
                    "of the others, for though they are weak and few, they",
                    "are cunning.'",
                ).also {
                    stage++
                }
            4 ->
                npc(
                    FaceAnim.ANNOYED,
                    "I'll be glad when all this is over. The General will be",
                    "strong again and we will push back this tide of filth that",
                    "is humanity.",
                ).also {
                    stage++
                }
            5 ->
                player(
                    FaceAnim.ANGRY,
                    "Oi! I am one of the filth, I mean, humans! Besides, we",
                    "were here first.",
                ).also {
                    stage++
                }
            6 ->
                npc(
                    FaceAnim.EXTREMELY_SHOCKED,
                    "Haa, haa, haa. Oh, you naive mortals. There were many",
                    "here before you. The last time the General went north, ",
                    "all this land belonged to the elves. Do not think so",
                    "highly of your race.",
                ).also {
                    stage++
                }
            7 -> player("Discovered anything interesting on your travels?").also { stage++ }
            8 ->
                if (isQuestComplete(player, Quests.BIOHAZARD) || isQuestComplete(player, Quests.PLAGUE_CITY)) {
                    npc(
                        "As I passed through Ardougne, I overheard some news.",
                        "The plague in the west grows worse. King Lathas's",
                        "popularity continues to increase, while the people are",
                        "becoming more and more hostile to Tyras. I suspect",
                    ).also {
                        stage++
                    }
                } else {
                    player("That is all that I have heard.").also { stage = 12 }
                }
            9 ->
                npc(
                    "there is more to be learned, but my mission is to",
                    "observe gnomes, not humans.",
                ).also { stage++ }
            10 ->
                if (!hasRequirement(player, Quests.HAZEEL_CULT)) {
                    npcl(
                        FaceAnim.FRIENDLY,
                        "I have heard that the cultists, who think they are so well hidden in the sewers, have failed in their ritual.",
                    ).also {
                        stage++
                    }
                } else {
                    npcl(
                        FaceAnim.FRIENDLY,
                        "I have heard that the cultists, who think they are so well hidden in the sewers, have succeeded in their ritual.",
                    ).also {
                        stage++
                    }
                }
            11 ->
                if (!isQuestComplete(player, Quests.THE_GRAND_TREE)) {
                    player("That is all that I have heard.").also { stage = 13 }
                } else {
                    npcl(
                        FaceAnim.FRIENDLY,
                        "I head to the land of the gnomes; rumours say it is peaceful right now.",
                    ).also { stage++ }
                }
            12 -> npcl(FaceAnim.FRIENDLY, "But I hear Glough has lost his position as advisor.").also { stage++ }
            13 -> {
                end()
                setAttribute(player, GeneralShadowUtils.GS_PROGRESS, 4)
            }
            100 ->
                npcl(FaceAnim.FRIENDLY, "I can't speak to you; I must continue on my mission.").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SCOUT_5567)
    }
}
