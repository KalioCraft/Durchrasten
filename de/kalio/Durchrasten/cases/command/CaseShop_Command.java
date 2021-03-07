package de.kalio.Durchrasten.cases.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.RegisteredServiceProvider;

import de.kalio.Durchrasten.Durchrasten;
import de.kalio.Durchrasten.utils.Cases;
import de.kalio.Durchrasten.utils.ItemBuilder;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;

public class CaseShop_Command implements Listener, CommandExecutor {

	
	public CaseShop_Command(Durchrasten plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		plugin.getCommand("caseshop").setExecutor(this);
	}

	   RegisteredServiceProvider<Economy> rsp = Durchrasten.getPlugin(Durchrasten.class).getServer().getServicesManager().getRegistration(Economy.class);
       Economy econ = (Economy)rsp.getProvider();
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		String prefix = Durchrasten.prefix;
		if(e.getView().getTitle().equalsIgnoreCase("§b§lKisten §8| §7Shop")) {
			e.setCancelled(true);
			System.out.println("d");
			Material mat = e.getCurrentItem().getType();
			if(mat == Material.CHEST) {
				if(econ.getBalance(p) >= 2000) {
					econ.withdrawPlayer(p, 2000);
					Cases.AddCase(p, 1, "V");
					p.sendMessage(prefix+"§7Du hast 1x Vote Kiste gekauft! §8| §6§l-$2000");
					p.closeInventory();
					return;
				}else {
					p.closeInventory();
					p.sendMessage(prefix+"§7Du hast nicht genug Geld!");
					return;
				}
			}else if(mat == Material.ENDER_CHEST) {
				if(econ.getBalance(p) >= 10000) {
					econ.withdrawPlayer(p, 10000);
					Cases.AddCase(p, 1, "E");
					p.sendMessage(prefix+"§7Du hast 1x §5Epische §7Kiste gekauft! §8| §6§l-$10000");
					p.closeInventory();
					return;
				}else {
					p.closeInventory();
					p.sendMessage(prefix+"§7Du hast nicht genug Geld!");
					return;
				}
			}else if(mat == Material.GRASS_BLOCK) {
				if(econ.getBalance(p) >= 20000) {
					econ.withdrawPlayer(p, 20000);
					Cases.AddCase(p, 1, "F");
					p.sendMessage(prefix+"§7Du hast 1x §a§lFrühlings §7Kiste gekauft! §8| §6§l-$20000");
					p.closeInventory();
					return;
				}else {
					p.closeInventory();
					p.sendMessage(prefix+"§7Du hast nicht genug Geld!");
					return;
				}
			}else if(mat == Material.END_PORTAL_FRAME) {
				if(econ.getBalance(p) >= 50000) {
					econ.withdrawPlayer(p, 50000);
					Cases.AddCase(p, 1, "H");
					p.sendMessage(prefix+"§7Du hast 1x §c§lHero §7Kiste gekauft! §8| §6§l-$50000");
					p.closeInventory();
					return;
				}else {
					p.closeInventory();
					p.sendMessage(prefix+"§7Du hast nicht genug Geld!");
					return;
				}
			}
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,String arg2,
			String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage("§c§lDu musst spieler sein!");
			return true;
		}
		Inventory inventory = Bukkit.createInventory(null, 9*3, "§b§lKisten §8| §7Shop");
		for(Integer i = 0; i != inventory.getSize(); i++) {
			inventory.setItem(i, ItemBuilder.createItemOL(Material.BLACK_STAINED_GLASS_PANE, "§b", 1));
		}
		inventory.setItem(inventory.getSize() / 2 - 2, ItemBuilder.createItemOL(Material.CHEST, "§7Vote Kiste §8(2000$, 1x)"
				,1));
		inventory.setItem(inventory.getSize() / 2, ItemBuilder.createItemOL(Material.ENDER_CHEST, "§5Epische§7 Kiste §8(10000$, 1x)"
				,1));
		inventory.setItem(inventory.getSize() / 2 + 2, ItemBuilder.createItemOL(Material.END_PORTAL_FRAME, "§c§lHero §7Kiste §8($50000, 1x)"
				,1));
		inventory.setItem(inventory.getSize() / 2 +9, ItemBuilder.createItemOL(Material.GRASS_BLOCK, "§a§lFrühlings §7Kiste §8(20000$, 1x)"
				,1));
		((Player) sender).openInventory(inventory);
		return false;
	}
}