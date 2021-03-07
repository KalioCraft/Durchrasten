package de.kalio.Durchrasten.perks.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.kalio.Durchrasten.Durchrasten;

public class PerkCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command command, String arg2, String[] args) {
		if(!(cs instanceof Player)) {
			return true;
		}
		Durchrasten.perk.openGUI((Player)cs);
		
		return false;
	}

}
