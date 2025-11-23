package content.global.skill.magic.spells.lunar

import content.global.skill.magic.spells.LunarSpells
import core.api.*
import core.game.component.Component
import core.game.dialogue.DialogueFile
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.spell.MagicSpell
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import shared.consts.Sounds

@Initializable
class SpellbookSwapSpell : MagicSpell(SpellBook.LUNAR, 96, 130.0, null, null, null, arrayOf(Item(Runes.LAW_RUNE.id, 1), Item(Runes.COSMIC_RUNE.id, 2), Item(Runes.ASTRAL_RUNE.id, 3))) {

    @Throws(Throwable::class)
    override fun newInstance(arg: SpellType?): Plugin<SpellType?> {
        SpellBook.LUNAR.register(LunarSpells.SPELLBOOK_SWAP, this)
        return this
    }

    override fun cast(entity: Entity, target: Node): Boolean {
        val player = entity as Player
        if (!super.meetsRequirements(player, true, true)) {
            return false
        }
        player.lock(9)
        visualize(player, ANIMATION, Graphics)
        openDialogue(player, SpellBookSwapDialogue())
        val id = RandomFunction.random(1, 500000)
        setAttribute(player, "spell:swap", id)
        Pulser.submit(
            object : Pulse(100, player) {
                override fun pulse(): Boolean {
                    if (player.getAttribute("spell:swap", 0) == id) {
                        removeTemporarySpell(player)
                    }
                    return true
                }
            },
        )
        return true
    }

    companion object {
        private val ANIMATION = Animation(6299)
        private val Graphics = Graphics(shared.consts.Graphics.SPELLBOOK_SWAP_GFX_1062)

        fun removeTemporarySpell(player: Player) {
            removeAttribute(player, "spell:swap")
            player.spellBookManager.setSpellBook(SpellBook.LUNAR)
            player.interfaceManager.openTab(Component(SpellBook.LUNAR.interfaceId))
        }
    }

    private class SpellBookSwapDialogue : DialogueFile() {

        override fun handle(componentID: Int, buttonID: Int) {
            when (stage) {
                0 -> sendOptions(player!!, "Select a Spellbook:", "Ancient", "Modern")
                1 -> {
                    var type = 0
                    when (buttonID) {
                        1 -> type = 1
                        2 -> type = 2
                    }
                    val book = if (type == 1) SpellBook.ANCIENT else SpellBook.MODERN
                    playAudio(player!!, Sounds.LUNAR_CHANGE_SPELLBOOK_3613)
                    player?.spellBookManager?.setSpellBook(book)
                    player?.interfaceManager?.openTab(Component(book.interfaceId))
                    end()
                }
            }
        }
    }
}
