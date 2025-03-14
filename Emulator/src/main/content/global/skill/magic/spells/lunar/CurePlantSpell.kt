package content.global.skill.magic.spells.lunar

import content.global.skill.farming.CompostBins
import content.global.skill.farming.FarmingPatch
import content.global.skill.magic.SpellListener
import content.global.skill.magic.spells.LunarSpells
import core.api.sendMessage
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Sounds

class CurePlantSpell : SpellListener("lunar") {
    override fun defineListeners() {
        onCast(LunarSpells.CURE_PLANT, OBJECT) { player, node ->
            requires(player, 66, arrayOf(Item(Items.ASTRAL_RUNE_9075), Item(Items.EARTH_RUNE_557, 8)))
            if (CompostBins.forObject(node!!.asScenery()) != null) {
                sendMessage(player, "Bins don't often get diseased.")
                return@onCast
            }
            val fPatch = FarmingPatch.forObject(node!!.asScenery())
            if (fPatch == null) {
                sendMessage(player, "Umm... this spell won't cure that!")
                return@onCast
            }
            val patch = fPatch.getPatchFor(player)
            if (patch.isWeedy()) {
                sendMessage(player, "The weeds are healthy enough already.")
                return@onCast
            }
            if (patch.isEmptyAndWeeded()) {
                sendMessage(player, "There's nothing there to cure.")
                return@onCast
            }
            if (patch.isGrown()) {
                sendMessage(player, "That's not diseased.")
                return@onCast
            }
            if (patch.isDead) {
                sendMessage(
                    player,
                    "It says 'Cure' not 'Resurrect'. Although death may arise from disease, it is not in itself a disease and hence cannot be cured. So there.",
                )
                return@onCast
            }
            if (!patch.isDiseased) {
                sendMessage(player, "It is growing just fine.")
                return@onCast
            }

            patch.cureDisease()
            removeRunes(player)
            addXP(player, 60.0)
            visualizeSpell(player, Animations.LUNAR_CURE_PLANT_4409, 748, 100, Sounds.LUNAR_CURE_GROUP_2882)
            setDelay(player, false)
        }
    }
}
