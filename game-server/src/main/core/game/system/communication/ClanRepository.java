package core.game.system.communication;

import core.ServerConstants;
import core.game.activity.ActivityPlugin;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.PlayerDetails;
import core.game.node.entity.player.info.PlayerMonitor;
import core.game.node.entity.player.info.Rights;
import core.game.world.GameWorld;
import core.game.world.repository.Repository;
import core.net.amsc.WorldCommunicator;
import core.net.packet.OutgoingContext;
import core.net.packet.PacketRepository;
import core.net.packet.out.CommunicationMessage;
import core.net.packet.out.UpdateClanChat;
import core.worker.ManagementEvents;
import proto.management.ClanJoinNotification;
import proto.management.ClanLeaveNotification;

import java.util.*;

/**
 * The type Clan repository.
 */
public final class ClanRepository {
    private static final int MAX_MEMBERS = 100;
    private static final Map<String, ClanRepository> CLAN_REPOSITORY = new HashMap<>();
    private final String owner;
    private String name = "Chat disabled";
    private ClanRank joinRequirement = ClanRank.ANY_FRIEND;
    private ClanRank messageRequirement = ClanRank.ANYONE;
    private ClanRank kickRequirement = ClanRank.ONLY_ME;
    private ClanRank lootRequirement = ClanRank.NO_ONE;
    private final Map<String, ClanRank> ranks = new HashMap<>();
    private final Map<String, Long> banned = new HashMap<>();
    private List<ClanEntry> players = new ArrayList<>(MAX_MEMBERS);
    private ActivityPlugin clanWar;

    /**
     * Instantiates a new Clan repository.
     *
     * @param owner the owner
     */
    public ClanRepository(String owner) {
        this.owner = owner;
    }

    /**
     * Enter boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public boolean enter(Player player) {
        if (!owner.equals(ServerConstants.SERVER_NAME.toLowerCase()) && players.size() >= MAX_MEMBERS) {
            player.getPacketDispatch().sendMessage("The channel you tried to join is full.:clan:");
            return false;
        }
        if (!player.getName().equals(owner) && player.getDetails().getRights() != Rights.ADMINISTRATOR) {
            if (isBanned(player.getName())) {
                player.getPacketDispatch().sendMessage("You are temporarily banned from this clan channel.:clan:");
                return false;
            }
            Player o = Repository.getPlayerByName(owner);
            if (o != null) {
                if (o.getCommunication().getBlocked().contains(player.getName())) {
                    player.getPacketDispatch().sendMessage("You do not have a high enough rank to join this clan channel.:clan:");
                    return false;
                }
            }
            ClanRank rank = getRank(player);
            if (rank.ordinal() < joinRequirement.ordinal()) {
                player.getPacketDispatch().sendMessage("You do not have a high enough rank to join this clan channel.:clan:");
                return false;
            }
        }
        ClanEntry entry = new ClanEntry(player);
        if (!players.contains(entry)) {
            players.add(entry);
        }

        ClanJoinNotification.Builder event = ClanJoinNotification.newBuilder();
        event.setUsername(player.getName());
        event.setWorld(GameWorld.getSettings().getWorldId());
        event.setClanName(owner);
        ManagementEvents.publish(event.build());

        player.getPacketDispatch().sendMessage("Now talking in clan channel " + name + ":clan:");
        player.getPacketDispatch().sendMessage("To talk, start each line of chat with the / symbol.:clan:");
        update();
        return true;
    }

    /**
     * Clean.
     *
     * @param disable the disable
     */
    public void clean(boolean disable) {
        if (WorldCommunicator.isEnabled()) {
            return;
        }
        for (Iterator<ClanEntry> it = players.iterator(); it.hasNext(); ) {
            ClanEntry entry = it.next();
            Player player = entry.getPlayer();
            boolean remove = disable;
            if (!remove) {
                remove = getRank(player).ordinal() < joinRequirement.ordinal();
            }
            if (remove) {
                leave(player, false);
                player.getCommunication().setClan(null);
                it.remove();
            }
        }
        if (players.isEmpty()) {
            banned.clear();
        }
        update();
    }

    /**
     * Is banned boolean.
     *
     * @param name the name
     * @return the boolean
     */
    public boolean isBanned(String name) {
        if (banned.containsKey(name)) {
            long time = banned.get(name);
            if (time > System.currentTimeMillis()) {
                return true;
            }
            banned.remove(name);
        }
        return false;
    }

    /**
     * Message.
     *
     * @param player  the player
     * @param message the message
     */
    public void message(Player player, String message) {
        if (player.getLocks().isLocked("cc_message") || isBanned(player.getName())) {
            return;
        }
        player.getLocks().lock("cc_message", 1);
        if (!player.getName().equals(owner) && player.getDetails().getRights() != Rights.ADMINISTRATOR) {
            ClanRank rank = getRank(player);
            if (rank.ordinal() < messageRequirement.ordinal()) {
                player.getPacketDispatch().sendMessage("You do not have a high enough rank to talk in this clan channel.:clan:");
                return;
            }
        }
        PlayerMonitor.logChat(player, "clan", message);
        for (Iterator<ClanEntry> it = players.iterator(); it.hasNext(); ) {
            ClanEntry entry = it.next();
            Player p = entry.getPlayer();
            if (p != null) {
                PacketRepository.send(
                        CommunicationMessage.class,
                        new OutgoingContext.MessageContext(
                                p,
                                player.getName(),
                                Rights.getChatIcon(player),
                                OutgoingContext.MessageContext.CLAN_MESSAGE,
                                message
                        )
                );
            }
        }
    }

    /**
     * Kick.
     *
     * @param player the player
     * @param target the target
     */
    public void kick(Player player, Player target) {
        ClanRank rank = getRank(player);
        if (target.getDetails().getRights() == Rights.ADMINISTRATOR) {
            player.sendMessage("You can't kick an administrator.:clan:");
            return;
        }
        if (target.getName().equals(player.getName())) {
            player.sendMessage("You can't kick yourself.:clan:");
            return;
        }
        if (player.getDetails().getRights() != Rights.ADMINISTRATOR && rank.ordinal() < kickRequirement.ordinal()) {
            player.getPacketDispatch().sendMessage("You do not have a high enough rank to kick in this clan channel.:clan:");
            return;
        }
        if (target.getName().equals(owner)) {
            player.getPacketDispatch().sendMessage("You can't kick the owner of this clan channel.:clan:");
            return;
        }
        for (ClanEntry e : players) {
            PacketRepository.send(
                    CommunicationMessage.class,
                    new OutgoingContext.MessageContext(
                            e.getPlayer(),
                            player.getName(),
                            Rights.getChatIcon(player),
                            OutgoingContext.MessageContext.CLAN_MESSAGE,
                            "[Attempting to kick/ban " + target.getUsername() + " from this Clan Chat.]"
                    )
            );
        }
        leave(target, true, "You have been kicked from the channel.:clan:");
        target.getCommunication().setClan(null);
        banned.put(target.getName(), System.currentTimeMillis() + (3_600_000));
    }

    /**
     * Leave.
     *
     * @param player the player
     * @param remove the remove
     */
    public void leave(Player player, boolean remove) {
        leave(player, remove, "You have left the channel.:clan:");
    }

    /**
     * Leave.
     *
     * @param player  the player
     * @param remove  the remove
     * @param message the message
     */
    public void leave(Player player, boolean remove, String message) {
        if (remove) {
            players.remove(new ClanEntry(player));
            update();
            if (players.size() < 1) {
                banned.clear();
            }
        }
        PacketRepository.send(UpdateClanChat.class, new OutgoingContext.Clan(player, this, true));
        player.getPacketDispatch().sendMessage(message);
        if (clanWar != null && !isDefault()) {
            clanWar.fireEvent("leavefc", player);
        }

        ClanLeaveNotification.Builder event = ClanLeaveNotification.newBuilder();
        event.setClanName(owner);
        event.setUsername(player.getName());
        event.setWorld(GameWorld.getSettings().getWorldId());
        ManagementEvents.publish(event.build());
    }

    /**
     * Rank.
     *
     * @param name the name
     * @param rank the rank
     */
    public void rank(String name, ClanRank rank) {
        boolean update;
        if (rank == ClanRank.ANYONE) {
            update = ranks.remove(name) != null;
        } else {
            update = ranks.put(name, rank) != rank;
        }
        if (update) {
            clean(false);
        }
    }

    /**
     * Update.
     */
    public void update() {
        for (Iterator<ClanEntry> it = players.iterator(); it.hasNext(); ) {
            ClanEntry e = it.next();
            if (e.getWorldId() == GameWorld.getSettings().getWorldId() && e.getPlayer() != null) {
                PacketRepository.send(UpdateClanChat.class, new OutgoingContext.Clan(e.getPlayer(), this, false));
            }
        }
    }

    /**
     * Gets rank.
     *
     * @param entry the entry
     * @return the rank
     */
    public ClanRank getRank(ClanEntry entry) {
        if (entry.getPlayer() != null) {
            return getRank(entry.getPlayer());
        }
        ClanRank rank = ranks.get(entry.getName());
        if (rank == null) {
            return ClanRank.ANYONE;
        }
        return rank;
    }

    /**
     * Gets rank.
     *
     * @param player the player
     * @return the rank
     */
    public ClanRank getRank(Player player) {
        ClanRank rank = ranks.get(player.getName());
        if (player.getDetails().getRights() == Rights.ADMINISTRATOR && !player.getName().equals(owner)) {
            return ClanRank.NO_ONE;
        }
        if (rank == null) {
            if (player.getName().equals(owner)) {
                return ClanRank.ONLY_ME;
            }
            return ClanRank.ANYONE;
        }
        return rank;
    }

    /**
     * Get clan repository.
     *
     * @param owner the owner
     * @return the clan repository
     */
    public static ClanRepository get(String owner) {
        return get(owner, false);
    }

    /**
     * Get clan repository.
     *
     * @param owner  the owner
     * @param create the create
     * @return the clan repository
     */
    public static ClanRepository get(String owner, boolean create) {
        ClanRepository clan = CLAN_REPOSITORY.get(owner);
        if (clan != null) {
            return clan;
        }
        Player player = Repository.getPlayerByName(owner);
        PlayerDetails details = player != null ? player.getDetails() : null;
        if (details == null) {
            details = PlayerDetails.getDetails(owner);
            if (details == null) {
                return null;
            }
        }
        String name = details.getCommunication().getClanName();
        if (name.length() < 1) {
            if (!create) {
                return null;
            }
            name = "Chat disabled";
        }
        CLAN_REPOSITORY.put(owner, clan = new ClanRepository(owner));
        for (Contact c : details.getCommunication().getContacts().values()) {
            clan.ranks.put(c.username, c.rank);
        }
        clan.name = name;
        clan.joinRequirement = details.getCommunication().getJoinRequirement();
        clan.messageRequirement = details.getCommunication().getMessageRequirement();
        clan.kickRequirement = details.getCommunication().getKickRequirement();
        clan.lootRequirement = details.getCommunication().getLootRequirement();
        return clan;
    }

    /**
     * Is default boolean.
     *
     * @return the boolean
     */
    public boolean isDefault() {
        return owner.equals(GameWorld.getSettings().getName().toLowerCase());
    }

    /**
     * Gets default.
     *
     * @return the default
     */
    public static ClanRepository getDefault() {
        return get(GameWorld.getSettings().getName().toLowerCase());
    }

    /**
     * Delete.
     */
    public void delete() {
        CLAN_REPOSITORY.remove(owner);
        clean(true);
    }

    /**
     * Gets clans.
     *
     * @return the clans
     */
    public static Map<String, ClanRepository> getClans() {
        return CLAN_REPOSITORY;
    }

    /**
     * Gets players.
     *
     * @return the players
     */
    public List<ClanEntry> getPlayers() {
        return players;
    }

    /**
     * Gets join requirement.
     *
     * @return the join requirement
     */
    public ClanRank getJoinRequirement() {
        return joinRequirement;
    }

    /**
     * Sets join requirement.
     *
     * @param joinRequirement the join requirement
     */
    public void setJoinRequirement(ClanRank joinRequirement) {
        this.joinRequirement = joinRequirement;
        clean(false);
    }

    /**
     * Gets message requirement.
     *
     * @return the message requirement
     */
    public ClanRank getMessageRequirement() {
        return messageRequirement;
    }

    /**
     * Sets message requirement.
     *
     * @param messageRequirement the message requirement
     */
    public void setMessageRequirement(ClanRank messageRequirement) {
        this.messageRequirement = messageRequirement;
    }

    /**
     * Gets kick requirement.
     *
     * @return the kick requirement
     */
    public ClanRank getKickRequirement() {
        return kickRequirement;
    }

    /**
     * Sets kick requirement.
     *
     * @param kickRequirement the kick requirement
     */
    public void setKickRequirement(ClanRank kickRequirement) {
        this.kickRequirement = kickRequirement;
        update();
    }

    /**
     * Gets loot requirement.
     *
     * @return the loot requirement
     */
    public ClanRank getLootRequirement() {
        return lootRequirement;
    }

    /**
     * Gets banned.
     *
     * @return the banned
     */
    public Map<String, Long> getBanned() {
        return banned;
    }

    /**
     * Sets loot requirement.
     *
     * @param lootRequirement the loot requirement
     */
    public void setLootRequirement(ClanRank lootRequirement) {
        this.lootRequirement = lootRequirement;
    }

    /**
     * Gets owner.
     *
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets ranks.
     *
     * @return the ranks
     */
    public Map<String, ClanRank> getRanks() {
        return ranks;
    }

    /**
     * Gets clan war.
     *
     * @return the clan war
     */
    public ActivityPlugin getClanWar() {
        return clanWar;
    }

    /**
     * Sets clan war.
     *
     * @param clanWar the clan war
     */
    public void setClanWar(ActivityPlugin clanWar) {
        this.clanWar = clanWar;
    }

}