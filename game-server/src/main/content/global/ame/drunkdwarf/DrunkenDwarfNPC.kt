package content.global.ame.drunkdwarf

import content.global.ame.RandomEventNPC
import core.api.addItemOrDrop
import core.api.getWorldTicks
import core.api.playGlobalAudio
import core.api.sendChat
import core.api.utils.WeightBasedTable
import core.game.dialogue.FaceAnim
import core.game.dialogue.SequenceDialogue.dialogue
import core.game.node.entity.npc.NPC
import core.game.system.timer.impl.AntiMacro
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Sounds

/**
 * Represents the Drunken Dwarf NPC.
 * @author Zerken (October 13, 2023)
 */
class DrunkenDwarfNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.DRUNKEN_DWARF_956) {

    private val phrases = arrayOf(
        "'Ere, matey, 'ave some 'o the good stuff.",
        "Dun ignore your matey!",
        "I hates you @name!",
        "Aww comeon, talk to ikle me @name!"
    )
    private val dwarfWave = Animation(Animations.DWARF_WAVE_105)
    private var attackPhrase = false
    private var attackDelay = 0
    private var lastPhraseTime = 0

    private fun sendPhrases() {
        if (getWorldTicks() > lastPhraseTime + 5) {
            playGlobalAudio(this.location, Sounds.DWARF_WHISTLE_2297)
            sendChat(
                this,
                phrases.random().replace("@name",
                    player.username.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }),
            )
            this.face(player)
            lastPhraseTime = getWorldTicks()
        }
    }

    override fun init() {
        super.init()
        playGlobalAudio(this.location, Sounds.DWARF_WHISTLE_2297)
        sendChat(this, "'Ello der ${player.username.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }}! *hic*")
        this.animate(dwarfWave, 2)
    }

    override fun tick() {
        if (RandomFunction.roll(20) && !attackPhrase) sendPhrases()
        if (ticksLeft <= 10) {
            ticksLeft = 10
            if (!attackPhrase) {
                sendChat("I hates you, ${player.username.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }}!").also {
                    attackPhrase = true
                }
            }
            if (attackDelay <= getWorldTicks()) this.attack(player)
        }
        super.tick()
    }

    override fun talkTo(npc: NPC) {
        attackDelay = getWorldTicks() + 10
        this.pulseManager.clear()
        dialogue(player) {
            npc(npc.id, FaceAnim.OLD_DRUNK_RIGHT, "I 'new it were you matey! 'Ere, have some ob the good stuff!")
            end {
                addItemOrDrop(player, Items.BEER_1917)
                addItemOrDrop(player, Items.KEBAB_1971)
                AntiMacro.terminateEventNpc(player)
            }
        }
    }
}
