package com.kirelcodes.log4j;

import org.bukkit.plugin.java.JavaPlugin;

public class LogManager extends JavaPlugin {

	@Override
	public void onEnable() {
		getLogger().info("onEnable is called!");
		getServer().getPluginManager().registerEvents(new PlayerListener(System.getenv("SERVER_IP"), 25565), this);
	}

	@Override
	public void onDisable() {
		getLogger().info("onDisable is called!");
	}
}

