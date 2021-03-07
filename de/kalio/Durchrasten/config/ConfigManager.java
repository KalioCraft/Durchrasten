package de.kalio.Durchrasten.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.kalio.Durchrasten.Durchrasten;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;

public class ConfigManager {

	

	private static Durchrasten instance = Durchrasten.getPlugin(Durchrasten.class);
	
	public static String getPrefix() {
		return ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("prefix"));
	}
	
	public static String getString(String path) {
			return ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString(path));
		
		
	}
	
	
	
	
	public static Object get(String path) {
		return instance.getConfig().get(path);
	}
	public static List<String>getStringList(HashMap<String, Object> placeholders, String path){
		
		List<String>list = instance.getConfig().getStringList(path);
		List<String>rtList = new ArrayList<String>();
		for(String str : list) {
			String strPlaceholders = ChatColor.translateAlternateColorCodes('&', str);
			for(String placeholder : placeholders.keySet()) {
				strPlaceholders.replaceAll(placeholder, placeholders.get(placeholder).toString());
			}
			
			rtList.add(strPlaceholders);
		}
		
		return rtList;
		
		
	}
	
	public static List<String>getStringList(String path){
		
		List<String>list = instance.getConfig().getStringList(path);
		List<String>rtList = new ArrayList<String>();
		for(String str : list) {
			String strPlaceholders = ChatColor.translateAlternateColorCodes('&', str);
			rtList.add(strPlaceholders);
		}
		
		return rtList;
	}
	

	
}
