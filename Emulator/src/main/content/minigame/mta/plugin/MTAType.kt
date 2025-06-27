package content.minigame.mta.plugin

import core.api.*
import core.game.component.Component
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Scenery

/**
 * Represents a room type in the Magic Training Arena (MTA) minigame.
 */
enum class MTAType(
    val sceneryId: Int,
    val overlay: Component,
    val varbit: Int,
    private val startLocation: Location? = null,
    private val endLocation: Location,
    val mtaZone: MTAZone,
) {
    TELEKINETIC(
        Scenery.TELEKINETIC_TP_10778,
        Component(Components.MAGICTRAINING_TELE_198),
        1485, // Telekinetic points
        null,
        Location.create(3363, 3316, 0),
        TelekineticTheatrePlugin(),
    ) {
        override fun hasRequirement(player: Player): Boolean {
            val magicLevel = getStatLevel(player, Skills.MAGIC)
            if (magicLevel < 33) {
                sendDialogueLines(
                    player,
                    "You need to be able to cast the Telekinetic Grab spell in order to",
                    "enter.",
                )
                return false
            }
            return true
        }
    },

    ALCHEMISTS(
        Scenery.ALCHEMISTS_TP_10780,
        Component(Components.MAGICTRAINING_ALCH_STATS_194),
        1489, // Alchemists points
        Location(3366, 9623, 2),
        Location(3363, 3320, 0),
        AlchemistPlaygroundPlugin.ZONE,
    ) {
        override fun hasRequirement(player: Player): Boolean {
            val magicLevel = getStatLevel(player, Skills.MAGIC)
            if (magicLevel < 21) {
                sendDialogueLines(player, "You need to be able to cast the Low Alchemy spell in order to", "enter.")
                return false
            }
            if (amountInInventory(player, Items.COINS_995) > 0) {
                sendDialogue(player, "You cannot take money into the Alchemists' Playground.")
                return false
            }
            return true
        }

        override fun exit(player: Player) {
            val earn = player.getAttribute("alch-earn", 0)
            if (earn != 0) {
                val coins = Item(995, earn)
                if (player.bank.hasSpaceFor(coins)) {
                    player.bank.add(coins)
                }
                sendDialogue(player, "You've been awarded $earn coins straight into your bank as a reward!")
            }
            super.exit(player)
        }
    },

    ENCHANTERS(
        Scenery.ENCHANTERS_TP_10779,
        Component(Components.MAGICTRAINING_ENCHANT_195),
        1488, // Enchanters points
        Location(3363, 9649, 0),
        Location(3361, 3318, 0),
        EnchantmentChamberPlugin.ZONE,
    ) {
        override fun hasRequirement(player: Player): Boolean {
            val magicLevel = getStatLevel(player, Skills.MAGIC)
            if (magicLevel < 7) {
                sendDialogueLines(player, "You need to be able to cast the Lvl-1 Enchant spell in order to", "enter.")
                return false
            }
            return true
        }
    },

    GRAVEYARD(
        Scenery.GRAVEYARD_TP_10781,
        Component(Components.MAGICTRAINING_GRAVE_196),
        1486, // Graveyard points
        Location(3363, 9639, 1),
        Location(3365, 3318, 0),
        CreatureGraveyardPlugin.ZONE,
    ) {
        override fun hasRequirement(player: Player): Boolean {
            val magicLevel = getStatLevel(player, Skills.MAGIC)
            if (magicLevel < 15) {
                sendDialogueLines(
                    player,
                    "You need to be able to cast the Bones to Bananas spell in order to",
                    "enter.",
                )
                return false
            }
            if (inInventory(player, Items.BANANA_1963, 1) || inInventory(player, Items.PEACH_6883, 1)) {
                sendDialogue(player, "You can't take bananas or peaches into the arena.")
                return false
            }
            return true
        }
    }, ;

    private val playerMagicLevels: MutableMap<Player, Int> = HashMap()

    fun enter(player: Player) {
        if (!player.getSavedData().activityData.isStartedMta ||
            !anyInInventory(player, *ProgressHat.hatIds) &&
            !anyInEquipment(player, *ProgressHat.hatIds)
        ) {
            sendDialogueLines(
                player,
                "You need a Pizazz Progress Hat in order to enter. Talk to the",
                "Entrance Guardian if you don't have one.",
            )
            return
        }

        if (!hasRequirement(player)) {
            return
        }

        if (this !== TELEKINETIC) {
            teleport(player, startLocation!!)
        } else {
            TelekineticTheatrePlugin.start(player)
        }

        sendMessage(player, "You've entered the " + mtaZone.name + ".")
    }

    open fun exit(player: Player) {
        teleport(player, endLocation)
    }

    open fun hasRequirement(player: Player): Boolean = false

    fun getZone(): MTAZone = mtaZone

    companion object {
        private val zoneCache: MutableSet<MTAType> = HashSet(values().toList())

        /**
         * Gets the [MTAType] for the given [MTAZone].
         *
         * @param mtaZone The zone to check.
         * @return The MTA type for this zone.
         */
        @JvmStatic
        fun forZone(mtaZone: MTAZone) = zoneCache.firstOrNull { it.getZone() === mtaZone } ?: TELEKINETIC

        /**
         * Gets the [MTAType] by scenery object id.
         *
         * @param id The object id to match.
         * @return The matching MTA type.
         */
        @JvmStatic
        fun forId(id: Int) = values().firstOrNull { it.sceneryId == id }
    }
}