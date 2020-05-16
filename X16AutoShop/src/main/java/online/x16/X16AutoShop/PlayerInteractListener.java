package online.x16.X16AutoShop;

import java.math.BigDecimal;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.Worth;
import com.earth2me.essentials.signs.EssentialsSign;
import com.earth2me.essentials.signs.SignBuy;
import com.mojang.datafixers.util.Pair;

public class PlayerInteractListener implements Listener {
	
	private X16AutoShop plugin;
	private boolean debug;
	
	public PlayerInteractListener(X16AutoShop instance) {
		plugin = instance;
		debug = plugin.getConfig().getBoolean("debug");
	}
	
	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled=true)
	public void onRightClick(PlayerInteractEvent e) {
		//Cancel shop sign creation if player did not right click
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
        {
            return;
        }
        //Define player who right clicked
		Player p = e.getPlayer();
        //Make sure that player is in shop mode
        if (plugin.getShopModeMap().isInShopMode(p))
        {
            //Cancel player event (Block place, block destroy, etc)
            e.setCancelled(true);
            //Retrieve the Sign Type to be used
            Material signType = plugin.getShopModeMap().getSignMaterial(p);
            //Retrieve the Sign Color to be used
            DyeColor color = plugin.getShopModeMap().getSignColor(p);
            //Retrieve the type of trade that will be set
            boolean isBuy = plugin.getShopModeMap().isBuy(p);
            //Retrieve the set amount of item
            int amount = plugin.getShopModeMap().getShopSize(p);
            //Retrieve item in player's main hand
            ItemStack itemToTrade = p.getInventory().getItemInMainHand();
            //Set item count to supplied count
            itemToTrade.setAmount(amount);
            //Retrieve worth of item to be traded based on value and amount
            BigDecimal itemWorth = getItemWorth(itemToTrade, amount);
            //Get block that sign will be faced on and direction of the block face that the player is looking at
            Pair<Block,BlockFace> targetedBlockInfo = getBlockFace(p);
            //Ensure that targeted Block and BlockFace have valid values
            if (targetedBlockInfo == null)
            {
                //Set a default Block value (Block where player is standing) and a default BlockFace value (Block standing up)
                targetedBlockInfo = new Pair<Block,BlockFace>(p.getLocation().getBlock(), BlockFace.UP);
            }
            //If sign is to be placed on a wall the sign type must be changed to a wall sign
            if (targetedBlockInfo.getSecond() != BlockFace.UP)
            {
                switch (signType)
                {
                    //Reset sign type to matching wall sign type
                    case OAK_SIGN:
                        signType = Material.OAK_WALL_SIGN;
                        break;
                    case BIRCH_SIGN:
                        signType = Material.BIRCH_WALL_SIGN;
                        break;
                    case SPRUCE_SIGN:
                        signType = Material.SPRUCE_WALL_SIGN;
                        break;
                    case DARK_OAK_SIGN:
                        signType = Material.DARK_OAK_WALL_SIGN;
                        break;
                    case ACACIA_SIGN:
                        signType = Material.ACACIA_WALL_SIGN;
                        break;
                    case JUNGLE_SIGN:
                        signType = Material.JUNGLE_WALL_SIGN;
                        break;
                    //In case invalid sign type is called default to oak wood
                    default:
                        signType = Material.OAK_WALL_SIGN;
                        break;
                }
            }
            //Create sign
            createShopSign(p, targetedBlockInfo.getFirst(), targetedBlockInfo.getSecond(), signType, color, isBuy, itemToTrade, itemWorth);
        }
	}
	
	/**
	 * Gets worth of supplied item
	 * @param itemInHand item type
	 * @param amount amount of item
	 * @return worth of item based on the object's value and amount
	 */
	private BigDecimal getItemWorth(ItemStack itemInHand, int amount)
	{
		//Get worth object which contains all blocks and their prices
		Worth worth = Essentials.getPlugin(Essentials.class).getWorth();
		//Ensure that item stack has the correct item amount set
		itemInHand.setAmount(amount);
		//Get price of item(s) based on the amount of items and the price from the worth obj
		return worth.getPrice(Essentials.getPlugin(Essentials.class), itemInHand);
	}

	/**
	* Gets the block that is adjacent to the targeted BlockFace and the BlockFace's direction.
	*
	* @param player the player whose targeted blocks BlockFace is going to check.
	* @return the Block adjacent to the player's targeted BlockFace
	* and the BlockFace direction of the targeted block, or null if the targeted block is non-occluding.
	*/
	private Pair<Block,BlockFace> getBlockFace(Player player) {
	    List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, 100);
	    if (lastTwoTargetBlocks.size() != 2 || !lastTwoTargetBlocks.get(1).getType().isOccluding()) return null;
	    Block targetBlock = lastTwoTargetBlocks.get(1);
	    Block adjacentBlock = lastTwoTargetBlocks.get(0);
	    return new Pair<Block,BlockFace>(adjacentBlock,targetBlock.getFace(adjacentBlock));
	}
	
	/**
	 * Creates a sign and writes the shop code on its lines
	 * @param signBlock Block where sign will be placed
     * @param blockFaceDirection Direction that sign will be placed
	 * @param signType Sign type/material (Oak, Birch, Spruce, etc)
	 * @param color Sign text color (Red, Green, Blue)
	 * @param isBuy Whether sign shop trade type is "Buy" or "Sell"
	 * @param amount Amount of items that will be sold
	 * @param itemToTrade Type of item to be sold
	 * @param itemWorth Value in supplied currency (default "$") of item purchase
	 */
	private void createShopSign(Player p, Block signBlock, BlockFace blockFaceDirection, Material signType, DyeColor color, boolean isBuy, ItemStack itemToTrade, BigDecimal itemWorth)
	{
        //CREATE SIGN
		//Set block type to sign (place a sign)
		signBlock.setType(signType);
		//Assign reference to sign object
		Sign sign = (Sign) signBlock.getState();
		
        //EDIT SIGN TEXT
		//Change color of text on sign
		sign.setColor(color);
		//Set first line of sign to match whether sign will be buying or selling
		if (isBuy) {
			sign.setLine(0, "[Buy]");
		}
		else {
			sign.setLine(0, "[Sell]");
		}
		//Set second line of sign to show the amount of the item that is being sold
		sign.setLine(1, String.valueOf(itemToTrade.getAmount()));
		//Set third line of sign to show the name of the item being sold
		sign.setLine(2, itemToTrade.getType().name());
		//Set third line of sign to show the value that the item(s) will be sold/bought for
		sign.setLine(3, plugin.getConfig().getString("default-currency-symbol") + itemWorth);
        //SIGN DIRECTION
		BlockData signData = sign.getBlockData();
        if (signData instanceof Rotatable) {
        	if (debug) plugin.log("Grabbed rotatable data for sign");
        	Rotatable signRotation = (Rotatable) signData;
        	signRotation.setRotation(p.getFacing().getOppositeFace());
        	sign.setBlockData(signRotation);
        }
        else if (signData instanceof Directional) {
        	if (debug) plugin.log("Grabbed directional data for sign");
        	Directional signDirection = (Directional) signData;
        	signDirection.setFacing(p.getFacing().getOppositeFace());
        	sign.setBlockData(signDirection);
        }
        else { if (debug) plugin.log("Failed to grab rotatable data"); }
        //Assign sign direction data to current sign
        //Refresh sign to update its text and position
        String[] theLines = new String[4];
        theLines[0] = sign.getLine(0);
        theLines[1] = sign.getLine(1);
        theLines[2] = sign.getLine(2);
        theLines[3] = sign.getLine(3);
        SignChangeEvent signChange = new SignChangeEvent(signBlock, p, theLines);
        Bukkit.getServer().getPluginManager().callEvent(signChange);
	}
}