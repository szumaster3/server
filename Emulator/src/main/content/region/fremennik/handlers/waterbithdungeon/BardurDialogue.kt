package content.region.fremennik.handlers.waterbithdungeon

import core.api.*
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class BardurDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
            npcl(
                FaceAnim.FRIENDLY,
                "Ah! Outerlander! Do not interrupt me in my business! I must cull these fiends!",
            ).also {
                stage =
                    0
            }
            return true
        }

        if (player.username.equals("Bardur", true)) {
            npcl(
                FaceAnim.HAPPY,
                if (!player.isMale) {
                    "Ah, Bardur my sister-in-name! It is good fortune that we meet here like this!"
                } else {
                    "Ah, Bardur my brother-in-name! It is good fortune that we meet here like this!"
                },
            ).also { stage = 4 }
        } else {
            npcl(FaceAnim.FRIENDLY, "Hello there Bardur. How's it going?").also { stage = 4 }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.FRIENDLY, "What are you doing down here?").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Only an outerlander would ask such a question! Is it not obvious what I am doing?",
                ).also {
                    stage++
                }
            2 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I kill the daggermouths for glory and for sport! When I have had my fill, I move on to the daggermouths' lair!",
                ).also {
                    stage++
                }
            3 -> playerl(FaceAnim.FRIENDLY, "Erm... okay then. I'll leave you to it.").also { stage = END_DIALOGUE }
            5 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Listen, ${getAttribute(
                        player,
                        "fremennikname",
                        "name",
                    )}. I'm sorry about the way I acted while you were exiled.",
                ).also {
                    stage++
                }
            6 -> sendDialogue(player!!, "Bardur gets hostile around outerlanders.").also { stage++ }
            7 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Don't worry about it, I understand! All's well that ends well! How've you been?",
                ).also {
                    stage++
                }
            8 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I have been here all the long day, slaughtering the daggermouth spawns so that none may pass!",
                ).also {
                    stage++
                }
            9 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "My sword arm grows weary, and the blood of my foes hangs heavy upon my clothing!",
                ).also {
                    stage++
                }
            10 -> playerl(FaceAnim.FRIENDLY, "Can you tell me anything about the caves down here?").also { stage++ }
            11 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Aye, that I can $${getAttribute(
                        player,
                        "fremennikname",
                        "name",
                    )}! Yonder lies the lair of the daggermouth kings, the three beasts of legend!",
                ).also {
                    stage++
                }
            12 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I train my skills upon its foul brood, to prepare myself for the fight ahead!",
                ).also {
                    stage++
                }
            13 -> playerl(FaceAnim.FRIENDLY, "Okay, thanks Bardur. Do you have any food with you?").also { stage++ }
            14 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Ah, you did not come prepared $${getAttribute(player, "fremennikname", "name")}?",
                ).also {
                    stage++
                }
            15 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I have nothing to spare, but my equipment grows weaker under the onslaught of the dagger-mouth spawns!",
                ).also {
                    stage++
                }
            16 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I will trade you a finely cooked shark if you have a replacement for my helm, my shield or my blade...",
                ).also {
                    stage++
                }
            17 ->
                if (!anyInInventory(player, *BardurExchangeListener.exchangeItemIDs)) {
                    playerl(
                        FaceAnim.SAD,
                        "Sorry, I don't have anything you'd be after...",
                    ).also { stage = END_DIALOGUE }
                } else {
                    npcl(
                        FaceAnim.FRIENDLY,
                        "Give me any equipment you wish to trade, and I will pay you a shark for it.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return BardurDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BARDUR_2879)
    }
}

class BardurExchangeDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.BARDUR_2879)
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Ah, just what I was looking for! You wish to trade me that for a cooked shark?",
                ).also { stage++ }

            1 -> sendDialogueOptions(player!!, "Trade with Bardur?", "YES", "NO").also { stage++ }
            2 ->
                when (buttonID) {
                    1 ->
                        if ((
                                inInventory(player!!, Items.FREMENNIK_HELM_3748) &&
                                    removeItem(
                                        player!!,
                                        Items.FREMENNIK_HELM_3748,
                                    )
                            ) ||
                            (
                                inInventory(player!!, Items.FREMENNIK_BLADE_3757) &&
                                    removeItem(
                                        player!!,
                                        Items.FREMENNIK_BLADE_3757,
                                    )
                            ) ||
                            (
                                inInventory(player!!, Items.FREMENNIK_SHIELD_3758) &&
                                    removeItem(
                                        player!!,
                                        Items.FREMENNIK_SHIELD_3758,
                                    )
                            )
                        ) {
                            end()
                            addItemOrDrop(player!!, Items.SHARK_385)
                            npcl(
                                FaceAnim.FRIENDLY,
                                "Ah, this will serve me well as a backup! My thanks to you ${
                                    getAttribute(
                                        player!!,
                                        "fremennikname",
                                        "name",
                                    )
                                }, I trust we will one day sing songs together of glorious battles in the Rellekka longhall!",
                            )
                        }

                    2 -> {
                        end()
                        npcl(
                            FaceAnim.FRIENDLY,
                            "As you wish, ${
                                getAttribute(
                                    player!!,
                                    "fremennikname",
                                    "name",
                                )
                            }. My weapons have lasted me this long, I will keep my trust in them yet.",
                        )
                    }
                }
        }
    }
}
