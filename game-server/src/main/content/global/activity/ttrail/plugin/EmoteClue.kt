package content.global.activity.ttrail.plugin

import content.global.activity.ttrail.ClueLevel
import content.global.activity.ttrail.npc.UriNPC
import core.game.node.entity.player.link.emote.Emotes
import core.game.world.map.zone.ZoneBorders
import core.plugin.ClassScanner.definePlugin
import core.plugin.Plugin
import org.rs.consts.Items

/**
 * Represents the emote clues plugin.
 * @author Vexia
 */
class EmoteClue : EmoteScroll {

    constructor() : super(null, -1, null, null, null, null, null)

    constructor(
        name: String,
        clueId: Int,
        level: ClueLevel,
        emote: Emotes,
        commenceEmote: Emotes?,
        equipment: Array<IntArray>,
        clue: String,
        vararg borders: ZoneBorders
    ) : super(name, clueId, level, emote, commenceEmote, equipment, clue, *borders)

    constructor(
        name: String,
        clueId: Int,
        level: ClueLevel,
        emote: Emotes,
        equipment: Array<IntArray>,
        clue: String,
        vararg borders: ZoneBorders
    ) : super(name, clueId, level, emote, null, equipment, clue, *borders)

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        var emote = Emotes.BECKON
        register(EmoteClue("digsite-beckon", Items.CLUE_SCROLL_2677, ClueLevel.MEDIUM, emote, Emotes.BOW, arrayOf(intArrayOf(Items.SNAKESKIN_BOOTS_6328), intArrayOf(Items.IRON_PICKAXE_1267), intArrayOf(Items.HAT_658)), "Beckon in the Digsite, near the eastern<br>winch. Bow before you talk to me.<br>Equip a green gnome hat, snakeskin<br>boots and an iron pickaxe.", ZoneBorders(3368, 3425, 3371, 3429)))
        register(EmoteClue("tai-bwo-beckon", Items.CLUE_SCROLL_2678, ClueLevel.MEDIUM, emote, Emotes.CLAP, arrayOf(intArrayOf(Items.GREEN_DHIDE_CHAPS_1099), intArrayOf(Items.RING_OF_DUELLING8_2552, Items.RING_OF_DUELLING7_2554, Items.RING_OF_DUELLING6_2556, Items.RING_OF_DUELLING5_2558, Items.RING_OF_DUELLING4_2560, Items.RING_OF_DUELLING3_2562, Items.RING_OF_DUELLING2_2564, Items.RING_OF_DUELLING1_2566), intArrayOf(Items.MITHRIL_MED_HELM_1143)), "Beckon in Tai Bwo Wannai.<br>Clap before you talk to me.<br>Equip green<br>dragonhide chaps, a<br>ring of dueling and a mithril<br>medium helmet.", ZoneBorders(2771, 3053, 2814, 3074)))
        emote = Emotes.BLOW_KISS
        register(EmoteClue("shilo-kiss", Items.CLUE_SCROLL_2681, ClueLevel.HARD, emote, arrayOf(intArrayOf(Items.MYSTIC_HAT_4089), intArrayOf(Items.BONE_SPEAR_5016), intArrayOf(Items.RUNE_PLATEBODY_1127)), "Blow a kiss between the tables in<br>Shilo Village bank.<br>Beware of double agents!<br>Equip a blue mystic hat, bone<br>spear and rune platebody.", ZoneBorders(2849, 2950, 2855, 2957)))
        emote = Emotes.BOW
        register(EmoteClue("legends-guild", Items.CLUE_SCROLL_2682, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.IRON_PLATELEGS_1067), intArrayOf(Items.EMERALD_AMULET_1696), intArrayOf(Items.OAK_LONGBOW_845)), "Bow outside the entrance to<br>the Legends' Guild.<br>Equip iron platelegs, an<br>emerald amulet and an oak<br>longbow.", ZoneBorders(2726, 3346, 2731, 3349)))
        emote = Emotes.RASPBERRY
        register(EmoteClue("monkey-cage", Items.CLUE_SCROLL_2679, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.STUDDED_BODY_1133), intArrayOf(Items.BRONZE_PLATELEGS_1075), intArrayOf(Items.STAFF_1379)), "Blow a raspberry at the<br>monkey cage in Ardougne Zoo.<br>Equip a studded leather body, bronze<br>platelegs and a normal staff<br>with no orb.", ZoneBorders(2597, 3274, 2609, 3284)))
        register(EmoteClue("keep-le-faye", Items.CLUE_SCROLL_2680, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.COIF_1169), intArrayOf(Items.IRON_PLATEBODY_1115), intArrayOf(Items.LEATHER_GLOVES_1059)), "Blow raspberries outside the<br>entrance to Keep Le Faye.<br>Equip a coif, an iron platebody<br>and leather gloves.", ZoneBorders(2759, 3401, 2762, 3403)))
        register(EmoteClue("fishing-guild-blow", Items.CLUE_SCROLL_2683, ClueLevel.HARD, emote, arrayOf(intArrayOf(Items.ELEMENTAL_SHIELD_2890), intArrayOf(Items.BLUE_DHIDE_CHAPS_2493), intArrayOf(Items.RUNE_WARHAMMER_1347)), "Blow a raspberry in the Fishing Guild<br>bank. Beware of double agents!<br>Equip an elemental shield ,blue<br>dragonhide chaps and a rune<br>warhammer.", ZoneBorders(2586, 3418, 2590, 3422)))
        emote = Emotes.CHEER
        register(EmoteClue("druid-circle-cheer", Items.CLUE_SCROLL_2684, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.BOOTS_4310), intArrayOf(Items.WIZARD_HAT_579), intArrayOf(Items.BRONZE_2H_SWORD_1307)), "Cheer at the Druids' Circle.<br>Equip a blue wizard hat, a bronze<br>two-handed sword and HAM boots.", ZoneBorders(2920, 3478, 2930, 3940)))
        register(EmoteClue("games-room-cheer", Items.CLUE_SCROLL_2685, ClueLevel.EASY, emote, arrayOf(), "Cheer at the games room.<br>Have nothing equipped at all when<br>you do.", ZoneBorders(2194, 4946, 2221, 4973)))
        register(EmoteClue("barb-agility-cheer", Items.CLUE_SCROLL_2686, ClueLevel.MEDIUM, emote, arrayOf(intArrayOf(Items.STEEL_PLATEBODY_1119), intArrayOf(Items.MAPLE_SHORTBOW_853), WILDERNESS_CAPES), "Cheer in the Barbarian Agility Arena.<br>Headbang before you talk to me.<br>Equip a steel platebody,<br>maple shortbow, and a Wilderness<br>cape.", ZoneBorders(2550, 3556, 2553, 3559), ZoneBorders(2529, 3542, 2553, 3559)))
        register(EmoteClue("edge-gen-cheer", Items.CLUE_SCROLL_2687, ClueLevel.MEDIUM, emote, Emotes.DANCE, arrayOf(intArrayOf(Items.BROWN_APRON_1757), intArrayOf(Items.LEATHER_BOOTS_1061), intArrayOf(Items.LEATHER_GLOVES_1059)), "Cheer in the Edgeville general store.<br>Dance before you talk to me. Equip a<br>brown apron, leather boots and leather<br>gloves.", ZoneBorders(3076, 3507, 3084, 3513)))
        register(EmoteClue("ogre-pen-cheer", Items.CLUE_SCROLL_2688, ClueLevel.MEDIUM, emote, Emotes.ANGRY, arrayOf(intArrayOf(Items.GREEN_DHIDE_BODY_1135), intArrayOf(Items.GREEN_DHIDE_CHAPS_1099), intArrayOf(Items.STEEL_SQ_SHIELD_1177)), "Cheer in the Ogre Pen in the<br>Training Camp. Show you are angry<br>before you talk to me.<br>Equip a green<br>dragonhide body and<br>chaps and a steel square shield.", ZoneBorders(2523, 3373, 2533, 3377)))
        emote = Emotes.CLAP
        register(EmoteClue("exam-room-clap", Items.CLUE_SCROLL_2689, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.WHITE_APRON_1005), intArrayOf(Items.BOOTS_628), intArrayOf(Items.LEATHER_GLOVES_1059)), "Clap in the main exam room<br>in the Exam Centre.<br>Equip a white apron, green gnome<br>boots and leather gloves.", ZoneBorders(3357, 3332, 3367, 3348)))
        register(EmoteClue("wizard-tower-clap", Items.CLUE_SCROLL_2690, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.IRON_MED_HELM_1137), intArrayOf(Items.EMERALD_RING_1639), intArrayOf(Items.WHITE_APRON_1005)), "Clap on the causeway to<br>the Wizards' Tower. Equip an iron medium helmet,<br>emerald ring and a white apron.", ZoneBorders(3112, 3173, 3115, 3206)))
        register(EmoteClue("ardgoune-mill-clap", Items.CLUE_SCROLL_2691, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.ROBE_TOP_640), intArrayOf(Items.HAM_ROBE_4300), intArrayOf(Items.TIARA_5525)), "Clap on the top level of<br>the mill, north of East Ardougne.<br>Equip a blue gnome robe top, HAM<br>robe bottom and an unenchanted tiara.", ZoneBorders(2628, 3382, 2636, 3390, 2)))
        register(EmoteClue("seers-court-clap", Items.CLUE_SCROLL_2692, ClueLevel.MEDIUM, emote, Emotes.SPIN, arrayOf(intArrayOf(Items.ADAMANT_HALBERD_3200), intArrayOf(Items.MYSTIC_ROBE_BOTTOM_4093), intArrayOf(Items.DIAMOND_RING_1643)), "Clap in Seers court house.<br>Spin before you talk to me.<br>Equip an adamant halberd, blue mystic<br>robe bottom<br>and a diamond ring.", ZoneBorders(2732, 3467, 2739, 3471)))
        emote = Emotes.CRY
        register(EmoteClue("catherby-range-cry", Items.CLUE_SCROLL_2693, ClueLevel.MEDIUM, emote, Emotes.BOW, arrayOf(intArrayOf(Items.BOOTS_630), intArrayOf(Items.HARDLEATHER_BODY_1131), intArrayOf(Items.SILVER_SICKLE_2961)), "Cry in the Catherby Ranging shop.<br>Bow before you talk to me.<br>Equip blue gnome boots, a hard<br>leather body and an unblessed silver<br>sickle.", ZoneBorders(2821, 3441, 2825, 3445)))
        register(EmoteClue("catherby-shore-cry", Items.CLUE_SCROLL_2694, ClueLevel.MEDIUM, emote, Emotes.LAUGH, arrayOf(intArrayOf(Items.ADAMANT_SQ_SHIELD_1183), intArrayOf(Items.BONE_DAGGER_8872), intArrayOf(Items.MITHRIL_PLATEBODY_1121)), "Cry on the shore of Catherby<br>beach. Laugh before you talk to me,<br>equip an<br>adamant sq shield, a bone dagger and<br>mithril platebody.", ZoneBorders(2849, 3423, 2857, 3430), ZoneBorders(2845, 3428, 2852, 3430)))
        emote = Emotes.DANCE
        register(EmoteClue("draynor-cross-dance", Items.CLUE_SCROLL_2695, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.IRON_CHAINBODY_1101), intArrayOf(Items.SAPPHIRE_RING_1637), intArrayOf(Items.LONGBOW_839)), "Dance at the crossroads north<br>of Draynor.<br>Equip an iron chain body,<br>a sapphire ring and a<br>longbow.", ZoneBorders(3109, 3293, 3110, 3296), ZoneBorders(3108, 3294, 3111, 3295)))
        register(EmoteClue("fally-party-dance", Items.CLUE_SCROLL_2696, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.STEEL_FULL_HELM_1157), intArrayOf(Items.STEEL_PLATEBODY_1119), intArrayOf(Items.IRON_PLATESKIRT_1081)), "Dance in the Party Room.<br>Equip a steel full helmet, steel<br>platebody and an iron plateskirt.", ZoneBorders(3041, 3372, 3050, 3384), ZoneBorders(3051, 3371, 3054, 3385), ZoneBorders(3073, 3371, 3040, 3385)))
        register(EmoteClue("fish-guild-jig", Items.CLUE_SCROLL_2697, ClueLevel.EASY, Emotes.JIG, arrayOf(intArrayOf(Items.EMERALD_RING_1639), intArrayOf(Items.SAPPHIRE_AMULET_1694), intArrayOf(Items.BRONZE_CHAINBODY_1103)), "Dance a jig by the entrance to<br>the Fishing Guild.<br>Equip an emerald ring, a sapphire<br>amulet, and a bronze chain body.", ZoneBorders(2610, 3392, 2612, 3393)))
        register(EmoteClue("lumb-shack-dance", Items.CLUE_SCROLL_2698, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.BRONZE_DAGGER_1205), intArrayOf(Items.IRON_FULL_HELM_1153), intArrayOf(Items.GOLD_RING_1635)), "Dance in the shack in Lumbridge<br>Swamp.<br>Equip a bronze dagger, iron full<br>helmet and a gold ring.", ZoneBorders(3202, 3167, 3205, 3170)))
        register(EmoteClue("lumb-cave-dance", Items.CLUE_SCROLL_2699, ClueLevel.MEDIUM, emote, Emotes.BLOW_KISS, arrayOf(intArrayOf(Items.STAFF_OF_AIR_1381), intArrayOf(Items.BRONZE_FULL_HELM_1155), intArrayOf(Items.AMULET_OF_POWER_1731)), "Dance in the dark cave beneath<br>Lumbridge Swamp. Blow a kiss before<br>you talk to me. Equip an air staff,<br>bronze full helm and an amulet of<br>power.", ZoneBorders(3139, 9534, 3260, 9587)))
        register(EmoteClue("shantay-guild-jig", Items.CLUE_SCROLL_2700, ClueLevel.MEDIUM, Emotes.JIG, Emotes.BOW, arrayOf(intArrayOf(Items.BRUISE_BLUE_SNELM_3343), intArrayOf(Items.STAFF_OF_AIR_1381), intArrayOf(Items.BRONZE_SQ_SHIELD_1173)), "Dance a jig under Shantay's Awning.<br>Bow before you talk to me.<br>Equip a pointed blue snail helmet, an<br>air staff and a bronze square shield.", ZoneBorders(3302, 3122, 3305, 3125)))
        register(EmoteClue("cat-entrance-dance", Items.CLUE_SCROLL_2701, ClueLevel.HARD, emote, arrayOf(intArrayOf(Items.RING_OF_LIFE_2570), intArrayOf(Items.AMULET_OF_GLORY_1704), intArrayOf(Items.ADAMANT_2H_SWORD_1317)), "Dance at the cat-doored pyramid in<br>Sophanem. Beware of double agents!<br>Equip a ring of life,<br> uncharged amulet of glory and<br> adamant two-handed sword.", ZoneBorders(3293, 2781, 3296, 2782)))
        emote = Emotes.HEADBANG
        register(EmoteClue("al-kharid-headbang", Items.CLUE_SCROLL_2702, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.DESERT_SHIRT_1833), intArrayOf(Items.LEATHER_GLOVES_1059), intArrayOf(Items.LEATHER_BOOTS_1061)), "Headbang in the mine<br>north of Al Kharid.<br>Equip a desert shirt, leather<br>gloves and leather boots.", ZoneBorders(3297, 3286, 3301, 3316)))
        emote = Emotes.JUMP_FOR_JOY
        register(EmoteClue("beehive-jump", Items.CLUE_SCROLL_2703, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.DESERT_SHIRT_1833), intArrayOf(Items.ROBE_BOTTOMS_648), intArrayOf(Items.STEEL_AXE_1353)), "Jump for joy at the beehives.<br>Equip a desert shirt, green<br>gnome robe bottoms and a<br>steel axe.", ZoneBorders(2752, 3437, 2766, 3450)))
        register(EmoteClue("yanille-jump", Items.CLUE_SCROLL_2704, ClueLevel.MEDIUM, Emotes.JUMP_FOR_JOY, Emotes.JIG, arrayOf(intArrayOf(Items.BROWN_APRON_1757), intArrayOf(Items.ADAMANT_MED_HELM_1145), intArrayOf(Items.SNAKESKIN_CHAPS_6324)), "Jump for joy in Yanille bank.<br>Dance a jig before you talk to me.<br>Equip a brown apron, adamantite<br>medium helmet and snakeskin chaps.", ZoneBorders(2609, 3088, 2614, 3097)))
        register(EmoteClue("tzhaar-jump", Items.CLUE_SCROLL_2705, ClueLevel.MEDIUM, Emotes.JUMP_FOR_JOY, Emotes.SHRUG, arrayOf(intArrayOf(Items.STEEL_LONGSWORD_1295), intArrayOf(Items.BLUE_DHIDE_BODY_2499), intArrayOf(Items.MYSTIC_GLOVES_4095)), "Jump for joy in the TzHaar sword<br>shop.<br>Shrug before you talk to me.<br>Equip a Steel longsword,<br>Blue D'hide<br>body and blue mystic gloves.", ZoneBorders(2477, 5144, 2480, 5147)))
        register(EmoteClue("jokul-tent-laugh", Items.CLUE_SCROLL_2706, ClueLevel.HARD, Emotes.LAUGH, arrayOf(intArrayOf(Items.RUNE_FULL_HELM_1163), intArrayOf(Items.BLUE_DHIDE_CHAPS_2493), intArrayOf(Items.FIRE_BATTLESTAFF_1393)), "Laugh in the Jokul's tent in the<br>Mountain Camp. Beware of double agents! Equip<br>a rune full helmet, blue dragonhide<br>chaps and a fire battlestaff.", ZoneBorders(2811, 3678, 2813, 3682)))
        emote = Emotes.PANIC
        register(EmoteClue("limestone-mine-panic", Items.CLUE_SCROLL_2707, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.BRONZE_PLATELEGS_1075), intArrayOf(Items.STEEL_PICKAXE_1269), intArrayOf(Items.STEEL_MED_HELM_1141)), "Panic in the Limestone Mine.<br>Equip bronze platelegs, a<br>steel pickaxe and a steel<br>medium helmet.", ZoneBorders(3368, 3496, 3374, 3505)))
        register(EmoteClue("fish-trawler-panic", Items.CLUE_SCROLL_2708, ClueLevel.EASY, emote, arrayOf(), "Panic on the pier where<br>you catch the Fishing trawler.<br>Have nothing equipped at all when<br>you do.", ZoneBorders(2675, 3162, 2677, 3175)))
        emote = Emotes.SALUTE
        register(EmoteClue("banana-salute", Items.CLUE_SCROLL_2710, ClueLevel.HARD, emote, arrayOf(intArrayOf(Items.DIAMOND_RING_1643), intArrayOf(Items.AMULET_OF_POWER_1731)), "Salute in the banana plantation.<br>Beware of double agents!<br>Equip a diamond ring, amulet of<br>power, and nothing on your chest<br>and legs.", ZoneBorders(2911, 3156, 2935, 3175)))
        emote = Emotes.SHRUG
        register(EmoteClue("rimmington-shrug", Items.CLUE_SCROLL_2711, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.GOLD_NECKLACE_1654), intArrayOf(Items.GOLD_RING_1635), intArrayOf(Items.BRONZE_SPEAR_1237)), "Shrug in the mine near Rimmington.<br>Equip a gold necklace,<br>a gold ring and a bronze<br>spear.", ZoneBorders(2970, 3230, 2987, 3251)))
        register(EmoteClue("catherby-shrug", Items.CLUE_SCROLL_2712, ClueLevel.MEDIUM, emote, Emotes.YAWN, arrayOf(intArrayOf(Items.MAPLE_LONGBOW_851), intArrayOf(Items.GREEN_DHIDE_CHAPS_1099), intArrayOf(Items.IRON_MED_HELM_1137)), "Shrug in Catherby bank.<br>Yawn before you talk to me.<br>Equip a maple longbow, green d'hide<br>chaps<br>and an iron med helm.", ZoneBorders(2806, 3438, 2812, 3442)))
        register(EmoteClue("zammy-altar-shrug", Items.CLUE_SCROLL_2713, ClueLevel.HARD, emote, arrayOf(intArrayOf(Items.RUNE_PLATELEGS_1079), intArrayOf(Items.IRON_PLATEBODY_1115), intArrayOf(Items.BLUE_DHIDE_VAMB_2487)), "Shrug in the Zamorak temple found<br>in the Eastern Wilderness.<br>Beware of double agents!<br>Equip rune plate legs, an iron<br>platebody and blue dragonhide<br>vambraces.", ZoneBorders(3233, 3603, 3246, 3614)))
        emote = Emotes.SPIN
        register(EmoteClue("rimmington-cross-spin", Items.CLUE_SCROLL_2716, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.HAT_658), intArrayOf(Items.ROBE_TOP_642), intArrayOf(Items.LEATHER_CHAPS_1095)), "Spin at the crossroads north<br>of Rimmington.<br>Equip a green gnome hat, cream<br>gnome top and leather chaps.", ZoneBorders(2979, 3275, 2985, 3279)))
        register(EmoteClue("draynor-manor-spin", Items.CLUE_SCROLL_2719, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.IRON_PLATEBODY_1115), intArrayOf(Items.STUDDED_CHAPS_1097), intArrayOf(Items.BRONZE_FULL_HELM_1155)), "Spin in the Draynor Manor by<br>the fountain.<br>Equip an iron platebody, studded<br>leather chaps and a bronze<br>full helmet.", ZoneBorders(3085, 3332, 3090, 3337)))
        register(EmoteClue("varrock-court-spin", Items.CLUE_SCROLL_2722, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.BLACK_AXE_1361), intArrayOf(Items.COIF_1169), intArrayOf(Items.RUBY_RING_1641)), "Spin in the Varrock<br>Castle courtyard. Equip a black axe, a coif<br>and a ruby ring.", ZoneBorders(3202, 3459, 3223, 3468)))
        register(EmoteClue("barb-bridge-spin", Items.CLUE_SCROLL_2723, ClueLevel.MEDIUM, emote, Emotes.SALUTE, arrayOf(intArrayOf(Items.GLOVES_2942), intArrayOf(Items.STEEL_KITESHIELD_1193), intArrayOf(Items.MITHRIL_FULL_HELM_1159)), "Spin on the bridge by the Barbarian<br>Village. Salute before you talk to me.<br>Equip purple gloves,<br>a steel kiteshield and a mithril<br>full helmet.", ZoneBorders(3102, 3419, 3108, 3422)))
        emote = Emotes.THINK
        register(EmoteClue("lumb-wheat-think", Items.CLUE_SCROLL_2725, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.ROBE_TOP_640), intArrayOf(Items.ROBE_BOTTOMS_654), intArrayOf(Items.OAK_SHORTBOW_843)), "Think in middle of the wheat<br>field by the Lumbridge mill.<br>Equip a blue gnome robetop,<br>a turquoise gnome robe bottom and<br>an oak shortbow.", ZoneBorders(3157, 3298, 3159, 3300)))
        register(EmoteClue("observatory-think", Items.CLUE_SCROLL_2727, ClueLevel.MEDIUM, emote, Emotes.SPIN, arrayOf(intArrayOf(Items.MITHRIL_CHAINBODY_1109), intArrayOf(Items.GREEN_DHIDE_CHAPS_1099), intArrayOf(Items.RUBY_AMULET_1698)), "Think in the centre of the<br>Observatory.<br>Spin before you talk to me.<br>Equip a mithril chain body, green<br>dragonhide chaps and a ruby amulet.", ZoneBorders(2439, 3160, 2442, 3163)))
        emote = Emotes.WAVE
        register(EmoteClue("lumber-wave", Items.CLUE_SCROLL_2729, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.HARDLEATHER_BODY_1131), intArrayOf(Items.LEATHER_CHAPS_1095), intArrayOf(Items.BRONZE_AXE_1351)), "Wave along the south fence<br>of the Lumber Yard.<br>Equip a hard leather body,<br>leather chaps and a bronze axe.", ZoneBorders(3306, 3490, 3309, 3492)))
        register(EmoteClue("fally-gem-wave", Items.CLUE_SCROLL_2731, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.MITHRIL_PICKAXE_1273), intArrayOf(Items.BLACK_PLATEBODY_1125), intArrayOf(Items.IRON_KITESHIELD_1191)), "Wave in the<br>Falador gem store.<br>Equip a Mithril pickaxe, Black<br>platebody and an Iron Kiteshield.", ZoneBorders(2944, 3332, 2946, 3337)))
        register(EmoteClue("mudskipper-wave", Items.CLUE_SCROLL_2733, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.BLACK_CAPE_1019), intArrayOf(Items.LEATHER_CHAPS_1095), intArrayOf(Items.STEEL_MACE_1424)), "Wave on Mudskipper Point.<br>Equip a black cape, leather<br>chaps and a steel mace.", ZoneBorders(2979, 3105, 3008, 3115)))
        emote = Emotes.YAWN
        register(EmoteClue("varrock-library-yawn", Items.CLUE_SCROLL_2735, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.ROBE_TOP_638), intArrayOf(Items.HAM_ROBE_4300), intArrayOf(Items.IRON_WARHAMMER_1335)), "Yawn in the Varrock library.<br>Equip a green gnome robe top, HAM<br>robe bottom and an iron<br>warhammer.", ZoneBorders(3207, 3490, 3214, 3497), ZoneBorders(3214, 3494, 3217, 3497)))
        register(EmoteClue("draynor-market-yawn", Items.CLUE_SCROLL_2737, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.STUDDED_CHAPS_1097), intArrayOf(Items.IRON_KITESHIELD_1191), intArrayOf(Items.STEEL_LONGSWORD_1295)), "Yawn in Draynor Marketplace.<br>Equip studded leather chaps, an iron<br>kiteshield and a steel longsword.", ZoneBorders(3075, 3245, 3086, 3255)))
        register(EmoteClue("castle-wars-yawn", Items.CLUE_SCROLL_2739, ClueLevel.MEDIUM, emote, Emotes.SHRUG, arrayOf(intArrayOf(Items.RUBY_AMULET_1698), intArrayOf(Items.MITHRIL_SCIMITAR_1329), WILDERNESS_CAPES), "Yawn in the Castle Wars lobby.<br>Shrug before you talk to me.<br>Equip ruby amulet, a mithril<br>scimitar and a Wilderness cape.", ZoneBorders(2434, 3061, 2464, 3102)))
        register(EmoteClue("rogue-gen-yawn", Items.CLUE_SCROLL_2741, ClueLevel.HARD, emote, arrayOf(intArrayOf(Items.ADAMANT_SQ_SHIELD_1183), intArrayOf(Items.BLUE_DHIDE_VAMB_2487), intArrayOf(Items.RUNE_PICKAXE_1275)), "Yawn in the rogues' general store.<br>Beware of double agents!<br>Equip an adamant square shield,<br>blue dragon vambraces and a rune<br>pickaxe.", ZoneBorders(3024, 3699, 3027, 3704)))
        definePlugin(UriNPC())
        return this
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any? {
        return null
    }
}
