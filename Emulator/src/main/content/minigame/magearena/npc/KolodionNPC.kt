package content.minigame.magearena.npc

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.MagicSwingHandler
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs.consts.NPCs

class KolodionNPC(id: Int = 0, location: Location? = null, session: KolodionSession? = null) : AbstractNPC(id, location) {

    val session: KolodionSession?
    var type: KolodionType?
    var isCommenced: Boolean = false

    init {
        this.isWalks = true
        this.session = session
        this.isRespawn = false
        this.type = KolodionType.forId(id)
    }

    override fun init() {
        super.init()
        setRandomSpell()
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (session == null) {
            return
        }
        if (!session.player.isActive) {
            clear()
            return
        }
        if (isCommenced && !properties.combatPulse.isAttacking) {
            properties.combatPulse.attack(session.player)
        }
    }

    override fun startDeath(killer: Entity) {
        if (killer === session!!.player) {
            type!!.transform(this, session!!.player)
            return
        }
        super.startDeath(killer)
    }

    fun setRandomSpell() {
        val spell = SpellBook.MODERN.getSpell(SPELL_IDS[RandomFunction.random(SPELL_IDS.size)]) as CombatSpell?
        properties.spell = spell
        properties.autocastSpell = spell
    }

    override fun getSwingHandler(swing: Boolean): CombatSwingHandler = SWING_HANDLER

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = KolodionNPC(id, location, null)

    override fun isAttackable(entity: Entity, style: CombatStyle, message: Boolean): Boolean {
        if (style != CombatStyle.MAGIC) {
            return false
        }
        if (session == null) {
            return false
        }
        if (session.player !== entity) {
            return false
        }
        return true
    }

    override fun canSelectTarget(target: Entity): Boolean {
        if (target is Player) {
            if (target !== session!!.player) {
                return false
            }
        }
        return true
    }

    override fun getIds(): IntArray =
        intArrayOf(NPCs.KOLODION_907, NPCs.KOLODION_908, NPCs.KOLODION_909, NPCs.KOLODION_910, NPCs.KOLODION_911)

    enum class KolodionType(val npcId: Int, val appearAnimation: Animation?, val graphcId: Int, val appearMessage: String?) {
        HUMAN(NPCs.KOLODION_907, Animation(6941), -1, "You must prove yourself... now!"),
        OGRE(NPCs.KOLODION_908, Animation(6941), 188, "This is only the beginning; you can't beat me!"),
        SPIDER(NPCs.KOLODION_909, Animation(5324), 190, "Foolish mortal; I am unstoppable."),
        GHOST(NPCs.KOLODION_910, Animation(715), 188, "Now you feel it.. The dark energy."),
        DEMON(NPCs.KOLODION_911, Animation(4623), 190, "Aaaaaaaarrgghhhh! The power!"),
        END(NPCs.KOLODION_906, Animation(6941), 188, null),
        ;

        fun transform(kolodion: KolodionNPC, player: Player) {
            val newType = next()
            kolodion.lock()
            kolodion.pulseManager.clear()
            kolodion.walkingQueue.reset()
            kolodion.impactHandler.disabledTicks = 50
            player.getSavedData().activityData.kolodionBoss = newType.ordinal
            if (newType == END) {
                player.lock()
            }
            player.lock(2)
            Pulser.submit(
                object : Pulse(1, kolodion, player) {
                    var counter: Int = 0

                    override fun pulse(): Boolean {
                        when (++counter) {
                            1 ->
                                if (newType != GHOST) {
                                    kolodion.animator.forceAnimation(kolodion.properties.deathAnimation)
                                }

                            3 ->
                                if (newType == GHOST) {
                                    kolodion.animator.forceAnimation(kolodion.properties.deathAnimation)
                                }

                            4 -> {
                                player.packetDispatch.sendPositionedGraphic(
                                    newType.graphcId,
                                    0,
                                    0,
                                    kolodion.getLocation(),
                                )
                                if (newType.appearAnimation != null) {
                                    kolodion.animate(newType.appearAnimation)
                                }
                            }

                            5 -> {
                                kolodion.unlock()
                                kolodion.animator.reset()
                                kolodion.fullRestore()
                                kolodion.type = newType
                                kolodion.transform(newType.npcId)
                                kolodion.impactHandler.disabledTicks = 1
                                if (newType != END) {
                                    kolodion.properties.combatPulse.attack(player)
                                }
                            }

                            6 -> {
                                if (newType.appearMessage != null) {
                                    kolodion.sendChat(newType.appearMessage)
                                }
                                if (newType == END) {
                                    return false
                                }
                                return true
                            }

                            7 -> {
                                player.unlock()
                                player.getSavedData().activityData.kolodionStage = 2
                                player.properties.teleportLocation = Location(2540, 4717, 0)
                                return true
                            }
                        }
                        return false
                    }
                },
            )
        }

        fun next(): KolodionType = values()[ordinal + 1]

        companion object {
            fun forId(id: Int): KolodionType? {
                for (type in values()) {
                    if (type.npcId == id) {
                        return type
                    }
                }
                return null
            }
        }
    }

    companion object {
        private val SWING_HANDLER: CombatSwingHandler =
            object : MagicSwingHandler() {
                override fun impact(
                    entity: Entity?,
                    victim: Entity?,
                    state: BattleState?,
                ) {
                    super.impact(entity, victim, state)
                    if (RandomFunction.random(10) < 4) {
                        (entity as KolodionNPC?)!!.setRandomSpell()
                    }
                }
            }

        private val SPELL_IDS = intArrayOf(41, 42, 43)
    }
}
