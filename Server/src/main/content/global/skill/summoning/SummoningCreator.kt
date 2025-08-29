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
import shared.consts.*

/**
 * Object responsible for summoning creation tasks.
 */
object SummoningCreator {

    /**
     * Parameters for summoning pouches.
     */
    private val POUCH_PARAMS =
        arrayOf(
            "List<col=FF9040>",
            "Infuse-X<col=FF9040>",
            "Infuse-All<col=FF9040>",
            "Infuse-10<col=FF9040>",
            "Infuse-5<col=FF9040>",
            "Infuse<col=FF9040>",
            20,
            4,
            Components.SUMMONING_POUCHES_669 shl 16 or 15
        )

    /**
     * Parameters for summoning scrolls.
     */
    private val SCROLL_PARAMS =
        arrayOf(
            "Transform-X<col=ff9040>",
            "Transform-All<col=ff9040>",
            "Transform-10<col=ff9040>",
            "Transform-5<col=ff9040>",
            "Transform<col=ff9040>",
            20,
            4,
            Components.SUMMONING_SCROLLS_673 shl 16 or 15,
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
     * Opens the summoning creation interface for the player, either for pouches or scrolls.
     *
     * @param player The player to open the interface for.
     * @param pouch A boolean indicating whether to open the pouch or scroll interface.
     */
    @JvmStatic
    fun open(player: Player, pouch: Boolean) = configure(player, pouch)

    /**
     * Configures the summoning interface based on whether the pouch or scroll interface is to be shown.
     *
     * @param player The player to configure the interface for.
     * @param pouch A boolean indicating whether to show the pouch or scroll interface.
     */
    @JvmStatic
    fun configure(player: Player, pouch: Boolean) {
        val component = if (pouch) SUMMONING_COMPONENT else SCROLL_COMPONENT
        val scriptParams = if (pouch) POUCH_PARAMS else SCROLL_PARAMS
        val ifaceId = if (pouch) 190 else 126
        val componentId = if (pouch) 669 else 673

        player.interfaceManager.open(component)
        player.packetDispatch.sendRunScript(
            if (pouch) 757 else 765,
            if (pouch) "Iiissssss" else "Iiisssss",
            *scriptParams,
        )
        player.packetDispatch.sendIfaceSettings(ifaceId, 15, componentId, 0, 78)
    }

    /**
     * Creates a summoning item for the player, using the specified amount and node.
     *
     * @param player The player to create the item for.
     * @param amount The amount of the item to create.
     * @param node The node containing the item creation information.
     */
    @JvmStatic
    fun create(player: Player, amount: Int, node: Any?) {
        node?.let {
            player.pulseManager.run(CreatePulse(player, SummoningNode.parse(node), amount))
        }
    }

    /**
     * Sends a message listing the items in the specified summoning pouch.
     *
     * @param player The player to send the message to.
     * @param pouch The summoning pouch to list.
     */
    @JvmStatic
    fun list(player: Player, pouch: SummoningPouch) {
        player.packetDispatch.sendMessage(CS2Mapping.forId(1186)?.map?.get(pouch.pouchId) as? String)
    }

    /**
     * Skill pulse used for creating a summoning item, which handles the animation, requirements check,
     * item removal, and experience reward.
     */
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
            if (regionId == 13720) {
                val z = if (player?.location?.z != 1) 0 else 1
                getObject(Location(3441, 9749, z))
            } else {
                val loc = regionLocations[regionId] ?: Location(2209, 5344, 0)
                getObject(loc)
            }
        }

        /**
         * Checks the requirements for the creation of the summoning item, including level and required items.
         *
         * @return True if the requirements are met, false otherwise.
         */
        override fun checkRequirements(): Boolean {
            closeInterface(player)
            return when {
                getStatLevel(player, Skills.SUMMONING) < type.level -> {
                    sendMessage(player, "You need a Summoning level of at least ${type.level} to do this.")
                    false
                }

                type.isPouch &&
                        type.product.id == Items.PHOENIX_POUCH_14624 &&
                        !isQuestComplete(player, Quests.IN_PYRE_NEED) -> {
                    sendMessage(player, "You must complete In Pyre Need to infuse phoenix pouches.")
                    false
                }

                amount == 0 || !type.required.all { inInventory(player, it.id) } -> {
                    sendMessage(player, "You don't have the required items to make this.")
                    false
                }

                else -> true
            }
        }

        /**
         * Plays the animation for the creation of the summoning item.
         */
        override fun animate() {
            lock(player, 3)
            animate(player, Animations.INFUSE_SUMMONING_POUCH_8500)
        }

        /**
         * Stops the creation pulse and plays the scenery animation.
         */
        override fun stop() {
            super.stop()
            animateScenery(player, objectIDs!!, 8510, true)
        }

        /**
         * Rewards the player with the created summoning item and experience.
         *
         * @return True if the reward process is complete, false otherwise.
         */
        override fun reward(): Boolean {
            if (delay == 1) {
                delay = 6
                animateScenery(player, objectIDs!!, 8509, true)
                playAudio(player, Sounds.CRAFT_POUCH_4164)
                return false
            }

            // "You should speak to Pikkupstix. He will tell you what to do with the scrolls and the"
            // "spirit wolf pouch."

            // You should speak to Pikkupstix to get your reward.

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
     * Data class representing a summoning creation node, containing the base item, required items, product,
     * experience, and the required level for the creation.
     *
     * @property base The base item (either a pouch or scroll).
     * @property required The items required for the creation.
     * @property product The product created after the pulse.
     * @property experience The experience awarded for creating the item.
     * @property level The level required to create the item.
     */
    class SummoningNode(val base: Any, val required: Array<Item>, val product: Item, val experience: Double, val level: Int, ) {
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
