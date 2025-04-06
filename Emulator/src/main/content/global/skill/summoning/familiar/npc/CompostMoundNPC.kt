package content.global.skill.summoning.familiar.npc

import content.global.skill.farming.CompostBins
import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import content.global.skill.summoning.familiar.Forager
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction

/**
 * The type Compost mound npc.
 */
@Initializable
class CompostMoundNPC
/**
 * Instantiates a new Compost mound npc.
 *
 * @param owner the owner
 * @param id    the id
 */
/**
 * Instantiates a new Compost mound npc.
 */
@JvmOverloads constructor(owner: Player? = null, id: Int = 6871) :
    Forager(owner, id, 2400, 12091, 12, WeaponInterface.STYLE_AGGRESSIVE, *ITEMS) {
    override fun construct(owner: Player, id: Int): Familiar {
        return CompostMoundNPC(owner, id)
    }

    public override fun configureFamiliar() {
        definePlugin(CompostBucketPlugin())
    }

    override fun isPoisonImmune(): Boolean {
        return true
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        val `object` = special.node as Scenery
        if (`object`.name != "Compost Bin") {
            owner.packetDispatch.sendMessage("This scroll can only be used on an empty compost bin.")
            return false
        }
        val cbin = CompostBins.forObject(special.node.asScenery()) ?: return false
        val bin = cbin.getBinForPlayer(owner)
        if (bin.isFinished || bin.isFull() || bin.isClosed) {
            return false
        }
        val superCompost = RandomFunction.random(10) == 1
        faceLocation(`object`.location)
        val toAdd = Item(if (superCompost) PINEAPPLE_2114 else POTATO_1942)
        toAdd.amount = 15
        bin.addItem(toAdd)
        bin.close()
        animate(Animation.create(7775))
        graphics(Graphics.create(1424))
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(6871, 6872)
    }

    /**
     * The type Compost bucket plugin.
     */
    inner class CompostBucketPlugin
    /**
     * Instantiates a new Compost bucket plugin.
     */
        : UseWithHandler(1925) {
        @Throws(Throwable::class)
        override fun newInstance(arg: Any?): Plugin<Any> {
            addHandler(6871, NPC_TYPE, this)
            addHandler(6872, NPC_TYPE, this)
            return this
        }

        override fun handle(event: NodeUsageEvent): Boolean {
            val player = event.player
            val familiar = event.usedWith as Familiar
            if (!player.familiarManager.isOwner(familiar)) {
                return true
            }
            player.animate(Animation.create(895))
            familiar.animate(Animation.create(7775))
            player.inventory.replace(ITEMS[0], event.usedItem.slot)
            familiar.impactHandler.manualHit(player, 2, HitsplatType.NORMAL)
            return true
        }
    }

    companion object {
        private val ITEMS = arrayOf(
            Item(6032),
            Item(6034),
            Item(5318),
            Item(5319),
            Item(5324),
            Item(5322),
            Item(5320),
            Item(5323),
            Item(5321),
            Item(5305),
            Item(5307),
            Item(5308),
            Item(5306),
            Item(5309),
            Item(5310),
            Item(5311),
            Item(5101),
            Item(5102),
            Item(5103),
            Item(5104),
            Item(5105),
            Item(5106),
            Item(5096),
            Item(5097),
            Item(5098),
            Item(5099),
            Item(5100),
            Item(5291),
            Item(5292),
            Item(5293),
            Item(5294),
            Item(5295),
            Item(12176),
            Item(5296),
            Item(5298),
            Item(5299),
            Item(5300),
            Item(5301),
            Item(5302),
            Item(5303),
            Item(5304)
        )
    }
}
