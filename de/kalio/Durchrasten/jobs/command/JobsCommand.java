package de.kalio.Durchrasten.jobs.command;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.kalio.Durchrasten.jobs.Jobs;
import de.kalio.Durchrasten.jobs.utils.ItemBuilder;



public class JobsCommand implements CommandExecutor {
   public boolean onCommand(CommandSender cs, Command cmd, String arg2, String[] args) {
      if (cs instanceof Player) {
         Player p = (Player)cs;
         Inventory inv;
         int i;
         ItemMeta fm;
         ItemStack Buddler;
         ItemMeta bm;
         ItemStack Miner;
         ItemMeta mm;
         ItemStack Farmer;
         ItemMeta fm1;
         ItemStack Fäller;
         if (args.length == 0) {
            inv = Bukkit.createInventory((InventoryHolder)null, 27, Jobs.JobsInventoryName);

            for(i = 0; i != 27; ++i) {
               inv.setItem(i, Jobs.getFüller());
            }

            Fäller = new ItemStack(Material.IRON_AXE);
            fm = Fäller.getItemMeta();
            fm.setDisplayName("§6Holz Fäller");
            Fäller.setItemMeta(fm);
            inv.setItem(10, Fäller);
            Buddler = new ItemStack(Material.IRON_SHOVEL);
            bm = Buddler.getItemMeta();
            bm.setDisplayName("§6Buddler");
            Buddler.setItemMeta(bm);
            inv.setItem(12, Buddler);
            Miner = new ItemStack(Material.IRON_PICKAXE);
            mm = Miner.getItemMeta();
            mm.setDisplayName("§6Miner");
            Miner.setItemMeta(mm);
            inv.setItem(14, Miner);
            Farmer = new ItemStack(Material.IRON_HOE);
            fm1 = Farmer.getItemMeta();
            fm1.setDisplayName("§6Farmer");
            Farmer.setItemMeta(fm1);
            inv.setItem(16, Farmer);
            inv.setItem(11, ItemBuilder.item(Material.MINECART).setDisplayName("§6Reisender").build());
            inv.setItem(15, ItemBuilder.item(Material.IRON_SWORD).setDisplayName("§6Killer").build());
            p.openInventory(inv);
         } else if (!args[0].equalsIgnoreCase("browse") && !args[0].equalsIgnoreCase("join")) {
            if (args[0].equalsIgnoreCase("leave")) {
               if (Jobs.getJob(p) != null) {
                  if (!Jobs.getJob(p).equalsIgnoreCase("-")) {
                     Jobs.setJob(p, "-");
                     p.sendMessage(Jobs.PREFIX + "§cDu hast deinen Job verlassen!");
                  } else {
                     p.sendMessage(Jobs.PREFIX + "§cDu hast keinen Job!");
                  }
               } else {
                  p.sendMessage(Jobs.PREFIX + "§cDu hast keinen Job!");
               }
            }
         } else {
            inv = Bukkit.createInventory((InventoryHolder)null, 27, Jobs.JobsInventoryName);

            for(i = 0; i != 27; ++i) {
               inv.setItem(i, Jobs.getFüller());
            }

            Fäller = new ItemStack(Material.IRON_AXE);
            fm = Fäller.getItemMeta();
            fm.setDisplayName("§6Holz F§ller");
            Fäller.setItemMeta(fm);
            inv.setItem(10, Fäller);
            Buddler = new ItemStack(Material.IRON_SHOVEL);
            bm = Buddler.getItemMeta();
            bm.setDisplayName("§6Buddler");
            Buddler.setItemMeta(bm);
            inv.setItem(12, Buddler);
            Miner = new ItemStack(Material.IRON_PICKAXE);
            mm = Miner.getItemMeta();
            mm.setDisplayName("§6Miner");
            Miner.setItemMeta(mm);
            inv.setItem(14, Miner);
            Farmer = new ItemStack(Material.IRON_HOE);
            fm1 = Farmer.getItemMeta();
            fm1.setDisplayName("§6Farmer");
            Farmer.setItemMeta(fm1);
            inv.setItem(16, Farmer);
            inv.setItem(11, ItemBuilder.item(Material.MINECART).setDisplayName("§6Reisender").build());
            inv.setItem(15, ItemBuilder.item(Material.IRON_SWORD).setDisplayName("§6Killer").build());
         
            p.openInventory(inv);
         }
      } else {
         cs.sendMessage(Jobs.PREFIX + "§cDu musst ein Spieler sein!");
      }

      return false;
   }
}
