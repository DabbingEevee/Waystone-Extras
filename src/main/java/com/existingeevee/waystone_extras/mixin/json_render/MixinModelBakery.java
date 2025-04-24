package com.existingeevee.waystone_extras.mixin.json_render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.existingeevee.waystone_extras.features.WaystoneJsonRenderer;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.util.ResourceLocation;

@Mixin(ModelBakery.class)
public abstract class MixinModelBakery {

	@Inject(method = { "getBlockstateLocation(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/util/ResourceLocation;", "func_188631_b(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/util/ResourceLocation;" }, at = @At("HEAD"), cancellable = true, remap = false)
	private void waystone_extras$replaceWaystoneBlockstateDef(ResourceLocation location, CallbackInfoReturnable<ResourceLocation> ci) {
		ResourceLocation loc = WaystoneJsonRenderer.getJsonWaystoneModelLocation(location);
		if (loc != null) {
			ci.setReturnValue(loc);
		}
	}
}
