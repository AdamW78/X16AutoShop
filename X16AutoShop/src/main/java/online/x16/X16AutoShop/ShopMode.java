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
	private MessageBuilder messageBuilder;
	private ShopModeMap shopModePlayers;
	private boolean isInShopMode;
	private boolean debug;
	
	public ShopMode(X16AutoShop instance) {
		plugin = instance;
		handler = new ArgumentHandlers(plugin);
		enableMsg = plugin.getConfig().getString("autoshop-enabled-message");
		disableMsg = plugin.getConfig().getString("autoshop-disabled-message");
		debug = plugin.getConfig().getBoolean("debug");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		messageBuilder = new MessageBuilder(plugin);
		shopModePlayers = plugin.getShopModeMap();
		if (!(sender instanceof Player)) {
			plugin.log("&cError: Shop mode can only be toggled by in-game players");
			return false;
		}
		Player p = (Player) sender;
		isInShopMode = shopModePlayers.isInShopMode(p);
		if (!(sender.hasPermission("x16autoshop.toggle"))) {
			if (debug) plugin.log("Player used command and has permission");
			return false;
		}
		//Handle the command differently based on different numbers of arguments provided
		switch (args.length) {
			//You can only provide 0 arguments if you are turning shop mode off
			case (0):
				try { shopModePlayers.toggleShopMode(p); }
				catch (IllegalArgumentException e) {
					send(p, "&cError: Please provide a type of trade sign - buy/sell");
				}
				return true;
			//Checks if you provided a valid buy/sell value, otherwise tells you to - then toggles shop mode
			case (1):
				if (args[0].equals("help")) {
					send (p, "Usage: /<command> (buy/sell) [shop-size] [sign-type] [sign-color]");
					return true;
				}
				if (catchIllegalArguments(p, args[0])) return true;
				shopModePlayers.toggleShopMode(p, handler.handle(p, args[0]));
				sendToggleMsg(p);
				break;
			//The rest check if you provided valid a buy/sell value and shop size value - then it toggles shop mode
			//If you provide an invalid Material and/or DyeColor, the plugin uses default values
			case (2):
				if (catchIllegalArguments(p, args[0], args[1])) return true;
				shopModePlayers.toggleShopMode(p, handler.handle(p, args[0], args[1]));
				sendToggleMsg(p);
				break;
			case (3):
				if (catchIllegalArguments(p, args[0], args[1])) return true;
				shopModePlayers.toggleShopMode(p, handler.handle(p, args[0], args[1], args[2]));
				sendToggleMsg(p);
				break;
			case (4):
				if (catchIllegalArguments(p, args[0], args[1])) return true;
				shopModePlayers.toggleShopMode(p, handler.handle(p, args[0], args[1], args[2], args[3]));
				sendToggleMsg(p);
				break;
			default:
				send(p, "&cError: Too many arguments - use "+command+"help");
				return true;
		}
		return true;
	}
	
	private boolean catchIllegalArguments(Player p, String arg1) {
		try { handler.handle((Player) p, arg1); }
		catch (IllegalArgumentException e) {
			if (e.getMessage().equals("isBuy argument must be either \"buy\" or \"sell\"")) {
				send(p, "&cError: Please provide a type of trade sign - buy/sell");
				return true;
			}
		}
		return false;
	}
	private boolean catchIllegalArguments(Player p, String arg1, String arg2) {
		try { handler.handle(p, arg1); }
		catch (IllegalArgumentException e) {
			if (e.getMessage().equals("isBuy argument must be either \"buy\" or \"sell\"")) {
				send(p, "&cError: Please provide a type of trade sign - buy/sell");
				return true;
			}
		}
		try { handler.handle(p, arg1, arg2); }
		catch (IllegalArgumentException e) {
			if (e.getMessage().equals("shopSize must be an integer between 1 and 2304")) {
				send(p, "&cError: Shop sizes must be integers between 1 and 2304");
				return true;
			}
		}
		return false;
	}

	private void sendToggleMsg (Player p) {
		if (isInShopMode) send(p, disableMsg);
		else send(p, enableMsg);
	}
	
	/**
	 * Sends a formatted message to the player
	 * @param sender Player to whom the message will be sent
	 * @param str Message to send to player
	 */
	
	private void send(Player p, String str) {
		p.spigot().sendMessage(messageBuilder.build(str));
	}
}