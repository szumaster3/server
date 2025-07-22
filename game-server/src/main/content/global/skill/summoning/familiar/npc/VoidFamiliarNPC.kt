package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import content.global.skill.summoning.familiar.Forager
import core.cache.def.impl.NPCDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillBonus
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class VoidFamiliarNPC : Plugin<Any?> {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any?> {
        definePlugin(VoidRavagerNPC())
        definePlugin(VoidShifterNPC())
        definePlugin(VoidSpinnerNPC())
        definePlugin(VoidTorcherNPC())
        return this
    }

    override fun fireEvent(identifier: String, vararg args: Any): Any? {
        return null
    }

    fun callToArms(familiar: Familiar, special: FamiliarSpecial?): Boolean {
        val owner = familiar.owner
        owner.lock()
        Pulser.submit(object : Pulse(1, owner) {
            var counter: Int = 0

            override fun pulse(): Boolean {
                when (++counter) {
                    1 -> owner.visualize(Animation.create(8136), Graphics.create(1503))
                    3 -> {
                        owner.unlock()
                        owner.properties.teleportLocation = Location.create(2659, 2658, 0)
                        owner.visualize(Animation.create(8137), Graphics.create(1502))
                        return true
                    }
                }
                return false
            }
        })
        return true
    }


    inner class VoidRavagerNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.VOID_RAVAGER_7370) :
        Forager(owner, id, 2700, Items.VOID_RAVAGER_POUCH_12818, 3, WeaponInterface.STYLE_AGGRESSIVE, *ITEMS) {


        init {
            boosts.add(SkillBonus(Skills.MINING, 1.0))
        }

        override fun construct(owner: Player, id: Int): Familiar {
            return VoidRavagerNPC(owner, id)
        }

        override fun specialMove(special: FamiliarSpecial): Boolean {
            return callToArms(this, special)
        }

        override fun getIds(): IntArray {
            return intArrayOf(NPCs.VOID_RAVAGER_7370, NPCs.VOID_RAVAGER_7371)
        }
    }


    inner class VoidShifterNPC
    @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.VOID_SHIFTER_7367) :
        Familiar(owner, id, 9400, Items.VOID_SHIFTER_POUCH_12814, 3, WeaponInterface.STYLE_ACCURATE) {
        override fun construct(owner: Player, id: Int): Familiar {
            return VoidShifterNPC(owner, id)
        }

        override fun adjustPlayerBattle(state: BattleState) {
            super.adjustPlayerBattle(state)
            val percentage = (owner.getSkills().getStaticLevel(Skills.HITPOINTS) * 0.10).toInt()
            if (owner.getSkills().lifepoints < percentage) {
                owner.properties.teleportLocation = Location.create(2659, 2658, 0)
            }
        }

        override fun specialMove(special: FamiliarSpecial): Boolean {
            return callToArms(this, special)
        }

        override fun getIds(): IntArray {
            return intArrayOf(NPCs.VOID_SHIFTER_7367, NPCs.VOID_SHIFTER_7368)
        }
    }


    inner class VoidSpinnerNPC
    @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.VOID_SPINNER_7333) :
        Familiar(owner, id, 2700, Items.VOID_SPINNER_POUCH_12780, 3, WeaponInterface.STYLE_DEFENSIVE) {
        private var healDelay = 0

        public override fun handleFamiliarTick() {
            super.handleFamiliarTick()
            if (healDelay < GameWorld.ticks) {
                getSkills().heal(1)
                healDelay = GameWorld.ticks + 25
            }
        }

        override fun construct(owner: Player, id: Int): Familiar {
            return VoidSpinnerNPC(owner, id)
        }

        override fun specialMove(special: FamiliarSpecial): Boolean {
            return callToArms(this, special)
        }

        override fun getIds(): IntArray {
            return intArrayOf(NPCs.VOID_SPINNER_7333, NPCs.VOID_SPINNER_7334)
        }
    }


    inner class VoidTorcherNPC
    @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.VOID_TORCHER_7351) :
        Familiar(owner, id, 9400, Items.VOID_TORCHER_POUCH_12798, 3, WeaponInterface.STYLE_CAST) {
        override fun construct(owner: Player, id: Int): Familiar {
            return VoidTorcherNPC(owner, id)
        }

        public override fun configureFamiliar() {
            definePlugin(object : OptionHandler() {
                @Throws(Throwable::class)
                override fun newInstance(arg: Any?): Plugin<Any> {
                    for (i in ids) {
                        NPCDefinition.forId(i).handlers["option:strike"] = this
                    }
                    return this
                }

                override fun handle(player: Player, node: Node, option: String): Boolean {
                    val familiar = node as Familiar
                    if (!player.familiarManager.isOwner(familiar)) {
                        return true
                    }
                    return true
                }
            })
        }

        override fun specialMove(special: FamiliarSpecial): Boolean {
            return callToArms(this, special)
        }

        override fun getIds(): IntArray {
            return intArrayOf(NPCs.VOID_TORCHER_7351, NPCs.VOID_TORCHER_7352)
        }
    }

    companion object {
        private val ITEMS = arrayOf(
            Item(Items.CLAY_434),
            Item(Items.IRON_ORE_440),
            Item(Items.COAL_453),
            Item(Items.GOLD_ORE_444),
            Item(Items.MITHRIL_ORE_447)
        )
    }
}
