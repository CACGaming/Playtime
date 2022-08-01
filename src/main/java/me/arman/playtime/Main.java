package me.arman.playtime;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.arman.playtime.commands.PlaytimeCommand;
import me.arman.playtime.events.Counter;



public class Main extends JavaPlugin {
	
	public File d;
	public FileConfiguration dc;
	private PluginManager pm;
	
	@Override
	public void onEnable() {
    this.pm = Bukkit.getPluginManager();
	this.getConfig().options().copyDefaults(true);
	this.saveConfig();
	this.createData();
	this.getCommand("playtime").setExecutor(new PlaytimeCommand(this));
	this.pm.registerEvents(new Counter(this), this);
    new BukkitRunnable() {
        public void run() {
        	Date now = new Date();
        	Calendar c = Calendar.getInstance();
        	c.setTime(now);
        	if (c.get(Calendar.DAY_OF_WEEK) == getConfig().getInt("resetDay") && c.get(Calendar.HOUR_OF_DAY) == getConfig().getInt("resetHour") && c.get(Calendar.MINUTE) == getConfig().getInt("resetMinute") && c.get(Calendar.SECOND) == getConfig().getInt("resetSecond")) {
        	d.delete();
        	createData();
            try {
               getData().save(d);
                }
                catch (IOException ex) {
                ex.printStackTrace();
                }
        	}
        }
        	
        }.runTaskTimer(this, 0L, 20L);
	}

	@Override
	public void onDisable() {
	
	}
	
    public void createData() {
       d = new File(getDataFolder(), "data.yml");
        if (!d.exists()) {
            d.getParentFile().mkdirs();
            saveResource("data.yml", false);
        }
        dc = new YamlConfiguration();
        try {
            dc.load(d);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
         
	 
	 public FileConfiguration getData() {
	        return this.dc;
	    }
	 
	 public void trackTime(Player p) {
			long c = System.currentTimeMillis();
			double pt = this.getData().getDouble(p.getUniqueId().toString());
		    new BukkitRunnable() {
		        public void run() {
				long b = System.currentTimeMillis();
				if (!p.hasPermission("playtime.immune")) {
		    getData().set(p.getUniqueId().toString(), pt + ((b-c)*0.001*0.00027777777));
		    try {
				getData().save(d);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		    if (!(p.isOnline())) {
		    this.cancel();
		    }
		    if (getData().getDouble(p.getUniqueId().toString()) >= getConfig().getDouble("maxPlaytime")) {
		        p.kickPlayer("You have exceeded the maximum weekly playtime!");
		        this.cancel();
		        }
			}
		        }
			}.runTaskTimer(this, 0L, 10L);
		}
	 
}
