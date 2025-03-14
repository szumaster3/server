package content.region.morytania.handlers.tarnslair

import core.api.*
import core.api.interaction.openBankAccount
import core.api.interaction.openBankPinSettings
import core.api.interaction.openGrandExchangeCollectionBox
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.ge.ExchangeHistory
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class OdovacarDialogue(
    player: Player? = null,
) : Dialogue(player),
    InteractionListener {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        val activeOffer = ExchangeHistory.getInstance(player)
        val hasOffer =
            activeOffer?.offerRecords?.any { record ->
                record != null &&
                    activeOffer.getOffer(record)?.let { offer ->
                        offer.withdraw[0] != null || offer.withdraw[1] != null
                    } == true
            } ?: false
        if (hasOffer) {
            npcl(
                FaceAnim.HALF_GUILTY,
                "Before we go any further, I should inform you that you have items ready for collection from the Grand Exchange.",
            )
        } else {
            npc("Well met, fellow adventurer! How can I help you?")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "Who are you?",
                    "I'd like to access my bank account, please.",
                    "I'd like to check my PIN settings.",
                    "I'd like to see my collection box.",
                ).also {
                    stage++
                }
            1 ->
                when (buttonId) {
                    1 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "How frightfully rude of me, my dear ${if (player.isMale) "chap" else "lady"}. My name is Odovacar and I work for that excellent enterprise, the Bank of Gielinor.",
                        ).also {
                            stage++
                        }
                    2 ->
                        if (!removeItem(player, Item(Items.COINS_995, 100))) {
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "I'm afraid you don't have the necessary funds with you at this time so I can't allow you to access your account.",
                            ).also {
                                stage =
                                    13
                            }
                        } else {
                            end()
                            sendMessage(player, "You pay 100 coins to use the travelling bank.")
                            openBankAccount(player)
                        }

                    3 -> {
                        end()
                        openBankPinSettings(player)
                    }

                    4 -> {
                        end()
                        openGrandExchangeCollectionBox(player)
                    }
                }
            2 ->
                options(
                    "If you work for the bank, what are you doing here?",
                    "I'd like to access my bank account, please.",
                    "I'd like to check my PIN settings.",
                    "I'd like to see my collection box.",
                ).also {
                    stage++
                }
            3 ->
                when (buttonId) {
                    1 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "As part of our ongoing service to provide you, the customer, with an absolutely world-class banking experience, we are investigating the option of opening a chain of travelling banks.",
                        ).also {
                            stage++
                        }
                    2 ->
                        if (!removeItem(player, Item(Items.COINS_995, 100))) {
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "I'm afraid you don't have the necessary funds with you at this time so I can't allow you to access your account.",
                            ).also {
                                stage =
                                    13
                            }
                        } else {
                            end()
                            sendMessage(player, "You pay 100 coins to use the travelling bank.")
                            openBankAccount(player)
                        }

                    3 -> {
                        end()
                        openBankPinSettings(player)
                    }

                    4 -> {
                        end()
                        openGrandExchangeCollectionBox(player)
                    }
                }
            4 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "These will provide you, our esteemed customer, with the convenience of having banking facilities where they will be of optimum use to you.",
                ).also {
                    stage++
                }
            5 -> npc("Such as here!").also { stage++ }
            6 -> player(FaceAnim.ASKING, "Huh?").also { stage++ }
            7 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "I am the first of a new generation of travelling bankers that will wander the perilous areas of the world to provide you, the valued customer, with a bank when you need it most!",
                ).also {
                    stage++
                }
            8 -> player("So I can access my bank account simply by talking to", "you?").also { stage++ }
            9 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Absolutely correct, dear ${if (player.isMale) "sir" else "lady"}. I must warn you, however, that due to the significantly increased overheads of an enterprise such as this,",
                ).also {
                    stage++
                }
            10 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "there is a small bank charge of 100gp every time you want to access your account.",
                ).also {
                    stage++
                }
            11 ->
                options(
                    "Sounds fair; here's the money. Can I access my account now?",
                    "Let me open my account and then I'll pay you.",
                    "That's preposterous! I'm not paying to withdraw my own money!",
                ).also {
                    stage++
                }
            12 ->
                when (buttonId) {
                    1, 2 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "It's not that I don't trust you, old ${if (player.isMale) "chap" else "girl"}, but as the old adage goes, 'money comes before friends'.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }

                    3 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "I'm sorry to hear that, ${if (player.isMale) "sir" else "madam"}. If you should reconsider, because I believe this service offers excellent value for money, do not hesitate to contact me.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }
            13 ->
                npcl(FaceAnim.HALF_GUILTY, "Please come again, when you have 100 gold to cover the fee.").also {
                    stage =
                        END_DIALOGUE
                }
            14 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "This is a branch of the Bank of Gielinor. We have branches in many towns.",
                ).also {
                    stage++
                }
            15 -> options("And what do you do?", "Didn't you used to be called the Bank of Varrock?")
            16 ->
                when (buttonId) {
                    1 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "We will look after your items and money for you. Leave your valuables with us if you want to keep them safe.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }

                    2 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Yes we did, but people kept on coming into our branches outside of Varrock and telling us that our signs were wrong.",
                        ).also {
                            stage++
                        }
                }
            17 ->
                npcl(FaceAnim.HALF_GUILTY, "They acted as if we didn't know what town we were in or something.").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return OdovacarDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ODOVACAR_5383)
    }

    override fun defineListeners() {
        on(NPCs.ODOVACAR_5383, IntType.NPC, "bank", "collect") { player, node ->
            val option = getUsedOption(player)
            when (option) {
                "bank" -> {
                    if (!removeItem(player, Item(Items.COINS_995, 100))) {
                        sendNPCDialogue(
                            player,
                            node.id,
                            "I'm afraid you don't have the necessary funds with you at this time so I can't allow you to access your account.",
                            FaceAnim.HALF_GUILTY,
                        )
                    } else {
                        sendMessage(player, "You pay 100 coins to use the travelling bank.")
                        openBankAccount(player)
                    }
                }

                else -> openGrandExchangeCollectionBox(player)
            }
            return@on true
        }
    }
}
