package de.kalio.Durchrasten.belohnung;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitTask;

import de.kalio.Durchrasten.Durchrasten;
import de.kalio.Durchrasten.jobs.utils.ItemBuilder;

public class BelohnungUtil implements Listener {
	
	
	private HashMap<Player, Boolean> can = new HashMap<>();
	private HashMap<Player, BukkitTask> tasks = new HashMap<>();
    private List<Player> players = new ArrayList<>();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		can.put(event.getPlayer(), false);

	    this.tasks.put(event.getPlayer(), Bukkit.getScheduler().runTaskTimer(Durchrasten.getPlugin(Durchrasten.class), () -> {
	    	if(players.contains(event.getPlayer())) {
	    		event.getPlayer().sendMessage(Durchrasten.prefix + "§7Sammle deine Belohnung (/belohnung) ein! Danke für deine Aktivität!");
		    	can.put(event.getPlayer(), true);
	    	}else {
	    		players.add(event.getPlayer());
	    	}
	    	
	    	
	    }, 0, 20 * 60 * 30));	
	}
	
	public boolean can(Player player) {
		return can.get(player);
	}
	
	public void openInventory(Player player) {
		Inventory inventory = Bukkit.createInventory(null, 9*3, "§6§lBelohnung");
		inventory.setItem(inventory.getSize() / 2, ItemBuilder.item(Material.CHEST).setDisplayName("§7Einsammeln").build());
		player.openInventory(inventory);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if(event.getView().getTitle().equalsIgnoreCase("§6§lBelohnung")) {
			event.setCancelled(true);
			Player player = (Player) event.getWhoClicked();
			if(can(player)) {
				Durchrasten.economy.depositPlayer(player, 200);
				player.sendMessage(Durchrasten.prefix + "§7Du hast als Belohnung 200 bekommen!");
				this.can.put(player, false);
				player.closeInventory();
				return;
			}
			player.closeInventory();
			player.sendMessage(Durchrasten.prefix + "§7Sei aktivier, um diese Belohnung zu bekommen!");
			return;
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		this.tasks.get(event.getPlayer()).cancel();
		this.tasks.remove(event.getPlayer());
		this.can.remove(event.getPlayer());
		this.players.remove(event.getPlayer());
	}
	
	

	
	
	
}
