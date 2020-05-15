package online.x16.X16AutoShop;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import online.x16.X16AutoShop.tools.MessageBuilder;

public class ShopMode implements CommandExecutor {
	
	private X16AutoShop plugin;
	private String enableMsg, disableMsg;
	private boolean isInShopMode;
	private String buySell;
	private int shopSize;
	private Material signMaterial;
	private DyeColor signColor;
	
	public ShopMode(X16AutoShop instance) {
		plugin = instance;
		enableMsg = plugin.getConfig().getString("autoshop-enabled-message");
		disableMsg = plugin.getConfig().getString("autoshop-disabled-message");
		
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		isInShopMode = plugin.getShopModeSet().isInShopMode((Player) sender);
		if (!(sender instanceof Player)) {
			plugin.log("&cError: Shop mode can only be toggled by in-game players");
			return false;
		}
		if (!(sender.hasPermission("x16autoshop.toggle")))
		//If there are no arguments supplied, pass in no arguments to toggleShopMode - this uses the default config size for the shop size
		if (args.length == 0) {
			send(sender, "&cError: Please supply a valid trade type - (Buy/Sell)");
			//No arguments supplied -- command is returned false
			return false;
		}
		//Check if there are enough arguments to toggle
		else if (args.length == 1) {
			buySell = args[0];
			//Check if the first argument is valid
			if (checkTradeArg((Player) sender, buySell)) {
				//Alert Player that shop mode has been toggled
				sendToggleMsg(sender);
				isInShopMode = plugin.getShopModeSet().toggleShopMode((Player) sender, checkTradeArg(buySell));
			}
			//An argument was invalid -- command is returned false
			else {
				return false;
			}
		}
		//Check if there are enough arguments to toggle
		else if (args.length == 2) {
			
			//Check if the first 2 arguments are valid
			if (checkTradeArg((Player) sender, args[0]) && checkAmountArg((Player) sender, args[1])) {
				buySell = args[0];
				shopSize = Integer.parseInt(args[1]);
				//Alert Player that shop mode has been toggled
				sendToggleMsg(sender);
				isInShopMode = plugin.getShopModeSet().toggleShopMode((Player) sender, checkTradeArg(buySell), shopSize);
			}
			//An argument was invalid -- command is returned false
			else {
				return false;
			}
		}
		//Check if there are the proper number of arguments
		else if (args.length == 3) {
			//Get material type from third argument
			signMaterial = getSignMaterial(args[2]);
			//Check if the first 3 arguments are valid
			if (checkTradeArg((Player) sender, args[0]) && checkAmountArg((Player) sender, args[1]) && signMaterial != null) {
				//Ensure that sign type parameter was valid
				if (signMaterial == null) {
					send(sender, "&cError: Please supply a valid wood type: (Oak, Birch, Spruce, etc)");
					return false;
				}
				//Alert Player that shop mode has been toggled
				sendToggleMsg(sender);
				isInShopMode = plugin.getShopModeSet().toggleShopMode((Player) sender, Boolean.parseBoolean(args[0]), Integer.parseInt(args[1]), signMaterial);
			}
			//An argument was invalid -- command is returned false
			else {
				return false;
			}
		}
		//Check if there are the proper number of arguments
		else if (args.length == 4) {
			//Get material type from third argument
			signMaterial = getSignMaterial(args[2]);
			//Get sign text color from fourth argument
			signColor = getSignColor(args[3]);
			//Check if the first 3 arguments are valid
			if (checkTradeArg((Player) sender, args[0]) && checkAmountArg((Player) sender, args[1]) && signMaterial != null && signColor != null) {
				//Ensure that sign type parameter was valid
				if (signMaterial == null) {
					send(sender, "&cError: Please supply a valid wood type: (Oak, Birch, Spruce, etc)");
					return false;
				}
				//Ensure that sign color parameter was valid
				if (signColor == null) {
					send(sender, "&cError: Please supply a valid color: (Red, Green, Blue, etc)");
					return false;
				}
				//Alert Player that shop mode has been toggled
				sendToggleMsg(sender);
				isInShopMode = plugin.getShopModeSet().toggleShopMode((Player) sender, Boolean.parseBoolean(args[0]), Integer.parseInt(args[1]), signMaterial, signColor);
			}
			//An argument was invalid -- command is returned false
			else {
				return false;
			}
		}
		//This is reached if the player sends more than 4 arguments
		else {
			//Send error message to player about supplying too many arguments
			send(sender, "&cError: Too many arguments supplied!");
			return false;
		}
		//Check if logging shop mode toggle to the console is enabled
		if (plugin.getConfig().getBoolean("log-shop-mode-toggle")) {
			String onOff = "off."; //String that changes based on whether shop mode is on or off for the command sender AFTER the toggle
			//If the player is in shop mode AFTER the toggle, change the string to "on."
			if (isInShopMode) onOff = "on.";
			//Log that the player toggled shop mode on or off
			plugin.log(sender+" turned shop mode "+onOff);
		}
		return true;
	}
	
	/**
	 * Checks if the supplied trade type is valid
	 * @param sender Player object for reference so that an error message can be sent
	 * @param arg String to be checked for validity
	 * @return Returns true if arg is "Buy" or "Sell"
	 */
	private boolean checkTradeArg(Player sender, String arg)
	{
		//Ensure that player input is valid
		if (arg.equalsIgnoreCase("buy") || arg.equalsIgnoreCase("sell"))
		{
			return true;
		}
		//This is reached if the argument was not valid
		//Send error message to the player about supplying a bad argument
		send(sender, "&cError: Please supply a valid trade type - (Buy/Sell)");
		return false;
	}
	private boolean checkTradeArg(String arg) {
		return arg.equalsIgnoreCase("buy");
	}
	
	/**
	 * Checks if the argument passed in was NOT an integer between 1 and 2304 (number of items that can fit in inventory)
	 * @param sender Player object for reference so that an error message can be sent
	 * @param arg String to be parsed
	 * @return Returns whether integer is in valid range
	 */
	private boolean checkAmountArg(Player sender, String arg)
	{
		//Check if the argument passed in was NOT an integer between 1 and 2304 (number of items that can fit in inventory)
		if (!(arg.matches("\\d{1,4}")) || Integer.parseInt(arg) > 2304 || Integer.parseInt(arg) < 1) {
			//Send error message to the player about supplying a bad argument
			send(sender, "&cError: If you are supplying a shop size, it must be an integer between 1 and 2304 (max inventory size)");
			return false;
		}
		//This is reached if the argument was an integer between 1 and 2304
		return true;
	}
	
	/**
	 * Gets sign material based on text supplied in argument.
	 * @param arg - String supplied holding text form of material (Valid arguments are "Oak", "Birch", "Spruce", "Dark_Oak", "Acacia", and "Jungle")
	 * @return Material representing sign is returned; If the argument is invalid null will be returned.
	 */
	
	
	/**
	 * Gets sign text color based on text supplied in argument.
	 * @param arg - String supplied holding text form of color (Valid arguments are "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
	 * @return Color of sign text is returned; If the argument is invalid null will be returned.
	 */
	private DyeColor getSignColor(String arg)
	{
		return DyeColor.valueOf(arg.toUpperCase());
	}
	
	/**
	 * Sends a message notifying the player whether auto-shop creation was toggled on or off
	 * @param sender Player to whom the message will be sent
	 */
	private void sendToggleMsg (CommandSender sender) {
		if (isInShopMode) send(sender, disableMsg);
		else send(sender, enableMsg);
	}
	
	/**
	 * Sends a formatted message to the player
	 * @param sender Player to whom the message will be sent
	 * @param str Message to send to player
	 */
	private void send(CommandSender sender, String str) {
		sender.spigot().sendMessage(MessageBuilder.build(str));
	}
}