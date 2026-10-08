package eu.lotusgaming.mg.ffa.api;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import eu.lotusgaming.mg.ffa.misc.MySQL;

public class StatsAPI {
	
	private Player player;
	
	public StatsAPI(Player player) {
		this.player = player;
	}
	
	public boolean hasMGAccount() {
		boolean hasAccount = false;
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT mg_user FROM mc_minigameusers WHERE mg_user = ? AND mg_name = 'FFA'");
			ps.setString(1, player.getUniqueId().toString());
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				hasAccount = true;
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hasAccount;
	}
	
	public void createMGAccount() {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO mc_minigameusers (mg_user, mg_name, kills, deaths, wins, loose, points) VALUES (?, ?, 0, 0, 0, 0, 0)");
			ps.setString(1, player.getUniqueId().toString());
			ps.setString(2, "FFA");
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addKill() {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE mc_minigameusers SET kills = kills + 1 WHERE mg_user = ? AND mg_name = 'FFA'");
			ps.setString(1, player.getUniqueId().toString());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getKills() {
		int kills = 0;
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT kills FROM mc_minigameusers WHERE mg_user = ? AND mg_name = 'FFA'");
			ps.setString(1, player.getUniqueId().toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				kills = rs.getInt("kills");
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kills;
	}
	
	public void addDeath() {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE mc_minigameusers SET deaths = deaths + 1 WHERE mg_user = ? AND mg_name = 'FFA'");
			ps.setString(1, player.getUniqueId().toString());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getDeaths() {
		int deaths = 0;
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT deaths FROM mc_minigameusers WHERE mg_user = ? AND mg_name = 'FFA'");
			ps.setString(1, player.getUniqueId().toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				deaths = rs.getInt("deaths");
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return deaths;
	}
	
	public void addGameWin() {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE mc_minigameusers SET wins = wins + 1 WHERE mg_user = ? AND mg_name = 'FFA'");
			ps.setString(1, player.getUniqueId().toString());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getGameWins() {
		int wins = 0;
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT wins FROM mc_minigameusers WHERE mg_user = ? AND mg_name = 'FFA'");
			ps.setString(1, player.getUniqueId().toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				wins = rs.getInt("wins");
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return wins;
	}
	
	public void addGameLost() {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE mc_minigameusers SET loose = loose + 1 WHERE mg_user = ? AND mg_name = 'FFA'");
			ps.setString(1, player.getUniqueId().toString());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getGameLosts() {
		int losts = 0;
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT loose FROM mc_minigameusers WHERE mg_user = ? AND mg_name = 'FFA'");
			ps.setString(1, player.getUniqueId().toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				losts = rs.getInt("loose");
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return losts;
	}
	
	public void addPoints(int points) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE mc_minigameusers SET points = points + ? WHERE mg_user = ? AND mg_name = 'FFA'");
			ps.setInt(1, points);
			ps.setString(2, player.getUniqueId().toString());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getPoints() {
		int points = 0;
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT points FROM mc_minigameusers WHERE mg_user = ? AND mg_name = 'FFA'");
            ps.setString(1, player.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                points = rs.getInt("points");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return points;
	}
}