package content.global.handlers.item.book.guide

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class BrewingGuide : InteractionListener {

    /*
     * It can be found near the mini-game or
     * on a Player-owned house bookshelf.
     */

    companion object {
        private const val TITLE = "Brewin' guide"

        private const val BOWL_OF_WATER_MODEL = 2367
        private const val BUCKET_OF_WATER_MODEL = 2504
        private const val BITTERNUTS_MODEL = 15726
        private const val SWEETGRUBS_MODEL = 2436
        private const val SCRAPEY_TREE_BARK_MODEL = 2625
        private const val LIT_TORCH_MODEL = 2671
        private const val WATER_PUMP_MODEL = 15725
        private const val LUMBER_PATCHES_MODEL = 15785
        private const val BRIDGE_SECTION_MODEL = 15727
        private const val TRICORN_HAT_MODEL = 15743
        private const val NAVAL_T_SHIRT_MODEL = 15735
        private const val NAVAL_PANTS_MODEL = 15736
        private const val THE_STUFF = 15741

        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine("BREWIN' GUIDE", 97),
                    BookLine("", 68),
                    BookLine("A guide to brewin'", 69)
                ),
                Page(
                    BookLine("${core.tools.DARK_RED}Chapters", 82),
                    BookLine("", 83),
                    BookLine("Mos Le'Harmless Rum", 84),
                    BookLine("1:Ingredients", 85),
                    BookLine("1.1: Water and Coloured", 86),
                    BookLine("Water", 87),
                    BookLine("1.2: Bitternuts", 88),
                    BookLine("1.3: Sweetgrubs", 89),
                    BookLine("1.4: Scrapey Tree Bark", 90),
                    BookLine("1.5: Boilers", 91),
                    BookLine("1.6: Brewin'", 92),
                    BookLine("2: Sabotage and Repair", 93),
                    BookLine("2.1: Makin' Repair Items", 94),
                    BookLine("2.2: The Steam Pump", 95),
                    BookLine("3: Plunder", 96),
                ),
            ),
            PageSet(
                Page(
                    BookLine("Mos Le'Harmless 'Rum'", 97),
                    BookLine("", 68),
                    BookLine("Ingredients", 69),
                    BookLine("", 70),
                    BookLine("Bitternuts", 71),
                    BookLine("", 72),
                    BookLine("Sweetgrubs", 73),
                    BookLine("", 74),
                    BookLine("Scrapey Tree Bark", 75),
                    BookLine("", 76),
                    BookLine("Coloured Water", 77),
                    BookLine("", 78),
                    BookLine("Water", 79),
                    BookLine("", 80)
                ),
                Page(
                    BookLine("Mos Le'Harmless 'rum' is", 82),
                    BookLine("based loosely on the recipe", 83),
                    BookLine("fer Braindeath 'rum', with the", 84),
                    BookLine("main exception bein' that it is", 85),
                    BookLine("brewed from items found", 86),
                    BookLine("locally to the island.", 87)
                ),
            ),
            PageSet(
                Page(
                    BookLine("Water and Coloured Water", 97),
                    BookLine("", 68),
                    BookLine("", 69),
                    BookLine("", 70), // MODEL
                    BookLine("", 71),
                    BookLine("", 72),
                    BookLine("", 73), // MODEL
                    BookLine("", 74),
                    BookLine("", 75),
                    BookLine("", 76),
                    BookLine("The last two ingredients be", 77),
                    BookLine("the easiest to plunder; water", 78),
                    BookLine("from either the lake in the", 79),
                    BookLine("middle of the brewery", 80),
                    BookLine("compound, or from one of", 81)
                ),
                Page(
                    BookLine("the pumps can be put into", 82),
                    BookLine("the correct hopper with nary", 83),
                    BookLine("a trouble. The coloured", 84),
                    BookLine("water be gained by placing", 85),
                    BookLine("the petals of local flowers into", 86),
                    BookLine("a kettle of boilin' water. This", 87),
                    BookLine("leeches the colour from them,", 88),
                    BookLine("and the water can be", 89),
                    BookLine("siphoned off into bowls.  It", 90),
                    BookLine("takes 5 buckets of water, and", 91),
                    BookLine("three bowls of coloured water", 92),
                    BookLine("to make one bottle of rum.", 93),
                ),
            ),
            PageSet(
                Page(
                    BookLine("Bitternuts", 97),
                    BookLine("", 68),
                    BookLine("", 69),
                    BookLine("", 70),
                    BookLine("Bitternuts be foul tastin' but", 71),
                    BookLine("highly intoxicatin' nuts found", 72),
                    BookLine("inside the brewin' compound.", 73),
                    BookLine("The trees they are in be too", 74),
                    BookLine("tall to climb, especially with all", 75),
                    BookLine("the action goin' on in the", 76),
                    BookLine("area. Hence, tis the best idea", 77),
                    BookLine("to capture a monkey, and", 78),
                    BookLine("put it up the tree to get the", 79),
                    BookLine("nut for ye. However, the", 80)
                ),
                Page(
                    BookLine("monkey will need to be dyed", 82),
                    BookLine("the same colour as yer team,", 83),
                    BookLine("or confusion will arise.", 84),
                    BookLine("Fortunately, a bowl of", 85),
                    BookLine("coloured water from the", 86),
                    BookLine("kettle will colour the monkey", 87),
                    BookLine("with only mild scaldin'. Some", 88),
                    BookLine("people tell tales that they can", 89),
                    BookLine("talk to the monkeys and", 90),
                    BookLine("persuade them to pass them", 91),
                    BookLine("the nut faster. Admittedly", 92),
                    BookLine("they say you need a magic", 93),
                    BookLine("amulet, but then again that", 94),
                    BookLine("sounds a little far-fetched to", 95),
                    BookLine("me. It should also be noted", 96)
                ),
            ),
            PageSet(
                Page(
                    BookLine("that if someone puts a", 97),
                    BookLine("different coloured monkey up", 68),
                    BookLine("the tree, then the two", 69),
                    BookLine("monkeys will fight over which", 70),
                    BookLine("gets the bitternut.", 71)
                ),
                Page(
                    BookLine("Sweetgrubs", 82),
                    BookLine("", 83),
                    BookLine("", 84),
                    BookLine("", 85),
                    BookLine("Sweetgrubs be vicious little", 86),
                    BookLine("grubs that live in mounds", 87),
                    BookLine("inside the brewin' area. They", 88),
                    BookLine("will go fer any meat nearby,", 89),
                    BookLine("so tis best to grab some rat", 90),
                    BookLine("meat and lay it on the", 91),
                    BookLine("mound.  The grubs be very", 92),
                    BookLine("sleepy after they have eaten,", 93),
                    BookLine("and ye'll be able te scoop", 94),
                    BookLine("them up after they have", 95)
                ),
            ),
            PageSet(
                Page(
                    BookLine("devoured the meat. To get", 97),
                    BookLine("the best results, one load of", 68),
                    BookLine("sweetgrubs be needed fer", 69),
                    BookLine("every bottle of rum.", 70)
                ),
                Page(
                    BookLine("Scrapey Tree Bark", 82),
                    BookLine("", 83),
                    BookLine("", 84), // MODEL
                    BookLine("", 85),
                    BookLine("", 86),
                    BookLine("Scrapey Tree Bark is added", 87),
                    BookLine("to give the mix a little more", 88),
                    BookLine("body. There be a few", 89),
                    BookLine("Scrapey Trees in the area", 90),
                    BookLine("that ye can go fer, all ye", 91),
                    BookLine("need is a hatchet. Take a knife", 92),
                    BookLine("te the logs, and that'll get the", 93),
                    BookLine("bark. Mind ye don't get nay", 94),
                    BookLine("of the sap on yer hands as it", 95),
                ),
            ),
            PageSet(
                Page(
                    BookLine("carries an annoyin' little", 97),
                    BookLine("disease.", 68)
                ),
                Page(
                    BookLine("Boilers", 82),
                    BookLine("", 83),
                    BookLine("While ye'll need all these te", 84),
                    BookLine("make 'rum', ye'll also need to", 85),
                    BookLine("keep the boilers nice and hot.", 86),
                    BookLine("These are quite easy te feed.", 87),
                    BookLine("Just take some logs and put", 88),
                    BookLine("them in the boiler, then light", 89),
                    BookLine("them on fire with a", 90),
                    BookLine("tinderbox. Once it's burnin'", 91),
                    BookLine("it'll take up a few logs every", 92),
                    BookLine("now and again. So long as", 93),
                    BookLine("there are at least three", 94),
                    BookLine("burnin' then the 'rum' will be", 95),
                    BookLine("produced. It's always best to", 96),
                ),
            ),
            PageSet(
                Page(
                    BookLine("keep them fed though, as ye", 97),
                    BookLine("never know when they will", 68),
                    BookLine("run out.", 69)
                ),
                Page(
                    BookLine("Brewin'", 82),
                    BookLine("", 83),
                    BookLine("Assumin' all the stuff is in", 84),
                    BookLine("the hoppers, and all the", 85),
                    BookLine("boilers are burnin' and all the", 86),
                    BookLine("pipes are nice and fixed, then", 87),
                    BookLine("the right amount of stuff will", 88),
                    BookLine("be taken out of the hoppers,", 89),
                    BookLine("and the brewin' will begin.", 90),
                    BookLine("This takes a little while te do,", 91),
                    BookLine("but not too long though.", 92),
                    BookLine("When it is done, the rum will", 93),
                    BookLine("pop out on the conveyor belt,", 94),
                    BookLine("and all ye have te do is pick", 95),
                    BookLine("it up and put it in the crate.", 96)
                ),
            ),
            PageSet(
                Page(
                    BookLine("", 97),
                    BookLine("However, it should be noted", 68),
                    BookLine("that if ye have not", 69),
                    BookLine("contributed enough to any", 70),
                    BookLine("bottle of rum, then you will", 71),
                    BookLine("not be able te collect it. To", 72),
                    BookLine("qualify, ye must add enough", 73),
                    BookLine("of any ingredient te the", 74),
                    BookLine("hoppers to make the light", 75),
                    BookLine("turn green. Hence, if ye add", 76),
                    BookLine("5 buckets of water, ye", 77),
                    BookLine("qualify te collect 1 bottle, and", 78),
                    BookLine("if ye add five bitternuts, ye", 79),
                    BookLine("qualify fer 5. If ye add both,", 80),
                    BookLine("then ye qualify fer 6 bottles.", 81)
                ),
                Page(
                    BookLine("Sabotage and Repair", 82),
                    BookLine("", 83),
                    BookLine("As it is against the Pirates", 84),
                    BookLine("Code, ye'll not be able te kill", 85),
                    BookLine("anyone in there directly.", 86),
                    BookLine("However, the stills are fair", 87),
                    BookLine("game. Most of them can be", 88),
                    BookLine("burned down with a torch,", 89),
                    BookLine("if ye try hard enough.", 90),
                    BookLine("", 91),
                    BookLine("", 92),
                    BookLine("", 93), // MODEL
                    BookLine("", 94),
                    BookLine("", 95),
                    BookLine("", 96),
                ),
            ),
            PageSet(
                Page(
                    BookLine("When some scum-sucking", 97),
                    BookLine("dog has set yer still on fire", 68),
                    BookLine("don't panic. Just take a", 69),
                    BookLine("bucket of water and douse", 70),
                    BookLine("the fire. Then, dependin' on", 71),
                    BookLine("what was burnin', use either", 72),
                    BookLine("a pipe section, lumber patch", 73),
                    BookLine("or bridge section on the", 74),
                    BookLine("burned bit to repair it. Pipe", 75),
                    BookLine("sections be used fer pipes", 76),
                    BookLine("and the water pump.", 77),
                    BookLine("", 78),
                    BookLine("", 79), // MODEL
                    BookLine("", 80),
                    BookLine("", 81),
                ),
                Page(
                    BookLine("", 82),
                    BookLine("", 83),
                    BookLine("Lumber Patches fer the", 84),
                    BookLine("hoppers.", 85),
                    BookLine("", 86),
                    BookLine("", 87), // MODEL
                    BookLine("", 88),
                    BookLine("", 89),
                    BookLine("Bridge sections be needed", 90),
                    BookLine("fer bridges.", 91),
                    BookLine("", 92),
                    BookLine("", 93),  // MODEL
                    BookLine("", 94),
                    BookLine("", 95),
                    BookLine("", 96),
                ),
            ),
            PageSet(
                Page(
                    BookLine("Makin' More Repair Items.", 97),
                    BookLine("", 68),
                    BookLine("If ye run low on items (as", 69),
                    BookLine("will happen if yer enemy be", 70),
                    BookLine("in a fiery mood) then ye will", 71),
                    BookLine("need te make yer own items.", 72),
                    BookLine("  This is quite easy, as there", 73),
                    BookLine("be a bench full of handy tools", 74),
                    BookLine("fer ye te uses to make more.", 75),
                    BookLine("Just take a log and use it on", 76),
                    BookLine("the table, and ye'll be able te", 77),
                    BookLine("craft up as many of the", 78),
                    BookLine("items as ye need.", 79),
                ),
                Page(
                    BookLine("The Steam Pump", 82),
                    BookLine("", 83),
                    BookLine("If things be looking dreadful,", 84),
                    BookLine("then ye can use the steam", 85),
                    BookLine("pump to raise the pressure in", 86),
                    BookLine("the boilers, and then vent it", 87),
                    BookLine("through the damaged parts te", 88),
                    BookLine("put out fires and badly scald", 89),
                    BookLine("anyone lurkin' nearby.", 90),
                    BookLine("The pump be quite stiff te", 91),
                    BookLine("work, and ye'll need a lot of", 92),
                    BookLine("energy te keep it goin'. Just", 93),
                    BookLine("haul on the lever fer a while", 94),
                    BookLine("until ye gets tired, and that", 95),
                    BookLine("will put up the pressure.", 96)
                ),
            ),
            PageSet(
                Page(
                    BookLine("When yer base is under", 97),
                    BookLine("attack, and things be on fire,", 68),
                    BookLine("then vent the pressure te", 69),
                    BookLine("give them a nasty surprise.", 70),
                    BookLine("Be warned though, the steam", 71),
                    BookLine("will damage the pipes and", 72),
                    BookLine("hoppers as it comes out, so", 73),
                    BookLine("that might do more harm", 74),
                    BookLine("than good.", 75)
                ),
                Page(
                    BookLine("Plunder", 82),
                    BookLine("", 82),
                    BookLine("Well, ye've set things on fire,", 83),
                    BookLine("worked the steam pump and", 84),
                    BookLine("stashed the rum, and now the", 85),
                    BookLine("game is over.  What do ye", 86),
                    BookLine("get?  Plunder!", 87),
                    BookLine("Ye will be awarded a number", 88),
                    BookLine("of Pieces of Eight depending", 89),
                    BookLine("on how well yer team did,", 90),
                    BookLine("and how hard ye worked", 91),
                    BookLine("during' the game. If ye have", 92),
                    BookLine("a word with Honest Jimmy,", 93),
                    BookLine("then he'll sell ye some nice", 94),
                    BookLine("items fer all yer hard work.", 95),
                ),
            ),
            PageSet(
                Page(
                    BookLine("Tricorn hats are the latest in", 97),
                    BookLine("pirate fashions. They go well", 68),
                    BookLine("with any outfit, and will make", 69),
                    BookLine("ye the envy of all the other", 70),
                    BookLine("pirates as ye walk down the", 71),
                    BookLine("plank.", 72),
                    BookLine("", 73),
                    BookLine("", 74), // MODEL
                    BookLine("", 75),
                    BookLine("", 76),
                    BookLine("Nothin' says 'I lived the", 77),
                    BookLine("Navy lifestyle before", 78),
                    BookLine("deserting te make me", 79),
                    BookLine("fortune' like an old tattered", 80),
                    BookLine("Navy uniform, and Jimmy", 81)
                ),
                Page(
                    BookLine("has plenty.  All pre-tattered", 82),
                    BookLine("and authentic, get one today!", 83),
                    BookLine("", 84),
                    BookLine("", 85),
                    BookLine("", 86), // MODEL
                    BookLine("", 87),
                    BookLine("", 88),
                    BookLine("", 89),
                    BookLine("", 90), // MODEL
                    BookLine("", 91),
                    BookLine("", 92),
                    BookLine("If ye save up enough Pieces", 93),
                    BookLine("of Eight, then ye can own", 94),
                    BookLine("yer very own surplus pirate", 95)
                ),
            ),
            PageSet(
                Page(
                    BookLine("flag! Jimmy has plenty on", 97),
                    BookLine("offer, including a rare", 68),
                    BookLine("example from the ghost ship", 69),
                    BookLine("The Phasmatys Pride!", 70),
                    BookLine("", 71),
                    BookLine("Last but by no means least,", 72),
                    BookLine("be The Stuff. Jimmy won't", 73),
                    BookLine("tell us where he gets it, as", 74),
                    BookLine("the Cooking Guild keeps", 75),
                    BookLine("threatening te reduce him te", 76),
                    BookLine("soup if he keeps sellin' it. It", 77),
                    BookLine("works by bein' sprinkled into", 78),
                    BookLine("yer ale before ye add the", 79),
                    BookLine("hops, and the effect means", 80),
                    BookLine("that yer a little more likely te", 81)
                ),
                Page(
                    BookLine("get a mature ale by the end", 82),
                    BookLine("of it.", 83),
                    BookLine("", 84), // MODEL
                    BookLine("", 85),
                    BookLine("", 86),
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
        BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_26, TITLE, CONTENTS)
        /*
         * Bowl of water model.
         */
        BookInterface.setModelOnPage(player, 2, BOWL_OF_WATER_MODEL, BookInterface.FANCY_BOOK_26, BookInterface.FANCY_BOOK_26_IMAGE_ENABLE_DRAW_IDS[3], BookInterface.FANCY_BOOK_26_IMAGE_DRAW_IDS[3], 600, 100, 0)
        /*
         * Bucket of water model.
         */
        BookInterface.setModelOnPage(player, 2, BUCKET_OF_WATER_MODEL, BookInterface.FANCY_BOOK_26, BookInterface.FANCY_BOOK_26_IMAGE_ENABLE_DRAW_IDS[6], BookInterface.FANCY_BOOK_26_IMAGE_DRAW_IDS[6], 500, 200, 1800)
        /*
         * Bitternut model
         */
        BookInterface.setModelOnPage(player, 3, BITTERNUTS_MODEL, BookInterface.FANCY_BOOK_26, BookInterface.FANCY_BOOK_26_IMAGE_ENABLE_DRAW_IDS[2], BookInterface.FANCY_BOOK_26_IMAGE_DRAW_IDS[2], 200, 300, 1800)
        /*
         * Sweetgrubs model // TODO
         */
        BookInterface.setModelOnPage(player, 4, SWEETGRUBS_MODEL, BookInterface.FANCY_BOOK_26, BookInterface.FANCY_BOOK_26_IMAGE_ENABLE_DRAW_IDS[21], BookInterface.FANCY_BOOK_26_IMAGE_DRAW_IDS[21], 600, 500, 100)
        /*
         * Scrapey bark model. // TODO
         */
        BookInterface.setModelOnPage(player, 5, SCRAPEY_TREE_BARK_MODEL, BookInterface.FANCY_BOOK_26, BookInterface.FANCY_BOOK_26_IMAGE_ENABLE_DRAW_IDS[21], BookInterface.FANCY_BOOK_26_IMAGE_DRAW_IDS[21], 500, 500, 100)
        /*
         * Lit torch model.
         */
        BookInterface.setModelOnPage(player, 8, LIT_TORCH_MODEL, BookInterface.FANCY_BOOK_26, BookInterface.FANCY_BOOK_26_IMAGE_ENABLE_DRAW_IDS[27], BookInterface.FANCY_BOOK_26_IMAGE_DRAW_IDS[27], 500, 200, 0)
        /*
         * Water pump model
         */
        BookInterface.setModelOnPage(player, 9, WATER_PUMP_MODEL, BookInterface.FANCY_BOOK_26, BookInterface.FANCY_BOOK_26_IMAGE_ENABLE_DRAW_IDS[13], BookInterface.FANCY_BOOK_26_IMAGE_DRAW_IDS[13], 700, 200, 1700)
        /*
         * Lumber patches model.
         */
        BookInterface.setModelOnPage(player, 9, LUMBER_PATCHES_MODEL, BookInterface.FANCY_BOOK_26, BookInterface.FANCY_BOOK_26_IMAGE_ENABLE_DRAW_IDS[23], BookInterface.FANCY_BOOK_26_IMAGE_DRAW_IDS[23], 700, 200, 0)
        /*
         * Bridge section model.
         */
        BookInterface.setModelOnPage(player, 9, BRIDGE_SECTION_MODEL, BookInterface.FANCY_BOOK_26, BookInterface.FANCY_BOOK_26_IMAGE_ENABLE_DRAW_IDS[26], BookInterface.FANCY_BOOK_26_IMAGE_DRAW_IDS[26], 1100, 200, 300)
        /*
         * Tricorn hat model.
         */
        BookInterface.setModelOnPage(player, 12, TRICORN_HAT_MODEL, BookInterface.FANCY_BOOK_26, BookInterface.FANCY_BOOK_26_IMAGE_ENABLE_DRAW_IDS[7], BookInterface.FANCY_BOOK_26_IMAGE_DRAW_IDS[7], 300, 200, 200)
        /*
         * Naval t shirt model.
         */
        BookInterface.setModelOnPage(player, 12, NAVAL_T_SHIRT_MODEL, BookInterface.FANCY_BOOK_26, BookInterface.FANCY_BOOK_26_IMAGE_ENABLE_DRAW_IDS[21], BookInterface.FANCY_BOOK_26_IMAGE_DRAW_IDS[21], 600, 400, 100)
        /*
         * Naval pants model.
         */
        BookInterface.setModelOnPage(player, 12, NAVAL_PANTS_MODEL, BookInterface.FANCY_BOOK_26, BookInterface.FANCY_BOOK_26_IMAGE_ENABLE_DRAW_IDS[24], BookInterface.FANCY_BOOK_26_IMAGE_DRAW_IDS[24], 800, 400, 800)
        /*
         * The stuff model.
         */
        BookInterface.setModelOnPage(player, 13, THE_STUFF, BookInterface.FANCY_BOOK_26, BookInterface.FANCY_BOOK_26_IMAGE_ENABLE_DRAW_IDS[20], BookInterface.FANCY_BOOK_26_IMAGE_DRAW_IDS[20], 500, 300, 200)
        return true
    }

    override fun defineListeners() {
        on(Items.BREWIN_GUIDE_8989, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_26, ::display)
            return@on true
        }
    }
}