/** 
 * Copyright (c) Krapht, 2011
 * 
 * "LogisticsPipes" is distributed under the terms of the Minecraft Mod Public 
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package net.minecraft.src.buildcraft.krapht;

import java.util.HashMap;
import java.util.LinkedList;

import net.minecraft.src.BuildCraftTransport;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.mod_LogisticsPipes;
//import net.minecraft.src.buildcraft.core.Action;
import net.minecraft.src.buildcraft.api.Action;
import net.minecraft.src.buildcraft.api.BuildCraftAPI;
import net.minecraft.src.buildcraft.api.IActionReceptor;
import net.minecraft.src.buildcraft.api.Trigger;
import net.minecraft.src.buildcraft.api.TriggerParameter;
import net.minecraft.src.buildcraft.core.ActionRedstoneOutput;
import net.minecraft.src.buildcraft.krapht.logic.BaseRoutingLogic;
import net.minecraft.src.buildcraft.transport.ActionSignalOutput;
import net.minecraft.src.buildcraft.transport.Pipe;
//import net.minecraft.src.buildcraft.transport.Pipe.GateKind;

public abstract class RoutedPipe extends CoreRoutedPipe{
	
	public RoutedPipe(BaseRoutingLogic logic, int itemID) {
		super(logic, itemID);
	}
	
	@Override
	public int getMainBlockTexture() {
		return _nextTexture;
	}
	
	@Override
	public void onNeighborBlockChange(int blockId) {
		super.onNeighborBlockChange(blockId);
		onNeighborBlockChange_Logistics();
	}

	@Override
	public LinkedList<Action> getActions() {
		LinkedList<Action> actions = super.getActions();
		actions.add(mod_LogisticsPipes.LogisticsDisableAction);
		return actions;
	}
	
	@Override
	protected void actionsActivated(HashMap<Integer, Boolean> actions) {
		super.actionsActivated(actions);

		setEnabled(true);
		// Activate the actions
		for (Integer i : actions.keySet()) {
			if (actions.get(i)) {
				if (BuildCraftAPI.actions[i] instanceof ActionDisableLogistics){
					setEnabled(false);
				}
			}
		}
	}
}
