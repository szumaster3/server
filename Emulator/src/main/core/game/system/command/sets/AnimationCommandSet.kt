package core.game.system.command.sets

import core.game.node.entity.npc.NPC
import core.game.system.command.CommandPlugin.Companion.toInteger
import core.game.system.command.Privilege
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable

@Initializable
class AnimationCommandSet : CommandSet(Privilege.ADMIN) {
    private var npcs: List<NPC> = ArrayList()

    override fun defineCommands() {
        define(
            name = "anim",
            privilege = Privilege.ADMIN,
            usage = "::anim <lt>Animation ID<gt>",
            description = "Plays the animation with the given ID.",
        ) { player, args ->
            if (args.size < 2) {
                reject(player, "Syntax error: ::anim <Animation ID>")
            }
            val animation = Animation(args[1].toInt())
            player.animate(animation)
        }

        define(
            name = "loopanim",
            privilege = Privilege.ADMIN,
            usage = "::loopanim <lt>Animation ID<gt> <lt>Times<gt>",
            description = "Plays the animation with the given ID the given number of times",
        ) { player, args ->
            if (args.size < 2) {
                reject(player, "Syntax error: ::loopanim <Animation ID> <Loop Amount>")
            }
            val start = toInteger(args[1])
            var end = toInteger((args[2]))
            if (end > 25) {
                notify(player, "Really...? $end times...? Looping 25 times instead.")
                end = 25
            }
            GameWorld.Pulser.submit(
                object : Pulse(3, player) {
                    var id = start

                    override fun pulse(): Boolean {
                        player.animate(Animation.create(id))
                        return ++id >= end
                    }
                },
            )
        }

        define(
            name = "ranim",
            privilege = Privilege.ADMIN,
            usage = "::ranim <lt>Render Anim ID<gt>",
            description = "Sets the player's render (walk/idle) animation.",
        ) { player, args ->
            if (args.size < 2) {
                reject(player, "Syntax error: ::ranim <Render Animation ID>")
            }
            if (args.size > 2) {
                GameWorld.Pulser.submit(
                    object : Pulse(3, player) {
                        var id = args[1].toInt()

                        override fun pulse(): Boolean {
                            player.appearance.setAnimations(Animation.create(id))
                            player.appearance.sync()
                            player.sendChat("Current: $id")
                            return ++id >= args[2].toInt()
                        }
                    },
                )
            } else {
                try {
                    player.appearance.setAnimations(Animation.create(args[1].toInt()))
                    player.appearance.sync()
                } catch (e: NumberFormatException) {
                    reject(player, "Syntax error: ::ranim <Render Animation ID>")
                }
            }
        }

        define(
            name = "resetanim",
            privilege = Privilege.ADMIN,
            usage = "::resetanim",
            description = "Resets the player's render (walk/idle) animation to default.",
        ) { player, _ ->
            player.appearance.prepareBodyData(player)
            player.appearance.setDefaultAnimations()
            player.appearance.setAnimations()
            player.appearance.sync()
        }
    }
}
