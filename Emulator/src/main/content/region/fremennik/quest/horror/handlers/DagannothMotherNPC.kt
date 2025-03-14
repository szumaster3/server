package content.region.fremennik.quest.horror.handlers

import content.region.fremennik.quest.horror.JossikLighthouseDialogue
import core.api.*
import core.api.quest.finishQuest
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class DagannothMotherNPC(
    id: Int = 0,
    location: Location? = null,
    session: DagannothSession? = null,
) : AbstractNPC(id, location) {
    private val airSpells = intArrayOf(1, 10, 24, 45)
    private val waterSpells = intArrayOf(4, 14, 27, 48)
    private val earthSpells = intArrayOf(6, 17, 33, 52)
    private val fireSpells = intArrayOf(8, 20, 38, 55)

    val session: DagannothSession?

    var type: DagannothType?

    var isSpawned = false

    init {
        this.isWalks = true
        this.session = session
        this.isRespawn = false
        type = DagannothType.forId(id)
    }

    override fun init() {
        super.init()
        if (session?.player?.location?.regionId == 10056) {
            Pulser.submit(DagannothTransform(session.player, this))
        } else {
            session?.close()
        }
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
        if (isSpawned && !properties.combatPulse.isAttacking) {
            properties.combatPulse.attack(session.player)
            this.getSkills().isLifepointsUpdate = false
        }

        if (RandomFunction.random(35) == 5) {
            type!!.transform(this, session.player)
            playAudio(session.player, 1617)
        }
        return
    }

    override fun checkImpact(state: BattleState) {
        if (state.attacker is Player) {
            if (state.victim is NPC) {
                if (type!!.npcId == NPCs.DAGANNOTH_MOTHER_1351) {
                    if (state.style != CombatStyle.MAGIC) {
                        state.neutralizeHits()
                        return
                    }
                    if (state.spell == null) {
                        state.neutralizeHits()
                        return
                    }
                    val spell = state.spell
                    for (id in airSpells) {
                        if (id == spell.spellId) {
                            state.estimatedHit = state.maximumHit
                            return
                        }
                    }
                    state.neutralizeHits()
                }

                if (type!!.npcId == NPCs.DAGANNOTH_MOTHER_1352) {
                    if (state.style != CombatStyle.MAGIC) {
                        state.neutralizeHits()
                        return
                    }
                    if (state.spell == null) {
                        state.neutralizeHits()
                        return
                    }
                    val spell = state.spell
                    for (id in waterSpells) {
                        if (id == spell.spellId) {
                            state.estimatedHit = state.maximumHit
                            return
                        }
                    }
                    state.neutralizeHits()
                }

                if (type!!.npcId == NPCs.DAGANNOTH_MOTHER_1353) {
                    if (state.style != CombatStyle.MAGIC) {
                        state.neutralizeHits()
                        return
                    }
                    if (state.spell == null) {
                        state.neutralizeHits()
                        return
                    }
                    val spell = state.spell
                    for (id in fireSpells) {
                        if (id == spell.spellId) {
                            state.estimatedHit = state.maximumHit
                            return
                        }
                    }
                    state.neutralizeHits()
                }

                if (type!!.npcId == NPCs.DAGANNOTH_MOTHER_1354) {
                    if (state.style != CombatStyle.MAGIC) {
                        state.neutralizeHits()
                        return
                    }
                    if (state.spell == null) {
                        state.neutralizeHits()
                        return
                    }
                    val spell = state.spell
                    for (id in earthSpells) {
                        if (id == spell.spellId) {
                            state.estimatedHit = state.maximumHit
                            return
                        }
                    }
                    state.neutralizeHits()
                }

                if (type!!.npcId == NPCs.DAGANNOTH_MOTHER_1355) {
                    if (state.style != CombatStyle.RANGE) {
                        state.neutralizeHits()
                        return
                    }
                    if (state.style == CombatStyle.RANGE) {
                        state.estimatedHit = state.maximumHit
                        return
                    }
                    state.neutralizeHits()
                }

                if (type!!.npcId == NPCs.DAGANNOTH_MOTHER_1356) {
                    if (state.style != CombatStyle.MELEE) {
                        state.neutralizeHits()
                        return
                    }
                    if (state.style == CombatStyle.MELEE) {
                        state.estimatedHit = state.maximumHit
                        return
                    }
                    state.neutralizeHits()
                }
            }
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        super.finalizeDeath(killer)
        if (killer is Player) {
            clearHintIcon(killer)
            val hasCasket = hasAnItem(killer, Items.RUSTY_CASKET_3849).container != null
            teleport(killer, Location.create(2515, 4625, 1))
            finishQuest(killer, Quests.HORROR_FROM_THE_DEEP)
            lock(killer, 10)
            lockInteractions(killer, 10)
            if (!hasCasket) {
                addItemOrDrop(killer, Items.RUSTY_CASKET_3849)
            }
            runTask(killer, 5) {
                closeInterface(killer)
                openDialogue(killer, JossikLighthouseDialogue())
            }
        }
        clear()
        super.finalizeDeath(killer)
    }

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return DagannothMotherNPC(id, location, null)
    }

    override fun isAttackable(
        entity: Entity,
        style: CombatStyle,
        message: Boolean,
    ): Boolean {
        if (session == null) {
            return false
        }
        return session.player == entity
    }

    override fun canSelectTarget(target: Entity): Boolean {
        if (target is Player) {
            if (target != session!!.player) {
                return false
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.DAGANNOTH_MOTHER_1351,
            NPCs.DAGANNOTH_MOTHER_1352,
            NPCs.DAGANNOTH_MOTHER_1353,
            NPCs.DAGANNOTH_MOTHER_1354,
            NPCs.DAGANNOTH_MOTHER_1355,
            NPCs.DAGANNOTH_MOTHER_1356,
        )
    }

    enum class DagannothType(
        var npcId: Int,
        var sendChat: String?,
        var sendMessage: String?,
    ) {
        WHITE(NPCs.DAGANNOTH_MOTHER_1351, "Tktktktktktkt", null),
        BLUE(NPCs.DAGANNOTH_MOTHER_1352, "Krrrrrrk", "the dagannoth changes to blue..."),
        RED(NPCs.DAGANNOTH_MOTHER_1353, "Sssssrrrkkkkk", "the dagannoth changes to red..."),
        BROWN(NPCs.DAGANNOTH_MOTHER_1354, "Krrrrrrssssssss", "the dagannoth changes to brown..."),
        GREEN(NPCs.DAGANNOTH_MOTHER_1355, "Krkrkrkrkrkrkrkr", "the dagannoth changes to green..."),
        ORANGE(NPCs.DAGANNOTH_MOTHER_1356, "Chkhkhkhkhk", "the dagannoth changes to orange..."),
        ;

        fun transform(
            dagannoth: DagannothMotherNPC,
            player: Player,
        ) {
            val newType = next()
            val oldHp = dagannoth.getSkills().lifepoints
            dagannoth.type = newType
            dagannoth.transform(newType.npcId)
            dagannoth.skills.isLifepointsUpdate = false
            Pulser.submit(DagannothTransform(player, dagannoth))
            dagannoth.getSkills().setLifepoints(oldHp)
        }

        operator fun next(): DagannothType {
            return values().random()
        }

        companion object {
            fun forId(id: Int): DagannothType? {
                for (type in values()) {
                    if (type.npcId == id) {
                        return type
                    }
                }
                return null
            }
        }
    }

    class DagannothTransform(
        val player: Player?,
        val dagannoth: DagannothMotherNPC,
    ) : Pulse() {
        var counter = 0

        override fun pulse(): Boolean {
            when (counter++) {
                0 -> {
                    registerHintIcon(player!!, dagannoth)
                    player.sendMessage(dagannoth.type?.sendMessage)
                    dagannoth.attack(player).also { dagannoth.sendChat(dagannoth.type?.sendChat) }
                }
            }
            return false
        }
    }
}
