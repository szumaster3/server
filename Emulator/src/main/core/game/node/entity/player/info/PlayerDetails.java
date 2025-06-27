package core.game.node.entity.player.info;

import core.auth.UserAccountInfo;
import core.game.system.communication.CommunicationInfo;
import core.game.world.GameWorld;
import core.net.IoSession;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.TimeUnit;

/**
 * The type Player details.
 */
public class PlayerDetails {

    /**
     * The Account info.
     */
    public UserAccountInfo accountInfo = UserAccountInfo.createDefault();

    private final CommunicationInfo communicationInfo = new CommunicationInfo();

    /**
     * Gets credits.
     *
     * @return the credits
     */
    public int getCredits() {
        return accountInfo.getCredits();
    }

    /**
     * Sets credits.
     *
     * @param amount the amount
     */
    public void setCredits(int amount) {
        accountInfo.setCredits(amount);
    }

    private final UIDInfo info = new UIDInfo();

    private IoSession session;

    /**
     * The Save parsed.
     */
    public boolean saveParsed = false;

    /**
     * Instantiates a new Player details.
     *
     * @param username the username
     */
    public PlayerDetails(String username) {
        accountInfo.setUsername(username);
    }

    /**
     * Is banned boolean.
     *
     * @return the boolean
     */
    public boolean isBanned() {
        return accountInfo.getBanEndTime() > System.currentTimeMillis();
    }

    /**
     * Is perm mute boolean.
     *
     * @return the boolean
     */
    public boolean isPermMute() {
        return TimeUnit.MILLISECONDS.toDays(accountInfo.getMuteEndTime() - System.currentTimeMillis()) > 1000;
    }

    /**
     * Is muted boolean.
     *
     * @return the boolean
     */
    public boolean isMuted() {
        return accountInfo.getMuteEndTime() > System.currentTimeMillis();
    }

    /**
     * Gets rights.
     *
     * @return the rights
     */
    public Rights getRights() {
        return Rights.values()[accountInfo.getRights()];
    }

    /**
     * Sets rights.
     *
     * @param rights the rights
     */
    public void setRights(Rights rights) {
        this.accountInfo.setRights(rights.ordinal());
    }

    /**
     * Gets session.
     *
     * @return the session
     */
    public IoSession getSession() {
        return session;
    }

    /**
     * Sets session.
     *
     * @param session the session
     */
    public void setSession(IoSession session) {
        this.session = session;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(final String password) {
        this.accountInfo.setPassword(password);
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return this.accountInfo.getUsername();
    }

    /**
     * Gets uid.
     *
     * @return the uid
     */
    public int getUid() {
        return accountInfo.getUid();
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return this.accountInfo.getPassword();
    }

    /**
     * Gets mac address.
     *
     * @return the mac address
     */
    public String getMacAddress() {
        return info.getMac();
    }

    /**
     * Gets comp name.
     *
     * @return the comp name
     */
    public String getCompName() {
        return info.getCompName();
    }

    /**
     * Gets ip address.
     *
     * @return the ip address
     */
    public String getIpAddress() {
        if (session == null) {
            return info.getIp();
        }
        return session.getAddress();
    }

    /**
     * Gets serial.
     *
     * @return the serial
     */
    public String getSerial() {
        return info.getSerial();
    }

    /**
     * Gets info.
     *
     * @return the info
     */
    public UIDInfo getInfo() {
        return info;
    }

    /**
     * Gets communication.
     *
     * @return the communication
     */
    public CommunicationInfo getCommunication() {
        return communicationInfo;
    }

    /**
     * Gets last login.
     *
     * @return the last login
     */
    public long getLastLogin() {
        return this.accountInfo.getLastLogin();
    }

    /**
     * Sets last login.
     *
     * @param lastLogin the last login
     */
    public void setLastLogin(long lastLogin) {
        this.accountInfo.setLastLogin(lastLogin);
    }

    /**
     * Gets time played.
     *
     * @return the time played
     */
    public long getTimePlayed() {
        return this.accountInfo.getTimePlayed();
    }

    /**
     * Sets time played.
     *
     * @param timePlayed the time played
     */
    public void setTimePlayed(long timePlayed) {
        this.accountInfo.setTimePlayed(timePlayed);
    }

    /**
     * Sets mute time.
     *
     * @param muteTime the mute time
     */
    public void setMuteTime(long muteTime) {
        this.accountInfo.setMuteEndTime(muteTime);
    }

    /**
     * Gets mute time.
     *
     * @return the mute time
     */
    public long getMuteTime() {
        return this.accountInfo.getMuteEndTime();
    }

    /**
     * Gets ban time.
     *
     * @return the ban time
     */
    public long getBanTime() {
        return this.accountInfo.getBanEndTime();
    }

    /**
     * Sets ban time.
     *
     * @param banTime the ban time
     */
    public void setBanTime(long banTime) {
        this.accountInfo.setBanEndTime(banTime);
    }

    /**
     * Save.
     */
    public void save() {
        if (!saveParsed) return;
        if (isBanned()) return;
        try {
            accountInfo.setContacts(communicationInfo.getContactString());
            accountInfo.setBlocked(communicationInfo.getBlockedString());
            accountInfo.setClanName(communicationInfo.getClanName());
            accountInfo.setClanReqs(communicationInfo.getClanReqString());
            accountInfo.setCurrentClan(communicationInfo.getCurrentClan());
            GameWorld.getAccountStorage().update(accountInfo);
        } catch (IllegalStateException ignored) {
        }
    }

    /**
     * Gets details.
     *
     * @param username the username
     * @return the details
     */
    public static PlayerDetails getDetails(@NotNull String username) {
        return new PlayerDetails(username);
    }
}
