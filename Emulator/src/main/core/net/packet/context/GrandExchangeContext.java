package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The type Grand exchange context.
 */
public class GrandExchangeContext implements Context {

    /**
     * The Idx.
     */
    public final byte idx;
    /**
     * The State.
     */
    public final byte state;
    /**
     * The Item id.
     */
    public final short itemID;
    /**
     * The Is sell.
     */
    public final boolean isSell;
    /**
     * The Value.
     */
    public final int value;
    /**
     * The Amt.
     */
    public final int amt;
    /**
     * The Completed amt.
     */
    public final int completedAmt;
    /**
     * The Total coins exchanged.
     */
    public final int totalCoinsExchanged;
    private final Player player;

    /**
     * Instantiates a new Grand exchange context.
     *
     * @param player              the player
     * @param idx                 the idx
     * @param state               the state
     * @param itemID              the item id
     * @param isSell              the is sell
     * @param value               the value
     * @param amt                 the amt
     * @param completedAmt        the completed amt
     * @param totalCoinsExchanged the total coins exchanged
     */
    public GrandExchangeContext(Player player, byte idx, byte state, short itemID, boolean isSell, int value, int amt, int completedAmt, int totalCoinsExchanged) {
        this.player = player;
        this.idx = idx;
        this.state = state;
        this.itemID = itemID;
        this.isSell = isSell;
        this.value = value;
        this.amt = amt;
        this.completedAmt = completedAmt;
        this.totalCoinsExchanged = totalCoinsExchanged;
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
