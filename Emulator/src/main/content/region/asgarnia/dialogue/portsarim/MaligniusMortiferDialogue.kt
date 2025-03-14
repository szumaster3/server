package content.region.asgarnia.dialogue.portsarim

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class MaligniusMortiferDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc("So, " + player.username + ", your curiosity leads you to speak to me?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                options(
                    "Who are you and what are you doing here?",
                    "Can you teach me something about magic?",
                    "Where can I get clothes like those?",
                    "Actually, I don't want to talk to you.",
                )
                stage = 1
            }

            1 ->
                when (buttonId) {
                    1 -> {
                        player("Who are you and what are you doing here?")
                        stage = 10
                    }

                    2 -> {
                        player("Can you teach me something about magic?")
                        stage = 20
                    }

                    3 -> {
                        player("Where can I get clothes like those?")
                        stage = 30
                    }

                    4 -> {
                        player("Actually, I don't want to talk to you.")
                        stage = 40
                    }
                }

            10 -> {
                npc(
                    "I am the great Malignius Mortifer, wielder of strange",
                    "and terrible powers. These lowly followers of mine are",
                    "dedicated students of the magical arts. Their business is",
                    "to follow me and learn all they can.",
                )
                stage = 11
            }

            11 -> {
                player("There don't look very tough.")
                stage = 12
            }

            12 -> {
                npc("You may believe that, but even if you strike one down,", "another will rise up within minutes.")
                stage = 13
            }

            13 -> {
                player("Yeah, right.")
                stage = 14
            }

            14 -> {
                npc(
                    "Each of my followers is a master of his chosen element.",
                    "His life becomes bound to that element in a way you",
                    "could not hope to understand.",
                )
                stage = 15
            }

            15 -> {
                player("And what do you do?")
                stage = 16
            }

            16 -> {
                npc(
                    "I am mastering a branch of magic that few dare to",
                    "attempt: Necromancy! THe fools in the Guild of",
                    "Wizards shun anyone who practices this art, but there",
                    "are a few across the lands who know the rudiments.",
                )
                stage = 17
            }

            17 -> {
                npc(
                    "Grayzag and Invrigar... Even Melzar studied the",
                    "methods of necromancy, until an accident affected his",
                    "mind. Now his spells tend to result in...",
                )
                stage = 18
            }

            18 -> {
                npc("... well, let us simply say that he does NOT raise", "armies of undead minions.")
                stage = 19
            }

            19 -> end()
            20 -> {
                npc(
                    "Ah, you are an inquisitive young fellow. I shall speak of",
                    "the great Wizards' Tower, destroyed by fire many",
                    "years ago...",
                )
                stage = 21
            }

            21 -> {
                npc(
                    "Many say it was the greatest building in the history of",
                    "Gielinor, a magnificent monument to human ingenuity.",
                )
                stage = 22
            }

            22 -> {
                npc("Yet when humans are offered great power, they so", "often buy it at the cost of their principles.")
                stage = 23
            }

            23 -> {
                npc(
                    "Wizards who claimed allegiance to Saradomin began to",
                    "insist that magic be restriced to the few they deemed",
                    "'worthy' of such powers.",
                )
                stage = 24
            }

            24 -> {
                npc(
                    "Before long, those who did not share their fatuous",
                    "obsession with Saradomin were excluded from the",
                    "Tower comletely. This state of affairs could not",
                    "continue.",
                )
                stage = 25
            }

            25 -> end()
            30 -> {
                npc(
                    "Bah! Our garments are an outward sign of our",
                    "dominance of the magical arts. You cannot simply buy",
                    "them in a shop.",
                )
                stage = 31
            }

            31 -> {
                player("What happens if I kill you and take them?")
                stage = 32
            }

            32 -> {
                npc("Try it and see!")
                stage = 33
            }

            33 -> {
                player("How about if you teach me enough about magic so I", "can wear those clothes too?")
                stage = 34
            }

            34 -> {
                npc("How about if I turn you into a mushroom to make you", "stop bothering me?")
                stage = 35
            }

            35 -> transform()
            40 -> {
                npc("Bah! Then go away!")
                stage = 41
            }

            41 -> end()
        }
        return true
    }

    private fun transform() {
        interpreter.sendDialogues(player, null, true, "MMMmmph!")
        npc.animate(Animation.create(811))
        player.appearance.transformNPC(3345)
        player.graphics(Graphics.create(453))
        player.lock(8)
        player.locks.lockMovement(10000)
        Pulser.submit(
            object : Pulse(12) {
                override fun pulse(): Boolean {
                    player.walkingQueue.reset()
                    player.locks.unlockMovement()
                    player.appearance.transformNPC(-1)
                    end()
                    return true
                }
            },
        )
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MALIGNIUS_MORTIFER_2713)
    }
}
