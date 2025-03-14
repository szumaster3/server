package content.region.morytania.handlers.npc

import content.data.consumables.Consumables
import content.global.handlers.item.SatchelListener
import content.region.morytania.quest.druidspirit.NSUtils
import core.api.*
import core.game.consumable.Food
import core.game.interaction.MovementPulse
import core.game.node.entity.Entity
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.RandomFunction
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Sounds

@Initializable
class GhastNPC : AbstractNPC {
    constructor() : super(NPCs.GHAST_1052, null, true) {}
    private constructor(id: Int, location: Location) : super(id, location) {}

    val BASE_CHARGE_AMOUNT = 1000
    private val supportRange = 10

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any?,
    ): AbstractNPC {
        isAggressive = id != ids[0]
        return GhastNPC(id, location)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (id == ids[0] && RandomFunction.roll(35)) {
            val players = RegionManager.getLocalPlayers(this, supportRange).filter { !it.inCombat() }
            if (players.isNotEmpty()) {
                val player = players.random()
                submitIndividualPulse(
                    this,
                    object : MovementPulse(this, player) {
                        override fun pulse(): Boolean {
                            playAudio(player, Sounds.GHAST_APPEAR_432)
                            animate(Animation(1093))
                            attemptLifeSiphon(player)
                            return true
                        }
                    },
                )
            }
        } else {
            val ticksTransformed = getWorldTicks() - getAttribute(this, "woke", 0)
            if (!inCombat() && ticksTransformed > 10) {
                reTransform()
            }
        }
        val playersInRange = RegionManager.getLocalPlayers(this, supportRange).filter { !it.inCombat() }
        for (player in playersInRange) {
            if (inInventory(player, Items.SILVER_SICKLEB_2963) || inEquipment(player, Items.SILVER_SICKLEB_2963)) {
                player.face(findLocalNPC(player, this.id)?.asNpc())
                animate(player, Animations.DEFEND_MACE_SHIELD_404)
                sendChat(findLocalNPC(player, this.id)!!, "OOooooohhh")
                sendMessage(player, "The power of the blessed silver sickle prevents the swamp from decaying you.")
                break
            }
        }
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.GHAST_1052, NPCs.GHAST_1053)

    fun attemptLifeSiphon(player: Player) {
        if (NSUtils.activatePouch(player, this)) return

        val rottenFoodItem = Items.ROTTEN_FOOD_2959
        val inventoryItems = player.inventory.toArray().filterNotNull()

        GlobalScope.launch {
            val foodInInventory =
                inventoryItems.firstOrNull { item ->
                    val consumable = Consumables.getConsumableById(item.id)
                    consumable?.consumable is Food
                }

            if (foodInInventory != null) {
                removeItem(player, foodInInventory, Container.INVENTORY)
                addItem(player, rottenFoodItem)
                sendMessage(player, "You feel something attacking your backpack, and smell a terrible stench.")
                return@launch
            }

            val foodInSatchel =
                inventoryItems.firstOrNull { item ->
                    item.id in SatchelListener.SATCHEL_IDS && getCharge(item) >= BASE_CHARGE_AMOUNT
                }

            if (foodInSatchel != null) {
                val chargesAmount = getCharge(foodInSatchel)
                val foodFound =
                    SatchelListener.SATCHEL_RESOURCES.firstOrNull { foodId ->
                        chargesAmount >= (foodId + BASE_CHARGE_AMOUNT)
                    }

                if (foodFound != null) {
                    addItem(player, rottenFoodItem)
                    adjustCharge(foodInSatchel, -(foodFound + BASE_CHARGE_AMOUNT))
                    sendMessage(player, "You feel something attacking your satchel, and smell a terrible stench.")
                    return@launch
                }
            }

            if (RandomFunction.roll(3)) {
                sendMessage(player, "An attacking Ghast just misses you.")
            } else {
                impact(player, RandomFunction.random(3, 6), ImpactHandler.HitsplatType.NORMAL)
                sendMessage(player, "A supernatural force draws energy from you.")
            }
        }
    }

    override fun commenceDeath(killer: Entity?) {
        super.commenceDeath(killer)
    }

    override fun finalizeDeath(killer: Entity?) {
        super.finalizeDeath(killer)
        if (id == ids[1]) {
            reTransform()
            if (killer is Player) {
                NSUtils.incrementGhastKC(killer)
                rewardXP(killer, Skills.PRAYER, 30.0)
                removeAttribute("woke")
            }
        }
    }
}
