package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.api.playGlobalAudio
import core.api.sendMessage
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.player.Player
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import shared.consts.Items
import shared.consts.NPCs

@Initializable
class HoneyBadgerNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.HONEY_BADGER_6845) :
    Familiar(owner, id, 2500, Items.HONEY_BADGER_POUCH_12065, 4, WeaponInterface.STYLE_AGGRESSIVE) {
    override fun construct(owner: Player, id: Int): Familiar {
        return HoneyBadgerNPC(owner, id)
    }

    override fun visualizeSpecialMove() {
        owner.visualize(Animation.create(7660), Graphics.create(1399))
    }

    public override fun getText(): String {
        return "Raaaar!"
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        if (isCharged) {
            return false
        }
        charge()
        playGlobalAudio(this.location, 4174)
        visualize(Animation(7928, Priority.HIGH), Graphics.create(1397))
        return true
    }

    override fun isCharged(): Boolean {
        if (charged) {
            sendMessage(owner, "Your honey badger is already enraged!")
            return true
        }
        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HONEY_BADGER_6845, NPCs.HONEY_BADGER_6846)
    }
}
