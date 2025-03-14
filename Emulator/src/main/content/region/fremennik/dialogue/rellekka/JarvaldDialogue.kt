package content.region.fremennik.dialogue.rellekka

import content.region.fremennik.handlers.TravelDestination
import content.region.fremennik.handlers.WaterbirthTravel.sail
import core.api.getVarp
import core.api.quest.isQuestComplete
import core.api.sendDialogueOptions
import core.api.setVarp
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class JarvaldDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (args.size > 1) {
            handleTravelStage()
            return true
        }
        if (npc.id == NPCs.JARVALD_2438) {
            npc("Ah, you live yet, outerlander!").also { stage = 37 }
            return true
        }
        npc(
            "What do you want from me outerlander?",
            "It is our policy not to associate with those not of our",
            "tribe.",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Where is your chieftain?", "What Jarvald is doing.", "Nothing").also { stage++ }
            1 ->
                when (buttonId) {
                    1 ->
                        player(
                            "Where is your chieftain?",
                            "I find it highly discriminatory to refuse to talk to",
                            "someone on the grounds that they are not part of your",
                            "tribe.",
                        ).also {
                            stage =
                                10
                        }
                    2 -> player("So what are you doing here?").also { stage = 20 }
                    3 -> end()
                }
            10 ->
                npc(
                    "I don't rightly understand your speech outerlander, but",
                    "my loyalty is with Chieftain Brundt.",
                ).also {
                    stage++
                }
            11 ->
                npc(
                    "He resides in our longhall; it is the large building over",
                    "there, you should speak to him for he speaks for us all.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            12 -> end()
            20 -> handleTravelStage()
            21 -> player("Hey, scary barbarian type guy, think I can join you on", "this expedition?").also { stage++ }
            22 -> npc("An outerlander join us on a honoured hunt???").also { stage++ }
            23 ->
                npc(
                    "Well.....",
                    "I gues...",
                    "I might be able to allow you to join us, although it is a",
                    "breach of many of our customs...",
                ).also {
                    stage++
                }
            24 -> player("Oh, pleeeeeeease?", "I really LOVE killing stuff!").also { stage++ }
            25 ->
                npc(
                    "Well...",
                    "I remain unconvinced that it would be wise to allow an",
                    "outerlander to join us in such dangerous battle, but",
                    "your enthusiasm seems genuine enough...",
                ).also {
                    stage++
                }
            26 -> npc("I will allow you to escort us, but you must pay me a", "sum of money first.").also { stage++ }
            27 ->
                player(
                    "What?",
                    "That's outrageous, why charge me money?",
                    "And, uh, how much does it cost me?",
                ).also { stage++ }
            28 ->
                npc(
                    "Ah, the outerlander have stolen from my people for",
                    "many years, in this way you can help my community",
                    "with a small amount of money...",
                ).also {
                    stage++
                }
            29 -> npc("Let us say...", "1000 coins.", "Payable in advance, of course.").also { stage++ }
            30 ->
                npc(
                    "For this I will take you to Waterbirth Island on my",
                    "boat, and will bring you back here when you have had",
                    "your fill of the hunt.",
                    "Assuming you are still alive to wish to leave, of course.",
                ).also {
                    stage++
                }
            31 -> {
                setVarp(player, 520, 1 shl 13, true)
                end()
            }
            32 -> options("YES", "NO").also { stage++ }
            33 ->
                when (buttonId) {
                    1 ->
                        if (!isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS) &&
                            !player.inventory.contains(995, 1000)
                        ) {
                            player("Sorry, I don't have enough coins.").also { stage = END_DIALOGUE }
                        } else if (!isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS) &&
                            player.inventory.remove(Item(995, 1000))
                        ) {
                            end()
                            sail(player, TravelDestination.RELLEKKA_TO_WATERBIRTH)
                            npcl(
                                FaceAnim.FRIENDLY,
                                "I suggest you head to the cave with some urgency outerlander, the cold air out here might be too much for the likes of you...",
                            )
                        } else {
                            end()
                            sail(player, TravelDestination.RELLEKKA_TO_WATERBIRTH)
                            npcl(
                                FaceAnim.FRIENDLY,
                                "I would head straight for the cave and not tarry too long " + player.username +
                                    ", the cold winds on this island can cut right through you should you spend too long in them.",
                            )
                        }

                    2 -> player("No, actually I have some stuff to do here first.").also { stage = 34 }
                }
            34 -> npc("As you wish.", "Come and see me when your bloodlust needs sating.").also { stage = END_DIALOGUE }
            36 ->
                when (buttonId) {
                    1 -> npc("Then let us away;", "There will be death to bring here another day!").also { stage = 39 }
                    2 -> end()
                }
            37 ->
                npc(
                    "Have you had your fill of the hunt and wish to return,",
                    "or are you still feeling the joy of the cull?",
                ).also {
                    stage++
                }
            38 -> sendDialogueOptions(player, "Leave island?", "YES", "NO").also { stage = 36 }
            39 -> {
                end()
                sail(player, TravelDestination.WATERBIRTH_TO_RELLEKKA)
            }
        }
        return true
    }

    private fun handleTravelStage() {
        if (npc.id == NPCs.JARVALD_2438) {
            sendDialogueOptions(player, "Leave island?", "YES", "NO")
            stage = 36
            return
        }
        stage =
            if (getVarp(player, 520) == 0) {
                npc(
                    "This should not concern you, outerlander.",
                    "I am awaiting other Fremenniks to join me on an",
                    "expedition to Waterbirth Island.",
                )
                21
            } else {
                if (isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
                    npcl(
                        FaceAnim.FRIENDLY,
                        "Of course, " + player.username +
                            "! Your presence is more than welcome on this cull! You wish to leave now?",
                    )
                    32
                } else {
                    npc("So do you have the 1000 coins for my service, and are", "you ready to leave?")
                    32
                }
            }
    }

    override fun newInstance(player: Player?): Dialogue {
        return JarvaldDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(2435, NPCs.JARVALD_2436, NPCs.JARVALD_2437, NPCs.JARVALD_2438)
    }
}
