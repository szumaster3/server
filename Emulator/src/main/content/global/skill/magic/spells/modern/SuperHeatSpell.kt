package content.global.skill.magic.spells.modern

import content.global.skill.magic.SpellListener
import content.global.skill.magic.spells.ModernSpells
import content.global.skill.smithing.smelting.Bar
import content.global.skill.smithing.smelting.SmeltingPulse
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.Sounds

class SuperHeatSpell : SpellListener("modern") {
    override fun defineListeners() {
        onCast(ModernSpells.SUPERHEAT, ITEM) { player, node ->
            val item = node?.asItem() ?: return@onCast
            requires(
                player = player,
                magicLevel = 43,
                runes = arrayOf(Item(Items.FIRE_RUNE_554, 4), Item(Items.NATURE_RUNE_561, 1)),
            )
            if (!item.name.contains("ore") && !item.name.equals("coal", true)) {
                sendMessage(player, "You can only cast this spell on ore.")
                return@onCast
            }

            if (item.id == Items.ELEMENTAL_ORE_2892) {
                sendMessage(player, "Even this spell is not hot enough to heat this item.")
                return@onCast
            }

            fun returnBar(
                player: Player,
                item: Item,
            ): Bar? {
                for (potentialBar in Bar.values().reversed()) {
                    val inputOreInBar = potentialBar.ores.map { it.id }.contains(item.id)
                    val playerHasNecessaryOres =
                        potentialBar.ores.all { ore -> inInventory(player, ore.id, ore.amount) }
                    if (inputOreInBar && playerHasNecessaryOres) return potentialBar
                }
                sendMessage(player, "You do not have the required ores to make this bar.")
                return null
            }

            var bar = returnBar(player, item) ?: return@onCast

            if (getStatLevel(player, Skills.SMITHING) < bar.level) {
                sendMessage(player, "You need a smithing level of ${bar.level} to superheat that ore.")
                return@onCast
            }

            lock(player, 3)
            removeRunes(player)
            addXP(player, 53.0)
            playAudio(player, Sounds.SUPERHEAT_ALL_190)
            showMagicTab(player)
            submitIndividualPulse(player, SmeltingPulse(player, item, bar, 1, true))
            setDelay(player, false)
        }
    }
}
