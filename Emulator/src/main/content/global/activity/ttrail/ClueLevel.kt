package content.global.activity.ttrail

import core.api.*
import core.game.component.Component
import core.game.container.access.InterfaceContainer
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.config.ClueRewardParser
import core.game.world.GameWorld
import core.tools.RandomFunction
import org.rs.consts.Components
import org.rs.consts.Items

/**
 * Represents the difficulty levels of clue scrolls.
 *
 * @property casketId the item id of the clue casket for this level.
 * @property minSteps minimum number of steps for a trail of this level.
 * @property maxSteps maximum number of steps for a trail of this level.
 */
enum class ClueLevel(
    val casketId: Int,
    val minSteps: Int,
    val maxSteps: Int,
) {
    EASY(Items.CASKET_2714, 2, 5),
    MEDIUM(Items.CASKET_2717, 2, 5),
    HARD(Items.CASKET_2720, 4, 8),
    UNKNOWN(0, 0, 0),
    ;

    companion object {
        /**
         * Determines the clue level corresponding to a given casket item.
         *
         * @param casket the casket item to identify.
         * @return the matching ClueLevel, or UNKNOWN if no match.
         */
        private fun getLevelForCasket(casket: Item): ClueLevel =
            when (casket.id) {
                Items.CASKET_2714 -> EASY
                Items.CASKET_2717 -> MEDIUM
                Items.CASKET_2720 -> HARD
                else -> UNKNOWN
            }

        /**
         * Opens a clue casket for the player, handling reward distribution or
         * clue progression depending on trail completion.
         *
         * If the trail is completed or dev mode is enabled, rewards are rolled,
         * granted to the player, and the trail is cleared.
         * Otherwise, a new clue is provided, replacing the casket.
         *
         * @param player the player opening the casket.
         * @param casket the casket item being opened.
         */
        fun open(
            player: Player,
            casket: Item?,
        ) {
            if (casket == null || !player.inventory.containsItem(casket)) {
                return
            }

            val playerTrails = TreasureTrailManager.getInstance(player)

            val trailCompleted = playerTrails.isCompleted
            val isDevMode = GameWorld.settings!!.isDevMode

            val clueLevel = getLevelForCasket(casket)

            if (trailCompleted || isDevMode) {
                val rewards = rollLoot(player, clueLevel)

                player.interfaceManager.open(Component(Components.TRAIL_REWARD_364))

                if (removeItem(player, casket)) {
                    var rewardValue = 0L

                    for (reward in rewards) {
                        addItemOrDrop(player, reward.id, reward.amount)
                        rewardValue += reward.value
                    }

                    playerTrails.incrementClues(clueLevel)
                    playerTrails.clearTrail()
                    playJingle(player, 193)
                    sendMessage(player,"Well done, you've completed the Treasure Trail!")
                    sendMessage(player, getChatColor(clueLevel) + "You have completed " + playerTrails.getCompletedClues(clueLevel) + " " + clueLevel.name.lowercase() + " clues.")
                    sendMessage(player,"<col=990000>Your clue is worth approximately $rewardValue gold coins!</col>")
                    val clueIfaceSettings = IfaceSettingsBuilder().enableAllOptions().build()
                    player.packetDispatch.sendIfaceSettings(clueIfaceSettings, 4, Components.TRAIL_REWARD_364, 0, 6)
                    InterfaceContainer.generateItems(player, rewards.toTypedArray(), arrayOf(""), Components.TRAIL_REWARD_364, 4, 3, 3)
                }
                return
            }

            val newClue = ClueScroll.getClue(clueLevel)

            if (casket != null && player.inventory.remove(casket, casket.slot, true)) {
                player.inventory.replace(newClue, casket.slot)
            } else {
                player.inventory.add(newClue)
            }

            playerTrails.clueId = newClue!!.id
            sendItemDialogue(player, newClue, "You've found another clue!")
        }

        /**
         * Rolls random loot rewards for the player based on clue level.
         *
         * The number of items and rarity depends on the clue difficulty.
         *
         * @param player the player receiving the loot.
         * @param level the clue difficulty level.
         * @return a list of items rewarded.
         */
        private fun rollLoot(
            player: Player,
            level: ClueLevel,
        ): List<Item> {
            val loot = ArrayList<Item>()
            var itemCount = RandomFunction.random(1, 6)

            if (level == HARD) {
                itemCount = itemCount.coerceAtLeast(RandomFunction.random(4, 6))
            }

            repeat(itemCount) {
                when (level) {
                    EASY -> loot.addAll(ClueRewardParser.easyTable.roll(player))
                    MEDIUM -> loot.addAll(ClueRewardParser.medTable.roll(player))
                    HARD -> loot.addAll(ClueRewardParser.hardTable.roll(player))
                    else -> {}
                }
            }

            if (level == HARD && RandomFunction.random(100) == 50) {
                loot.addAll(ClueRewardParser.rareTable.roll(player))
            }

            return loot
        }

        /**
         * Returns a chat color code string based on the clue level.
         *
         * @param level the clue difficulty level.
         * @return the color code string for chat messages.
         */
        private fun getChatColor(level: ClueLevel): String =
            when (level) {
                HARD -> "<col=ff1a1a>"
                MEDIUM -> "<col=b38f00>"
                else -> "<col=00e673>"
            }
    }
    /**
     * Creates an item representing the clue casket of this level.
     *
     * @return an Item instance of the casket.
     */
    fun getCasket(): Item = Item(casketId)

    /**
     * Returns the name of the clue level.
     *
     * @return the name of this ClueLevel enum constant.
     */
    fun getName(): String = Companion.toString()
}
