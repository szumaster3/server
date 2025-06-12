package content.region.kandarin.ardougne.quest.tol.cutscene

import content.region.kandarin.ardougne.quest.tol.plugin.TowerOfLifeUtils
import core.api.setAttribute
import core.game.activity.Cutscene
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.world.GameWorld

class TowerOfLifeCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> dialogueUpdate("What on " + GameWorld.settings!!.name + " is...")
            1 -> dialogueUpdate("It is time, my friends! The culmination of our work is complete!")
            2 -> dialogueUpdate("Long time indeed!")
            3 -> dialogueUpdate("Many hours we have worked!")
            4 ->
                dialogueUpdate(
                    "A great many years of planning and preperation has brought us here, my fellow alchemists. Now we have... The Tower of Life! We can already create gold, we can already transform matter from one thing to",
                )
            5 ->
                dialogueUpdate(
                    "another, and now we have the ability to create life itself! We owe much to the power of Guthix, for without the life essence he left below the ground, we would never have been able to bring this tower alive!",
                )
            6 -> dialogueUpdate("They're insane!")
            7 -> dialogueUpdate("It begins! The first day of creation!")

            8 -> dialogueUpdate("It worked!")
            9 -> dialogueUpdate("Marvellous!")
            10 -> dialogueUpdate("How is this possible?")
            11 -> dialogueUpdate("It's alive!")
            12 -> dialogueUpdate("We did it. A Homunculus!")
            13 -> dialogueUpdate("Beautiful")

            14 -> dialogueUpdate("It can talk! A real success.")
            15 -> dialogueUpdate("Where me? Skin burn...arrgh...eyes hurt...let me go!")
            16 -> dialogueUpdate("Excellent, it can fell! It's self-aware. This is really a great day.")
            17 -> dialogueUpdate("Whyeee cage? Argh...please help me! Graaahhh!")
            18 -> dialogueUpdate("This is terrible, you must do something!")
            19 -> dialogueUpdate("Ah, it's our helper. Thank you for fixing the tower; we will reward you greatly.")
            20 -> dialogueUpdate("I don't want a reward, just let it go. Can't you see it's in pain?")
            21 -> dialogueUpdate("Don't worry. It has no soul; it has no worth.")
            22 -> dialogueUpdate("Yes, it's just an experiment in alchemy.")
            23 -> dialogueUpdate("The creature must be grateful for his creation.")
            24 -> dialogueUpdate("You create hurt. Not fair, let me free!")
            25 -> dialogueUpdate("Silence, creature!")
            26 -> dialogueUpdate("Yes, be still and quit your moaning.")
            27 -> dialogueUpdate("No, mean, nasty people.")
            28 -> dialogueUpdate("Shut up, you pathetic being.")
            29 -> dialogueUpdate("Arghh! Let me go!")
            30 -> dialogueUpdate("Don't make us hurt you.")
            31 -> dialogueUpdate("Stop this! Let it go!")
            32 -> dialogueUpdate("So horrible beings. Daarrrr!")
            33 -> dialogueUpdate("It's getting angry.")
            34 -> dialogueUpdate("You should know your place, adventurer. This is no concern of yours now.")
            35 -> dialogueUpdate("You should be satisfied and leave. Then we can begin our experiments.")
            36 -> dialogueUpdate("Not wise.")
            37 -> dialogueUpdate("Arghhh!")
            38 -> dialogueUpdate("Oh no...")
            39 -> dialogueUpdate("Get out of the tower before it's too late!")
            40 -> dialogueUpdate("Run away!")
            41 -> dialogueUpdate("Flee for your lives!")

            42 ->
                playerDialogueUpdate(
                    FaceAnim.FRIENDLY,
                    "They've run away. I must go confront those alchemists downstairs.",
                )

            43 -> setAttribute(player, TowerOfLifeUtils.TOL_CUTSCENE, true)
        }
    }
}
