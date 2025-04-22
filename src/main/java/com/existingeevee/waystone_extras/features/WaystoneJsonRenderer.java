package com.existingeevee.waystone_extras.features;

import com.existingeevee.waystone_extras.WaystoneExtras;
import com.existingeevee.waystone_extras.WaystoneExtrasConfig;

import net.blay09.mods.waystones.Waystones;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;

public class WaystoneJsonRenderer { 

    //essentially we replace their model with our own since theirs is completely blank
	public static ResourceLocation getJsonWaystoneModelLocation(ResourceLocation location) {
		if (
				location.getNamespace().equals(Waystones.blockWaystone.getRegistryName().getNamespace()) && 
				location.getPath().equals(Waystones.blockWaystone.getRegistryName().getPath()) && 
				isEnabled()
			) {
	        return new ModelResourceLocation(WaystoneExtras.MODID + ":blockstates/" + location.getPath() + "_json.json", "inventory");
		}
		return null;
	}
	
	public static boolean isEnabled() {
		return WaystoneExtrasConfig.JsonModelRenderer.useJsonModelRenderer;
	}
}
