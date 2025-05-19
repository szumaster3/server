package content.global.activity.ttrail.clues

import content.global.activity.ttrail.ClueLevel
import content.global.activity.ttrail.ClueScroll
import content.global.activity.ttrail.npcs.UriNPC
import content.global.activity.ttrail.scrolls.EmoteClueScroll
import core.game.node.entity.player.link.emote.Emotes
import core.game.world.map.zone.ZoneBorders
import core.plugin.ClassScanner.definePlugin
import core.plugin.Plugin

/**
 * Represents the emote clues.
 */
class EmoteClue : EmoteClueScroll {
    /**
     * Instantiates a new Emote clue plugin.
     */
    constructor() : super(null, -1, null, null, null, null, null)

    /**
     * Instantiates a new Emote clue plugin.
     *
     * @param name          the name
     * @param clueId        the clue id
     * @param level         the level
     * @param emote         the emote
     * @param commenceEmote the commence emote
     * @param equipment     the equipment
     * @param clue          the clue
     * @param borders       the borders
     */
    constructor(
        name: String?,
        clueId: Int,
        level: ClueLevel?,
        emote: Emotes?,
        commenceEmote: Emotes?,
        equipment: Array<IntArray>?,
        clue: String?,
        vararg borders: ZoneBorders?
    ) : super(name, clueId, level, emote!!, commenceEmote, equipment!!, clue!!, *borders)

    /**
     * Instantiates a new Emote clue plugin.
     *
     * @param name      the name
     * @param clueId    the clue id
     * @param level     the level
     * @param emote     the emote
     * @param equipment the equipment
     * @param clue      the clue
     * @param borders   the borders
     */
    constructor(
        name: String?,
        clueId: Int,
        level: ClueLevel?,
        emote: Emotes?,
        equipment: Array<IntArray>?,
        clue: String?,
        vararg borders: ZoneBorders?
    ) : super(name, clueId, level, emote!!, null, equipment!!, clue!!, *borders)

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        var emote = Emotes.BECKON
        register(EmoteClue("digsite-beckon", 2677, ClueLevel.MEDIUM, emote, Emotes.BOW, arrayOf(intArrayOf(6328), intArrayOf(1267), intArrayOf(658)), "Beckon in the Digsite, near the<br>eastern winch. Bow or curtsy<br>before you talk to me.<br>Equip a green gnome hat,<br>snakeskin boots and an<br>iron pickaxe.", ZoneBorders(3368, 3425, 3371, 3429)))
        register(EmoteClue("tai-bwo-beckon", 2678, ClueLevel.MEDIUM, emote, Emotes.CLAP, arrayOf(intArrayOf(1099), intArrayOf(2552, 2554, 2556, 2558, 2560, 2562, 2564, 2566, 2568), intArrayOf(1143)), "Beckon in Tai Bwo<br>Wannai. Clap before<br>you talk to me.<br>Equip green<br>dragonhide chaps, a<br>ring of dueling and a<br>mithril medium helmet.", ZoneBorders(2771, 3053, 2814, 3074)))
        emote = Emotes.BLOW_KISS
        register(EmoteClue("shilo-kiss", 2681, ClueLevel.HARD, emote, arrayOf(intArrayOf(4089), intArrayOf(5016), intArrayOf(1127)), "Blow a kiss between<br>the tables in Shilo<br>Village bank. Beware<br>of double agents!<br>Equip a blue mystic<br>hat, bone spear and<br>rune plate body.", ZoneBorders(2849, 2950, 2855, 2957)))
        emote = Emotes.BOW
        register(EmoteClue("legends-guild", 2682, ClueLevel.EASY, emote, arrayOf(intArrayOf(1067), intArrayOf(1696), intArrayOf(845)), "Bow outside the entrance<br>to the Legends' Guild.<br>Equip iron platelegs, an<br>emerald amulet and an oak<br>longbow.", ZoneBorders(2726, 3346, 2731, 3349)))
        emote = Emotes.RASPBERRY
        register(EmoteClue("monkey-cage", 2679, ClueLevel.EASY, emote, arrayOf(intArrayOf(1133), intArrayOf(1075), intArrayOf(1379)), "Blow a raspberry at the<br>monkey cage in<br>Ardougne Zoo.<br>Equip a studded<br>leather body, bronze<br>platelegs and a normal<br>staff with no orb.", ZoneBorders(2597, 3274, 2609, 3284)))
        register(EmoteClue("keep-le-faye", 2680, ClueLevel.EASY, emote, arrayOf(intArrayOf(1169), intArrayOf(1115), intArrayOf(1059)), "Blow raspberries out<br>the entrance to Keep Le<br>Faye.<br>Equip a coif, an iron<br>platebody and leather<br>gloves.", ZoneBorders(2759, 3401, 2762, 3403)))
        register(EmoteClue("fishing-guild-blow", 2683, ClueLevel.HARD, emote, arrayOf(intArrayOf(2890), intArrayOf(2493), intArrayOf(1347)), "Blow a raspberry in the<br>Fishing Guild bank. Beware<br>of double agents!<br>Equip an elemental shield<br>,blue dragonhide chaps<br>and a rune warhammer.", ZoneBorders(2586, 3418, 2590, 3422)))
        emote = Emotes.CHEER
        register(EmoteClue("druid-circle-cheer", 2684, ClueLevel.EASY, emote, arrayOf(intArrayOf(4310), intArrayOf(579), intArrayOf(1307)), "Cheer at the Druids'<br>Circle.<br>Equip a blue wizard<br>hat, a bronze<br>two-handed sword and<br>HAM boots.", ZoneBorders(2920, 3478, 2930, 3940)))
        register(EmoteClue("games-room-cheer", 2685, ClueLevel.EASY, emote, arrayOf(), "Cheer at the games<br>room.<br>Have nothing equipped<br>at all when you do.", ZoneBorders(2194, 4946, 2221, 4973)))
        register(EmoteClue("barb-agility-cheer", 2686, ClueLevel.MEDIUM, emote, arrayOf(intArrayOf(1119), intArrayOf(853), WILDERNESS_CAPES), "Cheer in the Barbarian<br>Agility Arena.<br>Headbang before you<br>talk to me.<br>Equip a steel plate<br>body, maple shortbow, and a wilderness<br>cape.", ZoneBorders(2550, 3556, 2553, 3559), ZoneBorders(2529, 3542, 2553, 3559)))
        register(EmoteClue("edge-gen-cheer", 2687, ClueLevel.MEDIUM, emote, Emotes.DANCE, arrayOf(intArrayOf(1757), intArrayOf(1061), intArrayOf(1059)), "Cheer in the Edgeville general<br>store. Dance<br>before you talk to me.<br>Equip a brown apron,<br>leather boots and<br>leather gloves.", ZoneBorders(3076, 3507, 3084, 3513)))
        register(EmoteClue("ogre-pen-cheer", 2688, ClueLevel.MEDIUM, emote, Emotes.ANGRY, arrayOf(intArrayOf(1135), intArrayOf(1099), intArrayOf(1177)), "Cheer in the Ogre Pen<br>in the Training Camp.<br>Show you are angry<br>before you talk to me.<br>Equip a green<br>dragonhide body and<br>chaps and a steel<br>square shield.", ZoneBorders(2523, 3373, 2533, 3377)))
        emote = Emotes.CLAP
        register(EmoteClue("exam-room-clap", 2689, ClueLevel.EASY, emote, arrayOf(intArrayOf(1005), intArrayOf(628), intArrayOf(1059)), "Clap in the main exam room<br>in the Exam Centre.<br>Equip a white apron, green<br>gnome boots and leather<br>gloves.", ZoneBorders(3357, 3332, 3367, 3348)))
        register(EmoteClue("wizard-tower-clap", 2690, ClueLevel.EASY, emote, arrayOf(intArrayOf(1137), intArrayOf(1639), intArrayOf(1005)), "Clap on the causeway to the<br>Wizards' Tower.<br>Equip an iron medium<br>helmet, emerald ring and a<br>white apron.", ZoneBorders(3112, 3173, 3115, 3206)))
        register(EmoteClue("ardgoune-mill-clap", 2691, ClueLevel.EASY, emote, arrayOf(intArrayOf(640), intArrayOf(4300), intArrayOf(5525)), "Clap on the top level of the<br>mill, north of East Ardougne.<br>Equip a blue gnome robe top,<br>HAM robe bottom and an<br>unenchanted tiara.", ZoneBorders(2628, 3382, 2636, 3390, 2)))
        register(EmoteClue("seers-court-clap", 2692, ClueLevel.MEDIUM, emote, Emotes.SPIN, arrayOf(intArrayOf(3200), intArrayOf(4093), intArrayOf(1643)), "Clap in Seers court house.<br>Spin before you talk to me.<br>Equip an adamant halberd,<br>blue mystic robe bottom and<br>a diamond ring.", ZoneBorders(2732, 3467, 2739, 3471)))
        emote = Emotes.CRY
        register(EmoteClue("catherby-range-cry", 2693, ClueLevel.MEDIUM, emote, Emotes.BOW, arrayOf(intArrayOf(630), intArrayOf(1131), intArrayOf(2961)), "Cry in the Catherby<br>Ranging shop. Bow before you talk to me.<br>Equip blue gnome<br>boots, a hard leather<br>body and an<br>unblessed silver<br>sickle.", ZoneBorders(2821, 3441, 2825, 3445)))
        register(EmoteClue("catherby-shore-cry", 2694, ClueLevel.MEDIUM, emote, Emotes.LAUGH, arrayOf(intArrayOf(1183), intArrayOf(8872), intArrayOf(1121)), "Cry on the shore of<br>Catherby beach.<br>Laugh before you talk to me.<br>Equip an adamant sq<br>shield, a bone dagger<br>and mithril platebody.", ZoneBorders(2849, 3423, 2857, 3430), ZoneBorders(2845, 3428, 2852, 3430)))
        emote = Emotes.DANCE
        register(EmoteClue("draynor-cross-dance", 2695, ClueLevel.EASY, emote, arrayOf(intArrayOf(1101), intArrayOf(1637), intArrayOf(839)), "Dance at the<br>crossroads north of<br>Draynor.<br>Equip an iron chain<br>body, a sapphire ring<br>and a longbow.", ZoneBorders(3109, 3293, 3110, 3296), ZoneBorders(3108, 3294, 3111, 3295)))
        register(EmoteClue("fally-party-dance", 2696, ClueLevel.EASY, emote, arrayOf(intArrayOf(1157), intArrayOf(1119), intArrayOf(1081)), "Dance in the Party<br>Room.<br>Equip a steel full<br>helmet, steel<br>platebody and an iron<br>plateskirt.", ZoneBorders(3041, 3372, 3050, 3384), ZoneBorders(3051, 3371, 3054, 3385), ZoneBorders(3073, 3371, 3040, 3385)))
        register(EmoteClue("fish-guild-jig", 2697, ClueLevel.EASY, Emotes.JIG, arrayOf(intArrayOf(1639), intArrayOf(1694), intArrayOf(1103)), "Dance a jig by the<br>entrance to the<br>Fishing Guild.<br>Equip an emerald ring,<br>a sapphire amulet,<br>and a bronze chain<br>body.", ZoneBorders(2610, 3392, 2612, 3393)))
        register(EmoteClue("lumb-shack-dance", 2698, ClueLevel.EASY, emote, arrayOf(intArrayOf(1205), intArrayOf(1153), intArrayOf(1635)), "Dance in<br>the shack in Lumbridge Swamp.<br>Equip a bronze<br>dagger, iron full helmet<br>and a gold ring.", ZoneBorders(3202, 3167, 3205, 3170)))
        register(EmoteClue("lumb-cave-dance", 2699, ClueLevel.MEDIUM, emote, Emotes.BLOW_KISS, arrayOf(intArrayOf(1381), intArrayOf(1155), intArrayOf(1731)), "Dance in the dark<br>cave beneath<br>Lumbridge Swamp.<br>Blow a kiss before<br>you talk to me.<br>Equip an air staff,<br>bronze full helm and<br>an amulet of power.", ZoneBorders(3139, 9534, 3260, 9587)))
        register(EmoteClue("shantay-guild-jig", 2700, ClueLevel.MEDIUM, Emotes.JIG, Emotes.BOW, arrayOf(intArrayOf(3343), intArrayOf(1381), intArrayOf(1173)), "Dance a jig under<br>Shantay's Awning.<br>Bow before you talk to<br>me.<br>Equip a pointed blue<br>snail helmet, an air<br>staff and a bronze<br>square shield.", ZoneBorders(3302, 3122, 3305, 3125)))
        register(EmoteClue("cat-entrance-dance", 2701, ClueLevel.HARD, emote, arrayOf(intArrayOf(2570), intArrayOf(1704), intArrayOf(1317)), "Dance at the<br>cat doored pyramid in<br>Sophanem. Beware of double agents!<br>Equip a ring of life<br>an uncharged amulet of glory<br>and an adamant two handed sword.", ZoneBorders(3293, 2781, 3296, 2782)))
        emote = Emotes.HEADBANG
        register(EmoteClue("al-kharid-headbang", 2702, ClueLevel.EASY, emote, arrayOf(intArrayOf(1833), intArrayOf(1059), intArrayOf(1061)), "Headbang in the mine north of Al<br>Kharid.<br>Equip a desert shirt, leather gloves and<br>leather boots.", ZoneBorders(3297, 3286, 3301, 3316)))
        emote = Emotes.JUMP_FOR_JOY
        register(EmoteClue("beehive-jump", 2703, ClueLevel.EASY, emote, arrayOf(intArrayOf(1833), intArrayOf(648), intArrayOf(1353)), "Jump for joy at the beehives.<br>Equip a desert shirt, green<br>gnome robe bottoms and a<br>steel axe.", ZoneBorders(2752, 3437, 2766, 3450)))
        register(EmoteClue("yanille-jump", 2704, ClueLevel.MEDIUM, Emotes.JUMP_FOR_JOY, Emotes.JIG, arrayOf(intArrayOf(1757), intArrayOf(1145), intArrayOf(6324)), "Jump for joy in Yanille<br>bank. Dance a jig before you<br>talk to me.<br>Equip a brown apron,<br>adamantite medium helmet<br>and snakeskin chaps.", ZoneBorders(2609, 3088, 2614, 3097)))
        register(EmoteClue("tzhaar-jump", 2705, ClueLevel.MEDIUM, Emotes.JUMP_FOR_JOY, Emotes.SHRUG, arrayOf(intArrayOf(1295), intArrayOf(2499), intArrayOf(4095)), "Jump for joy in the TzHaar<br>sword shop. Shrug before you<br>talk to me.<br>Equip a Steel longsword,<br>Blue D'hide body and blue<br>mystic gloves.", ZoneBorders(2477, 5144, 2480, 5147)))
        register(EmoteClue("jokul-tent-laugh", 2706, ClueLevel.HARD, Emotes.LAUGH, arrayOf(intArrayOf(1163), intArrayOf(2493), intArrayOf(1393)), "Laugh in the Jokul's tent in the<br>Mountain Camp.<br>Beware of double agents! Equip a<br>rune full helmet, blue dragonhide<br>chaps and a fire battlestaff.", ZoneBorders(2811, 3678, 2813, 3682)))
        emote = Emotes.PANIC
        register(EmoteClue("limestone-mine-panic", 2707, ClueLevel.EASY, emote, arrayOf(intArrayOf(1075), intArrayOf(1269), intArrayOf(1141)), "Panic in the<br>Limestone Mine.<br>Equip bronze<br>platelegs, a steel<br>pickaxe and a steel<br>medium helmet.", ZoneBorders(3368, 3496, 3374, 3505)))
        register(EmoteClue("fish-trawler-panic", 2708, ClueLevel.EASY, emote, arrayOf(), "Panic on the pier<br>where you catch the<br>Fishing trawler.<br>Have nothing equipped<br>at all when you do.", ZoneBorders(2675, 3162, 2677, 3175)))
        emote = Emotes.SALUTE
        register(EmoteClue("banana-salute", 2710, ClueLevel.HARD, emote, arrayOf(intArrayOf(1643), intArrayOf(1731)), "Salute in the banana<br>plantation. Beware of<br>double agents!<br>Equip a diamond ring,<br>amulet of power, and nothing on<br>your chest and legs.", ZoneBorders(2911, 3156, 2935, 3175)))
        emote = Emotes.SHRUG
        register(EmoteClue("rimmington-shrug", 2711, ClueLevel.EASY, emote, arrayOf(intArrayOf(1654), intArrayOf(1635), intArrayOf(1237)), "Shrug in the mine near<br>Rimmington.<br>Equip a gold<br>necklace, a gold ring<br>and a bronze spear.", ZoneBorders(2970, 3230, 2987, 3251)))
        register(EmoteClue("catherby-shrug", 2712, ClueLevel.MEDIUM, emote, Emotes.YAWN, arrayOf(intArrayOf(851), intArrayOf(1099), intArrayOf(1137)), "Shrug in Catherby<br>bank. Yawn before<br>you talk to me.<br>Equip a maple<br>longbow, green d'hide<br>chaps and an iron<br>med helm.", ZoneBorders(2806, 3438, 2812, 3442)))
        register(EmoteClue("zammy-altar-shrug", 2713, ClueLevel.HARD, emote, arrayOf(intArrayOf(1079), intArrayOf(1115), intArrayOf(2487)), "Shrug in the Zamorak<br>temple found in the<br>Eastern Wilderness.<br>Beware of double<br>agents!<br>Equip rune plate legs,<br>an iron plate body and<br>blue dragonhide<br>vambraces.", ZoneBorders(3233, 3603, 3246, 3614)))
        emote = Emotes.SPIN
        register(EmoteClue("rimmington-cross-spin", 2716, ClueLevel.EASY, emote, arrayOf(intArrayOf(658), intArrayOf(642), intArrayOf(1095)), "Spin at the crossroads<br>north of Rimmington.<br>Equip a green gnome<br>hat, cream gnome top<br>and leather chaps.", ZoneBorders(2979, 3275, 2985, 3279)))
        register(EmoteClue("draynor-manor-spin", 2719, ClueLevel.EASY, emote, arrayOf(intArrayOf(1115), intArrayOf(1097), intArrayOf(1155)), "Spin in the Draynor<br>Manor by the fountain.<br>Equip an iron<br>platebody, studded<br>leather chaps and a<br>bronze full helmet.", ZoneBorders(3085, 3332, 3090, 3337)))
        register(EmoteClue("varrock-court-spin", 2722, ClueLevel.EASY, emote, arrayOf(intArrayOf(1361), intArrayOf(1169), intArrayOf(1641)), "Spin in the Varrock<br>Castle courtyard.<br>Equip a black axe, a<br>coif and a ruby ring.", ZoneBorders(3202, 3459, 3223, 3468)))
        register(EmoteClue("barb-bridge-spin", 2723, ClueLevel.MEDIUM, emote, Emotes.SALUTE, arrayOf(intArrayOf(2942), intArrayOf(1193), intArrayOf(1159)), "Spin on the bridge by<br>the Barbarian Village.<br>Salute before you talk<br>to me.<br>Equip purple gloves, a<br>steel kiteshield and a<br>mithril full helmet.", ZoneBorders(3102, 3419, 3108, 3422)))
        emote = Emotes.THINK
        register(EmoteClue("lumb-wheat-think", 2725, ClueLevel.EASY, emote, arrayOf(intArrayOf(640), intArrayOf(654), intArrayOf(843)), "Think in middle of the<br>wheat field by the<br>lumbridge mill.<br>Equip a blue gnome<br> robetop, a turquoise<br> gnome robe bottom<br>and an oak shortbow.", ZoneBorders(3157, 3298, 3159, 3300)))
        register(EmoteClue("observatory-think", 2727, ClueLevel.MEDIUM, emote, Emotes.SPIN, arrayOf(intArrayOf(1109), intArrayOf(1099), intArrayOf(1698)), "Think in the centre of<br>the Observatory. Spin<br>before you talk to me.<br>Equip a mithril chain<br>body, green<br>dragonhide chaps and<br>a ruby amulet.", ZoneBorders(2439, 3160, 2442, 3163)))
        emote = Emotes.WAVE
        register(EmoteClue("lumber-wave", 2729, ClueLevel.EASY, emote, arrayOf(intArrayOf(1131), intArrayOf(1095), intArrayOf(1351)), "Wave along the south fence of<br>the Lumber Yard.<br>Equip a hard leather body,<br>leather chaps and a<br>bronze axe.", ZoneBorders(3306, 3490, 3309, 3492)))
        register(EmoteClue("fally-gem-wave", 2731, ClueLevel.EASY, emote, arrayOf(intArrayOf(1273), intArrayOf(1125), intArrayOf(1191)), "Wave in the Falador<br>gem store.<br>Equip a Mithril pickaxe,<br>Black platebody and<br>an Iron Kiteshield.", ZoneBorders(2944, 3332, 2946, 3337)))
        register(EmoteClue("mudskipper-wave", 2733, ClueLevel.EASY, emote, arrayOf(intArrayOf(1019), intArrayOf(1095), intArrayOf(1424)), "Wave on Mudskipper Point.<br>Equip a black cape, leather<br>chaps and a steel mace.", ZoneBorders(2979, 3105, 3008, 3115)))
        emote = Emotes.YAWN
        register(EmoteClue("varrock-library-yawn", 2735, ClueLevel.EASY, emote, arrayOf(intArrayOf(638), intArrayOf(4300), intArrayOf(1335)), "Yawn in the Varrock<br>library.<br>Equip a green gnome<br>robe top, HAM robe<br>bottom and an iron<br>warhammer.", ZoneBorders(3207, 3490, 3214, 3497), ZoneBorders(3214, 3494, 3217, 3497)))
        register(EmoteClue("draynor-market-yawn", 2737, ClueLevel.EASY, emote, arrayOf(intArrayOf(1097), intArrayOf(1191), intArrayOf(1295)), "Yawn in Draynor<br>Marketplace.<br>Equip studded leather<br>chaps, an iron<br>kiteshield and a steel<br>longsword.", ZoneBorders(3075, 3245, 3086, 3255)))
        register(EmoteClue("castle-wars-yawn", 2739, ClueLevel.MEDIUM, emote, Emotes.SHRUG, arrayOf(intArrayOf(1698), intArrayOf(1329), WILDERNESS_CAPES), "Yawn in the Castle<br>Wars lobby. Shrug<br>before you talk to me.<br>Equip ruby amulet, a<br>mithril scimitar and a<br>Wilderness cape.", ZoneBorders(2434, 3061, 2464, 3102)))
        register(EmoteClue("rogue-gen-yawn", 2741, ClueLevel.HARD, emote, arrayOf(intArrayOf(1183), intArrayOf(2487), intArrayOf(1275)), "Yawn in the rogues'<br>general store. Beware<br>of double agents!<br>Equip an adamant<br>square shield, blue<br>dragon vambraces<br>and a rune pickaxe.", ZoneBorders(3024, 3699, 3027, 3704)))
        definePlugin(UriNPC())
        return this
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any? {
        return null
    }
}
