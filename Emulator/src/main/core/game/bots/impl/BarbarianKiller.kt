package core.game.bots.impl

import core.game.bots.CombatBotAssembler
import core.game.bots.Script
import core.game.world.map.zone.ZoneBorders

class BarbarianKiller : Script() {
    private val barbarianVillage = ZoneBorders(3091, 3405, 3071, 3450)

    var state = State.KILLING

    override fun tick() {
        when (state) {
            State.KILLING -> scriptAPI.attackNpcInRadius(bot, "Barbarian", 16)
        }
    }

    enum class State {
        KILLING,
    }

    override fun newInstance(): Script {
        val script = BarbarianKiller()
        script.bot =
            CombatBotAssembler().produce(
                CombatBotAssembler.Type.values().random(),
                CombatBotAssembler.Tier.MED,
                barbarianVillage.randomLoc,
            )
        script.state = State.KILLING
        return script
    }
}
