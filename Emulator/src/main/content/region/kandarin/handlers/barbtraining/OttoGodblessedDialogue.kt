package content.region.kandarin.handlers.barbtraining

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class OttoGodblessedDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (getAttribute(player, BarbarianTraining.BARBARIAN_TRAINING, false)) {
            setTitle(player, 3)
            sendDialogueOptions(
                player,
                "Choose an option:",
                "Please, supply me details of your cunning with harpoons.",
                if (getAttribute(player, BarbarianTraining.FISHING_FULL, false)) {
                    "What was that secret knowledge of Herblore we talked of?"
                } else {
                    "Are there any ways to use a fishing rod which I might learn?"
                },
                if (getAttribute(player, BarbarianTraining.FM_FULL, false)) {
                    "I have completed Firemaking with a bow. What follows this?"
                } else {
                    "My mind is ready for your Firemaking wisdom, please instruct me."
                },
            )
            stage = 14
        } else {
            if (inInventory(player, Items.ZAMORAKIAN_SPEAR_11716)) {
                player(FaceAnim.HALF_ASKING, "Can you convert Zamorakian spears into hastae?").also { stage = 200 }
            } else if (inInventory(player, Items.ZAMORAKIAN_HASTA_14638)) {
                player(FaceAnim.HALF_ASKING, "Can you convert Zamorakian hastae into spears?").also { stage = 300 }
            } else {
                npc(
                    FaceAnim.NEUTRAL,
                    "Good day, you seem a hearty warrior.",
                    "Maybe even some barbarian blood in that body of yours?",
                )
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "I really don't think I am related to any barbarian.",
                    "You think so?",
                    "Who are you?",
                    "Sorry, I'm too busy to talk about genealogy today.",
                ).also { stage++ }

            1 ->
                when (buttonId) {
                    1 ->
                        playerl(
                            FaceAnim.HALF_GUILTY,
                            "I really don't think I am related to any barbarian.",
                        ).also { stage++ }

                    2 -> player("You think so?").also { stage = 6 }
                    3 -> player("Who are you?").also { stage = 4 }
                    4 -> player("Sorry, I'm too busy to talk about genealogy today.").also { stage = 3 }
                }

            2 -> npc("Your scepticism will be your loss.").also { stage = END_DIALOGUE }
            3 -> npc("We will talk when you learn to be less impetuous.").also { stage = END_DIALOGUE }
            4 ->
                npc(
                    "Me? I am Otto, known as the Godblessed. I follow",
                    "the ways of our barbarian ancestors,",
                    "communing with the spirits of the dead and teaching",
                    "their ways to worthy disciples.",
                ).also { stage++ }

            5 ->
                npc(
                    "Maybe there's even some barbarian blood in that",
                    "body of yours? You look a likely sort",
                    "to learn of these ways.",
                ).also { stage = END_DIALOGUE }

            6 ->
                npc(
                    "Who can tell? My forefathers weren't averse to",
                    "travelling, so it is possible. They tended to cause too",
                    "much trouble in your so-called civilised lands, however,",
                    "so most returned to their ancestral lands.",
                ).also { stage++ }

            7 ->
                npc(
                    "In any case, I think you are ready to learn our more",
                    "secret tribal feats for yourself.",
                ).also { stage++ }

            8 -> player("Oh, that sounds interesting, what sort of skills would", "these be?").also { stage++ }
            9 ->
                npc(
                    "To begin with I can supply knowledge in the ways of",
                    "firemaking, our special rod fishing tricks and a selection",
                    "of spear skills.",
                ).also { stage++ }

            10 -> player("There are more advanced stages though, to judge by your", "description?").also { stage++ }
            11 ->
                npc(
                    "Your perception is creditable. I can eventually teach of",
                    "more advanced firemaking techniques, and the rod",
                    "fishing skills are but a preliminary to our special potions",
                    "and brews.",
                ).also { stage++ }

            12 ->
                npc(
                    "These secrets must, however, wait until you have",
                    "learned of the more basic skills.",
                ).also { stage++ }

            13 -> {
                setTitle(player, 3)
                sendDialogueOptions(
                    player,
                    "Choose an option:",
                    "Please, supply me details of your cunning with harpoons.",
                    if (getAttribute(player, BarbarianTraining.FISHING_FULL, false)) {
                        "What was that secret knowledge of Herblore we talked of?"
                    } else {
                        "Are there any ways to use a fishing rod which I might learn?"
                    },
                    if (getAttribute(player, BarbarianTraining.FM_FULL, false)) {
                        "I have completed Firemaking with a bow. What follows this?"
                    } else {
                        "My mind is ready for your Firemaking wisdom, please instruct me."
                    },
                )
                setAttribute(player, BarbarianTraining.BARBARIAN_TRAINING, true)
                stage = 14
            }

            14 ->
                when (buttonId) {
                    1 -> player("Please, supply me details of your cunning", "with harpoons.").also { stage = 116 }
                    2 -> {
                        if (getAttribute(player, BarbarianTraining.FISHING_BASE, false)) {
                            npc(
                                "Alas, I do not sense that you have been",
                                "successful in your Fishing yet. The look in your eyes",
                                "is not that of the osprey.",
                            )
                            stage = 106
                            return true
                        }

                        if (getAttribute(player, BarbarianTraining.FISHING_FULL, false)) {
                            player("What was that secret knowledge of Herblore we talked", " of?")
                            setAttribute(player, BarbarianTraining.HERBLORE_START, true)
                            stage = 110
                        } else {
                            player("Are there any ways to use a fishing rod", "which I might learn?")
                            setAttribute(player, BarbarianTraining.FISHING_START, true)
                            stage = 100
                        }
                    }

                    3 -> {
                        if (getAttribute(player, BarbarianTraining.FM_BASE, false)) {
                            npcl(
                                FaceAnim.NEUTRAL,
                                "The next stage is quite complex, so listen well. In order to send our ancestors into the spirit world, their mortal remains must be burned with due ceremony.",
                            )
                            stage = 22
                            return true
                        }

                        if (getAttribute(player, BarbarianTraining.FM_FULL, false)) {
                            npc("Fine news indeed, secrets of our spirit", "boats now await your attention.")
                            stage = END_DIALOGUE
                            return true
                        }

                        player(
                            "My mind is ready for your Firemaking wisdom, please",
                            "instruct me.",
                        ).also { stage = 15 }
                    }
                }

            15 -> {
                if (getStatLevel(player, Skills.FIREMAKING) < 35) {
                    npcl(
                        FaceAnim.NEUTRAL,
                        "You must have a Firemaking level of at least 35 in order to burn the oak log that is required for the firemaking portion of Barbarian training.",
                    ).also { stage = END_DIALOGUE }
                } else {
                    npc("The first point in your progression is that of lighting", "fires without a tinderbox.")
                    setAttribute(player, BarbarianTraining.FM_START, true)
                    stage++
                }
            }

            16 -> player("That sounds pretty useful, tell me more.").also { stage++ }
            17 ->
                npc(
                    "For this process you will require a strung bow. You",
                    "use the bow to quickly rotate pieces of wood against one",
                    "another. As you rub the wood becomes hot, eventually",
                    "springing into flame.",
                ).also { stage++ }

            18 -> player("No more secret details?").also { stage++ }
            19 ->
                npc(
                    "The spirits will aid you, the power they supply will guide",
                    "your hands. Go and benefit from their guidance upon",
                    "an oaken log.",
                ).also {
                    setAttribute(player, BarbarianTraining.FM_BASE, true)
                    stage++
                }

            20 -> options("I seek more answers.", "I have no more questions at this time.").also { stage++ }
            21 ->
                when (buttonId) {
                    1 -> player("I seek more answers.").also { stage = 13 }
                    2 -> player("I have no more questions at this time.").also { stage++ }
                }

            22 -> {
                if (!inInventory(player, Items.BARBARIAN_SKILLS_11340)) {
                    npc(
                        "I see you have free space and no record of the tasks I",
                        "have set you. Please take this book as a record of your",
                        "progress - between the spirits and your diligence, I",
                        "expect it will always be up to date.",
                    ).also { stage = END_DIALOGUE }
                    addItem(player, Items.BARBARIAN_SKILLS_11340)
                } else {
                    npc("In that case, farewell.").also { stage = END_DIALOGUE }
                }
            }

            25 ->
                npc("Fine news indeed, secrets of our spirit", "boats now await your attention.").also {
                    stage = END_DIALOGUE
                }

            100 ->
                npc(
                    "While you civilised folk use small, weak fishing rods, we",
                    "barbarians are skilled with heavier tackle. We fish in the",
                    "lake nearby.",
                ).also { stage++ }

            101 -> player("So can you teach me of this Fishing method?").also { stage++ }
            102 ->
                npc(
                    "Certainly. Take the rod from under the bed in my",
                    "dwelling and fish in the lake. When you have caught a",
                    "few fish I am sure you will be ready to talk more with",
                    "me.",
                ).also { stage++ }

            103 -> npc("You will know when you are ready, since inspiration will", "fill your mind.").also { stage++ }
            104 -> player("So I can obtain new foods from these Fishing spots?").also { stage++ }
            105 ->
                npc(
                    "We do not use these fish quite as you might expect.",
                    "When you return from Fishing we can speak more of",
                    "this matter.",
                ).also {
                    setAttribute(player, BarbarianTraining.FISHING_BASE, true)
                    stage = 20
                }

            106 -> player("Osprey?").also { stage++ }
            107 ->
                npc(
                    "Legendary birds, which the ignorant call eagles.",
                    "They prey upon fish. Do as they do to gain",
                    "inspiration.",
                ).also { stage = END_DIALOGUE }

            110 ->
                if (!inInventory(player, Items.ATTACK_MIX2_11429)) {
                    npc("Do you have my potion?")
                    stage++
                } else if (getAttribute(player, BarbarianTraining.HERBLORE_FULL, false)) {
                    player(
                        "I feel I am missing some vital information about your",
                        "need for this potion, though I often have this suspicion.",
                    )
                    stage = 114
                } else {
                    removeItem(player, Item(Items.ATTACK_MIX2_11429, 1))
                    npc(
                        FaceAnim.HAPPY,
                        "I see you have my potion. I will say no more than that",
                        "I am eternally grateful.",
                    )
                    stage = 113
                }

            111 -> player("What was it you needed again?").also { stage++ }
            112 ->
                npc(
                    "Bring me a lesser attack potion combined with fish roe.",
                    "There is more importance in this than you will ever",
                    "know.",
                ).also {
                    setAttribute(player, BarbarianTraining.HERBLORE_BASE, true)
                    stage = 20
                }

            113 ->
                player(
                    "I feel I am missing some vital information about your",
                    "need for this potion, though I often have this suspicion.",
                ).also { stage++ }

            114 ->
                npc(
                    "I will not reveal all of my private matters to you. Some",
                    "secrets are best kept rather than revealed.",
                ).also { stage = 20 }

            115 -> player("Please, supple me details of your cunning with", "harpoons.").also { stage++ }
            116 ->
                npc(
                    "First you must know more of harpoons through special",
                    "study of fish that are usually caught with such a device.",
                ).also { stage++ }

            117 -> player("What do I need to know?").also { stage++ }
            118 ->
                npc(
                    "You must catch fish which are usually harpooned,",
                    "without a harpoon. You will be using your skill and",
                    "strength.",
                ).also { stage++ }

            119 -> player("How do you expect me to do that?").also { stage++ }
            120 ->
                npc(
                    "Use your arm as bait. Wriggle your fingers as if they",
                    "are a tasty snack and hungry tuna and swordfish will",
                    "throng to be caught by you.",
                ).also { stage++ }

            121 ->
                player(
                    "That sounds rather insanely dangerous. I'm glad you",
                    "didn't mention sharks too.",
                ).also { stage++ }

            122 ->
                npc(
                    "Oh, my mind slipped for a moment, this method does",
                    "indeed work with sharks - though in this case the action",
                    "must be more of a frenzied thrashing of the arm than a",
                    "wriggle.",
                ).also { stage++ }

            123 -> player("..and I thought Fishing was a safe way to pass the time.").also { stage = 20 }
            200 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Yes, I can convert a Zamorakian spear into a hasta. The spirits require me to request 300,000 coins from you for this service.",
                ).also { stage++ }

            201 ->
                if (!inInventory(player, Items.COINS_995, 300000)) {
                    player("I can't afford that. Maybe another time.").also { stage = END_DIALOGUE }
                } else {
                    setTitle(player, 2)
                    sendDialogueOptions(player, "Do you wish to convert your spear?", "Yes", "No")
                    stage++
                }

            202 ->
                when (buttonId) {
                    1 -> {
                        end()
                        if (removeItem(player, Item(Items.COINS_995, 300000))) {
                            removeItem(player, Items.ZAMORAKIAN_SPEAR_11716)
                            sendItemDialogue(player, Items.ZAMORAKIAN_HASTA_14638, "Otto has converted your spear into a hasta.")
                            addItem(player, Items.ZAMORAKIAN_HASTA_14638)
                        } else {
                            end()
                        }
                    }

                    2 -> player("Actually, forget it.").also { stage++ }
                }

            203 -> npc("As you wish.").also { stage = END_DIALOGUE }

            300 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Yes, I can convert a Zamorakian hasta into a spear. I do not charge for this service.",
                ).also { stage++ }

            301 -> {
                setTitle(player, 2)
                sendDialogueOptions(player, "Do you wish to convert your hasta?", "Yes", "No")
                stage++
            }

            302 ->
                when (buttonId) {
                    1 -> {
                        end()
                        if (removeItem(player, Items.ZAMORAKIAN_HASTA_14638)) {
                            sendItemDialogue(
                                player,
                                Items.ZAMORAKIAN_SPEAR_11716,
                                "Otto has converted your hasta into a spear.",
                            )
                            addItem(player, Items.ZAMORAKIAN_SPEAR_11716)
                        } else {
                            end()
                        }
                    }

                    2 -> player("Actually, forget it.").also { stage = 203 }
                }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.OTTO_GODBLESSED_2725)
}
