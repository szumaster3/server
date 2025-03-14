package content.global.handlers.item.book.guide

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class TroubleBrewinGuide : InteractionListener {
    /*
     * Guide for Trouble Brewing minigame.
     */

    companion object {
        private const val TITLE = "Brewin' guide"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("       BREWIN' GUIDE", 58),
                        BookLine("     A guide to brewin'", 59),
                        BookLine("<col=08088A>      Chapters", 62),
                        BookLine("   Mos Le'Harmless Rum", 63),
                    ),
                    Page(
                        BookLine("1:Ingrediants", 66),
                        BookLine("1.1:Water and Coloured Water", 67),
                        BookLine("1.2:Bitternuts", 68),
                        BookLine("1.3:Sweetgrubs", 69),
                        BookLine("1.4:Scrapey Tree Bark", 70),
                        BookLine("1.5:Boilers", 71),
                        BookLine("1.6:Brewin'", 72),
                        BookLine("2:Sabotage and Repair", 73),
                        BookLine("2.1:Makin' Repair Items", 74),
                        BookLine("2.2:The Steam Pump", 75),
                        BookLine("3:Plunder", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Mos Le'Harmless 'Rum'", 55),
                        BookLine("    <col=08088A>Ingredients", 57),
                        BookLine("     Bitternuts", 59),
                        BookLine("     Sweetgrubs", 60),
                        BookLine("     Scrapey Tree Bark", 61),
                        BookLine("     Coloured Water", 62),
                        BookLine("     Water", 63),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("<col=08088A>Mos Le'Harmless 'rum'", 57),
                        BookLine("is based on the recipe", 58),
                        BookLine("fer Braindeath 'rum',", 59),
                        BookLine("with the main exception", 60),
                        BookLine("bein' that it is brewed", 61),
                        BookLine("from items found locally", 62),
                        BookLine("to the island. Water and", 63),
                    ),
                    Page(
                        BookLine("Coloured Water The last", 66),
                        BookLine("two ingredients be the", 67),
                        BookLine("easiest to plunder; water", 68),
                        BookLine("from either the lake in", 69),
                        BookLine("the middle of the brewery", 70),
                        BookLine("compound, or from one", 71),
                        BookLine("of the pumps can be put", 72),
                        BookLine("into the correct hopper", 73),
                        BookLine("with nary a trouble. The", 74),
                        BookLine("coloured water be gained", 75),
                        BookLine("by placing the petals", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("of rate flowers into a", 55),
                        BookLine("kettle of boilin' water.", 56),
                        BookLine("This leeches the colour", 57),
                        BookLine("from them, and the water", 58),
                        BookLine("be siphoned off into bowls.", 59),
                        BookLine("It takes three bowls of", 60),
                        BookLine("coloured water to make", 61),
                        BookLine("one bottle of rum.", 62),
                        BookLine("<col=08088A>Bitternuts", 64),
                        BookLine("Bitternuts be foul tastin'", 65),
                    ),
                    Page(
                        BookLine("but highly intoxicatin'", 66),
                        BookLine("nuts found inside the", 67),
                        BookLine("brewin' compound. The", 68),
                        BookLine("trees they are in be too", 69),
                        BookLine("tall to climb, especially", 70),
                        BookLine("with all the action goin'", 71),
                        BookLine("on in the area. Hence,", 72),
                        BookLine("tis the best idea to capture", 73),
                        BookLine("a monkey, and put it up", 74),
                        BookLine("a tree to get the nut", 75),
                        BookLine("for ye. However, the monkey", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("will need to be dyed the", 55),
                        BookLine("same colour as yer team", 56),
                        BookLine("or confusion will arise. Fortunately,", 57),
                        BookLine("a bowl of coloured water", 58),
                        BookLine("from the kettle will colour", 59),
                        BookLine("the monkey with only a", 60),
                        BookLine("mild scaldin'. Some people", 61),
                        BookLine("tell me tales that they", 62),
                        BookLine("can talk to the monkeys", 63),
                        BookLine("and persuade them to pass", 64),
                        BookLine("the nut faster. Admittedly", 65),
                    ),
                    Page(
                        BookLine("they say you need a magic", 66),
                        BookLine("amulet, but then again", 67),
                        BookLine("that sounds a little far-fetched", 68),
                        BookLine("to me. It should also", 69),
                        BookLine("be noted that if someone", 70),
                        BookLine("puts a different coloured", 71),
                        BookLine("monkey in a tree then", 72),
                        BookLine("the two monkeys will fight", 73),
                        BookLine("over which gets the bitternut.", 74),
                        BookLine("<col=08088A>Sweetgrubs", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Sweetgrubs be a vicious", 55),
                        BookLine("little grub that live", 56),
                        BookLine("in mounds inside the brewin'", 57),
                        BookLine("area. They will go fer", 58),
                        BookLine("any meat nearby, so tis", 59),
                        BookLine("best to grab some rat", 60),
                        BookLine("meat and lay it on the", 61),
                        BookLine("mound. The grubs be very", 62),
                        BookLine("sleepy after they have", 63),
                        BookLine("eaten, and ye'll be able", 64),
                        BookLine("te scoop them up after", 65),
                    ),
                    Page(
                        BookLine("they have devoured the", 66),
                        BookLine("meat. To get the best", 67),
                        BookLine("results, one load of sweetgrubs", 68),
                        BookLine("be needed fer every bottle", 69),
                        BookLine("of rum. Scrapey Tree Bark Scrapey", 70),
                        BookLine("Tree Bark is added to", 71),
                        BookLine("give the mix a little", 72),
                        BookLine("more body. There be a", 73),
                        BookLine("few Scrapey Trees in the", 74),
                        BookLine("area that ye can go fer,", 75),
                        BookLine("all ye need is an axe.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Take a knife te the logs,", 55),
                        BookLine("and that'll get the bark.", 56),
                        BookLine("Mind ye don't get nay", 57),
                        BookLine("of the sap on yer hands,", 58),
                        BookLine("as it carries an annoyin'", 59),
                        BookLine("little disease.", 60),
                        BookLine("<col=08088A>Boilers", 62),
                        BookLine("While ye'll need all these", 63),
                        BookLine("te make 'rum', ye'll also", 64),
                        BookLine("need to keep the boilers", 65),
                    ),
                    Page(
                        BookLine("nice and hot. These are", 66),
                        BookLine("quite easy te feed. Just", 67),
                        BookLine("take some logs and put", 68),
                        BookLine("them in the boiler, then", 69),
                        BookLine("light them on fire with", 70),
                        BookLine("a tinderbox. Once it's", 71),
                        BookLine("burnin' it'll take up", 72),
                        BookLine("a few logs every now and", 73),
                        BookLine("again. So long as there", 74),
                        BookLine("are at least three burnin'", 75),
                        BookLine("then the 'rum' will be", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("produced. It's always", 55),
                        BookLine("best to keep them fed", 56),
                        BookLine("though, as ye never know", 57),
                        BookLine("when they will run out.", 58),
                        BookLine("<col=08088A>Brewin'", 60),
                        BookLine("Assumin' all the stuff", 61),
                        BookLine("is in the hoppers, and", 62),
                        BookLine("all the boilers are burnin'", 63),
                        BookLine("and all the pipes are", 64),
                        BookLine("nice and fixed, then the", 65),
                    ),
                    Page(
                        BookLine("right amount of stuff", 66),
                        BookLine("will be taken out of the", 67),
                        BookLine("hoppers, and the brewin'", 68),
                        BookLine("will begin. This takes", 69),
                        BookLine("a while te do, but not", 70),
                        BookLine("too long though. When", 71),
                        BookLine("it is done, the rum will", 72),
                        BookLine("pop out and all ye have", 73),
                        BookLine("te do is pick it up and", 74),
                        BookLine("put it in the crate. However,", 75),
                        BookLine("it should be noted that", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("if ye have not contributed", 55),
                        BookLine("enough to any of the bottle", 56),
                        BookLine("rum, the you will not", 57),
                        BookLine("be able to collect it.", 58),
                        BookLine("To qualify you must add", 59),
                        BookLine("enough ingredient te the", 60),
                        BookLine("hoppers to make the light", 61),
                        BookLine("turn green. Hence, if", 62),
                        BookLine("ye add 5 buckets of water,", 63),
                        BookLine("ye qualify te collect", 64),
                        BookLine("1 bottle, and if ye add", 65),
                    ),
                    Page(
                        BookLine("5 bitternuts ye qualify", 66),
                        BookLine("fer 5. If ye add both,", 67),
                        BookLine("then ye qualify for 6", 68),
                        BookLine("bottles.", 69),
                        BookLine("<col=08088A>Sabotage and Repair", 71),
                        BookLine("As it is against the Pirates", 72),
                        BookLine("Code, ye'll not be able", 73),
                        BookLine("to kill anyone directly.", 74),
                        BookLine("However, the stills are", 75),
                        BookLine("fair game. Most of them", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("can be burned down with", 55),
                        BookLine("a torch if ye try hard", 56),
                        BookLine("enough. When some scum-sucking", 57),
                        BookLine("dog has set yer still", 58),
                        BookLine("on fire don't panic. Just", 59),
                        BookLine("take a bucket of water", 60),
                        BookLine("and douse the fire. Then,", 61),
                        BookLine("dependin' on what was", 62),
                        BookLine("burnin', use either a", 63),
                        BookLine("pipe section, lumber patch", 64),
                        BookLine("or bridge section to the", 65),
                    ),
                    Page(
                        BookLine("burned bit and repair", 66),
                        BookLine("it. Pipe sections can", 67),
                        BookLine("be used fer pipes and", 68),
                        BookLine("the water pump. Lumber", 69),
                        BookLine("Patches fer the hoppers.", 70),
                        BookLine("Bridge sections be needed", 71),
                        BookLine("fer bridges.", 72),
                        BookLine("<col=08088A>Makin' More Repair Items.", 74),
                        BookLine("If ye run low on items", 75),
                        BookLine("(as will happen if yer", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("enemy be in a fiery mood)", 55),
                        BookLine("then ye will need to make", 56),
                        BookLine("yer own items. This is", 57),
                        BookLine("quite easy as there be", 58),
                        BookLine("a bench full of handy", 59),
                        BookLine("tools fer ye te uses to", 60),
                        BookLine("make more. Just take a", 61),
                        BookLine("log and use it on the", 62),
                        BookLine("table, and ye'll be able", 63),
                        BookLine("te craft up as many of", 64),
                        BookLine("the items as ye need.", 65),
                    ),
                    Page(
                        BookLine("<col=08088A>The Steam Pump", 67),
                        BookLine("If things be looking dreadful,", 68),
                        BookLine("then ye can use the steam", 69),
                        BookLine("pump to raise the pressure", 70),
                        BookLine("in the boilers, and then", 71),
                        BookLine("vent through damaged te", 72),
                        BookLine("put out fires and badly", 73),
                        BookLine("scald anyone lurking nearby. The", 74),
                        BookLine("pump be quite stuff te", 75),
                        BookLine("work, and ye'll need a", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("lot of energy te keep", 55),
                        BookLine("it goin'. Just haul on", 56),
                        BookLine("the leaver fer a while", 57),
                        BookLine("until ye gets tired, and", 58),
                        BookLine("that will put up the pressure.", 59),
                        BookLine("When yer base is under", 60),
                        BookLine("attack, and things be", 61),
                        BookLine("on fire, then vent pressure", 62),
                        BookLine("te give them a nasty surprise.", 63),
                        BookLine("Be warned though, the", 64),
                        BookLine("steam will damage the", 65),
                    ),
                    Page(
                        BookLine("pipes and hoppers as it", 66),
                        BookLine("comes out, so that might", 67),
                        BookLine("do more harm than good.", 68),
                        BookLine("<col=08088A>Plunder", 70),
                        BookLine("Well, ye've set things", 71),
                        BookLine("on fire, worked the steam", 72),
                        BookLine("pump and stashed the rum,", 73),
                        BookLine("and now the game is over.", 74),
                        BookLine("What do ye get? Punder! Ye", 75),
                        BookLine("will be awarded a number", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("of Pieces of Eight depending", 55),
                        BookLine("on how well yer team did,", 56),
                        BookLine("and how hard ye worked", 57),
                        BookLine("during' the game. If ye", 58),
                        BookLine("have a word with Honest", 59),
                        BookLine("Jimmy, then he'll sell", 60),
                        BookLine("ye some nice items fer", 61),
                        BookLine("all yer hard work.", 62),
                        BookLine("Tricorn hats are the latest", 63),
                        BookLine("pirate fashions. They", 64),
                        BookLine("go well with any outfit,", 65),
                    ),
                    Page(
                        BookLine("and will make ye the envy", 66),
                        BookLine("of all the other pirates", 67),
                        BookLine("as ye walk down the plank.", 68),
                        BookLine("Nothing says 'I lived", 69),
                        BookLine("the Navy lifestyle before", 70),
                        BookLine("deserting te make me fortune'", 71),
                        BookLine("like an old tattered Navy", 72),
                        BookLine("Uniform, and Jimmy has", 73),
                        BookLine("plenty. All pre-tattered", 74),
                        BookLine("and authentic, get one", 75),
                        BookLine("today! If ye save up enough", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Pieces of Eight, then", 55),
                        BookLine("ye can own yer very own", 56),
                        BookLine("surplus pirate flag! Jimmy", 57),
                        BookLine("has plenty on offer, including", 58),
                        BookLine("a rare example from the", 59),
                        BookLine("ghost ship The Phasmatys", 60),
                        BookLine("Pride! Last but by no", 61),
                        BookLine("means least, be The Stuff.", 62),
                        BookLine("Jimmy won't tell us where", 63),
                        BookLine("he gets it, as the Cooking", 64),
                        BookLine("Guild keeps threatening", 65),
                    ),
                    Page(
                        BookLine("to reduce him te soup", 66),
                        BookLine("if he keeps sellin' it.", 67),
                        BookLine("It works by bein' sprinkled", 68),
                        BookLine("into yer ale before ye", 69),
                        BookLine("add the hops, and the", 70),
                        BookLine("effect means that yer", 71),
                        BookLine("a little more likely to", 72),
                        BookLine("get a mature ale by the", 73),
                        BookLine("end of it.", 74),
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
        on(Items.BREWIN_GUIDE_8989, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
