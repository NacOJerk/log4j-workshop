package com.kirelcodes.log4j;

import org.bukkit.plugin.java.JavaPlugin;

public class LogManager extends JavaPlugin {

	@Override
	public void onEnable() {
		getLogger().info("onEnable is called!");
		getLogger().info(String.format("Server IP is: %s", System.getenv("SERVER_IP")));
		getLogger().info(String.format("Client Image is: %s", System.getenv("CLIENT_IMAGE")));
		getServer().getPluginManager().registerEvents(new PlayerListener(System.getenv("SERVER_IP"), 25565, System.getenv("CLIENT_IMAGE")), this);
	}

	@Override
	public void onDisable() { 
		getLogger().info("onDisable is called!");
	}
}

