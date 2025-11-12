package content.global.activity.wom_tasks

import content.global.plugins.iface.ScrollInterface
import content.global.plugins.iface.ScrollLine
import core.api.*
import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs

// 13 November 2009

enum class WomDeliveryTasks(val npcId: Int, val itemId: Int?, val minAmount: Int = 1, val maxAmount: Int = minAmount, val description: String) {
    HIGH_PRIEST_MESSAGE(npcId = NPCs.HIGH_PRIEST_216, itemId = Items.OLD_MANS_MESSAGE_5506, description = "Deliver Old man's message to the High Priest of Entrana."),
    HIGH_PRIEST_LOGS(npcId = NPCs.HIGH_PRIEST_216, itemId = Items.LOGS_1511, minAmount = 5, maxAmount = 5, description = "Bring him 5 normal logs."),
    HIGH_PRIEST_BONES(npcId = NPCs.HIGH_PRIEST_216, itemId = Items.BONES_526, minAmount = 3, maxAmount = 7, description = "Bring him 3 to 7 normal bones."),
    HIGH_PRIEST_MEAT(npcId = NPCs.HIGH_PRIEST_216, itemId = Items.COOKED_MEAT_2142, minAmount = 13, description = "Bring him 13 cooked meat."),
    HIGH_PRIEST_BRONZE_ARROWTIPS(npcId = NPCs.HIGH_PRIEST_216, itemId = Items.BRONZE_ARROWTIPS_39, minAmount = 15, description = "Bring him 15 bronze arrowtips."),
    HIGH_PRIEST_POTATO(npcId = NPCs.HIGH_PRIEST_216, itemId = Items.POTATO_1942, minAmount = 10, description = "Bring him 10 potatoes."),
    HIGH_PRIEST_BRONZE_WIRE(npcId = NPCs.HIGH_PRIEST_216, itemId = Items.BRONZE_WIRE_1794, minAmount = 13, description = "Bring him 13 bronze wire."),
    HIGH_PRIEST_BRONZE_ARROW(npcId = NPCs.HIGH_PRIEST_216, itemId = Items.BRONZE_ARROW_882, minAmount = 4, description = "Bring him 4 bronze arrows."),
    HIGH_PRIEST_BRONZE_SHORTSWORD(npcId = NPCs.HIGH_PRIEST_216, itemId = Items.BRONZE_SWORD_1277, minAmount = 15, description = "Bring him 15 bronze shortswords."),
    HIGH_PRIEST_BRONZE_WARHAMMER(npcId = NPCs.HIGH_PRIEST_216, itemId = Items.BRONZE_WARHAMMER_1337, minAmount = 6, description = "Bring him 6 bronze warhammers."),
    HIGH_PRIEST_GRAIN(npcId = NPCs.HIGH_PRIEST_216, itemId = Items.GRAIN_1947, minAmount = 3, description = "Bring him 3 grain."),
    HIGH_PRIEST_SOFT_LEATHER(npcId = NPCs.HIGH_PRIEST_216, itemId = Items.LEATHER_1741, minAmount = 6, description = "Bring him 6 soft leather."),
    HIGH_PRIEST_BOW_STRING(npcId = NPCs.HIGH_PRIEST_216, itemId = Items.BOW_STRING_1777, minAmount = 5, description = "Bring him 5 bow strings."),
    HIGH_PRIEST_IRON_ORE(npcId = NPCs.HIGH_PRIEST_216, itemId = Items.IRON_ORE_440, minAmount = 5, description = "Bring him 5 iron ores."),
    ORACLE_MESSAGE(npcId = NPCs.ORACLE_746, itemId = Items.OLD_MANS_MESSAGE_5506, description = "Deliver Old man's message to the Oracle on Ice Mountain."),
    ORACLE_BEER(npcId = NPCs.ORACLE_746, itemId = Items.BEER_1917, minAmount = 5, description = "Bring her 5 beers."),
    ORACLE_BRONZE_DAGGER(npcId = NPCs.ORACLE_746, itemId = Items.BRONZE_DAGGER_1205, minAmount = 11, description = "Bring her 11 bronze daggers."),
    ORACLE_UNFIRED_PIE_DISH(npcId = NPCs.ORACLE_746, itemId = Items.UNFIRED_PIE_DISH_1789, minAmount = 4, maxAmount = 6, description = "Bring her 4 to 6 unfired pie dishes."),
    FATHER_LAWRENCE_MESSAGE(npcId = NPCs.FATHER_LAWRENCE_640, itemId = Items.OLD_MANS_MESSAGE_5506, description = "Deliver Old man's message to Father Lawrence."),
    ABBOT_LANGLEY_MESSAGE(npcId = NPCs.ABBOT_LANGLEY_801, itemId = Items.OLD_MANS_MESSAGE_5506, description = "Deliver Old man's message to Abbot Langley."),
    THURGO_MESSAGE(npcId = NPCs.THURGO_604, itemId = Items.OLD_MANS_MESSAGE_5506, description = "Deliver Old man's message to Thurgo.");
    val amount: Int = RandomFunction.random(minAmount, maxAmount)

    companion object {
        fun forItem(itemId: Int): WomDeliveryTasks? = values().firstOrNull { it.itemId == itemId }
    }
}

object WomDeliveryLetters {
    val THE_ORACLE_LETTER_CONTENT =
        arrayOf(
            ScrollLine("To the Oracle of Ice Mountain, greetings:", 2),
            ScrollLine("My sources tell me that many adventurers are becoming", 4),
            ScrollLine("confused about one of your riddles.", 5),
            ScrollLine("When asked to find a BOWL that has not seen heat, a large", 7),
            ScrollLine("number of adventurers assume that a POT will suffice. Of", 8),
            ScrollLine("course, this has no effect, so they send complaints about you to", 9),
            ScrollLine("the Council.", 10),
            ScrollLine("Strength through Wisdom!", 12),
            ScrollLine("*D", 14),
        )

    private val RELDO_LETTER_CONTENT =
        arrayOf(
            ScrollLine("To Reldo, librarian to the King of Varrock, greetings:", 2),
            ScrollLine("I have spent many hours pondering the question you asked me", 4),
            ScrollLine("in your recent letter. From the results seen so far, this new", 5),
            ScrollLine("method of refining metals from their ores possesses the", 6),
            ScrollLine("potential to revolutionise smithing, although I fear that the vast", 7),
            ScrollLine("majority of smiths will never fully adapt to the new technique.", 8),
            ScrollLine("Please keep an eye out for any documents that might be of", 10),
            ScrollLine("interest to me, as always.", 11),
            ScrollLine("*D", 13),
        )

    private val ABBOT_LANGLEY_LETTER_CONTENT =
        arrayOf(
            ScrollLine("To Langley, Abbot of the Monastery of Saradomin, greetings:", 2),
            ScrollLine("Long has it been since out last meeting, my friend, too long.", 4),
            ScrollLine("Truly we are living in tumultuous times, and the foul works of", 5),
            ScrollLine("Zamorak can be seen across the lands. Indeed, I hear a whisper", 6),
            ScrollLine("from the south that the power of the Terrible One has been", 7),
            ScrollLine("rediscovered! But be of good cheer, my friend, for we are all in", 8),
            ScrollLine("the hands of the Lord Saradomin.", 9),
            ScrollLine("Until our next meeting, then,", 11),
            ScrollLine("*D", 13),
        )

    private val THURGO_LETTER_CONTENT =
        arrayOf(
            ScrollLine("To Thurgo, master blacksmith, greetings:", 2),
            ScrollLine("Following your request, I have spend some time re-reading the", 4),
            ScrollLine("relevant scrolls in the Library of Varrock. It appears that when", 5),
            ScrollLine("your forefathers encountered that adventurer, he was on", 6),
            ScrollLine("a quest to find a mysterious shield.", 7),
            ScrollLine("Many thanks for the recipe you sent me; I shall certainly try this", 9),
            ScrollLine("'redberry pie' of which you speak so highly.", 10),
            ScrollLine("Regards,", 12),
            ScrollLine("*D", 14),
        )

    private val HIGH_PRIEST_LETTER_CONTENT =
        arrayOf(
            ScrollLine("To the High Priest of Entrana: greetings.", 2),
            ScrollLine("To respond to your recent questions about the effects of", 4),
            ScrollLine("summoning the power of Saradomin, I have spent some time", 5),
            ScrollLine("searching through the scrolls of Kolodion the battle mage. He", 6),
            ScrollLine("records that a bolt of lightning falls from above, accompanied", 7),
            ScrollLine("by a resounding crash, and the victim loses up to 20 life", 8),
            ScrollLine("points; however, he believed that this could be increased by", 9),
            ScrollLine("50% should one be wearing the Cape of Saradomin and be", 10),
            ScrollLine("charged when casting the spell.", 11),
            ScrollLine("Fare thee well, my young friend,", 13),
            ScrollLine("*D", 15),
        )

    private val FR_LAWRENCE_LETTER_CONTENT =
        arrayOf(
            ScrollLine("To Lawrence, resident priest of Varrock, greetings:", 2),
            ScrollLine("Despite our recent conversation on this matter, I hear that you", 4),
            ScrollLine("are still often found in a less than sober condition. I am forced", 5),
            ScrollLine("to repeat the warning I gave you at the time: if you continue to", 6),
            ScrollLine("indulge yourself in this manner, the Council will have no choice", 7),
            ScrollLine("but to transfer you to Entrana where you can be supervised", 8),
            ScrollLine("more carefully.", 9),
            ScrollLine("I trust you will heed this message.", 11),
            ScrollLine("*D", 13),
        )

    private val FR_AERECK_LETTER_CONTENT =
        arrayOf(
            ScrollLine("To Aereck, resident priest of Lumbridge, greetings:", 2),
            ScrollLine("I am pleased to inform you that, following a careful search, the", 4),
            ScrollLine("staff of the seminary have located your pyjamas.", 5),
            ScrollLine("Before returning them to you, however, they would be very", 7),
            ScrollLine("interested to know exactly how these garments came to be in", 8),
            ScrollLine("the graveyard.", 9),
            ScrollLine("Please pass on my regards to Urhney.", 11),
            ScrollLine("*D", 13),
        )

    const val LETTER_DELIVERY_ATTRIBUTE = "/save:${WomDeliveryListener.LETTER_DELIVERY}"

    private val letterMap =
        mapOf(
            NPCs.ORACLE_746 to THE_ORACLE_LETTER_CONTENT,
            NPCs.RELDO_2660 to RELDO_LETTER_CONTENT,
            NPCs.ABBOT_LANGLEY_801 to ABBOT_LANGLEY_LETTER_CONTENT,
            NPCs.THURGO_604 to THURGO_LETTER_CONTENT,
            NPCs.HIGH_PRIEST_216 to HIGH_PRIEST_LETTER_CONTENT,
            NPCs.FATHER_LAWRENCE_640 to FR_LAWRENCE_LETTER_CONTENT,
            NPCs.FATHER_AERECK_456 to FR_AERECK_LETTER_CONTENT,
        )

    fun openLetter(player: Player, npcId: Int?) {
        val content = letterMap[npcId]
        if (content != null) {
            ScrollInterface.scrollSetup(player, Components.MESSAGESCROLL_220, content)
            setAttribute(player, LETTER_DELIVERY_ATTRIBUTE, npcId ?: -1)
        } else {
            ScrollInterface.scrollSetup(
                player,
                Components.MESSAGESCROLL_220,
                arrayOf(ScrollLine("You unfold the letter, but it's blank...", 5))
            )
        }
    }

    fun getLetterNpc(player: Player): Int? = getAttribute(player, LETTER_DELIVERY_ATTRIBUTE, -1).takeIf { it != -1 }
}

enum class WomTaskReward(val npc: IntArray, val table: WeightBasedTable) {
    WISE_OLD_MAN_REWARD(
        intArrayOf(NPCs.WISE_OLD_MAN_2253),
        WeightBasedTable.create(
            WeightedItem(Items.UNCUT_DIAMOND_1617, 1, 1, 1.0),
            WeightedItem(Items.UNCUT_RUBY_1619, 1, 1, 5.0),
            WeightedItem(Items.LAW_RUNE_563, 5, 20, 10.0),
            WeightedItem(Items.COINS_995, 100, 500, 25.0),
        )
    ),
    HIGH_PRIEST_REWARD(
        intArrayOf(NPCs.HIGH_PRIEST_216),
        WeightBasedTable.create(
            WeightedItem(Items.BONES_526, 1, 3, 15.0),
            WeightedItem(Items.COINS_995, 50, 250, 30.0),
            WeightedItem(Items.RUNE_ESSENCE_1436, 10, 50, 15.0),
            WeightedItem(Items.LAW_RUNE_563, 2, 10, 10.0)
        )
    ),
    ORACLE_REWARD(
        intArrayOf(NPCs.ORACLE_746),
        WeightBasedTable.create(
            WeightedItem(Items.MIND_RUNE_558, 10, 50, 20.0),
            WeightedItem(Items.AIR_RUNE_556, 10, 75, 20.0),
            WeightedItem(Items.UNCUT_SAPPHIRE_1623, 1, 1, 10.0),
            WeightedItem(Items.COINS_995, 50, 150, 20.0)
        )
    ),
    THURGO_REWARD(
        intArrayOf(NPCs.THURGO_604),
        WeightBasedTable.create(
            WeightedItem(Items.IRON_BAR_2351, 3, 8, 20.0),
            WeightedItem(Items.COAL_453, 5, 15, 20.0),
            WeightedItem(Items.STEEL_BAR_2353, 1, 4, 15.0),
            WeightedItem(Items.COINS_995, 100, 300, 30.0)
        )
    ),
    ABBOT_LANGLEY_REWARD(
        intArrayOf(NPCs.ABBOT_LANGLEY_801),
        WeightBasedTable.create(
            WeightedItem(Items.GRIMY_RANARR_208, 1, 3, 10.0),
            WeightedItem(Items.PRAYER_POTION3_139, 1, 2, 10.0),
            WeightedItem(Items.COINS_995, 100, 300, 25.0)
        )
    );

    companion object {
        private val rewardMap = HashMap<Int, WeightBasedTable>()

        init {
            WomTaskReward.values().forEach { it.npc.forEach { id -> rewardMap[id] = it.table } }
        }

        fun forId(id: Int): WeightBasedTable? = rewardMap[id]
    }
}

class WomDeliveryDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(WISE_OLD_MAN)
        val currentTask = getAttribute(player!!, WomDeliveryListener.CURRENT_TASK, "")
        val hasTask = getAttribute(player!!, WomDeliveryListener.TASK_START, false)

        val task =
            if (hasTask && currentTask.isNotEmpty()) {
                runCatching { WomDeliveryTasks.valueOf(currentTask) }.getOrNull() ?: WomDeliveryTasks.values().random()
            } else {
                WomDeliveryTasks.values().random()
            }

        val amount = RandomFunction.random(task.minAmount, task.maxAmount)
        setAttribute(player!!, "/save:${WomDeliveryListener.CURRENT_AMOUNT}", amount)

        val itemName = if (task.itemId != null) getItemName(task.itemId) else "Old Man's Message"

        when (stage) {
            0 ->
                if (hasTask) {
                    player("What did you ask me to do?").also { stage++ }
                } else {
                    player("Is there anything I can do for you?").also { stage++ }
                }
            1 -> {
                npcl(
                    FaceAnim.HALF_GUILTY,
                    if (task.itemId != null) "I need you to bring me $amount × $itemName."
                    else "Please deliver the Old Man's message for me."
                )
                setAttribute(player!!, "/save:${WomDeliveryListener.TASK_START}", true)
                setAttribute(player!!, "/save:${WomDeliveryListener.CURRENT_TASK}", task.name)
                stage++
            }
            2 -> options("Where can I get that?", "Right, I'll see you later.").also { stage++ }
            3 ->
                when (buttonID) {
                    1 -> playerl(FaceAnim.HALF_GUILTY, "Where can I get that?").also { stage++ }
                    2 -> playerl(FaceAnim.HALF_GUILTY, "Right, I'll see you later.").also { stage = END_DIALOGUE }
                }
            4 -> npc(FaceAnim.HALF_GUILTY, task.description).also { stage++ }
            5 -> playerl(FaceAnim.HALF_GUILTY, "Right, I'll see you later.").also { stage = END_DIALOGUE }
        }
    }

    companion object {
        const val WISE_OLD_MAN = NPCs.WISE_OLD_MAN_2253
    }
}

class WomDeliveryListener : InteractionListener {

    override fun defineListeners() {

        onUseAnyWith(IntType.NPC, WISE_OLD_MAN) { player, _, _ ->
            sendNPCDialogue(player, WISE_OLD_MAN, "Humph! You could at least say hello before waving your items in my face.", FaceAnim.HALF_GUILTY)
            return@onUseAnyWith true
        }

        on(OLD_MAN_MESSAGE, IntType.ITEM, "read") { player, _ ->
            openInterface(player, Components.MESSAGESCROLL_220)
            val npcId = WomDeliveryLetters.getLetterNpc(player)
            WomDeliveryLetters.openLetter(player, npcId)
            return@on true
        }

        WomDeliveryTasks.values()
            .distinctBy { it.npcId }
            .forEach { task ->
                on(task.npcId, IntType.NPC, "talk-to") { player, npc ->
                    handleNpcDelivery(player, npc.asNpc())
                    return@on true
                }
            }
    }

    private fun handleNpcDelivery(player: Player, npc: NPC) {
        val taskName = getAttribute(player, CURRENT_TASK, "")
        val amount = getAttribute(player, CURRENT_AMOUNT, 0)
        if (taskName.isEmpty()) {
            sendNPCDialogue(player, npc.id, "Hello traveller, I don't believe the Wise Old Man sent you to me.", FaceAnim.FRIENDLY)
            return
        }

        val task = runCatching { WomDeliveryTasks.valueOf(taskName) }.getOrNull()
        if (task == null || task.npcId != npc.id) {
            sendNPCDialogue(player, npc.id, "I'm not expecting anything from you.", FaceAnim.HALF_GUILTY)
            return
        }

        if (task.itemId != null && task.itemId != OLD_MAN_MESSAGE) {
            if (inInventory(player, task.itemId, amount)) {
                removeItem(player, Item(task.itemId, amount))
                sendNPCDialogue(player, npc.id, "Ah, thank you for bringing me $amount ${getItemName(task.itemId)}!", FaceAnim.HAPPY)
                rollNpcReward(player, npc)
                finishTask(player)
            } else {
                sendNPCDialogue(player, npc.id, "You haven’t brought everything I asked for yet.", FaceAnim.HALF_GUILTY)
            }
        } else {
            if (!removeItem(player, OLD_MAN_MESSAGE)) {
                sendNPCDialogue(player, npc.id, "You don't have the Old Man's message with you.", FaceAnim.HALF_GUILTY)
                return
            }
            sendNPCDialogue(player, npc.id, "A message from the Wise Old Man? Thank you kindly!", FaceAnim.HAPPY)
            rollNpcReward(player, npc)
            finishTask(player)
        }
    }

    private fun finishTask(player: Player) {
        setAttribute(player, "/save:${TASK_START}", false)
        setAttribute(player, "/save:${CURRENT_TASK}", "")
        setAttribute(player, "/save:${CURRENT_AMOUNT}", 0)
    }

    private fun rollNpcReward(player: Player, npc: NPC) {
        val rewardTable = WomTaskReward.forId(npc.id)

        if (rewardTable == null) {
            sendNPCDialogue(player, npc.id, "I'm afraid I don't have anything to give you for your trouble, but thank you nonetheless.", FaceAnim.FRIENDLY)
            return
        }

        val rewards = rewardTable.roll(player, RandomFunction.random(1, 3))
        if (rewards.isEmpty()) {
            sendNPCDialogue(player, npc.id, "Hmm, it seems luck wasn’t on your side this time. Maybe next time!", FaceAnim.HALF_GUILTY)
            return
        }

        for (reward in rewards) {
            if (addItem(player, reward.id, reward.amount)) {
                sendItemDialogue(player, reward.id, "You received: ${reward.amount} × ${getItemName(reward.id)}!")
            } else {
                GroundItemManager.create(GroundItem(reward, player.location, player))
                sendItemDialogue(player, reward.id, "You couldn’t carry your reward, so it has been dropped on the ground.")
            }
        }
        sendMessage(player, "You have been rewarded for helping ${getNPCName(npc.id)}!")
    }

    companion object {
        const val WISE_OLD_MAN = NPCs.WISE_OLD_MAN_2253
        const val OLD_MAN_MESSAGE = Items.OLD_MANS_MESSAGE_5506

        const val CURRENT_TASK = "womt-task"
        const val CURRENT_AMOUNT = "womt-amount"
        const val TASK_START = "womt-start"
        const val LETTER_DELIVERY = "letter-delivery"
    }
}
