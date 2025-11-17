package content.global.skill.magic.spells.lunar

import content.global.skill.magic.SpellListener
import content.global.skill.magic.spells.LunarSpells
import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Animations
import shared.consts.Graphics
import shared.consts.Items
import shared.consts.Sounds

class DreamSpell : SpellListener("lunar") {

    override fun defineListeners() {
        onCast(LunarSpells.DREAM, NONE) { player, _ ->
            requires(player, 79, arrayOf(Item(Items.ASTRAL_RUNE_9075, 2), Item(Items.BODY_RUNE_559, 5), Item(Items.COSMIC_RUNE_564, 1)))
            if (player.skills.lifepoints >= getStatLevel(player, Skills.HITPOINTS)) {
                sendMessage(player, "You have no need to cast this spell since your hitpoints are already full.")
                return@onCast
            }

            animate(player, Animations.DREAM_SPELL_6295)
            delayEntity(player, 4)
            queueScript(player, 4, QueueStrength.WEAK) { stage: Int ->
                when (stage) {
                    0 -> {
                        animate(player, 6296)
                        sendGraphics(Graphics.SLEEPING_ZZZ_1056, player.location)
                        playAudio(player, Sounds.LUNAR_SLEEP_3619)
                        return@queueScript delayScript(player, 5)
                    }

                    else -> {
                        sendGraphics(Graphics.SLEEPING_ZZZ_1056, player.location)
                        if (stage.mod(10) == 0) {
                            heal(player, 1)
                            if (player.skills.lifepoints >= getStatLevel(player, Skills.HITPOINTS)) {
                                animate(player, Animations.GET_UP_FROM_SLEEPING_DREAM_SPELL_6297)
                                return@queueScript stopExecuting(player)
                            }
                        }
                        return@queueScript delayScript(player, 5)
                    }
                }
            }
        }
    }
}
