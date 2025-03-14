package content.region.fremennik.handlers.general_shadows

import core.api.*
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
class DesertScoutDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        if (!inEquipment(player, Items.GHOSTSPEAK_AMULET_552)) {
            npc("Whoooo wooo Whooooooooo.").also { stage = END_DIALOGUE }
            return true
        }
        if (getAttribute(player, GeneralShadowUtils.GS_PROGRESS, 0) == 0) {
            player("Hello there! General Khazard sent me.")
            return true
        }
        if (getAttribute(player, GeneralShadowUtils.GS_COMPLETE, false)) {
            player("Hello again.").also { stage = 100 }
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
            0 -> npc(FaceAnim.SCARED, "Gah! Don't sneak up on me!").also { stage++ }
            1 -> player(FaceAnim.GUILTY, "Uh, sorry.").also { stage++ }
            2 -> npc("I think I can see my heart pounding!").also { stage++ }
            3 ->
                player(
                    FaceAnim.LAUGH,
                    "You're pretty jumpy for a scout. I thought you'd be",
                    "made of sterner stuff.",
                ).also {
                    stage++
                }

            4 -> npc("What is it you want exactly?").also { stage++ }
            5 -> player("I bring a message from the General!").also { stage++ }
            6 ->
                player(
                    "Khazard says: 'The planets are nearly alignment; we",
                    "will meet in the place of half light and ice soon. Beware",
                    "of the others, for though they are weak and few, they",
                    "are cunning.'",
                ).also {
                    stage++
                }

            7 ->
                npc(
                    "Oh good, I long for the cold. This place is so hot! The",
                    "General has told me much of his time spent here and I",
                    "can see why he left.",
                ).also {
                    stage++
                }

            8 -> player("Oh, did the General live here?").also { stage++ }
            9 ->
                npc(
                    "Long ago, yes. But not by choice. His evil lord and",
                    "master forced him and his kind to reside here.",
                ).also {
                    stage++
                }

            10 -> player("Khazard with a master? Hard to believe. Who was it?").also { stage++ }
            11 -> npc("I think I have said too much already.").also { stage++ }
            12 -> player("So what have you discovered on your travels though", "the desert?").also { stage++ }
            13 ->
                npc(
                    "The fighting in the desert has come to a stop. However, ",
                    "there is still much distrust between Menaphos and Al",
                    "Kharid. It seems like it would be possible to reignite the",
                    "war if needed.",
                ).also {
                    stage++
                }

            14 ->
                if (isQuestComplete(player, Quests.DESERT_TREASURE)) {
                    npc(
                        "I have heard whisperings in the bandit camp that the",
                        "prisoner of Jaldraocht is now free.",
                    ).also { stage++ }
                } else if (isQuestComplete(player, Quests.ENAKHRAS_LAMENT) &&
                    !isQuestComplete(player, Quests.DESERT_TREASURE)
                ) {
                    npc(
                        "I followed one called Lazim. He let slip that he had",
                        "discovered the buried temple. Now the two others who",
                        "were there are on the move. The General may find there are new players at",
                        "the ritual this time.",
                    ).also {
                        stage++
                    }
                } else {
                    player(
                        "The Desert seems to be a hotbed of information.",
                        "Do you want me to relay any of this to the",
                        "General?",
                    ).also {
                        stage++
                    }
                }
            15 -> npc("Not at this time. I must investigate further.").also { stage++ }
            16 -> player(FaceAnim.SAD, "Any idea where I can find the other scouts?").also { stage++ }
            17 ->
                npc(
                    "Hmm. I left one of them near a haunted wood - the",
                    "trees kept trying to hit us. One scout headed for a",
                    "jungle and the other for the gnomes.",
                ).also {
                    stage++
                }
            18 -> {
                end()
                setAttribute(player, GeneralShadowUtils.GS_PROGRESS, 1)
            }
            100 -> npc("I can't speak to you; I must continue on my mission.").also { stage++ }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SCOUT_5570)
    }
}
