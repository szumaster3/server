package content.global.handlers.iface

import core.api.*
import core.api.quest.isQuestComplete
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.Log
import org.rs.consts.Components
import org.rs.consts.Quests
import org.rs.consts.Sounds

@Initializable
class ExperienceInterface : ComponentPlugin() {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any?> {
        ComponentDefinition.put(COMPONENT_ID, this)
        return this
    }

    override fun handle(
        player: Player,
        component: Component,
        opcode: Int,
        button: Int,
        slot: Int,
        itemId: Int,
    ): Boolean {
        if (button == 2) {
            val confirmedSkill = getAttribute(player, "exp_interface:skill", -1)
            if (confirmedSkill == -1) {
                sendMessage(player, "You must first select a skill.")
            } else {
                removeAttribute(player, "exp_interface:skill")
                when (confirmedSkill) {
                    Skills.HERBLORE ->
                        if (!checkHerblore(player)) {
                            sendMessage(
                                player,
                                "You need to have completed ${Quests.DRUIDIC_RITUAL} for this.",
                            ).also { return true }
                        }

                    Skills.RUNECRAFTING ->
                        if (!checkRunecrafting(player)) {
                            sendMessage(
                                player,
                                "You need to have completed Rune Mysteries for this.",
                            ).also { return true }
                        }

                    Skills.SUMMONING ->
                        if (!checkSummoning(player)) {
                            sendMessage(
                                player,
                                "You need to have completed ${Quests.WOLF_WHISTLE} for this.",
                            ).also { return true }
                        }
                }
                val caller = player.attributes["caller"]
                caller ?: return true
                if (caller is Plugin<*>) {
                    caller.handleSelectionCallback(confirmedSkill, player)
                } else {
                    (caller as (Int, Player) -> Unit).invoke(confirmedSkill, player)
                }
                playAudio(player, SOUND)
                closeInterface(player)
            }
        } else {
            val skill =
                when (button) {
                    29 -> Skills.ATTACK
                    30 -> Skills.STRENGTH
                    31 -> Skills.DEFENCE
                    32 -> Skills.RANGE
                    35 -> Skills.MAGIC
                    39 -> Skills.CRAFTING
                    34 -> Skills.HITPOINTS
                    33 -> Skills.PRAYER
                    36 -> Skills.AGILITY
                    37 -> Skills.HERBLORE
                    38 -> Skills.THIEVING
                    43 -> Skills.FISHING
                    47 -> Skills.RUNECRAFTING
                    48 -> Skills.SLAYER
                    50 -> Skills.FARMING
                    41 -> Skills.MINING
                    42 -> Skills.SMITHING
                    49 -> Skills.HUNTER
                    52 -> Skills.SUMMONING
                    45 -> Skills.COOKING
                    44 -> Skills.FIREMAKING
                    46 -> Skills.WOODCUTTING
                    40 -> Skills.FLETCHING
                    51 -> Skills.CONSTRUCTION
                    else ->
                        Skills.SLAYER.also {
                            log(this::class.java, Log.WARN, "EXP_INTERFACE: Invalid SKILL CHOICE BUTTON: $button")
                        }
                }
            setAttribute(player, "exp_interface:skill", skill)
        }
        return true
    }

    private fun checkHerblore(player: Player): Boolean {
        return (isQuestComplete(player, Quests.DRUIDIC_RITUAL))
    }

    private fun checkSummoning(player: Player): Boolean {
        return isQuestComplete(player, Quests.WOLF_WHISTLE)
    }

    private fun checkRunecrafting(player: Player): Boolean {
        return isQuestComplete(player, Quests.RUNE_MYSTERIES)
    }

    companion object {
        private val SOUND = Sounds.TBCU_FINDGEM_1270
        val COMPONENT_ID = Components.STATS_ADVANCEMENT_134
    }
}
