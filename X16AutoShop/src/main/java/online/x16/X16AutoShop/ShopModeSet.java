package online.x16.X16AutoShop;

import java.util.HashSet;
import org.bukkit.entity.Player;

public class ShopModeSet {
	
	private X16AutoShop plugin;
	private HashSet<Player> shopModePlayers;
	
	public ShopModeSet(X16AutoShop instance) {
		plugin = instance;
		shopModePlayers = new HashSet<Player>();
	}

	public boolean isInShopMode(Player p) {
		return shopModePlayers.contains(p);
	}
}
