package content.global.skill.summoning.familiar.npc

import content.global.skill.gather.mining.MiningNode
import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import content.global.skill.summoning.familiar.Forager
import core.api.sendMessage
import core.cache.def.impl.NPCDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillBonus
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Direction
import core.game.world.map.RegionManager.getObject
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Items
import shared.consts.NPCs

@Initializable
class DesertWyrmNPC @JvmOverloads constructor(
    owner: Player? = null,
    id: Int = NPCs.DESERT_WYRM_6831
) : Forager(owner, id, 1900, Items.DESERT_WYRM_POUCH_12049, 6, WeaponInterface.STYLE_AGGRESSIVE) {

    init {
        boosts.add(SkillBonus(Skills.MINING, 1.0))
    }

    override fun construct(owner: Player, id: Int): Familiar = DesertWyrmNPC(owner, id)

    override fun getIds(): IntArray = intArrayOf(NPCs.DESERT_WYRM_6831, NPCs.DESERT_WYRM_6832)

    override fun specialMove(special: FamiliarSpecial): Boolean {
        val target = special.node as? Entity ?: return false
        if (!canCombatSpecial(target)) return false

        faceTemporary(target, 2)
        visualize(Animation(7795), Graphics(1410))
        Projectile.magic(this, target, 1411, 40, 36, 51, 10).send()
        sendFamiliarHit(target, 5)
        return true
    }

    override fun configureFamiliar() {
        definePlugin(object : OptionHandler() {
            override fun newInstance(arg: Any?): Plugin<Any> {
                ids.forEach { id ->
                    NPCDefinition.forId(id).handlers["option:burrow"] = this
                }
                return this
            }

            override fun handle(player: Player, node: Node, option: String): Boolean {
                val familiar = node as? Familiar ?: return false
                if (!player.familiarManager.isOwner(familiar) || (node as NPC).locks.isMovementLocked()) {
                    return true
                }

                val rock = findClosestRock(player)
                val resource = rock?.let { MiningNode.forId(it.id) }

                if (resource == null) {
                    sendMessage(player, "There are no rocks around here for the desert wyrm to mine from!")
                    return true
                }

                player.lock(9)
                familiar.lock(8)
                familiar.visualize(Animation(7800), Graphics(1412))

                Pulser.submit(object : Pulse(1, player, familiar) {
                    var counter = 0
                    override fun pulse(): Boolean {
                        counter++
                        when (counter) {
                            4 -> familiar.isInvisible = true
                            8 -> {
                                familiar.call()
                                GroundItemManager.create(Item(resource.reward), familiar.location, player)
                                return true
                            }
                        }
                        return false
                    }
                })

                return true
            }

            private fun findClosestRock(player: Player): Scenery? {
                val rocks = mutableListOf<Scenery>()
                for (radius in 0..6) {
                    Direction.values().forEach { dir ->
                        val loc = player.location.transform(dir.stepX * radius, dir.stepY * radius, 0)
                        val obj = getObject(loc)
                        if (obj != null && obj.name == "Rocks") {
                            rocks.add(obj)
                        }
                    }
                }

                return rocks
                    .filter { MiningNode.forId(it.id) != null }
                    .maxByOrNull { MiningNode.forId(it.id)!!.ordinal.takeIf { ord -> ord < MiningNode.SILVER_ORE_0.ordinal } ?: -1 }
            }
        })
    }
}