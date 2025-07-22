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
 * A clue scroll level.
 * @author Vexia
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
         * Get difficulty level for the casket.
         *
         * @param casket the casket.
         * @return the casket matching [ClueLevel].
         */
        private fun getLevelForCasket(casket: Item): ClueLevel =
            when (casket.id) {
                Items.CASKET_2714 -> EASY
                Items.CASKET_2717 -> MEDIUM
                Items.CASKET_2720 -> HARD
                else -> UNKNOWN
            }

        /**
         * Opens a casket for the player.
         * @param player the player.
         * @param casket the casket.
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
                    playAudio(player, 50)
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
         * Gets the rewards
         * @return the rewards
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
         * Gets the Chat color to send on completed clues.
         * @param level The clue level.
         * @return the chat color.
         */
        private fun getChatColor(level: ClueLevel): String =
            when (level) {
                HARD -> "<col=ff1a1a>"
                MEDIUM -> "<col=b38f00>"
                else -> "<col=00e673>"
            }
    }

    /**
     * Gets the casket.
     * @return the casket
     */
    fun getCasket(): Item = Item(casketId)

    /**
     * Gets the name of the clue level.
     * @return the name.
     */
    fun getName(): String = Companion.toString()
}
