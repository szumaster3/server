package content.data

import core.api.*
import core.game.dialogue.DialogueAction
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.update.flag.context.Graphics
import core.tools.StringUtils
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Enum representing the different types of gods and their associated items and actions.
 *
 * @property cape The item representing the cape of the god.
 * @property staff The item representing the staff of the god.
 * @property statueId The ID of the god's statue.
 * @property npcId The ID of the associated NPC for the god.
 * @property dropMessage The message displayed when the god's cape drops.
 */
enum class GodType(
    val cape: Item,
    val staff: Item,
    val statueId: Int,
    val npcId: Int,
    val dropMessage: String,
) {
    /**
     * Saradomin god type with associated cape, staff, statue, and NPC.
     */
    SARADOMIN(
        cape = Item(Items.SARADOMIN_CAPE_2412),
        staff = Item(Items.SARADOMIN_STAFF_2415),
        statueId = org.rs.consts.Scenery.STATUE_OF_SARADOMIN_2873,
        npcId = NPCs.BATTLE_MAGE_913,
        dropMessage = "The cape disappears in a flash of light as it touches the ground.",
    ),

    /**
     * Guthix god type with associated cape, staff, statue, and NPC.
     */
    GUTHIX(
        cape = Item(Items.GUTHIX_CAPE_2413),
        staff = Item(Items.GUTHIX_STAFF_2416),
        statueId = org.rs.consts.Scenery.STATUE_OF_GUTHIX_2875,
        npcId = NPCs.BATTLE_MAGE_914,
        dropMessage = "The cape disintegrates as it touches the earth.",
    ),

    /**
     * Zamorak god type with associated cape, staff, statue, and NPC.
     */
    ZAMORAK(
        cape = Item(Items.ZAMORAK_CAPE_2414),
        staff = Item(Items.ZAMORAK_STAFF_2417),
        statueId = org.rs.consts.Scenery.STATUE_OF_ZAMORAK_2874,
        npcId = NPCs.BATTLE_MAGE_912,
        dropMessage = "The cape ignites and burns up as it touches the ground.",
    ), ;

    /**
     * Allows the player to pray to the associated god statue.
     * If the player does not already possess any of the god's items, it triggers a prayer action.
     *
     * @param player The player interacting with the statue.
     * @param statue The statue being prayed to.
     */
    fun pray(
        player: Player,
        statue: Scenery,
    ) {
        if (hasAny(player)) {
            lock(player, 3)
            animate(player, Animations.HUMAN_PRAY_645)
            sendMessage(player, "You kneel and begin to chant to ${getName()}...")
            sendMessage(player, "...but there is no response.")
            return
        }

        sendDialogue(player, "You kneel and begin to chant to ${getName()}...")
        addDialogueAction(
            player,
            object : DialogueAction {
                override fun handle(
                    player: Player,
                    buttonId: Int,
                ) {
                    lock(player, 4)
                    animate(player, Animations.HUMAN_PRAY_645)
                    GameWorld.Pulser.submit(
                        object : Pulse(3, player) {
                            override fun pulse(): Boolean {
                                val loc = statue.location.transform(0, -1, 0)
                                val g = GroundItemManager.get(cape.id, loc, player)
                                if (g == null) {
                                    GroundItemManager.create(cape, loc, player)
                                }

                                sendGraphics(
                                    Graphics(
                                        org.rs.consts.Graphics.RANDOM_EVENT_PUFF_OF_SMOKE_86,
                                        0,
                                        0,
                                    ),
                                    loc,
                                )
                                return true
                            }
                        },
                    )
                }
            },
        )
    }

    companion object {
        /**
         * Retrieves the corresponding god type for a given statue ID.
         *
         * @param scenery The ID of the statue.
         * @return The corresponding GodType, or null if not found.
         */
        @JvmStatic
        fun forScenery(scenery: Int): GodType? {
            return values().find { it.statueId == scenery }
        }

        /**
         * Retrieves the corresponding god type for a player based on the cape they possess.
         *
         * @param player The player whose inventory or equipment is checked for the cape.
         * @param invyOnly If true, only checks the player's inventory.
         * @return The corresponding GodType, or null if no cape is found.
         */
        @JvmStatic
        fun getCape(
            player: Player,
            invyOnly: Boolean,
        ): GodType? {
            return values().find { cape ->
                if (invyOnly) {
                    player.inventory.containsItems(cape.cape)
                } else {
                    player.equipment.containsItem(cape.cape) || player.inventory.containsItems(cape.cape)
                }
            }
        }

        /**
         * Retrieves the corresponding god type for a player based on the cape they possess, checking both inventory and equipment.
         *
         * @param player The player whose inventory and equipment are checked for the cape.
         * @return The corresponding GodType, or null if no cape is found.
         */
        fun getCape(player: Player): GodType? {
            return getCape(player, false)
        }

        /**
         * Retrieves the corresponding god type for a given cape item.
         *
         * @param cape The cape item.
         * @return The corresponding GodType, or null if not found.
         */
        fun forCape(cape: Item): GodType? {
            return values().find { it.cape.id == cape.id }
        }

        /**
         * Retrieves the corresponding god type for a given NPC ID.
         *
         * @param id The NPC ID.
         * @return The corresponding GodType, or null if not found.
         */
        fun forId(id: Int): GodType? {
            return values().find { it.npcId == id }
        }

        /**
         * Checks if the player possesses any of the god's cape items.
         *
         * @param player The player to check.
         * @return True if the player has any of the god's capes, otherwise false.
         */
        fun hasAny(player: Player): Boolean {
            return values().any { player.hasItem(it.cape) }
        }
    }

    /**
     * Checks if the player has the associated god's cape equipped.
     *
     * @param player The player to check.
     * @return True if the player has the cape equipped, otherwise false.
     */
    fun isFriendly(player: Player): Boolean {
        return player.equipment.containsItem(cape)
    }

    /**
     * Retrieves the display name of the god.
     *
     * @return The formatted display name of the god.
     */
    fun getName(): String {
        return StringUtils.formatDisplayName(name.lowercase())
    }
}
