package de.kalio.Durchrasten;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import de.kalio.Durchrasten.bank.BankUtil;
import de.kalio.Durchrasten.bank.command.BankCommand;
import de.kalio.Durchrasten.bank.listener.BankListener;
import de.kalio.Durchrasten.belohnung.BelohnungUtil;
import de.kalio.Durchrasten.belohnung.command.BelohnungCommand;
import de.kalio.Durchrasten.cases.command.CaseShop_Command;
import de.kalio.Durchrasten.cases.command.Case_Command;
import de.kalio.Durchrasten.cases.command.CreateCaseItem_Command;
import de.kalio.Durchrasten.cases.command.SetCaseBlock_Command;
import de.kalio.Durchrasten.cases.listener.CaseOpening_Listener;
import de.kalio.Durchrasten.clan.ClanUtil;
import de.kalio.Durchrasten.clan.command.ClanCommand;
import de.kalio.Durchrasten.config.ConfigManager;
import de.kalio.Durchrasten.jobs.Jobs;
import de.kalio.Durchrasten.jobs.command.JobsCommand;
import de.kalio.Durchrasten.perks.PerkUtil;
import de.kalio.Durchrasten.perks.command.PerkCommand;
import de.kalio.Durchrasten.prefix.PrefixUtil;
import de.kalio.Durchrasten.prefix.command.PrefixCommand;
import de.kalio.Durchrasten.scoreboard.ScoreboardManager;
import de.kalio.Durchrasten.warps.WarpUtil;
import de.kalio.Durchrasten.warps.command.WarpCommand;
import net.milkbowl.vault.economy.Economy;

public class Durchrasten extends JavaPlugin {

	
	
	public static ClanUtil clan;
	public static String prefix;
	private static File cachefile;
	public static YamlConfiguration cache;
	public static BankUtil bank;
	public static Economy economy;
	public static WarpUtil warp;
	public static BelohnungUtil belohnung;
	public static PerkUtil perk;
	public static ScoreboardManager scoreboard;

	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		this.prefix = ConfigManager.getString("prefix");
		this.cachefile = new File("plugins/Durchrasten/cache.yml");
		this.cache = YamlConfiguration.loadConfiguration(cachefile);
		this.bank = new BankUtil();
		this.warp = new WarpUtil();
		this.perk = new PerkUtil();
		this.belohnung = new BelohnungUtil();
		this.clan = new ClanUtil();
		this.scoreboard = new ScoreboardManager();
	
		

		Bukkit.getConsoleSender().sendMessage("§6$$$$$$$\\                                $$\\                                      $$\\                         \r\n"
				+ "$$  __$$\\                               $$ |                                     $$ |                        \r\n"
				+ "$$ |  $$ |$$\\   $$\\  $$$$$$\\   $$$$$$$\\ $$$$$$$\\   $$$$$$\\  $$$$$$\\   $$$$$$$\\ $$$$$$\\    $$$$$$\\  $$$$$$$\\  \r\n"
				+ "$$ |  $$ |$$ |  $$ |$$  __$$\\ $$  _____|$$  __$$\\ $$  __$$\\ \\____$$\\ $$  _____|\\_$$  _|  $$  __$$\\ $$  __$$\\ \r\n"
				+ "$$ |  $$ |$$ |  $$ |$$ |  \\__|$$ /      $$ |  $$ |$$ |  \\__|$$$$$$$ |\\$$$$$$\\    $$ |    $$$$$$$$ |$$ |  $$ |\r\n"
				+ "$$ |  $$ |$$ |  $$ |$$ |      $$ |      $$ |  $$ |$$ |     $$  __$$ | \\____$$\\   $$ |$$\\ $$   ____|$$ |  $$ |\r\n"
				+ "$$$$$$$  |\\$$$$$$  |$$ |      \\$$$$$$$\\ $$ |  $$ |$$ |     \\$$$$$$$ |$$$$$$$  |  \\$$$$  |\\$$$$$$$\\ $$ |  $$ |\r\n"
				+ "\\_______/  \\______/ \\__|       \\_______|\\__|  \\__|\\__|      \\_______|\\_______/    \\____/  \\_______|\\__|  \\__|\r\n"
				+ "                                                                                                             \r\n"
				+ "                                                                                                             \r\n"
				+ "                                                                                                             ");
		RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
	    economy = (Economy)rsp.getProvider();
	    init();
		Bukkit.getOnlinePlayers().forEach(player -> {
			player.kickPlayer(prefix + "§7Server lädt neu!");
		});
	}
	
	
	public static boolean hasPlaceHolderAPI() {
		if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
			return false;
		}
		return true;
	}
	
	public void init() {
		listener(scoreboard);
		command("bank", new BankCommand());
		command("clan", new ClanCommand());
		listener(new Jobs());
		listener(warp);
		listener(belohnung);
		if(getConfig().getBoolean("prefixes.use")) {
			command("prefix", new PrefixCommand());
			listener(new PrefixUtil());
		}
		command("belohnung", new BelohnungCommand());
		listener(perk);
		command("perks", new PerkCommand());
		command("sw", new WarpCommand());
		command("jobs", new JobsCommand());
		listener(new BankListener());
		new Case_Command(this);
		new SetCaseBlock_Command(this);
		new CreateCaseItem_Command(this);
		new CaseShop_Command(this);
		new CaseOpening_Listener(this);
	}
	
	public void command(String name, CommandExecutor executor) {
		getCommand(name).setExecutor(executor);
	}
	
	@Override
	public void onDisable() {
		Bukkit.getOnlinePlayers().forEach(player -> {
			player.kickPlayer(prefix + "§7Server lädt neu!");
		});
	}
	
	public void listener(Listener listener) {
		Bukkit.getPluginManager().registerEvents(listener, this);
	}
	
	
	
	public static YamlConfiguration getCache() {
		return cache;
	}
	
	public static void saveCache() {
		try {
			cache.save(cachefile);
		}catch(IOException ex) {
			exception(ex);
		}
	}
	
	public static void exception(Exception ex) {
		Bukkit.getConsoleSender().sendMessage("§cFehler im Plugin §6§lDurchrasten§8: §7"+ex.getMessage());
	}
	
}
