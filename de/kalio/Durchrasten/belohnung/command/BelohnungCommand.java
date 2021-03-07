package de.kalio.Durchrasten.belohnung.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import de.kalio.Durchrasten.Durchrasten;

public class BelohnungCommand implements CommandExecutor {

	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if(!(sender instanceof Player)) {
			
			return true;
		}
		Durchrasten.belohnung.openInventory((Player)sender);
		return false;
	}
}
