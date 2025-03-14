package core.api.utils

import core.game.node.entity.player.Player
import core.net.packet.PacketRepository
import core.net.packet.context.CameraContext
import core.net.packet.out.CameraViewPacket

class PlayerCamera(
    val player: Player?,
) {
    var ctx: CameraContext? = null

    /**
     * Positions the camera to the given region-local coordinates.
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @param height The height
     */
    fun setPosition(
        x: Int,
        y: Int,
        height: Int,
    ) {
        player ?: return
        ctx = CameraContext(player, CameraContext.CameraType.SET, x, y, height, 0, 0)
        PacketRepository.send(CameraViewPacket::class.java, ctx!!)
    }

    /**
     * Rotates the camera to face the given region-local coordinates.
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @param height The height
     * @param speed The rotation speed
     */
    fun rotateTo(
        x: Int,
        y: Int,
        height: Int,
        speed: Int,
    ) {
        player ?: return
        ctx = CameraContext(player, CameraContext.CameraType.ROTATION, x, y, height, speed, 1)
        PacketRepository.send(CameraViewPacket::class.java, ctx!!)
    }

    /**
     * Rotates the camera by given region-local coordinates.
     *
     * @param diffX The difference in x-coordinate
     * @param diffY The difference in y-coordinate
     * @param diffHeight The difference in height
     * @param speed The rotation speed
     */
    fun rotateBy(
        diffX: Int,
        diffY: Int,
        diffHeight: Int,
        speed: Int,
    ) {
        player ?: return
        ctx ?: return
        ctx =
            CameraContext(
                player,
                CameraContext.CameraType.ROTATION,
                ctx!!.x + diffX,
                ctx!!.y + diffY,
                ctx!!.height + diffHeight,
                speed,
                1,
            )
        PacketRepository.send(CameraViewPacket::class.java, ctx!!)
    }

    /**
     * Moves the camera to the given region-local coordinates.
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @param height The height
     * @param speed The movement speed
     */
    fun panTo(
        x: Int,
        y: Int,
        height: Int,
        speed: Int,
    ) {
        player ?: return
        ctx = CameraContext(player, CameraContext.CameraType.POSITION, x, y, height, speed, 1)
        PacketRepository.send(CameraViewPacket::class.java, ctx!!)
    }

    /**
     * Shakes the camera with the specified parameters.
     *
     * @param cameraType The camera type
     * @param jitter The jitter value
     * @param amplitude The amplitude value
     * @param frequency The frequency value
     * @param speed The shake speed
     */
    fun shake(
        cameraType: Int,
        jitter: Int,
        amplitude: Int,
        frequency: Int,
        speed: Int,
    ) {
        player ?: return
        ctx = CameraContext(player, CameraContext.CameraType.SHAKE, cameraType, jitter, amplitude, frequency, speed)
        PacketRepository.send(CameraViewPacket::class.java, ctx!!)
    }

    /**
     * Shakes the camera with the specified parameters.
     *
     * @param cameraType The [CameraShakeType]
     * @param jitter The jitter value
     * @param amplitude The amplitude value
     * @param frequency The frequency value
     * @param speed The shake speed
     */
    fun shake(
        cameraType: CameraShakeType,
        jitter: Int,
        amplitude: Int,
        frequency: Int,
        speed: Int,
    ) {
        val ctx =
            CameraContext(
                player,
                CameraContext.CameraType.SHAKE,
                cameraType.ordinal,
                jitter,
                amplitude,
                frequency,
                speed,
            )
        PacketRepository.send(CameraViewPacket::class.java, ctx)
    }

    /**
     * Resets the current camera position.
     */
    fun reset() {
        player ?: return
        ctx = CameraContext(player, CameraContext.CameraType.RESET, -1, -1, -1, -1, -1)
        PacketRepository.send(CameraViewPacket::class.java, ctx!!)
    }
}
