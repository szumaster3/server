package content.global.skill.summoning.familiar.npc

import content.global.skill.gathering.mining.MiningNode
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
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * The type Desert wyrm npc.
 */
@Initializable
class DesertWyrmNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.DESERT_WYRM_6831) :
    Forager(owner, id, 1900, Items.DESERT_WYRM_POUCH_12049, 6, WeaponInterface.STYLE_AGGRESSIVE) {
    /**
     * Instantiates a new Desert wyrm npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    /**
     * Instantiates a new Desert wyrm npc.
     */
    init {
        boosts.add(SkillBonus(Skills.MINING, 1.0))
    }

    override fun construct(owner: Player, id: Int): Familiar {
        return DesertWyrmNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        val target = special.node as Entity
        if (!canCombatSpecial(target)) {
            return false
        }
        faceTemporary(special.node as Entity, 2)
        visualize(Animation(7795), Graphics(1410))
        Projectile.magic(this, target, 1411, 40, 36, 51, 10).send()
        sendFamiliarHit(target, 5)
        return true
    }

    public override fun configureFamiliar() {
        definePlugin(object : OptionHandler() {
            @Throws(Throwable::class)
            override fun newInstance(arg: Any?): Plugin<Any> {
                for (i in ids) {
                    NPCDefinition.forId(i).handlers["option:burrow"] = this
                }
                return this
            }

            override fun handle(player: Player, node: Node, option: String): Boolean {
                val rock = getClosestRock(player)
                if (!player.familiarManager.isOwner(node as Familiar)) {
                    return true
                }
                if ((node as NPC).locks.isMovementLocked()) {
                    return true
                }
                if (rock == null) {
                    sendMessage(player, "There are no rocks around here for the desert wyrm to mine from!")
                    return true
                }
                val resource = MiningNode.forId(rock.id)
                if (resource == null) {
                    sendMessage(player, "There are no rocks around here for the desert wyrm to mine from!")
                    return true
                }
                val familiar = node
                player.lock(9)
                familiar.lock(8)
                familiar.visualize(Animation(7800), Graphics(1412))
                Pulser.submit(object : Pulse(1, player, familiar) {
                    var counter: Int = 0

                    override fun pulse(): Boolean {
                        when (++counter) {
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

            /**
             * Gets the closest combat rock.
             * @return the object.
             */
            /**
             * Gets the closest combat rock.
             * @return the object.
             */
            fun getClosestRock(player: Player): Scenery? {
                val rocks: MutableList<Scenery> = ArrayList(20)
                for (k in 0..6) {
                    for (i in 0..3) {
                        val dir = Direction.get(i)
                        val loc = player.location.transform(dir.stepX * k, dir.stepY * k, 0)
                        val `object` = getObject(loc)
                        if (`object` != null && `object`.name == "Rocks") {
                            rocks.add(`object`)
                        }
                    }
                }
                var ordinal = 0
                var o: Scenery? = null
                for (r in rocks) {
                    val resource = MiningNode.forId(r.id)
                    if (resource != null && MiningNode.SILVER_ORE_0.ordinal > resource.ordinal && resource.ordinal > ordinal) {
                        ordinal = resource.ordinal
                        o = r
                    }
                }
                return o
            }
        })
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DESERT_WYRM_6831, NPCs.DESERT_WYRM_6832)
    }
}
