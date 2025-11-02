package content.global.skill.construction.decoration.costumeroom

import core.api.animate
import core.api.playAudio
import core.api.replaceScenery
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

    /**
     * Represents all storage boxes & configuration.
     */
    private enum class StorageBox(val objectIds: IntArray, val storableType: StorableType? = null, val openable: Boolean = false, val closable: Boolean = false) {
        // Bookcases
        BOOKCASE(intArrayOf(Obj.BOOKCASE_13597, Obj.BOOKCASE_13598, Obj.BOOKCASE_13599), StorableType.BOOK),
        // Cape racks
        CAPE_RACK(intArrayOf(Obj.OAK_CAPE_RACK_18766, Obj.TEAK_CAPE_RACK_18767, Obj.MAHOGANY_CAPE_RACK_18768, Obj.GILDED_CAPE_RACK_18769, Obj.MARBLE_CAPE_RACK_18770, Obj.MAGIC_CAPE_RACK_18771), StorableType.CAPE),
        // Fancy Dress Boxes
        FANCY_BOX_OPEN(intArrayOf(Obj.FANCY_DRESS_BOX_18772, Obj.FANCY_DRESS_BOX_18774, Obj.FANCY_DRESS_BOX_18776), openable = true),
        FANCY_BOX_CLOSE(intArrayOf(Obj.FANCY_DRESS_BOX_18773, Obj.FANCY_DRESS_BOX_18775, Obj.FANCY_DRESS_BOX_18777), closable = true, storableType = StorableType.FANCY),
        // Toy Boxes
        TOY_BOX_OPEN(intArrayOf(Obj.TOY_BOX_18798, Obj.TOY_BOX_18800, Obj.TOY_BOX_18802), openable = true),
        TOY_BOX_CLOSE(intArrayOf(Obj.TOY_BOX_18799, Obj.TOY_BOX_18801, Obj.TOY_BOX_18803), closable = true, storableType = StorableType.TOY),
        // Treasure Chests
        TREASURE_LOW(intArrayOf(Obj.TREASURE_CHEST_18805), StorableType.LOW_LEVEL_TRAILS),
        TREASURE_MED(intArrayOf(Obj.TREASURE_CHEST_18807), StorableType.MED_LEVEL_TRAILS),
        TREASURE_HIGH(intArrayOf(Obj.TREASURE_CHEST_18809), StorableType.HIGH_LEVEL_TRAILS),
        TREASURE_OPEN(intArrayOf(Obj.TREASURE_CHEST_18804, Obj.TREASURE_CHEST_18806, Obj.TREASURE_CHEST_18808), openable = true),
        // Magic Wardrobes
        WARDROBE_1(intArrayOf(Obj.MAGIC_WARDROBE_18785), StorableType.ONE_SET_OF_ARMOUR),
        WARDROBE_2(intArrayOf(Obj.MAGIC_WARDROBE_18787), StorableType.TWO_SETS_OF_ARMOUR),
        WARDROBE_3(intArrayOf(Obj.MAGIC_WARDROBE_18789), StorableType.THREE_SETS_OF_ARMOUR),
        WARDROBE_4(intArrayOf(Obj.MAGIC_WARDROBE_18791), StorableType.FOUR_SETS_OF_ARMOUR),
        WARDROBE_5(intArrayOf(Obj.MAGIC_WARDROBE_18793), StorableType.FIVE_SETS_OF_ARMOUR),
        WARDROBE_6(intArrayOf(Obj.MAGIC_WARDROBE_18795), StorableType.SIX_SETS_OF_ARMOUR),
        WARDROBE_ALL(intArrayOf(Obj.MAGIC_WARDROBE_18797), StorableType.ALL_SETS_OF_ARMOUR),
        WARDROBE_OPEN(intArrayOf(Obj.MAGIC_WARDROBE_18784, Obj.MAGIC_WARDROBE_18786, Obj.MAGIC_WARDROBE_18788, Obj.MAGIC_WARDROBE_18790, Obj.MAGIC_WARDROBE_18792, Obj.MAGIC_WARDROBE_18794, Obj.MAGIC_WARDROBE_18796), openable = true),
        // Armour Cases
        ARMOUR_2(intArrayOf(Obj.ARMOUR_CASE_18779), StorableType.TWO_SETS_ARMOUR_CASE),
        ARMOUR_4(intArrayOf(Obj.ARMOUR_CASE_18781), StorableType.FOUR_SETS_ARMOUR_CASE),
        ARMOUR_ALL(intArrayOf(Obj.ARMOUR_CASE_18783), StorableType.ALL_SETS_ARMOUR_CASE),
        ARMOUR_OPEN(intArrayOf(Obj.ARMOUR_CASE_18778, Obj.ARMOUR_CASE_18780, Obj.ARMOUR_CASE_18782), openable = true)
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
            "search" -> box.storableType?.let { StorageBoxInterface.openStorage(player, it) }
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
