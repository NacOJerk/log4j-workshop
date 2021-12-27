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
	private String _dockerImage;
	private String HOST;
	private int PORT;
	List<String> bots;
	public PlayerListener(String host, int port, String dockerImage){
		this.HOST = host;
		this.PORT = port;
		_dockerImage = dockerImage;
		bots = new ArrayList<String>();
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		String player = event.getPlayer().getName();
		if(!bots.contains(player)) {
			try {
				String dockerCommand = String.format("docker run %s %s " +
													 "%d " + BOT_NAME + " %s", _dockerImage, HOST, PORT,
													 player,
													 player);
				Runtime.getRuntime()
						.exec(dockerCommand);
				Bukkit.getLogger().info(String.format("Player %s joined the server, spawning bot using: " +
													  "\"%s\"", player, dockerCommand));
				Bukkit.getLogger().info(String.format("%s's bot spawned", player));
				bots.add(String.format(BOT_NAME, player));
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
		event.getPlayer().setGameMode(GameMode.CREATIVE);
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event){
		String player = event.getPlayer().getName();
		if(bots.contains(player)){
			event.getPlayer().setGameMode(GameMode.SURVIVAL);
		}
		else {
			if(Bukkit.getPlayer(String.format(BOT_NAME, player)) != null)
				Bukkit.getPlayer(String.format(BOT_NAME, player)).kickPlayer("bye");
			Bukkit.getLogger().info(String.format("Player %s left the server, killing bot", player));
//				Runtime.getRuntime()
//						.exec(String.format("docker stop -f " + CONTAINER_NAME + " & docker rm -f " + CONTAINER_NAME, player, player));
			bots.remove(String.format(BOT_NAME, player));
		}
	}
}
