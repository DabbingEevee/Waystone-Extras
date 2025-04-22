package com.existingeevee.waystone_extras.features;

import com.existingeevee.waystone_extras.WaystoneExtrasConfig;

import baubles.api.BaublesApi;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.items.IItemHandlerModifiable;

public class WaystoneItemCost {

	public static boolean doesPlayerHaveKey(EntityPlayer player, boolean consume) {
		if (WaystoneExtrasConfig.ItemCost.teleportationKeyInBaubles && Loader.isModLoaded("baubles")) {
			IItemHandlerModifiable handler = getBaublesItems(player);
						
			String cfg = WaystoneExtrasConfig.ItemCost.teleportationKeyItem;
			if (cfg.trim().isEmpty())
				return true;
						
			for (int a = 0; a < handler.getSlots(); a++) {
				ItemStack stack = handler.getStackInSlot(a);

				if (!stack.isEmpty() && is(stack, cfg)) {
					int cost = WaystoneExtrasConfig.ItemCost.teleportationKeyCostCount;

					if (WaystoneExtrasConfig.ItemCost.teleportationKeyCostDurability) {
						if (stack.getMaxDamage() - stack.getItemDamage() < cost) {
							return false;
						}
						if (consume) {
							stack.damageItem(cost, player);
						}
					} else {

						if (consume) {
							if (stack.getCount() < cost) {
								return false;
							}
							stack.shrink(cost);
						}
					}
					return true;

				}
			}
		}
		
		return false;
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
}
