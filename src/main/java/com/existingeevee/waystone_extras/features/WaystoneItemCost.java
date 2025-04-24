package com.existingeevee.waystone_extras.features;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.existingeevee.waystone_extras.WaystoneExtrasConfig;
import com.google.common.collect.Lists;

import baubles.api.BaublesApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.IItemHandlerModifiable;

public class WaystoneItemCost {

	public static boolean doesPlayerHaveKey(EntityPlayer player, boolean consume) {
		String cfg = WaystoneExtrasConfig.ItemCost.teleportationKeyItem;
		if (cfg.trim().isEmpty() || player.capabilities.isCreativeMode)
			return true;
		
		int totalCount = 0;
		List<ItemStack> items = new ArrayList<>();
		
		if (WaystoneExtrasConfig.ItemCost.teleportationKeyInBaubles) {			
			if (!Loader.isModLoaded("baubles"))
				return true;
			
			IItemHandlerModifiable handler = getBaublesItems(player);

			for (int a = 0; a < handler.getSlots(); a++) {
				ItemStack stack = handler.getStackInSlot(a);

				if (WaystoneExtrasConfig.ItemCost.teleportationKeyCostDurability && !stack.isItemStackDamageable()) {
					continue;
				}

				if (!stack.isEmpty() && is(stack, cfg)) {
					items.add(stack);
					totalCount += WaystoneExtrasConfig.ItemCost.teleportationKeyCostDurability ? stack.getMaxDamage() - stack.getItemDamage() + 1 : stack.getCount();
				}
			}
		} else {			
			InventoryPlayer inventory = player.inventory;
			for (List<ItemStack> list : Arrays.asList(inventory.mainInventory, inventory.armorInventory, inventory.offHandInventory)) {
				for (ItemStack stack : list) {
					if (WaystoneExtrasConfig.ItemCost.teleportationKeyCostDurability && !stack.isItemStackDamageable()) {
						continue;
					}

					if (!stack.isEmpty() && is(stack, cfg)) {
						items.add(stack);
						totalCount += WaystoneExtrasConfig.ItemCost.teleportationKeyCostDurability ? stack.getMaxDamage() - stack.getItemDamage() + 1 : stack.getCount();
					}
				}
			}
		}
		if (totalCount < WaystoneExtrasConfig.ItemCost.teleportationKeyCostCount || WaystoneExtrasConfig.ItemCost.teleportationKeyCostCount == 0 && totalCount <= 0) {
			return false; // we dont have enough
		} else {
			if (consume) {
				int debt = WaystoneExtrasConfig.ItemCost.teleportationKeyCostCount;

				for (ItemStack stack : items) {
					if (WaystoneExtrasConfig.ItemCost.teleportationKeyCostDurability) {
						int durability = stack.getMaxDamage() - stack.getItemDamage() + 1;
						int payment = Math.min(durability, debt);
						debt -= payment;
						stack.damageItem(payment, player);
					} else {
						int count = stack.getCount();
						int payment = Math.min(count, debt);
						debt -= payment;
						stack.shrink(payment);
					}
				}
			}
			return true;
		}
	}

	public static boolean doesPlayerHaveTPItem(EntityPlayer player, boolean consume, int cost) {
		String cfg = WaystoneExtrasConfig.ItemCost.teleportationCostItem;
		if (cfg.trim().isEmpty() || player.capabilities.isCreativeMode)
			return true;

		int totalCount = 0;
		List<ItemStack> items = new ArrayList<>();

		InventoryPlayer inventory = player.inventory;
		for (List<ItemStack> list : Arrays.asList(inventory.mainInventory, inventory.armorInventory, inventory.offHandInventory)) {
			for (ItemStack stack : list) {
				if (WaystoneExtrasConfig.ItemCost.teleportationCostDurability && !stack.isItemStackDamageable()) {
					continue;
				}

				if (!stack.isEmpty() && is(stack, cfg)) {
					items.add(stack);
					totalCount += WaystoneExtrasConfig.ItemCost.teleportationCostDurability ? stack.getMaxDamage() - stack.getItemDamage() + 1 : stack.getCount();
				}
			}
		}

		if (totalCount < cost || cost == 0 && totalCount <= 0) {
			return false; // we dont have enough
		} else {
			if (consume) {
				int debt = cost;

				for (ItemStack stack : items) {
					if (WaystoneExtrasConfig.ItemCost.teleportationCostDurability) {
						int durability = stack.getMaxDamage() - stack.getItemDamage() + 1;
						int payment = Math.min(durability, debt);
						debt -= payment;
						stack.damageItem(payment, player);
					} else {
						int count = stack.getCount();
						int payment = Math.min(count, debt);
						debt -= payment;
						stack.shrink(payment);
					}
				}
			}
			return true;
		}
	}

	// We keep this in a seperate method in case baubles isnt installed so it dosent crash
	private static IItemHandlerModifiable getBaublesItems(EntityPlayer player) {
		return BaublesApi.getBaublesHandler(player);
	}

	public static boolean is(ItemStack stack, String config) {		
		
		if (config.isEmpty()) {
			return false;
		}

		String[] split = config.split("\\|");

		if (split.length != 2) {
			warnFaultyConfig(config);
			return false;
		}

		ResourceLocation refRegistryName = new ResourceLocation(split[0]);
		ResourceLocation itemRegistryName = stack.getItem().getRegistryName();

		if (refRegistryName.equals(itemRegistryName)) {
			boolean wildCard = split[1].equals("*");

			if (wildCard) {
				return true;
			} else {
				try {
					int meta = Integer.parseInt(split[1]);
					return stack.getItemDamage() == meta;
				} catch (NumberFormatException e) {
					warnFaultyConfig(config);
					return false;
				}
			}
		}

		return false;
	}

	protected static void warnFaultyConfig(String config) {

	}

	public static boolean isEnabled() {
		return WaystoneExtrasConfig.ItemCost.useItemCost;
	}

	public static interface ItemCostGuiButton {

		public int getCost();

		public void setCost(int newCost);

	}

	public static ItemStack getCostItem() {
		String config = WaystoneExtrasConfig.ItemCost.teleportationCostItem;

		if (config.isEmpty()) {
			return ItemStack.EMPTY;
		}

		String[] split = config.split("\\|");

		if (split.length != 2) {
			warnFaultyConfig(config);
			return ItemStack.EMPTY;
		}

		ResourceLocation refRegistryName = new ResourceLocation(split[0]);

		Item item = ForgeRegistries.ITEMS.getValue(refRegistryName);

		if (item == null)
			return ItemStack.EMPTY;

		boolean wildCard = split[1].equals("*");

		if (wildCard) {
			return new ItemStack(item);
		} else {
			try {
				int meta = Integer.parseInt(split[1]);
				return new ItemStack(item, 1, meta);
			} catch (NumberFormatException e) {
				warnFaultyConfig(config);
				return ItemStack.EMPTY;
			}
		}
	}

	public static void hookRenderItemCost(Minecraft mc, int mouseX, int mouseY, float partialTicks, CallbackInfo ci, boolean canAfford, int cost, ItemStack costItem, GuiButton button) {
		if (cost <= 0)
			return; // no need to render
		
		GlStateManager.enableRescaleNormal();
		if (canAfford) {
			RenderHelper.enableGUIStandardItemLighting();
		} else {
			RenderHelper.disableStandardItemLighting();
		}
		
		FontRenderer fr = costItem.getItem().getFontRenderer(costItem);
		if (fr == null)
			fr = mc.fontRenderer;

		int x = button.x + button.width - 2 - 16, y = button.y + 2;

		mc.getRenderItem().renderItemIntoGUI(costItem, x, y);

		String costStr = "" + cost;
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		GlStateManager.disableBlend();
		
		int color = canAfford ? 0xffffff : 0xff7171;
		
		if (!canAfford) {
			color = new Color(color).darker().darker().getRGB();
		}
		
		fr.drawStringWithShadow(costStr, (float) (x + 19 - 2 - fr.getStringWidth(costStr)), (float) (y + 6 + 3), color);
				
        if (button.isMouseOver() && mouseX >= button.x + button.width - 16) {
        	
        	String toRender = I18n.format("tooltip.waystone_extras:costRequirement",costItem.getDisplayName(), costStr);
            int textLineWidth = fr.getStringWidth(toRender);

        	GuiUtils.drawHoveringText(Lists.newArrayList((canAfford ? TextFormatting.GREEN : TextFormatting.RED) + toRender), mouseX - textLineWidth - 22, mouseY + mc.fontRenderer.FONT_HEIGHT, mc.displayWidth, mc.displayHeight, 200, mc.fontRenderer);
            GlStateManager.disableLighting();
        }
	}
}
