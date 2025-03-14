package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.Rights;
import core.net.packet.Context;

/**
 * The type Message context.
 */
public final class MessageContext implements Context {

    /**
     * The constant SEND_MESSAGE.
     */
    public static final int SEND_MESSAGE = 71;

    /**
     * The constant RECEIVE_MESSAGE.
     */
    public static final int RECEIVE_MESSAGE = 0;

    /**
     * The constant CLAN_MESSAGE.
     */
    public static final int CLAN_MESSAGE = 54;

    private final Player player;

    private final String other;

    private final int chatIcon;

    private final int opcode;

    private final String message;

    /**
     * Instantiates a new Message context.
     *
     * @param player  the player
     * @param other   the other
     * @param opcode  the opcode
     * @param message the message
     */
    public MessageContext(Player player, Player other, int opcode, String message) {
        this.player = player;
        this.other = other.getName();
        this.chatIcon = Rights.getChatIcon(other);
        this.opcode = opcode;
        this.message = message;
    }

    /**
     * Instantiates a new Message context.
     *
     * @param player   the player
     * @param other    the other
     * @param chatIcon the chat icon
     * @param opcode   the opcode
     * @param message  the message
     */
    public MessageContext(Player player, String other, int chatIcon, int opcode, String message) {
        this.player = player;
        this.other = other;
        this.chatIcon = chatIcon;
        this.opcode = opcode;
        this.message = message;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets other.
     *
     * @return the other
     */
    public String getOther() {
        return other;
    }

    /**
     * Gets opcode.
     *
     * @return the opcode
     */
    public int getOpcode() {
        return opcode;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets chat icon.
     *
     * @return the chat icon
     */
    public int getChatIcon() {
        return chatIcon;
    }
}