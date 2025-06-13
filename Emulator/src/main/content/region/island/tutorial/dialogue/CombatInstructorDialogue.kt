package content.region.island.tutorial.dialogue

import content.region.island.tutorial.plugin.TutorialStage
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class CombatInstructorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0)) {
            44 -> playerl(FaceAnim.FRIENDLY, "Hi! My name's ${player.username}.")
            47 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Very good, but that little butter knife isn't going to protect you much. Here, take these.",
                )

            53 -> playerl(FaceAnim.FRIENDLY, "I did it! I killed a giant rat!")
            54 -> {
                player.dialogueInterpreter.sendDoubleItemMessage(
                    Items.SHORTBOW_841,
                    Items.BRONZE_ARROW_882,
                    "The Combat Guide gives you some bronze arrows and a shortbow!",
                )
                if (!inInventory(player, Items.SHORTBOW_841) && !inEquipment(player, Items.SHORTBOW_841)) {
                    addItem(player, Items.SHORTBOW_841)
                }
                if (!inInventory(player, Items.BRONZE_ARROW_882) && !inEquipment(player, Items.BRONZE_ARROW_882)) {
                    addItem(player, Items.BRONZE_ARROW_882, 30)
                }
            }

            in 55..100 -> npcl(FaceAnim.FRIENDLY, "Do you need something?")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0)) {
            44 ->
                when (stage) {
                    0 ->
                        npcl(
                            FaceAnim.ANGRY,
                            "Do I look like I care? To me you're just another newcomer who thinks they're ready to fight.",
                        ).also {
                            stage++
                        }

                    1 -> npcl(FaceAnim.FRIENDLY, "I'm Vannaka, the greatest swordsman alive.").also { stage++ }
                    2 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Let's get started by teaching you to wield a weapon.",
                        ).also { stage++ }

                    3 -> {
                        end()
                        setAttribute(player, TutorialStage.TUTORIAL_STAGE, 45)
                        TutorialStage.load(player, 45)
                    }
                }

            47 ->
                when (stage) {
                    0 -> {
                        addItemOrDrop(player, Items.BRONZE_SWORD_1277)
                        addItemOrDrop(player, Items.WOODEN_SHIELD_1171)
                        sendDoubleItemDialogue(
                            player,
                            Items.BRONZE_SWORD_1277,
                            Items.WOODEN_SHIELD_1171,
                            "The Combat Guide gives you a <col=08088A>bronze sword</col> and a <col=08088A>wooden shield</col>!",
                        )
                        stage++
                    }

                    1 -> {
                        end()
                        setAttribute(player, TutorialStage.TUTORIAL_STAGE, 48)
                        TutorialStage.load(player, 48)
                    }
                }

            53 ->
                when (stage) {
                    0 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I saw, ${player.username}. You seem better at this than I thought. Now that you have grasped basic swordplay, let's move on.",
                        ).also {
                            stage++
                        }

                    1 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Let's try some ranged attacking, with this you can kill foes from a distance. Also, foes unable to reach you are as good as dead. You'll be able to attack the rats, without entering the pit.",
                        ).also {
                            stage++
                        }

                    2 -> {
                        sendDoubleItemDialogue(
                            player,
                            Items.SHORTBOW_841,
                            Items.BRONZE_ARROW_882,
                            "The Combat Guide gives you some bronze arrows and a shortbow!",
                        )
                        if (!inInventory(player, Items.SHORTBOW_841) && !inEquipment(player, Items.SHORTBOW_841)) {
                            addItem(player, Items.SHORTBOW_841)
                        }
                        if (!inInventory(player, Items.BRONZE_ARROW_882) &&
                            !inEquipment(player, Items.BRONZE_ARROW_882)
                        ) {
                            addItem(player, Items.BRONZE_ARROW_882, 30)
                        }
                        stage++
                    }

                    3 -> {
                        end()
                        setAttribute(player, TutorialStage.TUTORIAL_STAGE, 54)
                        TutorialStage.load(player, 54)
                    }
                }

            in 55..100 -> when (stage) {
                0 -> options("Tell me about combat stats.", "Tell me about melee combat again.", "Tell me about ranged combat again.", "Tell me about the Wilderness.",  "Nope, I'm ready to move on!").also { stage++ }
                1 -> when (buttonId) {
                    1 -> player("Tell me about combat stats again.").also { stage++ }
                    2 -> player("Tell me about melee combat again.").also { stage = 11 }
                    3 -> player("Tell me about ranged combat again.").also { stage = 17 }
                    4 -> player("Tell me about the Wilderness.").also { stage = 23 }
                    5 -> player("Nope, I'm ready to move on!").also { stage = 38 }
                }
                2 -> npcl(FaceAnim.FRIENDLY, "Certainly. You have three main combat stats: Strength, Defence and Attack.").also { stage++ }
                3 -> npcl(FaceAnim.FRIENDLY, "Strength determines the maximum hit you will be able to deal with your blows, Defence determines the amount of damage you will be able to defend and Attack determines the accuracy of your blows.").also { stage++ }
                4 -> npcl(FaceAnim.FRIENDLY, "Other stats are used in combat such as Prayer, Hitpoints, Magic and Ranged. All of these stats can go towards determining your combat level, which is shown near the top of your combat interface screen.").also { stage++ }
                5 -> npcl(FaceAnim.FRIENDLY, "You will find out on the mainland that certain items can also affect your stats. There are potions that can be drunk that can alter your stats temporarily, such as raising Strength.").also { stage++ }
                6 -> npcl(FaceAnim.FRIENDLY, "You will also raise your Defence and Attack values by using different weapons and armours.").also { stage++ }
                7 -> npcl(FaceAnim.FRIENDLY, "Before going into combat with an opponent it is wise to put the mouse over them and see what combat level they are.").also { stage++ }
                8 -> npcl(FaceAnim.FRIENDLY, "Green coloured writing usually means it will be an easy fight for you, red means you will probably lose, yellow means they are around your level and orange means they are slightly stronger.").also { stage++ }
                9 -> npcl(FaceAnim.FRIENDLY, "Sometimes things will go your way, sometimes they won't. There is no such thing as a guaranteed win, but if the odds are on your side, you stand the best chance of walking away victorious.").also { stage++ }
                10 -> npcl(FaceAnim.FRIENDLY, "Now, was there something else you wanted to hear about again?").also { stage = 0 }

                11 -> npcl(FaceAnim.FRIENDLY, "Ah, my speciality. Close combat fighting, which is also known as melee fighting, covers all combat done at close range to your opponent.").also { stage++ }
                12 -> npcl(FaceAnim.FRIENDLY, "Melee fighting depends entirely upon your three basic combat stats: Attack, Defence, and Strength.").also { stage++ }
                13 -> npcl(FaceAnim.FRIENDLY, "Also, of course, it depends on the quality of your armour and weaponry. A high-levelled fighter with good armour and weapons will be deadly up close.").also { stage++ }
                14 -> npcl(FaceAnim.FRIENDLY, "If this is the path you wish to take, remember your success depends on getting as close to your enemy as quickly as possible.").also { stage++ }
                15 -> npcl(FaceAnim.FRIENDLY, "Personally, I consider the fine art of melee combat to be the ONLY combat method worth using.").also { stage++ }
                16 -> npcl(FaceAnim.FRIENDLY, "Now, did you want to hear anything else?").also { stage = 0 }

                17 -> npcl(FaceAnim.FRIENDLY, "Thinking of being a ranger, eh? Well, okay. I don't enjoy it myself, but I can see the appeal.").also { stage++ }
                18 -> npcl(FaceAnim.FRIENDLY, "Ranging employs a lot of different weapons as a skill, not just the shortbow you have there. Spears, throwing knives, and crossbows are all used best at a distance from your enemy.").also { stage++ }
                19 -> npcl(FaceAnim.FRIENDLY, "Now, those rats were easy pickings, but on the mainland you will be very lucky if you can find a spot where you can shoot at your enemies without them being able to retaliate.").also { stage++ }
                20 -> npcl(FaceAnim.FRIENDLY, "At close range, rangers often do badly in combat. Your best tactic as a ranger is to hit and run, keeping your foe at a distance.").also { stage++ }
                21 -> npcl(FaceAnim.FRIENDLY, "Your effectiveness as a ranger is almost entirely dependent on your Ranged stat. As with all skills, the more you train it, the more powerful it will become.").also { stage++ }
                22 -> npcl(FaceAnim.FRIENDLY, "Anything else you wanted to know?").also { stage = 0 }

                23 -> npcl(FaceAnim.FRIENDLY, "Ah yes, the Wilderness. It is a place of evil, mark my words. Many is the colleague I have lost in that foul place.").also { stage++ }
                24 -> npcl(FaceAnim.FRIENDLY, "It is also a place of both adventure and wealth, so if you are brave enough and strong enough to survive it, you will make a killing. Literally!").also { stage++ }
                25 -> npcl(FaceAnim.FRIENDLY, "It is also the only place in the land of ${GameWorld.settings!!.name} where players are able to attack each other at will, and as such is the haunt of many Player Killers, or PKers if you will.").also { stage++ }
                26 -> npcl(FaceAnim.FRIENDLY, "There are a few things different in the Wilderness in comparison to the rest of the lands of ${GameWorld.settings!!.name}. Firstly, as I just mentioned, you can and will be attacked by other players.").also { stage++ }
                27 -> npcl(FaceAnim.FRIENDLY, "For this reason, you will be given a warning when you approach the Wilderness, as it is not a place you would wish to enter by accident.").also { stage++ }
                28 -> npcl(FaceAnim.FRIENDLY, "Secondly, there are a number of 'levels' to it. The further into it you travel, the greater the range of people you can attack.").also { stage++ }
                29 -> npcl(FaceAnim.FRIENDLY, "In level 1 wilderness you will only be able to attack, or be attacked by, those players within one combat level of yourself.").also { stage++ }
                30 -> npcl(FaceAnim.FRIENDLY, "In level 50, any player within fifty levels of you will be able to attack, or be attacked by you. Always keep an eye on what level of the Wilderness you are currently in.").also { stage++ }
                31 -> npcl(FaceAnim.FRIENDLY, "Your current Wilderness level is shown at the bottom right of the screen. The final thing you should know about the Wilderness is being 'skulled'.").also { stage++ }
                32 -> npcl(FaceAnim.FRIENDLY, "If you attack another player without them having attacked first, you will gain a skull above your character's head.").also { stage++ }
                33 -> npcl(FaceAnim.FRIENDLY, "What this means is that if you die while skulled, you will lose EVERYTHING that your character was carrying.").also { stage++ }
                34 -> npcl(FaceAnim.FRIENDLY, "When skulled, you should try to avoid dying for the twenty minutes or so it will take for the skull to wear off.").also { stage++ }
                35 -> npcl(FaceAnim.FRIENDLY, "If you wish to find the Wilderness, head north from where you start on the mainland. It is rather large and hard to miss.").also { stage++ }
                36 -> npcl(FaceAnim.FRIENDLY, "If you don't wish to end up there, take notice of the warning you will receive when getting near to it.").also { stage++ }
                37 -> npcl(FaceAnim.FRIENDLY, "Now, was there anything more?").also { stage = 0 }

                38 -> npcl(FaceAnim.FRIENDLY, "Okay then.").also { stage++ }
                39 -> TutorialStage.rollback(player!!)

            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = CombatInstructorDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.COMBAT_INSTRUCTOR_944)
}
