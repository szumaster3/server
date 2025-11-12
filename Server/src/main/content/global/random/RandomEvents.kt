package content.global.random

import content.global.random.event.certers.CerterNPC
import content.global.random.event.drill_demon.SergeantDamienNPC
import content.global.random.event.drunkdwarf.DrunkenDwarfNPC
import content.global.random.evil_bob.EvilBobNPC
import content.global.random.evilchicken.EvilChickenNPC
import content.global.random.evil_twin.MollyNPC
import content.global.random.freaky_forester.FreakyForesterNPC
import content.global.random.event.frog_princess.FrogHeraldNPC
import content.global.random.event.genie.GenieNPC
import content.global.random.event.gravedigger.LeoNPC
import content.global.random.event.lostandfound.LostAndFoundNPC
import content.global.random.event.maze.MazeNPC
import content.global.random.event.mime.MimeNPC
import content.global.random.pinball.PinballNPC
import content.global.random.quizmaster.QuizMasterNPC
import content.global.random.rickturpentine.RickTurpentineNPC
import content.global.random.rivertroll.RiverTrollNPC
import content.global.random.rockgolem.RockGolemNPC
import content.global.random.sandwich_lady.SandwichLadyNPC
import content.global.random.securityguard.SecurityGuardNPC
import content.global.random.shade.ShadeNPC
import content.global.random.strangeplant.StrangePlantNPC
import content.global.random.surprise_exam.PatternRecognitionNPC
import content.global.random.swarm.SwarmNPC
import content.global.random.treespirit.TreeSpiritNPC
import content.global.random.zombie.ZombieNPC
import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import core.game.node.entity.skill.Skills
import shared.consts.Items

/**
 * Handles random events.
 * @author Ceikry
 */
enum class RandomEvents(
    val npc: RandomEventNPC,
    val loot: WeightBasedTable? = null,
    val skillIds: IntArray = intArrayOf(),
    val type: String = "",
) {
    SANDWICH_LADY(npc = SandwichLadyNPC()),
    GENIE(npc = GenieNPC()),
    CERTER(
        CerterNPC(),
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
    // PILLORY(npc = PilloryNPC(), loot = CERTER.loot),
    MAZE(npc = MazeNPC()),
    QUIZ_MASTER(npc = QuizMasterNPC(), skillIds = intArrayOf(Skills.FISHING, Skills.PRAYER)),
    SWARM(npc = SwarmNPC()),
    GRAVEDIGGER(npc = LeoNPC(), skillIds = intArrayOf(Skills.PRAYER)),
    SECURITY_GUARD(npc = SecurityGuardNPC(), loot = CERTER.loot),
    DRUNKEN_DWARF(npc = DrunkenDwarfNPC()),
    MIME_EVENT(npc = MimeNPC(), skillIds = intArrayOf(Skills.PRAYER)),
    PINBALL_EVENT(npc = PinballNPC(), skillIds = intArrayOf(Skills.FISHING, Skills.WOODCUTTING), loot = CERTER.loot),
    FREAKY_FORESTER(npc = FreakyForesterNPC(), skillIds = intArrayOf(Skills.WOODCUTTING)),
    TREE_SPIRIT(npc = TreeSpiritNPC(), skillIds = intArrayOf(Skills.WOODCUTTING)),
    RIVER_TROLL(RiverTrollNPC(), skillIds = intArrayOf(Skills.FISHING)),
    STRANGE_PLANT(npc = StrangePlantNPC(), skillIds = intArrayOf(Skills.WOODCUTTING)),
    RICK_TURPENTINE(RickTurpentineNPC(), CERTER.loot),
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

        fun getSkillBasedRandomEvent(skill: Int): RandomEvents? = skillMap[skill]?.random()

        fun getNonSkillRandom(): RandomEvents = nonSkillList.random()

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
