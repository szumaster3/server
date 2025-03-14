package content.region.desert.quest.rescue.dialogue

import core.api.sendChat
import core.api.sendDialogueLines
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.world.GameWorld
import core.game.world.map.RegionManager
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class LadyKeliDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.PRINCE_ALI_RESCUE)
        when (quest!!.getStage(player)) {
            60, 100 -> {
                sendChat(npc, "You tricked me, and tied me up, Guards kill this stranger!")
                val npcc = RegionManager.getLocalNpcs(player)
                for (n in npcc) {
                    if (n.id == 917) {
                        n.sendChat("Yes M'lady")
                        n.properties.combatPulse.attack(player)
                    }
                }
                end()
            }

            else -> {
                player(
                    FaceAnim.HALF_GUILTY,
                    "Are you the famous Lady Keli? Leader of the toughest",
                    "gang of mercenary killers around?",
                )
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
                    0 -> npc(FaceAnim.HALF_GUILTY, "I am Keli, you have heard of me then?").also { stage++ }
                    1 ->
                        options(
                            "Heard of you? You are famous in " + GameWorld.settings!!.name + "!",
                            "I have heard a little, but I think Katrine is tougher.",
                            "I have heard rumours that you kill people.",
                            "No I have never really heard of you.",
                        ).also {
                            stage++
                        }
                    2 ->
                        when (buttonId) {
                            1 ->
                                player(
                                    FaceAnim.HALF_GUILTY,
                                    "The great Lady Keli, of course I have heard of you.",
                                    "You are famous in " + GameWorld.settings!!.name + "!",
                                ).also { stage = 10 }
                            2 -> player(FaceAnim.HALF_GUILTY, "I think Katrine is tougher.").also { stage = 20 }
                            3 ->
                                player(FaceAnim.HALF_GUILTY, "I have heard rumours that you kill people.").also {
                                    stage =
                                        30
                                }
                            4 ->
                                player(
                                    FaceAnim.HALF_GUILTY,
                                    "No I have never really heard of you.",
                                ).also { stage = 40 }
                        }
                    10 ->
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "That's very kind of you to say. Reputations are",
                            "not easily earned. I have managed to succeed",
                            "where many fail.",
                        ).also {
                            stage++
                        }
                    11 ->
                        options(
                            "I think Katrine is still tougher.",
                            "What is your latest plan then?",
                            "You must have trained a lot for this work.",
                            "I should not disturb someone as tough as you.",
                        ).also {
                            stage++
                        }
                    12 ->
                        when (buttonId) {
                            1 -> player(FaceAnim.HALF_GUILTY, "I think Katrine is tougher.").also { stage = 20 }
                            2 ->
                                player(
                                    FaceAnim.HALF_GUILTY,
                                    "What is your latest plan then? Of course, you need",
                                    "not go into specific details.",
                                ).also {
                                    stage =
                                        22
                                }
                            3 ->
                                player(FaceAnim.HALF_GUILTY, "You must have trained a lot for this work.").also {
                                    stage =
                                        25
                                }
                            4 ->
                                player(
                                    FaceAnim.HALF_GUILTY,
                                    "I should not disturb someone as tough as you.",
                                ).also { stage = 27 }
                        }
                    20 ->
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "Well you can think that all you like. I know those",
                            "blackarm cowards dare not leave the city. Out here, I",
                            "am the toughest. You can tell them that! Now get out of",
                            "my sight, before I call my guards.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    22 ->
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "Well, I can tell you I have a valuable prisoner here",
                            "in my cells.",
                        ).also { stage++ }
                    23 ->
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "I can expect a high reward to be paid very soon for",
                            "this guy. I can't tell you who he is, but he is a lot",
                            "colder now.",
                        ).also {
                            stage++
                        }
                    24 -> player(FaceAnim.HALF_GUILTY, "You must have been very skillful.").also { stage = 50 }
                    50 ->
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "Yes. I did most of the work. We had to grab the Pr...",
                        ).also { stage++ }
                    51 ->
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "Er, we had to grab him without his ten bodyguards",
                            "noticing. It was a stroke of genius.",
                        ).also {
                            stage++
                        }
                    52 ->
                        player(
                            FaceAnim.HALF_GUILTY,
                            "Can you be sure they will not try to get him out?",
                        ).also { stage++ }
                    53 ->
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "There is no way to release him. The only key to the",
                            "door is on a chain around my neck and the locksmith",
                            "who made the lock died suddenly when he had finished.",
                        ).also {
                            stage++
                        }
                    54 -> npc(FaceAnim.HALF_GUILTY, "There is not another key like this in the world.").also { stage++ }
                    55 ->
                        player(
                            FaceAnim.HALF_GUILTY,
                            "Could I see the key please? Just for a moment. It",
                            "would be something I can tell my grandchildren. When",
                            "you are even more famous than you are now.",
                        ).also {
                            stage++
                        }
                    56 ->
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "As you put it that way I am sure you can see it. You",
                            "cannot steal the key, it is on a Runite chain.",
                        ).also {
                            stage++
                        }
                    57 ->
                        core.api
                            .sendDialogue(
                                player,
                                "Keli shows you a small key on a strong looking chain.",
                            ).also { stage++ }
                    58 -> {
                        if (player.inventory.remove(SOFT_CLAY)) {
                            if (!player.inventory.add(KEY_PRINT)) {
                                GroundItemManager.create(KEY_PRINT, player)
                            }
                            player(FaceAnim.HALF_GUILTY, "Could I touch the key for a moment please?")
                            stage = 59
                        } else {
                            npc(FaceAnim.HALF_GUILTY, "There, run along now I am very busy.")
                            stage = 63
                        }
                    }
                    59 -> npc(FaceAnim.HALF_GUILTY, "Only for a moment then.").also { stage++ }
                    60 ->
                        sendDialogueLines(
                            player,
                            "You put a piece of your soft clay in your hand. As you touch the",
                            "key, you take an imprint of it.",
                        ).also {
                            stage++
                        }
                    61 ->
                        player(
                            FaceAnim.HALF_GUILTY,
                            "Thank you so much, you are too kind, o great Keli.",
                        ).also { stage++ }
                    62 ->
                        npc(FaceAnim.HALF_GUILTY, "You are welcome, run along now, I am very busy.").also {
                            stage =
                                END_DIALOGUE
                        }
                    25 ->
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "I have used a sword since I was a small girl. I stabbed",
                            "three people before I was 6 years old.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    27 ->
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "I need to do a lot of work, goodbye. When you get a",
                            "little tougher, maybe I will give you a job.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    30 ->
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "There's always someone ready to spread rumours. I",
                            "hear all sort of ridiculous things these days.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    40 ->
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "You must be new to this land then. EVERYONE",
                            "knows of Lady Keli and her prowess with the sword.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }
        }

        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LADY_KELI_919)
    }

    companion object {
        private val SOFT_CLAY = Item(Items.SOFT_CLAY_1761)
        private val KEY_PRINT = Item(Items.KEY_PRINT_2423)
    }
}