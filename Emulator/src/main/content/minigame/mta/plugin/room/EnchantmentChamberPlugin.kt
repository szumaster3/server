package content.minigame.mta.plugin.room

import content.minigame.mta.plugin.MTAZone
import core.api.*
import core.api.ui.sendInterfaceConfig
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
import org.rs.consts.Animations
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Music

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

    override fun interact(
        e: Entity,
        target: Node,
        option: Option,
    ): Boolean {
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

    fun getRespawnPulse(item: GroundItem) =
        object : Pulse(if (settings!!.isDevMode) 45 else RandomFunction.random(700, 800)) {
            override fun pulse(): Boolean {
                GroundItemManager.create(item)
                return true
            }
        }

    fun removeGroundSpawns(player: Player) {
        getGroundSpawns(player).forEach(GroundItemManager::destroy)
    }

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
        animate(player, Animations.MULTI_TAKE_832)
        removeItem(player, Item(ORB.id, amount))

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

    enum class Shapes(
        val sceneryId: Int,
        val item: Item,
    ) {
        CUBE(org.rs.consts.Scenery.CUBE_PILE_10799, Item(Items.CUBE_6899)),
        CYLINDER(org.rs.consts.Scenery.CYLINDER_PILE_10800, Item(Items.CYLINDER_6898)),
        ICOSAHEDRON(org.rs.consts.Scenery.ICOSAHEDRON_PILE_10801, Item(Items.ICOSAHEDRON_6900)),
        PENTAMID(org.rs.consts.Scenery.PENTAMID_PILE_10802, Item(Items.PENTAMID_6901)),
        ;

        fun take(
            player: Player,
            scenery: Scenery?,
        ) {
            if (!hasSpaceFor(player, item)) {
                sendMessage(player, "You have no space left in your inventory.")
                return
            }
            lock(player, 1)
            addItem(player, item.id)
            animate(player, Animations.HUMAN_BURYING_BONES_827)
        }

        fun setAsBonus(player: Player) {
            values().forEach {
                sendInterfaceConfig(player, Components.MAGICTRAINING_ENCHANT_195, it.child, it != this)
            }
        }

        val child: Int get() = 1 + ordinal

        companion object {
            fun forItem(item: Item): Shapes? = values().find { it.item.id == item.id }

            fun forId(id: Int): Shapes? = values().find { it.sceneryId == id }
        }
    }

    companion object {
        val ZONE = EnchantmentChamberPlugin()
        var BONUS_SHAPE = RandomFunction.getRandomElement(Shapes.values())
        private val RUNES =
            arrayOf(Item(Items.DEATH_RUNE_560, 3), Item(Items.BLOOD_RUNE_565, 3), Item(Items.COSMIC_RUNE_564, 3))
        private val STONES =
            arrayOf(
                Location.create(3354, 9645, 0),
                Location.create(3353, 9635, 0),
                Location.create(3359, 9632, 0),
                Location.create(3375, 9633, 0),
                Location.create(3374, 9643, 0),
                Location.create(3373, 9651, 0),
            )
        private val ORB = Item(Items.ORB_6902)
        private val PLAYERS = mutableListOf<Player>()
        private var guardian: NPC? = null
        private val DSPAWNS = mutableMapOf<String, MutableList<GroundItem>>()

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

                    sendChat(
                        guardian!!,
                        "The bonus shape has changed to the ${BONUS_SHAPE.name.lowercase().replace('_', ' ')}.",
                    )
                    return false
                }
            }
    }
}
