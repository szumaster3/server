package content.global.handlers.iface

import core.api.closeInterface
import core.api.openInterface
import core.api.sendString
import core.game.component.Component
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.player.link.quest.*
import core.game.node.entity.skill.Skills
import core.tools.colorize
import org.rs.consts.Components
import org.rs.consts.Quests
import kotlin.math.max
import kotlin.math.min

class QuestTabInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.QUESTJOURNAL_V2_274) { player, _, _, buttonID, _, _ ->
            if (buttonID == 3) {
                player.achievementDiaryManager.openTab()
            } else {
                val quest = player.questRepository.forButtonId(buttonID)
                if (quest != null) {
                    openInterface(player, Components.QUESTJOURNAL_SCROLL_275)
                    quest.drawJournal(player, quest.getStage(player))
                } else {
                    showRequirementsInterface(player, buttonID)
                }
            }
            return@on true
        }

        on(Components.AREA_TASK_259) { player, _, _, buttonID, _, _ ->
            if (buttonID == 8) {
                player.interfaceManager.openTab(2, Component(Components.QUESTJOURNAL_V2_274))
            } else {
                player.achievementDiaryManager.getDiary(DiaryType.forChild(buttonID))?.open(player)
            }
            return@on true
        }
    }

    companion object {
        @JvmStatic
        fun showRequirementsInterface(
            player: Player,
            button: Int,
        ) {
            val questName = getNameForButton(button)
            val questReq =
                QuestRequirements.values().filter { it.questName.equals(questName, true) }.firstOrNull() ?: return
            var (isMet, unmetReqs) = QuestReq(questReq).evaluate(player)

            var messageList = ArrayList<String>()

            val statMap = HashMap<Int, Int>()
            val questList = HashSet<String>()
            var maxQpReq = 0
            var qpPenalty = 0
            closeInterface(player)
            for (req in unmetReqs) {
                if (req is QuestReq) {
                    questList.add(req.questReq.questName)
                } else if (req is SkillReq) {
                    if (statMap[req.skillId] == null ||
                        (statMap[req.skillId] != null && statMap[req.skillId]!! < req.level)
                    ) {
                        statMap[req.skillId] =
                            req.level
                    }
                } else if (req is QPReq && req.amount > maxQpReq) {
                    maxQpReq = req.amount
                } else if (req is QPCumulative) {
                    qpPenalty += req.amount
                }
            }

            messageList.add(colorize("%B[Quests Needed]"))
            messageList.addAll(questList.map { "Completion of $it" })

            messageList.add(" ")
            messageList.add(colorize("%B[Skills Needed]"))

            for ((skillId, level) in statMap) {
                val name = Skills.SKILL_NAME[skillId]
                messageList.add("$level $name")
            }

            messageList.add(" ")
            messageList.add(colorize("%B[Other Reqs]"))

            val totalQpRequirement = QPReq(min(max(maxQpReq, qpPenalty), player.questRepository.getAvailablePoints()))
            val (meetsQp, _) = totalQpRequirement.evaluate(player)
            isMet = isMet && meetsQp

            if (isMet) messageList.add(colorize("%GCongratulations! You've earned this one."))

            if (!meetsQp) messageList.add("A total of ${totalQpRequirement.amount} Quest Points.")

            messageList.add("")
            messageList.add(colorize("%BDISCLAIMER: If you're seeing this screen, this quest is not"))
            messageList.add(colorize("%Bimplemented yet. These are the requirements that you need in order"))
            messageList.add(colorize("%Bto access implemented content that would normally require this quest"))

            sendString(player, questName, Components.QUESTJOURNAL_SCROLL_275, 2)
            var lineId = 11
            for (i in 0..299) {
                val entry = messageList.elementAtOrNull(i)
                if (entry != null) {
                    sendString(player, entry, Components.QUESTJOURNAL_SCROLL_275, lineId++)
                } else {
                    sendString(player, "", Components.QUESTJOURNAL_SCROLL_275, lineId++)
                }
            }
            openInterface(player, Components.QUESTJOURNAL_SCROLL_275)
        }

        fun getNameForButton(button: Int): String {
            val name =
                when (button) {
                    10 -> Quests.MYTHS_OF_THE_WHITE_LANDS
                    11 -> Quests.MYTHS_OF_THE_WHITE_LANDS

                    13 -> Quests.BLACK_KNIGHTS_FORTRESS
                    14 -> Quests.COOKS_ASSISTANT
                    15 -> Quests.DEMON_SLAYER
                    16 -> Quests.DORICS_QUEST
                    17 -> Quests.DRAGON_SLAYER
                    18 -> Quests.ERNEST_THE_CHICKEN
                    19 -> Quests.GOBLIN_DIPLOMACY
                    20 -> Quests.IMP_CATCHER
                    21 -> Quests.THE_KNIGHTS_SWORD
                    22 -> Quests.PIRATES_TREASURE
                    23 -> Quests.PRINCE_ALI_RESCUE
                    24 -> Quests.THE_RESTLESS_GHOST
                    25 -> Quests.ROMEO_JULIET
                    26 -> Quests.RUNE_MYSTERIES
                    27 -> Quests.SHEEP_SHEARER
                    28 -> Quests.SHIELD_OF_ARRAV
                    29 -> Quests.VAMPIRE_SLAYER
                    30 -> Quests.WITCHS_POTION

                    32 -> Quests.ANIMAL_MAGNETISM
                    33 -> Quests.BETWEEN_A_ROCK
                    34 -> Quests.BIG_CHOMPY_BIRD_HUNTING
                    35 -> Quests.BIOHAZARD
                    36 -> Quests.CABIN_FEVER
                    37 -> Quests.CLOCK_TOWER
                    38 -> Quests.CONTACT
                    39 -> Quests.ZOGRE_FLESH_EATERS
                    40 -> Quests.CREATURE_OF_FENKENSTRAIN
                    41 -> Quests.DARKNESS_OF_HALLOWVALE
                    42 -> Quests.DEATH_TO_THE_DORGESHUUN
                    43 -> Quests.DEATH_PLATEAU
                    44 -> Quests.DESERT_TREASURE
                    45 -> Quests.DEVIOUS_MINDS
                    46 -> Quests.THE_DIG_SITE
                    47 -> Quests.DRUIDIC_RITUAL
                    48 -> Quests.DWARF_CANNON
                    49 -> Quests.EADGARS_RUSE
                    50 -> Quests.EAGLES_PEAK
                    51 -> Quests.ELEMENTAL_WORKSHOP_I
                    52 -> Quests.ELEMENTAL_WORKSHOP_II
                    53 -> Quests.ENAKHRAS_LAMENT
                    54 -> Quests.ENLIGHTENED_JOURNEY
                    55 -> Quests.THE_EYES_OF_GLOUPHRIE
                    56 -> Quests.FAIRYTALE_I_GROWING_PAINS
                    57 -> Quests.FAIRYTALE_II_CURE_A_QUEEN
                    58 -> Quests.FAMILY_CREST
                    59 -> Quests.THE_FEUD
                    60 -> Quests.FIGHT_ARENA
                    61 -> Quests.FISHING_CONTEST
                    62 -> Quests.FORGETTABLE_TALE
                    63 -> Quests.THE_FREMENNIK_TRIALS
                    64 -> Quests.WATERFALL_QUEST
                    65 -> Quests.GARDEN_OF_TRANQUILLITY
                    66 -> Quests.GERTRUDES_CAT
                    67 -> Quests.GHOSTS_AHOY
                    68 -> Quests.THE_GIANT_DWARF
                    69 -> Quests.THE_GOLEM
                    70 -> Quests.THE_GRAND_TREE
                    71 -> Quests.THE_HAND_IN_THE_SAND
                    72 -> Quests.HAUNTED_MINE
                    73 -> Quests.HAZEEL_CULT
                    74 -> Quests.HEROES_QUEST
                    75 -> Quests.HOLY_GRAIL
                    76 -> Quests.HORROR_FROM_THE_DEEP
                    77 -> Quests.ICTHLARINS_LITTLE_HELPER
                    78 -> Quests.IN_AID_OF_THE_MYREQUE
                    79 -> Quests.IN_SEARCH_OF_THE_MYREQUE
                    80 -> Quests.JUNGLE_POTION
                    81 -> Quests.LEGENDS_QUEST
                    82 -> Quests.LOST_CITY
                    83 -> Quests.THE_LOST_TRIBE
                    84 -> Quests.LUNAR_DIPLOMACY
                    85 -> Quests.MAKING_HISTORY
                    86 -> Quests.MERLINS_CRYSTAL
                    87 -> Quests.MONKEY_MADNESS
                    88 -> Quests.MONKS_FRIEND
                    89 -> Quests.MOUNTAIN_DAUGHTER
                    90 -> Quests.MOURNINGS_END_PART_I
                    91 -> Quests.MOURNINGS_END_PART_II
                    92 -> Quests.MURDER_MYSTERY
                    93 -> Quests.MY_ARMS_BIG_ADVENTURE
                    94 -> Quests.NATURE_SPIRIT
                    95 -> Quests.OBSERVATORY_QUEST
                    96 -> Quests.ONE_SMALL_FAVOUR
                    97 -> Quests.PLAGUE_CITY
                    98 -> Quests.PRIEST_IN_PERIL
                    99 -> Quests.RAG_AND_BONE_MAN
                    100 -> Quests.RATCATCHERS
                    101 -> Quests.RECIPE_FOR_DISASTER
                    102 -> Quests.RECRUITMENT_DRIVE
                    103 -> Quests.REGICIDE
                    104 -> Quests.ROVING_ELVES
                    105 -> Quests.ROYAL_TROUBLE
                    106 -> Quests.RUM_DEAL
                    107 -> Quests.SCORPION_CATCHER
                    108 -> Quests.SEA_SLUG
                    109 -> Quests.THE_SLUG_MENACE
                    110 -> Quests.SHADES_OF_MORTTON
                    111 -> Quests.SHADOW_OF_THE_STORM
                    112 -> Quests.SHEEP_HERDER
                    113 -> Quests.SHILO_VILLAGE
                    114 -> Quests.A_SOULS_BANE
                    115 -> Quests.SPIRITS_OF_THE_ELID
                    116 -> Quests.SWAN_SONG
                    117 -> Quests.TAI_BWO_WANNAI_TRIO
                    118 -> Quests.A_TAIL_OF_TWO_CATS
                    119 -> Quests.TEARS_OF_GUTHIX
                    120 -> Quests.TEMPLE_OF_IKOV
                    121 -> Quests.THRONE_OF_MISCELLANIA
                    122 -> Quests.THE_TOURIST_TRAP
                    123 -> Quests.WITCHS_HOUSE
                    124 -> Quests.TREE_GNOME_VILLAGE
                    125 -> Quests.TRIBAL_TOTEM
                    126 -> Quests.TROLL_ROMANCE
                    127 -> Quests.TROLL_STRONGHOLD
                    128 -> Quests.UNDERGROUND_PASS
                    129 -> Quests.WANTED
                    130 -> Quests.WATCHTOWER
                    131 -> Quests.COLD_WAR
                    132 -> Quests.THE_FREMENNIK_ISLES
                    133 -> Quests.TOWER_OF_LIFE
                    134 -> Quests.THE_GREAT_BRAIN_ROBBERY
                    135 -> Quests.WHAT_LIES_BELOW
                    136 -> Quests.OLAFS_QUEST
                    137 -> Quests.ANOTHER_SLICE_OF_HAM
                    138 -> Quests.DREAM_MENTOR
                    139 -> Quests.GRIM_TALES
                    140 -> Quests.KINGS_RANSOM
                    141 -> Quests.THE_PATH_OF_GLOUPHRIE
                    142 -> Quests.BACK_TO_MY_ROOTS
                    143 -> Quests.LAND_OF_THE_GOBLINS
                    144 -> Quests.DEALING_WITH_SCABARAS
                    145 -> Quests.WOLF_WHISTLE
                    146 -> Quests.AS_A_FIRST_RESORT
                    147 -> Quests.CATAPULT_CONSTRUCTION
                    148 -> Quests.KENNITHS_CONCERNS
                    149 -> Quests.LEGACY_OF_SEERGAZE
                    150 -> Quests.PERILS_OF_ICE_MOUNTAIN
                    151 -> Quests.TOKTZ_KET_DILL
                    152 -> Quests.SMOKING_KILLS
                    153 -> Quests.ROCKING_OUT
                    154 -> Quests.SPIRIT_OF_SUMMER
                    155 -> Quests.MEETING_HISTORY
                    156 -> Quests.ALL_FIRED_UP
                    157 -> Quests.SUMMERS_END
                    158 -> Quests.DEFENDER_OF_VARROCK
                    159 -> Quests.SWEPT_AWAY
                    160 -> Quests.WHILE_GUTHIX_SLEEPS
                    161 -> Quests.IN_PYRE_NEED
                    162 -> Quests.MYTHS_OF_THE_WHITE_LANDS
                    else -> ""
                }
            return name
        }
    }
}
