package online.x16.X16AutoShop;

import java.util.ArrayList;

import org.bukkit.DyeColor;
import org.bukkit.Material;

public class ArgumentHandlers {
	
	private X16AutoShop plugin;
	private String materialString, dyeColorString;
	
	public ArgumentHandlers (X16AutoShop instance) {
		plugin = instance;
		materialString = plugin.getConfig().getString("default-sign-material").toUpperCase();
		//Check to make sure the sign material from the config is valid - if it's not, set the default to Material.OAK_WALL_SIGN
		materialString = (!materialString.equals("OAK_WALL_SIGN") && checkSignMaterial(materialString).equals(Material.OAK_WALL_SIGN))
				? "OAK_WALL_SIGN" : materialString;
		//Check to make sure the sign material from the config is valid - if it's not, set the default to Material.OAK_WALL_SIGN
		dyeColorString = plugin.getConfig().getString("default-sign-color").toUpperCase();
		dyeColorString = !dyeColorString.equals("BLACK") && checkSignColor(dyeColorString).equals(DyeColor.BLACK)
				? "BLACK" : dyeColorString;
	}
	
	/**
	 * Takes String inputs for all arguments and converts them to their proper respective objects so they can eventually be passed into toggleShopMode 
	 * @param isBuy String to convert to boolean and put first into ArrayList
	 * @param shopSize String to convert to int and put second in ArrayList
	 * @param material String to convert to material and put third into ArrayList
	 * @param dyeColor String to convert to dyeColor and put fourth into ArrayList
	 * @return ArrayList with a Boolean, Integer, Material, and DyeColor
	 */
	public ArrayList<Object> handle(String isBuy, String shopSize, String material, String dyeColor) {
		ArrayList<Object> handledArgs = new ArrayList<Object>();
		return handledArgs;
	}
	/**
	 * Takes a String and returns a wall sign Material value for that string
	 * @param arg String to check for a match for a wall sign material
	 * @return Material for string provided - if no material can be found, return OAK_WALL_SIGN
	 */
	private Material checkSignMaterial(String arg) {
        arg = arg.toUpperCase();
		switch (arg) {
			case "OAK_WALL_SIGN":
				return Material.OAK_WALL_SIGN;
			case "BIRCH_WALL_SIGN":
				return Material.BIRCH_WALL_SIGN;
			case "SPRUCE_WALL_SIGN":
				return Material.SPRUCE_WALL_SIGN;
			case "DARK_OAK_WALL_SIGN":
				return Material.DARK_OAK_WALL_SIGN;
			case "ACACIA_WALL_SIGN":
				return Material.ACACIA_WALL_SIGN;
			case "JUNGLE_WALL_SIGN":
				return Material.JUNGLE_WALL_SIGN;
			//In case invalid sign type is called default to oak wood
			default:
				return Material.OAK_WALL_SIGN;
        }
	}
	private DyeColor checkSignColor(String arg) {
        arg = arg.toUpperCase();
        try {
        	DyeColor.valueOf(arg);
        }
        catch (IllegalArgumentException e) {
        	return DyeColor.BLACK;
        }
        return DyeColor.valueOf(arg);
        		
	}

}
