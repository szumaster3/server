package content.global.handlers.item.book.guide

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.api.inInventory
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.GameWorld.settings
import org.rs.consts.Items

class ConstructionGuide : InteractionListener {
    companion object {
        private const val TITLE = "Construction guide book"

        private val CON_ITEMS =
            arrayOf(
                Item(Items.SAW_8794, 1),
                Item(Items.HAMMER_2347, 1),
                Item(Items.BRONZE_NAILS_4819, 5000),
                Item(Items.IRON_NAILS_4820, 5000),
                Item(Items.STEEL_NAILS_1539, 5000),
                Item(Items.BLACK_NAILS_4821, 5000),
                Item(Items.MITHRIL_NAILS_4822, 5000),
                Item(Items.ADAMANTITE_NAILS_4823, 5000),
                Item(Items.RUNE_NAILS_4824, 5000),
                Item(Items.PLANK_961, 5000),
                Item(Items.OAK_PLANK_8779, 5000),
                Item(Items.TEAK_PLANK_8781, 5000),
                Item(Items.MAHOGANY_PLANK_8783, 5000),
                Item(Items.BOLT_OF_CLOTH_8791, 5000),
                Item(Items.GOLD_LEAF_8785, 5000),
                Item(Items.MARBLE_BLOCK_8787, 5000),
                Item(Items.MAGIC_STONE_8789, 5000),
                Item(Items.BAGGED_DEAD_TREE_8418, 5000),
                Item(Items.BAGGED_NICE_TREE_8420, 5000),
                Item(Items.BAGGED_OAK_TREE_8422, 5000),
                Item(Items.BAGGED_WILLOW_TREE_8424, 5000),
                Item(Items.BAGGED_MAPLE_TREE_8426, 5000),
                Item(Items.BAGGED_YEW_TREE_8428, 5000),
                Item(Items.BAGGED_MAGIC_TREE_8430, 5000),
                Item(Items.BAGGED_PLANT_1_8432, 5000),
                Item(Items.BAGGED_PLANT_2_8434, 5000),
                Item(Items.BAGGED_PLANT_3_8436, 5000),
            )

        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("How to build in your house", 55),
                        BookLine("", 56),
                        BookLine("In order to build you will", 57),
                        BookLine("need to turn building mode", 58),
                        BookLine("on. This can be done on", 59),
                        BookLine("entering the house or using", 60),
                        BookLine("a button on the options", 61),
                        BookLine("interface. If you have a", 62),
                        BookLine("bank PIN you must enter", 63),
                        BookLine("it when entering building", 64),
                        BookLine("mode.", 65),
                    ),
                    Page(
                        BookLine("In building mode the ghostly", 66),
                        BookLine("shapes of furniture and", 67),
                        BookLine("doorways you have not", 68),
                        BookLine("built yet will appear in", 69),
                        BookLine("your house. These are called", 70),
                        BookLine("hotspots. You can use these", 71),
                        BookLine("to build furniture and new", 72),
                        BookLine("rooms. To build a piece", 73),
                        BookLine("of furniture, right-click", 74),
                        BookLine("the hotspot and select Build.", 75),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("You will then be able to", 56),
                        BookLine("select the piece of furniture", 57),
                        BookLine("you want to build from the", 58),
                        BookLine("menu. Below each furniture", 59),
                        BookLine("icon is a list of materials;", 60),
                        BookLine("to build the furniture you", 61),
                        BookLine("will need to have all these", 62),
                        BookLine("materials in your inventory.", 63),
                        BookLine("You will also need to have", 64),
                        BookLine("the correct Construction", 65),
                    ),
                    Page(
                        BookLine("level. Nails work slightly", 66),
                        BookLine("differently to other", 67),
                        BookLine("materials.", 68),
                        BookLine("", 69),
                        BookLine("You will sometimes find you", 70),
                        BookLine("break nails, especially if", 71),
                        BookLine("you have a low Construction", 72),
                        BookLine("level, so you may need to", 73),
                        BookLine("bring more nails than the", 74),
                        BookLine("furniture requires. Nails", 75),
                        BookLine("made of stronger metals", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("will break less often.", 55),
                        BookLine("You can remove a piece of", 56),
                        BookLine("furniture if you wish to", 57),
                        BookLine("build something else in the", 58),
                        BookLine("same space.", 59),
                        BookLine("", 60),
                        BookLine("To do this, right-click", 61),
                        BookLine("on it and select Remove.", 62),
                        BookLine("You will not get any of", 63),
                        BookLine("the materials back. Some", 64),
                        BookLine("pieces of furniture can", 65),
                    ),
                    Page(
                        BookLine("be upgraded to better pieces", 66),
                        BookLine("of furniture without having", 67),
                        BookLine("to remove them first. To", 68),
                        BookLine("build a new room, you must", 69),
                        BookLine("use one of the door hotspots", 70),
                        BookLine("at the edges of rooms or", 71),
                        BookLine("garden squares. Right-click", 72),
                        BookLine("on it and select Build.", 73),
                        BookLine("", 74),
                        BookLine("This will bring up a list", 75),
                        BookLine("of rooms. Different rooms", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("cost different amounts of", 55),
                        BookLine("gold and have different", 56),
                        BookLine("Construction level", 57),
                        BookLine("requirements.", 58),
                        BookLine("", 59),
                        BookLine("If you select Build on a", 60),
                        BookLine("door hotspot that already", 61),
                        BookLine("leads to a room, you will", 62),
                        BookLine("be asked whether you want", 63),
                        BookLine("to delete that room.", 64),
                    ),
                    Page(
                        BookLine("The main raw material you", 66),
                        BookLine("will use to make furniture", 67),
                        BookLine("is planks.", 68),
                        BookLine("", 69),
                        BookLine("The sawmill operator north", 70),
                        BookLine("east of Varrock will", 71),
                        BookLine("turn logs into planks for", 72),
                        BookLine("you, for a fee. The useful", 73),
                        BookLine("planks are wood, oak, teak", 74),
                        BookLine("and mahogany. The sawmill", 75),
                        BookLine("operator also sells saws,", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("cloth and nails. For higher", 55),
                        BookLine("level furniture you may", 56),
                        BookLine("also need limestone, marble,", 57),
                        BookLine("gold leaf and magic building", 58),
                        BookLine("crystals. These are sold", 59),
                        BookLine("by the stonemason who lives", 60),
                        BookLine("in Keldagrim. Some pieces", 61),
                        BookLine("of furniture also require", 62),
                        BookLine("materials that are", 63),
                        BookLine("not specific to", 64),
                        BookLine("construction, such as", 65),
                    ),
                    Page(
                        BookLine("steel bars and soft clay.", 66),
                        BookLine("", 67),
                        BookLine("Room types", 68),
                        BookLine("", 69),
                        BookLine("Parlour: This is the", 70),
                        BookLine("lowest-level room and", 71),
                        BookLine("provides space for three", 72),
                        BookLine("people to sit around", 73),
                        BookLine("a fire.", 74),
                        BookLine("", 75),
                        BookLine("Garden:", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("The garden is largely", 55),
                        BookLine("decorative but it also", 56),
                        BookLine("contains the exit portal.", 57),
                        BookLine("", 58),
                        BookLine("Kitchen: This room can", 59),
                        BookLine("be used for preparing", 60),
                        BookLine("food. As you build better", 61),
                        BookLine("furniture in it you will", 62),
                        BookLine("find yourself able to", 63),
                        BookLine("prepare better meals", 64),
                        BookLine("in it.", 65),
                    ),
                    Page(
                        BookLine("Dining room: Eight", 67),
                        BookLine("people can sit around", 68),
                        BookLine("the tables you build", 69),
                        BookLine("in this room.", 70),
                        BookLine("", 71),
                        BookLine("Bedroom: Some of the", 72),
                        BookLine("furniture in this room", 73),
                        BookLine("can be used to change", 74),
                        BookLine("your hair and clothes.", 75),
                        BookLine("You will also need to", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("have two of these rooms", 55),
                        BookLine("in order to hire a", 56),
                        BookLine("servant.", 57),
                        BookLine("", 58),
                        BookLine("Halls: These are", 59),
                        BookLine("primarily used to", 60),
                        BookLine("connect other rooms,", 61),
                        BookLine("but they also provide", 62),
                        BookLine("space to show off the", 63),
                        BookLine("owner's skill and quest", 64),
                        BookLine("achievements.", 65),
                    ),
                    Page(
                        BookLine("Games room: Various", 67),
                        BookLine("games can be built in", 68),
                        BookLine("this room to allow", 69),
                        BookLine("friends to play and", 70),
                        BookLine("train together.", 71),
                        BookLine("", 72),
                        BookLine("Combat Room: With this", 73),
                        BookLine("room you can challenge", 74),
                        BookLine("your friends in a", 75),
                        BookLine("personal duelling ring.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("", 55),
                        BookLine("Workshop: This room", 56),
                        BookLine("allows you to train", 57),
                        BookLine("Construction without", 58),
                        BookLine("modifying your own house,", 59),
                        BookLine("by making furniture that", 60),
                        BookLine("can be sold to other", 61),
                        BookLine("players. It also", 62),
                        BookLine("provides space for you", 63),
                        BookLine("to train Crafting and", 64),
                        BookLine("Smithing.", 65),
                    ),
                    Page(
                        BookLine("Chapel: This room can", 67),
                        BookLine("be dedicated to any of", 68),
                        BookLine("GameWorld.settings.name", 69),
                        BookLine("major gods, and the", 70),
                        BookLine("altar can be used to", 71),
                        BookLine("offer bones.", 72),
                        BookLine("", 73),
                        BookLine("Menagerie: You can keep", 74),
                        BookLine("your pets in this room.", 75),
                        BookLine("", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Study: You can use the", 55),
                        BookLine("lectern in this room to", 56),
                        BookLine("create clay tablets", 57),
                        BookLine("recording magic spells.", 58),
                        BookLine("Eagle lecterns make", 59),
                        BookLine("teleport spells and", 60),
                        BookLine("demon lecterns make", 61),
                        BookLine("enchantment spells.", 62),
                        BookLine("The elemental sphere in", 63),
                        BookLine("this room can be used", 64),
                        BookLine("to change the element", 65),
                        BookLine("of an elemental staff.", 66),
                    ),
                    Page(
                        BookLine("Portal chamber: In this", 68),
                        BookLine("room you can build", 69),
                        BookLine("portals to various places", 70),
                        BookLine("around the world.", 71),
                        BookLine("", 72),
                        BookLine("Formal garden: The formal", 73),
                        BookLine("garden can contain various", 74),
                        BookLine("plants and ornaments to", 75),
                        BookLine("beautify the grounds", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("of your house.", 55),
                        BookLine("", 56),
                        BookLine("Throne Room: This room", 57),
                        BookLine("can be used to hold", 58),
                        BookLine("audiences with large", 59),
                        BookLine("numbers of friends.", 60),
                        BookLine("It also contains the", 61),
                        BookLine("lever that turns on", 62),
                        BookLine("challenge mode.", 63),
                        BookLine("", 64),
                        BookLine("Oubliette: If you build", 65),
                    ),
                    Page(
                        BookLine("an oubliette below your", 66),
                        BookLine("throne room you can drop", 67),
                        BookLine("people from there into a", 68),
                        BookLine("cage which you can fill", 69),
                        BookLine("with various horrors.", 70),
                        BookLine("", 71),
                        BookLine("Dungeon: Dungeon corridors", 72),
                        BookLine("and junctions can be built", 73),
                        BookLine("to create an underground", 74),
                        BookLine("maze full of monsters,", 75),
                        BookLine("traps and doors.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("", 55),
                        BookLine("Treasure room: You can", 56),
                        BookLine("place a prize in this", 57),
                        BookLine("room for visitors to", 58),
                        BookLine("your dungeon to try", 59),
                        BookLine("to reach.", 60),
                        BookLine("", 61),
                        BookLine("Servants", 62),
                        BookLine("", 63),
                        BookLine("Once you have two", 64),
                        BookLine("bedrooms, you can hire", 65),
                    ),
                    Page(
                        BookLine("a servant by going to", 66),
                        BookLine("the Servants' Guild in", 67),
                        BookLine("Ardougne.", 68),
                        BookLine("", 69),
                        BookLine("You will have to pay", 70),
                        BookLine("when you hire them,", 71),
                        BookLine("and the servant will then", 72),
                        BookLine("periodically demand wages.", 73),
                        BookLine("", 74),
                        BookLine("Servants can take items", 75),
                        BookLine("to and from the bank for", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("you, and can also greet", 55),
                        BookLine("guests and serve food", 56),
                        BookLine("and drinks.", 57),
                    ),
                ),
            )
    }

    @Suppress("UNUSED_PARAMETER")
    private fun display(
        player: Player,
        pageNum: Int,
        buttonID: Int,
    ): Boolean {
        BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
        return true
    }

    override fun defineListeners() {
        on(Items.CONSTRUCTION_GUIDE_8463, IntType.ITEM, "read") { player, _ ->
            if (settings!!.isDevMode && settings!!.isBeta) {
                for (item in CON_ITEMS) {
                    if (!inInventory(player, item.id, item.amount)) {
                        player.inventory.add(item, player)
                    }
                }
            } else {
                BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            }
            return@on true
        }
    }
}
