package com.existingeevee.waystone_extras.mixin.cost;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.existingeevee.waystone_extras.features.WaystoneItemCost.ItemCostGuiButton;

import net.blay09.mods.waystones.client.gui.GuiButtonWaystoneEntry;

@Mixin(GuiButtonWaystoneEntry.class)
public class MixinGuiButtonWaystoneEntry implements ItemCostGuiButton {

	@Unique
	private int itemCost;
	
	@Unique
	@Override
	public int getCost() {
		return itemCost;
	}

	@Unique
	@Override
	public void setCost(int newCost) {
		this.itemCost = newCost;
	}

}
