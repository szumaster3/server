package content.region.asgarnia.dialogue.rimmington

import core.api.inInventory
import core.api.quest.finishQuest
import core.api.quest.getQuestStage
import core.api.quest.updateQuestTab
import core.api.replaceSlot
import core.api.sendDialogueLines
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class HettyDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        val quest = player.getQuestRepository().getQuest(Quests.WITCHS_POTION)
        if (quest.isCompleted(player)) {
            npc(FaceAnim.ASKING, "How's your magic coming along?")
            stage = 0
        }
        when (quest.getStage(player)) {
            0 -> {
                npc(FaceAnim.NEUTRAL, "What could you want with an old woman like me?")
                stage = 11
            }

            20 -> {
                npc(FaceAnim.HAPPY, "So have you found the things for the potion?")
                stage = 100
            }

            40 ->
                if (args.size == 2) {
                    sendDialogueLines(
                        player,
                        "You drink from the cauldron, it tastes horrible! You feel yourself",
                        "imbued with power.",
                    )
                    stage = 41
                } else {
                    npc(FaceAnim.HALF_GUILTY, "Well are you going to drink the potion or not?")
                    stage = 500
                }
        }
        if (getQuestStage(player, Quests.SWEPT_AWAY) >= 1) {
            player("Hello there. Maggie sent me to ask for you help in", "enchanting her broom.")
            stage = 200
        }
        if (getQuestStage(player, Quests.SWEPT_AWAY) >= 1 && inInventory(player, Items.NEWT_14064)) {
            npc("Have you managed to get that newt yet?")
            stage = 210
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val quest = player.getQuestRepository().getQuest(Quests.WITCHS_POTION)
        when (stage) {
            0 -> {
                player(FaceAnim.HALF_GUILTY, "I'm practicing and slowly getting better.").also { stage++ }
                stage = 1
            }

            1 -> npc(FaceAnim.HALF_GUILTY, "Good, good.").also { stage = END_DIALOGUE }
            11 -> {
                options("I am in search of a quest.", "I've heard that you are a witch.")
                stage = 12
            }

            12 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.NEUTRAL, "I am in search of a quest.")
                        stage = 13
                    }

                    2 -> {
                        player(FaceAnim.NEUTRAL, "I've heard that you are a witch.")
                        stage = 20
                    }
                }

            13 -> {
                npc(FaceAnim.HAPPY, "Hmmm... Maybe I can think of something for you.")
                stage = 14
            }

            14 -> {
                npc(FaceAnim.HAPPY, "Would you like to become more proficient in the dark", "arts?")
                stage = 15
            }

            15 -> {
                options("Yes help me become one with my darker side.", "No I have my principles and honour.")
                stage = 16
            }

            16 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.HAPPY, "Yes help me become one with my darker side.")
                        stage = 30
                    }

                    2 -> {
                        player(FaceAnim.NEUTRAL, "No I have my principles and honour.")
                        stage = 17
                    }
                }

            17 -> npc(FaceAnim.HALF_GUILTY, "Suit yourself, but you're missing out.").also { stage = END_DIALOGUE }
            20 -> {
                npc(FaceAnim.HALF_GUILTY, "Yes it does seem to be getting fairly common", "knowledge.")
                stage = 21
            }

            21 -> {
                npc(FaceAnim.HALF_GUILTY, "I fear I may get a visit from the witch hunter of", "Falador before long.")
                stage = 22
            }

            22 -> end()
            30 -> {
                npc(FaceAnim.NEUTRAL, "Ok I'm going to make a potion to help bring out your", "darker self.")
                stage = 31
            }

            31 -> {
                npc(FaceAnim.NEUTRAL, "You will need certain ingredients.")
                stage = 32
            }

            32 -> {
                player(FaceAnim.NEUTRAL, "What do I need?")
                stage = 33
            }

            33 -> {
                npc(
                    FaceAnim.NEUTRAL,
                    "You need an eye of newt, a rat's tail, and onion... Oh",
                    "and a piece of burnt meat.",
                )
                stage = 34
            }

            34 -> {
                player(FaceAnim.HAPPY, "Great, I'll go get them.")
                stage = 35
            }

            35 -> {
                quest.start(player)
                quest.setStage(player, 20)
                end()
            }

            100 ->
                if (!player.inventory.containItems(1957, 300, 2146, 221)) {
                    player(FaceAnim.HALF_GUILTY, "I'm afraid I don't have all of them yet.")
                    stage = 101
                } else {
                    player(FaceAnim.HAPPY, "Yes I have everything!")
                    stage = 110
                }

            110 -> {
                npc(FaceAnim.HAPPY, "Excellent, can I have them then?")
                stage = 111
            }

            111 -> {
                interpreter.sendDialogue(
                    "You pass the ingredients to Hetty and she puts them all into her",
                    "cauldron. Hetty closes her eyes and begins to chant. The cauldron",
                    "bubbles mysteriously.",
                )
                stage = 112
            }

            112 -> {
                interpreter.sendDialogues(player, FaceAnim.NEUTRAL, "Well, is it ready?")
                stage = 113
            }

            113 ->
                if (player.inventory.remove(Item(1957), Item(300), Item(2146), Item(221))) {
                    quest.setStage(player, 40)
                    npc(FaceAnim.HAPPY, "Ok, now drink from the cauldron.")
                    stage = 114
                }

            114 -> end()
            101 -> {
                npc(
                    FaceAnim.NEUTRAL,
                    "Well I can't make the potion without them! Remember...",
                    "You need an eye of newt, a rat's tail, an onion, and a",
                    "piece of burnt meat. Off you go dear!",
                )
                stage = 102
            }

            102 -> end()
            500 -> end()
            41 -> {
                end()
                finishQuest(player, Quests.WITCHS_POTION)
                updateQuestTab(player)
            }

            200 ->
                npc(
                    "Ah, brewing up a cauldron of her infamous 'good",
                    "stuff' is she? Well of course I'll help.",
                ).also {
                    stage++
                }
            201 ->
                npc(
                    "I think this calls for one of my specialties: my famous",
                    "theurgical broom ointment.",
                ).also { stage++ }
            202 ->
                npc(
                    "Now, I can provide you with the ointment; all you'll",
                    "need to do is apply the ointment to the broom.",
                ).also {
                    stage++
                }
            203 -> player("Excellent!").also { stage++ }
            204 -> npc("But, first, I'll need a newt.").also { stage++ }
            205 -> player("A newt?").also { stage++ }
            206 ->
                npc(
                    "Yes, a newt. Now, if you'll just pop down to my cellar,",
                    "through that trapdoor out back, I've just had a recent",
                    "delivery of newts and toads.",
                ).also {
                    stage++
                }
            207 ->
                npc(
                    "The delivery ghoul is still here - I can't think what",
                    "could be taking him so long.",
                ).also { stage++ }
            208 ->
                npc(
                    "In any case, once the crates are unloaded, just bring",
                    "me a newt from the newt crate and I'll set you up with",
                    "the ointment.",
                ).also {
                    stage++
                }
            209 -> player("Okay, will do.").also { stage = END_DIALOGUE }

            210 -> player("Yes, it's right here.").also { stage++ }
            211 -> npc("Excellent. I'll just take it and add a little newt slime to", "the ointment.").also { stage++ }
            212 -> {
                replaceSlot(player, Item(Items.NEWT_14064, 1).slot, Item(Items.BROOM_OINTMENT_14062, 1))
                npc("That should do it. Just rub this ointment on the broom;", "that should suit Maggie's purposes.")
                stage++
            }
            213 -> player("Wonderful, thanks.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HETTY_307)
    }
}
