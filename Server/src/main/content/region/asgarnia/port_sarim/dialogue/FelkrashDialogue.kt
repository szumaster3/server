package content.region.asgarnia.port_sarim.dialogue

import content.global.skill.summoning.pets.Pets
import core.api.hasRequirement
import core.api.openInterface
import core.api.sendDialogue
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Felkrash dialogue.
 */
@Initializable
class FelkrashDialogue : Dialogue {

    override fun newInstance(player: Player?): Dialogue = FelkrashDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if(!hasRequirement(player, Quests.RATCATCHERS)) {
            playerl(FaceAnim.HALF_GUILTY, "What was it I heard about some pits around here?")
        } else {
            showTopics(
                Topic("Can you train my cat for me?", 20),
                Topic("Can you rename my cat?",40),
                Topic("What was it I heard about some pits around here?", 60),
                IfTopic("Can you tell me more about Wily cats?", 70, hasRequirement(player, Quests.RATCATCHERS))
            )
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage) {
            0 -> sendDialogue(player, "- Felkrash breaks down into tears -").also { stage++ }
            1 -> npcl(FaceAnim.SAD, "It is... It used to be my rat pits. You could bring your cats here to fight. It was going to be glorious! It was open for years. Not a single person visited!").also { stage++ }
            2 -> npcl(FaceAnim.THINKING, "How could so many be so foolish as to ignore such brilliance?").also { stage++ }
            3 -> playerl(FaceAnim.HALF_THINKING, "I guess brilliance is one word for it.").also { stage++ }
            4 -> sendDialogue(player, "- Felkrash glares at you angrily, you walk away while you can. -").also { stage++ }
            5 -> end()

            // Morph
            20 -> {
                val pet = player.familiarManager.familiar
                if (pet == null) {
                    npcl(FaceAnim.ANGRY, "I can only train overgrown cats into wilycats.")
                    stage = END_DIALOGUE
                    return true
                }

                val petsDef = Pets.forId(pet.id)
                if (petsDef == null) {
                    npcl(FaceAnim.ANGRY, "I can only train overgrown cats into wilycats.")
                    stage = END_DIALOGUE
                    return true
                }

                when (pet.id) {
                    petsDef.overgrownItemId -> {
                        Pulser.submit(object : Pulse(5) {
                            override fun pulse(): Boolean {
                                val overgrownName = Item(petsDef.overgrownItemId).definition.name.lowercase()
                                if ("lazy" !in overgrownName && "wily" !in overgrownName) {
                                    val wilyCatID = Items.WILY_HELLCAT_7585
                                    player.familiarManager.morphPet(Item(wilyCatID), false, pet.location)
                                    sendMessage(player, "Your overgrown cat has grown into a lean mean hunting cat.")
                                    npcl(FaceAnim.HAPPY, "You should really name your cat.")
                                } else {
                                    npcl(FaceAnim.ANGRY, "This cat cannot be trained into a wily cat.")
                                }
                                stage = END_DIALOGUE
                                return true
                            }
                        })
                    }
                    in Items.WILY_CAT_6555..Items.WILY_CAT_6560, Items.WILY_CAT_14093 -> {
                        npcl(FaceAnim.ANGRY, "You already have a mature cat, look after that first.")
                        stage = END_DIALOGUE
                    }
                    else -> {
                        npcl(FaceAnim.ANGRY, "I can only train overgrown cats into wilycats.")
                        stage = END_DIALOGUE
                    }
                }
            }

            // Rename Cat
            40 -> {
                val pet = player.familiarManager.familiar
                if(pet == null || pet.id !in Items.LAZY_CAT_6549..Items.WILY_CAT_6560) {
                    npcl(FaceAnim.ANGRY, "I'll only help you name wily or lazy cats. Bring me one and I'll help you out.")
                    stage = END_DIALOGUE
                } else {
                    openInterface(player, Components.CAT_NAMING_61)
                    stage = END_DIALOGUE
                }
            }

            60 -> npcl(FaceAnim.HALF_GUILTY, "It is... It used to be my rat pits.").also { stage++ }
            61 -> npcl(FaceAnim.HALF_GUILTY, "You could bring your cats here to fight. It was going to be glorious! It was open for years.").also { stage++ }
            62 -> npcl(FaceAnim.HALF_GUILTY, "Not a single person visited! How could so many be so foolish as to ignore such brilliance?").also { stage++ }

            70 -> npcl(FaceAnim.NEUTRAL, "Of course, of course, they're the highest form of cat that I know of.").also { stage++ }
            71 -> npcl(FaceAnim.NEUTRAL, "They're tougher, stronger and faster than other variations of cat, they can catch butterflies and kalphite larva and are far superior to any other type of cat at hunting.").also { stage++ }
            72 -> npcl(FaceAnim.NEUTRAL, "Wily cats need looking after or else they get fat and lazy.").also { stage++ }
            73 -> npcl(FaceAnim.NEUTRAL, "The key is to keep it active, bring it hunting regularly. Try keep it happy too, they can be a little nasty when they're cranky.").also { stage++ }
            74 -> npcl(FaceAnim.NEUTRAL, "They would get fat and lazy, be much worse at hunting and would show you up as a really poor cat trainer.").also { stage++ }
            75 -> npcl(FaceAnim.NEUTRAL, "Yes you can get your lazy cat back in shape with a lot of rat catching, but you really shouldn't let it get into that state in the first place.").also { stage++ }
            76 -> end()
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.FELKRASH_2951)
}
