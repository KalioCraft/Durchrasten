package de.kalio.Durchrasten.jobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.util.Vector;

import de.kalio.Durchrasten.Durchrasten;
import de.kalio.Durchrasten.jobs.utils.ItemBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;

public class Jobs implements Listener {
	   static File file = new File("plugins/Jobs/Workspace.yml");
	   static FileConfiguration cfg;
	   public static String JobsInventoryName;
	   public static String PREFIX;
	   public static String ShowCaseInventoryName;
	   public static String SwitchInventoryNameMiner;
	   public static String SwitchInventoryNameFarmer;
	   public static String SwitchInventoryNameBuddler;
	   public static String SwitchInventoryNameFüller;
	   public static String ServerName;
	   public static String SwitchInventoryNameReisender;
	   public static String SwitchInventoryNameKiller;
	   public static String WorldMiner, NetherMiner, EndMiner;
	   
	   public static String MinerType;
	   // $FF: synthetic field
	   private static volatile int[] $SWITCH_TABLE$org$bukkit$Material;

	   static {
	      cfg = YamlConfiguration.loadConfiguration(file);
	      JobsInventoryName = "§8§l> §7Jobs §8§l<";
	      PREFIX = Durchrasten.prefix;
	      MinerType = "§8§l> §7Miner Type §8§l<";
	      WorldMiner = "§8§l> §7Job ändern? (Miner, §aOberwelt§7) §8§l<";
	      NetherMiner ="§8§l> §7Job ändern? (Miner, §cNether§7) §8§l<";
	      EndMiner ="§8§l> §7Job ändern? (Miner, §6End§7) §8§l<";
	      ShowCaseInventoryName = "§8§l> §c§lShow-Case §8§l<";
	      SwitchInventoryNameMiner = "§8§l> §7Job ändern? §7(Miner) §8§l<";
	      SwitchInventoryNameFarmer = "§8§l> §7Job ändern? §7(Farmer) §8§l<";
	      SwitchInventoryNameBuddler = "§8§l> §7Job ändern? §7(Buddler) §8§l<";
	      SwitchInventoryNameFüller = "§8§l> §7Job ändern? §7(Füller) §8§l<";
	      ServerName = "BlockUnity";
	      SwitchInventoryNameReisender = "§8§l> §7Job ändern? §7(Reisender) §8§l<";
	      SwitchInventoryNameKiller = "§8§l> §7Job ändern? §7(Killer) §8§l<";
	   }
	   
	   
	
	   public HashMap<UUID, Double>lastDist = new HashMap<>();
	   @EventHandler
	   public void onMove(PlayerMoveEvent e) {
		   if(getJob(e.getPlayer()).equalsIgnoreCase("Reisender")) {
			   RegisteredServiceProvider<Economy> rsp = Durchrasten.getPlugin(Durchrasten.class).getServer().getServicesManager().getRegistration(Economy.class);
		       Economy econ = (Economy)rsp.getProvider();
			   if(lastDist.containsKey(e.getPlayer().getUniqueId())) {
				   Player p = e.getPlayer();
				   Double lastDis = lastDist.get(p.getUniqueId());
				   Double thisDis = e.getFrom().toVector().distance(e.getTo().toVector());
				   if(!(lastDis + thisDis >= 1.0)) {
					   lastDist.remove(p.getUniqueId());
					   lastDist.put(p.getUniqueId(), lastDis+thisDis);
					   return;
				   }
				   lastDist.remove(p.getUniqueId());
				   lastDist.put(p.getUniqueId(), lastDis+thisDis-1.0);
				   Double isMoney = 0.05D + getMulti1(p, "Reisender");
				   econ.depositPlayer(p, isMoney);
			        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7+§b" + isMoney + "$ §8- §7+§b1XP §8- §7Level §b" + this.getLevel(p, "Reisender")));
			        setEXP(p, "Reisender", getEXP(p, "Reisender")+1);
			        if(getEXP(p, "Reisender") >= getMaxEXP(p, "Reisender")) {
			        	setMaxEXP(p, "Reisender", getMaxEXP(p, "Reisender")*2);
			        	setEXP(p, "Reisender", 0);
			        	setLevel(p, "Reisender", getLevel(p, "Reisender")+1);
			        	setMulti1(p, "Reisender", getMulti1(p, "Reisender")+0.02);
			        	p.sendMessage(PREFIX+"§7Du bist nun Level §b"+getLevel(p, "Reisender")+"§7!");
			        	
			        }
				   return;
			   }else {
				   lastDist.put(e.getPlayer().getUniqueId(), e.getFrom().toVector().distance(e.getTo().toVector()));
			   }
		   }
	   }
	   @EventHandler
	   public void onClick(InventoryClickEvent e) {
	      Player p = (Player)e.getWhoClicked();
	      if (e.getView().getTitle().equalsIgnoreCase(JobsInventoryName)) {
	         e.setCancelled(true);
	         if (e.isLeftClick()) {
	            switch(e.getCurrentItem().getType()) {
	            case IRON_SWORD:
	                if (getJob(p) != null) {
	                    if (getJob(p).equalsIgnoreCase("-")) {
	                       p.closeInventory();
	                       setJob(p, "Killer");
	                       p.sendMessage(PREFIX + "§7Du bist jetzt §bKiller§7!");
	                    } else if (!getJob(p).equalsIgnoreCase("Killer")) {
	                       this.openKillerSwitch(p);
	                    } else {
	                       p.sendMessage(PREFIX + "§cDu bist bereits Killer!");
	                       p.closeInventory();
	                    }
	                 } else {
	                    p.closeInventory();
	                    setJob(p, "Killer");
	                    p.sendMessage(PREFIX + "§7Du bist jetzt §bKiller§7!");
	                 }
	            	break;
	            case MINECART:
	                if (getJob(p) != null) {
	                    if (getJob(p).equalsIgnoreCase("-")) {
	                       p.closeInventory();
	                       setJob(p, "Reisender");
	                       p.sendMessage(PREFIX + "§7Du bist jetzt §bReisender§7!");
	                    } else if (!getJob(p).equalsIgnoreCase("Reisender")) {
	                       this.openReisenderSwitch(p);
	                    } else {
	                       p.sendMessage(PREFIX + "§cDu bist bereits Reisender!");
	                       p.closeInventory();
	                    }
	                 } else {
	                    p.closeInventory();
	                    setJob(p, "Reisender");
	                    p.sendMessage(PREFIX + "§7Du bist jetzt §bReisender§7!");
	                 }
	            	break;
	            case IRON_SHOVEL:
	               if (getJob(p) != null) {
	                  if (getJob(p).equalsIgnoreCase("-")) {
	                     p.closeInventory();
	                     setJob(p, "Buddler");
	                     p.sendMessage(PREFIX + "§7Du bist jetzt §bBuddler§7!");
	                  } else if (!getJob(p).equalsIgnoreCase("Buddler")) {
	                     this.openBuddlerSwitch(p);
	                  } else {
	                     p.sendMessage(PREFIX + "§cDu bist bereits Buddler!");
	                     p.closeInventory();
	                  }
	               } else {
	                  p.closeInventory();
	                  setJob(p, "Buddler");
	                  p.sendMessage(PREFIX + "§7Du bist jetzt §bBuddler§7!");
	               }
	               break;
	            case IRON_PICKAXE:
	               if (getJob(p) != null) {
	                  if (getJob(p).equalsIgnoreCase("-")) {
	                     setJob(p, "Miner");
	                     p.sendMessage(PREFIX + "§7Du bist jetzt §Miner§7!");
	                     p.closeInventory();
	                  } else if (getJob(p).equalsIgnoreCase("Miner")) {
	                     p.closeInventory();
	                     p.sendMessage(PREFIX + "§cDu bist breits Miner!");
	                  } else {
	                     this.openMinerSwitch(p);
	                  }
	               } else {
	                  setJob(p, "Miner");
	                  p.closeInventory();
	                  p.sendMessage(PREFIX + "§7Du bist jetzt §bHolz Füller§7!");
	               }
	               break;
	            case IRON_AXE:
	               if (getJob(p) != null) {
	                  if (getJob(p).equalsIgnoreCase("-")) {
	                     setJob(p, "Füller");
	                     p.sendMessage(PREFIX + "§7Du bist jetzt §bHolz Füller§7!");
	                     p.closeInventory();
	                  } else if (getJob(p).equalsIgnoreCase("Füller")) {
	                     p.closeInventory();
	                     p.sendMessage(PREFIX + "§cDu bist breits Holz Füller!");
	                  } else {
	                     this.openFüllerSwitch(p);
	                  }
	               } else {
	                  setJob(p, "Füller");
	                  p.closeInventory();
	                  p.sendMessage(PREFIX + "§7Du bist jetzt §bHolz Füller§7!");
	               }
	               break;
	            case IRON_HOE:
	               if (getJob(p) != null) {
	                  if (getJob(p).equalsIgnoreCase("-")) {
	                     setJob(p, "Farmer");
	                     p.closeInventory();
	                     p.sendMessage(PREFIX + "§7Du bist jetzt §bFarmer§7!");
	                  } else if (!getJob(p).equalsIgnoreCase("Farmer")) {
	                     this.openFarmerSwitch(p);
	                  } else {
	                     p.sendMessage(PREFIX + "§cDu bist bereits Farmer!");
	                     p.closeInventory();
	                  }
	               } else {
	                  setJob(p, "Farmer");
	                  p.closeInventory();
	                  p.sendMessage(PREFIX + "§7Du bist jetzt §bFarmer§7!");
	               }
	            }
	         } else if (e.isRightClick()) {
	            switch(e.getCurrentItem().getType()) {
	            case IRON_SHOVEL:
	               this.openBuddlerShowCase(p);
	               break;
	            case IRON_PICKAXE:
	               this.openMinerShowCase(p);
	               break;
	            case IRON_AXE:
	               this.openFüllerShowCase(p);
	               break;
	            case IRON_HOE:
	               this.openFarmerShowCase(p);
	               break;
	            case MINECART:
	            	this.openReisenderShowCase(p);
	            	break;
	            case IRON_SWORD:
	            	this.openKillerShowCase(p);
	            	break;
	            }
	         }
	      } else if (e.getView().getTitle().equalsIgnoreCase(ShowCaseInventoryName)) {
	         e.setCancelled(true);
	      } else if (e.getView().getTitle().equalsIgnoreCase(SwitchInventoryNameMiner)) {
	         e.setCancelled(true);
	         switch(e.getCurrentItem().getType()) {
	         case LIME_STAINED_GLASS_PANE:
	            p.closeInventory();
	            p.sendMessage(PREFIX + "§7Du bist nun §bMiner§7!");
	            setJob(p, "Miner");
	            break;
	         case RED_STAINED_GLASS_PANE:
	            p.closeInventory();
	            p.sendMessage(PREFIX + "§cDu hast deinen Job nicht gewechselt!");
	         }
	      } else if (e.getView().getTitle().equalsIgnoreCase(SwitchInventoryNameBuddler)) {
	         e.setCancelled(true);
	         switch(e.getCurrentItem().getType()) {
	         case LIME_STAINED_GLASS_PANE:
	            p.closeInventory();
	            p.sendMessage(PREFIX + "§7Du bist nun §bBuddler§7!");
	            setJob(p, "Buddler");
	            break;
	         case RED_STAINED_GLASS_PANE:
	            p.closeInventory();
	            p.sendMessage(PREFIX + "§cDu hast deinen Job nicht gewechselt!");
	         }
	      } else if (e.getView().getTitle().equalsIgnoreCase(SwitchInventoryNameFarmer)) {
	         e.setCancelled(true);
	         switch(e.getCurrentItem().getType()) {
	         case LIME_STAINED_GLASS_PANE:
	            p.closeInventory();
	            p.sendMessage(PREFIX + "§7Du bist nun §bFarmer§7!");
	            setJob(p, "Farmer");
	            break;
	         case RED_STAINED_GLASS_PANE:
	            p.closeInventory();
	            p.sendMessage(PREFIX + "§cDu hast deinen Job nicht gewechselt!");
	         }
	      } else if (e.getView().getTitle().equalsIgnoreCase(SwitchInventoryNameFüller)) {
	         e.setCancelled(true);
	         switch(e.getCurrentItem().getType()) {
	         case LIME_STAINED_GLASS_PANE:
	            p.closeInventory();
	            p.sendMessage(PREFIX + "§7Du bist nun §bHolzFäller§7!");
	            setJob(p, "Füller");
	            break;
	         case RED_STAINED_GLASS_PANE:
	            p.closeInventory();
	            p.sendMessage(PREFIX + "§cDu hast deinen Job nicht gewechselt!");
	         }
	      }else if(e.getView().getTitle().equalsIgnoreCase(SwitchInventoryNameReisender)) {
	    	  e.setCancelled(true);
	    	  switch(e.getCurrentItem().getType()) {
	    	    case LIME_STAINED_GLASS_PANE:
	                p.closeInventory();
	                p.sendMessage(PREFIX + "§7Du bist nun §bReisender§7!");
	                setJob(p, "Reisender");
	                break;
	             case RED_STAINED_GLASS_PANE:
	                p.closeInventory();
	                p.sendMessage(PREFIX + "§cDu hast deinen Job nicht gewechselt!");
	             
	    	  }
	      }else if(e.getView().getTitle().equalsIgnoreCase(SwitchInventoryNameKiller)) {
	    	  e.setCancelled(true);
	    	  switch(e.getCurrentItem().getType()) {
	    	    case LIME_STAINED_GLASS_PANE:
	                p.closeInventory();
	                p.sendMessage(PREFIX + "§7Du bist nun §bKiller§7!");
	                setJob(p, "Killer");
	                break;
	             case RED_STAINED_GLASS_PANE:
	                p.closeInventory();
	                p.sendMessage(PREFIX + "§cDu hast deinen Job nicht gewechselt!");
	             
	    	  }
	      }

	   }
	   
	   @EventHandler
	   public void onKill(EntityDeathEvent e) {
		   RegisteredServiceProvider<Economy> rsp = Durchrasten.getPlugin(Durchrasten.class).getServer().getServicesManager().getRegistration(Economy.class);
	       Economy econ = (Economy)rsp.getProvider();
	       Vector EntityVector = e.getEntity().getLocation().toVector();
	       for(Player all : Bukkit.getOnlinePlayers()) {
	    	   Vector PlayerVector = e.getEntity().getLocation().toVector();
	    	   if(EntityVector.distance(PlayerVector) <= 6.0D) {
	    		   if(getJob(all).equalsIgnoreCase("Killer")) {
	    			   Player p = all;
	    			   Double isMoney = 3.0D + getMulti1(p, "Killer");
	    			   econ.depositPlayer(p, isMoney);
	    		        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7+§b" + isMoney + "$ §8- §7+§b23XP §8- §7Level §b" + this.getLevel(p, "Killer")));
	    		        setEXP(p, "Killer", getEXP(p, "Killer")+23);
	    		        if(getEXP(p, "Killer") >= getMaxEXP(p, "Killer")) {
	    		        	setMaxEXP(p, "Killer", getMaxEXP(p, "Killer")*2);
	    		        	setEXP(p, "Killer", 0);
	    		        	setLevel(p, "Killer", getLevel(p, "Killer")+1);
	    		        	setMulti1(p, "Killer", getMulti1(p, "Killer")+0.50D);
	    		        	p.sendMessage(PREFIX+"§7Du bist nun Level §b"+getLevel(p, "Killer")+"§7!");
	    		        	
	    		        }
	    		   }
	    	   }
	       }
	   }
	   
	   private void openKillerShowCase(Player p) {
		   Inventory inv = Bukkit.createInventory(null, 9*3,ShowCaseInventoryName);
		   for(int i = 0; i != 27; i++) {
			   inv.setItem(i, getFüller());
		   }
		   List<String>lore = new ArrayList<>();
		   lore.add("§7Level §b1 §8§l> §73$");
		   if(this.getLevel(p, "Killer") >1) {
			   Double LevelValue = 3.0D + getMulti1(p, "Killer");
			   lore.add("§7Level §b"+getLevel(p, "Killer")+" §8§l> §7"+LevelValue+"$");
		   }
		   inv.setItem(13, ItemBuilder.item(Material.SHEEP_SPAWN_EGG).setDisplayName("§7Mob / Tier").setLore(lore).build());
		   p.openInventory(inv);
	   }

	   private void openKillerSwitch(Player p) {
		   Inventory inv = Bukkit.createInventory(null, 9*3, SwitchInventoryNameKiller);
		   for(int i = 0;i != 27; i++) {
			   inv.setItem(i, getFüller());
		   }
		   inv.setItem(12, ItemBuilder.item(Material.LIME_STAINED_GLASS_PANE).setDisplayName("§aJa").build());
		   inv.setItem(14, ItemBuilder.item(Material.RED_STAINED_GLASS_PANE).setDisplayName("§cNein").build());
		   p.openInventory(inv);
	   }
	   private void openReisenderShowCase(Player p) {
		   Inventory inv = Bukkit.createInventory(null, 9*3,ShowCaseInventoryName);
		   for(int i = 0; i != 27; i++) {
			   inv.setItem(i, getFüller());
		   }
		   List<String>lore = new ArrayList<>();
		   lore.add("§7Level §b1 §8§l> §70.05$");
		   if(this.getLevel(p, "Reisender") >1) {
			   Double LevelValue = 0.05D + getMulti1(p, "Reisender");
			   lore.add("§7Level §b"+getLevel(p, "Reisender")+" §8§l> §7"+LevelValue+"$");
		   }
		   inv.setItem(13, ItemBuilder.item(Material.FEATHER).setDisplayName("§fPro Block").setLore(lore).build());
		   p.openInventory(inv);
		
	}
	private void openReisenderSwitch(Player p) {
		   Inventory inv = Bukkit.createInventory(null, 9*3, SwitchInventoryNameReisender);
		   for(int i = 0;i != 27; i++) {
			   inv.setItem(i, getFüller());
		   }
		   inv.setItem(12, ItemBuilder.item(Material.LIME_STAINED_GLASS_PANE).setDisplayName("§aJa").build());
		   inv.setItem(14, ItemBuilder.item(Material.RED_STAINED_GLASS_PANE).setDisplayName("§cNein").build());
		   p.openInventory(inv);
		
	}

	public void openBuddlerShowCase(Player p) {
	      Inventory inv = Bukkit.createInventory((InventoryHolder)null, 27, ShowCaseInventoryName);

	      for(int i = 0; i != 27; ++i) {
	         inv.setItem(i, getFüller());
	      }

	      ItemStack Grass = new ItemStack(Material.GRASS);
	      ItemMeta gm = Grass.getItemMeta();
	      gm.setDisplayName("§aGrass");
	      List<String> lore = new ArrayList();
	      lore.add("§7Level §b1 §8§l> §70.20$");
	      if (this.getLevel(p, "Buddler") > 1) {
	         Double LevelValue = 0.2D + this.getMulti1(p, "Buddler");
	         lore.add("§7Level §b" + this.getLevel(p, "Buddler") + " §8§l> §7" + LevelValue + "$");
	      }

	      gm.setLore(lore);
	      Grass.setItemMeta(gm);
	      inv.setItem(12, Grass);
	      ItemStack Grass1 = new ItemStack(Material.DIRT);
	      ItemMeta gm1 = Grass1.getItemMeta();
	      gm1.setDisplayName("§6Dirt");
	      List<String> lore1 = new ArrayList();
	      lore1.add("§7Level §b1 §8§l> §70.20$");
	      if (this.getLevel(p, "Buddler") > 1) {
	         Double LevelValue = 0.2D + this.getMulti1(p, "Buddler");
	         lore1.add("§7Level §b" + this.getLevel(p, "Buddler") + " §8§l> §7" + LevelValue + "$");
	      }

	      gm1.setLore(lore1);
	      Grass1.setItemMeta(gm1);
	      inv.setItem(13, Grass1);
	      ItemStack No = new ItemStack(Material.BARRIER);
	      ItemMeta nm = No.getItemMeta();
	      nm.setDisplayName("§c");
	      No.setItemMeta(nm);
	      inv.setItem(14, No);
	      p.openInventory(inv);
	      p.openInventory(inv);
	   }

	   public void openFarmerShowCase(Player p) {
	      Inventory inv = Bukkit.createInventory((InventoryHolder)null, 27, ShowCaseInventoryName);

	      for(int i = 0; i != 27; ++i) {
	         inv.setItem(i, getFüller());
	      }

	      ItemStack Wheat = new ItemStack(Material.WHEAT);
	      ItemMeta wm = Wheat.getItemMeta();
	      wm.setDisplayName("§6Weizen");
	      List<String> lore = new ArrayList();
	      lore.add("§7Level §b1 §8§l> §70.30$");
	      if (this.getLevel(p, "Farmer") > 1) {
	         Double LevelValue = 0.3D + this.getMulti1(p, "Farmer");
	         lore.add("§7Level §b" + this.getLevel(p, "Farmer") + " §8§l> §7" + LevelValue + "$");
	      }

	      wm.setLore(lore);
	      Wheat.setItemMeta(wm);
	      inv.setItem(11, Wheat);
	      ItemStack Wheat1 = new ItemStack(Material.CARROT);
	      ItemMeta wm1 = Wheat1.getItemMeta();
	      wm1.setDisplayName("§6Karotte");
	      List<String> lore1 = new ArrayList();
	      lore1.add("§7Level §b1 §8§l> §70.30$");
	      if (this.getLevel(p, "Farmer") > 1) {
	         Double LevelValue = 0.3D + this.getMulti1(p, "Farmer");
	         lore1.add("§7Level §b" + this.getLevel(p, "Farmer") + " §8§l> §7" + LevelValue + "$");
	      }

	      wm1.setLore(lore1);
	      Wheat1.setItemMeta(wm1);
	      inv.setItem(12, Wheat1);
	      ItemStack Wheat11 = new ItemStack(Material.POTATO);
	      ItemMeta wm11 = Wheat11.getItemMeta();
	      wm11.setDisplayName("§eKartoffel");
	      List<String> lore11 = new ArrayList();
	      lore11.add("§7Level §b1 §8§l> §70.30$");
	      if (this.getLevel(p, "Farmer") > 1) {
	         Double LevelValue = 0.3D + this.getMulti1(p, "Farmer");
	         lore11.add("§7Level §b" + this.getLevel(p, "Farmer") + " §8§l> §7" + LevelValue + "$");
	      }

	      wm11.setLore(lore11);
	      Wheat11.setItemMeta(wm11);
	      inv.setItem(13, Wheat11);
	      ItemStack Wheat111 = new ItemStack(Material.BEETROOT);
	      ItemMeta wm111 = Wheat111.getItemMeta();
	      wm111.setDisplayName("§cRote Beete");
	      List<String> lore111 = new ArrayList();
	      lore111.add("§7Level §b1 §8§l> §70.30$");
	      if (this.getLevel(p, "Farmer") > 1) {
	         Double LevelValue = 0.3D + this.getMulti1(p, "Farmer");
	         lore111.add("§7Level §b" + this.getLevel(p, "Farmer") + " §8§l> §7" + LevelValue + "$");
	      }

	      wm111.setLore(lore111);
	      Wheat111.setItemMeta(wm111);
	      inv.setItem(14, Wheat111);
	      ItemStack No = new ItemStack(Material.BARRIER);
	      ItemMeta nm = No.getItemMeta();
	      nm.setDisplayName("§c");
	      No.setItemMeta(nm);
	      inv.setItem(15, No);
	      p.openInventory(inv);
	   }

	   public void openFüllerShowCase(Player p) {
	      Inventory inv = Bukkit.createInventory((InventoryHolder)null, 27, ShowCaseInventoryName);

	      for(int i = 0; i != 27; ++i) {
	         inv.setItem(i, getFüller());
	      }

	      ItemStack Oak11 = new ItemStack(Material.OAK_LOG);
	      ItemMeta om11 = Oak11.getItemMeta();
	      List<String> ol11 = new ArrayList();
	      ol11.add("§7Level §b1 §8§l> §70.30$");
	      Double LevelValue11 = 0.3D + this.getMulti1(p, "Füller");
	      if (this.getLevel(p, "Füller") > 1) {
	         ol11.add("§7Level §b" + this.getLevel(p, "Füller") + "§8 § §7" + LevelValue11 + "$");
	      }

	      om11.setLore(ol11);
	      om11.setDisplayName("§6Holz");
	      Oak11.setItemMeta(om11);
	      inv.setItem(13, Oak11);
	      p.openInventory(inv);
	   }

	   public void openMinerShowCase(Player p) {
	      Inventory inv = Bukkit.createInventory((InventoryHolder)null, 27, ShowCaseInventoryName);

	      for(int i = 0; i != 27; ++i) {
	         inv.setItem(i, getFüller());
	      }

	      ItemStack Stone = new ItemStack(Material.STONE);
	      ItemMeta sm = Stone.getItemMeta();
	      sm.setDisplayName("§7Stein");
	      List<String> lore = new ArrayList();
	      lore.add("§7Level §B1 §8§l> §70.20$");
	      if (this.getLevel(p, "Miner") > 1) {
	         Double LevelValue = 0.2D + this.getMulti1(p, "Miner");
	         lore.add("§7Level §b" + this.getLevel(p, "Miner") + "§8 § §7" + LevelValue + "$");
	      }

	      sm.setLore(lore);
	      Stone.setItemMeta(sm);
	      inv.setItem(10, Stone);
	      ItemStack Stone1 = new ItemStack(Material.COAL_ORE);
	      ItemMeta sm1 = Stone1.getItemMeta();
	      sm1.setDisplayName("§0Kohle");
	      List<String> lore1 = new ArrayList();
	      lore1.add("§7Level §B1 §8§l> §70.25$");
	      if (this.getLevel(p, "Miner") > 1) {
	         Double LevelValue = 0.25D + this.getMulti1(p, "Miner");
	         lore1.add("§7Level §b" + this.getLevel(p, "Miner") + "§8 § §7" + LevelValue + "$");
	      }

	      sm1.setLore(lore1);
	      Stone1.setItemMeta(sm1);
	      inv.setItem(11, Stone1);
	      ItemStack Stone11 = new ItemStack(Material.IRON_ORE);
	      ItemMeta sm11 = Stone11.getItemMeta();
	      sm11.setDisplayName("§7Eisen");
	      List<String> lore11 = new ArrayList();
	      lore11.add("§7Level §B1 §8§l> §70.30$");
	      if (this.getLevel(p, "Miner") > 1) {
	         Double LevelValue = 0.3D + this.getMulti2(p, "Miner");
	         lore11.add("§7Level §b" + this.getLevel(p, "Miner") + "§8 § §7" + LevelValue + "$");
	      }

	      sm11.setLore(lore11);
	      Stone11.setItemMeta(sm11);
	      inv.setItem(12, Stone11);
	      ItemStack Stone111 = new ItemStack(Material.GOLD_ORE);
	      ItemMeta sm111 = Stone111.getItemMeta();
	      sm111.setDisplayName("§eGold");
	      List<String> lore111 = new ArrayList();
	      lore111.add("§7Level §B1 §8§l> §70.35$");
	      if (this.getLevel(p, "Miner") > 1) {
	         Double LevelValue = 0.35D + this.getMulti2(p, "Miner");
	         lore111.add("§7Level §b" + this.getLevel(p, "Miner") + "§8 § §7" + LevelValue + "$");
	      }

	      sm111.setLore(lore111);
	      Stone111.setItemMeta(sm111);
	      inv.setItem(13, Stone111);
	      ItemStack Stone1111 = new ItemStack(Material.LAPIS_ORE);
	      ItemMeta sm1111 = Stone1111.getItemMeta();
	      sm1111.setDisplayName("§bLapis");
	      List<String> lore1111 = new ArrayList();
	      lore1111.add("§7Level §B1 §8§l> §70.40$");
	      if (this.getLevel(p, "Miner") > 1) {
	         Double LevelValue = 0.4D + this.getMulti2(p, "Miner");
	         lore1111.add("§7Level §b" + this.getLevel(p, "Miner") + "§8 § §7" + LevelValue + "$");
	      }

	      sm1111.setLore(lore1111);
	      Stone1111.setItemMeta(sm1111);
	      inv.setItem(14, Stone1111);
	      ItemStack Stone11111 = new ItemStack(Material.REDSTONE_ORE);
	      ItemMeta sm11111 = Stone11111.getItemMeta();
	      sm11111.setDisplayName("§4Redstone");
	      List<String> lore11111 = new ArrayList();
	      lore11111.add("§7Level §B1 §8§l> §70.40$");
	      if (this.getLevel(p, "Miner") > 1) {
	         Double LevelValue = 0.4D + this.getMulti2(p, "Miner");
	         lore11111.add("§7Level §b" + this.getLevel(p, "Miner") + "§8 § §7" + LevelValue + "$");
	      }

	      sm11111.setLore(lore11111);
	      Stone11111.setItemMeta(sm11111);
	      inv.setItem(15, Stone11111);
	      ItemStack Stone111111 = new ItemStack(Material.DIAMOND_ORE);
	      ItemMeta sm111111 = Stone111111.getItemMeta();
	      sm111111.setDisplayName("§bDias");
	      List<String> lore111111 = new ArrayList();
	      lore111111.add("§7Level §B1 §8§l> §70.50$");
	      if (this.getLevel(p, "Miner") > 1) {
	         Double LevelValue = 0.5D + this.getMulti3(p, "Miner");
	         lore111111.add("§7Level §b" + this.getLevel(p, "Miner") + "§8 § §7" + LevelValue + "$");
	      }

	      sm111111.setLore(lore111111);
	      Stone111111.setItemMeta(sm111111);
	      inv.setItem(16, Stone111111);
	      p.openInventory(inv);
	   }

	   @EventHandler
	   public void onBreak(BlockBreakEvent e) {
	      Player p = e.getPlayer();
	      if (p.getGameMode() != GameMode.CREATIVE && p.getWorld() != null && getJob(p) != null) {
	         RegisteredServiceProvider<Economy> rsp = Durchrasten.getPlugin(Durchrasten.class).getServer().getServicesManager().getRegistration(Economy.class);
	         Economy econ = (Economy)rsp.getProvider();
	         Double MoneyPlus;
	         if (getJob(p).equalsIgnoreCase("Miner")) {
	            switch(e.getBlock().getType()) {
	            case STONE:
	               MoneyPlus = 0.2D + this.getMulti1(p, "Miner");
	               p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7+§b" + MoneyPlus + "$§8 - §7+§b2XP §8- §7Level §b" + this.getLevel(p, "Miner")));
	               econ.depositPlayer(p, MoneyPlus);
	               this.setEXP(p, "Miner", this.getEXP(p, "Miner") + 2);
	               if (this.getEXP(p, "Miner") >= this.getMaxEXP(p, "Miner")) {
	                  this.setLevel(p, "Miner", this.getLevel(p, "Miner") + 1);
	                  p.sendMessage(PREFIX + "§7Du bist nun Level §b" + this.getLevel(p, "Miner") + "§7!");
	                  this.setMulti1(p, "Miner", this.getMulti1(p, "Miner") + 0.02D);
	                  this.setMulti2(p, "Miner", this.getMulti2(p, "Miner") + 0.03D);
	                  this.setMulti3(p, "Miner", this.getMulti3(p, "Miner") + 0.05D);
	                  this.setEXP(p, "Miner", 0);
	                  this.setMaxEXP(p, "Miner", this.getMaxEXP(p, "Miner") * 2);
	               }
	               break;
	            case COAL_ORE:
	               Double MoneyPlus111 = 0.25D + this.getMulti2(p, "Miner");
	               p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7+§b" + MoneyPlus111 + "$§8 - §7+§b5XP §8- §7Level §b" + this.getLevel(p, "Miner")));
	               econ.depositPlayer(p, MoneyPlus111);
	               this.setEXP(p, "Miner", this.getEXP(p, "Miner") + 5);
	               if (this.getEXP(p, "Miner") >= this.getMaxEXP(p, "Miner")) {
	                  this.setLevel(p, "Miner", this.getLevel(p, "Miner") + 1);
	                  p.sendMessage(PREFIX + "§7Du bist nun Level §b" + this.getLevel(p, "Miner") + "§7!");
	                  this.setMulti1(p, "Miner", this.getMulti1(p, "Miner") + 0.02D);
	                  this.setMulti2(p, "Miner", this.getMulti2(p, "Miner") + 0.03D);
	                  this.setMulti3(p, "Miner", this.getMulti3(p, "Miner") + 0.05D);
	                  this.setEXP(p, "Miner", 0);
	                  this.setMaxEXP(p, "Miner", this.getMaxEXP(p, "Miner") * 2);
	               }
	               break;
	            case IRON_ORE:
	               Double MoneyPlus11 = 0.3D + this.getMulti2(p, "Miner");
	               p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7+§b" + MoneyPlus11 + "$§8 - §7+§b4XP §8- §7Level §b" + this.getLevel(p, "Miner")));
	               econ.depositPlayer(p, MoneyPlus11);
	               this.setEXP(p, "Miner", this.getEXP(p, "Miner") + 4);
	               if (this.getEXP(p, "Miner") >= this.getMaxEXP(p, "Miner")) {
	                  this.setLevel(p, "Miner", this.getLevel(p, "Miner") + 1);
	                  p.sendMessage(PREFIX + "§7Du bist nun Level §b" + this.getLevel(p, "Miner") + "§7!");
	                  this.setMulti1(p, "Miner", this.getMulti1(p, "Miner") + 0.02D);
	                  this.setMulti2(p, "Miner", this.getMulti2(p, "Miner") + 0.03D);
	                  this.setMulti3(p, "Miner", this.getMulti3(p, "Miner") + 0.05D);
	                  this.setEXP(p, "Miner", 0);
	                  this.setMaxEXP(p, "Miner", this.getMaxEXP(p, "Miner") * 2);
	               }
	               break;
	            case GOLD_ORE:
	               Double MoneyPlus1 = 0.25D + this.getMulti1(p, "Miner");
	               p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7+§b" + MoneyPlus1 + "$§8 - §7+§b3XP §8- §7Level §b" + this.getLevel(p, "Miner")));
	               econ.depositPlayer(p, MoneyPlus1);
	               this.setEXP(p, "Miner", this.getEXP(p, "Miner") + 3);
	               if (this.getEXP(p, "Miner") >= this.getMaxEXP(p, "Miner")) {
	                  this.setLevel(p, "Miner", this.getLevel(p, "Miner") + 1);
	                  p.sendMessage(PREFIX + "§7Du bist nun Level §b" + this.getLevel(p, "Miner") + "§7!");
	                  this.setMulti1(p, "Miner", this.getMulti1(p, "Miner") + 0.02D);
	                  this.setMulti2(p, "Miner", this.getMulti2(p, "Miner") + 0.03D);
	                  this.setMulti3(p, "Miner", this.getMulti3(p, "Miner") + 0.05D);
	                  this.setEXP(p, "Miner", 0);
	                  this.setMaxEXP(p, "Miner", this.getMaxEXP(p, "Miner") * 2);
	               }
	               break;
	            case LAPIS_ORE:
	               Double MoneyPlus1111 = 0.4D + this.getMulti2(p, "Miner");
	               p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7+§b" + MoneyPlus1111 + "$§8 - §7+§b6XP §8- §7Level §b" + this.getLevel(p, "Miner")));
	               econ.depositPlayer(p, MoneyPlus1111);
	               this.setEXP(p, "Miner", this.getEXP(p, "Miner") + 6);
	               if (this.getEXP(p, "Miner") >= this.getMaxEXP(p, "Miner")) {
	                  this.setLevel(p, "Miner", this.getLevel(p, "Miner") + 1);
	                  p.sendMessage(PREFIX + "§7Du bist nun Level §b" + this.getLevel(p, "Miner") + "§7!");
	                  this.setMulti1(p, "Miner", this.getMulti1(p, "Miner") + 0.02D);
	                  this.setMulti2(p, "Miner", this.getMulti2(p, "Miner") + 0.03D);
	                  this.setMulti3(p, "Miner", this.getMulti3(p, "Miner") + 0.05D);
	                  this.setEXP(p, "Miner", 0);
	                  this.setMaxEXP(p, "Miner", this.getMaxEXP(p, "Miner") * 2);
	               }
	               break;
	            case DIAMOND_ORE:
	               Double MoneyPlus111111 = 0.5D + this.getMulti3(p, "Miner");
	               p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7+§b" + MoneyPlus111111 + "$§8 - §7+§b10XP §8- §7Level §b" + this.getLevel(p, "Miner")));
	               econ.depositPlayer(p, MoneyPlus111111);
	               this.setEXP(p, "Miner", this.getEXP(p, "Miner") + 10);
	               if (this.getEXP(p, "Miner") >= this.getMaxEXP(p, "Miner")) {
	                  this.setLevel(p, "Miner", this.getLevel(p, "Miner") + 1);
	                  p.sendMessage(PREFIX + "§7Du bist nun Level §b" + this.getLevel(p, "Miner") + "§7!");
	                  this.setMulti1(p, "Miner", this.getMulti1(p, "Miner") + 0.02D);
	                  this.setMulti2(p, "Miner", this.getMulti2(p, "Miner") + 0.03D);
	                  this.setMulti3(p, "Miner", this.getMulti3(p, "Miner") + 0.05D);
	                  this.setEXP(p, "Miner", 0);
	                  this.setMaxEXP(p, "Miner", this.getMaxEXP(p, "Miner") * 2);
	               }
	               break;
	            case REDSTONE_ORE:
	               Double MoneyPlus11111 = 0.4D + this.getMulti2(p, "Miner");
	               p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7+§b" + MoneyPlus11111 + "$§8 - §7+§b6XP §8- §7Level §b" + this.getLevel(p, "Miner")));
	               econ.depositPlayer(p, MoneyPlus11111);
	               this.setEXP(p, "Miner", this.getEXP(p, "Miner") + 6);
	               if (this.getEXP(p, "Miner") >= this.getMaxEXP(p, "Miner")) {
	                  this.setLevel(p, "Miner", this.getLevel(p, "Miner") + 1);
	                  p.sendMessage(PREFIX + "§7Du bist nun Level §b" + this.getLevel(p, "Miner") + "§7!");
	                  this.setMulti1(p, "Miner", this.getMulti1(p, "Miner") + 0.02D);
	                  this.setMulti2(p, "Miner", this.getMulti2(p, "Miner") + 0.03D);
	                  this.setMulti3(p, "Miner", this.getMulti3(p, "Miner") + 0.05D);
	                  this.setEXP(p, "Miner", 0);
	                  this.setMaxEXP(p, "Miner", this.getMaxEXP(p, "Miner") * 2);
	               }
	            }
	         } else if (getJob(p).equalsIgnoreCase("Füller")) {
	            switch(e.getBlock().getType()) {
	            case BIRCH_LOG:
	            case DARK_OAK_LOG:
	            case ACACIA_LOG:
	            case JUNGLE_LOG:
	            case OAK_LOG:
	               MoneyPlus = 0.3D + this.getMulti1(p, "Füller");
	               p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7+§b" + MoneyPlus + "$ §8- §7+§b4XP §8- §7Level §b" + this.getLevel(p, "Füller")));
	               econ.depositPlayer(p, MoneyPlus);
	               this.setEXP(p, "Füller", this.getEXP(p, "Füller") + 4);
	               if (this.getEXP(p, "Füller") >= this.getMaxEXP(p, "Füller")) {
	                  this.setLevel(p, "Füller", this.getLevel(p, "Füller") + 1);
	                  p.sendMessage(PREFIX + "§7Du bist nun Level §b" + this.getLevel(p, "Füller") + "§7!");
	                  this.setMulti1(p, "Füller", this.getMulti1(p, "Füller") + 0.03D);
	                  this.setEXP(p, "Füller", 0);
	                  this.setMaxEXP(p, "Füller", this.getMaxEXP(p, "Füller") * 2);
	               }
	            }
	         } else if (getJob(p).equalsIgnoreCase("Farmer")) {
	            switch(e.getBlock().getType()) {
	            case POTATO:
	            case CARROT:
	            case WHEAT:
	            case BEETROOT:
	               MoneyPlus = 0.3D + this.getMulti1(p, "Farmer");
	               p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7+§b" + MoneyPlus + "$ §8- §7+§b7XP §8- §7Level §b" + this.getLevel(p, "Farmer")));
	               econ.depositPlayer(p, MoneyPlus);
	               this.setEXP(p, "Farmer", this.getEXP(p, "Farmer") + 4);
	               if (this.getEXP(p, "Farmer") >= this.getMaxEXP(p, "Farmer")) {
	                  this.setLevel(p, "Farmer", this.getLevel(p, "Farmer") + 1);
	                  p.sendMessage(PREFIX + "§7Du bist nun Level §b" + this.getLevel(p, "Farmer") + "§7!");
	                  this.setMulti1(p, "Farmer", this.getMulti1(p, "Farmer") + 0.03D);
	                  this.setEXP(p, "Farmer", 0);
	                  this.setMaxEXP(p, "Farmer", this.getMaxEXP(p, "Farmer") * 2);
	               }
	            }
	         } else if (getJob(p).equalsIgnoreCase("Buddler")) {
	            switch(e.getBlock().getType()) {
	            case DIRT:
	            case GRASS:
	               MoneyPlus = 0.2D + this.getMulti1(p, "Buddler");
	               p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7+§b" + MoneyPlus + "$ §8- §7+§b7XP §8- §7Level §b" + this.getLevel(p, "Buddler")));
	               econ.depositPlayer(p, MoneyPlus);
	               this.setEXP(p, "Buddler", this.getEXP(p, "Buddler") + 4);
	               if (this.getEXP(p, "Buddler") >= this.getMaxEXP(p, "Buddler")) {
	                  this.setLevel(p, "Buddler", this.getLevel(p, "Buddler") + 1);
	                  p.sendMessage(PREFIX + "§7Du bist nun Level §b" + this.getLevel(p, "Buddler") + "§7!");
	                  this.setMulti1(p, "Buddler", this.getMulti1(p, "Buddler") + 0.03D);
	                  this.setEXP(p, "Buddler", 0);
	                  this.setMaxEXP(p, "Buddler", this.getMaxEXP(p, "Buddler") * 2);
	               }
	            }
	         }
	      }

	   }

	   @EventHandler
	   public void onJoin(PlayerJoinEvent e) {
	      Player p = e.getPlayer();
	      if (getJob(p) == null) {
	         this.setEXP(p, "Buddler", 0);
	         this.setEXP(p, "Farmer", 0);
	         this.setEXP(p, "Füller", 0);
	         this.setEXP(p, "Miner", 0);
	         this.setMaxEXP(p, "Buddler", 150);
	         this.setMaxEXP(p, "Farmer", 150);
	         this.setMaxEXP(p, "Füller", 150);
	         this.setMaxEXP(p, "Miner", 150);
	         setMaxEXP(p, "Reisender",150);
	         setLevel(p, "Reisender", 1);
	         setEXP(p, "Reisender", 0);
	         setMaxEXP(p, "Killer",150);
	         setLevel(p, "Killer", 1);
	         setEXP(p, "Killer", 0);
	         setMulti1(p, "Killer", 0.0D);
	         this.setLevel(p, "Buddler", 1);
	         this.setLevel(p, "Farmer", 1);
	         this.setLevel(p, "Miner", 1);
	         this.setLevel(p, "Füller", 1);
	         setJob(p, "-");
	         this.setMulti1(p, "Buddler", 0.0D);
	         this.setMulti1(p, "Farmer", 0.0D);
	         this.setMulti1(p, "Füller", 0.0D);
	         this.setMulti1(p, "Miner", 0.0D);
	         this.setMulti2(p, "Buddler", 0.0D);
	         this.setMulti2(p, "Farmer", 0.0D);
	         this.setMulti2(p, "Füller", 0.0D);
	         this.setMulti2(p, "Miner", 0.0D);
	         this.setMulti3(p, "Buddler", 0.0D);
	         this.setMulti3(p, "Farmer", 0.0D);
	         this.setMulti3(p, "Füller", 0.0D);
	         this.setMulti3(p, "Miner", 0.0D);
	         this.setMulti1(p, "Reisender", 0.0D);
	      }

	   }

	   public void openBuddlerSwitch(Player p) {
	      Inventory inv = Bukkit.createInventory((InventoryHolder)null, 27, SwitchInventoryNameBuddler);

	      for(int i = 0; i != 27; ++i) {
	         inv.setItem(i, getFüller());
	      }
	      inv.setItem(12, ItemBuilder.item(Material.LIME_STAINED_GLASS_PANE).setDisplayName("§aJa").build());
		  inv.setItem(14, ItemBuilder.item(Material.RED_STAINED_GLASS_PANE).setDisplayName("§cNein").build());
	      p.openInventory(inv);
	   }

	   public void openFüllerSwitch(Player p) {
	      Inventory inv = Bukkit.createInventory((InventoryHolder)null, 27, SwitchInventoryNameFüller);

	      for(int i = 0; i != 27; ++i) {
	         inv.setItem(i, getFüller());
	      }

	      inv.setItem(12, ItemBuilder.item(Material.LIME_STAINED_GLASS_PANE).setDisplayName("§aJa").build());
		  inv.setItem(14, ItemBuilder.item(Material.RED_STAINED_GLASS_PANE).setDisplayName("§cNein").build());
	      p.openInventory(inv);
	   }

	   public void openMinerSwitch(Player p) {
	      Inventory inv = Bukkit.createInventory((InventoryHolder)null, 27, SwitchInventoryNameMiner);

	      for(int i = 0; i != 27; ++i) {
	         inv.setItem(i, getFüller());
	      }

	      inv.setItem(12, ItemBuilder.item(Material.LIME_STAINED_GLASS_PANE).setDisplayName("§aJa").build());
		  inv.setItem(14, ItemBuilder.item(Material.RED_STAINED_GLASS_PANE).setDisplayName("§cNein").build());
	      p.openInventory(inv);
	   }

	   public void openFarmerSwitch(Player p) {
	      Inventory inv = Bukkit.createInventory((InventoryHolder)null, 27, SwitchInventoryNameFarmer);

	      for(int i = 0; i != 27; ++i) {
	         inv.setItem(i, getFüller());
	      }

	      inv.setItem(12, ItemBuilder.item(Material.LIME_STAINED_GLASS_PANE).setDisplayName("§aJa").build());
		  inv.setItem(14, ItemBuilder.item(Material.RED_STAINED_GLASS_PANE).setDisplayName("§cNein").build());
	      p.openInventory(inv);
	   }

	   public static void saveCFG() {
	      try {
	         cfg.save(file);
	      } catch (IOException var1) {
	      }

	   }

	   public static String getJob(Player p) {
	      try {
	         return cfg.getString("Spieler." + p.getUniqueId() + ".Job");
	      } catch (Exception var2) {
	         return null;
	      }
	   }

	   public int getLevel(Player p, String Job) {
	      return cfg.getInt("Spieler." + p.getUniqueId() + ".JobLevel." + Job);
	   }

	   public void setEXP(Player p, String Job, int EXP) {
	      cfg.set("Spieler." + p.getUniqueId() + ".JobEXP." + Job, EXP);
	      saveCFG();
	   }

	   public void setMaxEXP(Player p, String Job, int EXP) {
	      cfg.set("Spieler." + p.getUniqueId() + ".MaxEXP." + Job, EXP);
	      saveCFG();
	   }

	   public int getMaxEXP(Player p, String Job) {
	      return cfg.getInt("Spieler." + p.getUniqueId() + ".MaxEXP." + Job);
	   }

	   public void setLevel(Player p, String Job, int Level) {
	      cfg.set("Spieler." + p.getUniqueId() + ".JobLevel." + Job, Level);
	      saveCFG();
	   }

	   public double getMulti1(Player p, String Job) {
	      return makeGladeValus(cfg.getDouble("Spieler." + p.getUniqueId() + ".Multiplikator.1." + Job));
	   }

	   public double getMulti2(Player p, String Job) {
	      return makeGladeValus(cfg.getDouble("Spieler." + p.getUniqueId() + ".Multiplikator.2." + Job));
	   }

	   public double getMulti3(Player p, String Job) {
	      return makeGladeValus(cfg.getDouble("Spieler." + p.getUniqueId() + ".Multiplikator.3." + Job));
	   }

	   public void setMulti1(Player p, String Job, Double Multi) {
	      cfg.set("Spieler." + p.getUniqueId() + ".Multiplikator.1." + Job, Multi);
	      saveCFG();
	   }

	   public void setMulti2(Player p, String Job, Double Multi) {
	      cfg.set("Spieler." + p.getUniqueId() + ".Multiplikator.2." + Job, Multi);
	      saveCFG();
	   }

	   public void setMulti3(Player p, String Job, Double Multi) {
	      cfg.set("Spieler." + p.getUniqueId() + ".Multiplikator.3." + Job, Multi);
	      saveCFG();
	   }

	   public int getEXP(Player p, String Job) {
	      return cfg.getInt("Spieler." + p.getUniqueId() + ".JobEXP." + Job);
	   }

	   public void setMulti3(Player p, Double Multi) {
	      cfg.set("Spieler." + p.getUniqueId() + ".Multiplikator.3", Multi);
	      saveCFG();
	   }

	   public static void setJob(Player p, String Job) {
	      cfg.set("Spieler." + p.getUniqueId() + ".Job", Job);
	      saveCFG();
	   }

	   public static ItemStack getFüller() {
	      ItemStack Füller = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 0, (short) 15);
	      ItemMeta fm = Füller.getItemMeta();
	      fm.setDisplayName("§c");
	      Füller.setItemMeta(fm);
	      return Füller;
	   }

	   public static double makeGladeValus(double beforeValue) {
	      String beforeKommaString = String.valueOf(beforeValue);
	      if (beforeKommaString.length() <= 4) {
	         return beforeValue;
	      } else {
	         String beforeKomma = beforeKommaString.substring(0, 1);
	         String nextKomma = null;
	         nextKomma = beforeKommaString.substring(2, 4);
	         if (nextKomma.length() <= 2) {
	            return beforeValue;
	         } else {
	            double beforeKommaDouble = Double.parseDouble(beforeKomma);
	            double nextKommaDouble = Double.parseDouble("0." + nextKomma);
	            double gladeNumber = beforeKommaDouble + nextKommaDouble;
	            String gns = String.valueOf(gladeNumber);
	            return gns.length() <= 4 ? gladeNumber : Double.parseDouble(gns.substring(0, 4));
	         }
	      }
	   }

	   private static void log(String s) {
	      Bukkit.getConsoleSender().sendMessage(s);
	   }

	   // $FF: synthetic method
	   static int[] $SWITCH_TABLE$org$bukkit$Material() {
		return $SWITCH_TABLE$org$bukkit$Material;
	      // $FF: Couldn't be decompiled
	   }
	}