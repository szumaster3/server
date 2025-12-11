package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.api.inBorders
import core.api.sendMessage
import core.game.node.entity.Entity
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.RegionManager.getLocalEntitys
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.tools.RandomFunction
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Phoenix NPC (09.01.2009).
 */
@Initializable
class PhoenixNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.PHOENIX_8575) :
    Familiar(owner, id, 3200, Items.PHOENIX_POUCH_14623, 5, WeaponInterface.STYLE_CAST) {

    override fun construct(owner: Player, id: Int): Familiar = PhoenixNPC(owner, id)

    private var randomDelay = GameWorld.ticks + 100

    public override fun handleFamiliarTick() {
        super.handleFamiliarTick()
        val player = owner ?: return
        if (randomDelay < GameWorld.ticks && RandomFunction.random(25) == 5) {
            sendChat(FAMILIAR_DIALOGUE.random())
            randomDelay = GameWorld.ticks + RandomFunction.random(30, 60)
        }
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        val player = owner ?: return false
        val node = special.node ?: return false

        val inDuelArenaBorders = inBorders(player, 3325, 3198, 3404, 3279)
        if (inDuelArenaBorders) {
            sendMessage(player, "You can't do that right now.")
            return false
        }

        if (node is GroundItem) {
            teleport(node.asItem().location)
            visualize(Animation.create(11108), /*Graphics(1983)*/ Graphics(-1))
            sendMessage(player, "The Phoenix rises from the ashes!")
            node.isRemoved = true
            GroundItemManager.destroy(node)
            // visualize(Animation.create(11078), Graphics(1983))
        }


        GameWorld.Pulser.submit(object : Pulse(4, this) {
            override fun pulse(): Boolean {
                if (node !is Entity) return true

                if (!canCombatSpecial(node)) return true

                val entities = getLocalEntitys(owner, 1)
                    .filterIsInstance<Entity>()
                    .toMutableList()
                entities.remove(this@PhoenixNPC)
                entities.remove(owner)

                if (entities.isEmpty()) return true

                val maxTargets = 5
                if (entities.size > maxTargets) return true

                val targetsToHit = entities.shuffled().take(maxTargets)

                var totalDamage = 0
                val maxTotalDamage = 49

                for (e in targetsToHit) {
                    if (!super@PhoenixNPC.canCombatSpecial(e, false)) continue
                    if (totalDamage >= maxTotalDamage) break

                    val damage = RandomFunction.random(5, 15)
                    val finalDamage = if (totalDamage + damage > maxTotalDamage) maxTotalDamage - totalDamage else damage

                    e.impactHandler.manualHit(this@PhoenixNPC, finalDamage, HitsplatType.NORMAL)
                    e.visualize(Animation.create(11104), Graphics(1984))

                    totalDamage += finalDamage
                }

                return true
            }
        })

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.PHOENIX_8575, NPCs.PHOENIX_8576)

    companion object {
        private val FAMILIAR_DIALOGUE = listOf("Skrooooooooou!", "Skreeeeeeeeeeee!")
    }
}

