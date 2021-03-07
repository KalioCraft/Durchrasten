package de.kalio.Durchrasten.clan.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.List;
import de.kalio.Durchrasten.Durchrasten;
import de.kalio.Durchrasten.utils.UUIDFetcher;


public class ClanCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(Durchrasten.prefix+"§7Du musst ein Spieler sein!");
			return true;
		}
		
		Player player = (Player) sender;
		if(args.length == 0) {
			unkownCommand(player);
			return true;
		}
		
		if(args.length >= 1) {
			String sub = args[0];
			if(sub.equalsIgnoreCase("help"))  {
				sendHelp(player);
				return true;
			}
			if(sub.equalsIgnoreCase("message")) {
				if(Durchrasten.clan.getClan(player.getUniqueId().toString()) == null) {
					
					player.sendMessage(Durchrasten.prefix + "§7Du bist in keinen Clan!");
					return true;
				}
				
				String msg = "";
				for(int i = 1; i != args.length; i++) {
					msg += args[i] + " ";
				}
				Durchrasten.clan.sendMessage(player.getUniqueId().toString(), msg);
				return true;
				
			}else if(sub.equalsIgnoreCase("create")) {
				if(args.length != 2) {
					
				    syntax(player, "/clan create <Name>");
					return true;
				}
				String ClanName = args[1];
				if(!(ClanName.length() >= 1) && ClanName.length() <= 4) {
					player.sendMessage(Durchrasten.prefix + "§7Bitte benutze einen Namen zwischen 1 und 4 Zeichen!");
					return true;
				}
				
				if(Durchrasten.economy.getBalance(player) < 50000) {
					player.sendMessage(Durchrasten.prefix + "§7Du hast nicht genug Geld!");
					return true;
				}
				
				Durchrasten.economy.withdrawPlayer(player, 50000);
				Durchrasten.clan.create(ClanName, player.getUniqueId().toString());
				player.sendMessage(Durchrasten.prefix + "§7Du hast den Clan §6§l"+ClanName+"§7 erstellt!");
				
	
			} else if(sub.equalsIgnoreCase("invite")) { 
				if(!(Durchrasten.clan.isPlayerInClan(player.getUniqueId().toString()))) {
					player.sendMessage(Durchrasten.prefix + "§7Du bist in keinem Clan!");
					return true;
				}
				if(!(args.length == 2)) {
					syntax(player, "clan invite <Spieler>");
					return true;
				}
				if(!Durchrasten.clan.isOwner(player.getUniqueId().toString())) {
					player.sendMessage(Durchrasten.prefix + "§7Du musst Besitzer des Clanes sein!");
					return true;
					}
				
				String playerName = args[1];
				Player target = Bukkit.getPlayer(playerName);
				if(target == null) {
					player.sendMessage(Durchrasten.prefix + "§7Der Spieler §6§l"+playerName + "§7 ist nicht online!");
					return true;
				}
				
				if(Durchrasten.clan.getClan(target.getUniqueId().toString()).equals(Durchrasten.clan.getClan(player.getUniqueId().toString()))) {
					player.sendMessage(Durchrasten.prefix + "§7Der Spieler ist bereits in deinen Clan!");
					return true;
				}
				
				if(Durchrasten.clan.getInvites(target.getUniqueId().toString()) != null) {
					for(String str : Durchrasten.clan.getInvites(target.getUniqueId().toString())) {
						if(str.contains(player.getName())) { 
							player.sendMessage(Durchrasten.prefix + "§7Du hast diesen Spieler bereits eine Clan Anfrage gesendet!");
							return true;
						}
					}
				}
				
				Durchrasten.clan.addInvite(target.getUniqueId().toString(), player);
				player.sendMessage(Durchrasten.prefix + "§7Du hast den Spieler §6§l"+playerName + "§7 eine Clan Einladung geschickt!");
				target.sendMessage(Durchrasten.prefix + "§7Du hast eine Clan Einladung zu den Clan §6§l" + Durchrasten.clan.getClan(player.getUniqueId().toString()) + "§7 erhalten. Akzeptiere sie mit /clan accept "+player.getName());
				
			}else if(sub.equalsIgnoreCase("accept")) {
				
				if(args.length != 2) {
					syntax(player, "clan accept <Invite>");
					return true;
				}
				
				String accepter = args[1];
				if(Durchrasten.clan.getInvites(player.getUniqueId().toString()) == null) {
					player.sendMessage(Durchrasten.prefix + "§7Dieser Spieler hat dir keine Einladung gesendet!");
					return true;
				}
				
				boolean found = false;
				
				for(String str : Durchrasten.clan.getInvites(player.getUniqueId().toString())) {
					if(str.contains(accepter)) {
						found = true;
					}
				}
				
				if(!(found)) {
					player.sendMessage(Durchrasten.prefix + "§7Dieser Spieler hat dir keine Einladung gesendet!");
					return true;
				}
				
				if(Durchrasten.clan.getClan(player.getUniqueId().toString()) != null) {
					player.sendMessage(Durchrasten.prefix + "§7Du bist bereits in einen Clan!");
					return true;
				}
				
				Durchrasten.clan.removeInvite(player.getUniqueId().toString(), accepter);
				String clan = Durchrasten.clan.getClan(accepter);
				if(clan == null) {
					player.sendMessage(Durchrasten.prefix + "§7Dieser Spieler ist in kein Clan!");
					return true;
				}
				
				if(Durchrasten.clan.getMembers(UUIDFetcher.getUUID(accepter).toString()).size() >= (Durchrasten.clan.getClanLevel(UUIDFetcher.getUUID(accepter).toString()) * 10)) {
					player.sendMessage(Durchrasten.prefix + "§7Dieser Clan hat die Maximalen Mitglieder erreicht!");
					return true;
				}
				
				Durchrasten.clan.joinClan(player.getUniqueId().toString(), Durchrasten.clan.getClan(UUIDFetcher.getUUID(accepter).toString()));
				
				player.sendMessage(Durchrasten.prefix + "§7Du bist den Clan §6§l"+Durchrasten.clan.getClan(UUIDFetcher.getUUID(accepter).toString())+ "§7 beigetreten!");
				Durchrasten.clan.sendMessage(player.getUniqueId().toString(), "§7Hey! Ich bin neu hier :)");
				Durchrasten.clan.check(player.getUniqueId().toString());
				
			}else if(sub.equalsIgnoreCase("invites")) {
				
				List<String> invites = Durchrasten.clan.getInvites(player.getUniqueId().toString());
				if(invites == null || invites.isEmpty()) {
					player.sendMessage(Durchrasten.prefix + "§7Du hast keine Invites!");
					return true;
				}

				String inviteList = "";
				
				for(String invite : invites) {
					String playerInviter = invite.split("|")[1];
					inviteList += invite+", ";
				}
				
				player.sendMessage(Durchrasten.prefix + "§7Du hast Einladungen von folgenden Clans: §e"+inviteList);
				return true;
			} else if(sub.equalsIgnoreCase("deny")) {
				if(args.length != 2) {
					syntax(player, "clan deny <Invite>");
					return true;
				}
				String invite = args[1];
				if(Durchrasten.clan.getInvites(player.getUniqueId().toString()) == null) {
					player.sendMessage(Durchrasten.prefix + "§7Du hast keine Clan Invites!");
					return true;
				}
				for(String str : Durchrasten.clan.getInvites(player.getUniqueId().toString())) {
					if(str.split("|")[0].equalsIgnoreCase(invite)) {
						Durchrasten.clan.removeInvite(player.getUniqueId().toString(), invite);
					}
				}
				player.sendMessage(Durchrasten.prefix + "§7Du hast den Invite §6§l"+invite+"§7 abgelehnt!");
			} else if(sub.equalsIgnoreCase("leave")) {
				if(Durchrasten.clan.getClan(player.getUniqueId().toString()) == null) {
					player.sendMessage(Durchrasten.prefix + "§7Du bist in keinem Clan!");
					return true;
				}
				if(Durchrasten.clan.isOwner(player.getUniqueId().toString())) {
					Durchrasten.clan.deleteClan(player.getUniqueId().toString());
					player.sendMessage(Durchrasten.prefix + "§7Du hast den Clan gelöscht!");
					return true;
				}
				Durchrasten.clan.leaveClan(player.getUniqueId().toString());
				player.sendMessage(Durchrasten.prefix + "§7Du hast den Clan verlassen!");
				
			} else if(sub.equalsIgnoreCase("bank")) {
				if(Durchrasten.clan.getClan(player.getUniqueId().toString()) == null) {
					player.sendMessage(Durchrasten.prefix + "§7Du bist in keinem Clan!");
					return true;
				}
				if(args.length >= 2) {
					String subTwo = args[1];
					if(subTwo.equalsIgnoreCase("geld")) {
						player.sendMessage(Durchrasten.prefix + "§7Geld auf der Clan bank §8➜ §6§l"+Durchrasten.clan.getBank(player.getUniqueId().toString()));
						return true;
					}
					if(args.length == 3) {
						
						if(subTwo.equalsIgnoreCase("einzahlen")) {
							String betrag = args[2];
							Double d;
							try {
								d = Double.parseDouble(betrag);
							}catch(Exception ex) {
								player.sendMessage(Durchrasten.prefix + "§7Gebe einen gültigen wert an!");
								return true;
							}
							if(!(Durchrasten.economy.getBalance(player) >= d)) {
								player.sendMessage(Durchrasten.prefix + "§7Du hast nicht genug Geld!");
								return true;
							}
							
							if(!(Durchrasten.clan.canBank(player.getUniqueId().toString(), d))) {
								player.sendMessage(Durchrasten.prefix + "§7Dein Clan hat das Maximale an Geld in der Kasse!");
								return true;
							}
							Durchrasten.economy.withdrawPlayer(player, d);
							player.sendMessage(Durchrasten.prefix + "§7Du hast §6§l$"+d+"§7 auf die Clan Bank gezahlt!");
							Durchrasten.clan.setBank(player.getUniqueId().toString(), Durchrasten.clan.getBank(player.getUniqueId().toString()) + d);
							return true;
						} else if(subTwo.equalsIgnoreCase("auszahlen") ) {
							String betrag = args[2];
							Double d;
							try {
								d = Double.parseDouble(betrag);
							}catch(Exception ex) {
								player.sendMessage(Durchrasten.prefix + "§7Gebe einen gültigen wert an!");
								return true;
							}
							if(Durchrasten.clan.getBank(player.getUniqueId().toString()) <= d) {
								player.sendMessage(Durchrasten.prefix + "§7Die Clan Kasse hat nicht genug Geld!");
								return true;
							}
							Durchrasten.economy.depositPlayer(player, d);
							Durchrasten.clan.setBank(player.getUniqueId().toString(), Durchrasten.clan.getBank(player.getUniqueId().toString()) - d);
							player.sendMessage(Durchrasten.prefix + "§7Du hast §6§l"+d+"§7 aus der Bank gezahlt!");
							return true;
						}
				
						
					} 
				}
				syntax(player, "clan bank <Geld/Auszahlen/Einzahlen> [Betrag]");
			} else if(sub.equalsIgnoreCase("update")) {
				if(Durchrasten.clan.getClan(player.getUniqueId().toString()) == null) {
					player.sendMessage(Durchrasten.prefix + "§7Du bist in keinem Clan!");
					return true;
				}
				Integer level = Durchrasten.clan.getClanLevel(player.getUniqueId().toString());
				player.sendMessage(Durchrasten.prefix + "§7Bei den Level §6§l"+(level++)+"§7 erhälst dein Clan folgendes:");
				player.sendMessage(Durchrasten.prefix + "§7+ 10 Mitglieder ("+((level++) * 10)+")");
				player.sendMessage(Durchrasten.prefix + "§7+ 10000 Maximales Clan Kassen Geld ("+((level++)*10000)+")");
				player.sendMessage(Durchrasten.prefix + "§7Dein Clan brauch folgendes:");
				
				boolean has1 = (Durchrasten.clan.getMembers(player.getUniqueId().toString()).size() >= (Durchrasten.clan.getClanLevel(player.getUniqueId().toString()) * 10));
				player.sendMessage(Durchrasten.prefix + (has1 ? "§a🢂" : "§4🢂") + " §7"+(Durchrasten.clan.getClanLevel(player.getUniqueId().toString()) * 5) + " Mitglieder!");
				boolean has2 = Durchrasten.clan.getUpgradeBank(player.getUniqueId().toString()) >= (level++ * 10000);
				player.sendMessage(Durchrasten.prefix + (has2 ? "§a🢂" : "§4🢂") + " §7"+(level++ * 10000) + "$ in der Clan Update Bank!");
			} else if(sub.equalsIgnoreCase("updatebank")) {
				if(Durchrasten.clan.getClan(player.getUniqueId().toString()) == null) {
					player.sendMessage(Durchrasten.prefix + "§7Du bist in keinem Clan!");
					return true;
				}
				if(args.length >= 2) {
					String subTwo = args[1];
					if(subTwo.equalsIgnoreCase("geld")) {
						player.sendMessage(Durchrasten.prefix + "§7Geld auf der Updatebank §8➜ §6§l"+Durchrasten.clan.getUpgradeBank(player.getUniqueId().toString()));
						return true;
					} else if(subTwo.equalsIgnoreCase("einzahlen") && args.length == 3) {
						String betrag = args[2];
						Double d;
						try {
							d = Double.parseDouble(betrag);
						}catch(Exception ex) {
							player.sendMessage(Durchrasten.prefix + "§7Gebe einen gültigen wert an!");
							return true;
						}
						if(!(Durchrasten.economy.getBalance(player) >= d)) {
							player.sendMessage(Durchrasten.prefix + "§7Du hast nicht genug Geld!");
							return true;
						}
						
		
						Durchrasten.economy.withdrawPlayer(player, d);
						player.sendMessage(Durchrasten.prefix + "§7Du hast §6§l$"+d+"§7 auf die Clan UpdateBank gezahlt!");
						
						Durchrasten.clan.setUpgradeBank(player.getUniqueId().toString(), Durchrasten.clan.getUpgradeBank(player.getUniqueId().toString()) + d);
						Durchrasten.clan.check(player.getUniqueId().toString());
						return true;
					}
				}
				syntax(player, "clan updatebank <geld/einzahlen> [Betrag]");
			} else {
				unkownCommand(player);
				return true;
			}
		}else {
			unkownCommand(player);
			return true;
		}
		
		return false;
	}
	
	public void sendHelp(Player player) {
		player.sendMessage(Durchrasten.prefix + "§8§m-----------------------------------");
		player.sendMessage(Durchrasten.prefix + "");
		player.sendMessage(Durchrasten.prefix + "§6§l/clan message <Nachricht...> §8➜ §7Sende eine Nachricht, an jeden Clan Spieler, der Online ist!");
		player.sendMessage(Durchrasten.prefix + "§6§l/clan create <Name> §8➜ §7Erstelle einen Clan für §a$50000§7!");
		player.sendMessage(Durchrasten.prefix + "§6§l/clan leave §8➜ §7Leave deinen Aktuellen Clan!");
		player.sendMessage(Durchrasten.prefix + "§6§l/clan accept <Name> §8➜ §7Nehme eine Claneinladung an!");
		player.sendMessage(Durchrasten.prefix + "§6§l/clan invites §8➜ §7Sehe deine Clan Anfragen!");
		player.sendMessage(Durchrasten.prefix + "§6§l/clan deny <Name> §8➜ §7Lehne einen Invite ab!");
		player.sendMessage(Durchrasten.prefix + "§6§l/clan invite <Name> §8➜ §7Lade einen Spieler in deinen Clan ein!");
		player.sendMessage(Durchrasten.prefix + "§6§l/clan update §8➜ §7Sehe die nächste Belohnung, und was du dafür brauchst!");
		player.sendMessage(Durchrasten.prefix + "§6§l/clan updatebank geld §8➜ §7Sehe das Geld, was für ein Update verfügbar ist!");
		player.sendMessage(Durchrasten.prefix + "§6§l/clan updatebank einzahlen <Betrag> §8➜ §7Zahle geld in das Update ein! (/clan update)");
		player.sendMessage(Durchrasten.prefix + "§6§l/clan invite <Name> §8➜ §7Lade einen Spieler in deinen Clan ein!");
		player.sendMessage(Durchrasten.prefix + "§6§l/clan bank geld §8➜ §7Zeigt den Kontostand des Clanes an!");
		player.sendMessage(Durchrasten.prefix + "§6§l/clan bank einzahlen <Betrag> §8➜ §7Zahle was in die Clan Kasse ein!");
		player.sendMessage(Durchrasten.prefix + "§6§l/clan bank auszahlen <Betrag> §8➜ §7Zahle was aus deiner Clan Kasse aus!");
		player.sendMessage(Durchrasten.prefix + "");
		player.sendMessage(Durchrasten.prefix + "§8§m-----------------------------------");
	}
	
	public void syntax(Player player, String syntax) {
		player.sendMessage(Durchrasten.prefix + "§7Bitte benutze §8➜ §6§l" + syntax);
	}
	
	public void unkownCommand(Player player) {
		player.sendMessage(Durchrasten.prefix + "§7Unbekannter Befehl. Bitte benutze §6§l/clan help §7für Hilfe!");
	}

}
