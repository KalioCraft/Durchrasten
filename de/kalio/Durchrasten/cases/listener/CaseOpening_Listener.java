package de.kalio.Durchrasten.cases.listener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import de.kalio.Durchrasten.Durchrasten;
import de.kalio.Durchrasten.cases.command.CreateCaseItem_Command;
import de.kalio.Durchrasten.utils.Case_Opening;
import de.kalio.Durchrasten.utils.Cases;
import de.kalio.Durchrasten.utils.ItemBuilder;




public class CaseOpening_Listener implements Listener {
	
	private Durchrasten plugin;

	public CaseOpening_Listener(Durchrasten plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	public static Boolean using;
	
	BukkitTask cd1;
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		
		Player p = e.getPlayer();
		
		if(e.getClickedBlock() != null) {
			if(e.getClickedBlock().getType() == Material.CHEST) {
				
				try {
					File CO = new File("plugins/CaseOpening/CaseOpening.yml");
					YamlConfiguration yCO = YamlConfiguration.loadConfiguration(CO);
					
					String Welt = yCO.getString("Truhe" + ".Loc" + ".Welt");
					
					double X = yCO.getDouble("Truhe" + ".Loc" + ".X");
					double Y = yCO.getDouble("Truhe" + ".Loc" + ".Y");
					double Z = yCO.getDouble("Truhe" + ".Loc" + ".Z");
					
					Location truhe = new Location(Bukkit.getWorld(Welt), X, Y, Z);
					
					if(e.getClickedBlock().getLocation().equals(truhe)) {
						if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
							e.setCancelled(true);
							
							Inventory cases = Bukkit.createInventory(null, 6*9, "§7Kisten §8| §b" + p.getName());
							ItemStack füller = ItemBuilder.createItem(Material.BLACK_STAINED_GLASS_PANE, "§7", 1, null);
							for(int i = 0; i != 9; i++) {
								cases.setItem(i, füller);
							}
							for(int i = 9; i != cases.getSize(); i++) {
								if(i == 9 * 2 || i == 9 * 3 || i == 9 || i == 9 * 4) {
									cases.setItem(i, füller);
								}
								if(i == 17 || i == 17 + 9 || i == 17 + 9 + 9 || i == 17 + 9 + 9 + 9 || i == 17 + 9 +9 +9 +9) {
									cases.setItem(i, füller);
								}
							}
							for(int i = cases.getSize() - 9; i != cases.getSize(); i++) {
								cases.setItem(i, füller);
							}
							cases.setItem(49, ItemBuilder.createItem(Material.BARRIER, "§cSchließen", 1, null));
							cases.setItem(20, ItemBuilder.createItem(Material.CHEST, "§7Vote §aKisten", 1, new String[] {"§7Du hast §8" + Cases.getCase(p, "V") + " §7Kisten§7."}));
							cases.setItem(22, ItemBuilder.createItem(Material.ENDER_CHEST, "§5Epische §aKisten", 1, new String[] {"§7Du hast §a" + Cases.getCase(p, "E") + " Kisten§7."}));
							
							cases.setItem(24, ItemBuilder.createItem(Material.END_PORTAL_FRAME, "§c§lHero §aKisten", 1, new String[] {"§7Du hast §c§l" + Cases.getCase(p, "H") + " Kisten§7."}));
							cases.setItem(22 + 9 + 9, ItemBuilder.createItem(Material.GRASS_BLOCK, "§a§lFrühlings §aKisten", 1, new String[] {"§7Du hast §a§l" + Cases.getCase(p, "F") + " Kisten§7."}));
							
							
							
							
							p.openInventory(cases);
						} else if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
							if(e.getPlayer().getGameMode() == GameMode.CREATIVE) {
								if(e.getPlayer().isSneaking()) {
									
									String Prefix = "§b§lKisten §8| §7"
;									
									p.sendMessage(Prefix + "§aDu hast den CaseBlock abgebaut!");
									p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
									
									yCO.set("Truhe" + ".Loc", null);
									
									yCO.save(CO);
									
								} else {
									e.setCancelled(true);
									
									Inventory cases = Bukkit.createInventory(null, 6*9, "§7Kisten §8| §b" + p.getName());
									ItemStack füller = ItemBuilder.createItem(Material.BLACK_STAINED_GLASS_PANE, "§7", 1, null);
									for(int i = 0; i != 9; i++) {
										cases.setItem(i, füller);
									}
									for(int i = 9; i != cases.getSize(); i++) {
										if(i == 9 * 2 || i == 9 * 3 || i == 9 || i == 9 * 4) {
											cases.setItem(i, füller);
										}
										if(i == 17 || i == 17 + 9 || i == 17 + 9 + 9 || i == 17 + 9 + 9 + 9 || i == 17 + 9 +9 +9 +9) {
											cases.setItem(i, füller);
										}
									}
									for(int i = cases.getSize() - 9; i != cases.getSize(); i++) {
										cases.setItem(i, füller);
									}
									cases.setItem(49, ItemBuilder.createItem(Material.BARRIER, "§cSchließen", 1, null));
									cases.setItem(20, ItemBuilder.createItem(Material.CHEST, "§7Vote §aKisten", 1, new String[] {"§7Du hast §8" + Cases.getCase(p, "V") + " §7Kisten§7."}));
									cases.setItem(22, ItemBuilder.createItem(Material.ENDER_CHEST, "§5Epische §aKisten", 1, new String[] {"§7Du hast §a" + Cases.getCase(p, "E") + " Kisten§7."}));
									
									cases.setItem(24, ItemBuilder.createItem(Material.END_PORTAL_FRAME, "§c§lHero §aKisten", 1, new String[] {"§7Du hast §c§l" + Cases.getCase(p, "H") + " Kisten§7."}));
									cases.setItem(22 + 9 + 9, ItemBuilder.createItem(Material.GRASS_BLOCK, "§a§lFrühlings §aKisten", 1, new String[] {"§7Du hast §a§l" + Cases.getCase(p, "F") + " Kisten§7."}));
									
									
									
									p.openInventory(cases);
								}
							} else {
								e.setCancelled(true);
								
								
								Inventory cases = Bukkit.createInventory(null, 6*9, "§7Kisten §8| §b" + p.getName());
								ItemStack füller = ItemBuilder.createItem(Material.BLACK_STAINED_GLASS_PANE, "§7", 1, null);
								for(int i = 0; i != 9; i++) {
									cases.setItem(i, füller);
								}
								for(int i = 9; i != cases.getSize(); i++) {
									if(i == 9 * 2 || i == 9 * 3 || i == 9 || i == 9 * 4) {
										cases.setItem(i, füller);
									}
									if(i == 17 || i == 17 + 9 || i == 17 + 9 + 9 || i == 17 + 9 + 9 + 9 || i == 17 + 9 +9 +9 +9) {
										cases.setItem(i, füller);
									}
								}
								for(int i = cases.getSize() - 9; i != cases.getSize(); i++) {
									cases.setItem(i, füller);
								}
								cases.setItem(49, ItemBuilder.createItem(Material.BARRIER, "§cSchließen", 1, null));
								cases.setItem(20, ItemBuilder.createItem(Material.CHEST, "§7Vote §aKisten", 1, new String[] {"§7Du hast §8" + Cases.getCase(p, "V") + " §7Kisten§7."}));
								cases.setItem(22, ItemBuilder.createItem(Material.ENDER_CHEST, "§5Epische §aKisten", 1, new String[] {"§7Du hast §a" + Cases.getCase(p, "E") + " Kisten§7."}));
								
								cases.setItem(24, ItemBuilder.createItem(Material.END_PORTAL_FRAME, "§c§lHero §aKisten", 1, new String[] {"§7Du hast §c§l" + Cases.getCase(p, "H") + " Kisten§7."}));
								cases.setItem(22 + 9 + 9, ItemBuilder.createItem(Material.GRASS_BLOCK, "§a§lFrühlings §aKisten", 1, new String[] {"§7Du hast §a§l" + Cases.getCase(p, "F") + " Kisten§7."}));
								
								
								
								p.openInventory(cases);
							}
						} else {
							e.setCancelled(true);
							
							Inventory cases = Bukkit.createInventory(null, 6*9, "§7Kisten §8| §b" + p.getName());
							ItemStack füller = ItemBuilder.createItem(Material.BLACK_STAINED_GLASS_PANE, "§7", 1, null);
							for(int i = 0; i != 9; i++) {
								cases.setItem(i, füller);
							}
							for(int i = 9; i != cases.getSize(); i++) {
								if(i == 9 * 2 || i == 9 * 3 || i == 9 || i == 9 * 4) {
									cases.setItem(i, füller);
								}
								if(i == 17 || i == 17 + 9 || i == 17 + 9 + 9 || i == 17 + 9 + 9 + 9 || i == 17 + 9 +9 +9 +9) {
									cases.setItem(i, füller);
								}
							}
							for(int i = cases.getSize() - 8; i != cases.getSize(); i++) {
								cases.setItem(i, füller);
							}
							cases.setItem(49, ItemBuilder.createItem(Material.BARRIER, "§cSchließen", 1, null));
							cases.setItem(20, ItemBuilder.createItem(Material.CHEST, "§7Vote §aKisten", 1, new String[] {"§7Du hast §8" + Cases.getCase(p, "V") + " §7Kisten§7."}));
							cases.setItem(22, ItemBuilder.createItem(Material.ENDER_CHEST, "§5Epische §aKisten", 1, new String[] {"§7Du hast §a" + Cases.getCase(p, "E") + " Kisten§7."}));
							
							cases.setItem(24, ItemBuilder.createItem(Material.END_PORTAL_FRAME, "§c§lHero §aKisten", 1, new String[] {"§7Du hast §c§l" + Cases.getCase(p, "H") + " Kisten§7."}));
							cases.setItem(22 + 9 + 9, ItemBuilder.createItem(Material.GRASS_BLOCK, "§a§lFrühlings §aKisten", 1, new String[] {"§7Du hast §a§l" + Cases.getCase(p, "F") + " Kisten§7."}));
							
							
							
							
							p.openInventory(cases);
						}
					}
				} catch(Exception e1) {e1.printStackTrace();}
			}
		}
	}
	
	@EventHandler
	public void onSet(BlockPlaceEvent e) {
		
		Player p = e.getPlayer();
		
		try {
			
			if(p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§6§lCaseOpening")) {
	
					String Prefix = "§b§lKisten §8| §7";
					
					File CO = new File("plugins/CaseOpening/CaseOpening.yml");
					YamlConfiguration yCO = YamlConfiguration.loadConfiguration(CO);
					
					p.setItemInHand(new ItemStack(Material.AIR));
					
					p.sendMessage(Prefix + "§aDu hast den CaseBlock gesetzt!");
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
					
					yCO.set("Truhe" + ".Loc" + ".Welt", e.getBlock().getLocation().getWorld().getName());
					yCO.set("Truhe" + ".Loc" + ".X", e.getBlock().getLocation().getX());
					yCO.set("Truhe" + ".Loc" + ".Y", e.getBlock().getLocation().getY());
					yCO.set("Truhe" + ".Loc" + ".Z", e.getBlock().getLocation().getZ());
					
					yCO.save(CO);
				}
			
			
		} catch(Exception e1) {e1.printStackTrace();}
	}
	
	public void openco(Player p, String Truhe) {
		
		 Case_Opening l = new Case_Opening(p);
		 l.openco(p, Truhe);
			
		    
		   

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
	
	 public int zufallszahl(int min, int max) {
		    Random random = new Random();
		    return random.nextInt(max - min + 1) + min;
		  }
	 
		@EventHandler
		public void onClick(InventoryClickEvent e) {
			if(e.getView().getTitle().equalsIgnoreCase("§7Showcase §8| §7Vote")) {
				e.setCancelled(true);
				return;
			}
			if(e.getView().getTitle().equalsIgnoreCase("§7Showcase §8| §5§lEpisch")) {
				e.setCancelled(true);
				return;
			}
			if(e.getView().getTitle().equalsIgnoreCase("§7Showcase §8| §c§lHero")) {
				e.setCancelled(true);
				return;
			}
			
			if(e.getView().getTitle().equalsIgnoreCase("§7ShowCase §8| §a§lFrühling")) {
				e.setCancelled(true);
				return;
			}
			
			
			
			if(e.getView().getTitle().equalsIgnoreCase("§6CaseOpening")) {
				
				e.setCancelled(true);
				
			} else if(e.getView().getTitle().equalsIgnoreCase("§7Kisten §8| §b" + e.getWhoClicked().getName() + "")) {
				
				e.setCancelled(true);
				
				String Prefix = "§b§lKisten §8| §7";
				
				if(e.getCurrentItem() != null) {
					if(e.getCurrentItem().getType() == Material.BARRIER) {
						Player p = (Player) e.getWhoClicked();
						p.closeInventory();
						p.sendMessage(Prefix+"§7Du hast das Inventar §cgeschlossen§7!");
					}
					if(e.getCurrentItem().getType() != Material.AIR) {
						if(e.getCurrentItem().hasItemMeta()) {
							if(e.getCurrentItem().getItemMeta().hasDisplayName()) {
								if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§5Epische §aKisten")) {
									if(e.isLeftClick()) {
										if(Cases.getCase((Player) e.getWhoClicked(), "E") > 0) {
											
											openco((Player) e.getWhoClicked(), "Episch");
											
										} else {
											
											e.getWhoClicked().closeInventory();
											
											e.getWhoClicked().sendMessage(Prefix + "§cDu hast leider keine Epischen Truhen übrig.");
											
										}
									}else if(e.isRightClick()) {
										Inventory inventory = Bukkit.createInventory(null, 6*9, "§7Showcase §8| §5§lEpisch");
										for(ItemStack sorted : getAllItemsSorted("Episch")) {
											inventory.addItem(sorted);
										}
										((Player) e.getWhoClicked()).openInventory(inventory);
									}
								} else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§c§lHero §aKisten")) {
									if(e.isLeftClick()) {
										if(Cases.getCase((Player) e.getWhoClicked(), "H") > 0) {
											
											openco((Player) e.getWhoClicked(), "Hero");
											
										} else {
											
											e.getWhoClicked().closeInventory();
											
											e.getWhoClicked().sendMessage(Prefix + "§cDu hast leider keine Hero Truhen übrig.");
											
										}
									}else if(e.isRightClick()) {
										Inventory inventory = Bukkit.createInventory(null, 6*9, "§7Showcase §8| §c§lHero");
										for(ItemStack sorted : getAllItemsSorted("Hero")) {
											inventory.addItem(sorted);
										}
										((Player) e.getWhoClicked()).openInventory(inventory);
									}
								}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§a§lFrühlings §aKisten")) {
									if(e.isLeftClick()) {
										if(Cases.getCase((Player) e.getWhoClicked(), "F") > 0) {
											
											openco((Player) e.getWhoClicked(), "Frühling");
											
										} else {
											
											e.getWhoClicked().closeInventory();
											
											e.getWhoClicked().sendMessage(Prefix + "§cDu hast leider keine Frühlings Truhen übrig.");
											
										}
									}else if(e.isRightClick()) {
										Inventory inventory = Bukkit.createInventory(null, 6*9, "§7Showcase §8| §a§lFrühling");
										for(ItemStack sorted : getAllItemsSorted("Frühling")) {
											inventory.addItem(sorted);
										}
										((Player) e.getWhoClicked()).openInventory(inventory);
									}
								}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7Vote §aKisten")) {
									if(e.isLeftClick()) {
										if(Cases.getCase((Player) e.getWhoClicked(), "V") > 0) {
											
											openco((Player) e.getWhoClicked(), "Vote");
											
										} else {
											
											e.getWhoClicked().closeInventory();
											
											e.getWhoClicked().sendMessage(Prefix + "§cDu hast leider keine Vote Truhen übrig.");
											
										}
									}else if(e.isRightClick()) {
										Inventory inventory = Bukkit.createInventory(null, 6*9, "§7Showcase §8| §7Vote");
										for(ItemStack sorted : getAllItemsSorted("Vote")) {
											inventory.addItem(sorted);
										}
										((Player) e.getWhoClicked()).openInventory(inventory);
									}
								}
							}
						}
					}
				}
				
			} else if(e.getView().getTitle().equalsIgnoreCase("§7Seltenheit auswählen")) {
				
				e.setCancelled(true);
				
				if(e.getCurrentItem() != null) {
					if(e.getCurrentItem().getType() != Material.AIR) {
						if(e.getCurrentItem().hasItemMeta()) {
							if(e.getCurrentItem().getItemMeta().hasDisplayName()) {
								if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aNormal")) {
									
									e.getWhoClicked().closeInventory();
									
									ItemStack item = CreateCaseItem_Command.item.get(e.getWhoClicked());
									ItemMeta itemm = item.getItemMeta();
									itemm.setLore(Arrays.asList(new String[] {"§aNormal"}));
									item.setItemMeta(itemm);
									
									e.getWhoClicked().getInventory().setItemInHand(item);
									
								} else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Selten")) {
									
									e.getWhoClicked().closeInventory();
									
									ItemStack item = CreateCaseItem_Command.item.get(e.getWhoClicked());
									ItemMeta itemm = item.getItemMeta();
									itemm.setLore(Arrays.asList(new String[] {"§6Selten"}));
									item.setItemMeta(itemm);
									
									e.getWhoClicked().getInventory().setItemInHand(item);
									
								} else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§bUltra")) {
									
									e.getWhoClicked().closeInventory();
									
									ItemStack item = CreateCaseItem_Command.item.get(e.getWhoClicked());
									ItemMeta itemm = item.getItemMeta();
									itemm.setLore(Arrays.asList(new String[] {"§bUltra"}));
									item.setItemMeta(itemm);
									
									e.getWhoClicked().getInventory().setItemInHand(item);
									
								} else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§5Episch")) {
									
									e.getWhoClicked().closeInventory();
									
									ItemStack item = CreateCaseItem_Command.item.get(e.getWhoClicked());
									ItemMeta itemm = item.getItemMeta();
									itemm.setLore(Arrays.asList("§5Episch"));
									item.setItemMeta(itemm);
									
									e.getWhoClicked().getInventory().setItemInHand(item);
									
								} else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Legendaer")) {
									
									e.getWhoClicked().closeInventory();
									
									ItemStack item = CreateCaseItem_Command.item.get(e.getWhoClicked());
									ItemMeta itemm = item.getItemMeta();
									itemm.setLore(Arrays.asList(new String[] {"§6Legendaer"}));
									item.setItemMeta(itemm);
									
									e.getWhoClicked().getInventory().setItemInHand(item);
									
								}
							}
						}
					}
				}	
			}
			
		}
		
		@EventHandler
		public void onSave(InventoryCloseEvent e) {
			if(e.getView().getTitle().equalsIgnoreCase("§6Case §8| §5§lEpisch")) {
				
				String Prefix = "§b§lKisten §8| §7";
				Player p = (Player) e.getPlayer();
				
				File CO = new File("plugins/CaseOpening/CaseOpening.yml");
				YamlConfiguration yCO = YamlConfiguration.loadConfiguration(CO);
				
				Integer i1 = 0;
				Integer i2 = 0;
				
				for(ItemStack i : e.getInventory().getContents()) {
					if(i != null) {
						
						i1++;
						
						if(i.hasItemMeta()) {
							if(i.getItemMeta().hasLore()) {
								if(i.getItemMeta().getLore().get(0).equalsIgnoreCase("§aNormal") || i.getItemMeta().getLore().get(0).equalsIgnoreCase("§6Selten") || i.getItemMeta().getLore().get(0).equalsIgnoreCase("§bUltra") || i.getItemMeta().getLore().get(0).equalsIgnoreCase("Episch") || i.getItemMeta().getLore().get(0).equalsIgnoreCase("§6Legendaer")) {
									
									i2++;
									
								} else {
									e.getPlayer().sendMessage(Prefix + "§cDas Inventar wurde nicht gespeichert da nicht jedes Item im Inventar eine Seltenheit hat!" + "\n" + "§c/CreateCaseItem");
									p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
									break;
								}
							} else {
								e.getPlayer().sendMessage(Prefix + "§cDas Inventar wurde nicht gespeichert da nicht jedes Item im Inventar eine Seltenheit hat!" + "\n" + "§c/CreateCaseItem");
								p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
								break;
							}
						} else {
							e.getPlayer().sendMessage(Prefix + "§cDas Inventar wurde nicht gespeichert da nicht jedes Item im Inventar eine Seltenheit hat!" + "\n" + "§c/CreateCaseItem");
							p.playSound(p.getLocation(), Sound.ENTITY_ARMOR_STAND_BREAK, 1, 1);
							break;
						}
					}
				}
				if(i1 == i2) {
					e.getPlayer().sendMessage(Prefix + "§aInventar erfolgreich gespeichert!");
					
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
					
					Integer i3 = 0;
					
					for(ItemStack i : e.getInventory().getContents()) {
						i3++;						
						yCO.set("Truhe" + ".Episch" + ".Preise" + "." + i3, i);
						
						try {
							yCO.save(CO);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			} else if(e.getView().getTitle().equalsIgnoreCase("§6Case §8| §c§lHero")) {
				
				String Prefix = "§b§lKisten §8| §7";
				Player p = (Player) e.getPlayer();
				
				File CO = new File("plugins/CaseOpening/CaseOpening.yml");
				YamlConfiguration yCO = YamlConfiguration.loadConfiguration(CO);
				
				Integer i1 = 0;
				Integer i2 = 0;
				
				for(ItemStack i : e.getInventory().getContents()) {
					if(i != null) {
						
						i1++;
						
						if(i.hasItemMeta()) {
							if(i.getItemMeta().hasLore()) {
								if(i.getItemMeta().getLore().get(0).equalsIgnoreCase("§aNormal") || i.getItemMeta().getLore().get(0).equalsIgnoreCase("§6Selten") || i.getItemMeta().getLore().get(0).equalsIgnoreCase("§bUltra") || i.getItemMeta().getLore().get(0).equalsIgnoreCase("Episch") || i.getItemMeta().getLore().get(0).equalsIgnoreCase("§6Legendaer")) {
									
									i2++;
									
								} else {
									e.getPlayer().sendMessage(Prefix + "§cDas Inventar wurde nicht gespeichert da nicht jedes Item im Inventar eine Seltenheit hat!" + "\n" + "§c/CreateCaseItem");
									p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
									break;
								}
							} else {
								e.getPlayer().sendMessage(Prefix + "§cDas Inventar wurde nicht gespeichert da nicht jedes Item im Inventar eine Seltenheit hat!" + "\n" + "§c/CreateCaseItem");
								p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
								break;
							}
						} else {
							e.getPlayer().sendMessage(Prefix + "§cDas Inventar wurde nicht gespeichert da nicht jedes Item im Inventar eine Seltenheit hat!" + "\n" + "§c/CreateCaseItem");
							p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
							break;
						}
					}
				}
				if(i1 == i2) {
					e.getPlayer().sendMessage(Prefix + "§aInventar erfolgreich gespeichert!");
					
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
					
					Integer i3 = 0;
					
					for(ItemStack i : e.getInventory().getContents()) {
						i3++;
						
						yCO.set("Truhe" + ".Hero" + ".Preise" + "." + i3, i);
						
						try {
							yCO.save(CO);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}else if(e.getView().getTitle().equalsIgnoreCase("§6Case §8| §7Vote")) {
				
				String Prefix = "§b§lKisten §8| §7";
				Player p = (Player) e.getPlayer();
				
				File CO = new File("plugins/CaseOpening/CaseOpening.yml");
				YamlConfiguration yCO = YamlConfiguration.loadConfiguration(CO);
				
				Integer i1 = 0;
				Integer i2 = 0;
				
				for(ItemStack i : e.getInventory().getContents()) {
					if(i != null) {
						
						i1++;
						
						if(i.hasItemMeta()) {
							if(i.getItemMeta().hasLore()) {
								if(i.getItemMeta().getLore().get(0).equalsIgnoreCase("§aNormal") || i.getItemMeta().getLore().get(0).equalsIgnoreCase("§6Selten") || i.getItemMeta().getLore().get(0).equalsIgnoreCase("§bUltra") || i.getItemMeta().getLore().get(0).equalsIgnoreCase("Episch") || i.getItemMeta().getLore().get(0).equalsIgnoreCase("§6Legendaer")) {
									
									i2++;
									
								} else {
									e.getPlayer().sendMessage(Prefix + "§cDas Inventar wurde nicht gespeichert da nicht jedes Item im Inventar eine Seltenheit hat!" + "\n" + "§c/CreateCaseItem");
									p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
									break;
								}
							} else {
								e.getPlayer().sendMessage(Prefix + "§cDas Inventar wurde nicht gespeichert da nicht jedes Item im Inventar eine Seltenheit hat!" + "\n" + "§c/CreateCaseItem");
								p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
								break;
							}
						} else {
							e.getPlayer().sendMessage(Prefix + "§cDas Inventar wurde nicht gespeichert da nicht jedes Item im Inventar eine Seltenheit hat!" + "\n" + "§c/CreateCaseItem");
							p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
							break;
						}
					}
				}
				if(i1 == i2) {
					e.getPlayer().sendMessage(Prefix + "§aInventar erfolgreich gespeichert!");
					
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
					
					Integer i3 = 0;
					
					for(ItemStack i : e.getInventory().getContents()) {
						i3++;
						
						yCO.set("Truhe" + ".Vote" + ".Preise" + "." + i3, i);
						
						try {
							yCO.save(CO);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}else if(e.getView().getTitle().equalsIgnoreCase("§6Case §8| §a§lFrühling")) {
				
				String Prefix = "§b§lKisten §8| §7";
				Player p = (Player) e.getPlayer();
				
				File CO = new File("plugins/CaseOpening/CaseOpening.yml");
				YamlConfiguration yCO = YamlConfiguration.loadConfiguration(CO);
				
				Integer i1 = 0;
				Integer i2 = 0;
				
				for(ItemStack i : e.getInventory().getContents()) {
					if(i != null) {
						
						i1++;
						
						if(i.hasItemMeta()) {
							if(i.getItemMeta().hasLore()) {
								if(i.getItemMeta().getLore().get(0).equalsIgnoreCase("§aNormal") || i.getItemMeta().getLore().get(0).equalsIgnoreCase("§6Selten") || i.getItemMeta().getLore().get(0).equalsIgnoreCase("§bUltra") || i.getItemMeta().getLore().get(0).equalsIgnoreCase("Episch") || i.getItemMeta().getLore().get(0).equalsIgnoreCase("§6Legendaer")) {
									
									i2++;
									
								} else {
						
									e.getPlayer().sendMessage(Prefix + "§cDas Inventar wurde nicht gespeichert da nicht jedes Item im Inventar eine Seltenheit hat!" + "\n" + "§c/CreateCaseItem");
									p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
									break;
								}
							} else {
								e.getPlayer().sendMessage(Prefix + "§cDas Inventar wurde nicht gespeichert da nicht jedes Item im Inventar eine Seltenheit hat!" + "\n" + "§c/CreateCaseItem");
								p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
								break;
							}
						} else {
							e.getPlayer().sendMessage(Prefix + "§cDas Inventar wurde nicht gespeichert da nicht jedes Item im Inventar eine Seltenheit hat!" + "\n" + "§c/CreateCaseItem");
							p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
							break;
						}
					}
				}
				if(i1 == i2) {
					e.getPlayer().sendMessage(Prefix + "§aInventar erfolgreich gespeichert!");
					
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
					
					Integer i3 = 0;
					
					for(ItemStack i : e.getInventory().getContents()) {
						i3++;
						
						yCO.set("Truhe" + ".Frühling" + ".Preise" + "." + i3, i);
						
						try {
							yCO.save(CO);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		}
		
		
		private List<ItemStack> getAllItemsSorted(String Truhe){
			
			File CO = new File("plugins/CaseOpening/CaseOpening.yml");
			YamlConfiguration yCO = YamlConfiguration.loadConfiguration(CO);
			
			ArrayList<ItemStack> items = new ArrayList<>();
		
			ItemStack i = null;
			
			Integer ii = 0;
			ArrayList<ItemStack> common = new ArrayList<ItemStack>();
			ArrayList<ItemStack> rare = new ArrayList<ItemStack>();
			ArrayList<ItemStack> ultra = new ArrayList<ItemStack>();
			ArrayList<ItemStack> epic = new ArrayList<ItemStack>();
			ArrayList<ItemStack> legendary = new ArrayList<ItemStack>();
			
			ArrayList<ItemStack> sorted = new ArrayList<ItemStack>();
			
			while(ii != 100) {
				System.out.println(yCO.getString("Truhe."+Truhe+".Preise."+ii));
				ii++;
				if(yCO.getString("Truhe" + "." + Truhe + ".Preise" + "." + ii) != null) {
					
					ItemStack is = new ItemStack(yCO.getItemStack("Truhe" + "." + Truhe + ".Preise" + "." + ii));
					
					if(is.getItemMeta().getLore().get(0).equalsIgnoreCase("§aNormal")) {
						
						common.add(is);
						
					}
					if(is.getItemMeta().getLore().get(0).equalsIgnoreCase("§6Selten")) {
						
						rare.add(is);
						
					}
					if(is.getItemMeta().getLore().get(0).equalsIgnoreCase("§bUltra")) {
						
						ultra.add(is);
						
					}
					if(is.getItemMeta().getLore().get(0).equalsIgnoreCase("Episch")) {
						
						epic.add(is);
						
					}
					if(is.getItemMeta().getLore().get(0).equalsIgnoreCase("§6Legendaer")) {
						
						legendary.add(is);
						
					}
				}
			}

			for(ItemStack l : legendary) {
				sorted.add(l);
			}
			for(ItemStack e : epic) {
				sorted.add(e);
			}
			for(ItemStack u : ultra) {
				sorted.add(u);
			}
			for(ItemStack r : rare) {
				sorted.add(r);
			}
			for(ItemStack c : common) {
				sorted.add(c);
			}
			
			return sorted;
		}

}