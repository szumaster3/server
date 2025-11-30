package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.api.playGlobalAudio
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Sounds

@Initializable
class BarkerToadNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.BARKER_TOAD_6889) :
    Familiar(owner, id, 800, Items.BARKER_TOAD_POUCH_12123, 6, WeaponInterface.STYLE_AGGRESSIVE) {

    override fun construct(owner: Player, id: Int): Familiar {
        return BarkerToadNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        val target = special.target
        if (!canCombatSpecial(target)) {
            return false
        }
        playGlobalAudio(this.location, Sounds.BARKER_TOAD_SPECIAL_ATTACK_4310)
        animate(properties.attackAnimation)
        graphics(Graphics.create(1403))
        sendFamiliarHit(target, 8, Graphics.create(1404))
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BARKER_TOAD_6889, NPCs.BARKER_TOAD_6890)
    }
}
