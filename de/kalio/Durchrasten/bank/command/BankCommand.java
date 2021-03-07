package de.kalio.Durchrasten.bank.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.kalio.Durchrasten.Durchrasten;
import de.kalio.Durchrasten.bank.BankUtil;

public class BankCommand implements CommandExecutor {

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Durchrasten.prefix+"§cDu musst ein Spieler sein!");
			return true;
		}
		Player player = (Player) sender;
		if(args.length == 0) {
			player.sendMessage(Durchrasten.prefix + "§7Unbekannter Bank Befehl. Benutze §6/bank help §7für Hilfe!");
			return true;
		}
		if(args.length >= 1) {
			if(args[0].equalsIgnoreCase("help")) {
				player.sendMessage(Durchrasten.prefix + "§8§m-----------------------------------");
				player.sendMessage(Durchrasten.prefix + "");
				player.sendMessage(Durchrasten.prefix + "§6§l/bank einzahlen <Betrag> §8➜ §7Zahle geld ein!");
				player.sendMessage(Durchrasten.prefix + "§6§l/bank auszahlen <Betrag> §8➜ §7Zahle geld aus!");
				player.sendMessage(Durchrasten.prefix + "");
				player.sendMessage(Durchrasten.prefix + "§6§l/bank geld §8➜ §7Sehe dein Geld auf deiner Bank!");
				player.sendMessage(Durchrasten.prefix + "");
				player.sendMessage(Durchrasten.prefix + "§8§m-----------------------------------");
				return true;
			}
			if(args[0].equalsIgnoreCase("geld")) {
				player.sendMessage(Durchrasten.prefix + "§7Dein Geld auf der Bank §8➜ §6§l"+Durchrasten.bank.getValue(player));
				return true;
			}
			
			if(args[0].equalsIgnoreCase("einzahlen")) {
				if(args.length == 2) {
					Double d;
					try {
						d = Double.parseDouble(args[1]);
					}catch(Exception ex) {
						player.sendMessage(Durchrasten.prefix + "§7Bitte gebe eine Zahl an!");
						return true;
					}
					if(!(Durchrasten.economy.getBalance(player) >= d)) {
						player.sendMessage(Durchrasten.prefix + "§7Du hast nicht genügend Geld, für diese Aktion!");
						return true;
					}
					
					Durchrasten.economy.withdrawPlayer(player, d);
					Durchrasten.bank.setValue(player, Durchrasten.bank.getValue(player) + d);
					player.sendMessage(Durchrasten.prefix + "§7Du hast §6§l"+d+"§7 in die Bank eingezahlt!");
				}else {
					player.sendMessage(Durchrasten.prefix + "§7Benutze §8➜ §6§l/bank einzahlen <Betrag>");
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("auszahlen")) {
				if(args.length == 2) {
					Double d;
					try {
						d = Double.parseDouble(args[1]);
					}catch(Exception ex) {
						player.sendMessage(Durchrasten.prefix + "§7Bitte gebe eine Zahl an!");
						return true;
					}
					if(!(Durchrasten.bank.getValue(player) >= d)) {
						player.sendMessage(Durchrasten.prefix + "§7Du hast nicht genügend Geld, in deiner Bank, für diese Aktion!");
						return true;
					}
					
					Durchrasten.economy.depositPlayer(player, d);
					Durchrasten.bank.setValue(player, Durchrasten.bank.getValue(player) - d);
					player.sendMessage(Durchrasten.prefix + "§7Du hast §6§l"+d+"§7 aus der Bank ausgezahlt!");
					
				}else {
					player.sendMessage(Durchrasten.prefix + "§7Benutze §8➜ §6§l/bank auszahlen <Betrag>");
				}
				return true;
			}
			player.sendMessage(Durchrasten.prefix + "§7Unbekannter Bank Befehl. Benutze §6/bank help §7für Hilfe!");
		}
		return false;
	}

}
