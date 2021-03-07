package de.kalio.Durchrasten.utils;


import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Cases {
	
	public static void AddCase(Player p, int anzahl, String cases) {
		
		File file = new File("plugins/CaseOpening/Cases.yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		if(cfg.getString(p.getUniqueId() + ".Case" + "." + cases) == null) {
			cfg.set(p.getUniqueId() + ".Case" + "." + cases, 0);
			try {
				cfg.save(file);
			} catch (IOException e) {}
				int i = cfg.getInt(p.getUniqueId() + ".Case" + "." + cases);
				int total = i + anzahl;
				cfg.set(p.getUniqueId() + ".Case" + "." + cases, total);
				try {
					cfg.save(file);
				} catch (IOException e1) {}
			} else {
				try {
					cfg.save(file);
				} catch (IOException e) {}
					int i = cfg.getInt(p.getUniqueId() + ".Case" + "." + cases);
					int total = i + anzahl;
					cfg.set(p.getUniqueId() + ".Case" + "." + cases, total);
					try {
						cfg.save(file);
					} catch (IOException e1) {}
			}
		}
	
	public static int getCase(Player p, String cases) {
		
		File file = new File("plugins/CaseOpening/Cases.yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		int i = 0;
		if(cfg.getString(p.getUniqueId() + ".Case" + "." + cases) == null) {
			i = 0;
		} else {
			i = cfg.getInt(p.getUniqueId() + ".Case" + "." + cases);
		}
		return i;
	}
	
	public static void RemoveCase(Player p, int anzahl, String cases) {
		
		File file = new File("plugins/CaseOpening/Cases.yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		if(cfg.getString(p.getUniqueId() + ".Case" + "." + cases) == null) {
			cfg.set(p.getUniqueId() + ".Case" + "." + cases, 0);
			try {
				cfg.save(file);
			} catch (IOException e) {}
				int i = cfg.getInt(p.getUniqueId() + ".Case" + "." + cases);
				int total = i + anzahl;
				cfg.set(p.getUniqueId() + ".Case" + "." + cases, total);
				try {
					cfg.save(file);
				} catch (IOException e1) {}
			} else {
				try {
					cfg.save(file);
				} catch (IOException e) {}
					int i = cfg.getInt(p.getUniqueId() + ".Case" + "." + cases);
					int total = i - anzahl;
					cfg.set(p.getUniqueId() + ".Case" + "." + cases, total);
					try {
						cfg.save(file);
					} catch (IOException e1) {}
			}
		}
	
	}
