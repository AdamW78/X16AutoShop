package online.x16.X16AutoShop;

import java.math.BigDecimal;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.Worth;
import online.x16.X16AutoShop.tools.MessageBuilder;

public class PlayerInteractListener implements Listener {
	
	private X16AutoShop plugin;
	private SignUtil signUtil;
	private MessageBuilder messageBuilder;
	private boolean debug;
	
	public PlayerInteractListener(X16AutoShop instance) {
		plugin = instance;
		debug = plugin.getConfig().getBoolean("debug");
	}
	
	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled=true)
	public void onRightClick(PlayerInteractEvent e) {
		//Cancel shop sign creation if player did not right click
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		Player p = e.getPlayer();
        //Check that player is in shop mode
        if (plugin.getShopModeMap().isInShopMode(p)) {
            e.setCancelled(true);
            signUtil = new SignUtil(plugin);
            //Retrieve the Material, DyeColor, boolean value (for whether or not the sign is buy), and int shopSize to put on the Sign
            Material signMaterial = plugin.getShopModeMap().getSignMaterial(p);
            DyeColor signColor = plugin.getShopModeMap().getSignColor(p);
            boolean isBuy = plugin.getShopModeMap().isBuy(p);
            int shopSize = plugin.getShopModeMap().getShopSize(p);
            if (debug) plugin.log("Successfully fetched all values from ShopModeMap");
            //Retrieve ItemStack in player's main hand, set stack size
            ItemStack itemToTrade = p.getInventory().getItemInMainHand();
            itemToTrade.setAmount(shopSize);
        	if (debug) plugin.log("Successfully fetched ItemStack from player's hand");
            //Retrieve worth of item to be traded based on value and amount - if null, set to zero and notify player
            BigDecimal itemWorth = getItemWorth(itemToTrade, shopSize);
            if (itemWorth == null) {
            	itemWorth = new BigDecimal(0);
            	messageBuilder = new MessageBuilder(plugin);
            	p.spigot().sendMessage(messageBuilder.build("&7Warning: No worth was found for &7"+itemToTrade.getItemMeta().getDisplayName()
            			+"&7 - sign will have a prive of zero!"));
            }
            if (debug) plugin.log("Successfully fetched worth or set worth to zero for ItemStack in player's hand");
            //Get info for how the sign will be placed
            Block signBlock = signUtil.getSignBlock(p);
            BlockFace blockFace = signUtil.getSignBlockFace(p);
            if (blockFace != BlockFace.UP) {
            	signMaterial = signUtil.toWallSign(signMaterial);
            	System.out.println("Blockface was not up, creating wall sign");
            	System.out.println(signMaterial);
            }
            if (debug) plugin.log("Successfully fetched all values to create sign - creating...");
            System.out.println(blockFace);
            //Actually create the sign
            signUtil.createShopSign(p, signBlock, signMaterial, signColor, isBuy, itemToTrade, itemWorth);
        }
	}
	
	/**
	 * Gets worth of supplied item
	 * @param itemInHand item type
	 * @param amount amount of item
	 * @return worth of item based on the object's value and amount
	 */
	private BigDecimal getItemWorth(ItemStack itemInHand, int amount) {
		//Get worth object which contains all blocks and their prices
		Worth worth = Essentials.getPlugin(Essentials.class).getWorth();
		//Ensure that item stack has the correct item amount set
		itemInHand.setAmount(amount);
		//Get price of item(s) based on the amount of items and the price from the worth obj
		return worth.getPrice(Essentials.getPlugin(Essentials.class), itemInHand);
	}
}