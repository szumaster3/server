package content.minigame.mage_training.plugin.rooms

import content.minigame.mage_training.plugin.MTAZone
import core.api.*
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.GameWorld.settings
import core.game.world.map.Location
import core.game.world.map.RegionManager.getNpc
import core.game.world.map.zone.ZoneBorders
import core.tools.RandomFunction
import shared.consts.*

/**
 * Represents the Enchantment Chamber.
 */
class EnchantmentChamberPlugin :
    MTAZone(
        "Enchantment Chamber",
        arrayOf(
            Item(Items.CUBE_6899),
            Item(Items.CYLINDER_6898),
            Item(Items.ICOSAHEDRON_6900),
            Item(Items.PENTAMID_6901),
            Item(Items.DRAGONSTONE_6903),
            Item(Items.ORB_6902),
        ),
    ) {
    override fun leave(
        entity: Entity,
        logout: Boolean,
    ): Boolean {
        if (entity is Player) {
            PLAYERS.remove(entity)
            removeGroundSpawns(entity.asPlayer())
        }
        return super.leave(entity, logout)
    }

    override fun enter(entity: Entity): Boolean {
        if (guardian == null) guardian = getNpc(Location(3361, 9647, 0), 3100, 20)

        if (entity is Player && PLAYERS.add(entity)) {
            if (!PULSE.isRunning) {
                PULSE.restart()
                PULSE.start()
                Pulser.submit(PULSE)
            }
            createGroundSpawns(entity)
            BONUS_SHAPE.setAsBonus(entity)
            update(entity)
            entity.musicPlayer.takeIf { !it.hasUnlocked(Music.THE_ENCHANTER_541) }?.unlock(Music.THE_ENCHANTER_541)
        }
        return super.enter(entity)
    }

    override fun interact(e: Entity, target: Node, option: Option): Boolean {
        if (e is Player) {
            when (target.id) {
                in 10799..10802 -> Shapes.forId(target.id)?.take(e, target.asScenery())
                10803 -> deposit(e)
                else -> return super.interact(e, target, option)
            }
            return true
        }
        return super.interact(e, target, option)
    }

    fun createGroundSpawns(player: Player) {
        val playerName = player.name ?: throw IllegalArgumentException("Player name cannot be null.")
        DSPAWNS[playerName]?.forEach { it.dropper = player } ?: run {
            val items = mutableListOf<GroundItem>()
            STONES.forEach { location ->
                val item =
                    object : GroundItem(Item(6903), location, player) {
                        override fun isAutoSpawn() = true

                        override fun isActive() = true

                        override fun isRemainPrivate() = true

                        override fun respawn() = Pulser.submit(getRespawnPulse(this))
                    }
                items.add(item)
                GroundItemManager.create(item)
            }
            DSPAWNS[player.name] = items
        }
    }

    /**
     * Creates a respawn pulse for the given ground item.
     *
     * @param item The ground item to respawn.
     * @return A Pulse object managing respawn timing.
     */
    fun getRespawnPulse(item: GroundItem) =
        object : Pulse(if (settings!!.isDevMode) 45 else RandomFunction.random(700, 800)) {
            override fun pulse(): Boolean {
                GroundItemManager.create(item)
                return true
            }
        }

    /**
     * Removes all ground spawns..
     *
     * @param player The player whose ground spawns should be removed.
     */
    fun removeGroundSpawns(player: Player) {
        getGroundSpawns(player).forEach(GroundItemManager::destroy)
    }

    /**
     * Gets the list of ground items spawned.
     *
     * @param player The player to query.
     * @return Mutable list of ground items.
     */
    fun getGroundSpawns(player: Player?): MutableList<GroundItem> {
        if (player == null) throw IllegalArgumentException("Player cannot be null.")
        return DSPAWNS.getOrPut(player.name) { mutableListOf() }
    }

    private fun deposit(player: Player) {
        if (!inInventory(player, ORB.id)) {
            sendMessage(player, "You don't have any orbs to deposit.")
            return
        }
        val amount = amountInInventory(player, ORB.id)
        animate(player, Animations.HUMAN_MULTI_USE_832)
        removeItem(player, Item(ORB.id, amount))
        playAudio(player, Sounds.MTA_DEPOSIT_ORB_1664)

        val updatedAmount = (player.getAttribute("mta-orb", 0) + amount).also { setAttribute(player, "mta-orb", it) }

        if (updatedAmount >= 20) {
            player.inventory.add(RandomFunction.getRandomElement(RUNES), player)
            sendDialogue(player, "Congratulations! You've been rewarded with an item for your efforts.")
            setAttribute(player, "mta-orb", 0)
        }
    }

    override fun configure() {
        PULSE.stop()
        register(ZoneBorders(3335, 9612, 3389, 9663, 0, true))
    }

    /**
     * Enum representing the shapes available in this room.
     *
     * @property sceneryId The id of the shape.
     * @property item The item associated with the shape.
     */
    enum class Shapes(val sceneryId: Int, val item: Item) {
        CUBE(shared.consts.Scenery.CUBE_PILE_10799, Item(Items.CUBE_6899)),
        CYLINDER(shared.consts.Scenery.CYLINDER_PILE_10800, Item(Items.CYLINDER_6898)),
        ICOSAHEDRON(shared.consts.Scenery.ICOSAHEDRON_PILE_10801, Item(Items.ICOSAHEDRON_6900)),
        PENTAMID(shared.consts.Scenery.PENTAMID_PILE_10802, Item(Items.PENTAMID_6901)),
        ;

        /**
         * Handles player taking the shape item from scenery.
         *
         * @param player The player taking the item.
         * @param scenery The scenery object being taken from.
         */
        fun take(player: Player, scenery: Scenery?) {
            if (!hasSpaceFor(player, item)) {
                sendMessage(player, "You have no space left in your inventory.")
                return
            }
            lock(player, 1)
            addItem(player, item.id)
            animate(player, Animations.HUMAN_BURYING_BONES_827)
        }

        /**
         * Updates the interface to set this shape as the current bonus.
         *
         * @param player The player whose interface is updated.
         */
        fun setAsBonus(player: Player) {
            values().forEach {
                sendInterfaceConfig(player, Components.MAGICTRAINING_ENCHANT_195, it.child, it != this)
            }
        }

        val child: Int get() = 1 + ordinal

        companion object {
            /**
             * Finds a shape by the item.
             */
            fun forItem(item: Item): Shapes? = values().find { it.item.id == item.id }

            /**
             * Finds a shape by the scenery id.
             */
            fun forId(id: Int): Shapes? = values().find { it.sceneryId == id }
        }
    }

    companion object {
        /**
         * Singleton instance of the zone plugin.
         */
        val ZONE = EnchantmentChamberPlugin()

        /**
         * Current bonus shape for players.
         */
        var BONUS_SHAPE = RandomFunction.getRandomElement(Shapes.values())

        /**
         * Possible rune rewards for orb deposits.
         */
        private val RUNES =
            arrayOf(Item(Items.DEATH_RUNE_560, 3), Item(Items.BLOOD_RUNE_565, 3), Item(Items.COSMIC_RUNE_564, 3))

        /**
         * Locations of stones where ground items spawn.
         */
        private val STONES = arrayOf(
            Location.create(3354, 9645, 0),
            Location.create(3353, 9635, 0),
            Location.create(3359, 9632, 0),
            Location.create(3375, 9633, 0),
            Location.create(3374, 9643, 0),
            Location.create(3373, 9651, 0),
        )

        /**
         * Orb item instance used for deposits.
         */
        private val ORB = Item(Items.ORB_6902)

        /**
         * Currently active players in the zone.
         */
        private val PLAYERS = mutableListOf<Player>()

        /**
         * The guardian NPC in the chamber.
         */
        private var guardian: NPC? = null

        /**
         * Maps player names to their ground item spawns.
         */
        private val DSPAWNS = mutableMapOf<String, MutableList<GroundItem>>()

        /**
         * Pulse task that changes the bonus shape.
         */
        private val PULSE =
            object : Pulse(36) {
                override fun pulse(): Boolean {
                    if (PLAYERS.isEmpty()) return true

                    var newBonusShape: Shapes? = null
                    while (newBonusShape == null || newBonusShape == BONUS_SHAPE) {
                        newBonusShape = RandomFunction.getRandomElement(Shapes.values())
                    }
                    BONUS_SHAPE = newBonusShape
                    PLAYERS.filter(Player::isActive).forEach(BONUS_SHAPE::setAsBonus)

                    sendChat(guardian!!, "The bonus shape has changed to the ${BONUS_SHAPE.name.lowercase().replace('_', ' ')}.")
                    return false
                }
            }
    }
}
