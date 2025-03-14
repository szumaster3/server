package content.global.skill.magic.spells.modern

import content.global.skill.magic.SpellListener
import content.global.skill.magic.spells.ModernSpells
import core.api.anyInEquipment
import core.api.playAudio
import core.api.sendMessage
import core.api.visualize
import core.game.event.ItemAlchemizationEvent
import core.game.interaction.MovementPulse
import core.game.node.entity.impl.Animator
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Sounds

class AlchemySpell : SpellListener("modern") {
    private val lowAlchemySpellAnimation = Animation(9623, Animator.Priority.HIGH)
    private val lowAlchemyGraphics =
        core.game.world.update.flag.context
            .Graphics(org.rs.consts.Graphics.LOW_ALCHEMY_BY_HAND_763)
    private val lowAlchemyStaffSpellAnimation =
        Animation(Animations.HUMAN_CAST_LOW_ALCH_SPELL_9625, Animator.Priority.HIGH)
    private val lowAlchemyStaffGraphics =
        core.game.world.update.flag.context.Graphics(
            org.rs.consts.Graphics.LOW_ALCH_WITH_STAFF_1692,
        )

    private val highAlchemyAnimation = Animation(9631, Animator.Priority.HIGH)
    private val highAlchemyGraphics =
        core.game.world.update.flag.context
            .Graphics(org.rs.consts.Graphics.ALCH_1691)
    private val highAlchemyStaffAnimation = Animation(Animations.ALCH_WITH_STAFF_9633, Animator.Priority.HIGH)
    private val highAlchemyStaffGraphics =
        core.game.world.update.flag.context.Graphics(
            org.rs.consts.Graphics.HIGH_ALCH_WITH_STAFF_1693,
        )

    override fun defineListeners() {
        onCast(ModernSpells.LOW_ALCHEMY, ITEM) { player, node ->
            val item = node?.asItem() ?: return@onCast
            requires(
                player = player,
                magicLevel = 21,
                runes = arrayOf(Item(Items.FIRE_RUNE_554, 3), Item(Items.NATURE_RUNE_561)),
            )
            alchemize(player, item, high = false)
        }

        onCast(ModernSpells.HIGH_ALCHEMY, ITEM) { player, node ->
            val item = node?.asItem() ?: return@onCast
            requires(
                player = player,
                magicLevel = 55,
                runes = arrayOf(Item(Items.FIRE_RUNE_554, 5), Item(Items.NATURE_RUNE_561, 1)),
            )
            alchemize(player, item, high = true)
        }
    }

    fun alchemize(
        player: Player,
        item: Item,
        high: Boolean,
        explorersRing: Boolean = false,
    ): Boolean {
        if (item.name == "Coins") sendMessage(player, "Coins are already made of gold...").also { return false }
        if ((!item.definition.isTradeable) && (!item.definition.isAlchemizable)) {
            sendMessage(
                player,
                "You can't cast this spell on something like that.",
            ).also { return false }
        }

        if (player.zoneMonitor.isInZone("Alchemists' Playground")) {
            sendMessage(player, "You can only alch items from the cupboards!")
            return false
        }

        val coins = Item(Items.COINS_995, item.definition.getAlchemyValue(high))
        if (item.amount > 1 && coins.amount > 0 && !player.inventory.hasSpaceFor(coins)) {
            sendMessage(player, "Not enough space in your inventory.")
            return false
        }

        if (player.pulseManager.current !is MovementPulse) {
            player.pulseManager.clear()
        }

        val staves =
            intArrayOf(
                Items.STAFF_OF_FIRE_1387,
                Items.FIRE_BATTLESTAFF_1393,
                Items.MYSTIC_FIRE_STAFF_1401,
                Items.LAVA_BATTLESTAFF_3053,
                Items.MYSTIC_LAVA_STAFF_3054,
                Items.STEAM_BATTLESTAFF_11736,
                Items.MYSTIC_STEAM_STAFF_11738,
            )

        val explorersRingGraphics =
            core.game.world.update.flag.context
                .Graphics(1698)

        if (explorersRing) {
            visualize(entity = player, anim = lowAlchemySpellAnimation, gfx = explorersRingGraphics)
        } else {
            if (anyInEquipment(player, *staves)) {
                visualize(
                    player,
                    if (high) highAlchemyStaffAnimation else lowAlchemyStaffSpellAnimation,
                    if (high) highAlchemyStaffGraphics else lowAlchemyStaffGraphics,
                )
            } else {
                visualize(
                    player,
                    if (high) highAlchemyAnimation else lowAlchemySpellAnimation,
                    if (high) highAlchemyGraphics else lowAlchemyGraphics,
                )
            }
        }
        playAudio(player, if (high) Sounds.HIGH_ALCHEMY_97 else Sounds.LOW_ALCHEMY_98)

        player.dispatch(ItemAlchemizationEvent(item.id, high))
        if (player.inventory.remove(Item(item.id, 1)) && coins.amount > 0) {
            player.inventory.add(coins)
        }
        removeRunes(player)
        addXP(player, if (high) 65.0 else 31.0)
        showMagicTab(player)
        setDelay(player, 5)
        return true
    }
}
