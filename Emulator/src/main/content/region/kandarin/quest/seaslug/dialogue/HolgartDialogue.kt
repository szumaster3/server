package content.region.kandarin.quest.seaslug.dialogue

import content.region.kandarin.handlers.FishingPlatform
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class HolgartDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        when {
            isQuestComplete(player, Quests.SEA_SLUG) -> player("Hello Holgart.").also { stage = 100 }
            npc.id == 699 ->
                player("We'd better get back to the platform so we can see", "what's going on.").also {
                    stage =
                        200
                }
            getQuestStage(player, Quests.SEA_SLUG) == 50 ->
                player("Did you get the kid back to shore?").also {
                    stage =
                        300
                }
            getQuestStage(player, Quests.SEA_SLUG) == 10 ->
                player("Holgart, something strange is going on here.").also {
                    stage =
                        0
                }
            else -> sendDialogue(player, "Holgart seems too busy to talk.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc("You're telling me, none of the sailors seem to remember", "who I am.").also { stage++ }
            1 -> player("Apparently Kennith's father left for help a couple of", "days ago.").also { stage++ }
            2 ->
                npc(
                    "That's a worry, no-one's heard from him on shore.",
                    "Come on, we'd better look for him.",
                ).also { stage++ }
            3 -> {
                end()
                FishingPlatform.sail(player!!, FishingPlatform.Travel.FISHING_PLATFORM_TO_SMALL_ISLAND)
            }

            100 -> npc("Have you had enough of this place yet?", "It's really starting to scare me.").also { stage++ }
            101 -> options("No, I'm going to stay a while.", "Okay, let's go back.").also { stage++ }
            102 ->
                when (buttonId) {
                    1 -> player("No, I'm going to stay a while.").also { stage++ }
                    2 -> player("Okay, let's go back.").also { stage = 104 }
                }

            103 -> npc("Okay... you're the boss.").also { stage = END_DIALOGUE }
            104 -> {
                end()
                FishingPlatform.sail(player!!, FishingPlatform.Travel.FISHING_PLATFORM_TO_WITCHAVEN)
            }

            200 -> npc("You're right. It all sounds pretty creepy.").also { stage++ }
            201 -> {
                end()
                FishingPlatform.sail(player!!, FishingPlatform.Travel.SMALL_ISLAND_TO_FISHING_PLATFORM)
            }

            300 ->
                npc(
                    "Yes, he's safe and sound with his parents. Your turn to",
                    "return to land now adventurer.",
                ).also { stage++ }
            301 -> player(FaceAnim.NOD_YES, "Looking forward to it.").also { stage++ }
            302 -> {
                end()
                FishingPlatform.sail(player!!, FishingPlatform.Travel.FISHING_PLATFORM_TO_WITCHAVEN)
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HOLGART_699, NPCs.HOLGART_700)
    }
}
