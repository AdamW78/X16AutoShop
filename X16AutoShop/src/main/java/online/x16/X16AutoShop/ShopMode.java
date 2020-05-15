package online.x16.X16AutoShop;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import online.x16.X16AutoShop.tools.MessageBuilder;

public class ShopMode implements CommandExecutor {
	
	private X16AutoShop plugin;
	private String enableMsg, disableMsg;
	private boolean isInShopMode;
	
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
			sendToggleMsg(sender);
			isInShopMode = plugin.getShopModeSet().toggleShopMode((Player) sender);
		}
		//Check if there are the proper number of arguments (1)
		else if (args.length == 1) {
			//Check if the argument passed in was NOT an integer between 1 and 2304 (number of items that can fit in inventory)
			if (!(args[0].matches("\\d{1,4}")) || Integer.parseInt(args[0]) > 2304 || Integer.parseInt(args[0]) < 1) {
				//Send error message to the player about supplying a bad argument
				send(sender, "&cError: If you are supplying a shop size, it must be an integer between 1 and 2304 (max inventory size)");
				return false;
			}
			//This is reached if the argument was an integer between 1 and 2304
			else {
				//Send a message notifying player that shop mode was toggled on or off, toggle shop mode
				sendToggleMsg(sender);
				isInShopMode = plugin.getShopModeSet().toggleShopMode((Player) sender, Integer.parseInt(args[0]));
			}
		}
		//This is reached if the player sends more than 1 argument
		else {
			//Send error message to player about supplying too many arguments
			send(sender, "&cError: Too many arguments supplied - please supply one integer for the shop size");
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