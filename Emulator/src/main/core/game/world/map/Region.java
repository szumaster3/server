package core.game.world.map;

import core.cache.Cache;
import core.cache.CacheIndex;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.music.MusicZone;
import core.game.system.communication.CommunicationInfo;
import core.game.system.config.XteaParser;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.build.DynamicRegion;
import core.game.world.map.build.LandscapeParser;
import core.game.world.map.build.MapscapeParser;
import core.game.world.map.zone.RegionZone;
import core.game.world.repository.Repository;
import core.tools.Log;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static core.api.ContentAPIKt.log;

/**
 * The type Region.
 */
public class Region {

	/**
	 * The constant SIZE.
	 */
	public static final int SIZE = 64;

	private final int x;

	private final int y;

	private final RegionPlane[] planes = new RegionPlane[4];

	private final Pulse activityPulse;

	private final List<RegionZone> regionZones = new ArrayList<>(20);

	private int music = -1;

	private final List<MusicZone> musicZones = new ArrayList<>(20);

	private final HashMap<String,Long> tolerances = new HashMap<>();

	private boolean active;

	private int objectCount;

	private boolean hasFlags;

	private boolean loaded;

	private int viewAmount;

	private boolean build;

	private boolean updateAllPlanes;

	/**
	 * Instantiates a new Region.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public Region(int x, int y) {
		this.x = x;
		this.y = y;
		for (int plane = 0; plane < 4; plane++) {
			planes[plane] = new RegionPlane(this, plane);
		}
		this.activityPulse = new Pulse(50) {
			@Override
			public boolean pulse() {
				flagInactive();
				return true;
			}
		};
		activityPulse.stop();
	}

	/**
	 * Gets base location.
	 *
	 * @return the base location
	 */
	public Location getBaseLocation() {
		return Location.create(x << 6, y << 6, 0);
	}

	/**
	 * Add.
	 *
	 * @param zone the zone
	 */
	public void add(RegionZone zone) {
		regionZones.add(zone);
		for (RegionPlane plane : planes) {
			for (NPC npc : plane.getNpcs()) {
				npc.getZoneMonitor().updateLocation(npc.getLocation());
			}
			for (Player p : plane.getPlayers()) {
				p.getZoneMonitor().updateLocation(p.getLocation());
			}
		}
	}

	/**
	 * Remove.
	 *
	 * @param zone the zone
	 */
	public void remove(RegionZone zone) {
		regionZones.remove(zone);
		for (RegionPlane plane : planes) {
			for (NPC npc : plane.getNpcs()) {
				npc.getZoneMonitor().updateLocation(npc.getLocation());
			}
			for (Player p : plane.getPlayers()) {
				p.getZoneMonitor().updateLocation(p.getLocation());
			}
		}
	}

	/**
	 * Add.
	 *
	 * @param player the player
	 */
	public void add(Player player) {
		planes[player.getLocation().getZ()].add(player);
		tolerances.put(player.getUsername(), System.currentTimeMillis());
		flagActive();
	}

	/**
	 * Add.
	 *
	 * @param npc the npc
	 */
	public void add(NPC npc) {
		planes[npc.getLocation().getZ()].add(npc);
	}

	/**
	 * Remove.
	 *
	 * @param npc the npc
	 */
	public void remove(NPC npc) {
		RegionPlane plane = npc.getViewport().getCurrentPlane();
		if (plane != null && plane != planes[npc.getLocation().getZ()]) {
			plane.remove(npc);
		}
		planes[npc.getLocation().getZ()].remove(npc);
	}

	/**
	 * Remove.
	 *
	 * @param player the player
	 */
	public void remove(Player player) {
		player.getViewport().getCurrentPlane().remove(player);
		tolerances.remove(player.getUsername());
		checkInactive();
	}

	/**
	 * Is tolerated boolean.
	 *
	 * @param player the player
	 * @return the boolean
	 */
	public boolean isTolerated(Player player){
		return System.currentTimeMillis() - tolerances.getOrDefault(player.getUsername(), System.currentTimeMillis()) > TimeUnit.MINUTES.toMillis(10);
	}

	/**
	 * Check inactive boolean.
	 *
	 * @return the boolean
	 */
	public boolean checkInactive() {
		return isInactive(true);
	}

	/**
	 * Is inactive boolean.
	 *
	 * @param runPulse the run pulse
	 * @return the boolean
	 */
	public boolean isInactive(boolean runPulse) {
		if (isViewed()) {
			return false;
		}
		for (RegionPlane p : planes) {
			if (!p.getPlayers().isEmpty()) {
				return false;
			}
		}
		if (runPulse) {
			if (!activityPulse.isRunning()) {
				activityPulse.restart();
				activityPulse.start();
				GameWorld.getPulser().submit(activityPulse);
			}
		}
		return true;
	}

	/**
	 * Is pending removal boolean.
	 *
	 * @return the boolean
	 */
	public boolean isPendingRemoval() {
		return activityPulse.isRunning();
	}

	/**
	 * Flag active.
	 */
	public void flagActive() {
		activityPulse.stop();
		if (!active) {
			active = true;
			load(this);
			for (RegionPlane r : planes) {
				for (NPC n : r.getNpcs()) {
					if (n.isActive()) {
						Repository.addRenderableNPC(n);
					}
				}
			}
		}
	}

	/**
	 * Flag inactive boolean.
	 *
	 * @param force the force
	 * @return the boolean
	 */
	public boolean flagInactive(boolean force) {
		if (unload(this, force)) {
			active = false;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Flag inactive boolean.
	 *
	 * @return the boolean
	 */
	public boolean flagInactive() {
            return flagInactive(false);
	}

	/**
	 * Load.
	 *
	 * @param r the r
	 */
	public static void load(Region r) {
		load(r, r.build);
	}

	/**
	 * Load.
	 *
	 * @param r     the r
	 * @param build the build
	 */
	public static void load(Region r, boolean build) {
		try {
			if (r.isLoaded() && r.isBuild() == build) {
				return;
			}
			r.build = build;
			boolean dynamic = r instanceof DynamicRegion;
			int regionId = dynamic ? ((DynamicRegion) r).getRegionId() : r.getId();
			int regionX = regionId >> 8 & 0xFF;
			int regionY = regionId & 0xFF;
			int mapscapeId = Cache.getArchiveId(CacheIndex.LANDSCAPES, "m" + regionX + "_" + regionY);

			if (mapscapeId < 0 && !dynamic) {
				r.setLoaded(true);
				return;
			}

			byte[][][] mapscapeData = new byte[4][SIZE][SIZE];
			for (RegionPlane plane : r.planes) {
				plane.getFlags().setLandscape(new boolean[SIZE][SIZE]);
				//plane.getFlags().setClippingFlags(new int[SIZE][SIZE]);
				//plane.getProjectileFlags().setClippingFlags(new int[SIZE][SIZE]);
			}
			if (mapscapeId > -1) {
				ByteBuffer mapscape = ByteBuffer.wrap(Cache.getData(CacheIndex.LANDSCAPES, "m" + regionX + "_"+ regionY));
				MapscapeParser.parse(r, mapscapeData, mapscape);
			}
			r.hasFlags = dynamic;
			r.setLoaded(true);
			int landscapeId = Cache.getArchiveId(CacheIndex.LANDSCAPES, "l" + regionX + "_" + regionY);
			if (landscapeId > -1) {
				byte[] landscape = Cache.getData(CacheIndex.LANDSCAPES, "l" + regionX + "_"+ regionY, XteaParser.Companion.getRegionXTEA(regionId));
				if (landscape == null || landscape.length < 4) {
					return;
				}
				r.hasFlags = true;
				try {
					LandscapeParser.parse(r, mapscapeData, ByteBuffer.wrap(landscape), build);
				} catch (Throwable t) {
					new Throwable("Failed parsing region " + regionId + "!", t).printStackTrace();
				}
			}
			MapscapeParser.clipMapscape(r, mapscapeData);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}


	/**
	 * Unload boolean.
	 *
	 * @param r the r
	 * @return the boolean
	 */
	public static boolean unload(Region r) {
            return unload(r, false);
        }

	/**
	 * Unload boolean.
	 *
	 * @param r     the r
	 * @param force the force
	 * @return the boolean
	 */
	public static boolean unload(Region r, boolean force) {
		if (!force && r.isViewed()) {
			log(CommunicationInfo.class, Log.ERR, "Players viewing region!");
			r.flagActive();
			return false;
		}
		for (RegionPlane p : r.planes) {
			if (!force && !p.getPlayers().isEmpty()) {
				log(CommunicationInfo.class, Log.ERR, "Players still in region!");
				r.flagActive();
				return false;
			}
		}
		for (RegionPlane p : r.planes) {
			p.clear();
			if (!(r instanceof DynamicRegion)) {
				for (NPC n : p.getNpcs()) {
					n.onRegionInactivity();
				}
			}
		}
		if (r.isBuild())
			r.setLoaded(false);
		r.activityPulse.stop();
	        return true;
	}

	/**
	 * Is viewed boolean.
	 *
	 * @return the boolean
	 */
	public boolean isViewed() {
		synchronized (this) {
			return viewAmount > 0;
		}
	}

	/**
	 * Increment view amount int.
	 *
	 * @return the int
	 */
	public int incrementViewAmount() {
		synchronized (this) {
			return ++viewAmount;
		}
	}

	/**
	 * Decrement view amount int.
	 *
	 * @return the int
	 */
	public int decrementViewAmount() {
		synchronized (this) {
			if (viewAmount < 1) {
				//log(this.getClass(), Log.ERR,  "View amount is " + (viewAmount - 1));
				viewAmount++;
			}
			return --viewAmount;
		}
	}

	/**
	 * Is active boolean.
	 *
	 * @return the boolean
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets active.
	 *
	 * @param active the active
	 */
	@Deprecated
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Gets id.
	 *
	 * @return the id
	 */
	public int getId() {
		return x << 8 | y;
	}

	/**
	 * Gets region id.
	 *
	 * @return the region id
	 */
	public int getRegionId() {
		return getId();
	}

	/**
	 * Gets x.
	 *
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets y.
	 *
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Get planes region plane [ ].
	 *
	 * @return the region plane [ ]
	 */
	public RegionPlane[] getPlanes() {
		return planes;
	}

	/**
	 * Sets music.
	 *
	 * @param music the music
	 */
	public void setMusic(int music) {
		this.music = music;
	}

	/**
	 * Gets music.
	 *
	 * @return the music
	 */
	public int getMusic() {
		return this.music;
	}

	/**
	 * Gets region zones.
	 *
	 * @return the region zones
	 */
	public List<RegionZone> getRegionZones() {
		return regionZones;
	}

	/**
	 * Gets music zones.
	 *
	 * @return the music zones
	 */
	public List<MusicZone> getMusicZones() {
		return musicZones;
	}

	/**
	 * Gets object count.
	 *
	 * @return the object count
	 */
	public int getObjectCount() {
		return objectCount;
	}

	/**
	 * Sets object count.
	 *
	 * @param objectCount the object count
	 */
	public void setObjectCount(int objectCount) {
		this.objectCount = objectCount;
	}

	/**
	 * Is has flags boolean.
	 *
	 * @return the boolean
	 */
	public boolean isHasFlags() {
		return hasFlags;
	}

	/**
	 * Sets has flags.
	 *
	 * @param hasFlags the has flags
	 */
	public void setHasFlags(boolean hasFlags) {
		this.hasFlags = hasFlags;
	}

	/**
	 * Sets region time out.
	 *
	 * @param ticks the ticks
	 */
	public void setRegionTimeOut(int ticks) {
		activityPulse.setDelay(ticks);
	}

	/**
	 * Is loaded boolean.
	 *
	 * @return the boolean
	 */
	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * Sets loaded.
	 *
	 * @param loaded the loaded
	 */
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	/**
	 * Sets view amount.
	 *
	 * @param viewAmount the view amount
	 */
	public void setViewAmount(int viewAmount) {
		this.viewAmount = viewAmount;
	}

	/**
	 * Is build boolean.
	 *
	 * @return the boolean
	 */
	public boolean isBuild() {
		return build;
	}

	/**
	 * Sets build.
	 *
	 * @param build the build
	 */
	public void setBuild(boolean build) {
		this.build = build;
	}

	/**
	 * Is update all planes boolean.
	 *
	 * @return the boolean
	 */
	public boolean isUpdateAllPlanes() {
		return updateAllPlanes;
	}

	/**
	 * Sets update all planes.
	 *
	 * @param updateAllPlanes the update all planes
	 */
	public void setUpdateAllPlanes(boolean updateAllPlanes) {
		this.updateAllPlanes = updateAllPlanes;
	}
}
