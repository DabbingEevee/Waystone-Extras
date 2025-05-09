package com.existingeevee.waystone_extras.mixin.json_render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.existingeevee.waystone_extras.WaystoneExtras;
import com.existingeevee.waystone_extras.features.WaystoneJsonRenderer;

import net.minecraft.block.Block;
import net.minecraft.util.BlockRenderLayer;

@Mixin(Block.class)
public abstract class MixinBlock {

	@Inject(method = "getRenderLayer", at = @At("HEAD"), cancellable = true)
	public void waystone_extras$changeWaystoneRenderLayer(CallbackInfoReturnable<BlockRenderLayer> ci) {
		if (WaystoneJsonRenderer.isEnabled() && (Block) (Object) this == WaystoneExtras.blockWaystone) {
			ci.setReturnValue(BlockRenderLayer.CUTOUT_MIPPED);
		}
	}
}
