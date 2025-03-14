package content.region.misthalin.quest.dragon.dialogue

import content.region.misthalin.quest.dragon.DragonSlayer
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import org.rs.consts.NPCs
import org.rs.consts.Quests

class WormbrainDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.DRAGON_SLAYER)
        stage =
            when (quest!!.getStage(player)) {
                else -> {
                    npc(FaceAnim.OLD_DEFAULT, "Whut you want?")
                    -1
                }
            }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (quest!!.getStage(player)) {
            20 -> {
                when (stage) {
                    -1 ->
                        if (!player.inventory.containsItem(DragonSlayer.WORMBRAIN_PIECE) &&
                            !player.bank.containsItem(DragonSlayer.WORMBRAIN_PIECE)
                        ) {
                            player("I believe you've got a piece of a map that I need.")
                            stage = 500
                        } else {
                            defaultDialogue(buttonId)
                        }

                    500 -> {
                        npc(FaceAnim.OLD_DEFAULT, "So? Why should I be giving it to you? What you do", "for Wormbrain?")
                        stage = 501
                    }

                    501 -> {
                        player("I suppose I could pay you for the map piece. Say, 500", "coins?")
                        stage = 502
                    }

                    502 -> {
                        npc(FaceAnim.OLD_DEFAULT, "Me not stoopid, it worth at least 10,000 coins!")
                        stage = 503
                    }

                    503 -> {
                        options("You must be joking! Forget it.", "Alright then, 10,000 it is.")
                        stage = 504
                    }

                    504 ->
                        when (buttonId) {
                            1 -> {
                                player("You must be joking! Forget it.")
                                stage = 505
                            }

                            2 -> {
                                player("Alright, then, 10,000 it is.")
                                stage = 506
                            }
                        }

                    505 -> end()
                    506 ->
                        if (player.inventory.containsItem(COINS) && player.inventory.remove(COINS)) {
                            if (!player.inventory.add(DragonSlayer.WORMBRAIN_PIECE)) {
                                GroundItemManager.create(DragonSlayer.WORMBRAIN_PIECE, player)
                            }
                            interpreter.sendItemMessage(
                                DragonSlayer.WORMBRAIN_PIECE.id,
                                "You buy the map piece from Wormbrain.",
                            )
                            stage = 507
                        } else {
                            end()
                            player.packetDispatch.sendMessage("You don't have enough coins in order to do that.")
                        }

                    507 -> {
                        npc(FaceAnim.OLD_DEFAULT, "Fank you very much! Now me can bribe da guards,", "hehehe.")
                        stage = 508
                    }

                    508 -> end()
                }
                defaultDialogue(buttonId)
            }

            else -> defaultDialogue(buttonId)
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.WORMBRAIN_745)
    }

    fun defaultDialogue(buttonId: Int) {
        when (stage) {
            -1 -> {
                options("What are you in for?", "Sorry, thought this was a zoo.")
                stage = 0
            }

            0 ->
                when (buttonId) {
                    1 -> {
                        player("What are you in for?")
                        stage = 10
                    }

                    2 -> {
                        player("Sorry, thought this was a zoo.")
                        stage = 15
                    }
                }

            10 -> {
                npc(FaceAnim.OLD_DEFAULT, "Me not sure. Me pick some stuff up and take it away.")
                stage = 11
            }

            11 -> {
                player("Well, did the stuff belong to you?")
                stage = 12
            }

            12 -> {
                npc(FaceAnim.OLD_DEFAULT, "Umm...no.")
                stage = 13
            }

            13 -> {
                player("Well, that would be why then.")
                stage = 14
            }

            14 -> {
                npc(FaceAnim.OLD_DEFAULT, "Oh, right.")
                stage = 15
            }

            15 -> end()
        }
    }

    companion object {
        private val COINS = Item(995, 10000)
    }
}
