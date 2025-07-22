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
 * Represents god books.
 */
enum class GodType(val cape: Item, val staff: Item, val statueId: Int, val npcId: Int, val dropMessage: String, ) {
    SARADOMIN(Item(Items.SARADOMIN_CAPE_2412), Item(Items.SARADOMIN_STAFF_2415), org.rs.consts.Scenery.STATUE_OF_SARADOMIN_2873, NPCs.BATTLE_MAGE_913, "The cape disappears in a flash of light as it touches the ground."),
    GUTHIX(Item(Items.GUTHIX_CAPE_2413), Item(Items.GUTHIX_STAFF_2416), org.rs.consts.Scenery.STATUE_OF_GUTHIX_2875, NPCs.BATTLE_MAGE_914, "The cape disintegrates as it touches the earth."),
    ZAMORAK(Item(Items.ZAMORAK_CAPE_2414), Item(Items.ZAMORAK_STAFF_2417), org.rs.consts.Scenery.STATUE_OF_ZAMORAK_2874, NPCs.BATTLE_MAGE_912, "The cape ignites and burns up as it touches the ground."), ;

    /**
     * Allows the player to pray to the associated god statue.
     */
    fun pray(player: Player, statue: Scenery, ) {
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
         * Get god type for a given statue id.
         */
        fun forScenery(scenery: Int): GodType? = values().find { it.statueId == scenery }

        /**
         * Gets the god type for a player based on the cape they possess.
         */
        fun getCape(player: Player, inv: Boolean): GodType? =
            values().find { cape ->
                if (inv) {
                    player.inventory.containsItems(cape.cape)
                } else {
                    player.equipment.containsItem(cape.cape) || player.inventory.containsItems(cape.cape)
                }
            }

        /**
         * Retrieves the god type for a player based on the cape.
         */
        fun getCape(player: Player): GodType? = getCape(player, false)

        /**
         * Gets the god type for a given cape item.
         */
        fun forCape(cape: Item): GodType? = values().find { it.cape.id == cape.id }

        /**
         * Gets the god type for a given NPC id.
         */
        fun forId(id: Int): GodType? = values().find { it.npcId == id }

        /**
         * Checks if the player possesses any of the god's cape items.
         */
        fun hasAny(player: Player): Boolean = values().any { player.hasItem(it.cape) }
    }

    /**
     * Checks if the player has the associated god's cape equipped.
     */
    fun isFriendly(player: Player): Boolean = player.equipment.containsItem(cape)

    /**
     * Gets the display name of the god.
     */
    fun getName(): String = StringUtils.formatDisplayName(name.lowercase())
}
