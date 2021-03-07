package de.kalio.Durchrasten.jobs.utils;


import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ItemBuilder {
   private final ItemStack item;

   public ItemBuilder(Material material, int amount) {
      this.item = new ItemStack(material, amount);
   }

   public static ItemBuilder item(Material material) {
      return item(material, 1);
   }

   public static ItemBuilder item(Material material, int amount) {
      return new ItemBuilder(material, amount);
   }

   public static ItemBuilder item(Material material, int amount, short damage) {
      ItemBuilder builder = new ItemBuilder(material, amount);
      return builder.setDurability(damage);
   }

   public ItemBuilder setDisplayName(String name) {
      ItemMeta meta = this.item.getItemMeta();
      if (meta != null) {
         meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
         this.item.setItemMeta(meta);
      }

      return this;
   }

   public ItemBuilder setAmount(int amount) {
      this.item.setAmount(amount);
      return this;
   }

   public ItemBuilder setDurability(short damage) {
      this.item.setDurability(damage);
      return this;
   }

   public ItemStack build() {
      return this.item;
   }

   public ItemBuilder enchant(Enchantment et, int amount, boolean visible) {
      ItemMeta meta = this.item.getItemMeta();
      if (meta != null) {
         meta.addEnchant(et, amount, visible);
         this.item.setItemMeta(meta);
      }

      return this;
   }

   public ItemBuilder setColor(Color color) {
      LeatherArmorMeta LeatherMeta = (LeatherArmorMeta)this.item.getItemMeta();
      if (LeatherMeta != null) {
         LeatherMeta.setColor(color);
         this.item.setItemMeta(LeatherMeta);
      }

      return this;
   }

   public ItemBuilder setLore(List<String> lore) {
      ItemMeta meta = this.item.getItemMeta();
      if (meta != null) {
         meta.setLore(lore);
         this.item.setItemMeta(meta);
      }

      return this;
   }
}
