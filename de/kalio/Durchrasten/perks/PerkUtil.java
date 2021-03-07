package de.kalio.Durchrasten.perks;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.google.common.collect.HashBiMap;

import de.kalio.Durchrasten.Durchrasten;
import de.kalio.Durchrasten.jobs.utils.ItemBuilder;
import net.minecraft.server.v1_14_R1.ItemNamedBlock;

public class PerkUtil implements Listener {

	private HashMap<Player, HashMap<Integer, Boolean>> perks = new HashMap<>();
	
	
	public PerkUtil() {
		Bukkit.getScheduler().runTaskTimer(Durchrasten.getPlugin(Durchrasten.class), getRunnable(), 0, 3);
	}
	
	public void addPerk(Integer integer, Player player) {
		HashMap<Integer, Boolean> perksNow = perks.get(player);
		if(perksNow.containsKey(integer)) {
			boolean b = perksNow.get(integer);
			if(b) {
				perksNow.put(integer, false);
			} else {
				perksNow.put(integer, true);
			}
		} else {
			perksNow.put(integer, true);
		}
		
		perks.put(player, perksNow);
		
	}
	
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		perks.put(e.getPlayer(), new HashMap<>());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		perks.remove(e.getPlayer());
	}
	
	public void openGUI(Player player) {
		c(player);
		Inventory inventory = Bukkit.createInventory(null, 9*3, "§6§lPerks");
		for(Integer i = 0; i != inventory.getSize(); i++) {
			inventory.setItem(i, ItemBuilder.item(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§c").build());
		}
		inventory.setItem(0, ItemBuilder.item(Material.CHEST).setDisplayName("§8» §6KeepInventory").build());
		inventory.setItem(1, ItemBuilder.item(Material.COOKED_BEEF).setDisplayName("§8» §6Kein Hunger").build());
		inventory.setItem(2, ItemBuilder.item(Material.WATER_BUCKET).setDisplayName("§8» §6Kein Wasser Schaden").build());
		inventory.setItem(3, ItemBuilder.item(Material.LAVA_BUCKET).setDisplayName("§8» §6Kein Feuer Schaden").build());
		inventory.setItem(4, ItemBuilder.item(Material.DIAMOND_BOOTS).setDisplayName("§8» §6Schneller Laufen").build());
		inventory.setItem(5, ItemBuilder.item(Material.SEA_LANTERN).setDisplayName("§8» §6Nachtsicht").build());
		inventory.setItem(6, ItemBuilder.item(Material.GOLDEN_PICKAXE).setDisplayName("§8» §6Schneller Abbauen").build());
		inventory.setItem(7, ItemBuilder.item(Material.DIAMOND_AXE).setDisplayName("§8» §6Stärke").build());
		inventory.setItem(8, ItemBuilder.item(Material.FEATHER).setDisplayName("§8» §6Sprungkraft").build());
		inventory.setItem(9, get(0, "keepinventory", player));
		inventory.setItem(10, get(1, "nohunger", player));
		inventory.setItem(11, get(2, "nowater", player));
		inventory.setItem(12, get(3, "nofire", player));
		inventory.setItem(13, get(4, "run", player));
		inventory.setItem(14, get(5, "nachtsicht", player));
		inventory.setItem(15, get(6, "fastbreak", player));
		inventory.setItem(16, get(7, "stärke", player));
		inventory.setItem(17, get(8, "Sprungkraft", player));
		player.openInventory(inventory);
	}
	

	
	@EventHandler
	public void onHungerChange(FoodLevelChangeEvent e) {
		if(!(e.getEntity() instanceof Player)) {
			return;
		}
		Player player = (Player) e.getEntity();
		if(has(1, player)) {
			if(e.getFoodLevel() < player.getFoodLevel()) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if(has(0, e.getEntity())) {
			e.setKeepInventory(true);
		}
	}
	
	public Runnable getRunnable() {
		return () -> {
			for(Player player : Bukkit.getOnlinePlayers()) {
				c(player);
			}
		};
	}
	
	public void c(Player player) {
		if(has(2, player)) {
			PotionEffect effect = new PotionEffect(PotionEffectType.WATER_BREATHING, 20 * 15, 2);
			player.addPotionEffect(effect);
		}
		if(has(3, player)) {
			PotionEffect effect = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 15, 2);
			player.addPotionEffect(effect);
		}
		if(has(4, player)) {
			PotionEffect effect = new PotionEffect(PotionEffectType.SPEED, 20 * 15, 2);
			player.addPotionEffect(effect);
		}
		if(has(5, player)) {
			PotionEffect effect = new PotionEffect(PotionEffectType.NIGHT_VISION, 20 * 15, 2);
			player.addPotionEffect(effect);
		}
		if(has(6, player)) {
			PotionEffect effect = new PotionEffect(PotionEffectType.FAST_DIGGING, 20 * 15, 2);
			player.addPotionEffect(effect);
		}
		if(has(7, player)) {
			PotionEffect effect = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 15, 2);
			player.addPotionEffect(effect);
		}
		if(has(8, player)) {
			PotionEffect effect = new PotionEffect(PotionEffectType.JUMP, 20 * 15, 2);
			player.addPotionEffect(effect);
		}
		
	}
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getView().getTitle().equalsIgnoreCase("§6§lPerks")) {
			e.setCancelled(true);
			if(e.getCurrentItem().getType() != Material.BARRIER) {
				if(e.getCurrentItem().getType() == Material.GRAY_DYE || e.getCurrentItem().getType() == Material.LIME_DYE) {
					addPerk(e.getSlot() - 9, (Player) e.getWhoClicked());
					openGUI((Player) e.getWhoClicked());
				}
			}
		}
	}
	
	public ItemStack get(Integer i, String perk, Player player) {
		if(!(player.hasPermission("Perks.Perk."+perk))) {
			
			return ItemBuilder.item(Material.BARRIER).setDisplayName("§6Nicht freigeschaltet!").build();
		}
		if(has(i, player)) {
			return ItemBuilder.item(Material.LIME_DYE).setDisplayName("§a§lAktiviert").build();
		}
		return ItemBuilder.item(Material.GRAY_DYE).setDisplayName("§c§lDeaktiviert").build();
	}
	
	
    public boolean has(Integer i,Player player) {
    	HashMap<Integer, Boolean> bs = this.perks.get(player);
    	if(!bs.containsKey(i)) {
    		return false;
    	}
    	Boolean b = bs.get(i);
    	return b;
    }
	
	
	
}
