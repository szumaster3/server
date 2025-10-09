package content.region.kandarin.baxtorian.dialogue

import content.data.GameAttributes
import content.region.desert.quest.rescue.PrinceAliRescue.hasItem
import content.region.kandarin.baxtorian.barbtraining.BarbarianTraining
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

@Initializable
class OttoGodblessedDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val hasStarted = getAttribute(player, BarbarianTraining.BARBARIAN_TRAINING, false)
        val hasCompleted = player.savedData.activityData.hasCompleteBarbarianTraining()

        if (hasCompleted) {
            npcl(FaceAnim.FRIENDLY, "Welcome back oh friend of the spirits, I am humbled in your presence.")
            stage = 500
            return true
        }

        if (hasStarted) {
            setTitle(player, 3)
            sendOptions(
                player,
                "Choose an option:",
                "Please, supply me details of your cunning with harpoons.",
                if (player.savedData.activityData.isBarbarianFishingRod) "What was that secret knowledge of Herblore we talked of?" else "Are there any ways to use a fishing rod which I might learn?",
                if (player.savedData.activityData.isBarbarianFiremakingBow) "I have completed Firemaking with a bow. What follows this?" else "My mind is ready for your Firemaking wisdom, please instruct me.",
                if (player.savedData.activityData.isBarbarianSmithingSpear) "Tell me more about the one-handed spears." else "Tell me more about the use of spears."
            )
            stage = 14
        }

        npcl(FaceAnim.FRIENDLY, "Good day, you seem a hearty warrior. Maybe even some barbarian blood in that body of yours?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage) {
            0 -> options("I really don't think I am related to any barbarian.", "You think so?", "Who are you?", "Sorry, I'm too busy to talk about genealogy today.").also { stage++ }
            1 -> when (buttonId) {
                1 -> playerl(FaceAnim.HALF_GUILTY, "I really don't think I am related to any barbarian.").also { stage++ }
                2 -> playerl(FaceAnim.HALF_ASKING, "You think so?").also { stage = 6 }
                3 -> playerl(FaceAnim.HALF_ASKING, "Who are you?").also { stage = 4 }
                4 -> playerl(FaceAnim.HALF_ASKING, "Sorry, I'm too busy to talk about genealogy today.").also { stage = 3 }
            }
            2 -> npc("Your scepticism will be your loss.").also { stage = END_DIALOGUE }
            3 -> npc("We will talk when you learn to be less impetuous.").also { stage = END_DIALOGUE }
            4 -> npc("Me? I am Otto, known as the Godblessed. I follow", "the ways of our barbarian ancestors,", "communing with the spirits of the dead and teaching", "their ways to worthy disciples.").also { stage++ }
            5 -> npc("Maybe there's even some barbarian blood in that", "body of yours? You look a likely sort", "to learn of these ways.").also { stage = END_DIALOGUE }
            6 -> npc("Who can tell? My forefathers weren't averse to", "travelling, so it is possible. They tended to cause too", "much trouble in your so-called civilised lands, however,", "so most returned to their ancestral lands.").also { stage++ }
            7 -> npc("In any case, I think you are ready to learn our more", "secret tribal feats for yourself.").also { stage++ }
            8 -> player("Oh, that sounds interesting, what sort of skills would", "these be?").also { stage++ }
            9 -> npc("To begin with I can supply knowledge in the ways of", "firemaking, our special rod fishing tricks and a selection", "of spear skills.").also { stage++ }
            10 -> player("There are more advanced stages though, to judge by your", "description?").also { stage++ }
            11 -> npc("Your perception is creditable. I can eventually teach of", "more advanced firemaking techniques, and the rod", "fishing skills are but a preliminary to our special potions", "and brews.").also { stage++ }
            12 -> npc("These secrets must, however, wait until you have", "learned of the more basic skills.").also { stage++ }
            13 -> {
                setTitle(player, 3)
                sendOptions(
                    player,
                    "Choose an option:",
                    "Please, supply me details of your cunning with harpoons.",
                    if (player.savedData.activityData.isBarbarianFishingRod) "What was that secret knowledge of Herblore we talked of?" else "Are there any ways to use a fishing rod which I might learn?",
                    if (player.savedData.activityData.isBarbarianFiremakingBow) "I have completed Firemaking with a bow. What follows this?" else if(player.getAttribute(BarbarianTraining.PYRESHIP_START, false) && !player.savedData.activityData.isBarbarianFiremakingPyre) "Could you tell me more of these spirits you continually refer to?" else "My mind is ready for your Firemaking wisdom, please instruct me.",
                    if (player.savedData.activityData.isBarbarianSmithingSpear) "I've created a spear!" else "Tell me more about the use of spears."
                )
                setAttribute(player, BarbarianTraining.BARBARIAN_TRAINING, true)
                stage = 14
            }
            14 -> when (buttonId) {
                1 -> player("Please, supply me details of your cunning with harpoons.").also { stage = 116 }
                2 -> if (player.savedData.activityData.isBarbarianFishingRod || player.savedData.activityData.isBarbarianFishingBarehand || getAttribute(player, BarbarianTraining.FISHING_FULL, false)) {
                    player("What was that secret knowledge of Herblore we talked of?")
                    stage = 110
                } else {
                    player("Are there any ways to use a fishing rod which I might learn?")
                    stage = 100
                }
                3 -> if (!player.savedData.activityData.isBarbarianFiremakingPyre) {
                    npcl(FaceAnim.NEUTRAL, "The next stage is quite complex, so listen well...")
                    stage = 200
                } else if(player.savedData.activityData.isBarbarianFiremakingBow) {
                    player("My mind is ready for your Firemaking wisdom, please instruct me.")
                    stage = 25
                } else if(player.getAttribute(BarbarianTraining.PYRESHIP_START, false) && !player.savedData.activityData.isBarbarianFiremakingPyre){
                    player("Could you tell me more of these spirits you continually refer to?")
                    stage = 212
                } else {
                    player("My mind is ready for your Firemaking wisdom, please instruct me.")
                    stage = 15
                }
                4 -> if (player.savedData.activityData.isBarbarianSmithingSpear) {
                    npc(FaceAnim.NEUTRAL, "The manufacture of spears is now yours as a speciality.", "Use your skill well.")
                    stage = 300
                } else if(player.savedData.activityData.isBarbarianSmithingSpear && player.getAttribute(BarbarianTraining.FISHING_FULL, false)) {
                    player(FaceAnim.HALF_ASKING,"What of the one handed spears of which you spoke?")
                    stage = 308
                } else {
                    player("Tell me more about the use of spears.")
                    stage = 300
                }
            }

            15 -> if (getStatLevel(player, Skills.FIREMAKING) < 35) {
                npcl(FaceAnim.NEUTRAL, "You must have a Firemaking level of at least 35 in order to burn the oak log that is required for the firemaking portion of Barbarian training.").also { stage = END_DIALOGUE }
            } else {
                npc("The first point in your progression is that of lighting", "fires without a tinderbox.")
                setAttribute(player, BarbarianTraining.FM_START, true)
                stage++
            }
            16 -> player("That sounds pretty useful, tell me more.").also { stage++ }
            17 -> npc("For this process you will require a strung bow. You", "use the bow to quickly rotate pieces of wood against one", "another. As you rub the wood becomes hot, eventually", "springing into flame.").also { stage++ }
            18 -> player("No more secret details?").also { stage++ }
            19 -> npc("The spirits will aid you, the power they supply will guide", "your hands. Go and benefit from their guidance upon", "an oaken log.").also { stage++ }
            20 -> options("I seek more answers.", "I have no more questions at this time.").also { stage++ }
            21 -> when (buttonId) {
                1 -> player("I seek more answers.").also { stage = 13 }
                2 -> player("I have no more questions at this time.").also { stage++ }
            }
            22 -> {
                if (!hasItem(player, Item(Items.BARBARIAN_SKILLS_11340)) && freeSlots(player) > 0) {
                    npc("I see you have free space and no record of the tasks I", "have set you. Please take this book as a record of your", "progress - between the spirits and your diligence, I", "expect it will always be up to date.")
                    addItem(player, Items.BARBARIAN_SKILLS_11340)
                    stage = 23
                } else {
                    npc("In that case, farewell.").also { stage = 1000 }
                }
            }

            25 -> npc("Fine news indeed, secrets of our spirit", "boats now await your attention.").also { stage = END_DIALOGUE }

            // Barbarian rod fishing
            100 -> npc("While you civilised folk use small, weak fishing rods, we", "barbarians are skilled with heavier tackle. We fish in the", "lake nearby.").also { stage++ }
            101 -> player("So can you teach me of this Fishing method?").also { stage++ }
            102 -> npc("Certainly. Take the rod from under the bed in my", "dwelling and fish in the lake. When you have caught a", "few fish I am sure you will be ready to talk more with", "me.").also { stage++ }
            103 -> npc("You will know when you are ready, since inspiration will", "fill your mind.").also { stage++ }
            104 -> player("So I can obtain new foods from these Fishing spots?").also { stage++ }
            105 -> npc("We do not use these fish quite as you might expect.", "When you return from Fishing we can speak more of", "this matter.").also {
                setAttribute(player, BarbarianTraining.FISHING_BASE, true)
                stage = 20
            }
            106 -> player("Osprey?").also { stage++ }
            107 -> npc("Legendary birds, which the ignorant call eagles.", "They prey upon fish. Do as they do to gain", "inspiration.").also { stage = END_DIALOGUE }

            // Herblore
            110 -> if (!inInventory(player, Items.ATTACK_MIX2_11429)) {
                npc("Do you have my potion?").also { stage++ }
            } else if (player.savedData.activityData.isBarbarianHerbloreAttackMix && !getAttribute(player, BarbarianTraining.HERBLORE_FULL, false)) {
                player("I feel I am missing some vital information about your", "need for this potion, though I often have this suspicion.")
                stage = 114
            } else {
                removeItem(player, Item(Items.ATTACK_MIX2_11429, 1))
                npc(FaceAnim.HAPPY, "I see you have my potion. I will say no more than that", "I am eternally grateful.")
                stage = 113
            }

            111 -> player("What was it you needed again?").also { stage++ }
            112 -> npc("Bring me a lesser attack potion combined with fish roe.", "There is more importance in this than you will ever", "know.").also {
                player.savedData.activityData.setBarbarianHerbloreAttackMix(true)
                stage = 20
            }

            113 -> player("I feel I am missing some vital information about your", "need for this potion, though I often have this suspicion.").also { stage++ }
            114 -> npc("I will not reveal all of my private matters to you. Some", "secrets are best kept rather than revealed.").also { stage = 20 }

            // Barehand
            115 -> player("Please, supple me details of your cunning with", "harpoons.").also { stage++ }
            116 -> npc("First you must know more of harpoons through special", "study of fish that are usually caught with such a device.").also { stage++ }
            117 -> player("What do I need to know?").also { stage++ }
            118 -> npc("You must catch fish which are usually harpooned,", "without a harpoon. You will be using your skill and", "strength.").also { stage++ }
            119 -> player("How do you expect me to do that?").also { stage++ }
            120 -> npc("Use your arm as bait. Wriggle your fingers as if they", "are a tasty snack and hungry tuna and swordfish will", "throng to be caught by you.").also { stage++ }
            121 -> player("That sounds rather insanely dangerous. I'm glad you", "didn't mention sharks too.").also { stage++ }
            122 -> npc("Oh, my mind slipped for a moment, this method does", "indeed work with sharks - though in this case the action", "must be more of a frenzied thrashing of the arm than a", "wriggle.").also { stage++ }
            123 -> {
                playerl(FaceAnim.NEUTRAL, "...and I thought Fishing was a safe way to pass the time.")
                player.savedData.activityData.setBarbarianFishingBarehand(true)
                stage = 20
            }

            // Pyre ships 1
            200 -> npcl(FaceAnim.FRIENDLY, "In order to send our ancestors into the spirit world, their mortal remains must be burned with due ceremony.").also { stage++ }
            201 -> npcl(FaceAnim.FRIENDLY, "This can only be performed close to the water on the shore of the lake, just to our north-east. You will recognise the correct places by the ashes to be seen there.").also { stage++ }
            202 -> npcl(FaceAnim.FRIENDLY, "You will need to construct a small ship by using a hatchet upon logs in this area, then add the bones of a long dead barbarian hero.").also { stage++ }
            203 -> playerl(FaceAnim.HALF_ASKING, "Where do I obtain these bones?").also { stage++ }
            204 -> npcl(FaceAnim.FRIENDLY, "From the caverns beneath this lake. Many of our ancestors travelled to these caverns in order to hunt for glory and found only death. Their bones must still lie inside, their spirits trapped in torment.").also { stage++ }
            205 -> playerl(FaceAnim.HALF_ASKING, "So I make sure I am in the right place, then throw these bones on a ceremonial ship before I set light to it?").also { stage++ }
            206 -> npcl(FaceAnim.FRIENDLY, "Correct. The spirit will ascend to glory; the pyre will send the earthly remains to the depths. You will also obtain a closer link to the spirit world. During this heightened contact, any bones you bury will have increased importance to the gods.").also { stage++ }
            207 -> npcl(FaceAnim.FRIENDLY, "The number of bones that may be buried, before the link fades, is increased with the difficulty of obtaining the wood which you use.").also { stage++ }
            208 -> playerl(FaceAnim.HALF_ASKING, "Do you foresee any problems?").also { stage++ }
            209 -> npcl(FaceAnim.FRIENDLY, "I have little knowledge of the caverns - they are blocked from the sight of the spirits with whom I commune. I can only suspect that whatever slew barbarian heroes is indeed mighty. I would also suggest that these bones might well be very uncommon, since heroes are not found in vast numbers. Good luck.").also { stage++ }
            210 -> playerl(FaceAnim.HALF_ASKING, "How do I enter these caverns then?").also { stage++ }
            211 -> npcl(FaceAnim.FRIENDLY, "Dive into the whirlpool in the lake to the east. The spirits will use their abilities to ensure you arrive in the correct location, though their influence fades, so you must find your own way out.").also {
                player.setAttribute(BarbarianTraining.PYRESHIP_START, true)
                stage = 1000
            }

            // Pyre ships 2
            212 -> npcl(FaceAnim.FRIENDLY, "Certainly, though it is quite simple - they are the barbarians of the past, who have died in the passing of the years. There are three categories which learned sages claim may describe all spirits encountered.").also { stage++ }
            213 -> npcl(FaceAnim.FRIENDLY, "The majority are at peace, having died with their ambitions sated, even if this ambition was as simple as a glorious death.").also { stage++ }
            214 -> npcl(FaceAnim.FRIENDLY, "They can contact those in our world in order to spur us on to glory. Of course, they cannot or will not contact all inhabitants of these lands. All are not worthy.").also { stage++ }
            215 -> playerl(FaceAnim.HALF_ASKING, "So those are the spirits who give us guidance. What of the other sorts?").also { stage++ }
            216 -> npcl(FaceAnim.FRIENDLY, "A second category of spirit is made up of those who are not yet at peace. However, all that is needed is for their mortal remains to be laid to rest and they will join the peaceful majority.").also { stage++ }
            217 -> playerl(FaceAnim.HALF_ASKING, "I can see the pyres serve this purpose. The final category is also restless?").also { stage++ }
            218 -> npcl(FaceAnim.FRIENDLY, "Alas, there is a small minority who will never be at rest. These have been slain in moments of insanity by their brothers or by adventurers. They are not at rest and will attack those they blame for their tortured state.").also { stage++ }
            219 -> npcl(FaceAnim.FRIENDLY, "They are not known, however, for being able to determine this blame with any great accuracy, so may be considered as dangerous to all.").also { stage++ }
            220 -> playerl(FaceAnim.HAPPY, "I thank you Otto, it makes a bit more sense now.").also { stage = 20 }

            // Smithing
            300 -> if(!hasRequirement(player, Quests.TAI_BWO_WANNAI_TRIO)) {
                npcl(FaceAnim.FRIENDLY, "The next step is to manufacture a spear, suitable for combat.").also { stage++ }
            } else {
                npc(FaceAnim.FRIENDLY, "Many warriors complain that spears are difficult to find", "We barbarians thus forge our own.").also { stage = 306 }
            }
            301 -> npcl(FaceAnim.FRIENDLY, "Our distance cousins on Karamja are in need of help, however, and you must aid them before I can aid you.").also { stage++ }
            302 -> npcl(FaceAnim.NEUTRAL, "You must go now and complete the Tai Bwo Wannai Trio quest.").also { stage++ }
            303 -> playerl(FaceAnim.HALF_THINKING, "Couldn't you just take a bribe or something? I am sure you could do with some cash.").also { stage++ }
            304 -> npcl(FaceAnim.HALF_GUILTY, "I am afraid this is a vital step; the spirits foresee that your understanding of spears will increase through this quest.").also { stage++ }
            305 -> npcl(FaceAnim.NEUTRAL, "You may not progress in the use of spears until you have completed this mission.").also { stage = 20 }
            306 -> player(FaceAnim.HALF_ASKING, "Can i just use some metal on an anvil, or is there", "something else involved?").also { stage++ }
            307 -> npc(FaceAnim.NEUTRAL, "If you use our special barbarian anvil here, you will", "find it ideal. Other anvils are not sturdy enough or", "shaped appropriately for the forging work involved", "Make any of our spears and return.").also {
                player.savedData.activityData.setBarbarianSmithingSpear(true)
                stage = 20
            }

            308 -> npc(FaceAnim.NEUTRAL, "The next step is to manufacture a one-handed version", "of a spear, suitable for combat. Such a spear is known", "to us as a hasta.").also { stage++ }
            309 -> player(FaceAnim.HALF_ASKING, "Can't I just pick one up and use it in one hand?").also { stage++ }
            310 -> npc(FaceAnim.NEUTRAL, "You may try, but the process involves a differently", "balanced spear. A normal one is unlikely to benefit you.").also { stage++ }
            311 -> npc(FaceAnim.NEUTRAL, "Before you may use such a weapon in anger, you","must make an example. Only then will you fully", "understand the poise and techniques involved.").also { stage++ }
            312 -> player(FaceAnim.HALF_ASKING,"So I must create a hasta? I see. This makes sense.").also { stage++ }
            313 -> npc(FaceAnim.NEUTRAL, "Indeed. You may use our special anvil for this spear", "type too. The ways of black and dragon hastae are", "beyond our knowledge, however.").also {
                player.savedData.activityData.setBarbarianSmithingHasta(true)
                stage = 20
            }

            // After complete
            500 -> playerl(FaceAnim.HALF_ASKING, "That's very kind, but I was wondering whether you have any more information for me?").also { stage++ }
            501 -> npcl(FaceAnim.FRIENDLY, "You are no longer the apprentice, but the master. Seek to inspire us with your daring deeds and legendary feats. Aspire to join the spirits when your time comes.").also { stage++ }
            502 -> {
                setTitle(player, 3)
                sendOptions(player, "Choose an option:", "Could you tell me more of these spirits you continually refer to?", "I wish to change how I fish.", "I have no more questions at this time.")
                stage++
            }
            503 -> when (buttonId) {
                1 -> playerl(FaceAnim.HALF_ASKING, "Could you tell me more of these spirits you continually refer to?").also { stage++ }
                2 -> playerl(FaceAnim.HALF_ASKING, "I wish to change how I fish.").also { stage = 520 }
                3 -> playerl(FaceAnim.HALF_ASKING, "I have no more questions at this time.").also { stage = 530 }
            }
            504 -> npcl(FaceAnim.FRIENDLY, "Certainly, though it is quite simple - they are the barbarians of the past, who have died in the passing of the years.").also { stage++ }
            505 -> npcl(FaceAnim.FRIENDLY, "There are three categories which learned sages claim may describe all spirits encountered.").also { stage++ }
            506 -> npcl(FaceAnim.FRIENDLY, "The majority are at peace, having died with their ambitions sated, even if this ambition was as simple as a glorious death.").also { stage++ }
            507 -> playerl(FaceAnim.NEUTRAL, "So those are the spirits who give us guidance. What of the other sorts?").also { stage++ }
            508 -> npcl(FaceAnim.FRIENDLY, "A second category of spirit is made up of those who are not yet at peace. However, all that is needed is for their mortal remains to be laid to rest and they will join the peaceful majority.").also { stage++ }
            509 -> playerl(FaceAnim.NEUTRAL, "I can see the pyres serve this purpose. The final category is also restless?").also { stage++ }
            510 -> npcl(FaceAnim.FRIENDLY, "Alas, there is a small minority who will never be at rest. These have been slain in moments of insanity by their brothers or by adventurers.").also { stage++ }
            511 -> npcl(FaceAnim.FRIENDLY, "They are not at rest and will attack those they blame for their tortured state. They are not known, however, for being able to determine this blame with any great accuracy, so may be considered as dangerous to all.").also { stage++ }
            512 -> playerl(FaceAnim.NEUTRAL, "I thank you Otto, it makes a bit more sense now.").also { stage = 502 }

            // Change fishing technique
            520 -> {
                npc("So be it.")
                toggleBarbarianFishing(player)
                stage = 1000
            }

            // End dialogue
            530 -> {
                npc("In that case, farewell.")
                stage = 1000
            }
            600 -> npcl(FaceAnim.FRIENDLY, "Please take this book as a record of your researches - between the spirits and your diligence I expect it will always be up to date.").also { stage = 13 }

            1000 -> end()
        }
        return true
    }

    /**
     * Toggles between barehand fishing and regular fishing.
     *
     * @param player the player.
     */
    private fun toggleBarbarianFishing(player: Player) {
        val barbFishing = player.getAttribute(GameAttributes.BARBARIAN_BAREHAND_FISHING, false)

        if (barbFishing) {
            // Revert to default fishing
            player.setAttribute(GameAttributes.BARBARIAN_BAREHAND_FISHING, false)
            sendMessage(player, "You revert to default fishing techniques.")
        } else {
            // Switch to barbarian fishing
            player.setAttribute(GameAttributes.BARBARIAN_BAREHAND_FISHING, true)
            sendMessage(player, "You revert to barbarian fishing techniques.")
        }
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.OTTO_GODBLESSED_2725)
}
