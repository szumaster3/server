package content.region.misthalin.varrock.museum.dialogue

import content.data.GameAttributes
import core.api.getAttribute
import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

@Initializable
class NaturalHistorianDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val firstTimeVisit = getAttribute(player, GameAttributes.NATURAL_HISTORIAN_FIRST_TALK, true)
        if(!firstTimeVisit) {
            npcl(FaceAnim.HAPPY, "Hello there and welcome to the Natural History exhibit of the Varrock Museum!")
        } else {
            npcl(FaceAnim.ASKING, "Hello again, " + if (player.isMale) "sir" else "madam" + ", how can I help you on this fine day?").also { stage = 3 }
            setAttribute(player, GameAttributes.NATURAL_HISTORIAN_FIRST_TALK, true)
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.HALF_ASKING, "Hello. So what is it you do here?").also { stage++ }
            1 -> npcl(FaceAnim.HALF_GUILTY, "Well, we look after all of the research in this section.").also { stage++ }
            2 -> npcl(FaceAnim.FRIENDLY, "When I'm not doing that, I'm teaching people like yourself about how wonderful the natural world is.").also { stage = 4 }
            3 -> player(FaceAnim.ASKING, "I was hoping you could tell me about something.").also { stage++ }
            4 -> npcl(FaceAnim.FRIENDLY, "Ask me a question and I'll give you an insight into the exciting world of animals.").also { stage++ }
            5 -> when(npc.id) {
                NPCs.NATURAL_HISTORIAN_5967 -> showTopics(
                    Topic("Tell me about natural history.", 6, true),
                    Topic("Tell me about the terrorbirds.", 10, true),
                    Topic("Tell me about the Kalphite Queen.", 24, true),
                    Topic("That's enough education for one day.", 1000)
                )
                NPCs.NATURAL_HISTORIAN_5966 -> showTopics(
                    Topic("Tell me about lizards.", 39, true),
                    Topic("Tell me about tortoises", 50, true),
                    Topic("Tell me about dragons.", 61, true),
                    Topic("Tell me about wyverns.", 76, true),
                    Topic("That's enough education for one day.", 1000)
                )
                NPCs.NATURAL_HISTORIAN_5968 -> showTopics(
                    Topic("Tell me about snails.", 89, true),
                    Topic("Tell me about monkeys.", 102, true),
                    Topic("Tell me about sea slugs.", 113, true),
                    Topic("Tell me about snakes.", 126, true),
                    Topic("That's enough education for one day.", 1000)
                )
                NPCs.NATURAL_HISTORIAN_5969 -> showTopics(
                    Topic("Tell me about camels",138, true),
                    Topic("Tell me about leeches.",150, true),
                    Topic("Tell me about moles.",165, true),
                    Topic("Tell me about penguins.",175, true),
                    Topic("That's enough education for one day.", 1000)
                )
            }
            6  -> npcl(FaceAnim.HALF_GUILTY, "Well, the field of natural history covers a wide range of sciences.").also { stage++ }
            7  -> npcl(FaceAnim.HALF_GUILTY, "So we use biology, the study of living things, botany, the study of plants and zoology, the study of animals.").also { stage++ }
            8  -> npcl(FaceAnim.HALF_GUILTY, "Though the field is growing all the time and we're also using techniques from magic, astrology and numerology.").also { stage++ }
            9  -> npcl(FaceAnim.HALF_GUILTY, "A person interested in natural history is known as a naturalist.").also { stage = 5 }

            10 -> npcl(FaceAnim.HALF_GUILTY, "Ahh terrorbirds, the fastest bird on two legs.").also { stage++ }
            11 -> npcl(FaceAnim.HALF_GUILTY, "If you just follow me to the display case I shall explain all about them.").also { stage++ }
            12 -> npcl(FaceAnim.HALF_GUILTY, "Terrorbirds live in nomadic groups of between five and fifty birds that often travel together with other grazing animals.").also { stage++ }
            13 -> npcl(FaceAnim.HALF_GUILTY, "They mainly feed on seeds and other plants. They also eat insects such as locusts if they can catch them.").also { stage++ }
            14 -> npcl(FaceAnim.HALF_GUILTY, "They have no teeth to chew with, so they swallow pebbles that help to grind the swallowed foods in the gizzard.").also { stage++ }
            15 -> npcl(FaceAnim.HALF_GUILTY, "They can go without water for a long time, exclusively living off the water in the plants. However, they enjoy water and frequently take baths when they can.").also { stage++ }
            16 -> npcl(FaceAnim.HALF_GUILTY, "Terrorbirds are known to eat almost anything, particularly in captivity, where opportunity is increased.").also { stage++ }
            17 -> npcl(FaceAnim.HALF_GUILTY, "Terrorbirds usually weigh a little less than a small unicorn. The feathers of adult males are mostly green, with some white on the wings and tail.").also { stage++ }
            18 -> npcl(FaceAnim.HALF_GUILTY, "There are claws on two of the wings' fingers and their strong legs have no feathers. The bird stands on two toes, with the bigger one resembling a hoof. Its feet have no claws.").also { stage++ }
            19 -> npcl(FaceAnim.HALF_GUILTY, "This is an adaptation unique to terrorbirds that appears to aid in running. Their legs are powerful enough to kill even large animals.").also { stage++ }
            20 -> npcl(FaceAnim.HALF_GUILTY, "The gnomes in particular, prize the terrorbird for its fast running speed, using them as mounts whenever possible.").also { stage++ }
            21 -> npcl(FaceAnim.HALF_GUILTY, "There are a number of recorded incidents of people being attacked and killed. Big males can be very territorial and aggressive, and can attack and kick very powerfully with their legs.").also { stage++ }
            22 -> npcl(FaceAnim.HALF_GUILTY, "A terrorbird is so fast, it can easily outrun any human athlete.").also { stage++ }
            23 -> npcl(FaceAnim.HALF_GUILTY, "And this concludes my short lecture on terrorbirds. I hope you've enjoyed yourselves.").also { stage = 5 }

            24 -> npcl(FaceAnim.HALF_GUILTY, "Ahh kalphites, the insectoid eating machines.").also { stage++ }
            25 -> npcl(FaceAnim.HALF_GUILTY, "If you just follow me to the display case I shall explain all about them.").also { stage++ }
            26 -> npcl(FaceAnim.HALF_GUILTY, "The kalphites, otherwise known as the kalphiscarabeinae, are perhaps the largest species of insect on the face of ${GameWorld.settings?.name}. Their queen is called kalphiscarabeinae pasha.").also { stage++ }
            27 -> npcl(FaceAnim.HALF_GUILTY, "Most of the early documentation and research on this fearsome predatory species was performed by the noted bug hunter Iqbar Ali-Abdula.").also { stage++ }
            28 -> npcl(FaceAnim.HALF_GUILTY, "This, of course, was before he was driven insane by his research and ran off into the desert, screaming.").also { stage++ }
            29 -> npcl(FaceAnim.HALF_GUILTY, "Kalphites are related to beetles and scorpions; they are mainly green in colour. Some have remarkable antennae which can detect the slightest movement. Their carapace is composed of armoured plates called lamellae.").also { stage++ }
            30 -> npcl(FaceAnim.HALF_GUILTY, "This shell can be compressed into a ball or fanned out like leaves, in order to sense odours. The front legs are adapted for digging the enormous tunnel systems that serve as their nests.").also { stage++ }
            31 -> npcl(FaceAnim.HALF_GUILTY, "They exist in a caste-based society, with the soft shelled larvae at the bottom, up through the workers, soldiers and finally the queen.").also { stage++ }
            32 -> npcl(FaceAnim.HALF_GUILTY, "Voracious carnivores, a pack of adult workers can strip the flesh from a full grown camel in a matter of seconds, leaving nothing but a few bones and strips of fur for other scavengers to pick over.").also { stage++ }
            33 -> npcl(FaceAnim.HALF_GUILTY, "They typically live in large nests marked by the rock hard pillars found in hot, arid deserts, such as the one south-west of Al Kharid, which rise out of the sands like the tombs of desert pharaohs.").also { stage++ }
            34 -> npcl(FaceAnim.HALF_GUILTY, "Indeed, there is some relationship between the Kalphite Queen and the desert god Scabaras, but no one is really sure what.").also { stage++ }
            35 -> npcl(FaceAnim.HALF_GUILTY, "During the early part of the fourth age, Scabaras proclaimed himself omnipotent and outlawed worship of all other gods save him.").also { stage++ }
            36 -> npcl(FaceAnim.HALF_GUILTY, "When the people eventually revolted against his repressive rule and banished Scabaras, it is said his blood washed over the scarabs and transformed them into the kalphites we know today.").also { stage++ }
            37 -> npcl(FaceAnim.HALF_GUILTY, "Of course, any right-minded scientist discounts these myths as mere stories, with no historical basis in fact.").also { stage++ }
            38 -> npcl(FaceAnim.HALF_GUILTY, "And this concludes my short lecture on kalphites. I hope you've enjoyed yourselves.").also { stage++ }

            39 -> npcl(FaceAnim.HALF_GUILTY, "Ahh lizards, the scaly carnivores.").also { stage++ }
            40 -> npcl(FaceAnim.HALF_GUILTY, "If you just follow me to the display case I shall explain all about them.").also { stage++ }
            41 -> npcl(FaceAnim.HALF_GUILTY, "Contrary to popular belief, even though most lizards have a yellow belly, they are not in fact cowardly. Most ${GameWorld.settings?.name} lizards will not shy away from a good fight and can be very tough.").also { stage++ }
            42 -> npcl(FaceAnim.HALF_GUILTY, "This is due to a very thick and tough hide, made from thousands of thick scales. Most people who claim to be hunters have a very hard time trying to dispatch lizards.").also { stage++ }
            43 -> npcl(FaceAnim.HALF_GUILTY, "In fact, the only people to successfully discern how to kill these tough little squamatas are the five legendary Slayer masters, although we assume they must have some kind of natural predator.").also { stage++ }
            44 -> npcl(FaceAnim.HALF_GUILTY, "Interestingly enough, these scales are made from the same substance that your hair is comprised of. This substance is called keratin.").also { stage++ }
            45 -> npcl(FaceAnim.HALF_GUILTY, "Lizards have a very well developed sense of vision and hearing. Some people think that some lizards have a third eye!").also { stage++ }
            46 -> npcl(FaceAnim.HALF_GUILTY, "A tiny, light-sensitive, transparent structure on top of the head that helps them regulate how long they stay in the sun.").also { stage++ }
            47 -> npcl(FaceAnim.HALF_GUILTY, "This is vital for the cold-blooded lizards who have no means to regulate their internal temperature.").also { stage++ }
            48 -> npcl(FaceAnim.HALF_GUILTY, "Like many cold-blooded creatures, if they are subjected to a sudden decrease in temperature, they will become sluggish and sleepy.").also { stage++ }
            49 -> npcl(FaceAnim.HALF_GUILTY, "And this concludes my short lecture on lizards. I hope you've enjoyed yourselves.").also { stage = 5 }

            50 -> npcl(FaceAnim.HALF_GUILTY, "Ahh tortoises, the armoured ancients.").also { stage++ }
            51 -> npcl(FaceAnim.HALF_GUILTY, "If you just follow me to the display case I shall explain all about them.").also { stage++ }
            52 -> npcl(FaceAnim.HALF_GUILTY, "The tortoise is a very well defended beast. It uses an armoured shell just like its aquatic cousins the terrapin and the turtle.").also { stage++ }
            53 -> npcl(FaceAnim.HALF_GUILTY, "Tortoises can vary in size from about as long as your hand to as big as a unicorn. Most land tortoises eat nothing but plants in the wild.").also { stage++ }
            54 -> npcl(FaceAnim.HALF_GUILTY, "Did you know you can tell how old a tortoise is by the number of rings in its shell, just like a tree.").also { stage++ }
            55 -> npcl(FaceAnim.HALF_GUILTY, "Most land-based tortoises eat plants, feeding on grazing grasses, weeds, leafy greens, flowers, and cabbages.").also { stage++ }
            56 -> npcl(FaceAnim.HALF_GUILTY, "Tortoises generally live as long as people, and some individual ones are known to have lived longer than 300 years.").also { stage++ }
            57 -> npcl(FaceAnim.HALF_GUILTY, "Because of this, they symbolise longevity within some cultures, such as gnomes who also breed them for battle.").also { stage++ }
            58 -> npcl(FaceAnim.HALF_GUILTY, "The oldest tortoise ever recorded was Mibbiwocket, who was presented to the King Healthorg the Great, by the famous explorer Admiral Bake, shortly after its birth.").also { stage++ }
            59 -> npcl(FaceAnim.HALF_GUILTY, "Mibbiwocket is still in the care of the gnomish royal family.").also { stage++ }
            60 -> npcl(FaceAnim.HALF_GUILTY, "And this concludes my short lecture on tortoises. I hope you've enjoyed yourselves.").also { stage = 5 }

            61 -> npcl(FaceAnim.HALF_GUILTY, "Ahh dragons, the mighty hunters of the sky.").also { stage++ }
            62 -> npcl(FaceAnim.HALF_GUILTY, "If you just follow me to the display case I shall explain all about them.").also { stage++ }
            63 -> npcl(FaceAnim.HALF_GUILTY, "The dragons of ${GameWorld.settings?.name} are a most confusing species. Standing at approximately twelve feet tall, these imposing predators strike fear into the hearts and minds of most sane folk.").also { stage++ }
            64 -> npcl(FaceAnim.HALF_GUILTY, "However, if you delve a little deeper into their history and lifestyle, a few things stick out as very unusual.").also { stage++ }
            65 -> npcl(FaceAnim.HALF_GUILTY, "With most species there is a line, an ancestry if you will, whereby you can track how a creature has come to be in its present form.").also { stage++ }
            66 -> npcl(FaceAnim.HALF_GUILTY, "For instance, you can trace the ancestry of the common house cat back to the same creature that became the sabre-toothed kyatt.").also { stage++ }
            67 -> npcl(FaceAnim.HALF_GUILTY, "However, with the dragon, no such root ancestor can be found.").also { stage++ }
            68 -> npcl(FaceAnim.HALF_GUILTY, "There are many forms of dragon, such as the common coloured and the metallic, or ferrous, dragon.").also { stage++ }
            69 -> npcl(FaceAnim.HALF_GUILTY, "They colonise many areas of ${GameWorld.settings?.name}, though most notably, sites of ancient battles and small dank caves.").also { stage++ }
            70 -> npcl(FaceAnim.HALF_GUILTY, "Eating habits tend to vary, with the majority of their food being meat. However, it has also been noted that they can consume metals just as easily, with runite being thought of as a delicacy.").also { stage++ }
            71 -> npcl(FaceAnim.HALF_GUILTY, "Throughout history, dragons have appeared in myth and legend as fearsome adversaries and cunning creatures.").also { stage++ }
            72 -> npcl(FaceAnim.HALF_GUILTY, "However, modern evidence does not support this. Most young dragons are largely creatures of instinct with a strong vicious streak.").also { stage++ }
            73 -> npcl(FaceAnim.HALF_GUILTY, "The lifespan of the common dragon is as yet unknown, as no dragon has ever been observed dying of old age.").also { stage++ }
            74 -> npcl(FaceAnim.HALF_GUILTY, "Although, it has been mooted that spontaneous combustion could be considered a natural cause of death for this species.").also { stage++ }
            75 -> npcl(FaceAnim.HALF_GUILTY, "And this concludes my short lecture on dragons. I hope you've enjoyed yourselves.").also { stage = 5 }

            76 -> npcl(FaceAnim.HALF_GUILTY, "Ahh, wyverns. The extinct lizards.").also { stage++ }
            77 -> npcl(FaceAnim.HALF_GUILTY, "If you just follow me to the display case I shall explain all about them.").also { stage++ }
            78 -> npcl(FaceAnim.HALF_GUILTY, "The wyverns' tale is a sad one. This now extinct species is presumed in some way to be related to the dragon, in that they are both large flying reptiles. The wyvern stands much shorter than the adult dragon and only has two legs as opposed to the dragon's four.").also { stage++ }
            79 -> npcl(FaceAnim.HALF_GUILTY, "Much of the evidence for wyvern behaviour comes from the reconstruction of old bones in the icy mountains of Asgarnia.").also { stage++ }
            80 -> npcl(FaceAnim.HALF_GUILTY, "As most lizards cannot maintain their own body temperature, two theories as to how they managed to survive have been proposed.").also { stage++ }
            81 -> npcl(FaceAnim.HALF_GUILTY, "One is that Asgarnia was at one time a much more temperate climate than it is now. The other is that wyverns could generate fire internally in much the same way as dragons.").also { stage++ }
            82 -> npcl(FaceAnim.HALF_GUILTY, "If they follow the dragon paradigm, then they would have been carnivores, feeding on cows, sheep and other livestock animals.").also { stage++ }
            83 -> npcl(FaceAnim.HALF_GUILTY, "How and why the wyverns became extinct is something of a mystery. Though if you consider the theory that the climate of Asgarnia changed suddenly, then this could provide an explanation.").also { stage++ }
            84 -> npcl(FaceAnim.HALF_GUILTY, "There are some inconsistencies in the findings we have for the wyverns, such as the odd wear patterns of some of bones, which really could only have happened after the creature died.").also { stage++ }
            85 -> npcl(FaceAnim.HALF_GUILTY, "Also the bones we have collected remain a little below room temperature wherever they are kept.").also { stage++ }
            86 -> npcl(FaceAnim.HALF_GUILTY, "They have also been shown to radiate a very weak magical aura.").also { stage++ }
            87 -> npcl(FaceAnim.HALF_GUILTY, "I'm sure that in due time, these mysteries will be solved.").also { stage++ }
            88 -> npcl(FaceAnim.HALF_GUILTY, "And this concludes my short lecture on wyverns. I hope you've enjoyed yourselves.").also { stage = 5 }

            89  -> npcl(FaceAnim.HALF_GUILTY, "Ahh snails, the gelatinous gastropods.").also { stage++ }
            90  -> npcl(FaceAnim.HALF_GUILTY, "If you just follow me to the display case I shall explain all about them.").also { stage++ }
            91  -> npcl(FaceAnim.HALF_GUILTY, "Snails move like worms by squishing up and stretching out, very, very slowly. They also make a lot of slime, in order to aid moving by reducing friction.").also { stage++ }
            92  -> npcl(FaceAnim.HALF_GUILTY, "They also use slime for protection. For instance, snails can use their slime to crawl over razor-blades without being hurt. It also helps keep away dangerous insects like ants.").also { stage++ }
            93  -> npcl(FaceAnim.HALF_GUILTY, "When they hide in their shells, snails secrete a special type of slime, which dries to cover the entrance of their shells like a 'trapdoor'. This is called an operculum.").also { stage++ }
            94  -> npcl(FaceAnim.HALF_GUILTY, "The snails of Morytania are the most malignant molluscs ever to have been studied.").also { stage++ }
            95  -> npcl(FaceAnim.HALF_GUILTY, "They are broken down into two distinct species: achatina acidia and achatina acidia giganteus or, as they are more commonly known, the acid-spitting snail and the giant acid-spitting snail.").also { stage++ }
            96  -> npcl(FaceAnim.HALF_GUILTY, "Both of these varieties are voracious carnivores, using their mutated mouthpieces to spit a glob of powerful acid to kill their foe.").also { stage++ }
            97  -> npcl(FaceAnim.HALF_GUILTY, "They then simply have to wait, whilst the digestive juices make short work of the poor creature. Then, they simply slurp up what remains.").also { stage++ }
            98  -> npcl(FaceAnim.HALF_GUILTY, "How these strange creatures came to be is still something of a mystery. The most prevalent theory suggests that they mutated, as a reaction to an 'as yet unknown' pollutant that has appeared in the swamps.").also { stage++ }
            99  -> npcl(FaceAnim.HALF_GUILTY, "The local populace has capitalised on the appearance of these strange species, using their shells to fashion a rudimentary helm that is fairly resistant to the snails acid.").also { stage++ }
            100 -> npcl(FaceAnim.HALF_GUILTY, "Other known uses of snail by-products include a tasty local delicacy and a fireproof oil.").also { stage++ }
            101 -> npcl(FaceAnim.HALF_GUILTY, "And this concludes my short lecture on snails. I hope you've enjoyed yourselves.").also { stage = 5 }

            102 -> npcl(FaceAnim.HALF_GUILTY, "Ahh monkeys, the simian collective.").also { stage++ }
            103 -> npcl(FaceAnim.HALF_GUILTY, "If you just follow me to the display case I shall explain all about them.").also { stage++ }
            104 -> npcl(FaceAnim.HALF_GUILTY, "A monkey is a member of either of two known groupings of simian primates. These two known groupings are the Karamja monkeys and the 'harmless' monkeys.").also { stage++ }
            105 -> npcl(FaceAnim.HALF_GUILTY, "Because of their similarity to monkeys, apes such as chimpanzees and gibbons are often called monkeys by accident.").also { stage++ }
            106 -> npcl(FaceAnim.HALF_GUILTY, "Though some natural historians don't consider them to be monkeys. Also, a few monkey species have the word 'ape' in their common name.").also { stage++ }
            107 -> npcl(FaceAnim.HALF_GUILTY, "The Karamja monkeys are rumoured to be fairly cunning and intelligent creatures, although rumours that they have learned human speech is anecdotal at best.").also { stage++ }
            108 -> npcl(FaceAnim.HALF_GUILTY, "In appearance, they stand much shorter than a human and tend to move in a hunched fashion. Karamja monkeys also sport a red mohawk, though it is unknown whether this is an affectation or not.").also { stage++ }
            109 -> npcl(FaceAnim.HALF_GUILTY, "They are very fond of bananas and bitternuts, eating them in huge quantities whenever they can get their paws on them.").also { stage++ }
            110 -> npcl(FaceAnim.HALF_GUILTY, "The harmless monkeys of Mos Le'Harmless are a very similar, but in some ways entirely different, breed. They stand roughly the same size but are a lighter colour.").also { stage++ }
            111 -> npcl(FaceAnim.HALF_GUILTY, "Interestingly, Karamaja monkeys have a deep dislike of seaweed, though this may stem from the actions of a number of irresponsible people.").also { stage++ }
            112 -> npcl(FaceAnim.HALF_GUILTY, "And this concludes my short lecture on monkeys. I hope you've enjoyed yourselves.").also { stage = 5 }

            113 -> npcl(FaceAnim.HALF_GUILTY, "Ahh sea slugs, the cute crustaceans.").also { stage++ }
            114 -> npcl(FaceAnim.HALF_GUILTY, "If you just follow me to the display case I shall explain all about them.").also { stage++ }
            115 -> npcl(FaceAnim.HALF_GUILTY, "The term 'sea slug' is something of a misnomer. Whilst these small creatures have a soft body like that of a slug, they also possess a very hard shell like a snail.").also { stage++ }
            116 -> npcl(FaceAnim.HALF_GUILTY, "Very little is actually known about the sea slug, as we have, as yet, been unable to procure a sample for observation and study.").also { stage++ }
            117 -> npcl(FaceAnim.HALF_GUILTY, "For some reason all expeditions we have sent have either vanished mysteriously, or those on the expedition have sent letters back announcing their desire to leave the Museum and go on to other things.").also { stage++ }
            118 -> npcl(FaceAnim.HALF_GUILTY, "It is presumed that the species is native to the very deep waters around the eastern Ardougne coastline.").also { stage++ }
            119 -> npcl(FaceAnim.HALF_GUILTY, "There must be some natural resources in the area that the sea slugs are using, as the underwater habitat there is much the same around many coastal areas on ${GameWorld.settings?.name}.").also { stage++ }
            120 -> npcl(FaceAnim.HALF_GUILTY, "Through looking at similar species we have determined that the sea slug is a harmless little creature. It spends much of its life grazing on seaweed and other plant life.").also { stage++ }
            121 -> npcl(FaceAnim.HALF_GUILTY, "There are reports that these reclusive animals have two large fangs at their front, though this is assumed to be either for decorative or defensive purposes.").also { stage++ }
            122 -> npcl(FaceAnim.HALF_GUILTY, "If they do follow the same pattern as other similar creatures, the shell will be nigh on impervious to most attacks. The exposed soft skin may have a number of nematocysts, or stinging organs, similar to jellyfish.").also { stage++ }
            123 -> npcl(FaceAnim.HALF_GUILTY, "It is typical of prey animals such as these to develop some kind of unique defence mechanism that allows them to survive.").also { stage++ }
            124 -> npcl(FaceAnim.HALF_GUILTY, "If only we could acquire one for study. I'm sure we would find this mechanism to be truly unique.").also { stage++ }
            125 -> npcl(FaceAnim.HALF_GUILTY, "And this concludes my short lecture on sea slugs. I hope you've enjoyed yourselves.").also { stage = 5 }

            126 -> npcl(FaceAnim.HALF_GUILTY, "Ahh snakes, the slithering squamata.").also { stage++ }
            127 -> npcl(FaceAnim.HALF_GUILTY, "If you just follow me to the display case I shall explain all about them.").also { stage++ }
            128 -> npcl(FaceAnim.HALF_GUILTY, "Serpentes, or ophidia, is the suborder under squamata that snakes belong to, like a big family.").also { stage++ }
            129 -> npcl(FaceAnim.HALF_GUILTY, "Unlike lizards, snakes have no limbs whatsoever; not that this limits them. Snakes live in or near every habitat in the world.").also { stage++ }
            130 -> npcl(FaceAnim.HALF_GUILTY, "Found in nice tropical forests, temperate latitudes and even in the ocean - some have adapted specialised stomach juices that they inject into their prey as venom, while others prefer to grab and crush their food.").also { stage++ }
            131 -> npcl(FaceAnim.HALF_GUILTY, "While others still are fast hunters who use speed and strength to overcome their prey.").also { stage++ }
            132 -> npcl(FaceAnim.HALF_GUILTY, "The sense of smell in snakes has been enhanced in an amazing way. With most animals, like you and me, tiny particles are filtered through the nose.").also { stage++ }
            133 -> npcl(FaceAnim.HALF_GUILTY, "However, instead of using just their nose, these animals use their tongues as well. When a lizard or a snake wants to smell it's surroundings, it will wave its tongue around and pick up the particles in the air.").also { stage++ }
            134 -> npcl(FaceAnim.HALF_GUILTY, "The tongue then returns to the mouth and the tips of the tongue are pushed up against two tiny pits in the roof of the snake's mouth.").also { stage++ }
            135 -> npcl(FaceAnim.HALF_GUILTY, "Since these pits are split apart from each other, the tongue itself also has to split. This is why snakes have forked tongues.").also { stage++ }
            136 -> npcl(FaceAnim.HALF_GUILTY, "So the next time you see a snake sticking it's tongue out at you, remember, it's sniffing the air, not trying to bite you.").also { stage++ }
            137 -> npcl(FaceAnim.HALF_GUILTY, "And this concludes my short lecture on snakes. I hope you've enjoyed yourselves.").also { stage = 5 }

            138 -> npcl(FaceAnim.HALF_GUILTY, "Ahh camels, the ships of the desert.").also { stage++ }
            139 -> npcl(FaceAnim.HALF_GUILTY, "If you just follow me to the display case I shall explain all about them.").also { stage++ }
            140 -> npcl(FaceAnim.HALF_GUILTY, "The term camel refers to either of the two known species of camelid.").also { stage++ }
            141 -> npcl(FaceAnim.HALF_GUILTY, "There is the bactrian, which has two humps and can be found throughout Al Kharid. It also refers to the Ugthanki, or one-humped camel.").also { stage++ }
            142 -> npcl(FaceAnim.HALF_GUILTY, "Camels are ill-tempered beasts at the best of times, requiring a great deal of time and effort to tame.").also { stage++ }
            143 -> npcl(FaceAnim.HALF_GUILTY, "This would explain why used camel salesmen are in such a hurry to be rid of them.").also { stage++ }
            144 -> npcl(FaceAnim.HALF_GUILTY, "In the wild, camels have been known to lie in wait and ambush hapless travellers, before devouring them.").also { stage++ }
            145 -> npcl(FaceAnim.HALF_GUILTY, "This is quite surprising, as most domestic camels are happy eating nothing but vegetables.").also { stage++ }
            146 -> npcl(FaceAnim.HALF_GUILTY, "Camel milk is much more nutritious than cow milk and goes well in the strong desert drink akin to tea.").also { stage++ }
            147 -> npcl(FaceAnim.HALF_GUILTY, "Another useful camel by-product is dung. Their dung is very dry, due to the highly efficient metabolism of the camel.").also { stage++ }
            148 -> npcl(FaceAnim.HALF_GUILTY, "Scientific research has also shown that chilli has a disastrous effect on a camel's digestive system, which produces toxic dung.").also { stage++ }
            149 -> npcl(FaceAnim.HALF_GUILTY, "And this concludes my short lecture on camels. I hope you've enjoyed yourselves.").also { stage = 5 }

            150 -> npcl(FaceAnim.HALF_GUILTY, "Ahh leeches, the haemophagic parasites.").also { stage++ }
            151 -> npcl(FaceAnim.HALF_GUILTY, "If you just follow me to the display case I shall explain all about them.").also { stage++ }
            152 -> npcl(FaceAnim.HALF_GUILTY, "Leeches are fascinating creatures and are very similar to worms in most respects.").also { stage++ }
            153 -> npcl(FaceAnim.HALF_GUILTY, "They like to inhabit streams, rivers and seas, but their preference is for stagnant pools of water.").also { stage++ }
            154 -> npcl(FaceAnim.HALF_GUILTY, "One of the most common misconceptions is that all leeches feed on blood.").also { stage++ }
            155 -> npcl(FaceAnim.HALF_GUILTY, "In fact, very, very few leeches are parasitic bloodsucking animals.").also { stage++ }
            156 -> npcl(FaceAnim.HALF_GUILTY, "Most leeches are meat eaters, feeding on a variety of invertebrates such as worms, snails, insect larvae and snails.").also { stage++ }
            157 -> npcl(FaceAnim.HALF_GUILTY, "Those that do feed on blood have developed an amazing method of doing so. Firstly they latch onto the skin using a ring of tiny teeth, before injecting their prey with an anaesthetic.").also { stage++ }
            158 -> npcl(FaceAnim.HALF_GUILTY, "Then they bite into the skin using a Y-shaped mouthpiece and introducing a chemical that stops the blood from clotting.").also { stage++ }
            159 -> npcl(FaceAnim.HALF_GUILTY, "They will then feed until they are completely full, sometimes doubling in size!").also { stage++ }
            160 -> npcl(FaceAnim.HALF_GUILTY, "Most leeches are very small, measuring no more than the length of your middle finger. An exception to these are the leeches of Morytania, which can reach the size of a dog.").also { stage++ }
            161 -> npcl(FaceAnim.HALF_GUILTY, "They are much more mobile than their smaller cousins and are able to jump rather high when attacking.").also { stage++ }
            162 -> npcl(FaceAnim.HALF_GUILTY, "Quite how these leeches came to be so big is something of a mystery.").also { stage++ }
            163 -> npcl(FaceAnim.HALF_GUILTY, "All we can assume is that there is some kind of environmental influence, which has governed their immense growth.").also { stage++ }
            164 -> npcl(FaceAnim.HALF_GUILTY, "And this concludes my short lecture on leeches. I hope you've enjoyed yourselves.").also { stage = 5 }

            165 -> npcl(FaceAnim.HALF_GUILTY, "Ahh moles, the mammalian mountain makers.").also { stage++ }
            166 -> npcl(FaceAnim.HALF_GUILTY, "If you just follow me to the display case I shall explain all about them.").also { stage++ }
            167 -> npcl(FaceAnim.HALF_GUILTY, "Now, moles are small mammals of the talpidae family. These subterranean burrowers mainly live on a diet of slugs, snails and insects.").also { stage++ }
            168 -> npcl(FaceAnim.HALF_GUILTY, "They vary greatly in habitat and can be found in almost every part of ${GameWorld.settings?.name}. Some species have even known to be aquatic.").also { stage++ }
            169 -> npcl(FaceAnim.HALF_GUILTY, "Male moles are known as boars with the females called sows. Should you come across a group of moles, you would call them a labour.").also { stage++ }
            170 -> npcl(FaceAnim.HALF_GUILTY, "Moles are considered to be an agricultural pest in most places, digging up the ground and leaving molehills all over the place.").also { stage++ }
            171 -> npcl(FaceAnim.HALF_GUILTY, "This has been highlighted in Falador by Wyson the Gardener who, after using some Malignus Mortifer's Super Ultra Flora Growth Potion, managed to create ${GameWorld.settings?.name}'s only known species of giant mole.").also { stage++ }
            172 -> npcl(FaceAnim.HALF_GUILTY, "This fearsome beast has huge claws, wicked teeth and a penchant for shiny objects. It is a very tough animal with a thick protective hide and an ill temperament.").also { stage++ }
            173 -> npcl(FaceAnim.HALF_GUILTY, "That said, they do benefit the soil by aerating and tilling it, adding to its fertility. Contrary to popular belief, moles don't eat plant roots.").also { stage++ }
            174 -> npcl(FaceAnim.HALF_GUILTY, "And this concludes my short lecture on moles. I hope you've enjoyed yourselves.").also { stage = 5 }

            175 -> npcl(FaceAnim.HALF_GUILTY, "Ahh penguins, the cunning birds of the sea.").also { stage++ }
            176 -> npcl(FaceAnim.HALF_GUILTY, "If you just follow me to the display case I shall explain all about them.").also { stage++ }
            177 -> npcl(FaceAnim.HALF_GUILTY, "This often-maligned aquatic bird is known to be native to the ice fields of Etceteria, although they have been known to live as far as the fields of Lumbridge.").also { stage++ }
            178 -> npcl(FaceAnim.HALF_GUILTY, "These aquatic birds seem to spend the majority of their time eating and fostering their young.").also { stage++ }
            179 -> npcl(FaceAnim.HALF_GUILTY, "Unlike most animals that prefer cold climes, these sphenisciformes work very well together in large groups, by watching for predators and caring for each other's young.").also { stage++ }
            180 -> npcl(FaceAnim.HALF_GUILTY, "For creatures with such small brains, they do seem to have a disproportionate capacity for forward thinking and planning. As this serves no natural purpose, scholars are divided as to how this evolved.").also { stage++ }
            181 -> npcl(FaceAnim.HALF_GUILTY, "Their diet consists mainly of fish, squid and a small shrimp-like creature called krill. However, some have developed a taste for the mushrooms that grow around fairy rings.").also { stage++ }
            182 -> npcl(FaceAnim.HALF_GUILTY, "Penguins primarily rely on their vision while hunting. What we don't know is how penguins locate prey in the darkness, or at great depths.").also { stage++ }
            183 -> npcl(FaceAnim.HALF_GUILTY, "Some theories suggest that penguins are helped by some sort of extra sensory perception; perhaps even precognition.").also { stage++ }
            184 -> npcl(FaceAnim.HALF_GUILTY, "Penguins spend a long time going without food when they are breeding. In fact, they won't even leave their nests if they can help it.").also { stage++ }
            185 -> npcl(FaceAnim.HALF_GUILTY, "Fortunately, most penguins build up a layer of fat to keep them warm and provide energy until the moult is over.").also { stage++ }
            186 -> npcl(FaceAnim.HALF_GUILTY, "And this concludes my short lecture on penguins. I hope you've enjoyed yourselves.").also { stage = 5 }

            1000 -> npc(FaceAnim.FRIENDLY, "Nonsense! There's always room for more.", "And remember, science isn't dull!").also { stage = END_DIALOGUE }

        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = NaturalHistorianDialogue(player)

    override fun getIds(): IntArray = intArrayOf(
        NPCs.NATURAL_HISTORIAN_5966,
        NPCs.NATURAL_HISTORIAN_5967,
        NPCs.NATURAL_HISTORIAN_5968,
        NPCs.NATURAL_HISTORIAN_5969,
    )
}
