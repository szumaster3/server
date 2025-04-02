package core.game.node.entity.player.info;

import core.game.node.entity.player.Player;
import core.tools.StringUtils;

/**
 * The type Uid info.
 */
public class UIDInfo {

    private String ip;

    private String compName;

    private String mac;

    private String serial;

    /**
     * Instantiates a new Uid info.
     */
    public UIDInfo() {

    }

    /**
     * Instantiates a new Uid info.
     *
     * @param ip       the ip
     * @param compName the comp name
     * @param mac      the mac
     * @param serial   the serial
     */
    public UIDInfo(String ip, String compName, String mac, String serial) {
        this.ip = ip;
        this.compName = compName;
        this.mac = mac.replace(":", "-");
        this.serial = serial;
    }

    /**
     * Translate.
     *
     * @param other the other
     */
    public void translate(UIDInfo other) {
        ip = other.ip;
        compName = other.compName;
        mac = other.mac.replace(":", "-");
        serial = other.serial;
    }

    /**
     * To string string.
     *
     * @param player the player
     * @param target the target
     * @return the string
     */
    public String toString(Player player, Player target) {
        boolean admin = player.isAdmin();
        String format = toString();
        if (!admin) {// formats for non-admins
            String[] tokens = format.split("serial=");
            format = format.replace("serial=", "uid=").replace(tokens[tokens.length - 1], "*****");
        }
        player.sendMessage("[----------Info Debug----------]");
        String[] lines = StringUtils.splitIntoLine(format, 60);
        player.sendMessages(lines);
        player.sendMessage("[------------------------------]");
        return format;
    }

    /**
     * Gets comp name.
     *
     * @return the comp name
     */
    public String getCompName() {
        return compName;
    }

    /**
     * Gets ip.
     *
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * Gets mac.
     *
     * @return the mac
     */
    public String getMac() {
        return mac;
    }

    /**
     * Gets serial.
     *
     * @return the serial
     */
    public String getSerial() {
        return serial;
    }

    @Override
    public String toString() {
        // make sure serials always at end
        return "[ip=" + ip + ", compName=" + compName + ", mac=" + mac + ", serial=" + serial + "]";
    }

}
