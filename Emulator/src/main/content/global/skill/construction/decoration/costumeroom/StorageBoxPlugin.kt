package content.global.skill.construction.decoration.costumeroom

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Scenery as Obj

@Initializable
class StorageBoxPlugin : OptionHandler() {
    private val bookcase = intArrayOf(Obj.BOOKCASE_13597, Obj.BOOKCASE_13598, Obj.BOOKCASE_13599)
    private val capeRack = intArrayOf(Obj.OAK_CAPE_RACK_18766, Obj.TEAK_CAPE_RACK_18767, Obj.MAHOGANY_CAPE_RACK_18768, Obj.GILDED_CAPE_RACK_18769, Obj.MARBLE_CAPE_RACK_18770, Obj.MAGIC_CAPE_RACK_18771)
    private val fancyDressClosed = intArrayOf(Obj.FANCY_DRESS_BOX_18772, Obj.FANCY_DRESS_BOX_18774, Obj.FANCY_DRESS_BOX_18776)
    private val fancyDressOpened = intArrayOf(Obj.FANCY_DRESS_BOX_18773, Obj.FANCY_DRESS_BOX_18775, Obj.FANCY_DRESS_BOX_18777)
    private val toyBoxClosed = intArrayOf(Obj.TOY_BOX_18798, Obj.TOY_BOX_18800, Obj.TOY_BOX_18802)
    private val toyBoxOpened = intArrayOf(Obj.TOY_BOX_18799, Obj.TOY_BOX_18801, Obj.TOY_BOX_18803)
    private val treasureBoxClosed = intArrayOf(Obj.TREASURE_CHEST_18804, Obj.TREASURE_CHEST_18806, Obj.TREASURE_CHEST_18808)
    private val treasureBoxOpened = intArrayOf(Obj.TREASURE_CHEST_18805, Obj.TREASURE_CHEST_18807, Obj.TREASURE_CHEST_18809)
    private val magicWardrobeClosed = intArrayOf(Obj.MAGIC_WARDROBE_18784, Obj.MAGIC_WARDROBE_18786, Obj.MAGIC_WARDROBE_18788, Obj.MAGIC_WARDROBE_18790, Obj.MAGIC_WARDROBE_18792, Obj.MAGIC_WARDROBE_18794, Obj.MAGIC_WARDROBE_18796)
    private val magicWardrobeOpened = intArrayOf(Obj.MAGIC_WARDROBE_18785, Obj.MAGIC_WARDROBE_18787, Obj.MAGIC_WARDROBE_18789, Obj.MAGIC_WARDROBE_18791, Obj.MAGIC_WARDROBE_18793, Obj.MAGIC_WARDROBE_18795, Obj.MAGIC_WARDROBE_18797)
    private val armourCaseClosed = intArrayOf(Obj.ARMOUR_CASE_18778,Obj.ARMOUR_CASE_18780,Obj.ARMOUR_CASE_18782)
    private val armourCaseOpened = intArrayOf(Obj.ARMOUR_CASE_18779,Obj.ARMOUR_CASE_18781,Obj.ARMOUR_CASE_18783)

    override fun newInstance(arg: Any?): Plugin<Any> {
        for (id in bookcase) {
            SceneryDefinition.forId(id).handlers["option:search"] = this
        }
        for (id in capeRack) {
            SceneryDefinition.forId(id).handlers["option:search"] = this
        }
        for (id in fancyDressClosed) {
            SceneryDefinition.forId(id).handlers["option:open"] = this
        }
        for (id in fancyDressOpened) {
            SceneryDefinition.forId(id).handlers["option:search"] = this
            SceneryDefinition.forId(id).handlers["option:close"] = this
        }
        for (id in toyBoxClosed) {
            SceneryDefinition.forId(id).handlers["option:open"] = this
        }
        for (id in toyBoxOpened) {
            SceneryDefinition.forId(id).handlers["option:search"] = this
            SceneryDefinition.forId(id).handlers["option:close"] = this
        }
        for (id in treasureBoxClosed) {
            SceneryDefinition.forId(id).handlers["option:open"] = this
        }
        for (id in treasureBoxOpened) {
            SceneryDefinition.forId(id).handlers["option:search"] = this
            SceneryDefinition.forId(id).handlers["option:close"] = this
        }
        for (id in armourCaseClosed) {
            SceneryDefinition.forId(id).handlers["option:open"] = this
        }
        for (id in armourCaseOpened) {
            SceneryDefinition.forId(id).handlers["option:search"] = this
            SceneryDefinition.forId(id).handlers["option:close"] = this
        }
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val obj = node as Scenery

        when (obj.id) {
            in bookcase -> {
                StorageBoxInterface.openStorage(player, CostumeRoomStorage.Type.BOOK)
            }

            in capeRack -> {
                StorageBoxInterface.openStorage(player, CostumeRoomStorage.Type.CAPE)
            }

            in fancyDressClosed + toyBoxClosed + treasureBoxClosed + magicWardrobeClosed -> {
                if (option == "open") {
                    animate(player, Animations.OPEN_CHEST_536)
                    replaceScenery(obj.asScenery(), obj.id + 1, -1)
                }
            }

            in fancyDressOpened -> {
                when (option) {
                    "close" -> {
                        animate(player, Animations.HUMAN_CLOSE_CHEST_538)
                        replaceScenery(obj.asScenery(), obj.id - 1, -1)
                    }
                    "search" -> {
                        StorageBoxInterface.openStorage(player, CostumeRoomStorage.Type.FANCY)
                    }
                }
            }

            in toyBoxOpened -> {
                when (option) {
                    "close" -> {
                        animate(player, Animations.HUMAN_CLOSE_CHEST_538)
                        replaceScenery(obj.asScenery(), obj.id - 1, -1)
                    }
                    "search" -> {
                        StorageBoxInterface.openStorage(player, CostumeRoomStorage.Type.TOY)
                    }
                }
            }

            in treasureBoxOpened -> {
                when (option) {
                    "close" -> {
                        animate(player, Animations.HUMAN_CLOSE_CHEST_538)
                        replaceScenery(obj.asScenery(), obj.id - 1, -1)
                    }
                    "search" -> when(obj.id){
                        Obj.TREASURE_CHEST_18805 -> StorageBoxInterface.openStorage(player, CostumeRoomStorage.Type.LOW_LEVEL_TRAILS)
                        Obj.TREASURE_CHEST_18807 -> StorageBoxInterface.openStorage(player, CostumeRoomStorage.Type.MED_LEVEL_TRAILS)
                        Obj.TREASURE_CHEST_18809 -> StorageBoxInterface.openStorage(player, CostumeRoomStorage.Type.HIGH_LEVEL_TRAILS)
                    }
                }
            }

            in magicWardrobeOpened -> {
                when (option) {
                    "close" -> {
                        animate(player, Animations.HUMAN_CLOSE_CHEST_538)
                        replaceScenery(obj.asScenery(), obj.id - 1, -1)
                    }
                    "search" -> when(obj.id){
                        Obj.MAGIC_WARDROBE_18785 -> StorageBoxInterface.openStorage(player, CostumeRoomStorage.Type.ONE_SET_OF_ARMOUR)
                        Obj.MAGIC_WARDROBE_18787 -> StorageBoxInterface.openStorage(player, CostumeRoomStorage.Type.TWO_SETS_OF_ARMOUR)
                        Obj.MAGIC_WARDROBE_18789 -> StorageBoxInterface.openStorage(player, CostumeRoomStorage.Type.THREE_SETS_OF_ARMOUR)
                        Obj.MAGIC_WARDROBE_18791 -> StorageBoxInterface.openStorage(player, CostumeRoomStorage.Type.FOUR_SETS_OF_ARMOUR)
                        Obj.MAGIC_WARDROBE_18793 -> StorageBoxInterface.openStorage(player, CostumeRoomStorage.Type.FIVE_SETS_OF_ARMOUR)
                        Obj.MAGIC_WARDROBE_18795 -> StorageBoxInterface.openStorage(player, CostumeRoomStorage.Type.SIX_SETS_OF_ARMOUR)
                        Obj.MAGIC_WARDROBE_18797 -> StorageBoxInterface.openStorage(player, CostumeRoomStorage.Type.ALL_SETS_OF_ARMOUR)
                    }
                }
            }

            in armourCaseOpened -> {
                when (option) {
                    "close" -> {
                        animate(player, Animations.HUMAN_CLOSE_CHEST_538)
                        replaceScenery(obj.asScenery(), obj.id - 1, -1)
                    }
                    "search" -> when(obj.id){
                        Obj.ARMOUR_CASE_18779 -> StorageBoxInterface.openStorage(player, CostumeRoomStorage.Type.TWO_SETS_ARMOUR_CASE)
                        Obj.ARMOUR_CASE_18781 -> StorageBoxInterface.openStorage(player, CostumeRoomStorage.Type.FOUR_SETS_ARMOUR_CASE)
                        Obj.ARMOUR_CASE_18783 -> StorageBoxInterface.openStorage(player, CostumeRoomStorage.Type.ALL_SETS_ARMOUR_CASE)
                    }
                }
            }
        }

        return true
    }
}