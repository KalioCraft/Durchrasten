package de.kalio.Durchrasten.cases.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.kalio.Durchrasten.Durchrasten;
import de.kalio.Durchrasten.utils.ItemBuilder;




public class SetCaseBlock_Command implements CommandExecutor {

	private static Durchrasten plugin;

	@SuppressWarnings("static-access")
	public SetCaseBlock_Command(Durchrasten plugin) {
		this.plugin = plugin;
		plugin.getCommand("setcaseblock").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		String Prefix = "§b§lKisten §8| §7";
		String NoPerms = Prefix + "§cKeine Rechte!";
		
		if(sender instanceof Player) {
			
			Player p = (Player) sender;
			
			if(p.hasPermission("system.case")) {
				
				p.sendMessage(Prefix + "§aDu hast die Truhe bekommen!");
				p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
				
				p.getInventory().setItemInHand(ItemBuilder.createItem(Material.CHEST, "§6§lCaseOpening", 1, new String[] {"§7Setze die Location, der Kiste!"}));
				
			} else {
				p.sendMessage(NoPerms);
			}
		} else {
			Bukkit.getConsoleSender().sendMessage(Prefix + "§cDen Command kann die Console nicht ausfuehren!");
		}
		
		return true;
	}

}