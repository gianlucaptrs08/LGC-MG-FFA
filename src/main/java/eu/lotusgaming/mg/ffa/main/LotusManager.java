//Created by Maurice H. at 12.11.2024
package eu.lotusgaming.mg.ffa.main;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;

import eu.lotusgaming.mg.ffa.command.FFA_CMD;
import eu.lotusgaming.mg.ffa.command.Stats_CMD;
import eu.lotusgaming.mg.ffa.event.Chat_LIS;
import eu.lotusgaming.mg.ffa.event.FFA_LIS;
import eu.lotusgaming.mg.ffa.misc.MySQL;
import eu.lotusgaming.mg.ffa.misc.Prefix;

public class LotusManager {
	
	public static File mainFolder = new File("plugins/LotusFFA");
	public static File mainConfig = new File("plugins/LotusFFA/config.yml");
	
	public void preInit() {
		long current = System.currentTimeMillis();
		
		if(!mainFolder.exists()) mainFolder.mkdirs();
		if(!mainConfig.exists()) try { mainConfig.createNewFile(); } catch (Exception ex) { };
		
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(mainConfig);
		cfg.addDefault("MySQL.Host", "127.0.0.1");
		cfg.addDefault("MySQL.Port", "3306");
		cfg.addDefault("MySQL.Database", "Testdatenbank");
		cfg.addDefault("MySQL.Username", "Test");
		cfg.addDefault("MySQL.Password", "Supersicherespasswort");
		cfg.addDefault("mapsettings.mapchange", false);
		cfg.addDefault("mapsettings.teams", false);
		cfg.addDefault("maps.map1", true);
		cfg.addDefault("maps.map2", false);
		cfg.addDefault("maps.map3", false);
		cfg.addDefault("ffa.Mapname1", "Map 1");
		cfg.addDefault("ffa.Mapname2", "Map 2");
		cfg.addDefault("ffa.Mapname3", "Map 3");
		cfg.options().copyDefaults(true);
		try {
			cfg.save(mainConfig);
			cfg.load(mainConfig);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		
		if(!cfg.getString("MySQL.Password").equalsIgnoreCase("Supersicherespasswort")) {
			MySQL.connect(cfg.getString("MySQL.Host"), cfg.getString("MySQL.Port"), cfg.getString("MySQL.Database"), cfg.getString("MySQL.Username"), cfg.getString("MySQL.Password"));
		}
		
		Bukkit.getConsoleSender().sendMessage("§7Pre-Initialization took §a" + (System.currentTimeMillis() - current) + "ms§7.");
	}
	
	public void init() {
		long current = System.currentTimeMillis();
		
		//Commands
		Main.instance.getCommand("ffa").setExecutor(new FFA_CMD());
		Main.instance.getCommand("stats").setExecutor(new Stats_CMD());
		
		//Events
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new FFA_LIS(), Main.instance);
		pm.registerEvents(new Chat_LIS(), Main.instance);
		
		Bukkit.getConsoleSender().sendMessage("§7Initialization took §a" + (System.currentTimeMillis() - current) + "ms§7.");
	}
	
	public void postInit() {
		long current = System.currentTimeMillis();
		
		LotusController lc = new LotusController();
		//Without a database connection these would crash the plugin on startup
		if(MySQL.isConnected()) {
			lc.initLanguageSystem();
			lc.initPlayerLanguages();
			lc.initPrefixSystem();
		}else {
			Main.logger.severe("No MySQL connection! Language system, prefixes and stats will not work. Please check the MySQL settings in the config.yml");
		}
		lc.loadServerIDName();
		
		Chat_LIS.startSchedulermapchange();
		Chat_LIS.startscoreboardScheduler();
		
		Bukkit.getConsoleSender().sendMessage("§7Post-Initialization took §a" + (System.currentTimeMillis() - current) + "ms§7.");

		Bukkit.getConsoleSender().sendMessage(lc.getPrefix(Prefix.MAIN) + "§6FFA §cVersion: §e" + Main.instance.getPluginMeta().getVersion());
		Bukkit.getConsoleSender().sendMessage(lc.getPrefix(Prefix.MAIN) + "§cEntwickelt von: §e" + Main.instance.getPluginMeta().getAuthors());
	}

}