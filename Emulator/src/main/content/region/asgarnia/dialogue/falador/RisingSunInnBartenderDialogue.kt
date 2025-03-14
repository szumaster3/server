package content.region.asgarnia.dialogue.falador

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class RisingSunInnBartenderDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.HAPPY, "Hi! What can I get you?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE -> {
                if (hasAnyBeerGlasses()) {
                    showTopics(
                        Topic(FaceAnim.ASKING, "What ales are you serving?", 10),
                        Topic(FaceAnim.HAPPY, "I've got some beer glasses...", 20),
                    )
                } else {
                    playerl(FaceAnim.ASKING, "What ales are you serving?").also { stage = 10 }
                }
            }
            10 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Well, we've got Asgarnian Ale, Wizard's Mind Bomb and Dwarven Stout. " + "Each for only 3 coins.",
                ).also { stage++ }
            11 ->
                showTopics(
                    Topic(FaceAnim.HAPPY, "One Asgarnian Ale, please.", 12),
                    Topic(FaceAnim.HAPPY, "I'll try the Mind Bomb.", 13),
                    Topic(FaceAnim.ASKING, "Can I have a Dwarven Stout?", 14),
                    Topic(FaceAnim.NEUTRAL, "I don't feel like any of those.", END_DIALOGUE),
                )
            12 ->
                if (ensureHasMoney()) {
                    purchaseBrew(Items.ASGARNIAN_ALE_1905)
                }
            13 ->
                if (ensureHasMoney()) {
                    purchaseBrew(Items.WIZARDS_MIND_BOMB_1907)
                }
            14 ->
                if (ensureHasMoney()) {
                    purchaseBrew(Items.DWARVEN_STOUT_1913)
                }
            20 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Oh, we will buy those from you if you're interested. We offer 2 coins for each glass.",
                ).also {
                    stage++
                }
            21 ->
                showTopics(
                    Topic(FaceAnim.HAPPY, "Yes, please!", 22),
                    Topic(FaceAnim.NEUTRAL, "No thanks, I like my empty beer glasses.", END_DIALOGUE),
                )
            22 -> {
                trySellAllBeerGlasses()
                npcl(FaceAnim.FRIENDLY, "There you go!").also { stage = END_DIALOGUE }
            }
            30 -> playerl(FaceAnim.FRIENDLY, "Thanks, ${npc.name}.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray =
        intArrayOf(
            NPCs.EMILY_736,
            NPCs.KAYLEE_3217,
        )

    private fun ensureHasMoney(): Boolean {
        if (!inInventory(player, Items.COINS_995, 3)) {
            npcl(FaceAnim.ANGRY, "No freeloaders!").also {
                sendMessage(player, "You don't have enough money to buy that.")
                stage = END_DIALOGUE
            }
            return false
        }

        return true
    }

    private fun purchaseBrew(brewId: Int) {
        if (removeItem(player, Item(Items.COINS_995, 3))) {
            addItemOrDrop(player, brewId)
            sendItemDialogue(
                player,
                Item(brewId),
                "You hand 3 coins over to ${npc.name}. She gives you ${
                    when (brewId) {
                        Items.WIZARDS_MIND_BOMB_1907 -> "a Wizard's Mind Bomb"
                        Items.DWARVEN_STOUT_1913 -> "a Dwarven Stout"
                        Items.ASGARNIAN_ALE_1905 -> "an Asgarnian Ale"
                        else -> "Nothing interesting happens."
                    }
                }.",
            ).also { stage = 30 }
        }
    }

    private fun hasAnyBeerGlasses() =
        inInventory(player, Items.BEER_GLASS_1919) || inInventory(player, Items.BEER_GLASS_1920)

    private fun trySellAllBeerGlasses() {
        val regularGlassAmount = amountInInventory(player, Items.BEER_GLASS_1919)
        val notedGlassAmount = amountInInventory(player, Items.BEER_GLASS_1920)

        if (removeItem(player, Item(Items.BEER_GLASS_1919, regularGlassAmount))) {
            addItem(player, Items.COINS_995, regularGlassAmount * 2)
        }

        if (removeItem(player, Item(Items.BEER_GLASS_1920, notedGlassAmount))) {
            addItem(player, Items.COINS_995, notedGlassAmount * 2)
        }
    }
}
