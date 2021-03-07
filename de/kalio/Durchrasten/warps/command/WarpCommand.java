package de.kalio.Durchrasten.warps.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.kalio.Durchrasten.Durchrasten;
public class WarpCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Durchrasten.prefix + "§7Du musst ein Spieler sein!");
			return true;
		}
		Player player = (Player) sender;
		if(args.length == 0) {
			player.sendMessage(Durchrasten.prefix + "§7Unbekannter Befehl. Benutze §6§l/sw help §7für Hilfe!");
			return true;
		}
		if(args.length >= 1) {
			String sub = args[0];
			if(sub.equalsIgnoreCase("help")) {
				sendHelp(player);
				return true;
			} else if(sub.equalsIgnoreCase("create")) {
				if(!(args.length == 2)) {
					syntax(player, "sw create <Name>");
					return true;
				}
				String name = args[1];
				if(Durchrasten.warp.hasPlayerWarp(player.getUniqueId().toString())) {
					player.sendMessage(Durchrasten.prefix + "§7Du hast bereits ein Warp!");
					return true;
				}
				Durchrasten.warp.createWarp(player.getLocation(), player.getUniqueId().toString(), name);
				player.sendMessage(Durchrasten.prefix + "§7Du hast den Warp §6§l"+name+" §7erstellt!");
				return true;
			} else if(sub.equalsIgnoreCase("menu")) {
				Durchrasten.warp.openInvetory(player);
			} else if(sub.equalsIgnoreCase("teleport")) {
				if(!(args.length == 2)) {
					syntax(player, "sw teleport <Name>");
					return true;
				}
				String name = args[1];
				if(!Durchrasten.warp.warpExist(name)) {
					
					player.sendMessage(Durchrasten.prefix + "§7Der Warp existiert nicht!");
					return true;
				}
				Durchrasten.warp.teleport(player, name);
			} else if(sub.equalsIgnoreCase("wartung")) {
				
				if(!(args.length == 2)) {
					syntax(player, "sw wartung <Aus/An>");
					return true;
				}
				if(!Durchrasten.warp.hasPlayerWarp(player.getUniqueId().toString())) {
					player.sendMessage(Durchrasten.prefix + "§7Du hast kein Warp!");
					return true;
				}
				String bool = args[1];
			    Boolean b;
			    if(bool.equalsIgnoreCase("aus")) {
			    	b = false;
			    } else if(bool.equalsIgnoreCase("an")) {
			    	b = true;
			    } else {
					syntax(player, "sw wartung <Aus/An>");
			    	return true;
			    }
			    boolean wartung = Durchrasten.warp.getWartung(Durchrasten.warp.getPlayerWarp(player.getUniqueId().toString()));
			    if(b == false && wartung == false) {
			    	player.sendMessage(Durchrasten.prefix + "§7Die Wartung sind bereits Aus!");
			    	return true;
			    }
			    if(b == true && wartung == true) {
			    	player.sendMessage(Durchrasten.prefix + "§7Die Wartung sind bereits Aus!");
			    	return true;
			    }
			    Durchrasten.warp.setWartung(b, Durchrasten.warp.getPlayerWarp(player.getUniqueId().toString()));
			    player.sendMessage(Durchrasten.prefix + "§7Du hast die Wartungen umgeschaltet!");
			    return true;
			} else if(sub.equalsIgnoreCase("delete")) {
				if(!Durchrasten.warp.hasPlayerWarp(player.getUniqueId().toString())) {
					player.sendMessage(Durchrasten.prefix + "§7Du hast kein Warp!");
					return true;
				}
				Durchrasten.warp.deleteWarp(player.getUniqueId().toString());
				player.sendMessage(Durchrasten.prefix + "§7Du hast den Warp gelöscht!");
				return true;
			}
			
		}
		player.sendMessage(Durchrasten.prefix + "§7Unbekannter Befehl. Benutze §6§l/sw help §7für Hilfe!");
		return false;
	}
	
	public void syntax(Player player, String syntax) {
		player.sendMessage(Durchrasten.prefix + "§7Benutze: §6§l/"+syntax);
	}
	
	public void sendHelp(Player player) {
		player.sendMessage(Durchrasten.prefix + "§8§m--------------------------");
		player.sendMessage(Durchrasten.prefix + "§6§l/sw create <Name> §8➜ §7Erstelle einen Spieler Warp!");
		player.sendMessage(Durchrasten.prefix + "§6§l/sw teleport <Name> §8➜ §7Teleportiere dich zu ein Warp!");
		player.sendMessage(Durchrasten.prefix + "§6§l/sw delete §8➜ §7Lösche deinen Warp!");
		player.sendMessage(Durchrasten.prefix + "§6§l/sw menu §8➜ §7Öffne das Menü, wo du alle Warps siehst!");
		player.sendMessage(Durchrasten.prefix + "§6§l/sw wartung <Aus/An> §8➜ §7Mache die Wartungen aus oder an!");
		player.sendMessage(Durchrasten.prefix + "§8§m--------------------------");
	}

	
	
	
}
