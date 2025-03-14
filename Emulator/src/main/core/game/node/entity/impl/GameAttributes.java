package core.game.node.entity.impl;

import core.ServerConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code GameAttributes} class is responsible for managing game-related attributes for entities.
 * It provides methods to set, get, remove, and parse attributes from XML files.
 * Additionally, it supports the handling of attributes that are flagged for saving.
 */
public final class GameAttributes {

    /**
     * A map that stores the attributes with their respective values.
     */
    private final Map<String, Object> attributes = new HashMap<>();

    /**
     * A list of attributes that should be saved.
     */
    private final List<String> savedAttributes = new ArrayList<>(250);

    /**
     * A map that tracks the expiration times of specific attributes.
     */
    public final HashMap<String, Long> keyExpirations = new HashMap<>(250);

    /**
     * Constructs a new {@code GameAttributes} instance.
     */
    public GameAttributes() {
    }

    /**
     * Dumps the current game attributes to a file.
     *
     * @param file The file to dump the attributes to.
     * @deprecated This method is no longer in use.
     */
    @Deprecated
    public void dump(String file) {
    }

    /**
     * Parses the game attributes from an XML file and loads them into the attributes map.
     *
     * @param file The name of the XML file to parse.
     * @deprecated This method is no longer in use.
     */
    @Deprecated
    public void parse(String file) {
        File saveFile = new File(ServerConstants.PLAYER_ATTRIBUTE_PATH + file);
        if (!saveFile.exists()) {
            return;
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(saveFile);

            NodeList attributesList = doc.getElementsByTagName("GameAttribute");
            for (int i = 0; i < attributesList.getLength(); i++) {
                Node attrNode = attributesList.item(i);
                if (attrNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element attr = (Element) attrNode;
                    String key = attr.getAttribute("key");
                    switch (attr.getAttribute("type")) {
                        case "bool": {
                            boolean value = Boolean.parseBoolean(attr.getAttribute("value"));
                            attributes.put(key, value);
                            if (!savedAttributes.contains(key)) {
                                savedAttributes.add(key);
                            }
                            break;
                        }
                        case "long": {
                            long value = Long.parseLong(attr.getAttribute("value"));
                            attributes.put(key, value);
                            if (!savedAttributes.contains(key)) {
                                savedAttributes.add(key);
                            }
                            break;
                        }
                        case "short": {
                            short value = Short.parseShort(attr.getAttribute("value"));
                            attributes.put(key, value);
                            if (!savedAttributes.contains(key)) {
                                savedAttributes.add(key);
                            }
                            break;
                        }
                        case "int": {
                            int value = Integer.parseInt(attr.getAttribute("value"));
                            attributes.put(key, value);
                            if (!savedAttributes.contains(key)) {
                                savedAttributes.add(key);
                            }
                            break;
                        }
                        case "byte": {
                            byte value = Byte.parseByte(attr.getAttribute("value"));
                            attributes.put(key, value);
                            if (!savedAttributes.contains(key)) {
                                savedAttributes.add(key);
                            }
                            break;
                        }
                        case "string": {
                            String value = attr.getAttribute("value");
                            attributes.put(key, value);
                            if (!savedAttributes.contains(key)) {
                                savedAttributes.add(key);
                            }
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets an attribute with the specified key and value.
     * If the key starts with "/save:", the attribute will be added to the saved attributes list.
     *
     * @param key   The key of the attribute.
     * @param value The value of the attribute.
     */
    public void setAttribute(String key, Object value) {
        if (key.startsWith("/save:")) {
            key = key.substring(6);
            if (!savedAttributes.contains(key)) {
                savedAttributes.add(key);
            }
        }
        attributes.put(key, value);
    }

    /**
     * Retrieves the value of an attribute by its key.
     *
     * @param <T> The type of the attribute value.
     * @param key The key of the attribute.
     * @return The value of the attribute, or null if the attribute does not exist.
     */
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key) {
        key = key.replace("/save:", "");
        return (T) attributes.get(key);
    }

    /**
     * Retrieves the value of an attribute by its key, returning a default value if the attribute does not exist.
     *
     * @param <T>    The type of the attribute value.
     * @param string The key of the attribute.
     * @param fail   The default value to return if the attribute does not exist.
     * @return The value of the attribute, or the default value if the attribute does not exist.
     */
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String string, T fail) {
        string = string.replace("/save:", "");
        Object object = attributes.get(string);
        if (object != null) {
            return (T) object;
        }
        return fail;
    }

    /**
     * Removes an attribute with the specified key.
     *
     * @param string The key of the attribute to remove.
     */
    public void removeAttribute(String string) {
        savedAttributes.remove(string);
        attributes.remove(string);
    }

    /**
     * Retrieves a map of all attributes.
     *
     * @return A map containing all the attributes.
     */
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * Retrieves a list of saved attributes.
     *
     * @return A list of keys of saved attributes.
     */
    public List<String> getSavedAttributes() {
        return savedAttributes;
    }
}
