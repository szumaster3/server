package content.region.misthalin.handlers.lumbridge;

import core.game.activity.ActivityPlugin;
import core.game.activity.CutscenePlugin;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.map.build.DynamicRegion;
import core.game.world.update.flag.context.Animation;
import core.net.packet.PacketRepository;
import core.net.packet.context.CameraContext;
import core.net.packet.context.CameraContext.CameraType;
import core.net.packet.context.MusicContext;
import core.net.packet.out.CameraViewPacket;
import core.net.packet.out.MusicPacket;
import core.plugin.Initializable;

/**
 * The type Church organ cutscene.
 */
@Initializable
public final class ChurchOrganCutscene extends CutscenePlugin {

    /**
     * Instantiates a new Church organ cutscene.
     */
    public ChurchOrganCutscene() {
        super("organ cutscene");
    }

    /**
     * Instantiates a new Church organ cutscene.
     *
     * @param player the player
     */
    public ChurchOrganCutscene(final Player player) {
        super("organ cutscene");
        this.player = player;
    }

    @Override
    public void open() {
        final Scenery orgin = RegionManager.getObject(base.transform(43, 14, 0));
        final Scenery newOrgin = new Scenery(36979, base.transform(42, 14, 0));
        SceneryBuilder.remove(orgin);
        SceneryBuilder.add(newOrgin);
        PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.POSITION, player.getLocation().getX() + 2, player.getLocation().getY() - 7, 405, 1, 100));
        PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.ROTATION, player.getLocation().getX() + 1, player.getLocation().getY(), 405, 1, 100));
        player.lock();
        GameWorld.getPulser().submit(new Pulse(3) {
            @Override
            public boolean pulse() {
                player.getPacketDispatch().sendSceneryAnimation(RegionManager.getObject(base.transform(42, 14, 0)), new Animation(9982));
                PacketRepository.send(MusicPacket.class, new MusicContext(player, 147, true));
                PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.POSITION, player.getLocation().getX() + 2, player.getLocation().getY() - 3, 400, 1, 1));
                PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.ROTATION, player.getLocation().getX() + 1, player.getLocation().getY(), 400, 1, 1));
                return true;
            }
        });
        GameWorld.getPulser().submit(new Pulse(30) {
            @Override
            public boolean pulse() {
                unpause();
                return true;
            }
        });
    }

    @Override
    public ActivityPlugin newInstance(Player p) throws Throwable {
        return new ChurchOrganCutscene(p);
    }

    @Override
    public Location getSpawnLocation() {
        return null;
    }

    @Override
    public Location getStartLocation() {
        return base.transform(42, 14, 0);
    }

    @Override
    public void configure() {
        region = DynamicRegion.create(12850);
        setRegionBase();
        registerRegion(region.getId());
    }

}
