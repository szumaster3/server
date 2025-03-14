package core.game.bots.impl

import core.ServerConstants
import core.game.bots.Script
import core.game.interaction.DestinationFlag
import core.game.interaction.IntType
import core.game.interaction.InteractionListeners
import core.game.interaction.MovementPulse
import core.game.node.item.Item
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.Items

class ManThiever : Script() {
    private var state = State.INIT
    private val zone = ZoneBorders(3240, 3195, 3227, 3227)

    init {
        equipment.addAll(
            listOf(
                Item(Items.BRONZE_CHAINBODY_1103),
                Item(Items.IRON_WARHAMMER_1335),
                Item(Items.WOODEN_SHIELD_1171),
            ),
        )
    }

    override fun tick() {
        when (state) {
            State.INIT -> handleInitState()
            State.RESPAWN -> handleRespawnState()
        }
    }

    private fun handleInitState() {
        if (!zone.insideBorder(bot)) {
            scriptAPI?.walkTo(zone.randomLoc)
            return
        }

        val man = scriptAPI?.getNearestNode("Man", false) ?: return
        val destinationReached =
            bot
                ?.destinationFlag
                ?.getDestination(bot, man)
                ?.withinDistance(man.location) == true

        if (bot?.skills?.lifepoints ?: 0 <= 0) {
            bot?.startDeath(man.asNpc())
            state = State.RESPAWN
            return
        }

        bot?.interfaceManager?.close()

        if (!destinationReached) {
            bot?.pulseManager?.run(
                object : MovementPulse(bot, man.asNpc().location, DestinationFlag.ENTITY) {
                    override fun pulse(): Boolean {
                        InteractionListeners.run(man.id, IntType.NPC, "Pickpocket", bot!!, man)
                        return true
                    }
                },
            )
        } else {
            InteractionListeners.run(man.id, IntType.NPC, "Pickpocket", bot!!, man)
        }
    }

    private fun handleRespawnState() {
        bot?.walkingQueue?.reset()
        bot?.teleport(ServerConstants.HOME_LOCATION)
        state = State.INIT
    }

    override fun newInstance(): Script = ManThiever()

    enum class State {
        INIT,
        RESPAWN,
    }
}
