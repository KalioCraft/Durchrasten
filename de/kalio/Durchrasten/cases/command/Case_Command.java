package de.kalio.Durchrasten.cases.command;


import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import de.kalio.Durchrasten.Durchrasten;
import de.kalio.Durchrasten.utils.Cases;


public class Case_Command implements CommandExecutor {

	private static Durchrasten plugin;

	@SuppressWarnings("static-access")
	public Case_Command(Durchrasten plugin) {
		this.plugin = plugin;
		plugin.getCommand("case").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		String Prefix = Durchrasten.prefix;
		String NoPerms = Prefix + "§cKeine Rechte!";
		
		File CO = new File("plugins/CaseOpening/CaseOpening.yml");
		YamlConfiguration yCO = YamlConfiguration.loadConfiguration(CO);
		
		if(sender instanceof Player) {
			
			Player p = (Player) sender;
			
			if(p.hasPermission("system.case")) {
				if(args.length == 2) {
					if(args[0].equalsIgnoreCase("edit")) {
						if(args[1].equalsIgnoreCase("Episch")) {
							
							Inventory editepisch = Bukkit.createInventory(null, 9*5, "§6Case §8| §5§lEpisch");
							
							Integer ii = 0;
							
							while(ii != 100) {
								ii++;
								if(yCO.getString("Truhe" + ".Episch" + ".Preise" + "." + ii) != null) {
									
									editepisch.setItem(ii-1, yCO.getItemStack("Truhe" + ".Episch" + ".Preise" + "." + ii));
									
								}
							}
							
							p.openInventory(editepisch);
							
						} else if(args[1].equalsIgnoreCase("Hero")) {
							
							Inventory editSupreme = Bukkit.createInventory(null, 9*5, "§6Case §8| §c§lHero");
							
							Integer ii = 0;
							
							while(ii != 100) {
								ii++;
								if(yCO.getString("Truhe" + ".Hero" + ".Preise" + "." + ii) != null) {
									
									editSupreme.setItem(ii-1, yCO.getItemStack("Truhe" + ".Hero" + ".Preise" + "." + ii));
									
								}
							}
							
							p.openInventory(editSupreme);
							
						} else if(args[1].equalsIgnoreCase("Vote")) {
			               Inventory editSupreme = Bukkit.createInventory(null, 9*5, "§6Case §8| §7Vote");
							
							Integer ii = 0;
							
							while(ii != 100) {
								ii++;
								if(yCO.getString("Truhe" + ".Vote" + ".Preise" + "." + ii) != null) {
									
									editSupreme.setItem(ii-1, yCO.getItemStack("Truhe" + ".Vote" + ".Preise" + "." + ii));
									
								}
							}
							
							p.openInventory(editSupreme);
						}else if(args[1].equalsIgnoreCase("Frühling")) {
				               Inventory editSupreme = Bukkit.createInventory(null, 9*5, "§6Case §8| §a§lFrühling");
								
								Integer ii = 0;
								
								while(ii != 100) {
									ii++;
									if(yCO.getString("Truhe" + ".Vote" + ".Preise" + "." + ii) != null) {
										
										editSupreme.setItem(ii-1, yCO.getItemStack("Truhe" + ".Frühling" + ".Preise" + "." + ii));
										
									}
								}
								
								p.openInventory(editSupreme);
						}else {
							p.sendMessage(Prefix + "§cBenutze: §7/Case <edit/add> <Vote/Episch/Hero/Frühling> <Spieler> <Anzahl>");
						}
					} else {
						p.sendMessage(Prefix + "§cBenutze: §7/Case <edit/add> <Vote/Episch/Hero/Frühling> <Spieler> <Anzahl>");
					}
				} else if(args.length == 4) {
					if(args[0].equalsIgnoreCase("add")) {
						if(args[1].equalsIgnoreCase("episch")) {
							
							String target = args[2];
							Player tar = Bukkit.getPlayer(target);
							
							if(tar != null) {
								
								String AnzahlS = args[3];
								
								if(AnzahlS.matches("[0-9]+")) {
									
									Integer AnzahlI = Integer.valueOf(AnzahlS);
									
									p.sendMessage(Prefix + "§7Du hast dem Spieler §a" + tar.getName() + " §7" + AnzahlI + " §5epische Truhe(n) §7gegeben.");
									
									tar.sendMessage(Prefix + "§7Der Spieler §a" + p.getName() + " §7hat dir " + AnzahlI + " §5epische Truhe(n) §7gegeben.");
									
									Cases.AddCase(tar, AnzahlI, "E");
								
								} else {
									p.sendMessage(Prefix + "§a" + AnzahlS + " §cist keine Zahl!");
								}
							} else {
								p.sendMessage(Prefix + "§cDer Spieler ist nicht online!");
							}
						} else if(args[1].equalsIgnoreCase("Hero")) {
							
							String target = args[2];
							Player tar = Bukkit.getPlayer(target);
							
							if(tar != null) {
								
								String AnzahlS = args[3];
								
								if(AnzahlS.matches("[0-9]+")) {
									
									Integer AnzahlI = Integer.valueOf(AnzahlS);
									
									p.sendMessage(Prefix + "§7Du hast dem Spieler §a" + tar.getName() + " §7" + AnzahlI + " §c§lCedy Truhe(n) §7gegeben.");
									
									tar.sendMessage(Prefix + "§7Der Spieler §a" + p.getName() + " §7hat dir " + AnzahlI + " §c§lCedy Truhe(n) §7gegeben.");
									
									Cases.AddCase(tar, AnzahlI, "H");
								
								} else {
									p.sendMessage(Prefix + "§a" + AnzahlS + " §cist keine Zahl!");
								}
							} else {
								p.sendMessage(Prefix + "§cDer Spieler ist nicht online!");
							}
						} else if(args[1].equalsIgnoreCase("Frühling")) {
							String target = args[2];
							Player tar = Bukkit.getPlayer(target);
							
							if(tar != null) {
								
								String AnzahlS = args[3];
								
								if(AnzahlS.matches("[0-9]+")) {
									
									Integer AnzahlI = Integer.valueOf(AnzahlS);
									
									p.sendMessage(Prefix + "§7Du hast dem Spieler §a" + tar.getName() + " §7" + AnzahlI + " §a§lFrühling Truhe(n) §7gegeben.");
									
									tar.sendMessage(Prefix + "§7Der Spieler §a" + p.getName() + " §7hat dir " + AnzahlI + " §a§lFrühling Truhe(n) §7gegeben.");
									
									Cases.AddCase(tar, AnzahlI, "F");
								
								} else {
									p.sendMessage(Prefix + "§a" + AnzahlS + " §cist keine Zahl!");
								}
							} else {
								p.sendMessage(Prefix + "§cDer Spieler ist nicht online!");
							}
						} else if(args[1].equalsIgnoreCase("Vote")) {
							String target = args[2];
							Player tar = Bukkit.getPlayer(target);
							
							if(tar != null) {
								
								String AnzahlS = args[3];
								
								if(AnzahlS.matches("[0-9]+")) {
									
									Integer AnzahlI = Integer.valueOf(AnzahlS);
									
									p.sendMessage(Prefix + "§7Du hast dem Spieler §a" + tar.getName() + " §7" + AnzahlI + " §7Vote Truhe(n) §7gegeben.");
									
									tar.sendMessage(Prefix + "§7Der Spieler §a" + p.getName() + " §7hat dir " + AnzahlI + " §7Vote Truhe(n) §7gegeben.");
									
									Cases.AddCase(tar, AnzahlI, "V");
								
								} else {
									p.sendMessage(Prefix + "§a" + AnzahlS + " §cist keine Zahl!");
								}
							} else {
								p.sendMessage(Prefix + "§cDer Spieler ist nicht online!");
							}
						} else {
							p.sendMessage(Prefix + "§cBenutze: §7/Case <edit/add> <Vote/Episch/Hero/Frühling> <Spieler> <Anzahl>");
						}
					} else {
						p.sendMessage(Prefix + "§cBenutze: §7/Case <edit/add> <Vote/Episch/Hero/Frühling> <Spieler> <Anzahl>");
					}
				} else {
					p.sendMessage(Prefix + "§cBenutze: §7/Case <edit/add> <Vote/Episch/Hero/Frühling> <Spieler> <Anzahl>");
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
