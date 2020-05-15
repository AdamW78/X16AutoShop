package online.x16.X16AutoShop;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.entity.Player;

public class ShopModeSet {
	
	private HashMap<Player, ArrayList<Object>> shopModePlayers;
	private X16AutoShop plugin;
	
	public ShopModeSet(X16AutoShop instance) {
		shopModePlayers = new HashMap<Player, ArrayList<Object>>();
		plugin = instance;
	}

	/**
	 * Checks if a player is in shop mode
	 * @param p Player to check
	 * @return Boolean value for whether player is in ShopMode
	 */
	public boolean isInShopMode(Player p) {
		return shopModePlayers.containsKey(p);
	}
	/**
	 * Toggles shop mode for player p and sets their shop signs to sell numItems items
	 * @param p Player to toggle shop mode for
	 * @param numItems Number of items to sell in auto-generated shops
	 * @return Boolean value for whether or not player is in shop mode after toggling
	 */
	public boolean toggleShopMode(Player p, boolean isBuy, int numItems) {
		if (isInShopMode(p)) {
			shopModePlayers.remove(p);
			return false;
		}
		else {
			shopModePlayers.get(p).add(new Integer(numItems));
			return true;
		}
	}
	/**
	 * Toggles shop mode for player p and sets their shop signs to sell the default number of items
	 * @param p Player to toggle shop mode for
	 * @return Boolean value for whether or not player is in shop mode after toggling
	 */
	public boolean toggleShopMode(Player p) {
		return toggleShopMode(p, true, plugin.getConfig().getInt("default-shop-sell-size"));
	}
	public int getShopSize(Player p) {
		return (Integer) shopModePlayers.get(p).get(1);
	}
	
}
