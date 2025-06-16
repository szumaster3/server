package content.minigame.sorceressgarden.dialogue

import content.data.GameAttributes
import content.region.misthalin.draynor.quest.swept.plugin.SweptUtils
import core.api.*
import core.api.quest.hasRequirement
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class SorceressApprenticeDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (player.getSavedData().globalData.hasSpokenToApprentice()) {
            player(FaceAnim.HALF_GUILTY, "Hey apprentice, do you want to try out", "your teleport skills again?")
        } else {
            if (isQuestComplete(player, Quests.SWEPT_AWAY) && inInventory(player, Items.BROOMSTICK_14057)) {
                options("Hello. What are you doing?", "Could you enchant this broom for me?")
                stage = 100
            }
            player("Hello. What are you doing?")
            stage = 10
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.HALF_GUILTY, "Okay, here goes - and remember, to", "return just drink from the fountain.").also { stage++ }
            1 -> {
                teleport(npc, player)
                end()
            }
            10 -> npc("Cleaning, cleaning, always cleaning. This apprenticeship", "isn't all that it was cracked up to be.").also { stage++ }
            11 -> player("Whose apprentice are you?").also { stage++ }
            12 -> npc("Oh, Aadeela, the sorceress upstairs, said she'd teach me", "magic, she did. And here I am scrubbing floors without", "a spell to help me.").also { stage++ }
            13 -> options("I could cast a Water Blast or a Wind Blast spell?", "Surely there must be upsides to the task?").also { stage++ }
            14 -> when (buttonId) {
                1 -> player("I could cast a Water Blast or a Wind Blast spell to", "hurry things along if you'd like?").also { stage++ }
                2 -> player("Surely there must be upsides to the task?").also { stage = 19 }
            }
            15 -> npc("No, no, she'd kill me or worse if she knew I was using", "Magic to do chores. Her last apprentice - well I'd", "rather not say.").also { stage++ }
            16 -> player("Oh go on, what happened to them?").also { stage++ }
            17 -> npc("They say she turned them into little spiders.").also { stage++ }
            18 -> player("Oh, that's too bad. I had better leave you to it.").also { stage = END_DIALOGUE }
            19 -> npc("Nope. Clean this, clean that. When I'm finished cleaning", "here I have to go help out in the garden.").also { stage++ }
            20 -> player("What garden?").also { stage++ }
            21 -> npc("Oh, I shouldn't have told you.").also { stage++ }
            22 -> options("You're right, you shouldn't have.", "Oh, you can talk to me. I can see you're having a bad day.").also { stage++ }
            23 -> when (buttonId) {
                1 -> player("You're right, you shouldn't have.").also { stage = END_DIALOGUE }
                2 -> player("Oh, you can talk to me. I can see you're having a bad", "day.").also { stage++ }
            }
            24 -> npc("You know you're right. Nobody listens to me.").also { stage++ }
            25 -> player("A sympathetic ear can do wonders.").also { stage++ }
            26 -> npc("Yes, if I just let my frustrations out, I'd feel a lot", "better. Now what was I saying?").also { stage++ }
            27 -> player("You mentioned something about the garden.").also { stage++ }
            28 -> npc("Oh yeah, the dreadful garden of hers.").also { stage++ }
            29 -> player("Where is it?").also { stage++ }
            30 -> npc("Oh, it's nowhere.").also { stage++ }
            31 -> player("What do you mean?").also { stage++ }
            32 -> npc("Well it's here, but not really. You see the sorceress is", "trying out some new type of compression magic.").also { stage++ }
            33 -> player("Oh, that sounds interesting - so how does it work?").also { stage++ }
            34 -> npc("It would take too long to explain and, to be honest, I", "don't really understand how it works.").also { stage++ }
            35 -> player("Fair enough, but tell me, how do you get to the", "garden?").also { stage++ }
            36 -> npc("By magic! The sorceress did teach me one spell.").also { stage++ }
            37 -> options("Wow, cast the spell on me. It will be good Magic training for you.", "Oh, that's nice. Well it's been great talking to you.").also { stage++ }
            38 -> when (buttonId) {
                1 -> player("Wow, cast the spell on me. It will be good Magic", "training for you.").also { stage++ }
                2 -> player("Oh, that's nice. Well it's been great", "talking to you.").also { stage = END_DIALOGUE }
            }
            39 -> npc("You wouldn't mind?").also { stage++ }
            40 -> player("Of course not. I'd be glad to help.").also { stage++ }
            41 -> npc("Okay, here goes! Remember, to return, just drink from", "the fountain.").also { stage++ }
            42 -> {
                player.getSavedData().globalData.setApprentice(true)
                teleport(npc, player)
                end()
            }
            100 -> when (buttonId) {
                1 -> player("Cleaning, cleaning, always cleaning. This apprenticeship", "isn't all that it was cracked up to be.").also { stage = 11 }
                2 -> player("Could you enchant this broom for me?").also { stage++ }
            }
            101 -> if (!isQuestComplete(player, Quests.PRINCE_ALI_RESCUE) && !getAttribute(player, GameAttributes.TALK_ABOUT_SQ_IRKJUICE, false)) {
                npc("Not right now; I have too much housework to do at the", "moment.").also { stage++ }
            } else {
                npc("Ah, of course. I'll just cast this little spell on it and then", "you can teleport to Sorceress's Garden. All you need to", "do is wield the broom and operate it.").also { stage = 103 }
            }
            102 -> {
                end()
                sendDoubleItemDialogue(player, -1, Items.BROOMSTICK_14057, "You must have finished Prince Ali Rescue and have talked to Osman before Sorceress's apprentice will help you.")
            }
            103 -> {
                end()
                lock(player, 1)
                player("Excellent! Many thanks.")
                visualize(player, -1, SweptUtils.BROOM_ENCHANTMENT_GFX)
                removeAttribute(player, GameAttributes.TALK_ABOUT_SQ_IRKJUICE)
                setAttribute(player, GameAttributes.BROOM_ENCHANTMENT_TP, true)
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.APPRENTICE_5532)

    companion object {
        fun teleport(
            npc: NPC,
            player: Player,
        ) {
            if (!hasRequirement(player, Quests.PRINCE_ALI_RESCUE)) return
            npc.faceTemporary(player, 4)
            npc.graphics(Graphics(108))
            player.lock()
            Projectile.create(npc, player, 109).send()
            npc.sendChat("Senventior Disthinte Molesko!")
            Pulser.submit(
                object : Pulse(1) {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            2 -> player.properties.teleportLocation = Location.create(2912, 5474, 0)
                            3 -> {
                                // player.graphics(Graphics(110))
                                player.unlock()
                                return true
                            }
                        }
                        return false
                    }
                },
            )
        }
    }
}
