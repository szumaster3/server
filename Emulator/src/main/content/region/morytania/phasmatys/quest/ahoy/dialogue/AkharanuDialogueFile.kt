package content.region.morytania.phasmatys.quest.ahoy.dialogue

import content.region.morytania.phasmatys.quest.ahoy.plugin.GhostsAhoyUtils
import core.api.addItemOrDrop
import core.api.inInventory
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.sendItemDialogue
import core.api.setAttribute
import core.game.dialogue.DialogueFile
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class AkharanuDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        val questStage = getQuestStage(player!!, Quests.GHOSTS_AHOY)
        npc = NPC(NPCs.AK_HARANU_1688)
        when (questStage) {
            in 4..5 ->
                when (stage) {
                    0 -> player("It's nice to see a human face around here.").also { stage++ }
                    1 ->
                        npc(
                            "My name is Ak-Haranu. I am trader, come",
                            "from many far across sea in east.",
                        ).also { stage++ }
                    2 ->
                        npc(
                            "You come from the lands of the East?",
                            "Do you have anything that can help me translate a book that",
                            " is scribed in your language?",
                        ).also {
                            stage++
                        }
                    3 ->
                        npc(
                            "Ak-Haranu may help you. A translation manual I have,",
                            " much good for reading Eastern language.",
                        ).also {
                            stage++
                        }
                    4 -> player("How much do you want for it?").also { stage++ }
                    5 ->
                        npc(
                            "Ak-Haranu not want money for this book,",
                            "as is such small thing. But there may be something you",
                            "could do for me. I am big admirer of Robin, Master Bowman.",
                            "He staying in village inn.",
                        ).also {
                            stage++
                        }
                    6 -> player("What would you like me to do?").also { stage++ }
                    7 ->
                        npc(
                            "Please get Master Bowman sign an oak shieldbow for me,",
                            "so Ak-Haranu can show family and friends when returning",
                            "home and become much admired. Then I give you",
                            "book in exchange.",
                        ).also {
                            stage++
                        }
                    8 ->
                        options(
                            "Okay, wait here - I'll get you your bow.",
                            "Sorry, I have too much to do at the moment.",
                        ).also { stage++ }
                    9 ->
                        when (buttonID) {
                            1 -> player("Okay, wait here - I'll get you your bow.").also { stage++ }
                            2 -> player("Sorry, I have too much to do at the moment.").also { stage = END_DIALOGUE }
                        }
                    10 -> {
                        end()
                        setAttribute(player!!, GhostsAhoyUtils.getSignedBow, false)
                    }
                }

            in 6..11 ->
                when (stage) {
                    0 ->
                        if (!inInventory(player!!, Items.SIGNED_OAK_BOW_4236)) {
                            player(
                                "Have you got an oak shieldbow that",
                                "I can get Robin to sign for you?",
                            ).also { stage++ }
                        } else {
                            player("I have your signed longbow for you.").also { stage += 2 }
                        }

                    1 ->
                        npc(
                            "No, Ak-Haranu afraid that no shieldbow in supply at",
                            "moment. You must make one or buy one.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    2 -> npc("Ah can it be true? You have obtained bow from Master", "Bowman?").also { stage++ }
                    3 -> player("He was more than happy to oblige (cough).", "Here you are.").also { stage++ }
                    4 ->
                        sendItemDialogue(
                            player!!,
                            Items.TRANSLATION_MANUAL_4249,
                            "Ak-Haranu gives you a translation manual in return for the signed oak longbow.",
                        ).also {
                            addItemOrDrop(player!!, Items.TRANSLATION_MANUAL_4249)
                            stage++
                        }
                    5 -> npc("May honour be bestowed upon you and your family!").also { stage++ }
                    6 -> {
                        end()
                        setQuestStage(player!!, Quests.GHOSTS_AHOY, 80)
                    }
                }
        }
    }
}
