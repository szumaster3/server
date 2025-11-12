package content.global.bots

import core.game.bots.AIRepository
import core.game.bots.CombatBotAssembler
import core.game.bots.Script
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Items

class MinotaurKiller : Script() {

    companion object {
        const val MAX_ARROWS = 5000
    }

    var state = State.KILLING
    val type = CombatBotAssembler.Type.MELEE
    private var lootDelay = 0

    init {
        equipment.add(Item(Items.RUNE_FULL_HELM_1163))
        equipment.add(Item(Items.RUNE_SCIMITAR_1333))
        equipment.add(Item(Items.RUNE_CHAINBODY_1113))
        equipment.add(Item(Items.RUNE_PLATELEGS_1079))
        equipment.add(Item(Items.AMULET_OF_STRENGTH_1725))
        equipment.add(Item(Items.RUNE_KITESHIELD_1201))
        skills[Skills.ATTACK]   = (77..88).random()
        skills[Skills.STRENGTH] = (77..88).random()
        skills[Skills.DEFENCE]  = (77..88).random()
    }

    override fun tick() {
        if (!bot.isActive) state = State.STOP

        checkArrowStock()

        when (state) {
            State.KILLING    -> killingState()
            State.LOOT_DELAY -> lootDelayState()
            State.LOOTING    -> lootingState()
            State.TO_GE      -> toGeState()
            State.SELLING    -> sellState()
            State.STOP       -> running = false
        }
    }

    private fun killingState() {
        val attacked = scriptAPI.attackNpcInRadius(bot, "Minotaur", 10)
        if (attacked) state = State.LOOT_DELAY
    }

    private fun lootDelayState() {
        if (lootDelay++ >= 3) {
            lootDelay = 0
            state = State.LOOTING
        }
    }

    private fun lootingState() {
        val items = AIRepository.groundItems[bot]?.filter { it.id == Items.IRON_ARROW_884 } ?: emptyList()
        if (items.isEmpty()) {
            state = State.KILLING
            return
        }
        items.forEach { scriptAPI.takeNearestGroundItem(it.id) }
        state = if (bot.inventory.getAmount(Items.IRON_ARROW_884) >= MAX_ARROWS) {
            State.TO_GE
        } else {
            State.KILLING
        }

    }

    private fun toGeState() {
        scriptAPI.teleportToGE()
        State.SELLING
    }

    private fun sellState() {
        scriptAPI.sellOnGE(Items.IRON_ARROW_884)
        state = State.STOP
    }

    private fun checkArrowStock() {
        if (bot.inventory.getAmount(Items.IRON_ARROW_884) >= MAX_ARROWS) {
            state = State.TO_GE
        }
    }

    enum class State {
        KILLING, LOOTING, LOOT_DELAY, SELLING, TO_GE, STOP
    }

    override fun newInstance(): Script {
        val script = MinotaurKiller()
        val type = arrayOf(CombatBotAssembler.Type.MELEE, CombatBotAssembler.Type.RANGE)
        script.bot = CombatBotAssembler().produce(type.random(), CombatBotAssembler.Tier.MED, bot.startLocation)
        return script
    }
}
