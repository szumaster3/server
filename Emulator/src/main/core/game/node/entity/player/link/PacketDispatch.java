package core.game.node.entity.player.link;

import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.EntityFlag;
import core.game.world.update.flag.chunk.AnimateObjectUpdateFlag;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.net.packet.OutgoingContext;
import core.net.packet.PacketRepository;
import core.net.packet.context.*;
import core.net.packet.out.*;
import core.tools.Log;

import static core.api.ContentAPIKt.log;
import static core.api.ContentAPIKt.setVarp;

/**
 * The type Packet dispatch.
 */
public final class PacketDispatch {

    private final Player player;

    private final OutgoingContext.PlayerContext context;

    /**
     * Instantiates a new Packet dispatch.
     *
     * @param player the player
     */
    public PacketDispatch(Player player) {
        this.player = player;
        this.context = new OutgoingContext.PlayerContext(player);
    }

    /**
     * Send varp.
     *
     * @param index the index
     * @param value the value
     */
    public void sendVarp(int index, int value) {
        PacketRepository.send(Config.class, new OutgoingContext.Config(player, index, value));
    }

    /**
     * Send varc update.
     *
     * @param index the index
     * @param value the value
     */
    public void sendVarcUpdate(short index, int value) {
        PacketRepository.send(VarcUpdate.class, new OutgoingContext.VarcUpdate(player, index, value));
    }

    /**
     * Send message.
     *
     * @param message the message
     */
    public void sendMessage(String message) {
        if (message == null) {
            return;
        }
        if (player.getAttribute("chat_filter") != null && !message.contains("<col=CC6600>") && !message.contains("<col=FFFF00>")) {
            return;
        }
        if (message.length() > 255) {
            log(this.getClass(), Log.ERR, "Message length out of bounds (" + message + ")!");
            message = message.substring(0, 255);
        }
        PacketRepository.send(GameMessage.class, new OutgoingContext.GameMessage(player, message));
    }

    /**
     * Send messages.
     *
     * @param messages the messages
     */
    public void sendMessages(final String... messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }

    /**
     * Send message.
     *
     * @param message the message
     * @param ticks   the ticks
     */
    public void sendMessage(final String message, int ticks) {
        GameWorld.getPulser().submit(new Pulse(ticks, player) {
            @Override
            public boolean pulse() {
                sendMessage(message);
                return true;
            }
        });
    }

    /**
     * Send iface settings.
     *
     * @param settingsHash the settings hash
     * @param childId      the child id
     * @param interfaceId  the interface id
     * @param offset       the offset
     * @param length       the length
     */
    public void sendIfaceSettings(int settingsHash, int childId, int interfaceId, int offset, int length) {
        PacketRepository.send(AccessMask.class, new OutgoingContext.AccessMask(player, settingsHash, childId, interfaceId, offset, length));
    }
    /**
     * Send windows pane.
     *
     * @param windowId the window id
     * @param type     the type
     */
    public void sendWindowsPane(int windowId, int type) {
        PacketRepository.send(WindowsPane.class, new OutgoingContext.WindowsPane(player, windowId, type));
    }

    /**
     * Send system update.
     *
     * @param time the time
     */
    public void sendSystemUpdate(int time) {
        PacketRepository.send(SystemUpdatePacket.class, new OutgoingContext.SystemUpdate(player, time));
    }

    /**
     * Send music.
     *
     * @param musicId the music id
     */
    public void sendMusic(int musicId) {
        PacketRepository.send(MusicPacket.class, new OutgoingContext.Music(player, musicId, false));
    }

    /**
     * Send temp music.
     *
     * @param musicId the music id
     */
    public void sendTempMusic(int musicId) {
        PacketRepository.send(MusicPacket.class, new OutgoingContext.Music(player, musicId, true));
    }

    /**
     * Send script config.
     *
     * @param id         the id
     * @param value      the value
     * @param types      the types
     * @param parameters the parameters
     */
    public void sendScriptConfig(int id, int value, String types, java.lang.Object... parameters) {
        PacketRepository.send(CSConfigPacket.class, new OutgoingContext.CSConfig(player, id, value, types, parameters));
    }

    /**
     * Send run script.
     *
     * @param id      the id
     * @param string  the string
     * @param objects the objects
     */
    public void sendRunScript(int id, String string, java.lang.Object... objects) {
        PacketRepository.send(RunScriptPacket.class, new OutgoingContext.RunScript(player, id, string, objects));
    }

    /**
     * Send string.
     *
     * @param string      the string
     * @param interfaceId the interface id
     * @param lineId      the line id
     */
    public void sendString(String string, int interfaceId, int lineId) {
        PacketRepository.send(StringPacket.class, new OutgoingContext.StringContext(player, string, interfaceId, lineId));
    }

    /**
     * Send run energy.
     */
    public void sendRunEnergy() {
        PacketRepository.send(RunEnergy.class, getContext());
    }

    /**
     * Send logout.
     */
    public void sendLogout() {
        PacketRepository.send(Logout.class, getContext());
    }

    /**
     * Send animation interface.
     *
     * @param animationId the animation id
     * @param interfaceId the interface id
     * @param childId     the child id
     */
    public void sendAnimationInterface(int animationId, int interfaceId, int childId) {
        PacketRepository.send(AnimateInterface.class, new OutgoingContext.AnimateInterface(player, animationId, interfaceId, childId));
    }

    /**
     * Send player on interface.
     *
     * @param interfaceId the interface id
     * @param childId     the child id
     */
    public void sendPlayerOnInterface(int interfaceId, int childId) {
        PacketRepository.send(
                DisplayModel.class,
                new OutgoingContext.DisplayModel(
                        player,
                        OutgoingContext.DisplayModel.ModelType.PLAYER,
                        -1,
                        0,
                        interfaceId,
                        childId,
                        0
                )
        );
    }

    /**
     * Send npc on interface.
     *
     * @param npcId       the npc id
     * @param interfaceId the interface id
     * @param childId     the child id
     */
    public void sendNpcOnInterface(int npcId, int interfaceId, int childId) {
        PacketRepository.send(
                DisplayModel.class,
                new OutgoingContext.DisplayModel(
                        player,
                        OutgoingContext.DisplayModel.ModelType.NPC,
                        npcId,
                        0,
                        interfaceId,
                        childId,
                        0
                )
        );
    }

    /**
     * Send model on interface.
     *
     * @param modelID     the model id
     * @param interfaceId the interface id
     * @param childId     the child id
     * @param zoom        the zoom
     */
    public void sendModelOnInterface(int modelID, int interfaceId, int childId, int zoom) {
        PacketRepository.send(
                DisplayModel.class,
                new OutgoingContext.DisplayModel(
                        player,
                        OutgoingContext.DisplayModel.ModelType.MODEL,
                        modelID,
                        0,
                        interfaceId,
                        childId,
                        zoom
                )
        );
    }

    /**
     * Send angle on interface.
     *
     * @param interfaceId the interface id
     * @param childId     the child id
     * @param zoom        the zoom
     * @param pitch       the pitch
     * @param yaw         the yaw
     */
    public void sendAngleOnInterface(int interfaceId, int childId, int zoom, int pitch, int yaw) {
        PacketRepository.send(InterfaceSetAngle.class, new OutgoingContext.Default(player, pitch, zoom, yaw, interfaceId, childId));
    }

    /**
     * Send item on interface.
     *
     * @param itemId      the item id
     * @param amount      the amount
     * @param interfaceId the interface id
     * @param childId     the child id
     */
    public void sendItemOnInterface(int itemId, int amount, int interfaceId, int childId) {
        PacketRepository.send(
                DisplayModel.class,
                new OutgoingContext.DisplayModel(
                        player,
                        OutgoingContext.DisplayModel.ModelType.ITEM,
                        itemId,
                        amount,
                        interfaceId,
                        childId,
                        0
                )
        );
    }

    /**
     * Send item zoom on interface.
     *
     * @param itemId      the item id
     * @param zoom        the zoom
     * @param interfaceId the interface id
     * @param childId     the child id
     */
    public void sendItemZoomOnInterface(int itemId, int zoom, int interfaceId, int childId) {
        PacketRepository.send(DisplayModel.class, new OutgoingContext.DisplayModel(player, OutgoingContext.DisplayModel.ModelType.ITEM, itemId, zoom, interfaceId, childId, zoom));
    }

    /**
     * Send inter set items options script.
     *
     * @param interfaceId the interface id
     * @param componentId the component id
     * @param key         the key
     * @param width       the width
     * @param height      the height
     * @param options     the options
     */
    public void sendInterSetItemsOptionsScript(int interfaceId, int componentId, int key, int width, int height, String... options) {
        sendInterSetItemsOptionsScript(interfaceId, componentId, key, false, width, height, options);
    }

    /**
     * Send inter set items options script.
     *
     * @param interfaceId the interface id
     * @param componentId the component id
     * @param key         the key
     * @param negativeKey the negative key
     * @param width       the width
     * @param height      the height
     * @param options     the options
     */
    public void sendInterSetItemsOptionsScript(int interfaceId, int componentId, int key, boolean negativeKey, int width, int height, String... options) {
        java.lang.Object[] parameters = new java.lang.Object[6 + options.length];
        int index = 0;
        for (int count = options.length - 1; count >= 0; count--)
            parameters[index++] = options[count];
        parameters[index++] = -1;
        parameters[index++] = 0;
        parameters[index++] = height;
        parameters[index++] = width;
        parameters[index++] = key;
        parameters[index++] = interfaceId << 16 | componentId;
        sendRunScript(negativeKey ? 695 : 150, parameters);
    }

    /**
     * Send run script.
     *
     * @param scriptId the script id
     * @param params   the params
     */
    public void sendRunScript(int scriptId, java.lang.Object... params) {
        String parameterTypes = "";
        if (params != null) {
            for (int count = params.length - 1; count >= 0; count--) {
                if (params[count] instanceof String)
                    parameterTypes += "s"; // string
                else
                    parameterTypes += "i"; // integer
            }
        }
        sendRunScript(scriptId, parameterTypes, params);
    }

    /**
     * Send item zoom on interface.
     *
     * @param itemId      the item id
     * @param amount      the amount
     * @param zoom        the zoom
     * @param interfaceId the interface id
     * @param childId     the child id
     */
    public void sendItemZoomOnInterface(int itemId, int amount, int zoom, int interfaceId, int childId) {
        PacketRepository.send(DisplayModel.class, new OutgoingContext.DisplayModel(player, OutgoingContext.DisplayModel.ModelType.ITEM, itemId, amount, interfaceId, childId, zoom));
    }

    /**
     * Send interface config.
     *
     * @param interfaceId the interface id
     * @param childId     the child id
     * @param hide        the hide
     */
    public void sendInterfaceConfig(int interfaceId, int childId, boolean hide) {
        PacketRepository.send(InterfaceConfig.class, new OutgoingContext.InterfaceConfigContext(player, interfaceId, childId, hide));
    }

    /**
     * Send animation.
     *
     * @param id the id
     */
    public void sendAnimation(int id) {
        player.getUpdateMasks().register(EntityFlag.Animate, new Animation(id));
    }

    /**
     * Send animation.
     *
     * @param id    the id
     * @param delay the delay
     */
    public void sendAnimation(int id, int delay) {
        player.getUpdateMasks().register(EntityFlag.Animate, new Animation(id, delay));
    }

    /**
     * Send graphic.
     *
     * @param id the id
     */
    public void sendGraphic(int id) {
        player.getUpdateMasks().register(EntityFlag.SpotAnim, new Graphics(id));
    }

    /**
     * Send positioned graphic.
     *
     * @param id       the id
     * @param height   the height
     * @param delay    the delay
     * @param location the location
     */
    public void sendPositionedGraphic(int id, int height, int delay, Location location) {
        PacketRepository.send(PositionedGraphic.class, new OutgoingContext.PositionedGraphic(player, new Graphics(id, height, delay), location, 0, 0));
    }

    /**
     * Send global position graphic.
     *
     * @param id       the id
     * @param location the location
     */
    public void sendGlobalPositionGraphic(int id, Location location) {
        for (Player player : RegionManager.getLocalPlayers(location)) {
            player.getPacketDispatch().sendPositionedGraphic(id, 0, 0, location);
        }
    }

    /**
     * Send positioned graphics.
     *
     * @param graphics the graphics
     * @param location the location
     */
    public void sendPositionedGraphics(Graphics graphics, Location location) {
        PacketRepository.send(PositionedGraphic.class, new OutgoingContext.PositionedGraphic(player, graphics, location, 0, 0));
    }

    /**
     * Send scenery animation.
     *
     * @param scenery   the scenery
     * @param animation the animation
     */
    public void sendSceneryAnimation(Scenery scenery, Animation animation) {
        animation = new Animation(animation.getId(), animation.getDelay(), animation.getPriority());
        animation.setObject(scenery);
        RegionManager.getRegionChunk(scenery.getLocation()).flag(new AnimateObjectUpdateFlag(animation));
    }

    /**
     * Send scenery animation.
     *
     * @param scenery   the scenery
     * @param animation the animation
     * @param global    the global
     */
    public void sendSceneryAnimation(Scenery scenery, Animation animation, boolean global) {
        if (global) {
            sendSceneryAnimation(scenery, animation);
            return;
        }
        animation.setObject(scenery);
        PacketRepository.send(AnimateObjectPacket.class, new OutgoingContext.AnimateObject(player, animation));
    }

    /**
     * Send graphic.
     *
     * @param id     the id
     * @param height the height
     */
    public void sendGraphic(int id, int height) {
        player.getUpdateMasks().register(EntityFlag.SpotAnim, new Graphics(id, height));
    }

    /**
     * Send left shifted varbit.
     *
     * @param varpIndex the varp index
     * @param offset    the offset
     * @param value     the value
     */
    public void sendLeftShiftedVarbit(int varpIndex, int offset, int value) {
        setVarp(player, varpIndex, (value << offset));
    }

    /**
     * Send right shifted varbit.
     *
     * @param varpIndex the varp index
     * @param offset    the offset
     * @param value     the value
     */
    public void sendRightShiftedVarbit(int varpIndex, int offset, int value) {
        setVarp(player, varpIndex, (value >> offset));
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets context.
     *
     * @return the context
     */
    public OutgoingContext.PlayerContext getContext() {
        return context;
    }

    /**
     * Send script configs.
     *
     * @param id     the id
     * @param value  the value
     * @param type   the type
     * @param params the params
     */
    public void sendScriptConfigs(int id, int value, String type, java.lang.Object... params) {
        PacketRepository.send(CSConfigPacket.class, new OutgoingContext.CSConfig(player, id, value, type, params));
    }

    /**
     * Reset interface.
     *
     * @param id the id
     */
    public void resetInterface(int id) {
        PacketRepository.send(ResetInterface.class, new OutgoingContext.InterfaceContext(player, 0, 0, id, false));
    }

    /**
     * Send reposition on interface.
     *
     * @param id        the id
     * @param component the component
     * @param x         the x
     * @param y         the y
     */
    public void sendRepositionOnInterface(int id, int component, int x, int y) {
        PacketRepository.send(RepositionChild.class, new OutgoingContext.ChildPosition(player, id, component, x, y));
    }

}
