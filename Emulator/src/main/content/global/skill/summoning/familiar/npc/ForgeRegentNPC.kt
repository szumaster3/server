package content.global.skill.summoning.familiar.npc

import content.global.skill.firemaking.FiremakingPulse
import content.global.skill.firemaking.Log
import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.api.finishDiaryTask
import core.api.sendMessage
import core.game.container.impl.EquipmentContainer
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.SkillBonus
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.RegionManager.getObject
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * The type Forge regent npc.
 */
@Initializable
class ForgeRegentNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.FORGE_REGENT_7335) :
    Familiar(owner, id, 4500, Items.FORGE_REGENT_POUCH_12782, 6, WeaponInterface.STYLE_RANGE_ACCURATE) {
    /**
     * Instantiates a new Forge regent npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    /**
     * Instantiates a new Forge regent npc.
     */
    init {
        boosts.add(SkillBonus(Skills.FIREMAKING, 4.0))
    }

    override fun construct(owner: Player, id: Int): Familiar {
        return ForgeRegentNPC(owner, id)
    }

    public override fun configureFamiliar() {
        definePlugin(ForgeRegentFiremaking())
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        if (special.target !is Player) {
            sendMessage(owner, "You can't use this special on an npc.")
            return false
        }
        val target = special.target.asPlayer()
        if (!canCombatSpecial(target)) {
            return false
        }
        if (target.inventory.freeSlots() < 1) {
            sendMessage(owner, "The target doesn't have enough inventory space.")
            return false
        }
        val weapon = target.equipment[EquipmentContainer.SLOT_WEAPON]
        val shield = target.equipment[EquipmentContainer.SLOT_SHIELD]
        if (weapon == null && shield == null) {
            sendMessage(owner, "The target doesn't have a weapon or shield.")
            return false
        }
        var remove: Item? = null
        while (remove == null) {
            remove = if (RandomFunction.random(2) == 1) {
                weapon
            } else {
                shield
            }
        }
        graphics(Graphics.create(1394))
        target.graphics(Graphics.create(1393))
        if (target.equipment.remove(remove)) {
            target.inventory.add(remove)
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FORGE_REGENT_7335, NPCs.FORGE_REGENT_7336)
    }

    /**
     * The type Forge regent firemake.
     */
    inner class ForgeRegentFiremaking
    /**
     * Instantiates a new Forge regent firemake.
     */
        : UseWithHandler(
        Items.LOGS_1511,
        Items.ACHEY_TREE_LOGS_2862,
        Items.OAK_LOGS_1521,
        Items.WILLOW_LOGS_1519,
        Items.TEAK_LOGS_6333,
        Items.ARCTIC_PINE_LOGS_10810,
        Items.MAPLE_LOGS_1517,
        Items.MAHOGANY_LOGS_6332,
        Items.EUCALYPTUS_LOGS_12581,
        Items.YEW_LOGS_1515,
        Items.MAGIC_LOGS_1513,
        Items.CURSED_MAGIC_LOGS_13567,
        Items.PURPLE_LOGS_10329,
        Items.WHITE_LOGS_10328,
        Items.BLUE_LOGS_7406,
        Items.GREEN_LOGS_7405,
        Items.RED_LOGS_7404
    ) {
        @Throws(Throwable::class)
        override fun newInstance(arg: Any?): Plugin<Any> {
            for (id in ids) {
                addHandler(id, NPC_TYPE, this)
            }
            return this
        }

        override fun handle(event: NodeUsageEvent): Boolean {
            val player = event.player
            val log = Log.forId(event.usedItem.id)
            val familiar = event.usedWith as Familiar
            val ticks = FIREMAKING_ANIMATION.definition.getDurationTicks()
            if (!player.familiarManager.isOwner(familiar)) {
                return true
            }
            if (getObject(familiar.location) != null || familiar.zoneMonitor.isInZone("bank")) {
                sendMessage(player, "You can't light a fire here.")
                return false
            }
            familiar.lock(ticks)
            familiar.animate(FIREMAKING_ANIMATION)
            if (player.inventory.remove(event.usedItem)) {
                val ground = GroundItemManager.create(event.usedItem, familiar.location, player)
                Pulser.submit(object : Pulse(ticks, player, familiar) {
                    override fun pulse(): Boolean {
                        if (!ground.isActive) {
                            return true
                        }
                        val `object` = Scenery(log!!.fireId, familiar.location)
                        familiar.moveStep()
                        GroundItemManager.destroy(ground)
                        player.getSkills().addExperience(Skills.FIREMAKING, log.xp + 10)
                        familiar.faceLocation(`object`.getFaceLocation(familiar.location))
                        SceneryBuilder.add(`object`, log.life, FiremakingPulse.getAsh(player, log, `object`))
                        if (player.viewport.region.id == 10806) {
                            finishDiaryTask(player, DiaryType.SEERS_VILLAGE, 1, 9)
                        }
                        return true
                    }
                })
            }
            return true
        }
    }

    companion object {
        private val FIREMAKING_ANIMATION: Animation = Animation.create(8085)
    }
}
