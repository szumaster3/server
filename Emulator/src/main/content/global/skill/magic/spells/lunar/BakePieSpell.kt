package content.global.skill.magic.spells.lunar

import content.global.skill.cooking.data.CookableItem
import content.global.skill.magic.SpellListener
import content.global.skill.magic.spells.LunarSpells
import core.api.sendMessage
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Sounds

class BakePieSpell : SpellListener("lunar") {
    override fun defineListeners() {
        onCast(LunarSpells.BAKE_PIE, NONE) { player, _ ->
            requires(
                player,
                65,
                arrayOf(Item(Items.ASTRAL_RUNE_9075), Item(Items.FIRE_RUNE_554, 5), Item(Items.WATER_RUNE_555, 4)),
            )
            val playerPies = ArrayList<Item>()

            for (item in player.inventory.toArray()) {
                if (item == null) continue
                val pie = CookableItem.forId(item.id) ?: continue
                if (!pie.name.lowercase().contains("pie")) continue
                if (player.skills.getLevel(Skills.COOKING) < pie.level) continue
                playerPies.add(item)
            }

            if (playerPies.isEmpty()) {
                sendMessage(player, "You have no pies which you have the level to cook.")
                return@onCast
            }
            player.pulseManager.run(
                object : Pulse() {
                    var counter = 0

                    override fun pulse(): Boolean {
                        if (playerPies.isEmpty()) return true
                        if (counter == 0) delay = Animation(Animations.LUNAR_BAKE_PIE_4413).definition.durationTicks + 1
                        val item = playerPies[0]
                        val pie = CookableItem.forId(item.id)
                        visualizeSpell(player, Animations.LUNAR_BAKE_PIE_4413, 746, 75, Sounds.LUNAR_BAKE_PIE_2879)
                        addXP(player, 60.0)
                        player.skills.addExperience(Skills.COOKING, pie!!.experience)
                        setDelay(player, false)
                        player.inventory.remove(item)
                        player.inventory.add(Item(pie.cooked))
                        playerPies.remove(item)
                        if (playerPies.isNotEmpty()) removeRunes(player, false) else removeRunes(player, true)
                        return false
                    }
                },
            )
        }
    }
}
