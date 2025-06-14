package content.minigame.mta.plugin

import content.global.skill.magic.SpellListener
import content.global.skill.magic.spells.ModernSpells
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Sounds

class MTASpellLPlugin : SpellListener("modern") {
    private val lowAlchemyAnimation = Animations.HUMAN_CAST_LOW_ALCHEMY_SPELL_712
    private val lowAlchemyGraphics = Graphics(112, 5)
    private val highAlchemyAnimation = Animations.HUMAN_CAST_HIGH_ALCHEMY_SPELL_713
    private val highAlchemyGraphics = Graphics(org.rs.consts.Graphics.HIGH_LEVEL_ALCHEMY_113, 5)
    private val mtaAlchemyItems =
        AlchemistPlaygroundPlugin.AlchemistItem
            .values()
            .map { it.item.id }
            .toIntArray()

    override fun defineListeners() {
        onCast(ModernSpells.HIGH_ALCHEMY, ITEM, *mtaAlchemyItems) { player, node ->
            val item = node?.asItem() ?: return@onCast
            requires(player, 55)
            val alchemyItem = AlchemistPlaygroundPlugin.AlchemistItem.forItem(item.id) ?: return@onCast
            val coins = Item(AlchemistPlaygroundPlugin.COINS.id, alchemyItem.cost)
            val freeAlchemy = alchemyItem == AlchemistPlaygroundPlugin.freeConvert

            if (amountInInventory(player, AlchemistPlaygroundPlugin.COINS.id) + alchemyItem.cost > 12000) {
                sendDialogue(player, "Warning: You can't deposit more than 12000 coins at a time.")
            }

            if (coins.amount > 1 && !hasSpaceFor(player, coins)) {
                sendMessage(player, "Not enough space in your inventory.")
                return@onCast
            }

            if (!freeAlchemy) {
                requires(player, 55, arrayOf(Item(Items.FIRE_RUNE_554, 5), Item(Items.NATURE_RUNE_561, 1)))
            }

            lock(player, 3)
            visualize(player, highAlchemyAnimation, highAlchemyGraphics)
            if (removeItem(player, Item(item.id, 1))) {
                playAudio(player, Sounds.HIGH_ALCHEMY_97)
                if (coins.amount != 0) {
                    player.inventory.add(coins)
                }
            }

            showMagicTab(player)
            addXP(player, 65.0)
            setDelay(player, false)
            removeRunes(player)
        }

        onCast(ModernSpells.LOW_ALCHEMY, ITEM, *mtaAlchemyItems) { player: Player, node ->
            val item = node?.asItem() ?: return@onCast
            requires(player, 21)
            val alchemyItem = AlchemistPlaygroundPlugin.AlchemistItem.forItem(item.id) ?: return@onCast
            val coins = Item(AlchemistPlaygroundPlugin.COINS.id, alchemyItem.cost)
            val freeAlchemy = alchemyItem == AlchemistPlaygroundPlugin.freeConvert

            if (amountInInventory(player, AlchemistPlaygroundPlugin.COINS.id) + alchemyItem.cost > 12000) {
                sendDialogue(player, "Warning: You can't deposit more than 12000 coins at a time.")
            }

            if (coins.amount > 1 && !hasSpaceFor(player, coins)) {
                sendMessage(player, "Not enough space in your inventory.")
                return@onCast
            }

            if (!freeAlchemy) {
                requires(player, 21, arrayOf(Item(Items.FIRE_RUNE_554, 3), Item(Items.NATURE_RUNE_561, 1)))
            }

            lock(player, 3)
            visualize(player, lowAlchemyAnimation, lowAlchemyGraphics)
            if (removeItem(player, Item(item.id, 1))) {
                playAudio(player, Sounds.LOW_ALCHEMY_98)
                if (coins.amount != 0) {
                    player.inventory.add(coins)
                }
            }

            showMagicTab(player)
            addXP(player, 31.0)
            setDelay(player, false)
            removeRunes(player)
        }
    }
}
