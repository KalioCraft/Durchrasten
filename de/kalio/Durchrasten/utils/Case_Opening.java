package de.kalio.Durchrasten.utils;


import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import de.kalio.Durchrasten.Durchrasten;



public class Case_Opening implements Runnable {
	
	Integer i = 0;
	Integer max = zufallszahl(30, 34);
	Integer once = zufallszahl(3, 5);
	Integer twice = zufallszahl(6, 8);
	Integer three = zufallszahl(10, 14);
	Player p;
	Integer four = zufallszahl(14, 18);
	String Truhe;
	Integer six = zufallszahl(18, 20);
	Integer seven = zufallszahl(20, 24);
	Integer eigth = zufallszahl(24, 28);
	Integer nine = zufallszahl(28, 30);
	String Prefix = "§b§lKisten §8| §7";
	BukkitTask task;
	Inventory inv;
	public Case_Opening(Player player) {
		this.p = player;
	}

	public ItemStack PicRNDItems(String Truhe) {
		
		File CO = new File("plugins/CaseOpening/CaseOpening.yml");
		YamlConfiguration yCO = YamlConfiguration.loadConfiguration(CO);
		
		ArrayList<ItemStack> items = new ArrayList<>();
	
		ItemStack i = null;
		
		Integer ii = 0;
		
		while(ii != 100) {
			ii++;
			if(yCO.getString("Truhe" + "." + Truhe + ".Preise" + "." + ii) != null) {
				
				ItemStack is = new ItemStack(yCO.getItemStack("Truhe" + "." + Truhe + ".Preise" + "." + ii));
				
				if(is.getItemMeta().getLore().get(0).equalsIgnoreCase("§aNormal")) {
					
					items.add(is);
					items.add(is);
					items.add(is);
					items.add(is);
					items.add(is);
					items.add(is);
					items.add(is);
					items.add(is);
					
				}
				if(is.getItemMeta().getLore().get(0).equalsIgnoreCase("§6Selten")) {
					
					items.add(is);
					items.add(is);
					items.add(is);
					items.add(is);
					items.add(is);
					items.add(is);
					
				}
				if(is.getItemMeta().getLore().get(0).equalsIgnoreCase("§bUltra")) {
					
					items.add(is);
					items.add(is);
					items.add(is);
					items.add(is);
					
				}
				if(is.getItemMeta().getLore().get(0).equalsIgnoreCase("Episch")) {
					
					items.add(is);
					items.add(is);
					
				}
				if(is.getItemMeta().getLore().get(0).equalsIgnoreCase("§6Legendaer")) {
					
					items.add(is);
					
				}
			}
		}
		int random = new Random().nextInt(items.size());
		i = items.get(random);
		
		return i;
	}
	
	
	public void openco(Player p, String Truhe) {
		
		String Prefix = "§b§lKisten §8| §7";
		
	
			this.Truhe = Truhe;
			if(Truhe.equalsIgnoreCase("Episch")) {
				Cases.RemoveCase((Player) p, 1, "E");
			} else if(Truhe.equalsIgnoreCase("Hero")) {
				Cases.RemoveCase((Player) p, 1, "H");
			} else if(Truhe.equalsIgnoreCase("Frühling")) {
				Cases.RemoveCase(p, 1, "F");
			} else if(Truhe.equalsIgnoreCase("Vote")) {
				Cases.RemoveCase(p, 1, "V");
			}
			
			
			
			Inventory inv = Bukkit.createInventory(null, 9*3, "§6CaseOpening");
			this.inv = inv;
		    inv.setItem(4, ItemBuilder.createItemON(Material.HOPPER));
			
		    inv.setItem(9, PicRNDItems(Truhe));
		    inv.setItem(10, PicRNDItems(Truhe));
		    inv.setItem(11, PicRNDItems(Truhe));
		    inv.setItem(12, PicRNDItems(Truhe));
		    inv.setItem(13, PicRNDItems(Truhe));
		    inv.setItem(14, PicRNDItems(Truhe));
		    inv.setItem(15, PicRNDItems(Truhe));
		    inv.setItem(16, PicRNDItems(Truhe));
		    inv.setItem(17, PicRNDItems(Truhe));
		    
		    p.openInventory(inv);
		    this.task = Bukkit.getScheduler().runTaskTimer(Durchrasten.getPlugin(Durchrasten.class), this, 0, 1);


				}
		    public int zufallszahl(int min, int max) {
			    Random random = new Random();
			    return random.nextInt(max - min + 1) + min;
			  }

			@Override
			public void run() {
				
				if(i != max) {
					
					i++;
					if(i == once) {
						this.task.cancel();
					    this.task = Bukkit.getScheduler().runTaskTimer(Durchrasten.getPlugin(Durchrasten.class), this, 0,2);
					
					}
					if(i == twice) {
						this.task.cancel();
					    this.task = Bukkit.getScheduler().runTaskTimer(Durchrasten.getPlugin(Durchrasten.class), this, 0,3);
					
					}
					
					if(i == three) {
						this.task.cancel();
					    this.task = Bukkit.getScheduler().runTaskTimer(Durchrasten.getPlugin(Durchrasten.class), this, 0,4);
					}
					
					if(i == four) {
						this.task.cancel();
					    this.task = Bukkit.getScheduler().runTaskTimer(Durchrasten.getPlugin(Durchrasten.class), this, 0, 5);
					}
					if(i == six) {
						this.task.cancel();
					    this.task = Bukkit.getScheduler().runTaskTimer(Durchrasten.getPlugin(Durchrasten.class), this, 0, 6);
					}
					
					if(i == seven) {
						this.task.cancel();
					    this.task = Bukkit.getScheduler().runTaskTimer(Durchrasten.getPlugin(Durchrasten.class), this, 0, 8);
					}
					
					if(i == eigth) {
						this.task.cancel();
					    this.task = Bukkit.getScheduler().runTaskTimer(Durchrasten.getPlugin(Durchrasten.class), this, 0, 11);
					}
					if(i == nine) {
						this.task.cancel();
					    this.task = Bukkit.getScheduler().runTaskTimer(Durchrasten.getPlugin(Durchrasten.class), this, 0, 15);
					}
					
					p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1, 1);
					
					inv.setItem(17, inv.getItem(16));
					inv.setItem(16, inv.getItem(15));
					inv.setItem(15, inv.getItem(14));
					inv.setItem(14, inv.getItem(13));
					inv.setItem(13, inv.getItem(12));
					inv.setItem(12, inv.getItem(11));
					inv.setItem(11, inv.getItem(10));
					inv.setItem(10, inv.getItem(9));
					inv.setItem(9, PicRNDItems(Truhe));
					
					p.openInventory(inv);
					
				} else {
					
					inv.setItem(17, inv.getItem(16));
					inv.setItem(16, inv.getItem(15));
					inv.setItem(15, inv.getItem(14));
					inv.setItem(14, inv.getItem(13));
					inv.setItem(13, inv.getItem(12));
					inv.setItem(12, inv.getItem(11));
					inv.setItem(11, inv.getItem(10));
					inv.setItem(10, inv.getItem(9));
					inv.setItem(9, PicRNDItems(Truhe));
					
					p.openInventory(inv);
					
					this.task.cancel();
					
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
					
					p.sendMessage(Prefix + "§aDu hast " + inv.getItem(13).getItemMeta().getDisplayName() + " §agewonnen!");
					
					p.getInventory().addItem(inv.getItem(13));
					
			
					
				}
			}
	

		    
		    
}
