package com.existingeevee.waystone_extras.features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.existingeevee.waystone_extras.WaystoneExtrasConfig;

import baubles.api.BaublesApi;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
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

		if (totalCount < WaystoneExtrasConfig.ItemCost.teleportationKeyCostCount) {
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

		if (totalCount < WaystoneExtrasConfig.ItemCost.teleportationKeyCostCount) {
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
}
