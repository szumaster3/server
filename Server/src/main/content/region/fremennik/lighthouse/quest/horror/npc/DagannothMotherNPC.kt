package content.region.fremennik.lighthouse.quest.horror.npc

import core.api.*
import core.api.finishQuest
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.tools.RandomFunction
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

class DagannothMotherNPC(id: Int = 0, location: Location? = null, session: DagannothSession? = null) : AbstractNPC(id, location) {

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
        val attacker = state.attacker
        val victim = state.victim

        if (attacker !is Player || victim !is NPC) return

        val npcId = type?.npcId ?: return

        val dagannothPhases = mapOf(
            NPCs.DAGANNOTH_MOTHER_1351 to (CombatStyle.MAGIC to airSpells),
            NPCs.DAGANNOTH_MOTHER_1352 to (CombatStyle.MAGIC to waterSpells),
            NPCs.DAGANNOTH_MOTHER_1353 to (CombatStyle.MAGIC to fireSpells),
            NPCs.DAGANNOTH_MOTHER_1354 to (CombatStyle.MAGIC to earthSpells),
            NPCs.DAGANNOTH_MOTHER_1355 to (CombatStyle.RANGE to null),
            NPCs.DAGANNOTH_MOTHER_1356 to (CombatStyle.MELEE to null)
        )

        val (requiredStyle, allowedSpells) = dagannothPhases[npcId] ?: return

        if (state.style != requiredStyle) {
            state.neutralizeHits()
            return
        }

        if (requiredStyle == CombatStyle.MAGIC) {
            val spellId = state.spell?.spellId
            if (spellId == null || allowedSpells?.contains(spellId) == false) {
                state.neutralizeHits()
                return
            }
        }

        state.estimatedHit = state.maximumHit
    }

    override fun finalizeDeath(killer: Entity?) {
        super.finalizeDeath(killer)
        if (killer is Player) {
            clearHintIcon(killer)
            val hasCasket = hasAnItem(killer, Items.RUSTY_CASKET_3849).container != null

            queueScript(killer, 1, QueueStrength.SOFT) {
                teleport(killer, Location(2515, 10000, 1), TeleportManager.TeleportType.INSTANT)
                finishQuest(killer, Quests.HORROR_FROM_THE_DEEP)
                if (!hasCasket) {
                    addItemOrDrop(killer, Items.RUSTY_CASKET_3849)
                }
                return@queueScript stopExecuting(killer)
            }
        }
        clear()
        super.finalizeDeath(killer)
    }

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = DagannothMotherNPC(id, location, null)

    override fun isAttackable(entity: Entity, style: CombatStyle, message: Boolean): Boolean {
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

    override fun getIds(): IntArray =
        intArrayOf(
            NPCs.DAGANNOTH_MOTHER_1351,
            NPCs.DAGANNOTH_MOTHER_1352,
            NPCs.DAGANNOTH_MOTHER_1353,
            NPCs.DAGANNOTH_MOTHER_1354,
            NPCs.DAGANNOTH_MOTHER_1355,
            NPCs.DAGANNOTH_MOTHER_1356,
        )

    enum class DagannothType(var npcId: Int, var sendChat: String?, var sendMessage: String?) {
        WHITE(NPCs.DAGANNOTH_MOTHER_1351, "Tktktktktktkt", null),
        BLUE(NPCs.DAGANNOTH_MOTHER_1352, "Krrrrrrk", "the dagannoth changes to blue..."),
        RED(NPCs.DAGANNOTH_MOTHER_1353, "Sssssrrrkkkkk", "the dagannoth changes to red..."),
        BROWN(NPCs.DAGANNOTH_MOTHER_1354, "Krrrrrrssssssss", "the dagannoth changes to brown..."),
        GREEN(NPCs.DAGANNOTH_MOTHER_1355, "Krkrkrkrkrkrkrkr", "the dagannoth changes to green..."),
        ORANGE(NPCs.DAGANNOTH_MOTHER_1356, "Chkhkhkhkhk", "the dagannoth changes to orange..."),
        ;

        fun transform(dagannoth: DagannothMotherNPC, player: Player) {
            val newType = next()
            val oldHp = dagannoth.getSkills().lifepoints
            dagannoth.type = newType
            dagannoth.transform(newType.npcId)
            dagannoth.skills.isLifepointsUpdate = false
            Pulser.submit(DagannothTransform(player, dagannoth))
            dagannoth.getSkills().updateHitpoints(oldHp)
        }

        operator fun next(): DagannothType = values().random()

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

    class DagannothTransform(val player: Player?, val dagannoth: DagannothMotherNPC) : Pulse() {
        var counter = 0

        override fun pulse(): Boolean {
            when (counter++) {
                0 -> {
                    registerHintIcon(player!!, dagannoth)
                    dagannoth.type?.sendMessage?.let { sendMessage(player, it) }
                    dagannoth.attack(player).also { dagannoth.sendChat(dagannoth.type?.sendChat) }
                }
            }
            return false
        }
    }
}
