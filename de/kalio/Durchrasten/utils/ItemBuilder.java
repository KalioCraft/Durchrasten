package de.kalio.Durchrasten.utils;


import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {
	
  public static ItemStack createItem(Material material, String displayName, Integer amount, String[] lore) {
	  
    ItemStack itemStack = new ItemStack(material, amount.intValue());
    ItemMeta itemMeta = itemStack.getItemMeta();
    itemMeta.setDisplayName(displayName);
    if(lore != null) {
    	itemMeta.setLore(Arrays.asList(lore));
    }
    itemStack.setItemMeta(itemMeta);
    
    return itemStack;
  }
  
  public static ItemStack createItemWD(Material material, String displayName, Integer amount, int Data, String[] lore) {
	  
	    ItemStack itemStack = new ItemStack(material, amount.intValue(), (byte) Data);
	    ItemMeta itemMeta = itemStack.getItemMeta();
	    itemMeta.setDisplayName(displayName);
	    itemMeta.setLore(Arrays.asList(lore));
	    itemStack.setItemMeta(itemMeta);
	    
	    return itemStack;
	  }
  
  public static ItemStack createItemWDOL(Material material, String displayName, Integer amount, int Data) {
	  
	    ItemStack itemStack = new ItemStack(material, amount.intValue(), (byte) Data);
	    ItemMeta itemMeta = itemStack.getItemMeta();
	    itemMeta.setDisplayName(displayName);
	    itemStack.setItemMeta(itemMeta);
	    
	    return itemStack;
	  }
  
  public static ItemStack createItemOL(Material material, String displayName, Integer amount) {
    ItemStack itemStack = new ItemStack(material, amount.intValue());
    ItemMeta itemMeta = itemStack.getItemMeta();
    itemMeta.setDisplayName(displayName);
    itemStack.setItemMeta(itemMeta);
    
    return itemStack;
  }
  
  public static ItemStack createItemON(Material material) {
	  ItemStack itemStack = new ItemStack(material);
	    
	    return itemStack;
	  }
  
  public static Inventory InvFüller(Inventory inv, ItemStack item, Integer Reihen) {
	  
	  Integer i = -1;
	  
	  while(i < Reihen*9-1) {
		  
		  i++;
		  
		  if(inv.getItem(i) == null) {
		  	inv.setItem(i, item);
		  }
	  }
	  
	  return inv;
  }
  
}
