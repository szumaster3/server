package content.region.asgarnia.port_sarim.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.tools.END_DIALOGUE

class MaligniusMortiferDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npc(FaceAnim.NEUTRAL, "So, ${player!!.username}, your curiosity leads you to speak to me?").also { stage++ }

            1 -> showTopics(
                Topic("Who are you and what are you doing here?", 10),
                Topic("Can you teach me something about magic?", 20),
                Topic("Where can I get clothes like those?", 30),
                Topic("Actually, I don't want to talk to you.", 40)
            )

            10 -> npc(FaceAnim.FRIENDLY, "I am the great Malignius Mortifer, wielder of strange", "and terrible powers. These lowly followers of mine are", "dedicated students of the magical arts.").also { stage++ }
            11 -> player("They don't look very tough.").also { stage++ }
            12 -> npc("You may believe that, but even if you strike one down,", "another will rise up within minutes.").also { stage++ }
            13 -> player("Yeah, right.").also { stage++ }
            14 -> npc("Each of my followers is a master of his chosen element.", "His life becomes bound to that element in ways you", "could not understand.").also { stage++ }
            15 -> player("And what do you do?").also { stage++ }
            16 -> npc("I am mastering a branch of magic that few dare to", "attempt: Necromancy!").also { stage++ }
            17 -> npc("Grayzag and Invrigar... Even Melzar studied that art", "until an accident affected his mind.").also { stage++ }
            18 -> npc("Let us simply say he does NOT raise armies of undead.").also { stage++ }
            19 -> end()

            20 -> npc("Ah, you are an inquisitive young fellow. I shall speak of", "the great Wizards' Tower, destroyed by fire many years ago.").also { stage++ }
            21 -> npc("Many say it was the greatest building ever built —", "a monument to human ingenuity.").also { stage++ }
            22 -> npc("Yet humans often trade their principles for power.").also { stage++ }
            23 -> npc("Wizards loyal to Saradomin tried to restrict magic", "to those they deemed 'worthy'.").also { stage++ }
            24 -> npc("Those who disagreed were expelled. This tyranny could", "not continue.").also { stage++ }
            25 -> end()

            30 -> npc("Our garments are symbols of mastery over the magical arts.", "You cannot simply purchase them in a shop.").also { stage++ }
            31 -> player("What if I kill you and take them?").also { stage++ }
            32 -> npc("Try it and see!").also { stage++ }
            33 -> player("How about if you teach me enough about magic so I", "can wear those clothes too?").also { stage++ }
            34 -> npc("How about if I turn you into a mushroom to stop you", "bothering me?").also { stage = 35 }
            35 -> transform()

            40 -> npc("Bah! Then go away!").also { stage++ }
            41 -> stage = END_DIALOGUE
        }
    }

    private fun transform() {
        val p = player ?: return
        val n = npc ?: return

        p.dialogueInterpreter.sendDialogues(p, null, true, "MMMmmph!")

        n.animate(Animation.create(811))
        p.appearance.transformNPC(3345)
        p.graphics(Graphics.create(453))

        p.lock(8)
        p.locks.lockMovement(10000)

        Pulser.submit(object : Pulse(12) {
            override fun pulse(): Boolean {
                p.walkingQueue.reset()
                p.locks.unlockMovement()
                p.appearance.transformNPC(-1)
                end()
                return true
            }
        })
    }

}
