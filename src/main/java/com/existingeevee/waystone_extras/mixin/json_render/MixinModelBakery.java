package com.existingeevee.waystone_extras.mixin.json_render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.existingeevee.waystone_extras.WaystoneJsonRenderer;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.util.ResourceLocation;

@Mixin(ModelBakery.class)
public abstract class MixinModelBakery {

	@Inject(method = "getBlockstateLocation", at = @At("HEAD"), cancellable = true)
	private void waystone_extras$replaceWaystoneBlockstateDef(ResourceLocation location, CallbackInfoReturnable<ResourceLocation> ci) {
		ResourceLocation loc = WaystoneJsonRenderer.getJsonWaystoneModelLocation(location);
		if (loc != null) {
			ci.setReturnValue(loc);
		}
	}
}
