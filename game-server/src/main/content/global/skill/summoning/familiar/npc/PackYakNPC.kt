package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.SummoningScroll
import content.global.skill.summoning.familiar.BurdenBeast
import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.api.Container
import core.api.addItem
import core.api.removeItem
import core.api.sendMessage
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.LogType
import core.game.node.entity.player.info.PlayerMonitor.log
import core.game.node.item.Item
import core.game.system.config.ItemConfigParser
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class PackYakNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.PACK_YAK_6873) :
    BurdenBeast(owner, id, 5800, Items.PACK_YAK_POUCH_12093, 12, 30, WeaponInterface.STYLE_AGGRESSIVE) {
    override fun construct(owner: Player, id: Int): Familiar {
        return PackYakNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        val player = owner ?: return false
        var item = special.item ?: return false
        item = Item(item.id, 1)

        if (item.id == SummoningScroll.WINTER_STORAGE_SCROLL.itemId) {
            return false
        }

        if (!item.definition.getConfiguration(ItemConfigParser.BANKABLE, true)) {
            sendMessage(player, "A magical force prevents you from banking this item.")
            return false
        }

        val remove = if (!item.definition.isUnnoted()) {
            Item(item.id, 1)
        } else {
            item
        }

        if (!item.definition.isUnnoted()) {
            item = Item(item.noteChange, 1)
        }

        var success = addItem(player, item.id, item.amount, Container.BANK)
        if (success) {
            success = removeItem(player, remove, Container.INVENTORY)
            if (!success) {
                val recovered = removeItem(player, item, Container.BANK)
                if (recovered) {
                    log(
                        player,
                        LogType.DUPE_ALERT,
                        "Successfully recovered from potential dupe attempt involving the winter storage scroll"
                    )
                } else {
                    log(
                        player,
                        LogType.DUPE_ALERT,
                        "Failed to recover from potentially successful dupe attempt involving the winter storage scroll"
                    )
                }
            }
        }

        if (success) {
            player.dialogueInterpreter.close()
            graphics(Graphics.create(1358))
            sendMessage(player, "The pack yak has sent an item to your bank.")
        } else {
            sendMessage(player, "The pack yak can't send that item to your bank.")
        }
        return true
    }

    override fun visualizeSpecialMove() {
        owner.visualize(Animation.create(7660), Graphics.create(1316))
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PACK_YAK_6873, NPCs.PACK_YAK_6874)
    }

    override fun getText(): String {
        return "Baroo!"
    }
}
