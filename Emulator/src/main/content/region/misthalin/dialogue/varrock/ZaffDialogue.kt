package content.region.misthalin.dialogue.varrock

import core.ServerStore
import core.ServerStore.Companion.getInt
import core.api.*
import core.api.interaction.openNpcShop
import core.cache.def.impl.NPCDefinition
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.InputType
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import org.json.simple.JSONObject
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class ZaffDialogue : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any?> {
        NPCDefinition.setOptionHandler("buy-battlestaves", this)
        ZaffQuestDialogue().init()
        ZaffBattlestaffsDialogue().init()
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        player.dialogueInterpreter.open(9679)
        return true
    }

    class ZaffQuestDialogue : Dialogue {
        private var quest: Quest? = null

        constructor()
        constructor(player: Player?) : super(player)

        override fun newInstance(player: Player?): Dialogue {
            return ZaffQuestDialogue(player)
        }

        override fun open(vararg args: Any): Boolean {
            npc = args[0] as NPC
            quest = player.questRepository.getQuest(Quests.WHAT_LIES_BELOW)
            npc(
                FaceAnim.HALF_GUILTY,
                "Would you like to buy or sell some staves or is there",
                "something else you need?",
            )
            stage = 0
            return true
        }

        override fun handle(
            interfaceId: Int,
            buttonId: Int,
        ): Boolean {
            when (stage) {
                0 -> {
                    if (quest!!.getStage(player) == 60) {
                        sendDialogueOptions(
                            player,
                            "Select an Option",
                            "Yes, please.",
                            "No, thank you.",
                            "Rat Burgiss sent me.",
                        )
                        stage = 1
                    } else if (quest!!.getStage(player) == 80) {
                        sendDialogueOptions(
                            player,
                            "Select an Option",
                            "Yes, please.",
                            "No, thank you.",
                            "We did it! We beat Surok!",
                        )
                        stage = 1
                    } else if (quest!!.getStage(player) >= 70) {
                        sendDialogueOptions(
                            player,
                            "Select an Option",
                            "Yes, please.",
                            "No, thank you.",
                            "Can I have another ring?",
                        )
                        stage = 1
                    } else {
                        sendDialogueOptions(player, "Select an Option", "Yes, please.", "No, thank you.")
                        stage = 1
                    }
                }

                1 ->
                    when (buttonId) {
                        1 -> {
                            sendPlayerDialogue(player, "Yes, please.", FaceAnim.HALF_GUILTY)
                            stage = 10
                        }

                        2 -> {
                            sendPlayerDialogue(player, "No, thank you.", FaceAnim.HALF_GUILTY)
                            stage = 20
                        }

                        3 -> {
                            if (quest!!.getStage(player) == 60) {
                                player("Rat Burgiss sent me!")
                                stage = 70
                            } else if (quest!!.getStage(player) == 80) {
                                player("We did it! We beat Surok!")
                                stage = 200
                            } else {
                                sendPlayerDialogue(player, "Can I have another ring?", FaceAnim.HALF_GUILTY)
                                stage = 50
                            }
                        }
                    }

                10 -> {
                    if (player.achievementDiaryManager
                            .getDiary(DiaryType.VARROCK)!!
                            .levelRewarded
                            .contains(true)
                    ) {
                        npcl(FaceAnim.FRIENDLY, "Would you like to hear about my battlestaves?")
                        stage = 1000
                    } else {
                        end()
                        openNpcShop(player, NPCs.ZAFF_546)
                    }
                }

                20 -> {
                    npc(FaceAnim.HALF_GUILTY, "Well, 'stick' your head in again if you change your mind.")
                    stage = 21
                }

                21 -> {
                    player(FaceAnim.HALF_GUILTY, "Huh, terrible pun. You just can't get the 'staff' these", "days!")
                    stage = 22
                }

                22 -> end()
                50 -> {
                    if (inInventory(player, 11014, 1)) {
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "Go and get the one that's in your inventory " + player.username + "!",
                        )
                    } else if (player.bank.contains(11014, 1)) {
                        npc(FaceAnim.HALF_GUILTY, "Go and get the one that's in your bank" + player.username + "!")
                    } else if (inEquipment(player, 11014, 1)) {
                        npc(FaceAnim.HALF_GUILTY, "Go and get the one that's on your finger " + player.username + "!")
                    } else {
                        npc(FaceAnim.HALF_GUILTY, "Of course you can! Here you go " + player.username + "!")
                        player.inventory.add(BEACON_RING)
                        addItemOrDrop(player, Items.ZAFFS_INSTRUCTIONS_11011)
                    }
                    stage = 51
                }

                51 -> end()
                70 -> {
                    npc(
                        "Ah, yes; You must be " + player.username + "! Rat sent word that you",
                        "would be coming. Everything is prepared. I have created",
                        "a spell that will remove the mind control spell.",
                    )
                    stage++
                }

                71 -> {
                    player("Okay, what's the plan?")
                    stage++
                }

                72 -> {
                    npc(
                        "Listen carefully. For the spell to succeed, the king must",
                        "be made very weak, if his mind is controlled, you will",
                        "need to fight him until he is all but dead.",
                    )
                    stage++
                }

                73 -> {
                    npc(
                        "Then and ONLY then, use your ring to summon me.",
                        "I will teleport to you and cast the spell that will",
                        "cure the king.",
                    )
                    stage++
                }

                74 -> {
                    player("Why must I summon you? Can't you come with me?")
                    stage++
                }

                75 -> {
                    npc(
                        "I cannot. I must look after my shop here and",
                        "I have lots to do. Rest assured, I will come when you",
                        "summon me.",
                    )
                    stage++
                }

                76 -> {
                    player("Okay, so what do I do now?")
                    stage++
                }

                77 -> {
                    npc("Take this beacon ring and some instructions.")
                    stage++
                }

                78 -> {
                    npc("Once you have read the instructions. It will be time for", "you to arrest Surok.")
                    stage++
                }

                79 -> {
                    player("Won't he be disinclined to acquiesce to that request?")
                    stage++
                }

                80 -> {
                    npc("Won't he what?")
                    stage++
                }

                81 -> {
                    player("Won't he refuse?")
                    stage++
                }

                82 -> {
                    npc(
                        "I very much expect so. It may turn nasty, so be on your",
                        "guard. I hope we can stop him before he can cast his",
                        "spell!",
                        "Make sure you have that ring I gave you.",
                    )
                    stage++
                }

                83 -> {
                    player("Okay, thanks, Zaff!")
                    stage++
                }

                84 -> {
                    player.inventory.add(BEACON_RING)
                    addItemOrDrop(player, Items.ZAFFS_INSTRUCTIONS_11011)
                    quest!!.setStage(player, 70)
                    end()
                }

                200 -> {
                    npc("Yes. You have done well, " + player.username + ". You are to be", "commended for you actions!")
                    stage++
                }

                201 -> {
                    player("It was all in the call of duty!")
                    stage++
                }

                202 -> {
                    player("What will happen with Surok now?")
                    stage++
                }

                203 -> {
                    npc(
                        "Well, when I disrupted Surok's spell, he will have been",
                        "sealed in the library, but we still need to keep an",
                        "eye on him, just in case.",
                    )
                    stage++
                }

                204 -> {
                    npc("When you are ready, report back to Rat and he will", "reward you.")
                    stage++
                }

                205 -> {
                    player("Okay, I will.")
                    stage++
                }

                206 -> {
                    quest!!.setStage(player, 90)
                    end()
                }

                1000 -> options("Yes, please.", "No, thanks.").also { stage++ }
                1001 ->
                    when (buttonId) {
                        1 -> {
                            end()
                            openDialogue(player, 9679, npc)
                        }

                        2 -> {
                            end()
                            openNpcShop(player, NPCs.ZAFF_546)
                        }
                    }
            }
            return true
        }

        override fun getIds(): IntArray {
            return intArrayOf(NPCs.ZAFF_546)
        }

        companion object {
            private val STAFF = Item(11014, 1)
        }
    }

    inner class ZaffBattlestaffsDialogue : Dialogue {
        private var ammount = 0

        constructor()
        constructor(player: Player?) : super(player)

        override fun newInstance(player: Player?): Dialogue {
            return ZaffBattlestaffsDialogue(player)
        }

        override fun open(vararg args: Any): Boolean {
            ammount = getStoreFile().getInt(player.username.lowercase())
            player(FaceAnim.HALF_GUILTY, "Do you have any battlestaves?")
            stage = 0
            return true
        }

        override fun handle(
            interfaceId: Int,
            buttonId: Int,
        ): Boolean {
            when (stage) {
                0 -> {
                    if (ammount >= maxStaffs) {
                        sendNPCDialogue(
                            player,
                            NPCs.ZAFF_546,
                            "I'm very sorry! I seem to be out of battlestaves at the moment! I expect I'll get some more in by tomorrow, though.",
                            FaceAnim.HALF_GUILTY,
                        )
                        stage = 2
                        return true
                    }
                    sendNPCDialogueLines(
                        player,
                        NPCs.ZAFF_546,
                        FaceAnim.HAPPY,
                        false,
                        "Battlestaves cost 7,000 gold pieces each. I have ${maxStaffs - ammount} left.",
                        "How many would you like to buy?",
                    )
                    stage = 1
                }

                1 ->
                    end().also {
                        sendInputDialogue(player, InputType.AMOUNT, "Enter an amount:") { value ->
                            ammount = getStoreFile().getInt(player.username.lowercase())
                            var amt = value as Int
                            if (amt > maxStaffs - ammount) amt = maxStaffs - ammount
                            val coinage = amt * 7000
                            if (!inInventory(player, Items.COINS_995, coinage)) {
                                sendDialogue(player, "You can't afford that many.")
                                return@sendInputDialogue
                            }

                            if (amt == 0) {
                                return@sendInputDialogue
                            }

                            if (removeItem(player, Item(Items.COINS_995, coinage), Container.INVENTORY)) {
                                addItem(player, Items.BATTLESTAFF_1392, amt)
                                getStoreFile()[player.username.lowercase()] = amt + ammount
                            }
                        }
                    }

                2 -> player(FaceAnim.HALF_GUILTY, "Oh, okay then. I'll try again another time.").also { stage++ }

                3 -> end()
            }
            return true
        }

        val maxStaffs: Int
            get() {
                val level = player.achievementDiaryManager.getDiary(DiaryType.VARROCK)!!.level
                return when (level) {
                    2 -> 64
                    1 -> 32
                    0 -> 16
                    else -> 8
                }
            }

        override fun getIds(): IntArray {
            return intArrayOf(9679)
        }
    }

    companion object {
        val BEACON_RING = Item(Items.BEACON_RING_11014)

        fun getStoreFile(): JSONObject {
            return ServerStore.getArchive("daily-zaff")
        }
    }
}
