package content.global.skill.construction.decoration.study

import content.data.GameAttributes
import content.global.plugins.item.TeleportTablet
import content.global.skill.construction.Decoration
import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.InterfaceListener
import core.game.interaction.OptionHandler
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.combat.spell.MagicStaff
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Animations
import shared.consts.Components
import shared.consts.Items
import shared.consts.Scenery

/**
 * Handles lectern interactions.
 */
@Initializable
class LecternPlugin : OptionHandler() {

    /**
     * Represents all teleport–tablet buttons inside the lectern interface.
     */
    private enum class TeleportTabButton(val buttonId: Int, val requiredLevel: Int, val xp: Double, val tabItem: Item, val requiredDecorations: Array<Decoration>, vararg requiredItems: Item) {
        ARDOUGNE(2, 51, 61.0, Item(TeleportTablet.ARDOUGNE_TELEPORT.item), arrayOf(Decoration.TEAK_EAGLE_LECTERN, Decoration.MAHOGANY_EAGLE_LECTERN), SOFT_CLAY, Item(Items.LAW_RUNE_563, 2), Item(Items.WATER_RUNE_555, 2)),
        BONES_TO_BANANNAS(3, 15, 25.0, Item(Items.BONES_TO_BANANAS_8014), arrayOf(Decoration.DEMON_LECTERN, Decoration.TEAK_DEMON_LECTERN, Decoration.MAHOGANY_DEMON_LECTERN), SOFT_CLAY, Item(Items.NATURE_RUNE_561), Item(Items.EARTH_RUNE_557, 2), Item(Items.WATER_RUNE_555, 2)),
        BONES_TO_PEACHES(4, 60, 35.5, Item(Items.BONES_TO_PEACHES_8015), arrayOf(Decoration.MAHOGANY_DEMON_LECTERN), SOFT_CLAY, Item(Items.NATURE_RUNE_561, 2), Item(Items.EARTH_RUNE_557, 4), Item(Items.WATER_RUNE_555, 4)),
        CAMELOT(5, 45, 55.5, Item(TeleportTablet.CAMELOT_TELEPORT.item), arrayOf(Decoration.TEAK_EAGLE_LECTERN, Decoration.MAHOGANY_EAGLE_LECTERN), SOFT_CLAY, Item(Items.LAW_RUNE_563), Item(Items.AIR_RUNE_556, 5)),
        ENCHANT_DIAMOND(6, 57, 67.0, Item(Items.ENCHANT_DIAMOND_8019), arrayOf(Decoration.TEAK_DEMON_LECTERN, Decoration.MAHOGANY_DEMON_LECTERN), SOFT_CLAY, Item(Items.COSMIC_RUNE_564), Item(Items.EARTH_RUNE_557, 10)),
        ENCHANT_DRAGONSTONE(7, 68, 78.0,Item(Items.ENCHANT_DRAGONSTN_8020), arrayOf(Decoration.MAHOGANY_DEMON_LECTERN), SOFT_CLAY, Item(Items.COSMIC_RUNE_564), Item(Items.EARTH_RUNE_557, 15), Item(Items.WATER_RUNE_555, 15)),
        ENCHANT_EMERALD(8, 27, 37.0, Item(Items.ENCHANT_EMERALD_8017), arrayOf(Decoration.DEMON_LECTERN, Decoration.TEAK_DEMON_LECTERN, Decoration.MAHOGANY_DEMON_LECTERN), SOFT_CLAY, Item(Items.COSMIC_RUNE_564), Item(Items.AIR_RUNE_556, 3)),
        ENCHANT_ONYX(9, 87, 97.0, Item(Items.ENCHANT_ONYX_8021), arrayOf(Decoration.MAHOGANY_DEMON_LECTERN), SOFT_CLAY, Item(Items.COSMIC_RUNE_564), Item(Items.EARTH_RUNE_557, 20), Item(Items.FIRE_RUNE_554, 20)),
        ENCHANT_RUBY(10, 49, 59.0, Item(Items.ENCHANT_RUBY_8018), arrayOf(Decoration.TEAK_DEMON_LECTERN, Decoration.MAHOGANY_DEMON_LECTERN), SOFT_CLAY, Item(Items.COSMIC_RUNE_564), Item(Items.FIRE_RUNE_554, 5)),
        ENCHANT_SAPPHIRE(11, 7, 17.5, Item(Items.ENCHANT_SAPPHIRE_8016), arrayOf(Decoration.OAK_LECTERN, Decoration.EAGLE_LECTERN, Decoration.TEAK_EAGLE_LECTERN, Decoration.MAHOGANY_EAGLE_LECTERN, Decoration.DEMON_LECTERN, Decoration.TEAK_DEMON_LECTERN, Decoration.MAHOGANY_DEMON_LECTERN), SOFT_CLAY, Item(Items.COSMIC_RUNE_564), Item(Items.WATER_RUNE_555)),
        FALADOR(12, 37, 48.0, Item(TeleportTablet.FALADOR_TELEPORT.item), arrayOf(Decoration.EAGLE_LECTERN, Decoration.TEAK_EAGLE_LECTERN, Decoration.MAHOGANY_EAGLE_LECTERN), SOFT_CLAY, Item(Items.LAW_RUNE_563), Item(Items.WATER_RUNE_555), Item(Items.AIR_RUNE_556, 3)),
        LUMBRIDGE(13, 31, 41.0, Item(TeleportTablet.LUMBRIDGE_TELEPORT.item), arrayOf(Decoration.EAGLE_LECTERN, Decoration.TEAK_EAGLE_LECTERN, Decoration.MAHOGANY_EAGLE_LECTERN), SOFT_CLAY, Item(Items.LAW_RUNE_563), Item(Items.EARTH_RUNE_557), Item(Items.AIR_RUNE_556, 3)),
        HOUSE(14, 40, 30.0, Item(Items.TP_TO_HOUSE_8013), arrayOf(Decoration.MAHOGANY_EAGLE_LECTERN), SOFT_CLAY, Item(Items.LAW_RUNE_563), Item(Items.EARTH_RUNE_557), Item(Items.AIR_RUNE_556)),
        VARROCK(15, 25, 35.0, Item(TeleportTablet.VARROCK_TELEPORT.item), arrayOf(Decoration.OAK_LECTERN, Decoration.EAGLE_LECTERN, Decoration.TEAK_EAGLE_LECTERN, Decoration.MAHOGANY_EAGLE_LECTERN, Decoration.DEMON_LECTERN, Decoration.TEAK_DEMON_LECTERN, Decoration.MAHOGANY_DEMON_LECTERN), SOFT_CLAY, Item(Items.LAW_RUNE_563), Item(Items.FIRE_RUNE_554), Item(Items.AIR_RUNE_556, 3)),
        WATCHTOWER(16, 58, 68.0, Item(TeleportTablet.WATCH_TOWER_TELEPORT.item), arrayOf(Decoration.MAHOGANY_EAGLE_LECTERN), SOFT_CLAY, Item(Items.LAW_RUNE_563, 2), Item(Items.EARTH_RUNE_557, 2));

        val requiredItemsList = requiredItems.toList()

        /**
         * Checks that the player meets all requirements for crafting the tablet.
         */
        fun checkRequirements(player: Player): Boolean {
            val objectId = player.getAttribute<Int>(GameAttributes.CON_LECTERN_OBJECT) ?: 0

            if (
                player.spellBookManager.spellBook == 192 &&
                getStatLevel(player, Skills.MAGIC) < requiredLevel
            ) {
                sendMessage(player, "You need a Magic level of $requiredLevel to make that.")
                return false
            }

            if (this == BONES_TO_PEACHES && !player.savedData.activityData.isBonesToPeaches) {
                sendMessage(
                    player,
                    "You need the Bones to Peaches ability from Mage Training Arena first."
                )
                return false
            }

            if (requiredDecorations.none { it.objectId == objectId }) {
                sendMessage(player, "You're unable to make this tablet on this lectern.")
                return false
            }

            if (!inInventory(player, SOFT_CLAY.id)) {
                sendMessage(player, "You need a piece of soft clay in order to make a tablet.")
                return false
            }

            val missingItems =
                requiredItemsList.filter { item ->
                    val staff = MagicStaff.forId(item.id)
                    val hasStaff = staff != null && anyInEquipment(player, *staff.staves)
                    !hasStaff && !inInventory(player, item.id)
                }

            if (missingItems.isNotEmpty()) {
                val names = missingItems.joinToString(", ") { it.definition!!.name.lowercase() }
                sendMessage(
                    player,
                    "You need $names to make ${tabItem.definition!!.name.lowercase()}."
                )
                return false
            }

            return true
        }

        companion object {
            private val byId = values().associateBy { it.buttonId }
            /**
             * Gets the matching [TeleportTabButton] definition for a button id.
             */
            fun forId(id: Int) = byId[id]
        }
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        (Scenery.LECTERN_13642..Scenery.LECTERN_13648).forEach { id ->
            SceneryDefinition.forId(id).handlers["option:study"] = this
        }
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val id = node.asScenery().id
        setAttribute(player, GameAttributes.CON_LECTERN_OBJECT, id)

        player.lock(3)
        player.animator.animate(Animation(1894))
        queueScript(player, 2, QueueStrength.SOFT) {
            openInterface(player, Components.POH_MAGIC_TABLETS_400)
            return@queueScript stopExecuting(player)
        }
        return true
    }

    /**
     * Handles the magic tablet interface.
     */
    class MagicTabletInterface : InterfaceListener {

        private val decorationVars =
            mapOf(
                Decoration.OAK_LECTERN to (0 to 0),
                Decoration.EAGLE_LECTERN to (1 to 0),
                Decoration.DEMON_LECTERN to (0 to 1),
                Decoration.TEAK_EAGLE_LECTERN to (2 to 0),
                Decoration.TEAK_DEMON_LECTERN to (0 to 2),
                Decoration.MAHOGANY_EAGLE_LECTERN to (3 to 0),
                Decoration.MAHOGANY_DEMON_LECTERN to (0 to 3)
            )

        override fun defineInterfaceListeners() {

            onOpen(Components.POH_MAGIC_TABLETS_400) { player, _ ->
                val deco =
                    Decoration.forObjectId(
                        getAttribute(player, GameAttributes.CON_LECTERN_OBJECT, 0)
                    )
                val (v1, v2) = decorationVars[deco] ?: (0 to 0)
                setVarp(player, 261, v1)
                setVarp(player, 262, v2)
                return@onOpen true
            }

            on(Components.POH_MAGIC_TABLETS_400) { player, _, _, buttonID, _, _ ->
                val ttb = TeleportTabButton.forId(buttonID) ?: return@on true

                if (!ttb.checkRequirements(player)) {
                    resetAnimator(player)
                    return@on true
                }

                closeInterface(player)

                val required =
                    ttb.requiredItemsList.filterNot { item ->
                        MagicStaff.forId(item.id)?.let { staff ->
                            anyInEquipment(player, *staff.staves)
                        } ?: false
                    }

                queueScript(player, 1, QueueStrength.NORMAL) { stage ->
                    if (!ttb.checkRequirements(player)) return@queueScript stopExecuting(player)
                    if (!removeItems(player, required)) return@queueScript stopExecuting(player)

                    when (stage) {
                        0 -> {
                            val hasStaff = MagicStaff.values().any { staff -> anyInEquipment(player, *staff.staves) }
                            val animationId = if (hasStaff) 4068 else 4067
                            player.animate(Animation(animationId))
                            return@queueScript delayScript(player, 3)
                        }
                        1 -> {
                            rewardXP(player, Skills.MAGIC, ttb.xp)
                            addItemOrDrop(player, ttb.tabItem.id)
                            val obj = getAttribute(player, GameAttributes.CON_LECTERN_OBJECT, 0)
                            if (
                                ttb == TeleportTabButton.VARROCK &&
                                obj in
                                setOf(
                                    Decoration.MAHOGANY_EAGLE_LECTERN.objectId,
                                    Decoration.MAHOGANY_DEMON_LECTERN.objectId
                                )
                            ) {
                                finishDiaryTask(player, DiaryType.VARROCK, 2, 8)
                            }
                            return@queueScript stopExecuting(player)
                        }
                        else -> return@queueScript stopExecuting(player)
                    }
                }

                return@on true
            }
        }

        private fun removeItems(player: Player, items: List<Item>): Boolean {
            for (item in items) {
                if (!removeItem(player, item)) {
                    sendMessage(
                        player,
                        "You do not have ${item.definition?.name?.lowercase()} to make the tablet."
                    )
                    return false
                }
            }
            return true
        }
    }

    companion object {
        private val SOFT_CLAY = Item(Items.SOFT_CLAY_1761)
    }
}