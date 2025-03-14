package content.region.kandarin.quest.biohazard.dialogue

import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class ChemistDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.CHEMIST_367)
        when {
            (getQuestStage(player!!, Quests.BIOHAZARD) in 10..15) -> {
                when (stage) {
                    0 ->
                        npc(
                            "Sorry, I'm afraid we're just closing now. You'll have to",
                            "come back another time.",
                        ).also { stage++ }

                    1 ->
                        options(
                            "This can't wait, I'm carrying a plague sample.",
                            "It's ok, I'm Elena's friend.",
                        ).also { stage++ }

                    2 ->
                        when (buttonID) {
                            1 -> player("This can't wait, I'm carrying a plague sample.").also { stage++ }
                            2 -> player("It's ok, I'm Elena's friend.").also { stage = 6 }
                        }

                    3 -> npc("You idiot! A plague sample should be confined to a lab!").also { stage++ }
                    4 ->
                        if (!removeItem(player!!, Items.PLAGUE_SAMPLE_418)) {
                            end()
                        } else {
                            npc("I'm taking it off you. I'm afraid it's the only responsible thing to do.")
                        }

                    6 ->
                        npc(
                            "Oh, well that's different then. Must be pretty important",
                            "to come all this way.",
                        ).also { stage++ }

                    7 -> npc("How's everyone doing there anyway? Wasn't there", "some plague scare?").also { stage++ }
                    8 ->
                        options(
                            "I need some more touch paper for this plague sample.",
                            "I just need some touch paper for a guy called Guidor.",
                        ).also { stage++ }

                    9 ->
                        when (buttonID) {
                            1 -> player("I need some more touch paper for this", "plague sample.").also { stage++ }
                            2 -> player("I just need some touch paper for a guy", "called Guidor.").also { stage = 11 }
                        }

                    10 -> player("Who knows... I just need some touch paper for a guy called Guidor.").also { stage++ }
                    11 ->
                        npc(
                            "Guidor? This one's on me then... the poor guy. Sorry",
                            "for the interrogation",
                        ).also { stage++ }

                    12 ->
                        npc(
                            "It's just that there've been rumours of a " + (if (player!!.isMale) "man" else "woman") +
                                " travelling with the plague on " +
                                (if (player!!.isMale) "him" else "her") +
                                ".",
                        ).also { stage++ }
                    13 ->
                        npc(
                            "They're even doing spot checks in Varrock. It's a",
                            "pharmaceutical disaster!",
                        ).also { stage++ }

                    14 ->
                        player(
                            "Oh right... so am I going to be ok carrying these three",
                            "vials with me?",
                        ).also { stage++ }

                    15 ->
                        npc(
                            "With touch paper as well? You're asking for trouble.",
                            "You'd better use my errand boys, outside. Give them a",
                            "vial each.",
                        ).also { stage++ }

                    16 ->
                        npc(
                            "They're not the most reliable people in the world. One's",
                            "a painter, one's a gambler, and one's a drunk. Still if",
                            "you pay peanuts you'll get monkeys, right?",
                        ).also { stage++ }

                    17 ->
                        npc(
                            "It's better than entering Varrock with half a laboratory",
                            "in your napsack.",
                        ).also { stage++ }

                    18 ->
                        player(
                            FaceAnim.NOD_YES,
                            "Ok, thanks for your help. I know Elena appreciates it.",
                        ).also { stage++ }

                    19 ->
                        npc(
                            "Yes well don't stand around here gassing. You'd better",
                            "hurry if you want to see Guidor... He won't be around",
                            "for much longer.",
                        ).also { stage++ }

                    20 -> {
                        end()
                        sendMessage(player!!, "He gives you the touch paper.")
                        addItemOrDrop(player!!, Items.TOUCH_PAPER_419)
                        setQuestStage(player!!, Quests.BIOHAZARD, 11)
                    }
                }
            }
        }
    }
}
