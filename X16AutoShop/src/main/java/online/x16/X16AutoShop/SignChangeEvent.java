package online.x16.X16AutoShop;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;

public class SignChangeEvent extends BlockEvent implements Cancellable {

	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCancelled(boolean cancel) {
		// TODO Auto-generated method stub

	}

	@Override
	public HandlerList getHandlers() {
		// TODO Auto-generated method stub
		return null;
	}

}
