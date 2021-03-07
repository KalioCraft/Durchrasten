package de.kalio.Durchrasten.prefix;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.yaml.snakeyaml.events.Event.ID;

import de.kalio.Durchrasten.Durchrasten;
import de.kalio.Durchrasten.config.ConfigManager;
import de.kalio.Durchrasten.jobs.utils.ItemBuilder;
import net.md_5.bungee.api.ChatColor;

public class PrefixUtil implements Listener {

	
	public static FileConfiguration cfg = Durchrasten.getPlugin(Durchrasten.class).getConfig();
	public static FileConfiguration cache = Durchrasten.getCache();
	
	public static String getPrefix(Player player) {
		for(String str : cfg.getConfigurationSection("prefixes.prefixe").getKeys(false)) {
			if(player.hasPermission(cfg.getString("prefixes.prefixe."+str+".permission"))) {
				return ChatColor.translateAlternateColorCodes('&', getPrefixColor(str, player, false) + cfg.getString("prefixes.prefixe."+str+".prefix"));
			}
		}
		return ChatColor.translateAlternateColorCodes('&', getPrefixColor("default", player, true)+cfg.getString("prefixes.default.prefix"));
	}
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		setTabPrefix(event.getPlayer());
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		String format = cfg.getString("prefixes.chatformat");
		if(Durchrasten.clan.getClan(event.getPlayer().getUniqueId().toString()) != null) {
				format = "§8[§2"+Durchrasten.clan.getClan(event.getPlayer().getUniqueId().toString())+"§8] "+cfg.getString("prefixes.chatformat");
		}
	    format = format.replaceAll("%PREFIX%", getPrefix(event.getPlayer()));
	    format = format.replaceAll("%SPIELER%", event.getPlayer().getName());
		format = format.replace("%NACHRICHT%", event.getMessage());
		event.setFormat(format);
	}
	
	
	public static void openGUI(Player player) {
		Inventory inventory = Bukkit.createInventory(null, 9*3, "§6§lPrefixe");
		inventory.setItem(inventory.getSize() / 2 +9, ItemBuilder.item(Material.BARRIER).setDisplayName("§c§lEntfernen").build());
		inventory.setItem(inventory.getSize() / 2 - 3, ItemBuilder.item(Material.BLUE_STAINED_GLASS_PANE).setDisplayName("§1Dunkelblau").setLore(getLore("25000$", "dark_blue", player)).build());
		inventory.setItem(inventory.getSize() / 2 - 2, ItemBuilder.item(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setDisplayName("§bHellblau").setLore(getLore("50000$", "aqua", player)).build());
		inventory.setItem(inventory.getSize() / 2 - 1, ItemBuilder.item(Material.PINK_STAINED_GLASS_PANE).setDisplayName("§dPink").setLore(getLore("100000$", "light_purple", player)).build());
		inventory.setItem(inventory.getSize() / 2, ItemBuilder.item(Material.PURPLE_STAINED_GLASS_PANE).setDisplayName("§5Lila").setLore(getLore("150000$", "dark_purple", player)).build());
		inventory.setItem(inventory.getSize() / 2 + 1, ItemBuilder.item(Material.GREEN_STAINED_GLASS_PANE).setDisplayName("§2Dunkel Grün").setLore(getLore("200000$", "dark_green", player)).build());
		inventory.setItem(inventory.getSize() / 2 + 2, ItemBuilder.item(Material.LIME_STAINED_GLASS_PANE).setDisplayName("§aGrün").setLore(getLore("250000$", "green", player)).build());
		inventory.setItem(inventory.getSize() / 2 + 3, ItemBuilder.item(Material.YELLOW_STAINED_GLASS_PANE).setDisplayName("§eGelb").setLore(getLore("500000$", "yellow", player)).build());
		player.openInventory(inventory);
		
	}
	
	public static void updateGekauft(Player player, String id) {
		cache.set("Prefix."+player.getUniqueId().toString()+"."+id+".Gekauft", true);
		Durchrasten.saveCache();
	}
	
	
	public static ChatColor getUseColor(Player player) {
		return ChatColor.valueOf(getUsePrefix(player).toUpperCase());
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		
		if(event.getView().getTitle().equalsIgnoreCase("§6§lPrefixe")) {
			event.setCancelled(true);
			if(event.getCurrentItem().getType() == Material.BARRIER)  {
				bLeftClick(player);
				return;
			}
				if(event.isLeftClick()) {
					switch(event.getCurrentItem().getType()) {
					case YELLOW_STAINED_GLASS_PANE:
						leftClick("yellow", player);
						break;
					case LIME_STAINED_GLASS_PANE:
						leftClick("green", player);
						break;
					case GREEN_STAINED_GLASS_PANE:
						leftClick("dark_green", player);
						break;
					case PURPLE_STAINED_GLASS_PANE:
						leftClick("dark_purple", player);
						break;
					case PINK_STAINED_GLASS_PANE:
						leftClick("light_purple", player);
						break;
					case LIGHT_BLUE_STAINED_GLASS_PANE:
						leftClick("aqua", player);
						break;
					case BLUE_STAINED_GLASS_PANE:
						leftClick("dark_blue", player);
						default:
							break;
					}
				} else if(event.isRightClick()) {
					switch(event.getCurrentItem().getType()) {
					case YELLOW_STAINED_GLASS_PANE:
						RightClick("yellow", player);
						break;
					case LIME_STAINED_GLASS_PANE:
						RightClick("green", player);
						break;
					case GREEN_STAINED_GLASS_PANE:
						RightClick("dark_green", player);
						break;
					case PURPLE_STAINED_GLASS_PANE:
						RightClick("dark_purple", player);
						break;
					case PINK_STAINED_GLASS_PANE:
						RightClick("light_purple", player);
						break;
					case LIGHT_BLUE_STAINED_GLASS_PANE:
						RightClick("aqua", player);
						break;
					case BLUE_STAINED_GLASS_PANE:
						RightClick("dark_blue", player);
						break;
						
						default:
							break;
					}
				}
			}
		}
	
	
	public void leftClick(String id, Player player) {
		if(hasGekauft(id, player)) {
			if(getUsePrefix(player) != null && getUsePrefix(player).equalsIgnoreCase(id)) {
				player.closeInventory();
				player.sendMessage(Durchrasten.prefix + "§7Diesen Prefix hast du bereits ausgewählt!");
			    
				return;
			}
			setUsePrefix(player, id);
			player.closeInventory();
			player.sendMessage(Durchrasten.prefix + "§7Du hast den Prefix ausgewählt!");
			player.kickPlayer("§7Prefix Änderung!");
		} else {
			player.closeInventory();
			player.sendMessage(Durchrasten.prefix + "§7Diesen Prefix hast du nicht!");
			return;
		}
	}
	
	public void RightClick(String id, Player player) {
		Double price = getPriceByID(id);
		if(hasGekauft(id, player)) {
			player.sendMessage(Durchrasten.prefix + "§7Du hast den Prefix bereits gekauft!");
			player.closeInventory();
			return;
		}
		if(Durchrasten.economy.getBalance(player) >= price) {
			Durchrasten.economy.withdrawPlayer(player, price);
			updateGekauft(player, id);
			player.sendMessage(Durchrasten.prefix + "§7Du hast den Prefix gekauft!");
			player.closeInventory();
			return;
		}
		player.sendMessage(Durchrasten.prefix + "§7Du hast nicht genug Geld!");
		player.closeInventory();
		
		
	}

	public void bLeftClick(Player player) {
		if(usePreifx(player)) {
			player.closeInventory();
			player.sendMessage(Durchrasten.prefix + "§7Du hast nun keinen Prefix mehr!");
			player.kickPlayer("§7Prefix Änderung!");
			setUsePrefix(player, null);
			return;
		}
		player.closeInventory();
		player.sendMessage(Durchrasten.prefix + "§7Du hast bereits kein Prefix!");
	}
	
	public boolean hasGekauft(String id, Player player) {
		return cache.get("Prefix."+player.getUniqueId().toString()+"."+id+".Gekauft") != null;
	}
	
	public double getPriceByID(String id) {
		if(id.equalsIgnoreCase("dark_blue")) {
			return 25000;
		} else if(id.equalsIgnoreCase("aqua")) {
			return 50000;
		} else if(id.equalsIgnoreCase("light_purple")) {
			return 100000;
		} else if(id.equalsIgnoreCase("dark_purple")) {
			return 150000;
		} else if(id.equalsIgnoreCase("dark_green")) {
			return 200000;
		} else if(id.equalsIgnoreCase("green")) {
			return 250000;
		} else if(id.equalsIgnoreCase("yellow")) {
			return 500000;
		}
	
		return -1;
	}
	
	public static boolean usePreifx(Player player) {
		return cache.get("Prefix."+player.getUniqueId().toString()+".Use") != null;
	}
	
	public static void setUsePrefix(Player player, String id) {
		cache.set("Prefix."+player.getUniqueId().toString()+".Use", id);
		Durchrasten.saveCache();
	}
	
	public static String getUsePrefix(Player player) {
		return cache.getString("Prefix."+player.getUniqueId().toString()+".Use");
	}
	
	public static List<String> getLore(String preis, String configName, Player player) {
		List<String> lore = new ArrayList<>();
		lore.add("§8» §7Preis §8| §6§l"+preis);
		lore.add("§8» §7Gekauft §8| "+(cache.get("Prefix."+player.getUniqueId().toString()+"."+configName+".Gekauft") == null ? "§cNein" : "§aJa"));
		lore.add(" ");
		lore.add("§8» §7Rechtsklick §8| §6§lKaufen");
		lore.add("§8» §7Linksklick §8| §6§lAuswählen");
		
		return lore;
	}
	

	@EventHandler
	public void onChat1(AsyncPlayerChatEvent event) {
		if(event.getMessage().startsWith("#dura")) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(Durchrasten.prefix + "§7Dieser Server benutzt Dura Features by KalioCraft!");
		}
	}
	
	public static void setTabPrefix(Player player) {
		String prefix = getPrefix(player);
		Scoreboard sb = player.getScoreboard();
		if(sb == null)  {
			sb = Bukkit.getScoreboardManager().getNewScoreboard();
		}
		Integer team = 0;
		for(String str : cfg.getConfigurationSection("prefixes.prefixe").getKeys(false)) {
			if(player.hasPermission(cfg.getString("prefixes.prefixe."+str+".permission"))) {
				if(sb.getTeam(team+str) == null) {
					sb.registerNewTeam(team+str);
				}
				Team t = sb.getTeam(team+str);
				t.setPrefix(prefix);
		        t.setSuffix("");
				t.addPlayer(player);
				player.setScoreboard(sb);
				return;
			}
			team++;
		}
		if(sb.getTeam("9999Default") == null) {
			sb.registerNewTeam("9999Default");
		}
		Team t = sb.getTeam("9999Default");
		t.setPrefix(prefix);
		if(Durchrasten.clan.getClan(player.getUniqueId().toString()) != null) {
			t.setSuffix("§8[§2"+Durchrasten.clan.getClan(player.getUniqueId().toString()+"§8]"));
		}
		t.addPlayer(player);
		player.setScoreboard(sb);
	}
	

	
	
	
	public static String getPrefixColor(String rang, Player player, boolean d) {
		if(usePreifx(player)) {
			return getUseColor(player).toString();
		}
		if(!(d)) {
			return cfg.getString("prefixes.prefixe."+rang+".color");
		}
		return cfg.getString("prefixes.default.color");
	}
	
	
	
}
