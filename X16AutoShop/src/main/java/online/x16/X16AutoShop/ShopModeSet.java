package online.x16.X16AutoShop;

import java.util.HashSet;
import org.bukkit.entity.Player;

public class ShopModeSet {
	
	private HashSet<Player> shopModePlayers;
	
	public ShopModeSet() {
		shopModePlayers = new HashSet<Player>();
	}

	/**
	 * Checks if a player is in shop mode
	 * @param p Player to check
	 * @return Boolean value for whether player is in ShopMode
	 */
	public boolean isInShopMode(Player p) {
		return shopModePlayers.contains(p);
	}
	
	/**
	 * Puts a player in shop mode
	 * @param p Player to put in shop mode
	 * @return value for whether player was successfully put in shop mode
	 */
	public boolean enterShopMode(Player p) {
		return shopModePlayers.add(p);
	}
	
	/**
	 * Removes a player from shop mode
	 * @param p Player to remove from shop mode
	 * @return value for whether player was successfully removed from shop mode
	 */
	public boolean exitShopMode(Player p) {
		return shopModePlayers.remove(p);
	}
}
