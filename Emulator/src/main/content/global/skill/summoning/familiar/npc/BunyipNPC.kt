package content.global.skill.summoning.familiar.npc

import content.data.consumables.Consumables
import content.global.skill.cooking.CookableItems
import content.global.skill.gathering.fishing.Fish
import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.game.consumable.Consumable
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.GameWorld
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class BunyipNPC : Familiar {

    companion object {
        private val FISH = intArrayOf(Items.RAW_SHRIMPS_317, Items.RAW_SARDINE_327, Items.RAW_KARAMBWANJI_3150, Items.RAW_HERRING_345, Items.RAW_ANCHOVIES_321, Items.RAW_MACKEREL_353, Items.RAW_TROUT_335, Items.RAW_COD_341, Items.RAW_PIKE_349, Items.SLIMY_EEL_3379, Items.RAW_SALMON_331, Items.FROG_SPAWN_5004, Items.RAW_TUNA_359, Items.RAW_RAINBOW_FISH_10138, Items.RAW_CAVE_EEL_5001, Items.RAW_LOBSTER_377, Items.RAW_BASS_363, Items.RAW_SWORDFISH_371, Items.RAW_LAVA_EEL_2148, Items.RAW_MONKFISH_7944, Items.RAW_KARAMBWAN_3142, Items.RAW_SHARK_383, Items.RAW_SEA_TURTLE_395, Items.RAW_MANTA_RAY_389, Items.SEAWEED_401, Items.CASKET_405, Items.OYSTER_407)
    }

    private var lastHeal: Int = 0

    constructor() : this(null, NPCs.BUNYIP_6813)

    constructor(owner: Player?, id: Int) : super(owner, id, 4400, Items.BUNYIP_POUCH_12029, 3, WeaponInterface.STYLE_ACCURATE) {
        setLastHeal()
    }

    override fun construct(owner: Player?, id: Int): Familiar = BunyipNPC(owner, id)

    override fun tick() {
        super.tick()
        if (lastHeal < GameWorld.ticks) {
            setLastHeal()
            owner?.graphics(Graphics.create(1507), 1)
            owner?.skills?.heal(2)
        }
    }

    override fun isPoisonImmune(): Boolean = true

    private fun setLastHeal() {
        lastHeal = GameWorld.ticks + (15 / 0.6).toInt()
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        val fish = special.item?.let { Fish.forItem(it) }
        val player = owner ?: return false

        if (fish == null) {
            player.sendMessage("You can't use this special on an object like that.")
            return false
        }

        val consumable: Consumable? = Consumables.getConsumableById(special.item!!.id + 2)?.consumable
        if (consumable == null) {
            player.sendMessage("Error: Report to admin.")
            return false
        }

        if (player.skills.getLevel(Skills.COOKING) < CookableItems.forId(special.item!!.id)!!.level) {
            player.sendMessage("You need a Cooking level of at least ${CookableItems.forId(special.item!!.id)?.level} in order to do that.")
            return false
        }

        if (player.inventory.remove(special.item)) {
            animate(Animation.create(7747))
            graphics(Graphics.create(1481))
            val healthEffectValue = consumable.getHealthEffectValue(player)
            if (healthEffectValue > 0) {
                owner?.skills?.heal(healthEffectValue)
            } else {
                owner?.impactHandler?.manualHit(player, healthEffectValue, ImpactHandler.HitsplatType.NORMAL)
            }
        }
        return true
    }

    override fun visualizeSpecialMove() {
        owner?.visualize(Animation.create(7660), Graphics.create(1316))
    }

    override fun handleFamiliarTick() {
        // no-op
    }

    override fun configureFamiliar() {
        UseWithHandler.addHandler(NPCs.BUNYIP_6813, UseWithHandler.NPC_TYPE, object : UseWithHandler(*FISH) {
            override fun newInstance(arg: Any?): Plugin<Any> {
                addHandler(NPCs.BUNYIP_6814, UseWithHandler.NPC_TYPE, this)
                return this
            }

            override fun handle(event: NodeUsageEvent): Boolean {
                val player = event.player
                val fish = Fish.forItem(event.usedItem) ?: return true
                val consumable = Consumables.getConsumableById(fish.id + 2)?.consumable ?: return true

                player.lock(1)
                val runes = Item(555, RandomFunction.random(1, consumable.getHealthEffectValue(player)))

                if (player.inventory.remove(event.usedItem)) {
                    player.animate(Animation.create(2779))
                    Projectile.create(player, event.usedWith.asNpc(), 1435).send()
                    player.inventory.add(runes)
                    player.sendMessage("The bunyip transmutes the fish into some water runes.")
                }
                return true
            }
        })
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.BUNYIP_6813, NPCs.BUNYIP_6814)
}
