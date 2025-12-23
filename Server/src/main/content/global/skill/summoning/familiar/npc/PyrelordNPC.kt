package content.global.skill.summoning.familiar.npc

import content.global.skill.crafting.CraftingDefinition
import content.global.skill.firemaking.FireMakingPlugin
import content.global.skill.firemaking.items.Log
import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.api.finishDiaryTask
import core.api.lock
import core.api.sendMessage
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
import shared.consts.Items
import shared.consts.NPCs

@Initializable
class PyrelordNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.PYRELORD_7377) :
    Familiar(owner, id, 3200, Items.PYRELORD_POUCH_12816, 6, WeaponInterface.STYLE_AGGRESSIVE) {

    init {
        boosts.add(SkillBonus(Skills.FIREMAKING, 3.0))
    }

    override fun construct(owner: Player, id: Int): Familiar {
        return PyrelordNPC(owner, id)
    }

    public override fun configureFamiliar() {
        definePlugin(PyrelordAbility())
    }


    override fun specialMove(special: FamiliarSpecial): Boolean {
        val item = special.node as Item

        if (item.id != Items.GOLD_BAR_2357 && item.id != Items.PERFECT_GOLD_BAR_2365) {
            sendMessage(owner, "You can only use this special on gold bars.")
            return false
        }

        lock(owner, 1)
        visualize(
            Animation.create(8081),
            Graphics.create(shared.consts.Graphics.RAINING_FLAMES_1463)
        )
        CraftingDefinition.openGoldJewelleryInterface(owner)
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PYRELORD_7377, NPCs.PYRELORD_7378)
    }


    inner class PyrelordAbility : UseWithHandler(*intArrayOf(Items.LOGS_1511, Items.OAK_LOGS_1521, Items.WILLOW_LOGS_1519, Items.MAPLE_LOGS_1517, Items.YEW_LOGS_1515, Items.MAGIC_LOGS_1513, Items.ACHEY_TREE_LOGS_2862, Items.PYRE_LOGS_3438, Items.OAK_PYRE_LOGS_3440, Items.WILLOW_PYRE_LOGS_3442, Items.MAPLE_PYRE_LOGS_3444, Items.YEW_PYRE_LOGS_3446, Items.MAGIC_PYRE_LOGS_3448, Items.TEAK_PYRE_LOGS_6211, Items.MAHOGANY_PYRE_LOG_6213, Items.MAHOGANY_LOGS_6332, Items.TEAK_LOGS_6333, Items.RED_LOGS_7404, Items.GREEN_LOGS_7405, Items.BLUE_LOGS_7406, Items.PURPLE_LOGS_10329, Items.WHITE_LOGS_10328, Items.SCRAPEY_TREE_LOGS_8934, Items.DREAM_LOG_9067, Items.ARCTIC_PYRE_LOGS_10808, Items.ARCTIC_PINE_LOGS_10810, Items.SPLIT_LOG_10812, Items.WINDSWEPT_LOGS_11035, Items.EUCALYPTUS_LOGS_12581, Items.EUCALYPTUS_PYRE_LOGS_12583, Items.JOGRE_BONES_3125)) {
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
                        SceneryBuilder.add(`object`, log.life, FireMakingPlugin.getAsh(player, log, `object`))
                        if (player.viewport.region!!.id == 10806) {
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
