package content.region.island.tutorial.npc

import content.data.GameAttributes
import core.api.getAttribute
import core.api.sendNPCDialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.world.map.Location
import core.plugin.Initializable
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

@Initializable
class GiantRatNPC : AbstractNPC {
    constructor() : super(0, null) {
        this.isAggressive = false
    }

    private constructor(id: Int, location: Location) : super(id, location, true)

    override fun init() {
        super.init()
        isAggressive = false
        getSkills().setLevel(Skills.HITPOINTS, 3)
        getSkills().setStaticLevel(Skills.HITPOINTS, 3)
        getSkills().lifepoints = 3
    }

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = GiantRatNPC(id, location)

    override fun tick() {
        super.tick()
    }

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        if (killer !is Player) {
            return
        }
        val player = killer
        val tutorialStage = GameAttributes.TUTORIAL_STAGE
        if (killer is Player) {
            if (player.getQuestRepository().getQuest(Quests.WITCHS_POTION).isStarted(player)) {
                GroundItemManager.create(Item(Items.RATS_TAIL_300), getLocation(), player)
            }
            if (getAttribute(player, tutorialStage, -1) < 50) {
                sendNPCDialogue(
                    player,
                    NPCs.COMBAT_INSTRUCTOR_944,
                    "I admire your thirst for violence! Let's have a chat before you kill another one, though, eh?",
                    FaceAnim.ANNOYED,
                )
            }
        }
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.GIANT_RAT_86, NPCs.GIANT_RAT_950)
}
