package online.x16.X16AutoShop;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ShopModeSet {
	
	private HashMap<Player, ArrayList<Object>> shopModePlayers;
	private X16AutoShop plugin;
	private Material defaultSignMaterial;
	private DyeColor defaultSignColor;
	
	public ShopModeSet(X16AutoShop instance) {
		shopModePlayers = new HashMap<Player, ArrayList<Object>>();
		plugin = instance;
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
	 * @param numItems Number of items to sell in auto-generated shops
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
	 * @return Boolean value for whether or not player is in shop mode after toggling
	 */
	public boolean toggleShopMode(Player p, boolean isBuy) {
		return toggleShopMode(p, isBuy, plugin.getConfig().getInt("default-shop-sell-size"), defaultSignMaterial, defaultSignColor);
	}
	
	public boolean toggleShopMode(Player p, boolean isBuy, Material signMaterial) {
		return toggleShopMode(p, isBuy, plugin.getConfig().getInt("default-shop-sell-size"), signMaterial, defaultSignColor);
	}
	
	public boolean toggleShopMode(Player p, boolean isBuy, DyeColor signColor) {
		return toggleShopMode(p, isBuy, plugin.getConfig().getInt("default-shop-sell-size"), defaultSignMaterial, signColor);
	}
	
	public boolean isBuy(Player p) {
		return (Boolean) shopModePlayers.get(p).get(0);
	}
	
	public int getShopSize(Player p) {
		return (Integer) shopModePlayers.get(p).get(1);
	}
	
	public Material getSignMaterial(Player p) {
		return (Material) shopModePlayers.get(p).get(2);
	}
	
	public DyeColor getSignColor(Player p) {
		return (DyeColor) shopModePlayers.get(p).get(3);
	}
}