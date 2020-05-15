package online.x16.X16AutoShop;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import online.x16.X16AutoShop.tools.MessageBuilder;

public class ShopMode implements CommandExecutor {
	
	private X16AutoShop plugin;
	private ArgumentHandlers handler;
	private String enableMsg, disableMsg;
	private boolean isInShopMode;
	
	public ShopMode(X16AutoShop instance) {
		plugin = instance;
		handler = new ArgumentHandlers(plugin);
		enableMsg = plugin.getConfig().getString("autoshop-enabled-message");
		disableMsg = plugin.getConfig().getString("autoshop-disabled-message");
		
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		isInShopMode = plugin.getShopModeSet().isInShopMode((Player) sender);
		if (!(sender instanceof Player)) {
			plugin.log("&cError: Shop mode can only be toggled by in-game players");
			return false;
		}
		if (!(sender.hasPermission("x16autoshop.toggle"))) return false;
		//If there are no arguments supplied, pass in no arguments to toggleShopMode - this uses the default config size for the shop size
		switch (args.length) {
			case (0):
				send(sender, "&cError: Please provide a type of trade sign - buy/sell");
				return true;
			case (1):
				if (args[0].equals("help")) {
					send (sender, "Usage: /<command> (buy/sell) [shop-size] [sign-type] [sign-color]");
					return true;
				}
				if (catchIllegalArguments(sender, args[0])) return true;
				handler.handle(args[0]);
				sendToggleMsg(sender);
				break;
			case (2):
				if (catchIllegalArguments(sender, args[0], args[1])) return true;
				handler.handle(args[0], args[1]);
				sendToggleMsg(sender);
				break;
			case (3):
				if (catchIllegalArguments(sender, args[0], args[1])) return true;
				handler.handle(args[0], args[1], args[2]);
				sendToggleMsg(sender);
				break;
			case (4):
				if (catchIllegalArguments(sender, args[0], args[1])) return true;
				handler.handle(args[0], args[1], args[2], args[3]);
				sendToggleMsg(sender);
				break;
			default:
				send(sender, "&cError: Too many arguments - use "+command+"help");
				return true;
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
	
	private boolean catchIllegalArguments(CommandSender sender, String arg1) {
		try { handler.handle(arg1); }
		catch (IllegalArgumentException e) {
			if (e.getMessage().equals("isBuy argument must be either \"buy\" or \"sell\"")) {
				send(sender, "&cError: Please provide a type of trade sign - buy/sell");
				return true;
			}
		}
		return false;
	}
	private boolean catchIllegalArguments(CommandSender sender, String arg1, String arg2) {
		try { handler.handle(arg1); }
		catch (IllegalArgumentException e) {
			if (e.getMessage().equals("isBuy argument must be either \"buy\" or \"sell\"")) {
				send(sender, "&cError: Please provide a type of trade sign - buy/sell");
				return true;
			}
		}
		try { handler.handle(arg1, arg2); }
		catch (IllegalArgumentException e) {
			if (e.getMessage().equals("shopSize must be an integer between 1 and 2304")) {
				send(sender, "&cError: Shop sizes must be integers between 1 and 2304");
				return true;
			}
		}
		return false;
	}

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