package de.kalio.Durchrasten.bank;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import de.kalio.Durchrasten.Durchrasten;

public class BankUtil {

	

	YamlConfiguration yalm = Durchrasten.getCache();
	
	public void join(PlayerJoinEvent e) { 
		Player player = e.getPlayer();
		if(yalm.get("Bank."+player.getUniqueId()+".Value") == null) {
			yalm.set("Bank."+player.getUniqueId()+".Value", 0.0);
			Durchrasten.saveCache();
		}
	}
	
	public Double getValue(Player player) {
		return yalm.getDouble("Bank."+player.getUniqueId()+".Value");
	}
	
	public void setValue(Player player, Double value) {
		yalm.set("Bank."+player.getUniqueId()+".Value", value);
		Durchrasten.saveCache();
	}
	
	
	
	
}
