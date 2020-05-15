package online.x16.X16AutoShop.tools;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import online.x16.X16AutoShop.X16AutoShop;

public class MessageBuilder {
	
	private X16AutoShop plugin;
	private static String prefix;
	
	public MessageBuilder (X16AutoShop instance) {
		plugin = instance;
		prefix = plugin.getConfig().getString("prefix");
	}

	public static TextComponent build(String str) {
		return new TextComponent(ChatColor.translateAlternateColorCodes('&', prefix+" "+str));
	}
	
}
