package content.region.misthalin.edge.dialogue

import content.global.skill.summoning.pet.Pet
import core.api.hasRequirement
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Animations
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Doris dialogue.
 */
@Initializable
class DorisDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!hasRequirement(player, Quests.RECIPE_FOR_DISASTER)) {
            npc("What are you doing in my house?")
        } else {
            npc("Hello again dearie. How are you doing?").also { stage = 5 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val hasHellCat = player.familiarManager.hasPet() && player.familiarManager.familiar.name.contains("hell", ignoreCase = true)
        when (stage) {
            0 -> options(
                "I'm just wandering around.",
                "I want to use your kitchen.",
                "Give me all your money!"
            ).also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player("I'm just wandering around.").also { stage++ }
                    2 -> player("I want to use your kitchen.").also { stage += 2 }
                    3 -> player("Give me all your money!").also { stage += 3 }
                }
            2 -> npc("Would you mind wandering out of my house?").also { stage = END_DIALOGUE }
            3 -> npc("I suppose you can, but try not to make a mess.").also { stage = END_DIALOGUE }
            4 -> npc("I haven't got any money!").also { stage = END_DIALOGUE }
            5 -> showTopics(
                Topic("Pretty good!", 7),
                Topic("Not too good actually!", 8),
                Topic("What's it like living so close to the Wilderness?", 9),
                Topic("How did Dave come to be evil?", 14),
                IfTopic("What's happened to my cat?", 19, hasHellCat)
            )
            7 -> npc("That's good to hear.").also { stage = END_DIALOGUE }
            8 -> npc("Oh well.").also { stage = END_DIALOGUE }
            9 -> npcl(FaceAnim.HALF_GUILTY, "Oh, it's not all that bad. It was a bit scary at first but as long as I don't go past the warning signs I'm all right.").also { stage++ }
            10 -> npcl(FaceAnim.HALF_GUILTY, "It's actually pretty quiet here. I used to live in Lumbridge and, let me tell you, that's the dangerous place for people like me.").also { stage++ }
            11 -> npcl(FaceAnim.HALF_GUILTY, "Thieves were picking people's pockets with impunity, and killing them on the streets in broad daylight!").also { stage++ }
            12 -> npcl(FaceAnim.HALF_GUILTY, "And there weren't even any guards to protect us! Not that the guards in the other cities do much good to protect people from what i've heard.").also { stage++ }
            13 -> npcl(FaceAnim.HALF_GUILTY, "I just thank goodness I don't have an Attack option any more! I wouldn't last five minutes!").also { stage = 5 }
            14 -> npcl(FaceAnim.HALF_GUILTY, "Oh, I don't really know. He's always been evil, I think.").also { stage++ }
            15 -> npcl(FaceAnim.HALF_GUILTY, "Maybe growing up so close to the wilderness has affected him in some way.").also { stage++ }
            16 -> npcl(FaceAnim.HALF_GUILTY, "When he was little he used to want to go into Edgeville dungeon. You know how kids are, always wanting to be dungeon adventurers! But he didn't want to be a hero, he always wanted to be a monster!").also { stage++ }
            17 -> npcl(FaceAnim.HALF_GUILTY, "And then he spent all summer building a tree-house. Except that it wasn't a tree-house, it was his Tower of Fear!").also { stage++ }
            18 -> npcl(FaceAnim.HALF_GUILTY, "Of course I was hoping he'd have grown out of it by now and maybe even left home. But at least it keeps him happy.").also { stage = END_DIALOGUE }
            19 -> npc(FaceAnim.SAD, "Oh, poor thing. The hell-rats do that, you know. I think", "a nice drink of milk would sort it out. Shall I give it", "one?").also { stage++ }
            20 -> options("Yes please!", "No, I like it this way.").also { stage++ }
            21 -> when (buttonId) {
                1 -> player("Yes please!").also { stage++ }
                2 -> player("No, I like it this way.").also { stage = 23 }
            }
            22 -> {
                end()
                npc("Here puss...")
                npc.animate(Animation(Animations.MULTI_BEND_OVER_827))
                val familiar = player.familiarManager.familiar
                if (familiar is Pet) {
                    val itemId = familiar.itemId
                    player.familiarManager.morphPet(
                        Item(itemId),
                        false,
                        player.location
                    )
                    familiar.sendChat("Meeow!")
                    player.setAttribute("/save:hellcat", false)
                    sendMessage(player, "Your hell-cat transforms into an ordinary cat.")
                }
            }
            23 -> npcl(FaceAnim.FRIENDLY, "Well, I suppose as long as the cat is all right, it doesn't matter what it looks like.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = DorisDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.DORIS_3381)
}
