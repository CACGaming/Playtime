package me.arman.playtime.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import me.arman.playtime.Main;

public class Counter implements Listener {
	
	private Main plugin;
	
	public Counter(Main instance) {
	plugin = instance;
	}

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent e) {
	Player p = e.getPlayer();
	if (!p.hasPermission("playtime.immune")) {
	if ((plugin.getData().contains(p.getUniqueId().toString())) && plugin.getData().getDouble(p.getUniqueId().toString()) >= plugin.getConfig().getDouble("maxPlaytime")) {
	e.disallow(Result.KICK_FULL, "You have exceeded the maximum weekly playtime!");
	return;
	}
	plugin.trackTime(p);
	}
	}
}
