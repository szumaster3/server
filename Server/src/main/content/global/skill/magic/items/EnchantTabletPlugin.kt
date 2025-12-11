package content.global.skill.magic.items

import content.global.skill.magic.spells.modern.EnchantSpellEffect
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import shared.consts.Animations
import shared.consts.Sounds

class EnchantTabletPlugin : InteractionListener {

    override fun defineListeners() {
        for (tablet in Enchantments.BY_TABLET.keys) {
            onUseWith(IntType.ITEM, tablet, *Enchantments.BY_TABLET[tablet]!!.keys.toIntArray()) { player: Player, used: Node, with: Node ->
                enchantItem(player, used.id, with.id)
                true
            }

            on(tablet, IntType.ITEM, "break") { player, node ->
                val itemsMap = Enchantments.BY_TABLET[node.id] ?: return@on true

                val targetId = player.inventory.toArray()
                    .filterNotNull()
                    .firstOrNull { itemsMap.containsKey(it.id) }
                    ?.id

                if (targetId != null) {
                    enchantItem(player, node.id, targetId)
                } else {
                    removeItem(player, node.id)
                    playAudio(player, Sounds.POH_TABLET_BREAK_979)
                    player.animator.forceAnimation(Animation(Animations.BREAK_SPELL_TABLET_A_4069))
                }

                return@on true
            }
        }
    }

    private fun enchantItem(player: Player, tabletId: Int, targetId: Int) {
        val itemsMap = Enchantments.BY_TABLET[tabletId] ?: return
        val productId = itemsMap[targetId] ?: return

        removeItem(player, tabletId)
        removeItem(player, targetId)
        addItem(player, productId)

        val effect = EnchantSpellEffect.fromItemId(targetId)

        playAudio(player, Sounds.POH_TABLET_BREAK_979)
        player.animator.forceAnimation(Animation(Animations.BREAK_SPELL_TABLET_A_4069))
        player.lock(5)
        setAttribute(player, "tablet-spell", true)

        runTask(player, 3) {
            effect?.let {
                playAudio(player, it.sound)
                visualize(player, it.animation, Graphics(it.graphic, 92))
            }
        }
    }
}
