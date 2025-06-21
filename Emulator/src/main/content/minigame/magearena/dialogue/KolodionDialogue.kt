package content.minigame.magearena.dialogue

import content.minigame.magearena.npc.KolodionSession
import core.api.animate
import core.api.getStatLevel
import core.api.visualize
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.GameWorld.settings
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Animations
import org.rs.consts.NPCs

@Initializable
class KolodionDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (player.getSavedData().activityData.hasStartedKolodion()) {
            player("Hi.")
            return true
        }
        if (player.getSavedData().activityData.hasKilledKolodion()) {
            player("Hello, Kolodion.")
            return true
        }
        player("Hello there. What is this place?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int, ): Boolean {
        if (player.getSavedData().activityData.hasStartedKolodion()) {
            when (stage) {
                0 -> npc("You return, young conjurer. You obviously have a", "taste for the dark side of magic.").also { stage++ }
                1 -> end().also { startFight(player) }
            }
            return true
        }
        if (player.getSavedData().activityData.hasReceivedKolodionReward()) {
            when (stage) {
                0 -> npc("Hey there, how are you? Are you enjoying the", "bloodshed?").also { stage++ }
                1 -> player("I think I've had enough for now.").also { stage++ }
                2 -> npc("A shame. You're a good battle mage. I hope", "to see you soon.").also { stage++ }
                3 -> end()
            }
            return true
        } else if (player.getSavedData().activityData.hasKilledKolodion()) {
            when (stage) {
                0 -> npc("Hello, you mage. You're a tough one.").also { stage++ }
                1 -> player("What now?").also { stage++ }
                2 -> npc("Step into the magic pool. It will take you to a chamber.", "There, you must decide which god you will represent in", "the arena.").also { stage++ }
                3 -> player("Thanks, Kolodion.").also { stage++ }
                4 -> npc("That's what I'm here for.").also { stage++ }
                5 -> end()
            }
            return true
        }
        when (stage) {
            0 -> if (getStatLevel(player, Skills.MAGIC) < 60) {
                npc("Do not waste my time with trivial questions. I am the", "Great Kolodion, master of battle magic. I have an arena", "to run.").also { stage++ }
            } else {
                npc("I am the great Kolodion, master of battle magic, and", "this is my battle arena. Top wizards travel from all over", settings!!.name + " to fight here.").also { stage = 4 }
            }
            1 -> player("Can I enter?").also { stage++ }
            2 -> npc("Hah! A wizard of your level? Don't be absurd.").also { stage++ }
            3 -> end()
            4 -> options("Can I fight here?", "What's the point of that?", "That's barbaric!").also { stage++ }
            5 -> when (buttonId) {
                1 -> player("Can I fight here?").also { stage++ }
                2 -> player("What's the point of that?").also { stage = 19 }
                3 -> player("That's barbaric!").also { stage = END_DIALOGUE }
            }
            6 -> npc("My arena is open to any high level wizard, but this is", "no game. Many wizards fall in this arena, never to rise", "again. The strongest mages have been destroyed.").also { stage++ }
            7 -> npc("If you're sure you want in?").also { stage++ }
            8 -> options("Yes indeedy.", "No I don't.").also { stage++ }
            9 -> when (buttonId) {
                1 -> player("Yes indeed.").also { stage++ }
                2 -> end()
            }
            10 -> npc("Good, good. You have a healthy sense of competition.").also { stage++ }
            11 -> npc("Remember, traveller - in my arena, hand-to-hand", "combat is useless. Your strength will diminish as you", "enter the arena, but the spells you can learn are", "amongst the most powerful in all of " + settings!!.name + ".").also { stage++ }
            12 -> npc("Before I can accept you in, we must duel.").also { stage++ }
            13 -> options("Okay, let's fight.", "No thanks.").also { stage++ }
            14 -> when (buttonId) {
                1 -> player("Okay, let's fight.").also { stage++ }
                2 -> end()
            }
            15 -> npc("I must first check that you are up to scratch.").also { stage++ }
            16 -> player("You don't need to worry about that.").also { stage++ }
            17 -> npc("Not just any magician can enter - only the most", "powerful and most feared. Before you can use the", "power of this arena, you must prove yourself against", "me.").also { stage++ }
            18 -> end().also { startFight(player) }
            19 -> npc("They want to crown themselves the best", "mage in all of " + settings!!.name + "!").also { stage = END_DIALOGUE }
        }
        return true
    }

    private fun startFight(player: Player) {
        player.getSavedData().activityData.kolodionStage = 1
        player.lock()
        animate(npc, Animation.create(Animations.HUMAN_CAST_SPELL_LONG_811))
        player.teleport(Location.create(3105, 3934, 0), 3)
        visualize(
            player,
            Animation.create(Animations.OLD_SHRINK_AND_RISE_UP_TP_1816),
            Graphics.create(org.rs.consts.Graphics.LIGHT_TP_GRAPHIC_301, 50),
        )
        KolodionSession.create(player).start()
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.KOLODION_905)
}
