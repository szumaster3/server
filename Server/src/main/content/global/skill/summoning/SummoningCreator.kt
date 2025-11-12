package content.global.skill.summoning

import core.api.*
import core.cache.def.impl.CS2Mapping
import core.game.component.Component
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import shared.consts.Animations
import shared.consts.Components
import shared.consts.Sounds

/**
 * Object responsible for summoning creation tasks.
 */
object SummoningCreator {

    /**
     * Represents the params used for the access mask on the pouch creating
     * interface.
     */
    private val POUCH_PARAMS = arrayOf(
        "List<col=ff9040>", "Infuse-X<col=ff9040>", "Infuse-All<col=ff9040>", "Infuse-10<col=ff9040>", "Infuse-5<col=ff9040>", "Infuse<col=ff9040>",
        20, 4, (Components.SUMMONING_POUCHES_669 shl 16) or 15
    )

    /**
     * Represents the params used for the access mask on the scroll creating
     * interface.
     */
    private val SCROLL_PARAMS = arrayOf(
        "Transform-X<col=ff9040>", "Transform-All<col=ff9040>", "Transform-10<col=ff9040>", "Transform-5<col=ff9040>", "Transform<col=ff9040>",
        20, 4, (Components.SUMMONING_SCROLLS_673 shl 16) or 15
    )

    /**
     * The component representing the summoning pouch interface.
     */
    private val SUMMONING_COMPONENT = Component(Components.SUMMONING_POUCHES_669)

    /**
     * The component representing the summoning scroll interface.
     */
    private val SCROLL_COMPONENT = Component(Components.SUMMONING_SCROLLS_673)

    /**
     * The client script for init the lore interface inventory.
     */
    private const val LORE_INTERFACE_INV_INIT_SCRIPT = 757

    /**
     * The procedure for init the lore interface inventory.
     */
    private const val LORE_INTERFACE_INV_INIT_PROC = 765

    /**
     * Opens the summoning creation interface.
     */
    @JvmStatic
    fun open(player: Player, pouch: Boolean) = configure(player, pouch)

    /**
     * Configures the summoning interface.
     */
    @JvmStatic
    fun configure(player: Player, pouch: Boolean) {
        val component = if (pouch) SUMMONING_COMPONENT else SCROLL_COMPONENT
        val scriptParams = if (pouch) POUCH_PARAMS else SCROLL_PARAMS
        val settings = if (pouch) 190 else 126
        val interfaceId = if (pouch) 669 else 673

        player.interfaceManager.open(component)
        player.packetDispatch.sendRunScript(
            if (pouch) LORE_INTERFACE_INV_INIT_SCRIPT else LORE_INTERFACE_INV_INIT_PROC,
            if (pouch) "Iiissssss" else "Iiisssss",
            *scriptParams,
        )
        player.packetDispatch.sendIfaceSettings(settings, 15, interfaceId, 0, 78)
    }


    /**
     * Method used to create a summoning node type.
     * @param player the player.
     * @param amount the amount.
     * @param node the node.
     */
    @JvmStatic
    fun create(player: Player, amount: Int, node: Any?) {
        node?.let {
            player.pulseManager.run(CreatePulse(player, SummoningNode.parse(node), amount))
        }
    }

    /**
     * Method used to list the items needed for a pouch.
     * @param pouch the pouch.
     */
    @JvmStatic
    fun list(player: Player, pouch: SummoningPouch) {
        sendMessage(player, "${CS2Mapping.forId(1186)?.map?.get(pouch.pouchId) as? String}")
    }

    class CreatePulse(player: Player?, private val type: SummoningNode, private val amount: Int) : SkillPulse<Item?>(player, null) {

        private val regionLocations = mapOf(
            9366  to Location(2323, 9629, 0),
            9372  to Location(2332, 10009, 0),
            10031 to Location(2521, 3055, 0),
            10802 to Location(2716, 3211, 0),
            13201 to Location(3295, 9311, 0),
            13720 to null
        )

        private val objectIDs = run {
            val regionId = player?.location?.regionId
            val loc = when (regionId) {
                13720 -> Location(3441, 9749, if (player?.location?.z == 1) 1 else 0)
                else  -> regionLocations[regionId] ?: Location(2209, 5344, 0)
            }
            getObject(loc)
        }

        override fun checkRequirements(): Boolean {
            player.interfaceManager.close()
            return when {
                getStatLevel(player, Skills.SUMMONING) < type.level -> {
                    sendMessage(player, "You need a Summoning level of at least ${type.level} to do this.")
                    false
                }

                amount == 0 || !type.required.all { inInventory(player, it.id) } -> {
                    sendMessage(player, "You don't have the required items to make this.")
                    false
                }

                // "You should speak to Pikkupstix. He will tell you what to do with the scrolls and the"
                // "spirit wolf pouch."

                // You should speak to Pikkupstix to get your reward.

                else -> true
            }
        }

        override fun animate() {
            lock(player, 3)
            playAudio(player, Sounds.CRAFT_POUCH_4164)
            animate(player, Animations.INFUSE_SUMMONING_POUCH_8500)
        }

        override fun stop() {
            super.stop()
            animateScenery(player, objectIDs!!, 8510, true)
        }

        override fun reward(): Boolean {
            if (delay == 1) {
                delay = 6
                animateScenery(player, objectIDs!!, 8509, true)
                return false
            }

            animateScenery(player, objectIDs!!, 8510, true)
            repeat(amount) {
                if (type.required.all { anyInInventory(player, it.id) } && player.inventory.remove(*type.required)) {
                    player.inventory.add(type.product)
                    rewardXP(player, Skills.SUMMONING, type.experience)
                }
            }
            return true
        }
    }

    /**
     * Represents a summoning node type.
     * @author Vexia
     */
    class SummoningNode(val base: Any, val required: Array<Item>, val product: Item, val experience: Double, val level: Int) {
        val isPouch: Boolean get() = base is SummoningPouch

        companion object {
            /**
             * Parses a node (either a pouch or scroll) into a [SummoningNode] object.
             *
             * @param node The node to parse.
             * @return The parsed [SummoningNode].
             * @throws IllegalArgumentException If the node type is invalid.
             */
            fun parse(node: Any): SummoningNode =
                when (node) {
                    is SummoningPouch ->
                        SummoningNode(
                            base = node,
                            required = node.items,
                            product = Item(node.pouchId, 1),
                            experience = node.createExperience,
                            level = node.requiredLevel,
                        )

                    is SummoningScroll ->
                        SummoningNode(
                            base = node,
                            required = node.items.map { Item(it, 1) }.toTypedArray(),
                            product = Item(node.itemId, 10),
                            experience = node.xp,
                            level = node.level,
                        )

                    else -> throw IllegalArgumentException("Invalid node type: [${node::class.simpleName}]")
                }
        }
    }
}
