package content.region.kandarin.gnome.quest.makinghistory.plugin

import content.region.kandarin.gnome.quest.makinghistory.MHUtils
import content.region.kandarin.gnome.quest.makinghistory.book.OutpostHistory
import content.region.kandarin.gnome.quest.makinghistory.book.TheMysteriousAdventurer
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import shared.consts.*
import kotlin.random.Random

class MakingHistoryPlugin : InteractionListener, LogoutListener {

    override fun defineListeners() {
        on(Items.SCROLL_6758, IntType.ITEM, "read") { player, _ ->
            val outpostScroll =
                arrayOf(
                    "",
                    "<col=8A0808>Timeline of the Ardougne Outpost</col>",
                    "",
                    "",
                    "Start of Fifth age: Outpost built",
                    "",
                    "+ 65 Years: The dreaded Years of Tragedy'",
                    "",
                    "+ 68 Years: The Great Battle'",
                    "",
                    "+ 71 Years: Survivors of battle start a new line of",
                    "",
                    "kings of Ardougne and the Equal Trade Market.",
                    "",
                )
            openInterface(player, Components.BLANK_SCROLL_222)
            sendString(player, outpostScroll.joinToString("<br>"), Components.BLANK_SCROLL_222, 2)
            return@on true
        }

        on(Scenery.SHIELD_DISPLAY_10267, IntType.SCENERY, "study") { player, _ ->
            sendDialogue(player, "Inside the case is a giant shield, the plate reads: 'This shield comes from the Fourth age when people were fighting to become more settled and less nomadic'.")
            return@on true
        }

        onDig(Location(2440, 3145, 0)) { player ->
            if (getVarbit(player, MHUtils.ERIN_PROGRESS) >= 1) {
                sendDialogue(player, "You use the spade and find a chest. Wonder what's inside?")
                setVarbit(player, MHUtils.ERIN_PROGRESS, 2, true)
                addItemOrDrop(player, Items.CHEST_6759)
            }
            return@onDig
        }

        onUseWith(IntType.ITEM, Items.ENCHANTED_KEY_6754, Items.CHEST_6759) { player, used, _ ->
            if (removeItem(player, used.asItem())) {
                sendDialogueLines(player, "You look in the chest and find a journal, and then you throw away", "the chest.")
                addItemOrDrop(player, Items.JOURNAL_6755)
                setVarbit(player, MHUtils.ERIN_PROGRESS, 4, true)
                setAttribute(player, MHUtils.ATTRIBUTE_ERIN_PROGRESS, true)
            }
            return@onUseWith true
        }

        on(Items.ENCHANTED_KEY_6754, IntType.ITEM, "feel") { player, _ ->
            if (inBorders(player, 2438, 3143, 2442, 3147)) {
                sendMessage(player, "The key is steaming. It must be right below your feet.")
                playAudio(player, Sounds.HISTORY_KEY_STEAMING_1201)
                return@on true
            }

            val keyHints = listOf(
                getRegionBorders(10574) to ("It's very cold" to Sounds.HISTORY_KEY_COLD_1198),
                getRegionBorders(10546) to ("It's cold" to Sounds.HISTORY_KEY_COLD_1198),
                getRegionBorders(10290) to ("It's warm" to Sounds.HISTORY_KEY_WARM_1202),
                getRegionBorders(10289) to ("It's hot" to Sounds.HISTORY_KEY_HOT_1200),
                getRegionBorders(9776) to ("It's very hot" to Sounds.HISTORY_KEY_HOT_1200),
                getRegionBorders(9777) to ("Ouch! It's burning hot and warmer than last time" to Sounds.HISTORY_KEY_BURNING_1197),
            )

            val match = keyHints.firstOrNull { (region, _) -> inBorders(player, region) }

            if (match != null) {
                val (message, sound) = match.second
                sendMessage(player, message)
                playAudio(player, sound)
            } else {
                sendMessage(player, "It's freezing")
                playAudio(player, Sounds.HISTORY_KEY_FREEZING_1199)
            }

            return@on true
        }

        on(Scenery.BOOKCASE_10273, IntType.SCENERY, "study") { player, _ ->
            sendOptions(player, "There's a great variety of books. Which shall you choose?", "The History of the Outpost.", "The Times of Lathas.", "The Mysterious Adventurer.")
            addDialogueAction(player) { p, button ->
                when (button) {
                    1 -> closeDialogue(player).also { OutpostHistory.openBook(p) }
                    2 -> closeDialogue(player).also { openDialogue(player, TimeOfLathasBookcaseDialogue()) }
                    3 -> closeDialogue(player).also { TheMysteriousAdventurer.openBook(p) }
                }
            }
            return@on true
        }
    }

    inner class TimeOfLathasBookcaseDialogue : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            when(stage) {
                0 -> sendDialogue(player!!, "You pick up a new looking book: 'The Times of Lathas'.").also { stage++ }
                1 -> sendDialogue(player!!, "You skim over the heavy book. It talks about the heritage of the line of kings who carry the name Ardignas.").also { stage++ }
                2 -> sendDialogue(player!!, "They only came into power 68 years ago, but in which time there have been five kings, the current being King Lathas.").also { stage++ }
                3 -> end()
            }
        }
    }

    /**
     * Called when a player logs out.
     */
    override fun logout(player: Player) {
        val session = player.getAttribute<MakingHistoryPlugin>("enchanted-key", null) ?: return
        removeAttribute(player, session.toString())
        clearLogoutListener(player, "enchanted-key")
    }

    /**
     * Starts the activity for the given player.
     * @param player the player
     */
    fun startActivity(player: Player) {
        if (getVarbit(player, Vars.VARBIT_MAKING_HISTORY_MUSEUM_BUILT_1390) == 1 && inInventory(player, Items.ENCHANTED_KEY_6754)) {
            registerLogoutListener(player, "enchanted-key") { pl ->
                removeItem(pl, Items.ENCHANTED_KEY_6754, Container.INVENTORY)
            }
        }
    }
}


/**
 * Represents the Enchanted key activity rewards.
 */
private enum class EnchantedKeyTreasures(val location: Location, val rewards: List<Pair<Int, Int>>, val group: Group, val completionMessage: String? = null) {
    RELLEKKA(Location.create(2716, 3607, 0), listOf(Items.STEEL_ARROW_886 to 20, Items.MITHRIL_ORE_448 to 10, Items.LAW_RUNE_563 to 15), Group.MAKING),
    ARDOUGNE(Location.create(2621, 3239, 0), listOf(Items.PURE_ESSENCE_7937 to 36, Items.IRON_ORE_441 to 15, Items.FIRE_RUNE_554 to 30), Group.MAKING),
    BENCH(Location.create(2417, 3382, 0), listOf(Items.PURE_ESSENCE_7937 to 40, Items.IRON_ARROWTIPS_40 to 20, Items.FIRE_RUNE_554 to 20), Group.MAKING),
    GNOME(Location.create(2448, 3443, 0), listOf(Items.PURE_ESSENCE_7937 to 39, Items.IRON_ARROWTIPS_40 to 20, Items.WATER_RUNE_555 to 30), Group.MAKING),
    ALTAR(Location.create(3033, 3437, 0), listOf(Items.MITHRIL_ORE_448 to 10, Items.IRON_ORE_441 to 15, Items.EARTH_RUNE_557 to 45), Group.MAKING),
    FALADOR(Location.create(2972, 3304, 0), listOf(Items.EARTH_RUNE_557 to 15, Items.IRON_ARROW_884 to 20, Items.SARADOMIN_MJOLNIR_6762 to 1), Group.MAKING),
    MUDSKIPPER(Location.create(3007, 3161, 0), listOf(Items.IRON_ORE_441 to 15, Items.MITHRIL_ARROW_888 to 20, Items.DEATH_RUNE_560 to 15), Group.MAKING),
    SWAMP(Location.create(3161, 3176, 0), listOf(Items.PURE_ESSENCE_7937 to 29, Items.MIND_RUNE_558 to 20, Items.STEEL_ARROW_886 to 20, Items.ZOMBIE_HEAD_6722 to 1), Group.MAKING),
    ALKHARID(Location.create(3292, 3219, 0), listOf(Items.PURE_ESSENCE_7937 to 40, Items.MITHRIL_ORE_448 to 10, Items.ZAMORAK_MJOLNIR_6764 to 1), Group.MAKING),
    EXAM(Location.create(3300, 3350, 0), listOf(Items.PURE_ESSENCE_7937 to 40, Items.IRON_ORE_441 to 15, Items.GUTHIX_MJOLNIR_6760 to 1), Group.MAKING),
    GE(Location.create(3160, 3490, 0), listOf(Items.PURE_ESSENCE_7937 to 39, Items.MITHRIL_ARROW_888 to 10, Items.LAW_RUNE_563 to 15), Group.MAKING, completionMessage = "You have recovered all the buried rewards!"),
    GNOMEBALL_FIELD(Location.create(2400, 3478, 0), listOf(Items.COINS_995 to 510, Items.GOLD_CHARM_12158 to 3, Items.LAW_RUNE_563 to 15, Items.MITHRIL_ARROW_888 to 20), Group.MEETING),
    SHANTAY_PASS(Location.create(3304, 3128, 0), listOf(Items.COINS_995 to 530, Items.GOLD_CHARM_12158 to 3, Items.PURE_ESSENCE_7937 to 10, Items.UNCUT_SAPPHIRE_1624 to 3), Group.MEETING),
    BRIMHAVEN(Location.create(2714, 3168, 0), listOf(Items.COINS_995 to 560, Items.GREEN_CHARM_12159 to 1, Items.COSMIC_RUNE_564 to 5, Items.UNCUT_EMERALD_1622 to 2), Group.MEETING),
    WILDERNESS(Location.create(3167, 3551, 0), listOf(Items.COINS_995 to 650, Items.GREEN_CHARM_12159 to 1, Items.PURE_ESSENCE_7937 to 10, Items.UNCUT_RUBY_1620 to 1), Group.MEETING),
    TAIBWO_WANNAI(Location.create(2845, 3037, 0), listOf(Items.COINS_995 to 750, Items.GREEN_CHARM_12159 to 2, Items.COSMIC_RUNE_564 to 10, Items.MITHRIL_ARROW_888 to 30), Group.MEETING),
    FELDIP_HILLS(Location.create(2515, 2924, 0), listOf(Items.COINS_995 to 800, Items.GOLD_CHARM_12158 to 30, Items.CRIMSON_CHARM_12160 to 1, Items.NATURE_RUNE_561 to 15), Group.MEETING),
    AGILITY_PYRAMID(Location.create(3344, 2787, 0), listOf(Items.COINS_995 to 830, Items.CRIMSON_CHARM_12160 to 1, Items.DEATH_RUNE_560 to 5, Items.UNCUT_RUBY_1620 to 2), Group.MEETING),
    BANDIT_CAMP(Location.create(3046, 3734, 0), listOf(Items.COINS_995 to 950, Items.CRIMSON_CHARM_12160 to 2, Items.UNCUT_EMERALD_1621 to 3, Items.CHAOS_RUNE_562 to 15), Group.MEETING),
    DAEMONHEIM(Location.create(3352, 3674, 0), listOf(Items.COINS_995 to 950, Items.BLUE_CHARM_12163 to 1, Items.PURE_ESSENCE_7937 to 20, Items.GOLD_BAR_2358 to 5), Group.MEETING),
    DEATH_PLATEAU(Location.create(2874, 3607, 0), listOf(Items.COINS_995 to 1010, Items.BLUE_CHARM_12163 to 1, Items.PURE_ESSENCE_7937 to 20, Items.BLOOD_RUNE_565 to 10), Group.MEETING),
    SCORPION_PIT(Location.create(3229, 3945, 0), listOf(Items.COINS_995 to 1100, Items.BLUE_CHARM_12163 to 2, Items.GOLD_BAR_2358 to 15, Items.DEATH_RUNE_560 to 10), Group.MEETING, completionMessage = "You have found the final treasure!");


    enum class Group { MAKING, MEETING }

    data class Treasure(
        val rewards: List<Pair<Int, Int>>,
        val completionMessage: String? = null,
    )

    private fun giveRewards(
        player: Player,
        rewards: List<Pair<Int, Int>>,
    ) {
        rewards.forEach { (item, amount) ->
            addItemOrDrop(player, item, amount)
        }
    }

    private fun finishActivity(
        player: Player,
        attribute: String,
    ) {
        removeItem(player, Items.ENCHANTED_KEY_6754)
        removeAttribute(player, attribute)
        sendMessage(player, "Congratulations! You have completed the Enchanted key mini-quest!")
    }

    private fun handleTreasureDig(
        player: Player,
        location: Location,
        treasures: List<EnchantedKeyTreasures>,
        attribute: String
    ) {
        val currentProgress = getAttribute(player, attribute, 0)

        if (currentProgress < treasures.size && treasures[currentProgress].location == location) {
            val treasure = treasures[currentProgress]

            player.incrementAttribute(attribute)
            giveRewards(player, treasure.rewards)
            sendMessage(player, "You found a treasure!")

            if (treasure.completionMessage != null && currentProgress == treasures.lastIndex) {
                sendMessage(player, treasure.completionMessage)
                finishActivity(player, attribute)
            }
        }
    }

    companion object {
        /**
         * Attribute key for tracking progress with the first Enchanted Key.
         */
        const val ENCHANTED_KEY_ATTR = "/save:start-enchantedkey"

        /**
         * Attribute key for tracking progress with the second Enchanted Key.
         */
        const val ENCHANTED_KEY_2_ATTR = "/save:start-enchantedkey2"

        fun getTreasures(group: Group): List<EnchantedKeyTreasures> =
            values().filter { it.group == group }

        fun getShuffledMaking(player: Player): List<EnchantedKeyTreasures> {
            val (initial, rest) = getTreasures(Group.MAKING).partition { it == RELLEKKA }
            return initial + rest.shuffled(Random(player.hashCode()))
        }

        fun asMap(treasures: List<EnchantedKeyTreasures>): Map<Location, Treasure> =
            treasures.associate { it.location to Treasure(it.rewards, it.completionMessage) }
    }
}