package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.api.playAudio
import core.api.sendMessage
import core.game.activity.CutscenePlugin
import core.game.dialogue.Dialogue
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.path.Pathfinder
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Sounds

/**
 * The type Spirit wolf npc.
 */
@Initializable
class SpiritWolfNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.SPIRIT_WOLF_6829) :
    Familiar(owner, id, 600, Items.SPIRIT_WOLF_POUCH_12047, 3, WeaponInterface.STYLE_ACCURATE) {
    private var cutscene: CutscenePlugin? = null

    /**
     * Instantiates a new Spirit wolf npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    /**
     * Instantiates a new Spirit wolf npc.
     */
    init {
        if (owner != null) {
            cutscene = owner.getAttribute<Any>("in-cutscene", null) as? CutscenePlugin
        }
    }

    override fun construct(owner: Player, id: Int): Familiar {
        return SpiritWolfNPC(owner, id)
    }

    override fun init() {
        super.init()
        if (cutscene != null) {
            properties.teleportLocation = cutscene!!.base.transform(44, 52, 1)
            faceLocation(cutscene!!.base.transform(42, 53, 1))
        }
    }

    override fun startFollowing() {
        if (cutscene != null) {
            return
        }
        super.startFollowing()
    }

    override fun call(): Boolean {
        if (cutscene != null) {
            return true
        }
        return super.call()
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        if (special.node !is NPC) {
            sendMessage(owner, "You can only target monsters with this special move.")
            return false
        }
        val npc = special.node as NPC
        if (npc.walkRadius > 20) { // Usually indicates special NPC.
            sendMessage(owner, "This monster won't get intimidated by your familiar.")
            return false
        }
        if (cutscene != null) {
            if (npc.id != 6990) {
                sendMessage(owner, "You can't do this right now.")
                return false
            }
            val dial = owner.getAttribute<Dialogue>("wolf-dial", null)
            dial?.handle(0, 0) ?: cutscene!!.stop(true)
            return false
        }
        if (!canAttack(npc)) {
            return false
        }
        visualizeSpecialMove()
        playAudio(owner, Sounds.WOLF_HOWL2_4265)
        faceTemporary(npc, owner, 2)
        super.visualize(Animation.create(8293), Graphics(1334, 96))
        Projectile.magic(this, npc, 1333, 40, 36, 50, 5).send()
        Pulser.submit(object : Pulse(2, this, npc) {
            override fun pulse(): Boolean {
                npc.faceTemporary(this@SpiritWolfNPC, 2)
                val destination = npc.location.transform(Direction.getLogicalDirection(location, npc.location), 3)
                val path = Pathfinder.find(npc, destination)
                path.walk(npc)
                return true
            }
        })
        return true
    }

    override fun getSpawnLocation(): Location {
        if (cutscene != null) {
            return cutscene!!.base.transform(44, 52, 1)
        }
        return super.getSpawnLocation()
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SPIRIT_WOLF_6829, NPCs.SPIRIT_WOLF_6830)
    }
}
