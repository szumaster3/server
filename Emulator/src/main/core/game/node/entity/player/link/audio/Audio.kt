package core.game.node.entity.player.link.audio

class Audio
    @JvmOverloads
    constructor(
        @JvmField val id: Int,
        @JvmField val delay: Int = 0,
        @JvmField val loops: Int = 1,
        @JvmField val radius: Int = defaultAudioRadius,
    ) {
        companion object {
            const val defaultAudioRadius = 8
        }
    }
