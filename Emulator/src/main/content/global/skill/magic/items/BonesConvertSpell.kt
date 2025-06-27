package content.global.skill.magic.items

import content.global.skill.magic.SpellListener
import content.global.skill.magic.spells.ModernSpells
import content.global.skill.prayer.Bones
import content.minigame.mta.plugin.CreatureGraveyardPlugin
import core.api.playAudio
import core.api.sendMessage
import core.game.node.entity.impl.Animator
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Sounds

class BonesConvertSpell : SpellListener("modern") {
    private val boneConvertAnimation = Animation(Animations.ENCHANT_JEWEL_722, Animator.Priority.HIGH)
    private val boneConvertGraphics =
        Graphics(org.rs.consts.Graphics.BONES_TO_BANANAS_141, 96)

    override fun defineListeners() {
        onCast(ModernSpells.BONES_TO_BANANAS, NONE) { player, _ ->
            requires(
                player = player,
                magicLevel = 15,
                runes =
                    arrayOf(
                        Item(Items.EARTH_RUNE_557, 2),
                        Item(Items.WATER_RUNE_555, 2),
                        Item(Items.NATURE_RUNE_561, 1),
                    ),
            )
            boneConvert(player = player, bananas = true)
        }

        onCast(ModernSpells.BONES_TO_PEACHES, NONE) { player, _ ->
            requires(
                player = player,
                magicLevel = 60,
                runes =
                    arrayOf(
                        Item(Items.EARTH_RUNE_557, 4),
                        Item(Items.WATER_RUNE_555, 4),
                        Item(Items.NATURE_RUNE_561, 2),
                    ),
            )
            boneConvert(player = player, bananas = false)
        }
    }

    private fun boneConvert(
        player: Player,
        bananas: Boolean,
    ) {
        val isInMTA = player.zoneMonitor.isInZone("Creature Graveyard")
        if (isInMTA && player.getAttribute("tablet-spell", false)) {
            sendMessage(player, "You can not use this tablet in the Mage Training Arena.")
            return
        }

        if (!bananas &&
            !player.savedData.activityData.isBonesToPeaches &&
            !player.getAttribute("tablet-spell", false)
        ) {
            sendMessage(player, "You can only learn this spell from the Mage Training Arena.")
            return
        }

        val bones =
            if (isInMTA) {
                intArrayOf(
                    Items.ANIMALS_BONES_6904,
                    Items.ANIMALS_BONES_6905,
                    Items.ANIMALS_BONES_6906,
                    Items.ANIMALS_BONES_6907,
                )
            } else {
                Bones.values().map { it.itemId }.toIntArray()
            }

        for (item in player.inventory.toArray()) {
            item ?: continue
            if (isInMTA) {
                if (bones.contains(item.id)) {
                    val inInventory = player.inventory.getAmount(item.id)
                    val amount = inInventory * (CreatureGraveyardPlugin.BoneType.forItem(Item(item.id))!!.ordinal + 1)
                    if (amount > 0) {
                        player.inventory.remove(Item(item.id, inInventory))
                        player.inventory.add(Item(if (bananas) Items.BANANA_1963 else Items.PEACH_6883, amount))
                    }
                }
            } else {
                if (bones.contains(item.id)) {
                    val inInventory = player.inventory.getAmount(item.id)
                    player.inventory.remove(Item(item.id, inInventory))
                    player.inventory.add(Item(if (bananas) Items.BANANA_1963 else Items.PEACH_6883, inInventory))
                }
            }
        }
        visualizeSpell(player, boneConvertAnimation, boneConvertGraphics)
        playAudio(player, Sounds.BONES_TO_BANANAS_ALL_114)
        removeRunes(player)
        addXP(player, if (bananas) 25.0 else 65.0)
        setDelay(player, false)
    }
}
