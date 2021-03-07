package de.kalio.Durchrasten.bank.listener;


import org.bukkit.event.player.PlayerJoinEvent;

import de.kalio.Durchrasten.Durchrasten;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BankListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Durchrasten.bank.join(event);
	}

}
