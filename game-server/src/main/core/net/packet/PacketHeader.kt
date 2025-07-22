package core.net.packet

/**
 * Represents the types of packet headers.
 * @author Emperor
 */
enum class PacketHeader {
    /**
     * Normal packet header.
     */
    NORMAL,

    /**
     * Byte packet header.
     */
    BYTE,

    /**
     * Short packet header.
     */
    SHORT,
}
