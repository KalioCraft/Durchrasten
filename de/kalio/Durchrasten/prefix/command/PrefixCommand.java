package de.kalio.Durchrasten.prefix.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.kalio.Durchrasten.prefix.PrefixUtil;

public class PrefixCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if(!(sender instanceof Player)) {
			return true;
		}
		PrefixUtil.openGUI((Player)sender);
		return false;
	}

}
