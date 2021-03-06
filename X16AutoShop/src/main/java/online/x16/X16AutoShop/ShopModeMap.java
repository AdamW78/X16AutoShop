package online.x16.X16AutoShop;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ShopModeMap {
	
	private X16AutoShop plugin;
	private HashMap<Player, ArrayList<Object>> shopModePlayers;
	private boolean debug;
	
	public ShopModeMap(X16AutoShop instance) {
		shopModePlayers = new HashMap<Player, ArrayList<Object>>();
		plugin = instance;
		debug = plugin.getConfig().getBoolean("debug");
		if (debug) plugin.log("ShopModeMap instantiated");
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
	 * Toggles shop mode for player p with ArrayList<Object> args passed as arguments
	 * @param p Player to toggle shop mode for
     * @param args ArrayList<Object> containing properly handled args
	 * @return Boolean value for whether or not player is in shop mode after toggling
	 */
	public boolean toggleShopMode(Player p, ArrayList<Object> args) {
		//Check if the player is already in shop Mode
		if (debug) plugin.log("Begun toggling shop mode for "+p.getName());
		if (isInShopMode(p)) {
			if (debug) plugin.log(p.getName()+" was already in shop mode, toggling off");
			shopModePlayers.remove(p);
			return false;
		}
		else {
			if (debug) plugin.log(p.getName()+" was NOT in shop mode, putting in shop mode now");
			shopModePlayers.put(p, args);
			return true;
		}
	}
	public boolean toggleShopMode(Player p) throws IllegalArgumentException {
		if (isInShopMode(p)) {
			shopModePlayers.remove(p);
			return false;
		}
		else throw new IllegalArgumentException("No arguments supplied for player not already in shop mode");
	}
		
    /**
     * Checks whether or not a player (must be a player in shop mode) is creating buy signs
     * @param p Player being checked
     * @return Boolean value for whether or not the player is creating buy signs
     */
	public boolean isBuy(Player p) {
		return (Boolean) shopModePlayers.get(p).get(0);
	}
	/**
     * Checks the size of a player's (must be a player in shop mode) shop signs
     * @param p Player being checked
     * @return int value for the size of shop signs
     */
	public int getShopSize(Player p) {
		return (Integer) shopModePlayers.get(p).get(1);
	}
	/**
     * Checks the Material of a player's (must be a player in shop mode) shop signs
     * @param p Player being checked
     * @return Material value for shop signs
     */
	public Material getSignMaterial(Player p) {
		return (Material) shopModePlayers.get(p).get(2);
	}
	/**
     * Checks the dye color of a player's (must be a player in shop mode) shop signs
     * @param p Player being checked
     * @return DyeColor value for color of shop signs
     */
	public DyeColor getSignColor(Player p) {
		return (DyeColor) shopModePlayers.get(p).get(3);
	}
}