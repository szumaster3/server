package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.api.playGlobalAudio
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Sounds

@Initializable
class SpiritDagannothNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.SPIRIT_DAGANNOTH_6804) :
    Familiar(owner, id, 5700, Items.SPIRIT_DAGANNOTH_POUCH_12017, 6, WeaponInterface.STYLE_CONTROLLED) {
    override fun construct(owner: Player, id: Int): Familiar {
        return SpiritDagannothNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        val target = special.target
        if (!canCombatSpecial(target)) {
            return false
        }
        playGlobalAudio(this.location, Sounds.DAGGANOTH_ATTACK_SPECIAL_4300)
        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SPIRIT_DAGANNOTH_6804, NPCs.SPIRIT_DAGANNOTH_6805)
    }
}
