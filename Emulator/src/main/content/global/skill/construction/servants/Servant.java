package content.global.skill.construction.servants;

import core.game.node.entity.npc.NPC;
import core.game.node.item.Item;
import org.json.simple.JSONObject;

import java.nio.ByteBuffer;

/**
 * Represents a servant NPC that performs various actions related to construction tasks.
 * The servant has a type, an associated item, a number of uses, and a greeting status.
 */
public final class Servant extends NPC {

    private final ServantType type;
    private Item item;
    private int uses;
    private boolean greet;

    /**
     * Instantiates a new Servant with the given type.
     *
     * @param type the type of the servant (determines its NPC ID and behavior)
     */
    public Servant(ServantType type) {
        super(type.getId());
        this.type = type;
    }

    /**
     * Saves the servant's state to a byte buffer.
     * This method writes the servant's type, uses, item, and greet status to the buffer.
     *
     * @param buffer the byte buffer where the servant's state will be saved
     */
    public void save(ByteBuffer buffer) {
        buffer.put((byte) type.ordinal());
        buffer.putShort((byte) uses);
        if (item == null) {
            buffer.putShort((short) -1);
        } else {
            buffer.putShort((short) item.getId());
            buffer.putInt(item.getAmount());
        }
        buffer.put((byte) (greet ? 1 : 0));
    }

    /**
     * Parses a servant from a JSON object.
     * This method reads the servant's data from a JSON object and creates a `Servant` instance.
     *
     * @param data the JSON object containing the servant's data
     * @return a new `Servant` instance populated with the parsed data
     */
    public static Servant parse(JSONObject data) {
        int type = Integer.parseInt(data.get("type").toString());
        Servant servant = new Servant(ServantType.values()[type]);
        servant.uses = Integer.parseInt(data.get("uses").toString());
        Object itemRaw = data.get("item");
        if (itemRaw != null) {
            JSONObject item = (JSONObject) itemRaw;
            servant.item = new Item(Integer.parseInt(item.get("id").toString()), Integer.parseInt(item.get("amount").toString()));
        }
        servant.greet = (boolean) data.get("greet");
        return servant;
    }

    /**
     * Parses a servant from a byte buffer.
     * This method reads the servant's state from the buffer and creates a `Servant` instance.
     *
     * @param buffer the byte buffer containing the servant's data
     * @return a new `Servant` instance populated with the data from the buffer, or `null` if the type is -1
     */
    public static Servant parse(ByteBuffer buffer) {
        int type = buffer.get();
        if (type == -1) {
            return null;
        }
        Servant servant = new Servant(ServantType.values()[type]);
        servant.uses = buffer.getShort() & 0xFFFF;
        int itemId = buffer.getShort() & 0xFFFF;
        if ((short) itemId != -1) {
            servant.item = new Item(itemId, buffer.getInt());
        }
        servant.greet = buffer.get() == 1;
        return servant;
    }

    /**
     * Gets the item associated with the servant.
     *
     * @return the item the servant holds
     */
    public Item getItem() {
        return item;
    }

    /**
     * Sets the item for the servant.
     *
     * @param item the item to be set for the servant
     */
    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * Gets the number of uses remaining for the servant.
     *
     * @return the number of uses the servant has left
     */
    public int getUses() {
        return uses;
    }

    /**
     * Sets the number of uses for the servant.
     *
     * @param uses the number of uses to be set for the servant
     */
    public void setUses(int uses) {
        this.uses = uses;
    }

    /**
     * Checks if the servant has greeted the player.
     *
     * @return `true` if the servant has greeted the player, otherwise `false`
     */
    public boolean isGreet() {
        return greet;
    }

    /**
     * Sets the greeting status of the servant.
     *
     * @param greet the greeting status to be set for the servant
     */
    public void setGreet(boolean greet) {
        this.greet = greet;
    }

    /**
     * Gets the type of the servant.
     *
     * @return the `ServantType` associated with the servant
     */
    public ServantType getType() {
        return type;
    }
}
