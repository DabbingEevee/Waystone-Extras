package com.existingeevee.waystone_extras.mixin.json_render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.existingeevee.waystone_extras.WaystoneExtras;
import com.existingeevee.waystone_extras.features.WaystoneJsonRenderer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;

@Mixin(BlockContainer.class)
public class MixinBlockContainer {

	@Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
	public void waystone_extras$changeWaystoneRenderType(IBlockState state, CallbackInfoReturnable<EnumBlockRenderType> ci) {
		if (WaystoneJsonRenderer.isEnabled() && (Block) (Object) this == WaystoneExtras.blockWaystone) {
			ci.setReturnValue(EnumBlockRenderType.MODEL);
		}
	}
	
}
