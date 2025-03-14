package content.region.misthalin.quest.anma.dialogue

import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class OldCroneDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        val questStage = getQuestStage(player!!, Quests.ANIMAL_MAGNETISM)
        val hasCroneAmulet = hasAnItem(player!!, Items.CRONE_MADE_AMULET_10500).container != null
        npc = NPC(NPCs.OLD_CRONE_1695)

        when (questStage) {
            16 ->
                when (stage) {
                    0 -> player("I'm here about the farmers east of here.").also { stage++ }
                    1 ->
                        player(
                            "Alice and her husband are having trouble talking to one",
                            "another and said you might be able to help.",
                        ).also {
                            stage++
                        }
                    2 ->
                        npc(
                            "Ah, I know them; shame about those cows. Why would",
                            "they think that I could help?",
                        ).also { stage++ }
                    3 ->
                        player(
                            "Alice seems to think you could alter a ghostspeak amulet",
                            "in order to allow them to communicate.",
                        ).also {
                            stage++
                        }
                    4 ->
                        npc(
                            "Well, the poor young lady has such family problems; I",
                            "quite feel her pain. I'd be happy to help.",
                        ).also {
                            stage++
                        }
                    5 ->
                        npc(
                            "You seem to have one of her golden hairs on your",
                            "shoulder, so I can use that...",
                        ).also { stage++ }
                    6 ->
                        sendDialogue(
                            player!!,
                            "In a flash, the crone whisks away an unseen hair from your shoulder.",
                        ).also { stage++ }
                    7 ->
                        npc(
                            "Talk to me again with a ghostspeak amulet and some",
                            "space in your backpack and I'll be ready to work on",
                            "the little good deed. The way I plan is quite simple,",
                            "really.",
                        ).also {
                            stage++
                        }
                    8 ->
                        npc(
                            "I can mirror part of the unused mystical essence of the",
                            "ghostspeak amulet, bind it with Alice's hair and thus",
                            "create a second amulet.",
                        ).also {
                            stage++
                        }
                    9 ->
                        npc(
                            "The second amulet will be useful for the purpose you",
                            "desire, thought it won't work for any other ghost or",
                            "human other than the farmer and his wife.",
                        ).also {
                            stage++
                        }
                    10 -> {
                        end()
                        setQuestStage(player!!, Quests.ANIMAL_MAGNETISM, 17)
                    }
                }

            17 ->
                when (stage) {
                    0 -> player("I'm here about the farmers east of here.").also { stage++ }
                    1 ->
                        player(
                            "I'm here to see if you are ready to do your mystical",
                            "stuff with my ghostspeak amulet.",
                        ).also { stage++ }
                    2 -> {
                        if (freeSlots(player!!) < 1) {
                            npc("I most certainly am, but you don't have enough", "space in your backpack.")
                            stage++
                        } else if (!inEquipmentOrInventory(player!!, Items.GHOSTSPEAK_AMULET_552)) {
                            npc("I most certainly am, but you don't have an ghostspeak", "amulet.")
                            stage++
                        } else {
                            npc("I most certainly am; there you go.")
                            stage += 2
                        }
                    }
                    3 -> end()
                    4 -> player("Wow, that was quick and painless.").also { stage++ }
                    5 -> npc("Just being a good neighbour.").also { stage++ }
                    6 -> {
                        end()
                        addItemOrDrop(player!!, Items.CRONE_MADE_AMULET_10500)
                        setQuestStage(player!!, Quests.ANIMAL_MAGNETISM, 18)
                    }
                }

            18 ->
                when (stage) {
                    0 ->
                        if (!hasCroneAmulet) {
                            player(
                                "Would you be able to replace the amulet you made? I",
                                "seem to have lost it.",
                            ).also {
                                stage =
                                    4
                            }
                        } else {
                            npc("Yes?").also { stage++ }
                        }

                    1 ->
                        player(
                            "Well, to tell the truth, I just came back to chat with",
                            "you. Any news?",
                        ).also { stage++ }
                    2 ->
                        npc("Disgraceful! Deliver that amulet; a young lady's", "happiness depends upon it.").also {
                            stage =
                                END_DIALOGUE
                        }
                    4 ->
                        npc(
                            "Here you are; luckily, I saved some of Alice's hair in",
                            "case you were careless. Which you were.",
                        ).also {
                            stage++
                        }
                    5 -> {
                        if (freeSlots(player!!) < 1) {
                            npc("You don't have enough space in your inventory.")
                            stage = 3
                        } else if (!inEquipmentOrInventory(player!!, Items.GHOSTSPEAK_AMULET_552)) {
                            npc("You need to bring me a ghostspeak amulet.")
                            stage = 3
                        } else {
                            addItemOrDrop(player!!, Items.CRONE_MADE_AMULET_10500)
                            end()
                        }
                    }
                }
        }
    }
}
