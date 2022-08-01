package me.arman.playtime.commands;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.arman.playtime.Main;

public class PlaytimeCommand implements CommandExecutor {
	
	private Main plugin;
	
	public PlaytimeCommand(Main instance) {
	plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("playtime")) { 
        if (args.length == 0) {
        if (!(sender instanceof Player)) {
        	sender.sendMessage(ChatColor.RED + "Specify a player.");
        return true;
        }
        Player p = (Player) sender;
        double t = plugin.getData().getDouble(p.getUniqueId().toString());
        p.sendMessage(ChatColor.GREEN + "Playtime: " + ChatColor.WHITE + String.format("%.2f", t) + ChatColor.GREEN + " hours");
        }
        else if (args.length > 0) {
        if (!(args[0].equalsIgnoreCase("reset"))) {
        @SuppressWarnings("deprecation")
		OfflinePlayer pl = Bukkit.getOfflinePlayer(args[0]);
        double tp = plugin.getData().getDouble(pl.getUniqueId().toString());
        sender.sendMessage(ChatColor.GREEN + pl.getName() + "'s playtime: " + tp + " hours");
        }
        else {
        if (!(sender.hasPermission("playtime.reset"))) {
        sender.sendMessage(ChatColor.RED + "You don't have permission to reset playtime!");
        return true;
        }
        plugin.d.delete();
        plugin.createData();
        try {
        plugin.getData().save(plugin.d);
        }
        catch (IOException ex) {
        ex.printStackTrace();
        }
        sender.sendMessage(ChatColor.GREEN + "Successfully reset playtimes!");
        }
        }
		}
		
		return false;
		
	}

}
