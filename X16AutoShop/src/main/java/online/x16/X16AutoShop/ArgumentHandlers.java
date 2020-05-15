package online.x16.X16AutoShop;

import java.util.ArrayList;
import org.bukkit.DyeColor;
import org.bukkit.Material;

public class ArgumentHandlers {
	
	private X16AutoShop plugin;
	private String defaultShopSize, defaultMaterial, defaultDyeColor;
	
	public ArgumentHandlers (X16AutoShop instance) {
		plugin = instance;
		defaultShopSize = Integer.toString(plugin.getConfig().getInt("default-shop-size"));
		defaultMaterial = plugin.getConfig().getString("default-sign-material").toUpperCase();
		//Check to make sure the sign material from the config is valid - if it's not, set the default to Material.OAK_WALL_SIGN
		defaultMaterial = (!defaultMaterial.equals("OAK_WALL_SIGN") && checkSignMaterial(defaultMaterial).equals(Material.OAK_WALL_SIGN))
				? "OAK_WALL_SIGN" : defaultMaterial;
		//Check to make sure the sign material from the config is valid - if it's not, set the default to Material.OAK_WALL_SIGN
		defaultDyeColor = plugin.getConfig().getString("default-sign-color").toUpperCase();
		defaultDyeColor = !defaultDyeColor.equals("BLACK") && checkSignColor(defaultDyeColor).equals(DyeColor.BLACK)
				? "BLACK" : defaultDyeColor;
	}
	
	/**
	 * Takes String inputs for all arguments and converts them to their proper respective objects so they can eventually be passed into toggleShopMode 
	 * @param isBuy String to convert to boolean and put first into ArrayList
	 * @param shopSize String to convert to int and put second in ArrayList
	 * @param material String to convert to material and put third into ArrayList
	 * @param dyeColor String to convert to dyeColor and put fourth into ArrayList
	 * @return ArrayList with a Boolean, Integer, Material, and DyeColor
	 */
	public ArrayList<Object> handle(String isBuy, String shopSize, String material, String dyeColor) throws IllegalArgumentException {
		ArrayList<Object> handledArgs = new ArrayList<Object>();
		//Check if String isBuy was NOT "buy" or "sell" - then throw IllegalArgumentException
		if (checkTradeArg(isBuy) == null) throw new IllegalArgumentException("isBuy argument must be either \"buy\" or \"sell\"");
		//Check if String shopSize is NOT an integer between 1 and 2304 - then throw IllegalArgumentException
		if (checkAmountArg(shopSize) == null) throw new IllegalArgumentException("shopSize must be an integer between 1 and 2304");
		//Args will be valid, add to handledArgs and return handledArgs
		handledArgs.add(checkTradeArg(isBuy));
		handledArgs.add(checkAmountArg(shopSize));
		handledArgs.add(checkSignMaterial(material));
		handledArgs.add(checkSignColor(dyeColor));
		return handledArgs;
	}

	/**
	 * Takes String inputs for all arguments (except DyeColor) and converts them to their proper respective objects 
	 * so they can eventually be passed into toggleShopMode 
	 * @param isBuy String to convert to boolean and put first into ArrayList
	 * @param shopSize String to convert to int and put second in ArrayList
	 * @param material String to convert to material and put third into ArrayList
	 * @return ArrayList with a Boolean, Integer, Material, and DyeColor
	 */
	public ArrayList<Object> handle(String isBuy, String shopSize, String material) {
		return handle(isBuy, shopSize, material, defaultDyeColor);
	}
	
	/**
	 * Takes String inputs for all arguments (except DyeColor & Material) and converts them to their proper respective objects 
	 * so they can eventually be passed into toggleShopMode 
	 * @param isBuy String to convert to boolean and put first into ArrayList
	 * @param shopSize String to convert to int and put second in ArrayList
	 * @return ArrayList with a Boolean, Integer, Material, and DyeColor
	 */
	public ArrayList<Object> handle(String isBuy, String shopSize) {
		return handle(isBuy, defaultShopSize, defaultMaterial, defaultDyeColor);
	}
	
	/**
	 * Takes String input for isBuy and converts it to its proper respective objects so it can eventually be passed into toggleShopMode 
	 * @param isBuy String to convert to boolean and put first into ArrayList
	 * @return ArrayList with a Boolean, Integer, Material, and DyeColor
	 */
	public ArrayList<Object> handle(String isBuy) {
		return handle(isBuy, defaultShopSize, defaultMaterial, defaultDyeColor);
	}
	
	/**
	 * Checks if the supplied trade type is valid
	 * @param arg String to be checked for validity
	 * @return Boolean object for if String was buy - if arg wasn't "buy" or "sell", returns a null Boolean object
	 */
	private Boolean checkTradeArg(String arg) {
		//Ensure that player input is valid
		arg = arg.toLowerCase();
		if (!(arg.equals("buy") || arg.equals("sell"))) 
			return new Boolean (null);
		return arg.equals("buy");
	}
	
	/**
	 * Checks if the supplied shop size is valid
	 * @param arg String to be checked for validity
	 * @return Integer object for if String was valid int - if arg wasn't valid, returns a null Integer object
	 */
	private Integer checkAmountArg(String arg)
	{
		//Check if the argument passed in was an integer between 1 and 2304 (number of items that can fit in inventory)
		if (arg.matches("\\d{1,4}") && (Integer.parseInt(arg) <= 2304 && Integer.parseInt(arg) >= 1)) return Integer.parseInt(arg);
		//This is reached if the argument was NOT an integer between 1 and 2304
		//Set Integer to null to throw an IllegalArgumentException later
		return new Integer (null);
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
	
	/**
	 * Checks if a String arg matches a valid DyeColor - returns either the DyeColor from the string or BLACK if arg didn't match a DyeColor
	 * @param arg String to check
	 * @return DyeColor for String arg
	 */
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
