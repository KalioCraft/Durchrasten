package de.kalio.Durchrasten.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import de.kalio.Durchrasten.Durchrasten;
import de.kalio.Durchrasten.utils.nms.NMSManager;




public class AnvilGUI {
   private Plugin plugin = Durchrasten.getPlugin(Durchrasten.class);
   private boolean colorrename = true;
   private Player player;
   private char colorchar = '&';
   private String title = "";
   private String defaulttext = "";
   private Inventory inventory;
   private HashMap<AnvilGUI.AnvilSlot, ItemStack> items = new HashMap();
   private Listener listener;
   private AnvilGUI.AnvilClickEventHandler handler;
   private Class<?> BlockPosition;
   private Class<?> PacketPlayOutOpenWindow;
   private Class<?> ContainerAnvil;
   private Class<?> ChatMessage;
   private Class<?> EntityHuman;
   private Class<?> ContainerAccess;
   private Class<?> Containers;
   private boolean useNewVersion = NMSManager.useNewVersion();

   private void loadClasses() {
      this.BlockPosition = NMSManager.getNMSClass("BlockPosition");
      this.PacketPlayOutOpenWindow = NMSManager.getNMSClass("PacketPlayOutOpenWindow");
      this.ContainerAnvil = NMSManager.getNMSClass("ContainerAnvil");
      this.ChatMessage = NMSManager.getNMSClass("ChatMessage");
      this.EntityHuman = NMSManager.getNMSClass("EntityHuman");
      if (this.useNewVersion) {
         this.ContainerAccess = NMSManager.getNMSClass("ContainerAccess");
         this.Containers = NMSManager.getNMSClass("Containers");
      }

   }

   public boolean getColorRename() {
      return this.colorrename;
   }

   public void setColorRename(boolean ColorRename) {
      this.colorrename = ColorRename;
   }

   public Player getPlayer() {
      return this.player;
   }

   public String getTitle() {
      return this.title;
   }

   public void setTitle(String Title) {
      this.title = Title;
   }

   public String getDefaultText() {
      return this.defaulttext;
   }

   public void setDefaultText(String DefaultText) {
      this.defaulttext = DefaultText;
   }

   public ItemStack getSlot(AnvilGUI.AnvilSlot Slot) {
      return (ItemStack)this.items.get(Slot);
   }

   public void setSlot(AnvilGUI.AnvilSlot Slot, ItemStack Item) {
      this.items.put(Slot, Item);
   }

   public String getSlotName(AnvilGUI.AnvilSlot Slot) {
      ItemStack IS = this.getSlot(Slot);
      if (IS != null && IS.hasItemMeta()) {
         ItemMeta M = IS.getItemMeta();
         return M.hasDisplayName() ? M.getDisplayName() : "";
      } else {
         return "";
      }
   }

   public void setSlotName(AnvilGUI.AnvilSlot Slot, String Name) {
      ItemStack IS = this.getSlot(Slot);
      if (IS != null) {
         ItemMeta M = IS.getItemMeta();
         M.setDisplayName(Name != null ? ChatColor.translateAlternateColorCodes(this.colorchar, Name) : null);
         IS.setItemMeta(M);
         this.setSlot(Slot, IS);
      }

   }

   public AnvilGUI(Player Player, AnvilGUI.AnvilClickEventHandler Handler) {
      this.loadClasses();
      this.player = Player;
      this.handler = Handler;
      this.listener = new Listener() {
         @EventHandler
         public void ICE(InventoryClickEvent e) {
            if (e.getInventory().equals(AnvilGUI.this.inventory)) {
               e.setCancelled(true);
               if (e.getClick() != ClickType.LEFT && e.getClick() != ClickType.RIGHT) {
                  return;
               }

               ItemStack IS = e.getCurrentItem();
               int S = e.getRawSlot();
               String T = null;
               if (IS != null && IS.hasItemMeta()) {
                  ItemMeta M = IS.getItemMeta();
                  if (M.hasDisplayName()) {
                     T = M.getDisplayName();
                  }
               }

               AnvilGUI.AnvilClickEvent ACE = AnvilGUI.this.new AnvilClickEvent(AnvilGUI.AnvilSlot.bySlot(S), IS, T);
               AnvilGUI.this.handler.onAnvilClick(ACE);
               if (ACE.getWillClose() || ACE.getWillDestroy()) {
                  e.getWhoClicked().closeInventory();
               }

               if (ACE.getWillDestroy()) {
                  HandlerList.unregisterAll(AnvilGUI.this.listener);
               }
            }

         }

         @EventHandler
         public void PAE(PrepareAnvilEvent e) {
            if (e.getInventory().equals(AnvilGUI.this.inventory)) {
               ItemStack IS = e.getResult();
               if (AnvilGUI.this.colorrename && IS != null && IS.hasItemMeta()) {
                  ItemMeta M = IS.getItemMeta();
                  if (M.hasDisplayName()) {
                     M.setDisplayName(ChatColor.translateAlternateColorCodes(AnvilGUI.this.colorchar, M.getDisplayName()));
                  }

                  IS.setItemMeta(M);
                  e.setResult(IS);
               }
            }

         }

         @EventHandler
         public void ICE(InventoryCloseEvent e) {
            if (e.getInventory().equals(AnvilGUI.this.inventory)) {
               AnvilGUI.this.player.setLevel(AnvilGUI.this.player.getLevel() - 1);
               AnvilGUI.this.inventory.clear();
               HandlerList.unregisterAll(AnvilGUI.this.listener);
            }

         }

         @EventHandler
         public void PQE(PlayerQuitEvent e) {
            if (e.getPlayer().equals(AnvilGUI.this.player)) {
               AnvilGUI.this.player.setLevel(AnvilGUI.this.player.getLevel() - 1);
               HandlerList.unregisterAll(AnvilGUI.this.listener);
            }

         }
      };
      Bukkit.getPluginManager().registerEvents(this.listener, this.plugin);
   }

   public void open() {
      this.open(this.title);
   }

   public void open(String Title) {
      this.player.setLevel(this.player.getLevel() + 1);

      try {
         Object P = NMSManager.getHandle(this.player);
         Constructor<?> CM = this.ChatMessage.getConstructor(String.class, Object[].class);
         Object PC;
         if (this.useNewVersion) {
            Method CAM = NMSManager.getMethod("at", this.ContainerAccess, NMSManager.getNMSClass("World"), this.BlockPosition);
            Object CA = this.ContainerAnvil.getConstructor(Integer.TYPE, NMSManager.getNMSClass("PlayerInventory"), this.ContainerAccess).newInstance(9, NMSManager.getPlayerField(this.player, "inventory"), CAM.invoke(this.ContainerAccess, NMSManager.getPlayerField(this.player, "world"), this.BlockPosition.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE).newInstance(0, 0, 0)));
            NMSManager.getField(NMSManager.getNMSClass("Container"), "checkReachable").set(CA, false);
            this.inventory = (Inventory)NMSManager.invokeMethod("getTopInventory", NMSManager.invokeMethod("getBukkitView", CA));
            Iterator var7 = this.items.keySet().iterator();

            while(var7.hasNext()) {
               AnvilGUI.AnvilSlot AS = (AnvilGUI.AnvilSlot)var7.next();
               this.inventory.setItem(AS.getSlot(), (ItemStack)this.items.get(AS));
            }

            int ID = (Integer)NMSManager.invokeMethod("nextContainerCounter", P);
            PC = NMSManager.getPlayerField(this.player, "playerConnection");
            Object PPOOW = this.PacketPlayOutOpenWindow.getConstructor(Integer.TYPE, this.Containers, NMSManager.getNMSClass("IChatBaseComponent")).newInstance(ID, NMSManager.getField(this.Containers, "ANVIL").get(this.Containers), CM.newInstance(ChatColor.translateAlternateColorCodes(this.colorchar, Title), new Object[0]));
            Method SP = NMSManager.getMethod("sendPacket", PC.getClass(), this.PacketPlayOutOpenWindow);
            SP.invoke(PC, PPOOW);
            Field AC = NMSManager.getField(this.EntityHuman, "activeContainer");
            if (AC != null) {
               AC.set(P, CA);
               NMSManager.getField(NMSManager.getNMSClass("Container"), "windowId").set(AC.get(P), ID);
               NMSManager.getMethod("addSlotListener", AC.get(P).getClass(), P.getClass()).invoke(AC.get(P), P);
            }
         } else {
            Object CA = this.ContainerAnvil.getConstructor(NMSManager.getNMSClass("PlayerInventory"), NMSManager.getNMSClass("World"), this.BlockPosition, this.EntityHuman).newInstance(NMSManager.getPlayerField(this.player, "inventory"), NMSManager.getPlayerField(this.player, "world"), this.BlockPosition.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE).newInstance(0, 0, 0), P);
            NMSManager.getField(NMSManager.getNMSClass("Container"), "checkReachable").set(CA, false);
            this.inventory = (Inventory)NMSManager.invokeMethod("getTopInventory", NMSManager.invokeMethod("getBukkitView", CA));
            Iterator var16 = this.items.keySet().iterator();

            while(var16.hasNext()) {
               AnvilGUI.AnvilSlot AS = (AnvilGUI.AnvilSlot)var16.next();
               this.inventory.setItem(AS.getSlot(), (ItemStack)this.items.get(AS));
            }

            int ID = (Integer)NMSManager.invokeMethod("nextContainerCounter", P);
            Object PC1 = NMSManager.getPlayerField(this.player, "playerConnection");
            PC1 = this.PacketPlayOutOpenWindow.getConstructor(Integer.TYPE, String.class, NMSManager.getNMSClass("IChatBaseComponent"), Integer.TYPE).newInstance(ID, "minecraft:anvil", CM.newInstance(ChatColor.translateAlternateColorCodes(this.colorchar, Title), new Object[0]), 0);
            Method SP = NMSManager.getMethod("sendPacket", PC1.getClass(), this.PacketPlayOutOpenWindow);
            SP.invoke(PC1, PC1);
            Field AC = NMSManager.getField(this.EntityHuman, "activeContainer");
            if (AC != null) {
               AC.set(P, CA);
               NMSManager.getField(NMSManager.getNMSClass("Container"), "windowId").set(AC.get(P), ID);
               NMSManager.getMethod("addSlotListener", AC.get(P).getClass(), P.getClass()).invoke(AC.get(P), P);
            }
         }
      } catch (Exception var11) {
         var11.printStackTrace();
      }

   }

   public class AnvilClickEvent {
      private AnvilGUI.AnvilSlot slot;
      private ItemStack item;
      private String text;
      private boolean close = false;
      private boolean destroy = false;

      public AnvilClickEvent(AnvilGUI.AnvilSlot Slot, ItemStack Item, String Text) {
         this.slot = Slot;
         this.item = Item;
         this.text = Text;
      }

      public AnvilGUI.AnvilSlot getSlot() {
         return this.slot;
      }

      public ItemStack getItemStack() {
         return this.item;
      }

      public void setItemStack(ItemStack Item) {
         this.item = Item;
         AnvilGUI.this.inventory.setItem(this.slot.getSlot(), this.item);
      }

      public boolean hasText() {
         return this.text != null;
      }

      public String getText() {
         return this.text != null ? this.text : AnvilGUI.this.defaulttext;
      }

      public boolean getWillClose() {
         return this.close;
      }

      public void setWillClose(boolean Close) {
         this.close = Close;
      }

      public boolean getWillDestroy() {
         return this.destroy;
      }

      public void setWillDestroy(boolean Destroy) {
         this.destroy = Destroy;
      }
   }

   public interface AnvilClickEventHandler {
      void onAnvilClick(AnvilGUI.AnvilClickEvent var1);
   }

   public static enum AnvilSlot {
      INPUT_LEFT(0),
      INPUT_RIGHT(1),
      OUTPUT(2);

      private int slot;

      private AnvilSlot(int Slot) {
         this.slot = Slot;
      }

      public int getSlot() {
         return this.slot;
      }

      public static AnvilGUI.AnvilSlot bySlot(int Slot) {
         AnvilGUI.AnvilSlot[] var4;
         int var3 = (var4 = values()).length;

         for(int var2 = 0; var2 < var3; ++var2) {
            AnvilGUI.AnvilSlot AS = var4[var2];
            if (AS.getSlot() == Slot) {
               return AS;
            }
         }

         return null;
      }
   }
}
