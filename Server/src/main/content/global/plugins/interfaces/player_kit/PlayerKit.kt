package content.global.plugins.interfaces.player_kit

import core.game.node.item.Item
import shared.consts.Components
import shared.consts.Items

object PlayerKit {
    /* ╔════════════════════════════════════════════╗
     * ║ GLOBAL ATTRIBUTES                          ║
     * ╚════════════════════════════════════════════╝ */
    val PLAYER_KIT_PAID_ATTRIBUTE             = "player_kit:paid"
    val PLAYER_KIT_TYPE_ATTRIBUTE             = "player_kit:type"
    val PLAYER_KIT_GENDER_SAVE_ATTIBUTE       = "player_kit:gender"
    val PLAYER_KIT_SKIN_COLOR_SAVE_ATTRIBUTE  = "player_kit:skin-color"
    val PLAYER_KIT_WRIST_SAVE_ATTRIBUTE       = "player_kit:wrists"
    val PLAYER_KIT_HAIR_SAVE_ATTRIBUTE        = "player_kit:hair"
    val PLAYER_KIT_HAIR_COLOR_SAVE_ATTRIBUTE  = "player_kit:hair-color"
    val PLAYER_KIT_BEARD_SAVE_ATTRIBUTE       = "player_kit:beard"
    val PLAYER_KIT_BEARD_SETTINGS_ATTRIBUTE   = "player_kit:beard-setting"
    val PLAYER_KIT_TORSO_SAVE_ATTRIBUTE       = "player_kit:torso"
    val PLAYER_KIT_TORSO_COLOR_SAVE_ATTRIBUTE = "player_kit:torso-color"
    val PLAYER_KIT_ARMS_SAVE_ATTRIBUTE        = "player_kit:arms"
    val PLAYER_KIT_ARMS_COLOR_SAVE_ATTRIBUTE  = "player_kit:arms-color"
    val PLAYER_KIT_LEGS_SAVE_ATTRIBUTE        = "player_kit:legs"
    val PLAYER_KIT_LEGS_COLOR_SAVE_ATTRIBUTE  = "player_kit:legs-color"
    val PLAYER_KIT_FEET_SAVE_ATTRIBUTE        = "player_kit:feet"
    /* ╔════════════════════════════════════════════╗
     * ║ INTERFACE IDS                              ║
     * ╚════════════════════════════════════════════╝ */
    const val YRSA_SHOE_STORE_INTERFACE_ID    = Components.YRSA_SHOE_STORE_200     // Feet appearance.
    const val MAKEOVER_MAGE_INTERFACE_ID      = Components.MAKEOVER_MAGE_205   // Skin & gender appearance.
    const val REINALD_BRACELETS_INTERFACE_ID  = Components.REINALD_SMITHING_EMPORIUM_593   // Wrists appearance.
    const val THESSALIA_MALE_INTERFACE_ID     = Components.THESSALIA_CLOTHES_MALE_591  // Clothes (male) appearance.
    const val THESSALIA_FEMALE_INTERFACE_ID   = Components.THESSALIA_CLOTHES_FEMALE_594    // Clothes (female) appearance.
    const val HAIRDRESSER_MALE_INTERFACE_ID   = Components.HAIRDRESSER_MALE_596// Hair (male) appearance.
    const val HAIRDRESSER_FEMALE_INTERFACE_ID = Components.HAIRDRESSER_FEMALE_592// Hair (female) appearance.
    /* ╔════════════════════════════════════════════╗
     * ║ PRICES                                     ║
     * ╚════════════════════════════════════════════╝ */
    val WRISTS_CHANGE_PRICE = Item(Items.COINS_995, 500)
    val FEET_CHANGE_PRICE = Item(Items.COINS_995, 500)
    val CLOTHES_PRICE = Item(Items.COINS_995, 1000)
    val HAIR_CHANGE_PRICE = Item(Items.COINS_995, 2000)
    val MAKEOVER_PRICE = Item(Items.COINS_995, 3000)
    val MAKEOVER_VOUCHER = Item(Items.MAKEOVER_VOUCHER_5606, 1)

    /* ╔════════════════════════════════════════════╗
     * ║ YRSA SHOE STORE INTERFACE COMPONENT IDS    ║
     * ╚════════════════════════════════════════════╝ */
    val YRSA_SELECT_BUTTONS_COMPONENT_IDS = intArrayOf(15,16,17,18,19,20)
    val YRSA_COLOR_BUTTONS_COMPONENT_IDS  = intArrayOf(0,1,2,3,4,5)
    val YRSA_FEET_MODEL_IDS = intArrayOf(3680,3681,3682,3683,3684,3685)// Pictures

    /* ╔════════════════════════════════════════════╗
     * ║ MAKEOVER MAGE INTERFACE COMPONENT IDS      ║
     * ╚════════════════════════════════════════════╝ */
    const val MAKEOVER_MODEL_MALE_COMPONENT_ID    = 90
    const val MAKEOVER_MODEL_FEMALE_COMPONENT_ID  = 92
    const val MAKEOVER_TEXT_COMPONENT_ID          = 88
    val SKIN_COLOR_BUTTON_COMPONENT_IDS     = 93..100

    /* ╔════════════════════════════════════════════╗
     * ║ THESSALIA CLOTHES INTERFACE COMPONENT IDS  ║
     * ╚════════════════════════════════════════════╝ */
    const val CLOTHES_DISPLAY_COMPONENT_ID = 59
    val FEMALE_TORSO_BUTTONS_COMPONENT_IDS = 186..196 ; val MALE_TORSO_BUTTONS_COMPONENT_IDS = 185..198
    val FEMALE_ARMS_BUTTONS_COMPONENT_IDS  = 197..207 ; val MALE_ARMS_BUTTONS_COMPONENT_IDS  = 199..210
    val FEMALE_LEGS_BUTTONS_COMPONENT_IDS  = 208..222 ; val MALE_LEGS_BUTTONS_COMPONENT_IDS  = 211..221
    val FEMALE_COLOR_BUTTONS_COMPONENT_IDS = 253..281 ; val MALE_COLOR_BUTTONS_COMPONENT_IDS = 252..280

    val MALE_TORSO_IDS =
        intArrayOf(111,113,114,115,21,116,18,19,20,112,24,23,24,25)
    val MALE_SLEEVE_IDS =
        intArrayOf(107,108,29,106,110,109,28,26,27,105,30,31)
    val MALE_LEG_IDS =
        intArrayOf(36,85,37,40,89,90,86,88,39,38,87)
    val FEMALE_TORSO_IDS =
        intArrayOf(153,155,156,157,154,158,56,57,58,59,60)
    val FEMALE_ARMS_IDS =
        intArrayOf(149,150,65,148,151,152,64,61,63,147,62)
    val FEMALE_LEG_IDS =
        intArrayOf(129,130,128,74,133,134,71,131,132,75,73,76)

    val LEGS_BUTTON_COLOR_COMPONENT_IDS =
        intArrayOf(24,23,3,22,13,12,7,19,5,1,10,14,25,9,0,26,21,8,20,15,11,28,27,4,6,18,17,2,16)
    val TORSO_BUTTON_COLOR_COMPONENT_IDS =
        intArrayOf(24,23,2,22,12,11,6,19,4,0,9,13,25,8,15,26,21,7,20,14,10,28,27,3,5,18,17,1,16)

    enum class ColorType { TORSO, ARMS, LEGS }

    /* ╔════════════════════════════════════════════╗
     * ║ HAIRDRESSER INTERFACE COMPONENT IDS        ║
     * ╚════════════════════════════════════════════╝ */
    val maleColorButtonRange = 229..253
    val femaleColorButtonRange = 73..97
    val maleStyleButtonRange = 65..90
    val femaleStyleButtonRange = 148..181

    private val FEMALE_HAIR_STYLES =
        intArrayOf(45,46,47,48,49,50,51,52,53,54,135,136,137,138,139,140,141,142,143,144,145,242,269,270,271,272,273,274,275,276,277,278,279,280)
    private val MALE_HAIR_STYLES =
        intArrayOf(0,1,2,3,4,5,6,7,8,91,92,93,94,95,96,97,246,262,251,265,252,257,247,253)
    private val MALE_FACIAL_STYLES =
        intArrayOf(10,11,12,13,14,15,16,17,98,99,100,101,102,103,104,305,306,307,308)
    val HAIR_COLORS =
        intArrayOf(20,19,10,18,4,5,15,7,0,6,21,9,22,17,8,16,11,24,23,3,2,1,14,13,12)

    // Full arrays.
    val MALE_HAIR   = MALE_HAIR_STYLES
    val FEMALE_HAIR = FEMALE_HAIR_STYLES
    val MALE_FACIAL = MALE_FACIAL_STYLES

    /* ╔════════════════════════════════════════════╗
     * ║ REINALD BRACELETS INTERFACE COMPONENT IDS  ║
     * ╚════════════════════════════════════════════╝ */
    const val BRACELET_PREVIEW_COMPONENT_ID = 69
    val WRISTS_MODELS = mapOf(
        122 to 0,
        123 to 27703,
        124 to 27704,
        125 to 27706,
        126 to 27707,
        127 to 27697,
        128 to 27699,
        129 to 0,
        130 to 27698,
        131 to 27708,
        132 to 27702,
        133 to 27705,
        134 to 27700,
        135 to 27709
    )
}