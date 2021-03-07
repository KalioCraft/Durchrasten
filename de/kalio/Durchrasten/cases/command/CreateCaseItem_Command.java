package de.kalio.Durchrasten.cases.command;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.kalio.Durchrasten.Durchrasten;
import de.kalio.Durchrasten.utils.AnvilGUI;
import de.kalio.Durchrasten.utils.AnvilGUI.AnvilClickEvent;
import de.kalio.Durchrasten.utils.ItemBuilder;




public class CreateCaseItem_Command implements CommandExecutor {

	private static Durchrasten plugin;

	@SuppressWarnings("static-access")
	public CreateCaseItem_Command(Durchrasten plugin) {
		this.plugin = plugin;
		plugin.getCommand("createcaseitem").setExecutor(this);
	}
	
	public static HashMap<Player, ItemStack> item = new HashMap<>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		String Prefix = Durchrasten.prefix;
		String NoPerms = Prefix + "§cKeine Rechte";
		
		if(sender instanceof Player) {
			
			Player p = (Player) sender;
			
			if(p.hasPermission("system.case")) {
				if(p.getItemInHand().getType() != Material.AIR) {
					
					ItemStack item = new ItemStack(p.getItemInHand());
					
					CreateCaseItem_Command.item.clear();
					
					AnvilGUI anzahlauswahl = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler() {

						@Override
						public void onAnvilClick(AnvilClickEvent e) {
							if(e.getSlot() == AnvilGUI.AnvilSlot.OUTPUT) {
								
								e.setWillClose(true);
								e.setWillDestroy(true);
								
								String anzahl = e.getText().replaceAll("Anzahl von Item:", "");
									
								if(anzahl.matches("[0-9]+")) {
									
									Integer anzahli = Integer.valueOf(anzahl);
									
									item.setAmount(anzahli);
									
									Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
										
										@Override
										public void run() {

											AnvilGUI nameauswahl = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler() {

												@Override
												public void onAnvilClick(AnvilClickEvent e) {
													if(e.getSlot() == AnvilGUI.AnvilSlot.OUTPUT) {
														
														e.setWillClose(true);
														e.setWillDestroy(true);
														
														String name = e.getText().replaceAll("Itemname:", "");
														
														ItemMeta itemm = item.getItemMeta();
														itemm.setDisplayName(name);
														item.setItemMeta(itemm);
														
														CreateCaseItem_Command.item.put(p, item);
														
														Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
															
															@Override
															public void run() {

																
																Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
																	
																	@Override
																	public void run() { //Normal Selten Ultra Episch Legendär
															
																		Inventory sets = Bukkit.createInventory(null, 3*9, "§7Seltenheit auswählen");
																		for(Integer i = 0; i != sets.getSize(); i++) {
																			sets.setItem(i, ItemBuilder.createItemOL(Material.BLACK_STAINED_GLASS_PANE, "§7", 1));
																		}
																		sets.setItem(0 + 9, ItemBuilder.createItemOL(Material.STONE, "§aNormal", 1));
																		
																		sets.setItem(2 + 9, ItemBuilder.createItemOL(Material.DIAMOND, "§6Selten", 1));
																		
																		sets.setItem(4 + 9, ItemBuilder.createItemOL(Material.BEACON, "§bUltra", 1));
																		
																		sets.setItem(6 + 9, ItemBuilder.createItemOL(Material.DRAGON_EGG, "§5Episch", 1));
																		
																		sets.setItem(8 + 9, ItemBuilder.createItemOL(Material.COMMAND_BLOCK, "§6Legendaer", 1));
																		
																		p.openInventory(sets);
																		
																	}
																}, 20);
																
															}
														}, 20);
					
													} else {
														e.setWillClose(false);
														e.setWillDestroy(false);
													}
													
												}
											});
											
											nameauswahl.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, ItemBuilder.createItemOL(Material.NAME_TAG, "Itemname:", 1));
											
											nameauswahl.open();
											
										}
									}, 20);
								
								} else {
									p.sendMessage(Prefix + "§a" + anzahl + " §cist keine Zahl!");
								}
							} else {
								e.setWillClose(false);
								e.setWillDestroy(false);
							}
							
						}
					});
					
					anzahlauswahl.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, ItemBuilder.createItemOL(Material.NAME_TAG, "Anzahl von Item:", 1));
					
					anzahlauswahl.open();
					
				} else {
					p.sendMessage(Prefix + "§cDu musst ein Item in der Hand haben!");
				}
			} else {
				p.sendMessage(NoPerms);
			}
		} else {
			Bukkit.getConsoleSender().sendMessage(Prefix + "§cDen Command kann nur ein Spieler ausführen!");
		}
		
		return true;
	}

}
