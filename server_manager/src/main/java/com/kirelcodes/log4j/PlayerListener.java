package com.kirelcodes.log4j;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerListener implements Listener {
	private static String BOT_NAME = "%s_dummy";
	private static String CONTAINER_NAME = "%s_container";
	private String HOST;
	private int PORT;
	List<String> bots;
	public PlayerListener(String host, int port){
		this.HOST = host;
		this.PORT = port;
		bots = new ArrayList<String>();
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		String player = event.getPlayer().getName();
		if(bots.contains(player)){
			event.getPlayer().setGameMode(GameMode.CREATIVE);
		}
		else {
			try {
				Runtime.getRuntime()
						.exec(String.format("docker run --name " + CONTAINER_NAME + " hacked_client %s " +
													 "%d " + BOT_NAME + " %s", player, HOST, PORT, player,
													 player));
				bots.add(String.format(BOT_NAME, player));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event){
		String player = event.getPlayer().getName();
		if(bots.contains(player)){
			event.getPlayer().setGameMode(GameMode.SURVIVAL);
		}
		else {
			try {
				if(Bukkit.getPlayer(String.format(BOT_NAME, player)) != null)
					Bukkit.getPlayer(String.format(BOT_NAME, player)).kickPlayer("bye");
				Runtime.getRuntime()
						.exec(String.format("docker rm -f " + CONTAINER_NAME, player));
				bots.remove(String.format(BOT_NAME, player));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
