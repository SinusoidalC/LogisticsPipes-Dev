package net.minecraft.src.buildcraft.logisticspipes.modules;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.buildcraft.krapht.SimpleServiceLocator;
import net.minecraft.src.buildcraft.logisticspipes.IInventoryProvider;
import net.minecraft.src.krapht.ItemIdentifier;

public class ModuleQuickSort implements ILogisticsModule {

	private final int ticksToAction = 100;
	private int currentTick = 0;
	private boolean sent;
	private int ticksToResend = 0;
	
	private final IInventoryProvider _invProvider;
	private final ISendRoutedItem _itemSender;
	
	public ModuleQuickSort(IInventoryProvider invProvider, ISendRoutedItem itemSender) {
		_invProvider = invProvider;
		_itemSender = itemSender;
	}


	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound, String prefix) {}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound, String prefix) {}

	@Override
	public boolean displayGui(EntityPlayer entityplayer, GuiScreen previousGui) {		
		return false;
	}

	@Override
	public SinkReply sinksItem(ItemIdentifier item) {
		return null;
	}

	@Override
	public ILogisticsModule getSubModule(int slot) {
		return null;
	}

	@Override
	public void tick() {
		if (sent){
			ticksToResend = 6;
			sent = false;
		}
		
		if (ticksToResend > 0 && --ticksToResend < 1){
			currentTick = ticksToAction;
		}
		
		if (++currentTick < ticksToAction) return;
		currentTick = 0;
		
		//Extract Item
		IInventory targetInventory = _invProvider.getInventory();
		if (targetInventory == null) return;
		if (targetInventory.getSizeInventory() < 27) return;
		ItemStack stackToSend;
		for (int i = 0; i < targetInventory.getSizeInventory(); i++){
			stackToSend = targetInventory.getStackInSlot(i);
			if (stackToSend == null) continue;
			
			if (!this.shouldSend(stackToSend)) continue;
			_itemSender.sendStack(stackToSend);
			targetInventory.setInventorySlotContents(i, null);
			
			sent = true;
			break;
		}		
	}
	
	private boolean shouldSend(ItemStack stack){
		return SimpleServiceLocator.logisticsManager.hasDestination(stack, false, _itemSender.getSourceUUID(), true);
	}

}
