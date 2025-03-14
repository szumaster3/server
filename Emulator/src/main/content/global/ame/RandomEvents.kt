package content.global.ame

import content.global.ame.certer.CerterNPC
import content.global.ame.drilldemon.SergeantDamienNPC
import content.global.ame.drunkdwarf.DrunkenDwarfNPC
import content.global.ame.evilbob.EvilBobNPC
import content.global.ame.evilchicken.EvilChickenNPC
import content.global.ame.eviltwin.MollyNPC
import content.global.ame.freakyforest.FreakyForesterNPC
import content.global.ame.frogs.FrogHeraldNPC
import content.global.ame.genie.GenieNPC
import content.global.ame.lostandfound.LostAndFoundNPC
import content.global.ame.mime.MimeNPC
import content.global.ame.pillory.PilloryNPC
import content.global.ame.pinball.PinballNPC
import content.global.ame.quizmaster.QuizMasterNPC
import content.global.ame.rickturpentine.RickTurpentineNPC
import content.global.ame.rivertroll.RiverTrollNPC
import content.global.ame.rockgolem.RockGolemNPC
import content.global.ame.sandwichlady.SandwichLadyNPC
import content.global.ame.securityguard.SecurityGuardNPC
import content.global.ame.shade.ShadeNPC
import content.global.ame.strangeplant.StrangePlantNPC
import content.global.ame.surpriseexam.PatternRecognitionNPC
import content.global.ame.swarm.SwarmNPC
import content.global.ame.treespirit.TreeSpiritNPC
import content.global.ame.zombie.ZombieNPC
import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import core.game.node.entity.skill.Skills
import org.rs.consts.Items

enum class RandomEvents(
    val npc: RandomEventNPC,
    val loot: WeightBasedTable? = null,
    val skillIds: IntArray = intArrayOf(),
    val type: String = "",
) {
    SANDWICH_LADY(npc = SandwichLadyNPC()),
    GENIE(npc = GenieNPC()),
    CERTER(
        npc = CerterNPC(),
        loot =
            WeightBasedTable.create(
                WeightedItem(Items.UNCUT_SAPPHIRE_1623, 1, 1, 3.4),
                WeightedItem(Items.KEBAB_1971, 1, 1, 1.7),
                WeightedItem(Items.UNCUT_EMERALD_1621, 1, 1, 1.7),
                WeightedItem(Items.SPINACH_ROLL_1969, 1, 1, 1.5),
                WeightedItem(Items.COINS_995, 80, 80, 1.1),
                WeightedItem(Items.COINS_995, 160, 160, 1.1),
                WeightedItem(Items.COINS_995, 320, 320, 1.1),
                WeightedItem(Items.COINS_995, 480, 480, 1.1),
                WeightedItem(Items.COINS_995, 640, 640, 1.1),
                WeightedItem(Items.UNCUT_RUBY_1619, 1, 1, 0.86),
                WeightedItem(Items.COINS_995, 240, 240, 0.65),
                WeightedItem(Items.COSMIC_TALISMAN_1454, 1, 1, 0.43),
                WeightedItem(Items.UNCUT_DIAMOND_1617, 1, 1, 0.22),
                WeightedItem(Items.TOOTH_HALF_OF_A_KEY_985, 1, 1, 0.1),
                WeightedItem(Items.LOOP_HALF_OF_A_KEY_987, 1, 1, 0.1),
            ),
    ),
    DRILL_DEMON(npc = SergeantDamienNPC()),
    EVIL_CHICKEN(npc = EvilChickenNPC()),
    KISS_THE_FROG(npc = FrogHeraldNPC()),
    LOST_AND_FOUND(npc = LostAndFoundNPC(), skillIds = intArrayOf(Skills.MAGIC)),
    EVIL_BOB(npc = EvilBobNPC(), skillIds = intArrayOf(Skills.FISHING, Skills.MAGIC)),
    EVIL_TWIN(npc = MollyNPC(), skillIds = intArrayOf(Skills.MINING, Skills.MAGIC)),
    SURPRISE_EXAM(npc = PatternRecognitionNPC(), type = "sexam"),
    PILLORY(
        npc = PilloryNPC(),
        skillIds =
            intArrayOf(
                Skills.THIEVING,
                Skills.ATTACK,
                Skills.ATTACK,
                Skills.STRENGTH,
                Skills.DEFENCE,
                Skills.RANGE,
                Skills.PRAYER,
            ),
        loot = CERTER.loot,
    ),
    // MAZE(npc = MazeNPC()),

    QUIZ_MASTER(
        npc = QuizMasterNPC(),
        skillIds =
            intArrayOf(
                Skills.FISHING,
                Skills.HITPOINTS,
                Skills.PRAYER,
                Skills.MINING,
                Skills.WOODCUTTING,
            ),
    ),

    SWARM(npc = SwarmNPC()),
    SECURITY_GUARD(
        npc = SecurityGuardNPC(),
        skillIds =
            intArrayOf(
                Skills.ATTACK,
                Skills.STRENGTH,
                Skills.DEFENCE,
                Skills.RANGE,
                Skills.PRAYER,
                Skills.HITPOINTS,
                Skills.SUMMONING,
            ),
        loot = CERTER.loot,
    ),
    DRUNKEN_DWARF(
        npc = DrunkenDwarfNPC(),
        skillIds = intArrayOf(Skills.STRENGTH, Skills.DEFENCE, Skills.ATTACK, Skills.RANGE, Skills.HITPOINTS),
    ),
    MIME_EVENT(npc = MimeNPC(), skillIds = intArrayOf(Skills.PRAYER)),
    PINBALL_EVENT(npc = PinballNPC(), skillIds = intArrayOf(Skills.FISHING, Skills.WOODCUTTING), loot = CERTER.loot),
    FREAKY_FORESTER(npc = FreakyForesterNPC(), skillIds = intArrayOf(Skills.WOODCUTTING)),
    TREE_SPIRIT(npc = TreeSpiritNPC(), skillIds = intArrayOf(Skills.WOODCUTTING)),
    RIVER_TROLL(RiverTrollNPC(), skillIds = intArrayOf(Skills.FISHING)),
    STRANGE_PLANT(npc = StrangePlantNPC(), skillIds = intArrayOf(Skills.WOODCUTTING)),
    RICK_TURPENTINE(
        npc = RickTurpentineNPC(),
        skillIds =
            intArrayOf(
                Skills.STRENGTH,
                Skills.DEFENCE,
                Skills.ATTACK,
                Skills.RANGE,
                Skills.HITPOINTS,
                Skills.FISHING,
                Skills.WOODCUTTING,
            ),
        loot = CERTER.loot,
    ),
    ROCK_GOLEM(RockGolemNPC(), skillIds = intArrayOf(Skills.MINING)),
    SHADE(ShadeNPC(), skillIds = intArrayOf(Skills.PRAYER)),
    ZOMBIE(ZombieNPC(), skillIds = intArrayOf(Skills.PRAYER)),
    ;

    companion object {
        @JvmField
        val randomIDs = values().map { it.npc.id }.toList()
        val skillMap = HashMap<Int, ArrayList<RandomEvents>>()
        val nonSkillList = ArrayList<RandomEvents>()

        init {
            populateMappings()
        }

        fun getSkillBasedRandomEvent(skill: Int): RandomEvents? {
            return skillMap[skill]?.random()
        }

        fun getNonSkillRandom(): RandomEvents {
            return nonSkillList.random()
        }

        private fun populateMappings() {
            for (event in values()) {
                for (id in event.skillIds) {
                    val list = skillMap[id] ?: ArrayList<RandomEvents>().also { skillMap[id] = it }
                    list.add(event)
                }
                if (event.skillIds.isEmpty()) nonSkillList.add(event)
            }
        }
    }
}
