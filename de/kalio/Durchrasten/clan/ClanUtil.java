package de.kalio.Durchrasten.clan;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;


import de.kalio.Durchrasten.Durchrasten;

public class ClanUtil {

	
	YamlConfiguration config = Durchrasten.getCache();
	
	
	public void create(String clan, String owner) {
		
		config.set("Clan."+clan+".members", Arrays.asList(owner));
		config.set("Clan."+clan+".owner", owner);
		config.set("Clan." + clan + ".Bank", 0.0);
		config.set("Clan."+clan+".Level", 1);
		config.set("Clan." + clan + ".UpgradeBank", 0.0);
		config.set(owner+".Clan", clan);
		Durchrasten.saveCache();
		
		
	}
	
	
	
	public Integer getClanLevel(String player) {
		String clan = config.getString(player+".Clan");
		return config.getInt("Clan." + clan + ".Level");
	}
	
	public void setClanLevel(String player, Integer new_level) {
		String clan = config.getString(player+".Clan");
		config.set("Clan." + clan + ".Level", new_level);
		Durchrasten.saveCache();
	}
	
	public boolean canBank(String player, Double d) {
		String clan = config.getString(player+".Clan");
		Double bank = config.getDouble("Clan." + clan + ".Bank");
		if((getClanLevel(player) * 10000.0) <= bank + d) {
			return false;
		}
		return true;
	}
	
	public double getBank(String player) {
		String clan = config.getString(player+".Clan");
		return config.getDouble("Clan." + clan + ".Bank");
	}
	
	public void setBank(String player, Double new_bank) {
		String clan = config.getString(player+".Clan");
		config.set("Clan." + clan + ".Bank", new_bank);
		Durchrasten.saveCache();
	}
	
	public double getUpgradeBank(String player) {
		String clan = config.getString(player+".Clan");
		return config.getDouble("Clan." + clan + ".UpgradeBank");
	}
	
	public void setUpgradeBank(String player, Double d) {
		String clan = getClan(player);
		config.set("Clan." + clan + ".UpgradeBank", d);
		Durchrasten.saveCache();
	}
	
	public void check(String player) {
		Integer level = getClanLevel(player);
		Double money = 10000.0 * level;
		Integer MembersRequire = 5 * level;
		if(getMembers(player).size() >= MembersRequire && getUpgradeBank(player) >= money) {
			setClanLevel(player, level ++);
			for(String member : getMembers(player)) {
				if(Bukkit.getPlayer(UUID.fromString(member)) != null) {
				   Player bp = Bukkit.getPlayer(UUID.fromString(member));
				   bp.sendMessage(Durchrasten.prefix + "§7EUER CLAN HAT LEVEL §6§l"+(level++)+"§7 ERREICHT! IHR ERHALTET FOLGENDES:");
				   bp.sendMessage(Durchrasten.prefix + "§7Neues Clankassen Limit: §6§l"+((level++) * 10000));
				   bp.sendMessage(Durchrasten.prefix + "§7Neues Memberlimit: §6§l"+((level++) * 10));
				}
			}
		}
		
	}
	
	public boolean isPlayerInClan(String player) {
		if(config.get(player+".clan") == null) {
			return false;
		}
		return true;
	}
	
	public List<String> getMembers(String player){
		String clan = config.getString(player+".Clan");
		return config.getStringList("Clan."+clan+".members");
	}
	
	public void leaveClan(String player) {
		String clan = config.getString(player+".clan");
		List<String> players = config.getStringList("Clan."+clan+".members");
		players.remove(player);
		config.set(clan+".members", players);
		config.set(player + ".Clan", null);
		Durchrasten.saveCache();
	}
	
	public void joinClan(String player, String clan) {
		List<String> players = config.getStringList("Clan."+clan+".members");
		players.add(player);
		config.set(clan+".members", players);
		config.set(player + ".Clan", clan);
		Durchrasten.saveCache();
	}
	
	public String getClan(String player) {
		return config.getString(player+".Clan");
	}
	
	public void addInvite(String player, Player inviteSender) {
		
		if(config.get(player+".invites") != null) {
			List<String> invites = config.getStringList(player+".invites");
			invites.add(inviteSender.getUniqueId().toString()+"|"+inviteSender.getName());
			config.set(player + ".invites", invites);
			Durchrasten.saveCache();
			return;
		}
	    config.set(player+".invites", Arrays.asList(inviteSender.getUniqueId().toString()+"|"+inviteSender.getName()));
		Durchrasten.saveCache();
		
		
		
	}
	
	public void removeInvite(String player, String invite) {
		
		if(!(config.get(player+".invites") == null)) {
			List<String> invites = config.getStringList(player+".invites");
			for(String str : invites) {
				String[] args = str.split("|");
				String name = args[1];
				if(name == invite) {
					invites.remove(str);
				}
			}
			if(invites.isEmpty()) {
				config.set(player + ".invites", null);
			} else {
				config.set(player + ".invites", invites);
			}
			Durchrasten.saveCache();
			
		}
		
		
	}
	
	public boolean hasInvite(String player, String invite) {
		
		
		if(config.get(player+".invites") == null) {
			return false;
		}
		List<String> invites = config.getStringList(player + ".invites");
		for(String str : invites) { 
			if(str.contains(invite)) {
				return true;
			}
		}
		
		return false;
	}
	
	
	public List<String> getInvites(String player) {
		return config.getStringList(player+".invites");
	}
	
	public void deleteClan(String owner) {
		String clan = config.getString(owner+".Clan");
		List<String> members = config.getStringList("clan."+clan+".members");
		for(String member : members) {
			config.set(member+".clan", null);
		}
		config.set("clan."+clan, null);
		Durchrasten.saveCache();
	}
	
	public boolean isOwner(String player) {
		String clan = config.getString(player + ".Clan");
		String owner = config.getString("Clan."+clan+".owner");
		if(player.equalsIgnoreCase(owner)) {
			return true;
		}
		return false;
	}
	
	public void sendMessage(String player, String str) {
		String clan = config.getString(player + ".Clan");
		
		List<String> members = config.getStringList("Clan."+clan+".members");
		for(String member : members) {
			    System.out.println(Bukkit.getPlayer(UUID.fromString(member)));
				if(Bukkit.getPlayer(UUID.fromString(member)) != null) {
					String senderName = Bukkit.getPlayer(UUID.fromString(member)).getName();
					Player memberPlayer = Bukkit.getPlayer(UUID.fromString(member));
					memberPlayer.sendMessage("§2§lClan §8| §7"+senderName + " §8➤ §7"+str);
				}
			
		}
	}
	

	
}
