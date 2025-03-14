package content.region.kandarin.dialogue.catherby

import core.ServerStore
import core.api.*
import core.api.interaction.openNpcShop
import core.api.quest.getQuestStage
import core.game.dialogue.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.StringUtils
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class ArheinDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private val limits = mapOf(Items.PINEAPPLE_2114 to 40, Items.SEAWEED_401 to 80)
    val period = "daily"
    private var goods = Items.PINEAPPLE_2114
    private var goodsMessage = ""
    private var goodsName = ""
    private var stock = 0

    private fun getGoods(
        requestedItem: Int,
        requestedAmount: Int,
    ): Int {
        val price = 2
        val afford = player.inventory.getAmount(Items.COINS_995) / price
        var realamount = minOf(requestedAmount, afford)
        val exactafford = (realamount == afford) && (afford == freeSlots(player) + 1)
        realamount = minOf(realamount, if (exactafford) realamount else freeSlots(player))
        realamount =
            ServerStore.getNPCItemAmount(
                NPCs.ARHEIN_563,
                requestedItem,
                limits.getOrDefault(requestedItem, 0),
                player,
                realamount,
                period,
            )
        if (removeItem(player, Item(Items.COINS_995, realamount * price), Container.INVENTORY)) {
            if (addItem(player, requestedItem, realamount, Container.INVENTORY)) {
                ServerStore.addNPCItemAmount(
                    NPCs.ARHEIN_563,
                    requestedItem,
                    limits.getOrDefault(requestedItem, 0),
                    player,
                    realamount,
                    period,
                )
                return realamount
            }
        }
        return 0
    }

    private fun selectGoods(requestedItem: Int) {
        this.goods = requestedItem
        this.stock =
            ServerStore.getNPCItemStock(NPCs.ARHEIN_563, this.goods, limits.getOrDefault(this.goods, 0), player, period)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (args.size > 1) {
            npcl(FaceAnim.ANGRY, "Hey buddy! Get away from my ship alright?")
            stage = 701
        } else {
            npc(
                FaceAnim.HAPPY,
                "Hello! Would you like to trade? I've a variety of wares!",
                "for sale, as well as a special supply of pineapples and",
                "seaweed!",
            )
            stage = 1
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 ->
                showTopics(
                    Topic("Yes.", 7),
                    Topic("I hear you sell seaweed...", 900),
                    Topic("Someone told me you sell pineapples...", 800),
                    Topic("No thank you.", END_DIALOGUE),
                    Topic("Is that your ship?", 100),
                )
            7 -> {
                end()
                openNpcShop(player, NPCs.ARHEIN_563)
            }
            100 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Yes, I use it to make deliveries to my customers up and down the coast. These crates here are all ready for my next trip.",
                ).also {
                    stage++
                }
            101 ->
                showTopics(
                    Topic("Where do you deliver to?", 120),
                    Topic("Are you rich then?", 110),
                    IfTopic(
                        "Do you deliver to the fort just down the coast?",
                        500,
                        getQuestStage(player!!, Quests.MERLINS_CRYSTAL) == 30,
                    ),
                )
            110 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Business is going reasonably well... I wouldn't say I was the richest of merchants ever, but I'm doing fairly well all things considered.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            120 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Various places up and down the coast. Mostly Karamja and Port Sarim.",
                ).also { stage++ }
            121 ->
                showTopics(
                    Topic("I don't suppose I could get a lift anywhere?", 140),
                    Topic("Well, good luck with your business.", 130),
                )

            130 -> npcl(FaceAnim.HAPPY, "Thanks buddy!").also { stage = END_DIALOGUE }
            140 -> npcl(FaceAnim.GUILTY, "Sorry pal, but I'm afraid I'm not quite ready to sail yet.").also { stage++ }
            141 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "I'm waiting on a big delivery of candles which I need to deliver further along the coast.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            500 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Yes, I do have orders to deliver there from time to time. I think I may have some bits and pieces for them when I leave here next actually.",
                ).also {
                    stage++
                }
            501 -> {
                showTopics(
                    Topic(FaceAnim.NEUTRAL, "Can you drop me off on the way down please?", 502),
                    Topic(FaceAnim.NEUTRAL, "Aren't you worried about supplying evil knights?", 505),
                )
            }

            502 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I don't think Sir Mordred would like that. He wants as few outsiders visiting as possible. I wouldn't want to lose his business.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            505 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Hey, you gotta take business where you can find it these days! Besides, if I didn't supply them, someone else would.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            701 -> playerl(FaceAnim.GUILTY, "Yeah... uh... sorry...").also { stage = END_DIALOGUE }
            800 -> {
                selectGoods(Items.PINEAPPLE_2114)
                if (stock == 0) {
                    npcl(FaceAnim.SAD, "Actually, I've run out. Come back tomorrow and I should have some more.").also {
                        stage =
                            END_DIALOGUE
                    }
                } else {
                    this.goodsMessage =
                        "I certainly do! I've got $stock in stock, going for 2 coins each. How many would you like?"
                    npcl(FaceAnim.HAPPY, this.goodsMessage)
                    this.goodsName =
                        if (stock == 1) getItemName(this.goods) else StringUtils.plusS(getItemName(this.goods))
                    stage = 1200
                }
            }

            900 -> {
                selectGoods(Items.SEAWEED_401)
                if (stock == 0) {
                    npcl(FaceAnim.SAD, "Actually, I've run out. Come back tomorrow and I should have some more.").also {
                        stage =
                            END_DIALOGUE
                    }
                } else {
                    this.goodsMessage =
                        "I certainly do! I've $stock at the moment and they cost 2 coins each. How many would you like?"
                    npcl(FaceAnim.HAPPY, this.goodsMessage)
                    this.goodsName = getItemName(this.goods)
                    stage = 1200
                }
            }

            1200 -> {
                val goodsPrompt = "Arhein has ${this.stock} ${this.goodsName}. How many would you like to buy?"
                sendInputDialogue(player, InputType.AMOUNT, goodsPrompt) { value ->
                    val amountReceived = getGoods(this.goods, Integer.parseInt(value.toString()))
                    var exitMsg = ""
                    exitMsg =
                        if (amountReceived == this.stock) {
                            "Here you go! I've run out for now. Come again tomorrow and I should have more."
                        } else if (amountReceived > 0) {
                            "Here you go, buddy!"
                        } else {
                            "Take care, buddy!"
                        }
                    npcl(FaceAnim.HAPPY, exitMsg)
                }.also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return ArheinDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ARHEIN_563)
    }
}
