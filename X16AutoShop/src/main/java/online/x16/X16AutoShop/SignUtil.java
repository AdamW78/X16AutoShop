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
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;
import com.mojang.datafixers.util.Pair;

import online.x16.X16AutoShop.tools.MessageBuilder;

public class SignUtil {
	
	private X16AutoShop plugin;
	private boolean debug;
	private MessageBuilder messageBuilder;
	
	public SignUtil(X16AutoShop instance) {
		plugin = instance;
		debug = plugin.getConfig().getBoolean("debug");
		messageBuilder = new MessageBuilder(plugin);
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
	public void createShopSign(Player p, Block signBlock, BlockFace blockFaceDirection, Material signType, DyeColor color, 
			boolean isBuy, ItemStack itemToTrade, BigDecimal itemWorth) {
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
        for (int i = 0; i < 4; i++) theLines[i] = sign.getLine(i);
        SignChangeEvent signChange = new SignChangeEvent(signBlock, p, theLines);
        Bukkit.getServer().getPluginManager().callEvent(signChange);
	}
	
	/**
	 * Takes a sign Material and changes it to the wall equivalent - if the Material input is invalid, return OAK_WALL_SIGN
	 * @param signMaterial Material to handle and change into a wall equivalent
	 * @return Wall equivalent of the sign Material passed in
	 */
	public Material toWallSign(Material signMaterial) {
		switch (signMaterial)
        {
            //Reset sign type to matching wall sign type
            case OAK_SIGN:
                return Material.OAK_WALL_SIGN;
            case BIRCH_SIGN:
                return Material.BIRCH_WALL_SIGN;
            case SPRUCE_SIGN:
                return Material.SPRUCE_WALL_SIGN;
            case DARK_OAK_SIGN:
                return Material.DARK_OAK_WALL_SIGN;
            case ACACIA_SIGN:
                return Material.ACACIA_WALL_SIGN;
            case JUNGLE_SIGN:
                return Material.JUNGLE_WALL_SIGN;
            //In case invalid sign type is called default to oak wood
            default:
                return Material.OAK_WALL_SIGN;
        }
	}
	
	/**
	 * Takes a player and returns a Pair<Block, BlockFace> used to place a place a sign
	 * If player was looking somewhere a sign can't be placed, return Pair<Block, BlockFace> for player's feet
	 * @param p Player - fetches where the Player was looking
	 * @return Pair<Block, BlockFace> for where Player was looking
	 */
	public Block getSignBlock(Player p) {
		//Fetch block where the sign will be placed
		Block signBlock = getBlockFacePair(p).getFirst();
		//Check if signBlock is null (happens if you try placing on a block without a valid BlockFace like on a sign)
        if (signBlock == null) {
            //Return the block at the player's feet to avoid a NullPointerException
            return p.getLocation().getBlock();
        }
        return signBlock;
	}
	public BlockFace getSignBlockFace(Player p) {
        BlockFace signBlockFace = getBlockFacePair(p).getSecond();
      //Check if signBlockFace is null (happens if you try placing on a block without a valid BlockFace like on a sign)
        if (signBlockFace == null) {
            //Return the BlockFace pointing up (this will be at the Player's feet) to avoid a NullPointerException
        	//Notify the player that you had to do this
            p.spigot().sendMessage(messageBuilder.build("&cError: could not place sign - placed at your feet"));
            return BlockFace.UP;
        }
        return signBlockFace;
	}

	/**
	* Gets the block that is adjacent to the targeted BlockFace and the BlockFace's direction.
	*
	* @param player the player whose targeted blocks BlockFace is going to check.
	* @return the Block adjacent to the player's targeted BlockFace
	* and the BlockFace direction of the targeted block, or null if the targeted block is non-occluding.
	*/
	private Pair<Block, BlockFace> getBlockFacePair(Player player) {
	    List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, 100);
	    if (lastTwoTargetBlocks.size() != 2 || !lastTwoTargetBlocks.get(1).getType().isOccluding()) return null;
	    Block targetBlock = lastTwoTargetBlocks.get(1);
	    Block adjacentBlock = lastTwoTargetBlocks.get(0);
	    return new Pair<Block,BlockFace>(adjacentBlock,targetBlock.getFace(adjacentBlock));
	}
}
