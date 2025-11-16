package content.global.skill.summoning.familiar.dialogue

import content.global.skill.summoning.pets.Pet
import content.global.skill.summoning.pets.Pets
import core.api.*
import core.game.dialogue.Dialogue
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.RegionManager.getLocalNpcs
import core.game.world.map.path.Pathfinder
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Sounds
import kotlin.random.Random

/**
 * Represents the Kitten interaction pet dialogue.
 */
@Initializable
class KittenInteractDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        val npcName = npc?.id?.let { getNPCName(it) } ?: "kitten"
        val displayName = if (npcName.contains("cat", ignoreCase = true)) "cat" else "kitten"
        val familiar = player.familiarManager.familiar ?: return false

        if (familiar.owner != player && familiar.name.lowercase().contains("hell")) {
            npcl(null, "Hiss! Go away before I scratch those curious eyes out!")
            return true
        }

        setTitle(player, 3)
        sendOptions(player, "Interact with $displayName", "Stroke.", "Chase vermin.", "Shoo away.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val familiar = player.familiarManager.familiar ?: return false

        when (buttonId) {
            1 -> {
                queueScript(player, 1, QueueStrength.WEAK) {
                    player.animate(PLAYER_STROKE_ANIMATION)
                    familiar.face(player)
                    familiar.animate(KITTEN_STROKE_ANIMATION)
                    playAudio(player, Sounds.PURR_340)
                    familiar.sendChat("Purr...purr...", 1)
                    sendDialogue(player, "That cat sure loves to be stroked.")
                    familiar.sendChat("Miaow!", PLAYER_STROKE_ANIMATION.duration)
                    stopExecuting(player)
                }
            }
            2 -> {
                end()
                player.sendChat("Go on puss...kill that rat!")
                val rat = getLocalNpcs(player.location, 10)
                    .filter { it.name.contains("rat", ignoreCase = true) }
                    .minByOrNull { it.location.getDistance(familiar.location) }

                if (rat == null || rat.location.getDistance(familiar.location) >= 8) {
                    sendMessage(player, "Your cat cannot get to its prey.")
                    return true
                }

                playAudio(player, Sounds.KITTENS_MEW_339)
                familiar.sendChat("Meeeeeoooow!")
                Pathfinder.find(familiar, rat).walk(familiar)
                rat.sendChat("Eeek!")

                Pulser.submit(object : Pulse(5) {
                    override fun pulse(): Boolean {
                        familiar.call()
                        sendMessage(player, "The rat manages to get away!")

                        if (rat.name.equals("hell-rat", ignoreCase = true) && Random.nextInt(100) == 0) {
                            familiar.owner.setAttribute("/save:hellcat", true)
                            val pet = familiar as Pet
                            val pets = Pets.forId(pet.itemId) ?: return true
                            val hellcatID = when (pet.itemId) {
                                pets.babyItemId -> Items.HELL_KITTEN_7583
                                pets.grownItemId -> Items.HELL_CAT_7582
                                pets.overgrownItemId -> Items.OVERGROWN_HELLCAT_7581
                                else -> return true
                            }
                            val item = Item(hellcatID)
                            playAudio(player, Sounds.CAT_INTO_HELLCAT_1008)
                            player.familiarManager.morphPet(item, false, pet.location)
                            sendMessage(player, "Your cat suddenly transforms!")
                            familiar.face(player)
                            familiar.sendChat("Meoooooow!")
                        }
                        return true
                    }
                })
            }
            3 -> {
                sendOptions(player, "Are you sure?", "Yes I am.", "No I'm not.")
                stage = 4
            }
        }

        if (stage == 4) {
            when (buttonId) {
                1 -> {
                    end()
                    if (!player.familiarManager.hasFamiliar()) return true
                    val pet = player.familiarManager.familiar
                    player.sendChat("Shoo cat!")
                    pet.sendChat("Miaow!")
                    player.familiarManager.removeDetails(pet.idHash)
                    pet.dismiss()
                    player.packetDispatch.sendMessage("The cat has run away.")
                }
                2 -> end()
            }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(343823)

    companion object {
        private val PLAYER_STROKE_ANIMATION = Animation(Animations.KITTEN_STROKE_9224)
        private val KITTEN_STROKE_ANIMATION = Animation(Animations.KITTEN_STROKE_ANIMATION_9173)
    }
}