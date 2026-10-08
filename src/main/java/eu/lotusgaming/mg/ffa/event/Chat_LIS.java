package eu.lotusgaming.mg.ffa.event;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import eu.lotusgaming.mg.ffa.api.MapAPI;
import eu.lotusgaming.mg.ffa.main.LotusController;
import eu.lotusgaming.mg.ffa.main.Main;
import eu.lotusgaming.mg.ffa.misc.Prefix;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class Chat_LIS implements Listener{

	//Converts a String with §-Colorcodes into a Component
	private static Component legacy(String text) {
		return LegacyComponentSerializer.legacySection().deserialize(text);
	}

	@EventHandler
	public void onChat(AsyncChatEvent e) {
		Player p = e.getPlayer();
		Component msg = e.message();
		if(p.hasPermission("LobbySystem.ColorChat")) {
			String plain = PlainTextComponentSerializer.plainText().serialize(msg);
			msg = LegacyComponentSerializer.legacyAmpersand().deserialize(plain);
		}
		Component finalMsg = msg;
		e.renderer((source, sourceDisplayName, message, viewer) ->
			sourceDisplayName.append(legacy("§8: §f")).append(finalMsg)
		);
	}

	public static void Tablist() {
		Component header = legacy("§cLotus§aGaming §8>> §cHier kann dein Text stehen \n§coder hier \n§c§lICH DENKE AN DICH xD");
		Component footer = legacy(" §eFFA exclusiv für Lotus! \n§cViel Spaß damit \n§aLG Gianluca");
		for(Player all : Bukkit.getOnlinePlayers()) {
			all.sendPlayerListHeaderAndFooter(header, footer);
		}
	}

	public static void setScoreboard(Player p) {
		File config = new File("plugins/LotusFFA/config.yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(config);
		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective o = sb.registerNewObjective("aaa", Criteria.DUMMY, Component.text("name"));
		LotusController lc = new LotusController();

		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		o.displayName(legacy("§cLotus§aGaming §8>> " + lc.getPrefix(Prefix.MAIN)));
		o.getScore("§6").setScore(4);
		o.getScore("§eMapname:").setScore(3);
		if(cfg.getBoolean("maps.map1") == true) {
			o.getScore("§8>> §6" + cfg.getString("ffa.Mapname1")).setScore(2);
		}else if(cfg.getBoolean("maps.map2") == true) {
			o.getScore("§8>> §6" + cfg.getString("ffa.Mapname2")).setScore(2);
		}else if(cfg.getBoolean("maps.map3") == true) {
			o.getScore("§8>> §6" + cfg.getString("ffa.Mapname3")).setScore(2);
		}else {
			o.getScore("§8>> §cFehler beim laden des Mapnamens..").setScore(2);
		}
		o.getScore("§9").setScore(1);
		if(cfg.getBoolean("mapsettings.teams") == true) {
			o.getScore("§aTeams erlaubt!").setScore(0);
		}else if(cfg.getBoolean("mapsettings.teams") == false) {
			o.getScore("§cTeams verboten!").setScore(0);
		}
		p.setScoreboard(sb);
	}

	public static void startscoreboardScheduler() {
		new BukkitRunnable() {

			@Override
			public void run() {
				for(Player all : Bukkit.getOnlinePlayers()) {
					setScoreboard(all);
					Tablist();
				}

			}
		}.runTaskTimer(Main.instance, 0, 600);
	}

	public static void startSchedulermapchange() {
		File configfile = new File("plugins/LotusFFA/config.yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(configfile);
		LotusController lc = new LotusController();
		if(config.getBoolean("mapsettings.mapchange") == true) {

		new BukkitRunnable() {

			@Override
			public void run() {
				int next = MapAPI.getNextMap();
				if(MapAPI.getSpawn(next) == null) {
					Bukkit.getConsoleSender().sendMessage(lc.getPrefix(Prefix.MAIN) + "§cMap " + next + " wurde noch nicht eingerichtet, Mapwechsel übersprungen!");
					return;
				}
				Bukkit.broadcast(legacy(lc.getPrefix(Prefix.MAIN) + "§7Die Map wird in §e10 Sekunden §7gewechselt!"));
				Bukkit.getScheduler().runTaskLater(Main.instance, () -> changeMap(), 200);
			}
		}.runTaskTimer(Main.instance, 12000, 12000);
		} else {
			Bukkit.getConsoleSender().sendMessage(lc.getPrefix(Prefix.MAIN) + "§cMapchange wurde in der §econfig.yml §cdeaktiviert!");
		}
	}

	//Switches to the next map and teleports all players. Returns false, if the next map has not been set up yet.
	public static boolean changeMap() {
		LotusController lc = new LotusController();
		int next = MapAPI.getNextMap();
		Location loc = MapAPI.getSpawn(next);
		if(loc == null) {
			return false;
		}
		MapAPI.setCurrentMap(next);
		for(Player all : Bukkit.getOnlinePlayers()) {
			all.teleport(loc);
			setScoreboard(all);
		}
		Bukkit.broadcast(legacy(lc.getPrefix(Prefix.MAIN) + "§7Die Map wurde nun gewechselt zu: §e" + MapAPI.getMapName(next)));
		return true;
	}
}
