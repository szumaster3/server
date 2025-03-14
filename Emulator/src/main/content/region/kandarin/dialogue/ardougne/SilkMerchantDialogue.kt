package content.region.kandarin.dialogue.ardougne

import core.api.sendChat
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.RegionManager.getLocalNpcs
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class SilkMerchantDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private val silk = Item(950)
    private val notedSilk = Item(951)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (player.getSavedData().globalData.getSilkSteal() > System.currentTimeMillis()) {
            end()
            for (npc in getLocalNpcs(player.location, 8)) {
                if (!npc.properties.combatPulse.isAttacking && npc.id == 32) {
                    sendChat(npc, "Hey! Get your hands off there!")
                    npc.attack(player)
                    break
                }
            }
            Pulser.submit(
                object : Pulse(1) {
                    var count: Int = 0

                    override fun pulse(): Boolean {
                        if (count == 0) sendChat(npc, "You're the one who stole something from me!")
                        if (count == 2) {
                            sendChat(npc, "Thief! Thief! Thief!")
                            return true
                        }
                        count++
                        return false
                    }
                },
            )
            return false
        }
        npc(FaceAnim.HAPPY, "I buy silk. If you ever want to", "sell any silk, bring it here.").also { stage = 0 }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                if (player.inventory.containsItem(silk)) {
                    player(
                        FaceAnim.FRIENDLY,
                        "Hello. I have some fine silk from Al-Kharid to sell to",
                        "you.",
                    ).also { stage++ }
                } else if (player.inventory.containsItem(notedSilk)) {
                    player(FaceAnim.ASKING, "I've got some silk here. Will you buy it?")
                    stage = 100
                } else {
                    player(FaceAnim.NEUTRAL, "Sorry, I don't have any silk.").also { stage = END_DIALOGUE }
                }
            1 ->
                npc(
                    FaceAnim.HAPPY,
                    "Ah, I may be interested in that. What sort of price were",
                    "you looking at per piece of silk?",
                ).also {
                    stage++
                }
            2 -> options("20 coins.", "30 coins.", "120 coins.", "200 coins.").also { stage++ }
            3 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.FRIENDLY, "20 coins.")
                        stage = 1000
                    }
                    2 -> {
                        player(FaceAnim.FRIENDLY, "30 coins.")
                        stage = 2000
                    }
                    3 -> {
                        player(FaceAnim.FRIENDLY, "120 coins.")
                        stage = 300
                    }
                    4 -> {
                        player(FaceAnim.FRIENDLY, "200 coins.")
                        stage = 400
                    }
                }
            1000, 2000 -> {
                npc(FaceAnim.HAPPY, "That price is fine by me! Hand over your silk.")
                stage = if (stage == 1000) 1010 else 1011
            }
            1010 -> buy(20)
            1011 -> buy(30)
            300, 400 -> {
                npc(FaceAnim.FRIENDLY, "You'll never get that much for it. I'll be generous and", "give you 50 for it.")
                stage = 500
            }
            500 -> {
                options("Ok, I guess 50 will do.", "I'll give it to you for 60.", "No, that is not enough.")
                stage = 501
            }
            501 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.FRIENDLY, "Ok, I guess 50 will do.")
                        stage = 510
                    }

                    2 -> {
                        player(FaceAnim.HAPPY, "I'll give it to you for 60.")
                        stage = 520
                    }

                    3 -> {
                        player(FaceAnim.HALF_GUILTY, "No, that is not enough.")
                        stage = END_DIALOGUE
                    }
                }
            510 -> buy(50)
            520 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "You drive a hard bargain, but",
                    "I guess that will have to do.",
                ).also { stage++ }
            521 -> buy(60)
            100 -> npc("Silk? You're not carrying any silk.").also { stage++ }
            101 -> player("Yes I am.").also { stage++ }
            102 ->
                npc(
                    "No, you're carrying " + player.inventory.getAmount(notedSilk) + " bits of paper that say they",
                    "can be exchanged for silk at a bank or general store.",
                    "I'm not buying those. Fetch me some real silk, then",
                    "we'll trade.",
                ).also {
                    stage++
                }
            103 -> player("Oh, alright.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return SilkMerchantDialogue(player)
    }

    fun buy(price: Int) {
        end()
        val amt = player.inventory.getAmount(silk)
        val remove = Item(silk.id, amt)
        if (!player.inventory.containsItem(remove)) {
            return
        }
        if (player.inventory.remove(remove)) {
            player.inventory.add(Item(995, price * amt))
        }
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SILK_MERCHANT_574)
    }
}
