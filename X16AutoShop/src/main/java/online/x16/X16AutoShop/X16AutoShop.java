package online.x16.X16AutoShop;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import online.x16.X16AutoShop.tools.Colorizer;

public class X16AutoShop extends JavaPlugin {

	final FileConfiguration config = getConfig();
	private ShopModeMap shopModePlayers;
	
	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
        PlayerInteractListener listener = new PlayerInteractListener(this);
        pm.registerEvents(listener, this);
        config.addDefault("default-shop-size", Integer.valueOf(64));
        config.addDefault("default-sign-material", "OAK_WALL_SIGN");
        config.addDefault("default-sign-color", "BLACK");
        config.addDefault("default-currency-symbol", "$");
        config.addDefault("log-shop-mode-toggle", Boolean.valueOf(true));
        config.addDefault("prefix", "&7[&9X16 AutoShop&7] &r");
        config.addDefault("autoshop-enabled-message", "&7Automatic shop creation mode enabled - right-click a block-face with the desired shop item.");
        config.addDefault("autoshop-disabled-message", "&7Automatic shop creation mode disabled.");
        config.addDefault("no-permission-message", "&c[Server] You do not have permission to use this command");
        config.addDefault("debug", false);
        config.options().copyDefaults(true);
        
        this.saveDefaultConfig();
        this.getCommand("shopmode").setExecutor(new ShopMode(this));
        shopModePlayers = new ShopModeMap(this);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	
	public void log(Object obj) {
        if(getConfig().getBoolean("color-logs", true)){
            getServer().getConsoleSender().sendMessage(Colorizer.colorize("&3[&d" + getName() + "&3] &r" + obj));
        } 
        else {
            Bukkit.getLogger().log(Level.INFO, "[" + getName() + "] " + Colorizer.colorize((String) obj).replaceAll("(?)\u00a7([a-f0-9k-or])", ""));
        }
    }
	
	public ShopModeMap getShopModeMap() {
		return shopModePlayers;
	}
}