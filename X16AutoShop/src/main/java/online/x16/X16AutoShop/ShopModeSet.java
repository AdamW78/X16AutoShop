package online.x16.X16AutoShop;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ShopModeSet {
	
	private HashMap<Player, ArrayList<Object>> shopModePlayers;
	private X16AutoShop plugin;
    private int defaultShopSize;
	private Material defaultSignMaterial;
	private DyeColor defaultSignColor;
	
	public ShopModeSet(X16AutoShop instance) {
		shopModePlayers = new HashMap<Player, ArrayList<Object>>();
		plugin = instance;
        defaultShopSize = plugin.getConfig().getInt("default-shop-sell-size");
		defaultSignMaterial = Material.getMaterial("minecraft:"+plugin.getConfig().getString("default-sign-material").toUpperCase());
		defaultSignColor = DyeColor.valueOf(plugin.getConfig().getString("default-sign-color"));
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
     * @param isBuy Boolean for whether or not the sign is for buying - if not then it's for selling
	 * @param numItems Number of items to sell in auto-generated shops
     * @param signMaterial Material of the sign that the shop will be made out of
     * @param signColor DyeColor of the sign that the shop will be made out of
	 * @return Boolean value for whether or not player is in shop mode after toggling
	 */
	public boolean toggleShopMode(Player p, boolean isBuy, int numItems, Material signMaterial, DyeColor signColor) {
		ArrayList<Object> list = shopModePlayers.get(p);
		if (isInShopMode(p)) {
			shopModePlayers.remove(p);
			return false;
		}
		else {
			list.add(isBuy);
			list.add(numItems);
			list.add(signMaterial);
			list.add(signColor);
			return true;
		}
	}
	/**
	 * Toggles shop mode for player p and sets their shop signs to sell the default number of items
	 * @param p Player to toggle shop mode for
     * @param isBuy Boolean for whether or not the sign is for buying - if not then it's for selling
	 * @return Boolean value for whether or not player is in shop mode after toggling
	 */
	public boolean toggleShopMode(Player p, boolean isBuy) {
		plugin.log("Player: "+p);
		plugin.log("Buy signs: "+isBuy);
		plugin.log("Sign material: "+defaultSignMaterial);
		plugin.log("Sign color: "+defaultSignColor);
		return toggleShopMode(p, isBuy, defaultShopSize, defaultSignMaterial, defaultSignColor);
		
	}
	public boolean toggleShopMode(Player p, boolean isBuy, int numItems) {
		return toggleShopMode(p, isBuy, numItems, defaultSignMaterial, defaultSignColor);
	}
	public boolean toggleShopMode(Player p, boolean isBuy, int numItems, Material signMaterial) {
		return toggleShopMode(p, isBuy, numItems, signMaterial, defaultSignColor);
	}
	/**
	 * Toggles shop mode for player p and sets their shop signs to default number of items
	 * @param p Player to toggle shop mode for
     * @param isBuy Boolean for whether or not the sign is for buying - if not then it's for selling
     * @param signMaterial Material of the sign that the shop will be made out of
	 * @return Boolean value for whether or not player is in shop mode after toggling
	 */
	public boolean toggleShopMode(Player p, boolean isBuy, Material signMaterial) {
		return toggleShopMode(p, isBuy, defaultShopSize, signMaterial, defaultSignColor);
	}
	/**
	 * Toggles shop mode for player p and sets their shop signs to sell numItems items
	 * @param p Player to toggle shop mode for
     * @param isBuy Boolean for whether or not the sign is for buying - if not then it's for selling
     * @param signColor DyeColor of the sign that the shop will be made out of
	 * @return Boolean value for whether or not player is in shop mode after toggling
	 */
	public boolean toggleShopMode(Player p, boolean isBuy, DyeColor signColor) {
		return toggleShopMode(p, isBuy, defaultShopSize, defaultSignMaterial, signColor);
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