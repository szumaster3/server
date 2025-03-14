package content.region.asgarnia.quest.death.dialogue

import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.Quests

class TenzingDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (getQuestStage(player!!, Quests.DEATH_PLATEAU)) {
            20 -> {
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hello!").also { stage++ }
                    1 -> npcl(FaceAnim.FRIENDLY, "Hello. How can I help?").also { stage++ }
                    2 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "I'm helping the Imperial Guard. They need to find a way to sneak up Death Plateau to destroy the troll camp! Saba seemed to think you'd be able to help.",
                        ).also { stage++ }

                    3 -> npcl(FaceAnim.FRIENDLY, "Ah...Saba is still alive and kicking?").also { stage++ }
                    4 -> playerl(FaceAnim.FRIENDLY, "Yeh, he seemed very bitter.").also { stage++ }
                    5 -> npcl(FaceAnim.FRIENDLY, "That's Saba alright!").also { stage++ }
                    6 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I do know of a secret way up to Death Plateau, the Imperial Guard would be able to use it at night and not be seen until it was too late!",
                        ).also { stage++ }

                    7 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I'd be happy to show you it if you do something for me first.",
                        ).also { stage++ }

                    8 -> playerl(FaceAnim.FRIENDLY, "Name it.").also { stage++ }
                    9 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I don't get into town much and I'm getting low on supplies. I need ten loaves of bread and ten cooked trout, that should see me through the winter.",
                        ).also { stage++ }

                    10 -> playerl(FaceAnim.FRIENDLY, "Anything else?").also { stage++ }
                    11 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Yes. My climbing boots need to have new spikes, so can you take them to Dunstan in Burthorpe? He always puts my spikes on for me.",
                        ).also { stage++ }

                    12 ->
                        showTopics(
                            Topic(FaceAnim.FRIENDLY, "OK, I'll get those for you.", 20),
                            Topic(FaceAnim.FRIENDLY, "I'll find the secret way for myself.", 30),
                        )

                    20 -> npcl(FaceAnim.FRIENDLY, "Thank you traveller!").also { stage++ }
                    21 ->
                        sendItemDialogue(
                            player!!,
                            Items.CLIMBING_BOOTS_3105,
                            "Tenzing has given you his climbing boots.",
                        ).also {
                            sendMessage(player!!, "Tenzing has given you his climbing boots.")
                            addItemOrDrop(player!!, Items.CLIMBING_BOOTS_3105, 1)
                            setQuestStage(player!!, Quests.DEATH_PLATEAU, 21)
                            stage = END_DIALOGUE
                        }

                    30 -> npcl(FaceAnim.ANNOYED, "Hmph.").also { stage = END_DIALOGUE }
                }
            }

            in 21..23 -> {
                when (stage) {
                    START_DIALOGUE ->
                        playerl(FaceAnim.FRIENDLY, "Hello!").also {
                            if (inInventory(player!!, Items.CLIMBING_BOOTS_3105) ||
                                inInventory(
                                    player!!,
                                    Items.SPIKED_BOOTS_3107,
                                )
                            ) {
                                stage = 1
                            } else {
                                stage = 5
                            }
                        }

                    1 -> npcl(FaceAnim.FRIENDLY, "Has Dunstan added spikes to my climbing boots yet?").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "No, not yet.").also { stage++ }
                    3 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Well, when he has, bring the boots to me with ten loaves of bread and ten cooked trout. I have to prepare for the winter, after all.",
                        ).also { stage = END_DIALOGUE }

                    5 -> npcl(FaceAnim.FRIENDLY, "Have you brought me the items I asked for?").also { stage++ }
                    6 -> playerl(FaceAnim.FRIENDLY, "I've lost the climbing boots.").also { stage++ }
                    7 -> npcl(FaceAnim.FRIENDLY, "These are expensive, do not lose another pair!").also { stage++ }
                    8 ->
                        sendItemDialogue(
                            player!!,
                            Items.CLIMBING_BOOTS_3105,
                            "Tenzing has given you his climbing boots.",
                        ).also {
                            sendMessage(player!!, "Tenzing has given you his climbing boots.")
                            addItemOrDrop(player!!, Items.CLIMBING_BOOTS_3105, 1)
                            stage = END_DIALOGUE
                        }
                }
            }

            24 -> {
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hello!").also { stage++ }
                    1 ->
                        npcl(FaceAnim.FRIENDLY, "Have you brought me the items I asked for?").also {
                            if (inInventory(player!!, Items.SPIKED_BOOTS_3107) &&
                                inInventory(
                                    player!!,
                                    Items.BREAD_2309,
                                    10,
                                ) &&
                                inInventory(player!!, Items.TROUT_333, 10)
                            ) {
                                stage = 3
                            } else {
                                stage = 2
                            }
                        }

                    2 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "I don't have the:" +
                                (if (!inInventory(player!!, Items.SPIKED_BOOTS_3107)) " Spiked boots." else "") +
                                (
                                    if (!inInventory(
                                            player!!,
                                            Items.BREAD_2309,
                                            10,
                                        )
                                    ) {
                                        " Ten loaves of bread."
                                    } else {
                                        ""
                                    }
                                ) +
                                (if (!inInventory(player!!, Items.TROUT_333, 10)) " Ten cooked trout." else ""),
                        ).also {
                            stage = END_DIALOGUE
                        }

                    3 -> {
                        if (!removeItem(player!!, Item(Items.SPIKED_BOOTS_3107))) {
                            end()
                        } else {
                            sendItemDialogue(player!!, Items.SPIKED_BOOTS_3107, "You give Tenzing the Spiked boots.")
                            sendMessage(player!!, "You give Tenzing the Spiked boots.")
                            stage++
                        }
                    }

                    4 -> {
                        if (!inInventory(player!!, Items.BREAD_2309, 10) &&
                            !inInventory(
                                player!!,
                                Items.TROUT_333,
                                10,
                            )
                        ) {
                            end()
                        } else {
                            removeItem(player!!, Item(Items.BREAD_2309, 10))
                            removeItem(player!!, Item(Items.TROUT_333, 10))
                            sendDoubleItemDialogue(
                                player!!,
                                Items.TROUT_333,
                                Items.BREAD_2309,
                                "You give Tenzing the loaves of bread and the cooked trout.",
                            )
                            sendMessage(player!!, "You give Tenzing the loaves of bread and the cooked trout.")
                            stage++
                        }
                    }

                    5 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Thank you very much traveller. I'm now ready for the winter!",
                        ).also { stage++ }

                    6 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "You said you would show me the secret way to Death Plateau?",
                        ).also { stage++ }

                    7 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Yes, of course! I drew up a map in case I ever needed to use it again.",
                        ).also { stage++ }

                    8 ->
                        sendItemDialogue(
                            player!!,
                            Items.SECRET_WAY_MAP_3104,
                            "Tenzing has given you a map of the secret way!",
                        ).also {
                            addItemOrDrop(player!!, Items.SECRET_WAY_MAP_3104)
                            sendMessage(player!!, "Tenzing has given you a map of the secret way!")
                            stage++
                        }

                    9 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I don't think the Trolls have found the secret way yet, if they had I would've been attacked by now.",
                        ).also { stage++ }

                    10 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "OK thanks but I think I'd better check the path. I don't want to send the Imperial Guards to their death!",
                        ).also { stage++ }

                    11 ->
                        npcl(FaceAnim.FRIENDLY, "You are wise for one so young.").also {
                            setQuestStage(player!!, Quests.DEATH_PLATEAU, 25)
                            stage = END_DIALOGUE
                        }
                }
            }

            in 25..29 -> {
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hello Tenzing!").also { stage++ }
                    1 ->
                        npcl(FaceAnim.FRIENDLY, "Hello again traveller. What can I do for you?").also {
                            if (inInventory(player!!, Items.SECRET_WAY_MAP_3104)) {
                                stage = 10
                            } else {
                                stage = 2
                            }
                        }

                    2 -> playerl(FaceAnim.FRIENDLY, "I've lost the secret way map.").also { stage++ }
                    3 -> npcl(FaceAnim.FRIENDLY, "Never mind. I'll quickly draw you another one.").also { stage++ }
                    4 ->
                        sendDialogue(player!!, "Tenzing has given you a map of the secret way!").also {
                            addItemOrDrop(player!!, Items.SECRET_WAY_MAP_3104)
                            stage = END_DIALOGUE
                        }

                    10 ->
                        showTopics(
                            Topic(FaceAnim.FRIENDLY, "I'm lost!", 20),
                            Topic(FaceAnim.FRIENDLY, "Nothing, thanks.", 30),
                        )

                    20 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "To get back to Burthorpe follow the path going east.",
                        ).also { stage++ }

                    21 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I thought you were going to investigate the secret way to Death Plateau? Use the back door to my hut, hop over the stile and follow that path.",
                        ).also { stage++ }

                    22 -> playerl(FaceAnim.FRIENDLY, "Oh yes, of course! Thanks!").also { stage = END_DIALOGUE }
                    30 -> npcl(FaceAnim.FRIENDLY, "Go in peace traveller.").also { stage = END_DIALOGUE }
                }
            }
        }
    }
}
