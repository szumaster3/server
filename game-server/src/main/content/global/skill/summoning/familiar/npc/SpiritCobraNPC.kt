package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.api.sendMessage
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class SpiritCobraNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.SPIRIT_COBRA_6802) :
    Familiar(owner, id, 5600, Items.SPIRIT_COBRA_POUCH_12015, 3, WeaponInterface.STYLE_ACCURATE) {
    override fun construct(owner: Player, id: Int): Familiar {
        return SpiritCobraNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        val item = special.node as Item
        val egg = Egg.forEgg(item)
        if (egg == null) {
            sendMessage(owner, "You can't use the special move on this item.")
            return false
        }
        owner.inventory.replace(egg.product, item.slot)
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SPIRIT_COBRA_6802, NPCs.SPIRIT_COBRA_6803)
    }

    /**
     * Represents a type of eggs.
     *
     * @property egg The input egg.
     * @property product The resulting item.
     */
    enum class Egg(val egg: Item, val product: Item) {
        COCKATRICE(Item(Items.EGG_1944), Item(Items.COCKATRICE_EGG_12109)),
        SARATRICE(Item(Items.BIRDS_EGG_5077), Item(Items.SARATRICE_EGG_12113)),
        ZAMATRICE(Item(Items.BIRDS_EGG_5076), Item(Items.ZAMATRICE_EGG_12115)),
        GUTHATRICE(Item(Items.BIRDS_EGG_5078), Item(Items.GUTHATRICE_EGG_12111)),
        CORACATRICE(Item(Items.RAVEN_EGG_11964), Item(Items.CORAXATRICE_EGG_12119)),
        PENGATRICE(Item(Items.PENGUIN_EGG_12483), Item(Items.PENGATRICE_EGG_12117)),
        VULATRICE(Item(Items.VULTURE_EGG_11965), Item(Items.VULATRICE_EGG_12121));

        companion object {
            /**
             * Finds the corresponding [Egg] enum for the given [item], if any.
             *
             * @param item The egg item to look up.
             * @return The matching [Egg] instance, or `null` if the item does not match any known egg.
             */
            fun forEgg(item: Item): Egg? {
                return values().firstOrNull { it.egg.id == item.id }
            }
        }
    }
}
