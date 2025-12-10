package content.region.kandarin.seers_village.quest.elemental_workshop.npc

import content.data.skill.SkillingTool
import core.api.getTool
import core.api.sendDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import shared.consts.NPCs

@Initializable
class ElementalRockNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location),
    InteractionListener {
    private val ELEMENTAL_ROCK_TRANSFORMATION_4865 = Animation(4865)

    override fun init() {
        isWalks = false
        isNeverWalks = true
        transform(NPCs.ELEMENTAL_ROCK_4911)
        super.init()
    }

    override fun construct(
        id: Int,
        location: Location?,
        vararg objects: Any?,
    ): AbstractNPC = ElementalRockNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.EARTH_ELEMENTAL_4910, NPCs.ELEMENTAL_ROCK_4911)

    override fun defineListeners() {
        on(ids, IntType.NPC, "Mine") { player, node ->
            val tool: SkillingTool? = getTool(player, true)

            if (tool == null) {
                sendDialogue(player, "You do not have a pickaxe which you have the Mining level to use.")
                return@on true
            }

            if (player.skills.getStaticLevel(Skills.MINING) < 20) {
                sendDialogue(player, "You need a Mining level of at least 20 to mine elemental ore.")
                return@on true
            }

            pulseManager.run(
                object : Pulse() {
                    var count = 0

                    override fun pulse(): Boolean {
                        when (count) {
                            0 -> node.asNpc().animate(ELEMENTAL_ROCK_TRANSFORMATION_4865)
                            1 -> {
                                node.asNpc().sendChat("Grr... Ge'roff us!")
                                node.asNpc().transform(NPCs.EARTH_ELEMENTAL_4910)
                            }
                            3 -> {
                                node.asNpc().attack(player)
                                node.asNpc().isNeverWalks = false
                                node.asNpc().isWalks = true
                                return true
                            }
                        }
                        count++
                        return false
                    }
                },
            )
            return@on true
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        isWalks = false
        isNeverWalks = true
        transform(NPCs.ELEMENTAL_ROCK_4911)
        super.finalizeDeath(killer)
    }
}
