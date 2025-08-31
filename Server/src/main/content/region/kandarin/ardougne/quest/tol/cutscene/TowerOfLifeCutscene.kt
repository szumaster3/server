package content.region.kandarin.ardougne.quest.tol.cutscene

import content.data.GameAttributes
import core.api.setAttribute
import core.game.activity.Cutscene
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.world.GameWorld

class TowerOfLifeCutscene(player: Player) : Cutscene(player) {

    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0  -> dialogueUpdate(true, "What on " + GameWorld.settings!!.name + " is...")
            1  -> dialogueUpdate(true, "It is time, my friends! The culmination of our work is complete!")
            2  -> dialogueUpdate(true, "Long time indeed!")
            3  -> dialogueUpdate(true, "Many hours we have worked!")
            4  -> dialogueUpdate(true, "A great many years of planning and preparation has brought us here, my fellow alchemists. Now we have... The Tower of Life! We can already create gold, we can already transform matter from one thing to")
            5  -> dialogueUpdate(true, "another, and now we have the ability to create life itself! We owe much to the power of Guthix, for without the life essence he left below the ground, we would never have been able to bring this tower alive!")
            6  -> dialogueUpdate(true, "They're insane!")
            7  -> dialogueUpdate(true, "It begins! The first day of creation!")
            8  -> dialogueUpdate(true, "It worked!")
            9  -> dialogueUpdate(true, "Marvellous!")
            10 -> dialogueUpdate(true, "How is this possible?")
            11 -> dialogueUpdate(true, "It's alive!")
            12 -> dialogueUpdate(true, "We did it. A Homunculus!")
            13 -> dialogueUpdate(true, "Beautiful")
            14 -> dialogueUpdate(true, "It can talk! A real success.")
            15 -> dialogueUpdate(true, "Where me? Skin burn...arrgh...eyes hurt...let me go!")
            16 -> dialogueUpdate(true, "Excellent, it can fell! It's self-aware. This is really a great day.")
            17 -> dialogueUpdate(true, "Whyeee cage? Argh...please help me! Graaahhh!")
            18 -> dialogueUpdate(true, "This is terrible, you must do something!")
            19 -> dialogueUpdate(true, "Ah, it's our helper. Thank you for fixing the tower; we will reward you greatly.")
            20 -> dialogueUpdate(true, "I don't want a reward, just let it go. Can't you see it's in pain?")
            21 -> dialogueUpdate(true, "Don't worry. It has no soul; it has no worth.")
            22 -> dialogueUpdate(true, "Yes, it's just an experiment in alchemy.")
            23 -> dialogueUpdate(true, "The creature must be grateful for his creation.")
            24 -> dialogueUpdate(true, "You create hurt. Not fair, let me free!")
            25 -> dialogueUpdate(true, "Silence, creature!")
            26 -> dialogueUpdate(true, "Yes, be still and quit your moaning.")
            27 -> dialogueUpdate(true, "No, mean, nasty people.")
            28 -> dialogueUpdate(true, "Shut up, you pathetic being.")
            29 -> dialogueUpdate(true, "Arghh! Let me go!")
            30 -> dialogueUpdate(true, "Don't make us hurt you.")
            31 -> dialogueUpdate(true, "Stop this! Let it go!")
            32 -> dialogueUpdate(true, "So horrible beings. Daarrrr!")
            33 -> dialogueUpdate(true, "It's getting angry.")
            34 -> dialogueUpdate(true, "You should know your place, adventurer. This is no concern of yours now.")
            35 -> dialogueUpdate(true, "You should be satisfied and leave. Then we can begin our experiments.")
            36 -> dialogueUpdate(true, "Not wise.")
            37 -> dialogueUpdate(true, "Arghhh!")
            38 -> dialogueUpdate(true, "Oh no...")
            39 -> dialogueUpdate(true, "Get out of the tower before it's too late!")
            40 -> dialogueUpdate(true, "Run away!")
            41 -> dialogueUpdate(true, "Flee for your lives!")
            42 -> playerDialogueUpdate(FaceAnim.FRIENDLY, "They've run away. I must go confront those alchemists downstairs.")
            43 -> setAttribute(player, GameAttributes.TOL_CUTSCENE, true)
        }
    }
}
