package content.region.asgarnia.taverley.quest.ball.plugin

import content.minigame.mta.plugin.TelekineticGrabSpell
import content.region.asgarnia.taverley.quest.ball.npc.WitchExperimentNPC
import core.api.inInventory
import core.game.interaction.MovementPulse
import core.game.interaction.Option
import core.game.interaction.PluginInteraction
import core.game.interaction.PluginInteractionManager
import core.game.node.entity.combat.spell.SpellBlocks.register
import core.game.node.entity.impl.PulseType
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items
import java.util.*

/**
 * Represents interaction with Ball.
 * @author Ethan Kyle Millard (March 25, 2020, 8:28 PM)
 */
@Initializable
class BallPlugin : PluginInteraction() {
    private var handled = false

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        register(TelekineticGrabSpell.SPELL_ID, Item(Items.BALL_2407))
        setIds(intArrayOf(Items.BALL_2407))
        PluginInteractionManager.register(this, PluginInteractionManager.InteractionType.ITEM)
        return this
    }

    override fun handle(
        player: Player,
        item: Item,
        option: Option,
    ): Boolean {
        if (option.name.lowercase(Locale.getDefault()) == "take") {
            player.pulseManager.run(
                object : MovementPulse(player, item.location.transform(0, -1, 0)) {
                    override fun pulse(): Boolean = true
                },
                PulseType.STANDARD,
            )
            handleBall(player)
        }
        return handled
    }

    fun handleBall(player: Player) {
        if (player.getAttribute<Boolean>("witchs_house:experiment_killed", false)) {
            if (inInventory(player, Items.BALL_2407)) {
                player.sendMessage("You already have the ball.")
                handled = true
            } else {
                handled = false
            }
        } else {
            if (player.getAttribute("witchs-experiment:npc_spawned", false)) {
                player.sendMessage("Finish fighting the experiment first!")
                return
            }
            val skillsToDecrease =
                intArrayOf(Skills.DEFENCE, Skills.ATTACK, Skills.STRENGTH, Skills.RANGE, Skills.MAGIC)
            for (i in skillsToDecrease.indices) {
                player
                    .getSkills()
                    .setLevel(i, if (player.getSkills().getLevel(i) > 5) player.getSkills().getLevel(i) - 5 else 1)
            }
            player.packetDispatch.sendMessage(
                "<col=ff0000>The experiment glares at you, and you feel yourself weaken.</col>",
            )
            WitchExperimentNPC(
                player.getAttribute("witchs_house:experiment_id", 897),
                Location.create(2936, 3463, 0),
                player,
            ).init()
            player.setAttribute("witchs-experiment:npc_spawned", true)
            handled = true
        }
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any,
    ): Any? = null
}
