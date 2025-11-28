package content.region.morytania.mort_myre.npc

import content.data.consumables.Consumables
import content.global.activity.creation.SatchelPlugin
import content.region.morytania.mort_myre.quest.druidspirit.plugin.NSUtils
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
import shared.consts.Animations
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Sounds

@Initializable
class GhastNPC : AbstractNPC {
    constructor() : super(NPCs.GHAST_1052, null, true) {}
    private constructor(id: Int, location: Location) : super(id, location) {}

    private val supportRange = 10

    override fun construct(id: Int, location: Location, vararg objects: Any?): AbstractNPC {
        isAggressive = id != ids[0]
        return GhastNPC(id, location)
    }

    override fun handleTickActions() {
        super.handleTickActions()

        val playersInRange = RegionManager.getLocalPlayers(this, supportRange).filter { !it.inCombat() }

        if (id == ids[0] && RandomFunction.roll(35) && playersInRange.isNotEmpty()) {
            val player = playersInRange.random()
            submitIndividualPulse(this, object : MovementPulse(this, player) {
                override fun pulse(): Boolean {
                    playAudio(player, Sounds.GHAST_APPEAR_432)
                    animate(Animation(1093))
                    attemptLifeSiphon(player)
                    return true
                }
            })
        } else {
            val ticksTransformed = getWorldTicks() - getAttribute(this, "woke", 0)
            if (!inCombat() && ticksTransformed > 10) {
                reTransform()
            }
        }

        val currentTick = getWorldTicks()
        for (player in playersInRange) {
            val hasSickle =
                inInventory(player, Items.SILVER_SICKLEB_2963) || inEquipment(player, Items.SILVER_SICKLEB_2963)
            if (!hasSickle) continue

            val lastRepel = getAttribute(player, "lastRepelTick", 0)
            if (currentTick - lastRepel < 100) continue

            setAttribute(player, "lastRepelTick", currentTick)

            player.face(this)
            animate(player, Animations.DEFEND_MACE_SHIELD_404)
            sendChat(this.asNpc(), "OOooooohhh")
            sendMessage(player, "The power of the blessed silver sickle prevents the swamp from decaying you.")
        }
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.GHAST_1052, NPCs.GHAST_1053)

    fun attemptLifeSiphon(player: Player) {
        // Pouch effect takes priority.
        if (NSUtils.activatePouch(player, this)) return

        // Check normal food in inventory.
        val foodInInventory = player.inventory.toArray()
            .filterNotNull()
            .firstOrNull { item ->
                val consumable = Consumables.getConsumableById(item.id)
                consumable?.consumable is Food
            }

        if (foodInInventory != null) {
            playAudio(player, Sounds.FOOD_ROT_1494)
            removeItem(player, foodInInventory, Container.INVENTORY)
            addItem(player, Items.ROTTEN_FOOD_2959)
            sendMessage(player, "You feel something attacking your backpack, and smell a terrible stench.")
            return
        }

        // Check food in satchel.
        val foodInSatchel = (
                player.inventory.toArray().filterNotNull() +
                        player.equipment.toArray().filterNotNull()
                ).firstOrNull { it.id in SatchelPlugin.SATCHEL_IDS }

        if (foodInSatchel != null) {
            val contents = SatchelPlugin.decodeList(getCharge(foodInSatchel))

            if (contents.isNotEmpty()) {
                // Pick random food to rot
                val foodToRot = contents.random()
                contents.remove(foodToRot)
                SatchelPlugin.encodeList(foodInSatchel, contents)
                addItem(player, Items.ROTTEN_FOOD_2959)
                sendMessage(player, "You feel something attacking your satchel, and smell a terrible stench.")
                playAudio(player, Sounds.FOOD_ROT_1494)
                return
            }
        }

        // No food.
        if (RandomFunction.roll(3)) {
            sendMessage(player, "An attacking Ghast just misses you.")
        } else {
            impact(player, RandomFunction.random(3, 6), ImpactHandler.HitsplatType.NORMAL)
            sendMessage(player, "A supernatural force draws energy from you.")
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
