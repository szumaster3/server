package content.global.skill.construction.decoration.costumeroom

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Animations
import shared.consts.Sounds
import shared.consts.Scenery as Obj

/**
 * Handles interactions with storage objects.
 */
@Initializable
class StorageBoxOptionPlugin : OptionHandler() {

    private enum class StorageBox(val objectIds: IntArray, val storableType: StorableType? = null, val openable: Boolean = false, val closable: Boolean = false) {
        BOOKCASE(intArrayOf(Obj.BOOKCASE_13597, Obj.BOOKCASE_13598, Obj.BOOKCASE_13599), StorableType.BOOK),
        CAPE_RACK(intArrayOf(Obj.OAK_CAPE_RACK_18766, Obj.TEAK_CAPE_RACK_18767, Obj.MAHOGANY_CAPE_RACK_18768, Obj.GILDED_CAPE_RACK_18769, Obj.MARBLE_CAPE_RACK_18770, Obj.MAGIC_CAPE_RACK_18771), StorableType.CAPE),
        FANCY_BOX_OPEN(intArrayOf(Obj.FANCY_DRESS_BOX_18772, Obj.FANCY_DRESS_BOX_18774, Obj.FANCY_DRESS_BOX_18776), openable = true),
        FANCY_BOX_CLOSE(intArrayOf(Obj.FANCY_DRESS_BOX_18773, Obj.FANCY_DRESS_BOX_18775, Obj.FANCY_DRESS_BOX_18777), closable = true, storableType = StorableType.FANCY),
        TOY_BOX_OPEN(intArrayOf(Obj.TOY_BOX_18798, Obj.TOY_BOX_18800, Obj.TOY_BOX_18802), openable = true),
        TOY_BOX_CLOSE(intArrayOf(Obj.TOY_BOX_18799, Obj.TOY_BOX_18801, Obj.TOY_BOX_18803), closable = true, storableType = StorableType.TOY),
        TREASURE_CHEST(intArrayOf(Obj.TREASURE_CHEST_18804, Obj.TREASURE_CHEST_18805, Obj.TREASURE_CHEST_18806, Obj.TREASURE_CHEST_18807, Obj.TREASURE_CHEST_18808, Obj.TREASURE_CHEST_18809), storableType = StorableType.TRAILS, openable = true),
        MAGIC_WARDROBE(intArrayOf(Obj.MAGIC_WARDROBE_18784, Obj.MAGIC_WARDROBE_18785, Obj.MAGIC_WARDROBE_18786, Obj.MAGIC_WARDROBE_18787, Obj.MAGIC_WARDROBE_18788, Obj.MAGIC_WARDROBE_18789, Obj.MAGIC_WARDROBE_18790, Obj.MAGIC_WARDROBE_18791, Obj.MAGIC_WARDROBE_18792, Obj.MAGIC_WARDROBE_18793, Obj.MAGIC_WARDROBE_18794, Obj.MAGIC_WARDROBE_18795, Obj.MAGIC_WARDROBE_18796, Obj.MAGIC_WARDROBE_18797), storableType = StorableType.ARMOUR, openable = true),
        ARMOUR_CASE(intArrayOf(Obj.ARMOUR_CASE_18778, Obj.ARMOUR_CASE_18779, Obj.ARMOUR_CASE_18780, Obj.ARMOUR_CASE_18781, Obj.ARMOUR_CASE_18782, Obj.ARMOUR_CASE_18783), storableType = StorableType.ARMOUR_CASE, openable = true)
    }

    private val allBoxes = StorageBox.values()

    override fun newInstance(arg: Any?): Plugin<Any> {
        allBoxes.forEach { box ->
            box.objectIds.forEach { id ->
                SceneryDefinition.forId(id)?.let { def ->
                    if (box.storableType != null) def.handlers["option:search"] = this
                    if (box.openable) def.handlers["option:open"] = this
                    if (box.closable) def.handlers["option:close"] = this
                }
            }
        }
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val obj = node as Scenery
        val box = allBoxes.firstOrNull { obj.id in it.objectIds } ?: return true

        when (option) {
            "search" -> {
                when (obj.id) {
                    Obj.TREASURE_CHEST_18807 -> {
                        setTitle(player, 2)
                        sendOptions(
                            player,
                            "Take which level of Treasure Trail reward?",
                            "Level 1",
                            "Level 2"
                        )
                        addDialogueAction(player) { p, button ->
                            val tier = when (button) {
                                2 -> 0  // Low-level
                                3 -> 1  // Medium-level
                                else -> null
                            }
                            tier?.let {
                                val container = p.getCostumeRoomState().getContainer(StorableType.TRAILS)
                                container.setTier(StorableType.TRAILS, it)
                                StorageBoxInterface.openStorage(p, StorableType.TRAILS)
                            }
                            return@addDialogueAction
                        }
                    }

                    Obj.TREASURE_CHEST_18809 -> {
                        setTitle(player, 3)
                        sendOptions(
                            player,
                            "Take which level of Treasure Trail reward?",
                            "Level 1",
                            "Level 2",
                            "Level 3"
                        )
                        addDialogueAction(player) { p, button ->
                            val tier = when (button) {
                                2 -> 0  // Low-level
                                3 -> 1  // Medium-level
                                4 -> 2  // High-level
                                else -> null
                            }
                            tier?.let {
                                val container = p.getCostumeRoomState().getContainer(StorableType.TRAILS)
                                container.setTier(StorableType.TRAILS, it)
                                StorageBoxInterface.openStorage(p, StorableType.TRAILS)
                            }
                            return@addDialogueAction
                        }
                    }

                    else -> {
                        box.storableType?.let { StorageBoxInterface.openStorage(player, it) }
                    }
                }
            }

            "open" -> if (box.openable) openBox(player, obj)
            "close" -> if (box.closable) closeBox(player, obj)
        }
        return true
    }

    private fun openBox(player: Player, obj: Scenery) {
        playAudio(player, Sounds.CHEST_OPEN_52)
        animate(player, Animations.HUMAN_OPEN_CHEST_536)
        replaceScenery(obj, obj.id + 1, -1)
    }

    private fun closeBox(player: Player, obj: Scenery) {
        playAudio(player, Sounds.CHEST_CLOSE_51)
        animate(player, Animations.HUMAN_CLOSE_CHEST_538)
        replaceScenery(obj, obj.id - 1, -1)
    }
}
