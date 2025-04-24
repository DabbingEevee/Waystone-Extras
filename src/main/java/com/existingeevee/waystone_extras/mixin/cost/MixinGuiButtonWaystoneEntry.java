package com.existingeevee.waystone_extras.mixin.cost;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.existingeevee.waystone_extras.WaystoneExtrasConfig;
import com.existingeevee.waystone_extras.features.WaystoneItemCost;
import com.existingeevee.waystone_extras.features.WaystoneItemCost.ItemCostGuiButton;

import net.blay09.mods.waystones.WarpMode;
import net.blay09.mods.waystones.client.gui.GuiButtonWaystoneEntry;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.FMLClientHandler;

@Mixin(GuiButtonWaystoneEntry.class)
public class MixinGuiButtonWaystoneEntry extends GuiButton implements ItemCostGuiButton {

	public MixinGuiButtonWaystoneEntry(int buttonId, int x, int y, String buttonText) {
		super(buttonId, x, y, buttonText);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void waystone_extras$preventButtonPressNoItem(int id, int x, int y, WaystoneEntry waystone, WarpMode mode, CallbackInfo ci) {
		costItem = ItemStack.EMPTY;
		
		EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
		String cfg = WaystoneExtrasConfig.ItemCost.teleportationCostItem;
		if (cfg.trim().isEmpty() || player.capabilities.isCreativeMode)
			return;

		int dist = (int) Math.sqrt(player.getDistanceSqToCenter(waystone.getPos()));
		setCost((int) (WaystoneExtrasConfig.ItemCost.teleportationCostCoefficient * dist));

		if (getCost() <= 0)
			return;

		costItem = WaystoneItemCost.getCostItem();
				
		if (!WaystoneItemCost.doesPlayerHaveTPItem(player, false, getCost())) {
			enabled = false;
			missingCost = true;
		}
	}

	@Unique
	private int itemCost;

	@Unique
	private boolean missingCost;
	
	@Unique
	private ItemStack costItem;
	
	
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

	@Inject(method = { "drawButton(Lnet/minecraft/client/Minecraft;IIF)V", "func_191745_a(Lnet/minecraft/client/Minecraft;IIF)V" }, at = @At("TAIL"))
	public void waystone_extras$renderItemCost(Minecraft mc, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		WaystoneItemCost.hookRenderItemCost(mc, mouseX, mouseY, partialTicks, ci, !missingCost, getCost(), costItem, this);
	}
}
