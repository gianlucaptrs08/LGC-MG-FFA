package eu.lotusgaming.mg.ffa.api;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

public class MapAPI {

	static File configFile = new File("plugins/LotusFFA/config.yml");
	static File spawnFile = new File("plugins/LotusFFA/spawn.yml");

	//Cache for both files, reloaded automatically when the file changes (e.g. after /FFA setup)
	static YamlConfiguration configCfg = null;
	static long configLastModified = -1;
	static YamlConfiguration spawnCfg = null;
	static long spawnLastModified = -1;

	private static YamlConfiguration getConfig() {
		if(configCfg == null || configFile.lastModified() != configLastModified) {
			configLastModified = configFile.lastModified();
			configCfg = YamlConfiguration.loadConfiguration(configFile);
		}
		return configCfg;
	}

	private static YamlConfiguration getSpawnConfig() {
		if(spawnCfg == null || spawnFile.lastModified() != spawnLastModified) {
			spawnLastModified = spawnFile.lastModified();
			spawnCfg = YamlConfiguration.loadConfiguration(spawnFile);
		}
		return spawnCfg;
	}

	//Map 1 is stored as "Spawn" in the spawn.yml, Map 2 and 3 as "Map2" and "Map3"
	private static String getSection(int map) {
		return map == 1 ? "Spawn" : "Map" + map;
	}

	public static int getCurrentMap() {
		YamlConfiguration config = getConfig();
		if(config.getBoolean("maps.map1")) return 1;
		if(config.getBoolean("maps.map2")) return 2;
		if(config.getBoolean("maps.map3")) return 3;
		return 1;
	}

	public static int getNextMap() {
		return (getCurrentMap() % 3) + 1;
	}

	public static void setCurrentMap(int map) {
		YamlConfiguration config = getConfig();
		for(int i = 1; i <= 3; i++) {
			config.set("maps.map" + i, i == map);
		}
		try {
			config.save(configFile);
			configLastModified = configFile.lastModified();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getMapName(int map) {
		return getConfig().getString("ffa.Mapname" + map);
	}

	//Returns null, if the spawn of this map has not been set up yet
	public static Location getSpawn(int map) {
		YamlConfiguration cfg = getSpawnConfig();
		String section = getSection(map);
		String worldName = cfg.getString(section + ".WORLD");
		if(worldName == null) return null;
		World world = Bukkit.getWorld(worldName);
		if(world == null) return null;
		return new Location(world,
				cfg.getDouble(section + ".X"),
				cfg.getDouble(section + ".Y"),
				cfg.getDouble(section + ".Z"),
				(float) cfg.getDouble(section + ".YAW"),
				(float) cfg.getDouble(section + ".PITCH"));
	}

	//Checks if the location is inside the spawn area (pos1/pos2) of the current map. Only X and Z are checked.
	public static boolean isInSpawnArea(Location loc) {
		int map = getCurrentMap();
		YamlConfiguration cfg = getSpawnConfig();
		String section = getSection(map);
		if(!cfg.contains(section + ".pos1") || !cfg.contains(section + ".pos2")) return false;
		Location spawn = getSpawn(map);
		if(spawn == null || !spawn.getWorld().equals(loc.getWorld())) return false;
		double x1 = cfg.getDouble(section + ".pos1.X");
		double z1 = cfg.getDouble(section + ".pos1.Z");
		double x2 = cfg.getDouble(section + ".pos2.X");
		double z2 = cfg.getDouble(section + ".pos2.Z");
		return loc.getX() >= Math.min(x1, x2) && loc.getX() <= Math.max(x1, x2)
				&& loc.getZ() >= Math.min(z1, z2) && loc.getZ() <= Math.max(z1, z2);
	}

}
