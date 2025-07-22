package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import core.game.world.map.RegionManager.isTeleportPermitted
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class SpiritSpiderNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.SPIRIT_SPIDER_6841) :
    Familiar(owner, id, 1500, Items.SPIRIT_SPIDER_POUCH_12059, 6, WeaponInterface.STYLE_CONTROLLED) {
    private var eggDelay = GameWorld.ticks + 500

    public override fun handleFamiliarTick() {
        super.handleFamiliarTick()
        if (eggDelay < GameWorld.ticks) {
            if (RandomFunction.random(25) == 5) {
                createEggs()
                sendChat("Clicketyclack")
                eggDelay = GameWorld.ticks + 500
            }
        }
    }

    override fun construct(owner: Player, id: Int): Familiar {
        return SpiritSpiderNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        createEggs()
        return true
    }

    private fun createEggs() {
        val amount = RandomFunction.random(8)
        var location: Location? = null
        for (i in 0 until amount) {
            location = eggLocation
            if (location == null) {
                continue
            }
            owner.packetDispatch.sendPositionedGraphic(1342, 0, 0, location)
            GroundItemManager.create(EGG, location, owner)
        }
        animate(Animation.create(5328))
    }

    private val eggLocation: Location?
        get() {
            val loc = owner.location.transform(
                if (RandomFunction.random(2) == 1) -RandomFunction.random(2) else RandomFunction.random(2),
                if (RandomFunction.random(2) == 1) -RandomFunction.random(2) else RandomFunction.random(2),
                0
            )
            if (loc == owner.location || !isTeleportPermitted(loc) || getObject(loc) != null) {
                return null
            }
            return loc
        }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SPIRIT_SPIDER_6841, NPCs.SPIRIT_SPIDER_6842)
    }

    companion object {
        private val EGG = Item(Items.RED_SPIDERS_EGGS_223)
    }
}
