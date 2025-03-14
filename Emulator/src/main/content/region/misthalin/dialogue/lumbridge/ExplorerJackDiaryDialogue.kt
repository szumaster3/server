package content.region.misthalin.dialogue.lumbridge

import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.Diary
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.player.link.emote.Emotes
import core.game.world.GameWorld.settings
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class ExplorerJackDiaryDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private val level = 0

    override fun open(vararg args: Any): Boolean {
        npc("What ho! Where did you come from?")
        stage = -1
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            -1 -> {
                if (Diary.canClaimLevelRewards(player, DiaryType.LUMBRIDGE, 0)) {
                    player("I've done all the beginner tasks in my Lumbridge", "Achievement Diary.").also { stage = 50 }
                    stage = 12
                }
                if (Diary.canReplaceReward(player, DiaryType.LUMBRIDGE, 0)) {
                    player("I've seemed to have lost my explorer's ring...").also { stage = 16 }
                }
                options(
                    "What is the Achievement Diary?",
                    "What are the rewards?",
                    "How do I claim the rewards?",
                    "See you later.",
                ).also { stage = 0 }
            }

            0 ->
                when (buttonId) {
                    1 -> player("What is the Achievement Diary?").also { stage = 5 }
                    2 -> player("What are the rewards?").also { stage = 8 }
                    3 -> player("How do I claim the rewards?").also { stage = 10 }
                    4 -> player("See you later!").also { stage = END_DIALOGUE }
                }

            1 -> player("Oh sorry. I was just looking around.").also { stage++ }
            2 -> npc("Oh that's perfectly alright. Mi Casa and all that what!").also { stage++ }
            3 -> player("Uh...and all what?").also { stage++ }
            4 ->
                npc("Splendid! I love a person with a sense of humour. I bet", "you're from Ardougne eh? Ha!").also {
                    stage =
                        -1
                }
            5 ->
                npc(
                    "Ah, well it's a diary that helps you keep track of",
                    "particular achievements you've made in the world of",
                    "${settings!!.name}. In Lumbridge and Draynor I can help you",
                    "discover some very useful things indeed.",
                ).also {
                    stage++
                }
            6 ->
                npc(
                    "Eventually with enough exploration you will be",
                    "rewarded for your explorative efforts.",
                ).also { stage++ }
            7 ->
                npc(
                    "You can access your Achievement Diary by going to",
                    "the Quest Journal. When you've opened the Quest",
                    "Journal click on the green star icon on the top right",
                    "hand corner. This will open the diary.",
                ).also {
                    stage =
                        -1
                }
            8 ->
                npc(
                    "Ah, well there are different rewards for each",
                    "Achievement Diary. For completing the Lumbridge and",
                    "Draynor diary you are presented with an explorer's",
                    "ring.",
                ).also {
                    stage++
                }
            9 ->
                npc(
                    "This ring will become increasingly useful with each",
                    "section of the diary that you complete.",
                ).also {
                    stage =
                        -1
                }
            10 ->
                npc(
                    "You need to complete the tasks so that they're all ticked",
                    "off then you can claim your reward. Most of them are",
                    "straightforward although you might find some required",
                    "quests to be started, if not finished.",
                ).also {
                    stage++
                }
            11 ->
                npc(
                    "To claim the explorer's ring speak to Bob in Bob's",
                    "Axes in Lumbridge, Ned in Draynor Village or myself.",
                ).also {
                    stage =
                        -1
                }
            12 -> npc("Yes I see that, you'll be wanting your", "reward then I assume?").also { stage++ }
            13 -> player("Yes please.").also { stage++ }
            14 -> {
                Diary.flagRewarded(player, DiaryType.LUMBRIDGE, level)
                player.emoteManager.unlock(Emotes.EXPLORE)
                npc(
                    "This ring is a representation of the adventures you",
                    "went on to complete your tasks.",
                ).also { stage++ }
            }

            15 -> player("Wow, thanks!").also { stage = -1 }
            16 -> {
                Diary.grantReplacement(player, DiaryType.LUMBRIDGE, level)
                npc("You better be more careful this time.").also { stage = -1 }
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return ExplorerJackDiaryDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.EXPLORER_JACK_7969)
    }
}
