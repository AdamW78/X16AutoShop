package online.x16.X16AutoShop;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import online.x16.X16AutoShop.tools.Colorizer;

public class X16AutoShop extends JavaPlugin {

	@Override
	public void onEnable() {
		
	}
	
	@Override
	public void onDisable() {
		
	}
	public void log(Object obj){
        if(getConfig().getBoolean("color-logs", true)){
            getServer().getConsoleSender().sendMessage(Colorizer.colorize("&3[&d" + getName() + "&3] &r" + obj));
        }else{
            Bukkit.getLogger().log(Level.INFO, "[" + getName() + "] " + Colorizer.colorize((String) obj).replaceAll("(?)\u00a7([a-f0-9k-or])", ""));
        }
    }
}
