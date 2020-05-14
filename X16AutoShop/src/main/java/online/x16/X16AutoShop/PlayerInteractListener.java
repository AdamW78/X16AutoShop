package online.x16.X16AutoShop;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
	
	private X16AutoShop plugin;
	
	public PlayerInteractListener(X16AutoShop instance) {
		plugin = instance;
	}
	
	@EventHandler (priority = EventPriority.NORMAL, ignoreCancelled=true)
	public void onRightClick(PlayerInteractEvent e) {
	}

}
