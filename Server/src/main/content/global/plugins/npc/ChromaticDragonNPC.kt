package content.global.plugins.npc

import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.MultiSwingHandler
import core.game.node.entity.combat.equipment.SwitchAttack
import core.game.node.entity.combat.equipment.special.DragonfireSwingHandler
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.tools.RandomFunction
import shared.consts.Items
import shared.consts.NPCs

@Initializable
class ChromaticDragonNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    companion object {
        private val DRAGONFIRE: SwitchAttack = DragonfireSwingHandler.get(true, 52, Animation(81, Priority.HIGH), Graphics(1, 64), null, null)
    }

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = ChromaticDragonNPC(id, location)

    private val combatAction: CombatSwingHandler =
        MultiSwingHandler(
            false,
            SwitchAttack(CombatStyle.MELEE.swingHandler, Animation(80, Priority.HIGH)),
            SwitchAttack(CombatStyle.MELEE.swingHandler, Animation(91, Priority.HIGH)),
            DRAGONFIRE
        )

    override fun getSwingHandler(swing: Boolean): CombatSwingHandler = combatAction

    override fun getDragonfireProtection(fire: Boolean): Int = 0x2 or 0x4 or 0x8

    override fun getIds(): IntArray =
        intArrayOf(
            // Green dragons.
            NPCs.GREEN_DRAGON_941,
            NPCs.GREEN_DRAGON_4677,
            NPCs.GREEN_DRAGON_4678,
            NPCs.GREEN_DRAGON_4679,
            NPCs.GREEN_DRAGON_4680,
            // Red dragons.
            NPCs.RED_DRAGON_53,
            NPCs.RED_DRAGON_4669,
            NPCs.RED_DRAGON_4670,
            NPCs.RED_DRAGON_4671,
            NPCs.RED_DRAGON_4672,
            // Black dragons.
            NPCs.BLACK_DRAGON_54,
            NPCs.BLACK_DRAGON_4673,
            NPCs.BLACK_DRAGON_4674,
            NPCs.BLACK_DRAGON_4675,
            NPCs.BLACK_DRAGON_4676,
            // Blue dragons.
            NPCs.BLUE_DRAGON_55,
            NPCs.BLUE_DRAGON_4681,
            NPCs.BLUE_DRAGON_4682,
            NPCs.BLUE_DRAGON_4683,
            NPCs.BLUE_DRAGON_4684,
        )

    class ChromaticDragonBehavior : NPCBehavior(*greenDragons, *blueDragons, *redDragons, *blackDragons) {

        override fun onDropTableRolled(self: NPC, killer: Entity, drops: ArrayList<Item>) {
            val removeList = ArrayList<Item>()
            for (item in drops) {
                when (item.id) {
                    Items.BLACK_DRAGON_EGG_12480,
                    Items.RED_DRAGON_EGG_12477,
                    Items.BLUE_DRAGON_EGG_12478,
                    Items.GREEN_DRAGON_EGG_12479,
                    -> removeList.add(item)
                }
            }
            drops.removeAll(removeList)

            if (killer.skills.getStaticLevel(Skills.SUMMONING) >= 99 && RandomFunction.roll(EGG_RATE)) {
                drops.add(
                    when (self.id) {
                        in greenDragons -> Item(Items.GREEN_DRAGON_EGG_12479)
                        in blueDragons -> Item(Items.BLUE_DRAGON_EGG_12478)
                        in redDragons -> Item(Items.RED_DRAGON_EGG_12477)
                        in blackDragons -> Item(Items.BLACK_DRAGON_EGG_12480)
                        else -> Item(Items.DRAGON_BONES_536)
                    },
                )
            }
        }

        companion object {
            val greenDragons = intArrayOf(
                NPCs.GREEN_DRAGON_941,
                NPCs.GREEN_DRAGON_4677,
                NPCs.GREEN_DRAGON_4678,
                NPCs.GREEN_DRAGON_4679,
                NPCs.GREEN_DRAGON_4680,
            )
            val blueDragons = intArrayOf(
                NPCs.BLUE_DRAGON_55,
                NPCs.BLUE_DRAGON_4681,
                NPCs.BLUE_DRAGON_4682,
                NPCs.BLUE_DRAGON_4683,
                NPCs.BLUE_DRAGON_4684,
            )
            val redDragons = intArrayOf(
                NPCs.RED_DRAGON_53,
                NPCs.RED_DRAGON_4669,
                NPCs.RED_DRAGON_4670,
                NPCs.RED_DRAGON_4671,
                NPCs.RED_DRAGON_4672,
            )
            val blackDragons = intArrayOf(
                NPCs.BLACK_DRAGON_54,
                NPCs.BLACK_DRAGON_4673,
                NPCs.BLACK_DRAGON_4674,
                NPCs.BLACK_DRAGON_4675,
                NPCs.BLACK_DRAGON_4676,
            )
            var EGG_RATE = 1000
        }
    }
}

