package de.kalio.Durchrasten.warps;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;



import de.kalio.Durchrasten.Durchrasten;
import de.kalio.Durchrasten.jobs.utils.ItemBuilder;

public class WarpUtil implements Listener {

	
	public FileConfiguration cfg = Durchrasten.cache;
	public HashMap<Player, HashMap<Integer, List<Player>>> players = new HashMap<>();
	
	public boolean warpExist(String name) {
		if(cfg.getString("Warp."+name+".Owner") == null) {
			return false;
		}
		return true;
	}
	
	
	public void teleport(Player player, String name) {
		Location location = (Location) cfg.get("Warp."+name+".Location");
		boolean wartung = cfg.getBoolean("Warp."+name+".Wartung");
		if(wartung) {
			player.sendMessage(Durchrasten.prefix + "§7Der Warp ist zurzeit in Wartung!");
			return;
		}
		player.teleport(location);
		player.sendTitle("§A§LERFOLG!", "§7Du wurdest teleportiert!");
		cfg.set("Warp."+name+".Besucher", cfg.getInt("Warp."+name+".Besucher")+1);
		Durchrasten.saveCache();
	}
	
	public String getPlayerWarp(String player) {
		return cfg.getString(player+".Warp");
	}
	
	public void setWartung(boolean b, String name) {
		cfg.set("Warp."+name+".Wartung", b);
		Durchrasten.saveCache();
	}
	
	public boolean getWartung(String name) {
		return cfg.getBoolean("Warp."+name+".Wartung");
	}
	
	public boolean hasPlayerWarp(String player) {
		if(cfg.getString(player+".Warp") == null) {
			return false;
		}
		return true;
	}
	
	public void createWarp(Location location, String player, String name) {
		cfg.set(player+".Warp", name);
		cfg.set("Warp."+name+".Owner", player);
		cfg.set("Warp."+name+".Wartung", false);
		cfg.set("Warp."+name+".Created", System.currentTimeMillis());
		cfg.set("Warp."+name+".Location", location);
		cfg.set("Warp."+name+".Besucher", 0);
		Durchrasten.saveCache();
	}
	
	public void deleteWarp(String player) {
		String name = cfg.getString(player+".Warp");
		cfg.set(player+".Warp", null);
		cfg.set("Warp."+name+"", null);
		Durchrasten.saveCache();
	}
	
	public void openInvetory(Player player) {
		Inventory inventory = Bukkit.createInventory(null, 6*9, "§6§lWarps §8| §71");
		for(int i = 45; i != inventory.getSize(); i++) {
			inventory.setItem(i, ItemBuilder.item(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§c").build());
		}
		HashMap<Integer, List<Player>> already = new HashMap<>();
		List<Player> players = new ArrayList<>();
		Bukkit.getOnlinePlayers().forEach(aplayer -> {
			if(inventory.getItem(44) == null) {
				System.out.println(aplayer.getUniqueId());
				String aplayer_u = aplayer.getUniqueId().toString();
				if(hasPlayerWarp(aplayer_u)) {
					String displayname = cfg.getString(aplayer_u+".Warp");
					List<String> lore = new ArrayList<>();
					lore.add("§8§m---------------");
					Long created = cfg.getLong("Warp."+displayname+".Created");
					Date data = new Date();
					data.setTime(created);
					String sdf = new SimpleDateFormat("yyyy:MM:dd mm:ss").format(data);
					lore.add("§6§lErstellt§8: §7"+sdf);
					boolean wartung = cfg.getBoolean("Warp."+displayname+".Wartung");
					lore.add("§6§lWartungen§8: §7"+(wartung ? "§cJa" : "§aNein"));
					lore.add("§6§lBesitzer§8: §7"+aplayer.getName());
					lore.add("§6§lBesucher§8: §7"+cfg.getInt("Warp."+displayname+".Besucher"));
					lore.add("§8§m---------------");
					inventory.addItem(ItemBuilder.item(Material.PAPER).setDisplayName(displayname).setLore(lore).build());

				    players.add(player);
				}
			}
		});
		already.put(1, players);
		this.players.put(player, already);
		inventory.setItem(48, ItemBuilder.item(Material.FIRE_CHARGE).setDisplayName("§6Zurück").build());
		inventory.setItem(50, ItemBuilder.item(Material.FIRE_CHARGE).setDisplayName("§6Weiter").build());
		player.openInventory(inventory);
	}
	

	
	@EventHandler
	public void onClick(InventoryClickEvent event){
		Player player = (Player) event.getWhoClicked();
		if(event.getView().getTitle().contains("§6§lWarps §8|")) {
			System.out.println(event.getView().getTitle().split("|")[0]);
			event.setCancelled(true);
			if(event.getCurrentItem().getType().equals(Material.PAPER)) {
				player.getServer().dispatchCommand(player, "sw teleport "+event.getCurrentItem().getItemMeta().getDisplayName());
			}else if(event.getCurrentItem().getType() == Material.FIRE_CHARGE) {
				Integer site = Integer.parseInt(event.getView().getTitle().split(" ")[2].replaceAll("§7", ""));

				if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Weiter")) {
					if(event.getInventory().getItem(44) != null) {
						Inventory inventory = Bukkit.createInventory(null, 9*6, "§6§lWarps §8| §7"+(site++));
						Integer newSite = site+1;
						for(int i = 45; i != inventory.getSize(); i++) {
							inventory.setItem(i, ItemBuilder.item(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§c").build());
						}
						HashMap<Integer, List<Player>> already = players.get(player);
						if(already.containsKey(newSite)) {
							already.forEach((integer, players) -> {
								players.forEach(aplayer -> {
									String aplayer_u = aplayer.getUniqueId().toString();
									String displayname = cfg.getString(aplayer_u+".Warp");
									List<String> lore = new ArrayList<>();
									lore.add("§8§m---------------");
									Long created = cfg.getLong("Warp."+displayname+".Created");
									Date data = new Date();
									data.setTime(created);
									String sdf = new SimpleDateFormat("yyyy:MM:dd mm:ss").format(data);
									lore.add("§6§lErstellt§8: §7"+sdf);
									boolean wartung = cfg.getBoolean("Warp."+displayname+".Wartung");
									lore.add("§6§lWartungen§8: §7"+(wartung ? "§cJa" : "§aNein"));
									lore.add("§6§lBesitzer§8: §7"+aplayer.getName());
									lore.add("§6§lBesucher§8: §7"+cfg.getInt("Warp."+displayname+".Besucher"));
									lore.add("§8§m---------------");
									inventory.addItem(ItemBuilder.item(Material.PAPER).setDisplayName(displayname).setLore(lore).build());
									
								});
							});
						} else {
;
							for(int i = 45; i != inventory.getSize(); i++) {
								inventory.setItem(i, ItemBuilder.item(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§c").build());
							}
							List<Player> players = new ArrayList<>();
							Bukkit.getOnlinePlayers().forEach(aplayer -> {
								if(inventory.getItem(44) != null) {
									String aplayer_u = aplayer.getUniqueId().toString();
									if(hasPlayerWarp(aplayer_u)) {
										String displayname = cfg.getString(aplayer_u+".Warp");
										List<String> lore = new ArrayList<>();
										lore.add("§8§m---------------");
										Long created = cfg.getLong("Warp."+displayname+".Created");
										Date data = new Date();
										data.setTime(created);
										String sdf = new SimpleDateFormat("yyyy:MM:dd mm:ss").format(data);
										lore.add("§6§lErstellt§8: §7"+sdf);
										boolean wartung = cfg.getBoolean("Warp."+displayname+".Wartung");
										lore.add("§6§lWartungen§8: §7"+(wartung ? "§cJa" : "§aNein"));
										lore.add("§6§lBesitzer§8: §7"+aplayer.getName());
										lore.add("§6§lBesucher§8: §7"+cfg.getInt("Warp."+displayname+".Besucher"));
										lore.add("§8§m---------------");
										inventory.addItem(ItemBuilder.item(Material.PAPER).setDisplayName(displayname).setLore(lore).build());

									    players.add(player);
									}
								}
							});
							already.put(newSite, players);
							this.players.put(player, already);
							inventory.setItem(48, ItemBuilder.item(Material.FIRE_CHARGE).setDisplayName("§6Zurück").build());
							inventory.setItem(50, ItemBuilder.item(Material.FIRE_CHARGE).setDisplayName("§6Weiter").build());
							player.openInventory(inventory);
							return;
						}
	
						inventory.setItem(48, ItemBuilder.item(Material.FIRE_CHARGE).setDisplayName("§6Zurück").build());
						inventory.setItem(50, ItemBuilder.item(Material.FIRE_CHARGE).setDisplayName("§6Weiter").build());
						player.openInventory(inventory);
					}
				} else {
					System.out.println(site);
					if(site != 1) {
						HashMap<Integer, List<Player>> already = players.get(player);
						Inventory inventory = Bukkit.createInventory(null, 9*6, "§6§lWarps §8| §7"+(site-1));
						Integer newSite = site-1;
						for(int i = 45; i != inventory.getSize(); i++) {
							inventory.setItem(i, ItemBuilder.item(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§c").build());
						}
						already.forEach((integer, players) -> {
							players.forEach(aplayer -> {
								String aplayer_u = aplayer.getUniqueId().toString();
								String displayname = cfg.getString(aplayer_u+".Warp");
								List<String> lore = new ArrayList<>();
								lore.add("§8§m---------------");
								Long created = cfg.getLong("Warp."+displayname+".Created");
								Date data = new Date();
								data.setTime(created);
								String sdf = new SimpleDateFormat("yyyy:MM:dd mm:ss").format(data);
								lore.add("§6§lErstellt§8: §7"+sdf);
								boolean wartung = cfg.getBoolean("Warp."+displayname+".Wartung");
								lore.add("§6§lWartungen§8: §7"+(wartung ? "§cJa" : "§aNein"));
								lore.add("§6§lBesitzer§8: §7"+aplayer.getName());
								lore.add("§6§lBesucher§8: §7"+cfg.getInt("Warp."+displayname+".Besucher"));
								lore.add("§8§m---------------");
								inventory.addItem(ItemBuilder.item(Material.PAPER).setDisplayName(displayname).setLore(lore).build());
								
							});
						});
						inventory.setItem(48, ItemBuilder.item(Material.FIRE_CHARGE).setDisplayName("§6Zurück").build());
						inventory.setItem(50, ItemBuilder.item(Material.FIRE_CHARGE).setDisplayName("§6Weiter").build());
						player.openInventory(inventory);
					}
				}
			}
			
		}
	}
	
}
