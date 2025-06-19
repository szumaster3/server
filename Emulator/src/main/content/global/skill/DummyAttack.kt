package content.global.skill

import core.api.sendMessage
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Scenery

@Initializable
class DummyAttack : OptionHandler() {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        for (i in intArrayOf(Scenery.DUMMY_2038, Scenery.DUMMY_15624, Scenery.DUMMY_15625, Scenery.DUMMY_15626, Scenery.DUMMY_15627, Scenery.DUMMY_15628, Scenery.DUMMY_15629, Scenery.DUMMY_15630, Scenery.DUMMY_18238, Scenery.DUMMY_25648, Scenery.DUMMY_PAWYA_CORPSE_28912, Scenery.DUMMY_823, Scenery.DUMMY_23921)) {
            SceneryDefinition.forId(i).handlers["option:attack"] = this
            SceneryDefinition.forId(i).handlers["option:hit"] = this
        }
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        player.lock(3)
        player.animate(player.properties.attackAnimation)
        if (player.properties.currentCombatLevel < 8) {
            val p = player
            var experience = 5.0
            when (p.properties.attackStyle.style) {
                WeaponInterface.STYLE_ACCURATE -> p.skills.addExperience(Skills.ATTACK, experience)
                WeaponInterface.STYLE_AGGRESSIVE -> p.skills.addExperience(Skills.STRENGTH, experience)
                WeaponInterface.STYLE_DEFENSIVE -> p.skills.addExperience(Skills.DEFENCE, experience)
                WeaponInterface.STYLE_CONTROLLED -> {
                    experience /= 3
                    p.skills.addExperience(Skills.ATTACK, experience)
                    p.skills.addExperience(Skills.STRENGTH, experience)
                    p.skills.addExperience(Skills.DEFENCE, experience)
                }
            }
        } else {
            sendMessage(player, "You swing at the dummy...")
            sendMessage(player, "There is nothing more you can learn from hitting a dummy.")
        }
        return true
    }
}
