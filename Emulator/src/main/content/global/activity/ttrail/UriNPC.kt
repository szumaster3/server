package content.global.activity.ttrail

import core.api.removeAttribute
import core.api.sendItemDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.RegionManager.getSpawnLocation
import core.plugin.ClassScanner.definePlugin
import core.plugin.Plugin
import org.rs.consts.Items
import org.rs.consts.NPCs

class UriNPC : AbstractNPC {
    private var clueScroll: ClueScrollPlugin? = null

    var player: Player? = null

    constructor() : super(0, null)

    constructor(id: Int, location: Location?) : super(id, location, false) {
        this.isRespawn = false
    }

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return UriNPC(id, location)
    }

    override fun init() {
        player = getAttribute("player", null)
        clueScroll = getAttribute("clue", null)

        player?.let {
            location = getSpawnLocation(it, this) ?: it.location
        }
        super.init()
    }

    override fun finalizeDeath(killer: Entity) {
        if (killer is Player && killer == player && isDoubleAgent) {
            killer.setAttribute("killed-agent", clueScroll!!.clueId)
        }
        super.finalizeDeath(killer)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        player?.let {
            if (it.location.getDistance(getLocation()) > 10 || !it.isActive) {
                clear()
            }
            if (isDoubleAgent && !properties.combatPulse.isAttacking) {
                properties.combatPulse.attack(it)
            }
        }
    }

    override fun isAttackable(
        entity: Entity,
        style: CombatStyle,
        message: Boolean,
    ): Boolean {
        return if (entity is Player && player == entity) {
            true
        } else {
            super.isAttackable(entity, style, message)
        }
    }

    override fun canSelectTarget(target: Entity): Boolean {
        return target == player
    }

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        definePlugin(UriDialogue())
        return super.newInstance(arg)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.URI_5141, NPCs.URI_5142, NPCs.URI_5143, NPCs.DOUBLE_AGENT_5144, NPCs.DOUBLE_AGENT_5145)
    }

    private val isDoubleAgent: Boolean
        get() = getAttribute("double-agent", false)

    class UriDialogue : Dialogue {
        constructor() : super()

        constructor(player: Player?) : super(player)

        override fun newInstance(player: Player): Dialogue {
            return UriDialogue(player)
        }

        override fun open(vararg args: Any): Boolean {
            npc = args[0] as NPC
            if (!canSpeak()) {
                npc("I do not believe we have any business, Comrade.")
                stage = -1
                return true
            }
            npc(QUOTES.random())
            return true
        }

        override fun handle(
            interfaceId: Int,
            buttonId: Int,
        ): Boolean {
            when (stage) {
                -1 -> end()
                0 -> {
                    player("What?")
                    asUri().clueScroll!!.reward(player)
                    stage++
                }

                1 -> {
                    sendItemDialogue(player, Items.CASKET_405, "You've been given a casket!")
                    stage++
                }

                2 -> end()
            }
            return true
        }

        override fun close(): Boolean {
            if (stage >= 1) {
                npc.clear()
                removeAttribute(player, "killed-agent")
            }
            return super.close()
        }

        private fun canSpeak(): Boolean {
            val scroll = asUri().clueScroll as EmoteClueScroll?
            return asUri().player == player &&
                player.getAttribute(
                    "commence-emote",
                    !scroll!!.hasCommencedEmote(),
                ) &&
                scroll.hasEquipment(player, scroll.equipment)
        }

        private fun asUri(): UriNPC {
            return npc as UriNPC
        }

        override fun getIds(): IntArray {
            return intArrayOf(
                NPCs.URI_5141,
                NPCs.URI_5142,
                NPCs.URI_5143,
                NPCs.DOUBLE_AGENT_5144,
                NPCs.DOUBLE_AGENT_5145,
            )
        }

        companion object {
            private val QUOTES =
                arrayOf(
                    "Once, I was a poor man, but then I found a party hat.",
                    "There were three goblins in a bar, which one left first?",
                    "Would you like to buy a pewter spoon?",
                    "In the end, only the three-legged survive.",
                    "I heard that the tall man fears only strong winds.",
                    "In Canifis the men are known for eating much spam.",
                    "I am the egg man, are you one of the egg men?",
                    "I believe that it is very rainy in Varrock.",
                    "The slowest of fishermen catch the swiftest of fish.",
                    "It is quite easy being green.",
                    "Don't forget to find the jade monkey.",
                    "Do you want ants? Because that's how you get ants.",
                    "I once named a duck after a girl. Big mistake.",
                    "Loser says what.",
                    "I'm looking for a girl named Molly. I can't find her.",
                    "Guys, let's lake dive!",
                    "I gave you what you needed; not what you think you needed.",
                    "Want to see me bend a spoon?",
                    "Is that Deziree?",
                    "This is the last night you'll spend alone.",
                    "(Breathing intensifies)",
                    "Init doe. Lyk, I hope yer reward iz goodd aye?",
                )
        }
    }
}
