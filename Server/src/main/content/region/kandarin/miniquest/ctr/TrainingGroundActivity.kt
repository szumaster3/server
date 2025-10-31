package content.region.kandarin.miniquest.ctr

import content.data.GameAttributes
import content.region.kandarin.miniquest.ctr.npc.KnightWavesNPC
import content.region.kandarin.miniquest.ctr.npc.MerlinKnightWavesNPC
import core.api.*
import core.game.activity.ActivityManager
import core.game.activity.ActivityPlugin
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.timer.impl.SkillRestore
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import core.plugin.ClassScanner
import core.plugin.Initializable

/**
 * Represents the Training Grounds activity.
 */
@Initializable
class TrainingGroundActivity : ActivityPlugin("Knight's training", true, false, true), MapArea {

    private var activity: TrainingGroundActivity? = this

    init {
        activity = this
        this.safeRespawn = Location.create(2750, 3507, 2)
    }

    override fun death(entity: Entity, killer: Entity): Boolean {
        if (entity is Player) {
            entity.getProperties().teleportLocation = Location.create(2751, 3507, 2)
            return true
        }
        return false
    }

    override fun areaEnter(entity: Entity) {
        super.areaEnter(entity)
        if (entity is Player) {
            setAttribute(entity, GameAttributes.PRAYER_LOCK, true)
            entity.hook(Event.PrayerDeactivated, SkillRestore.PrayerDeactivatedHook)
            registerLogoutListener(entity, "Knight's training") { _ ->
                entity.unhook(SkillRestore.PrayerDeactivatedHook)
                entity.hook(Event.PrayerActivated, SkillRestore.PrayerActivatedHook)
                teleport(entity, Location.create(2751, 3507, 2))
            }
        }
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        super.areaLeave(entity, logout)
        if (entity is Player) {
            removeAttributes(entity, GameAttributes.PRAYER_LOCK, GameAttributes.KW_SPAWN, GameAttributes.KW_TIER, GameAttributes.KW_BEGIN)
            findLocalNPC(entity, KnightWavesNPC().id)?.let { poofClear(it) }
            clearLogoutListener(entity, "Knight's training")
            entity.unhook(SkillRestore.PrayerActivatedHook)
            entity.unhook(SkillRestore.PrayerDeactivatedHook)
        }
    }

    override fun start(player: Player?, login: Boolean, vararg args: Any?): Boolean = super.start(player, login, *args)

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(ZoneBorders(2752, 3502, 2764, 3513, 2, false))

    override fun newInstance(p: Player?): ActivityPlugin {
        ActivityManager.register(this)
        ClassScanner.definePlugin(KnightWavesNPC())
        ClassScanner.definePlugin(MerlinKnightWavesNPC())
        return this
    }

    override fun getRestrictions(): Array<ZoneRestriction> = arrayOf(ZoneRestriction.CANNON, ZoneRestriction.RANDOM_EVENTS, ZoneRestriction.FOLLOWERS, ZoneRestriction.TELEPORT)
    override fun getSpawnLocation(): Location? = null
}
