package content.minigame.magearena.npc

import content.data.GodType
import content.minigame.magearena.MageArena
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import org.rs.consts.NPCs

class BattleMageNPC : AbstractNPC {
    private val type: GodType?

    constructor() : super(0, null) {
        this.type = null
    }

    constructor(id: Int, location: Location?) : super(id, location) {
        this.isWalks = true
        this.type = GodType.forId(id)
    }

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = BattleMageNPC(id, location)

    override fun init() {
        super.init()
        val spell = SpellBook.MODERN.getSpell(41 + type!!.ordinal) as CombatSpell?
        properties.spell = spell
        properties.autocastSpell = spell
        properties.combatPulse.style = CombatStyle.MAGIC
    }

    override fun visualize(animation: Animation?, graphics: Graphics?): Boolean {
        return super.visualize(animation, graphics)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (properties.combatPulse.isAttacking && RandomFunction.random(20) < 6) {
            sendChat("Feel the wrath of " + type?.getName() + ".")
        } else if (properties.combatPulse.isInCombat && RandomFunction.random(60) < 10) {
            sendChat("Hail " + type?.getName() + "!")
        }
    }

    override fun canSelectTarget(target: Entity): Boolean {
        if (target is Player) {
            if (type!!.isFriendly(target.asPlayer())) {
                return false
            }
            if (target.asPlayer().zoneMonitor.isInZone("mage arena")) {
                if (MageArena().hasSession(target.asPlayer())) {
                    return false
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.BATTLE_MAGE_913, NPCs.BATTLE_MAGE_912, NPCs.BATTLE_MAGE_914)
}
