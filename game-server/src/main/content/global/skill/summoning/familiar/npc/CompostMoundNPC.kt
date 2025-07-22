package content.global.skill.summoning.familiar.npc

import content.global.skill.farming.CompostBins
import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import content.global.skill.summoning.familiar.Forager
import core.api.sendMessage
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
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class CompostMoundNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.COMPOST_MOUND_6871) :
    Forager(owner, id, 2400, Items.COMPOST_MOUND_POUCH_12091, 12, WeaponInterface.STYLE_AGGRESSIVE, *ITEMS) {
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
            sendMessage(owner, "This scroll can only be used on an empty compost bin.")
            return false
        }
        val cbin = CompostBins.forObject(special.node.asScenery()) ?: return false
        val bin = cbin.getBinForPlayer(owner)
        if (bin.isFinished || bin.isFull() || bin.isClosed) {
            return false
        }
        val superCompost = RandomFunction.random(10) == 1
        faceLocation(`object`.location)
        val toAdd = Item(if (superCompost) Items.PINEAPPLE_2114 else Items.POTATO_1942)
        toAdd.amount = 15
        bin.addItem(toAdd)
        bin.close()
        animate(Animation.create(7775))
        graphics(Graphics.create(1424))
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.COMPOST_MOUND_6871, NPCs.COMPOST_MOUND_6872)
    }

    inner class CompostBucketPlugin : UseWithHandler(Items.BUCKET_1925) {

        @Throws(Throwable::class)
        override fun newInstance(arg: Any?): Plugin<Any> {
            addHandler(NPCs.COMPOST_MOUND_6871, NPC_TYPE, this)
            addHandler(NPCs.COMPOST_MOUND_6872, NPC_TYPE, this)
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
            Item(Items.COMPOST_6032),
            Item(Items.SUPERCOMPOST_6034),
            Item(Items.POTATO_SEED_5318),
            Item(Items.ONION_SEED_5319),
            Item(Items.CABBAGE_SEED_5324),
            Item(Items.TOMATO_SEED_5322),
            Item(Items.SWEETCORN_SEED_5320),
            Item(Items.STRAWBERRY_SEED_5323),
            Item(Items.WATERMELON_SEED_5321),
            Item(Items.BARLEY_SEED_5305),
            Item(Items.HAMMERSTONE_SEED_5307),
            Item(Items.ASGARNIAN_SEED_5308),
            Item(Items.JUTE_SEED_5306),
            Item(Items.YANILLIAN_SEED_5309),
            Item(Items.KRANDORIAN_SEED_5310),
            Item(Items.WILDBLOOD_SEED_5311),
            Item(Items.REDBERRY_SEED_5101),
            Item(Items.CADAVABERRY_SEED_5102),
            Item(Items.DWELLBERRY_SEED_5103),
            Item(Items.JANGERBERRY_SEED_5104),
            Item(Items.WHITEBERRY_SEED_5105),
            Item(Items.POISON_IVY_SEED_5106),
            Item(Items.MARIGOLD_SEED_5096),
            Item(Items.ROSEMARY_SEED_5097),
            Item(Items.NASTURTIUM_SEED_5098),
            Item(Items.WOAD_SEED_5099),
            Item(Items.LIMPWURT_SEED_5100),
            Item(Items.GUAM_SEED_5291),
            Item(Items.MARRENTILL_SEED_5292),
            Item(Items.TARROMIN_SEED_5293),
            Item(Items.HARRALANDER_SEED_5294),
            Item(Items.RANARR_SEED_5295),
            Item(Items.SPIRIT_WEED_SEED_12176),
            Item(Items.TOADFLAX_SEED_5296),
            Item(Items.AVANTOE_SEED_5298),
            Item(Items.KWUARM_SEED_5299),
            Item(Items.SNAPDRAGON_SEED_5300),
            Item(Items.CADANTINE_SEED_5301),
            Item(Items.LANTADYME_SEED_5302),
            Item(Items.DWARF_WEED_SEED_5303),
            Item(Items.TORSTOL_SEED_5304)
        )
    }
}
