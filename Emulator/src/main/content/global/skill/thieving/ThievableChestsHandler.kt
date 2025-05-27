package content.global.skill.thieving

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.GameWorld.ticks
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Items

/**
 * Handles the interaction with thievable chests.
 */
@Initializable
class ThievableChestsHandler : OptionHandler() {

    /**
     * Initializes the chest interactions by assigning this handler to the appropriate scenery objects.
     *
     * @param arg Optional argument, ignored in this implementation.
     * @return The current instance of [ThievableChestsHandler].
     */
    override fun newInstance(arg: Any?): Plugin<Any> {
        for (chest in Chest.values()) {
            for (id in chest.objectIds) {
                val def = SceneryDefinition.forId(id)
                def.handlers["option:open"] = this
                def.handlers["option:search for traps"] = this
            }
        }
        return this
    }

    /**
     * Handles the interaction when the player selects an option on a chest.
     *
     * @param player The player interacting with the chest.
     * @param node The node (chest) being interacted with.
     * @param option The option selected by the player ("open" or "search for traps").
     * @return True if the interaction was handled successfully, false otherwise.
     */
    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        val chest = Chest.forId(node.id)
        when (option) {
            "open" -> {
                if (chest != null) {
                    chest.open(player, node as Scenery)
                    return true
                }
                return true
            }

            "search for traps" -> {
                chest!!.searchTraps(player, node as Scenery)
                return true
            }
        }
        return true
    }

    /**
     * Represents the different types of thievable chests in the game.
     */
    enum class Chest(val objectIds: IntArray, val level: Int, val experience: Double, val rewards: Array<Item>, val respawn: Int, ) {
        TEN_COIN(2566, 13, 7.8, arrayOf(Item(Items.COINS_995, 10)), 7),
        NATURE_RUNE(2567, 28, 25.0, arrayOf(Item(Items.COINS_995, 3), Item(Items.NATURE_RUNE_561, 1)), 8),
        FIFTY_COIN(2568, 43, 125.0, arrayOf(Item(Items.COINS_995, 50)), 55),
        STEEL_ARROWHEADS(2573, 47, 150.0, arrayOf(Item(41, 5)), 210),
        BLOOD_RUNES(2569, 59, 250.0, arrayOf(Item(Items.COINS_995, 500), Item(Items.BLOOD_RUNE_565, 2)), 135),
        PALADIN(2570, 72, 500.0, arrayOf(Item(Items.COINS_995, 1000), Item(Items.RAW_SHARK_383, 1), Item(Items.ADAMANTITE_ORE_449, 1), Item(Items.UNCUT_SAPPHIRE_1623, 1)), 120),
        ;

        private var currentRespawn = 0

        constructor(objectId: Int, level: Int, experience: Double, rewards: Array<Item>, respawn: Int, ) : this(intArrayOf(objectId), level, experience, rewards, respawn)

        /**
         * Opens the chest and applies the corresponding effects such as trap activation and damage.
         *
         * @param player The player interacting with the chest.
         * @param scenery The chest object being opened.
         */
        internal fun open(
            player: Player,
            scenery: Scenery,
        ) {
            if (isRespawning) {
                sendMessage(player, "It looks like this chest has already been looted.")
                return
            }
            lock(player, 2)
            sendMessage(player, "You have activated a trap on the chest.")
            impact(player, getHitAmount(player), HitsplatType.NORMAL)
        }

        /**
         * Searches the chest for traps, checking if the player is capable of disabling the trap.
         * If successful, the player receives rewards and experience.
         *
         * @param player The player searching for traps.
         * @param scenery The chest object being searched.
         */
        fun searchTraps(player: Player, scenery: Scenery, ) {
            player.faceLocation(scenery.location)
            if (isRespawning) {
                sendMessage(player, "It looks like this chest has already been looted.")
                return
            }
            if (getStatLevel(player, Skills.THIEVING) < level) {
                animate(player, Animations.OPEN_CHEST_536, false)
                lock(player, 2)
                sendMessage(player, "You search the chest for traps.")
                sendMessageWithDelay(player, "You find nothing.", 1)
                return
            }
            if (freeSlots(player) == 0) {
                sendMessage(player, "Not enough inventory space.")
                return
            }
            lock(player, 6)
            animate(player, Animations.OPEN_CHEST_536, false)
            sendMessage(player, "You find a trap on the chest...")
            player.impactHandler.disabledTicks = 6
            Pulser.submit(
                object : Pulse(1, player) {
                    var counter: Int = 0

                    override fun pulse(): Boolean {
                        when (++counter) {
                            2 -> sendMessage(player, "You disable the trap.")
                            4 -> {
                                animate(player, Animation.create(Animations.OPEN_CHEST_536))
                                player.faceLocation(scenery.location)
                                sendMessage(player, "You open the chest.")
                            }

                            6 -> {
                                for (i in rewards) {
                                    player.inventory.add(i, player)
                                }
                                sendMessage(player, "You find treasure inside!")
                                rewardXP(player, Skills.THIEVING, experience)
                                if (scenery.isActive) {
                                    replaceScenery(scenery, 2574, 3)
                                }
                                setRespawn()
                                return true
                            }
                        }
                        return false
                    }
                },
            )
        }

        /**
         * Sets the respawn time for the chest after it has been looted.
         */
        fun setRespawn() {
            currentRespawn = ticks + (respawn / 0.6).toInt()
        }

        /**
         * Returns whether the chest is currently respawning (i.e., it cannot be looted again).
         */
        val isRespawning: Boolean
            get() = currentRespawn > ticks

        companion object {
            /**
             * Calculates the damage to apply to the player when they activate a trap.
             *
             * @param player The player who triggered the trap.
             * @return The amount of damage to deal to the player.
             */
            protected fun getHitAmount(player: Player): Int {
                var hit = player.getSkills().lifepoints / 12
                if (hit < 2) {
                    hit = 2
                }
                return hit
            }

            /**
             * Finds a chest by its object id.
             *
             * @param id The object id of the chest.
             * @return The corresponding [Chest] enum, or `null` if no match is found.
             */
            fun forId(id: Int): Chest? {
                for (chest in values()) {
                    for (i in chest.objectIds) {
                        if (i == id) {
                            return chest
                        }
                    }
                }
                return null
            }
        }
    }
}
