package core.game.system.command.sets

import core.api.playAudio
import core.api.playGlobalAudio
import core.game.node.entity.player.link.music.MusicEntry
import core.game.system.command.Privilege
import core.game.world.map.Location
import core.game.world.repository.Repository
import core.net.packet.PacketRepository
import core.net.packet.context.MusicContext
import core.net.packet.out.MusicPacket
import core.plugin.Initializable

@Initializable
class AudioCommandSet : CommandSet(Privilege.ADMIN) {
    override fun defineCommands() {
        define(
            name = "playsong",
            privilege = Privilege.ADMIN,
            usage = "::playsong <lt>Song ID<gt>",
            description = "Plays the song with the given ID.",
        ) { player, args ->
            if (args.size < 2) {
                reject(player, "Usage: ::playsong songID")
            }
            val id = args[1].toIntOrNull()
            if (id == null) {
                reject(player, "Please use a valid integer for the song id.")
            }
            player.musicPlayer.play(MusicEntry.forId(id!!))
            notify(player, "Now playing song $id")
        }

        define(
            name = "playjingle",
            privilege = Privilege.ADMIN,
            usage = "::playjingle <lt>jingle ID<gt>",
            description = "Plays the jingle with the given ID.",
        ) { player, args ->
            if (args.size < 2) {
                reject(player, "Usage: ::playjingle jingleID")
            }
            val id = args[1].toIntOrNull()
            if (id == null) {
                reject(player, "Please use a valid integer for the jingle id.")
            }
            PacketRepository.send(MusicPacket::class.java, MusicContext(player, id!!, true))
            notify(player, "Now playing jingle $id")
        }

        define(name = "playid", Privilege.ADMIN) { player, arg ->
            if (arg.size < 2) reject(player, "Needs more args.")
            val id = arg[1].toIntOrNull()
            if (id != null) {
                PacketRepository.send(MusicPacket::class.java, MusicContext(player, id))
                notify(player, "Now playing song $id")
            }
        }

        define(
            name = "allmusic",
            privilege = Privilege.ADMIN,
            usage = "::allmusic",
            description = "Unlocks all music tracks.",
        ) { player, _ ->
            for (me in MusicEntry.getSongs().values) {
                player.musicPlayer.unlock(me.id)
            }
        }

        define(
            name = "audio",
            privilege = Privilege.ADMIN,
            usage = "::audio id <lt>loops[optional]</lt>",
            description = "Plays the audio with the given ID.",
        ) { player, args ->
            if (args.size < 2 || args.size > 3) reject(player, "Usage: ::audio id loops[optional]")
            val id = args[1].toInt()
            val loops = if (args.size > 2) args[2].toInt() else 1
            playAudio(player, id, 0, loops)
        }

        define(
            name = "globalaudio",
            privilege = Privilege.ADMIN,
            usage = "::globalaudio id radius[max 15] location[player name or x y z]",
            description = "Play global audio by id, radius, and location",
        ) { player, args ->
            if (args.size < 3) reject(player, "Usage: ::globalaudio id radius[max 15] location[player name or x y z]")
            val loc =
                when (args.size) {
                    6 -> Location(args[3].toInt(), args[4].toInt(), args[5].toInt())
                    4 -> Repository.getPlayerByName(args[3])?.location
                    else -> null
                }
            if (loc == null) reject(player, "Invalid player name / location")
            val id = args[1].toInt()
            val radius = if (args[2].toInt() > 15) 15 else args[2].toInt()
            if (loc != null) playGlobalAudio(loc, id, 0, 1, radius)
        }
    }
}
