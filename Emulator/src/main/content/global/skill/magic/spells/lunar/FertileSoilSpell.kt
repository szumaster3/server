package content.global.skill.magic.spells.lunar

import content.global.skill.farming.CompostBins
import content.global.skill.farming.CompostType
import content.global.skill.farming.FarmingPatch
import content.global.skill.magic.SpellListener
import content.global.skill.magic.spells.LunarSpells
import core.api.animate
import core.api.playGlobalAudio
import core.api.sendGraphics
import core.api.sendMessage
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Sounds

class FertileSoilSpell : SpellListener("lunar") {
    override fun defineListeners() {
        onCast(LunarSpells.FERTILE_SOIL, OBJECT) { player, node ->
            node?.let {
                if (CompostBins.forObjectID(node.id) != null) {
                    sendMessage(player, "No, that would be silly.")
                    return@onCast
                }

                val fPatch = FarmingPatch.forObject(node.asScenery())
                if (fPatch == null) {
                    sendMessage(player, "Um... I don't want to fertilize that!")
                    return@onCast
                }

                val patch = fPatch.getPatchFor(player)
                if (patch.isGrown()) {
                    sendMessage(player, "Composting isn't going to make it get any bigger.")
                    return@onCast
                }
                if (patch.isFertilized()) {
                    sendMessage(player, "This patch has already been composted.")
                    return@onCast
                }
                requires(
                    player,
                    83,
                    arrayOf(
                        Item(Items.ASTRAL_RUNE_9075, 3),
                        Item(Items.NATURE_RUNE_561, 2),
                        Item(Items.EARTH_RUNE_557, 15),
                    ),
                )
                removeRunes(player, true)
                animate(player, Animations.LUNAR_FERTILE_SOIL_4413)
                sendGraphics(724, node.location)
                playGlobalAudio(node.location, Sounds.LUNAR_FERTILIZE_2891)
                patch.compost = CompostType.SUPERCOMPOST
                sendMessage(player, "You fertilize the soil.")
                addXP(player, 87.0)
            }
        }
    }
}
