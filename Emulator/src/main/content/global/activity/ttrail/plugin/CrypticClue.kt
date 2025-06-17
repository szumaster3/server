package content.global.activity.ttrail.plugin

import content.global.activity.ttrail.ClueLevel
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.plugin.Plugin
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Scenery

/**
 * Represents the cryptic clues.
 * @author szu
 */
class CrypticClue : CrypticScroll {

    /**
     * Constructs a new Cryptic clue plugin.
     */
    constructor() : this(null, -1, null, null, null)

    /**
     * Constructs a cryptic clue with basic parameters.
     */
    constructor(name: String?, clueId: Int, level: ClueLevel?, clue: String?, location: Location?) : super(name, clueId, level, clue, location)

    /**
     * Constructs a cryptic clue with additional object and zone data.
     */
    constructor(name: String, clueId: Int, level: ClueLevel, clue: String, location: Location, `object`: Int, vararg borders: ZoneBorders) : super(name, clueId, level, clue, location, `object`, *borders)

    /**
     * Registers all known cryptic clue scrolls
     */
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        register(CrypticClue("etceteria-evergreen", Items.CLUE_SCROLL_10190, ClueLevel.HARD, "And so on, and so on, and so on.<br>Walking from the land of many<br>unimportant things leads to a choice<br>of<br>paths.", Location.create(2591, 3879, 0)))
        register(CrypticClue("elemental-workshop-crates", Items.CLUE_SCROLL_10192, ClueLevel.HARD, "You have all of the elements<br>available to solve this clue.<br>Fortunately you do not have to<br>go so far as to stand in a<br>draft.", Location.create(2723, 9891, 0), Scenery.CRATE_18506, ZoneBorders(2722, 9890, 2724, 9892)))
        register(CrypticClue("east-ardougne-crate", Items.CLUE_SCROLL_10194, ClueLevel.EASY, "<br><br><br>A crate found in the<br>tower of a church is<br>your next stop.", Location.create(2612, 3306, 1), Scenery.CRATE_34585, ZoneBorders(2609, 3304, 2621, 3311, 1)))
        register(CrypticClue("aggie-house-draynor", Items.CLUE_SCROLL_10196, ClueLevel.HARD, "Aggie I see, Lonely and southern I<br>feel, I am neither inside nor outside the<br>house,<br>yet no house would be complete without<br>me. The treasure lies beneath me!", Location.create(3085, 3255, 0)))
        register(CrypticClue("wilderness-sapphire", Items.CLUE_SCROLL_10198, ClueLevel.HARD, "46 is my number,<br>my body is the colour of burnt orange<br>and crawls among those with eight.<br>Three mouths I have,<br>yet I cannot eat.<br>My blinking blue eye hides my grave.", Location.create(3169, 3885, 0)))
        register(CrypticClue("almera-house", Items.CLUE_SCROLL_10200, ClueLevel.HARD, "A great view - watch<br>the rapidly drying<br>hides get splashed.<br>Check the box you are sitting on.", Location.create(2523, 3493, 1), Scenery.BOXES_359, ZoneBorders(2522, 3492, 2524, 3494, 1)))
        register(CrypticClue("canifis-clothes-shop", Items.CLUE_SCROLL_10202, ClueLevel.MEDIUM, "A town with a different sort of<br>night-life is your destination.<br>Search for some crates<br>in one of the houses.", Location.create(3498, 3507, 0), Scenery.CRATE_24911, ZoneBorders(3497, 3506, 3500, 3508)))
        register(CrypticClue("nardah-flying-rug", Items.CLUE_SCROLL_10204, ClueLevel.HARD, "As you desert this town,<br>keep an eye out for a set of spines<br>that could ruin nearby rugs:<br>dig carefully around the greenery.", Location.create(3397, 2917, 0)))
        register(CrypticClue("west-ardougne-prison", Items.CLUE_SCROLL_10206, ClueLevel.MEDIUM, "Being this far north has meant<br>that these crates have escaped<br>being battled over.", Location.create(2519, 3259, 0), Scenery.CRATE_40495, ZoneBorders(2517, 3258, 2521, 3260)))
        register(CrypticClue("sandstone-granite-quarry", Items.CLUE_SCROLL_10208, ClueLevel.MEDIUM, "Brush off the sand and dig in the<br>quarry.<br>There is a wheely handy<br>barrow to the east.<br>Don't worry, it's coal to dig there-in<br>fact, it's all oclay.", Location.create(3175, 2915, 0)))
        register(CrypticClue("port-phasmatys-tree", Items.CLUE_SCROLL_10210, ClueLevel.MEDIUM, "By the town of the dead,<br>walk south down a rickety bridge,<br>then dig near the slime-covered<br>tree.", Location.create(3644, 3494, 0)))
        register(CrypticClue("edgeville-yew-tree", Items.CLUE_SCROLL_10212, ClueLevel.HARD, "Come to the evil ledge,<br>Yew know yew want to.<br>And try not to get stung.", Location.create(3090, 3469, 0)))
        register(CrypticClue("mortton-circle-shadows", Items.CLUE_SCROLL_10214, ClueLevel.HARD, "Covered in shadows, the centre<br>of the circle is where you will<br>find the answer.", Location.create(3488, 3289, 0)))
        register(CrypticClue("pollnivneach-well", Items.CLUE_SCROLL_10216, ClueLevel.MEDIUM, "Dig here if you aren't feeling too<br>well<br>after travelling through the desert.<br>Ali heartily recommends it.", Location.create(3358, 2970, 0)))
        register(CrypticClue("mudskipper-point", Items.CLUE_SCROLL_10218, ClueLevel.MEDIUM, "Don't skip here,<br>it's too muddy.<br>You'll feel like a star if you dig here,<br>though.", Location.create(3000, 3110, 0)))
        register(CrypticClue("baxtorian-falls-hay", Items.CLUE_SCROLL_10220, ClueLevel.MEDIUM, "Hay! Stop for a bit and admire<br>the scenery, just like the tourism<br>promoter says.", Location.create(2525, 3436, 0), Scenery.HAYSTACK_300, ZoneBorders(2524, 3435, 2526, 3438)))
        register(CrypticClue("varrock-church", Items.CLUE_SCROLL_10222, ClueLevel.MEDIUM, "You'll need to look for<br>a town with a central<br>fountain. Look for a locked<br>chest in the town's chapel.", Location.create(3256, 3487, 0), Scenery.OPEN_CHEST_24204, ZoneBorders(2524, 3435, 2526, 3438)))
        register(CrypticClue("monastery-monks", Items.CLUE_SCROLL_10224, ClueLevel.MEDIUM, "Find a crate close to the<br>monks that like to paaarty!", Location.create(2614, 3204, 0), Scenery.CRATE_354, ZoneBorders(2613, 3203, 2615, 3205)))
        register(CrypticClue("mage-training-bookcase", Items.CLUE_SCROLL_10226, ClueLevel.MEDIUM, "For any aspiring mage,<br>I'm sure searching this bookcase<br>will be a rewarding experience.", Location.create(3366, 3319, 1), Scenery.OLD_BOOKSHELF_10723, ZoneBorders(3365, 3318, 3367, 3320, 1)))
        register(CrypticClue("lumbridge-windmill", Items.CLUE_SCROLL_10228, ClueLevel.HARD, "Four blades I have yet I draw no blood.<br>But I mash andturn my victims to<br>powder.<br>Search in my head,<br>search in my rafters,<br>Where my blades are louder.", Location.create(3166, 3309, 2), Scenery.CRATE_12963, ZoneBorders(3165, 3308, 3167, 3310, 2)))
        register(CrypticClue("lighthouse-drawers", Items.CLUE_SCROLL_10230, ClueLevel.MEDIUM, "Go to this building to be<br>illuminated, and search the<br>drawers while you're there.", Location.create(2512, 3641, 1), Scenery.DRAWERS_351, ZoneBorders(2511, 3639, 2513, 3641, 1)))
        register(CrypticClue("fishing-platform-crate", Items.CLUE_SCROLL_10232, ClueLevel.MEDIUM, "Try not to step on<br>the crates that dot<br>this platform.", Location.create(2764, 3273, 0), Scenery.CRATE_18502, ZoneBorders(2763, 3272, 2765, 3274)))
        register(CrypticClue("lumbridge-crates", Items.CLUE_SCROLL_10234, ClueLevel.HARD, "You will need to under-cook<br>to solve this one.", Location.create(3219, 9617, 0), Scenery.CRATE_357, ZoneBorders(3218, 9616, 3220, 9618)))
        register(CrypticClue("dunstan-burthorpe", Items.CLUE_SCROLL_10236, ClueLevel.MEDIUM, "Go to the village being<br>attacked by trolls, search the<br>drawers while you are there.", Location.create(2921, 3577, 0), Scenery.DRAWERS_351, ZoneBorders(2920, 3576, 2922, 3578)))
        register(CrypticClue("dungeon-chest", Items.CLUE_SCROLL_10238, ClueLevel.MEDIUM, "This temple is rather sluggish.<br>The chest just inside the entrance,<br>however,<br>is filled with goodies.", Location.create(2698, 9684, 0), Scenery.CHEST_18321, ZoneBorders(2697, 9683, 2699, 9685)))
        register(CrypticClue("abbot-langley", Items.CLUE_SCROLL_10240, ClueLevel.MEDIUM, "'A bag belt only?'<br>he asked his balding<br>brothers.", Location.create(3058, 3485, 0), NPCs.ABBOT_LANGLEY_801, ZoneBorders(3053, 3481, 3062, 3500)))
        register(CrypticClue("oziac-armor-seller", Items.CLUE_SCROLL_10242, ClueLevel.HARD, "A strange little man who sells armour<br>only to those who've proven<br>themselves to be unafraid of dragons.", Location.create(3069, 3516, 0), NPCs.OZIACH_747, ZoneBorders(3066, 3514, 3070, 3518)))
        register(CrypticClue("zanaris-forge", Items.CLUE_SCROLL_10244, ClueLevel.MEDIUM, "After a hard slays spraying<br>back the vegetation, why not<br>pop off to the nearby forge and<br>search the crates?", Location.create(2399, 4476, 0), Scenery.CRATE_12105, ZoneBorders(2398, 4475, 2400, 4477)))
        register(CrypticClue("port-khazard-anvil", Items.CLUE_SCROLL_10246, ClueLevel.MEDIUM, "After trawling for bars,<br>go to the nearest place and smith<br>them and dig by the door.", Location.create(2656, 3161, 0)))
        register(CrypticClue("citric-cellar", Items.CLUE_SCROLL_10248, ClueLevel.HARD, "The cryptic reveals<br>who to speak to:<br>Citric Cellar.", Location.create(2491, 3488, 1), NPCs.HECKEL_FUNCH_603, ZoneBorders(2484, 3486, 2493, 3489, 1)))
        register(CrypticClue("blue-moon-inn", Items.CLUE_SCROLL_10250, ClueLevel.HARD, "Find a bar with a centre<br>fountain in its city.<br>Go upstairs and get<br>changed.", Location.create(3223, 3399, 1), Scenery.DRAWERS_24295, ZoneBorders(3222, 3398, 3224, 3400, 1)))
        register(CrypticClue("general-bentnoze", Items.CLUE_SCROLL_10252, ClueLevel.HARD, "Generally speaking,<br>his nose was very bent.", Location.create(2957, 3513, 0), NPCs.GENERAL_BENTNOZE_4493, ZoneBorders(2954, 3510, 2961, 3514)))
        register(CrypticClue("lumbridge-castle-spinning-wheel", Items.CLUE_SCROLL_10254, ClueLevel.HARD, "My home is grey and made of stone;<br>A castle with a search for a meal.<br>Hidden in some drawers I am,<br>Across from a wooden wheel.", Location.create(3213, 3216, 1), Scenery.DRAWERS_37012, ZoneBorders(3212, 3215, 3214, 3217, 1)))
        register(CrypticClue("tai-bwo-wannai-crates", Items.CLUE_SCROLL_10256, ClueLevel.HARD, "In a village made of bamboo,<br>look for some crates<br>under one of the houses.", Location.create(2800, 3074, 0), Scenery.CRATE_356, ZoneBorders(2799, 3073, 2801, 3075)))
        register(CrypticClue("shilo-village-bookcase", Items.CLUE_SCROLL_10258, ClueLevel.HARD, "This village has a problem<br>with cartloads of the undead.<br>Try checking the bookcase<br>to find the answer.", Location.create(2833, 2991, 0), Scenery.BOOKCASE_394, ZoneBorders(2832, 2990, 2834, 2993)))
        register(CrypticClue("necromancer-tower", Items.CLUE_SCROLL_10260, ClueLevel.HARD, "Throat mage seeks companionship.<br>Seek answers inside my furniture<br>if interested.", Location.create(2668, 3238, 1), Scenery.DRAWERS_353, ZoneBorders(2667, 3237, 2670, 3239, 1)))
        register(CrypticClue("braine-death-island-lake", Items.CLUE_SCROLL_10262, ClueLevel.HARD, "You will need to wash the old ash<br>off of your spade when you dig here,<br>but the only water nearby is<br>stagnant.", Location.create(2135, 5166, 0)))
        register(CrypticClue("digsite-doug-deep", Items.CLUE_SCROLL_10264, ClueLevel.HARD, "You'll need to have Doug Deep<br>into the distant<br>past to get to these sacks.", Location.create(3348, 9758, 0), Scenery.SACKS_32049, ZoneBorders(3347, 9757, 3349, 9759)))
        register(CrypticClue("captain-klemfoodle", Items.CLUE_SCROLL_10266, ClueLevel.MEDIUM, "You can cook food on me,<br>but don't cook any foodles -<br>That would be just wrong.", Location.create(2969, 2975, 0), Scenery.FIRE_2732, ZoneBorders(2968, 2974, 2970, 2976)))
        register(CrypticClue("lubufu-brimhaven", Items.CLUE_SCROLL_10268, ClueLevel.MEDIUM, "The owner of this crate has<br>a hunch that he put more<br>than fish inside.", Location.create(2770, 3172, 0), Scenery.CRATE_366, ZoneBorders(2769, 3171, 2771, 3173)))
        register(CrypticClue("canifis-archery-shop", Items.CLUE_SCROLL_10270, ClueLevel.EASY, "Search the drawers in<br>Catherby's Archery shop.", Location.create(2825, 3442, 0), Scenery.DRAWERS_33932, ZoneBorders(2824, 3441, 2826, 3443)))
        register(CrypticClue("yanille-crates", Items.CLUE_SCROLL_10272, ClueLevel.EASY, "Search the crates in a house<br>in Yanille that has a piano.", Location.create(2598, 3105, 0), Scenery.CRATE_357, ZoneBorders(2597, 3104, 2599, 3106)))
        return this
    }
}
