package core.cache.def;

import core.game.node.Node;
import core.tools.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a general definition for various types of game nodes.
 *
 * @param <T> the type parameter for the node
 */
public class Definition<T extends Node> {

    /**
     * The Id of the definition.
     */
    protected int id;

    /**
     * The Name of the definition, defaulting to "null" if not set.
     */
    protected String name = "null";

    /**
     * A description or examine text for the definition.
     */
    protected String examine;

    /**
     * The options related to this definition.
     */
    protected String[] options;

    /**
     * A map of handlers associated with the definition, allowing dynamic configuration retrieval.
     */
    protected final Map<String, Object> handlers = new HashMap<>();

    /**
     * Default constructor for the Definition class.
     */
    public Definition() {
        // Empty.
    }

    /**
     * Checks if the definition has any valid options.
     *
     * @return true if the definition has valid options, false otherwise
     */
    public boolean hasOptions() {
        return hasOptions(true);
    }

    /**
     * Checks if the definition has any valid options.
     *
     * @param examine whether to check for the "Examine" option as well
     * @return true if the definition has valid options, false otherwise
     */
    public boolean hasOptions(boolean examine) {
        if (name.equals("null") || options == null) {
            return false;
        }
        for (String option : options) {
            if (option != null && !option.equals("null")) {
                if (examine || !option.equals("Examine")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Retrieves a configuration associated with the provided key.
     *
     * @param <V> the type of the configuration
     * @param key the key of the configuration to retrieve
     * @return the configuration associated with the key, or null if not found
     */
    @SuppressWarnings("unchecked")
    public <V> V getConfiguration(String key) {
        return (V) handlers.get(key);
    }

    /**
     * Retrieves a configuration associated with the provided key, with a fallback value.
     *
     * @param <V>  the type of the configuration
     * @param key  the key of the configuration to retrieve
     * @param fail the fallback value if the configuration is not found
     * @return the configuration associated with the key, or the fallback value if not found
     */
    @SuppressWarnings("unchecked")
    public <V> V getConfiguration(String key, V fail) {
        V object = (V) handlers.get(key);
        if (object == null) {
            return fail;
        }
        return object;
    }

    /**
     * Retrieves the ID of the definition.
     *
     * @return the ID of the definition
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the definition.
     *
     * @param id the new ID of the definition
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the name of the definition.
     *
     * @return the name of the definition
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the definition.
     *
     * @param name the new name of the definition
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the examine text for the definition.
     * If not already set, it attempts to retrieve it from the handlers map or generate a default one.
     *
     * @return the examine text
     */
    public String getExamine() {
        if (examine == null) {
            try {
                if (handlers.get("examine") != null) {
                    examine = handlers.get("examine").toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (examine == null) {
                if (name.length() > 0) {
                    examine = "It's a" + (StringUtils.isPlusN(name) ? "n " : " ") + name + ".";
                } else {
                    examine = "null";
                }
            }
        }
        return examine;
    }

    /**
     * Sets the examine text for the definition.
     *
     * @param examine the new examine text
     */
    public void setExamine(String examine) {
        this.examine = examine;
    }

    /**
     * Retrieves the options associated with the definition.
     *
     * @return the options associated with the definition
     */
    public String[] getOptions() {
        return options;
    }

    /**
     * Sets the options for the definition.
     *
     * @param options the new options for the definition
     */
    public void setOptions(String[] options) {
        this.options = options;
    }

    /**
     * Retrieves the handlers map associated with the definition.
     *
     * @return the handlers map
     */
    public Map<String, Object> getHandlers() {
        return handlers;
    }
}
