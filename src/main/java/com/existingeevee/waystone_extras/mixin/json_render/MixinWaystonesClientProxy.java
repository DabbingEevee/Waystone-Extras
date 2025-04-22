package com.existingeevee.waystone_extras.mixin.json_render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.existingeevee.waystone_extras.features.WaystoneJsonRenderer;

import net.blay09.mods.waystones.client.ClientProxy;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.client.registry.ClientRegistry;

@Mixin(remap = false, value = ClientProxy.class)
public abstract class MixinWaystonesClientProxy {

	@Redirect(method = "preInit()V", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/client/registry/ClientRegistry;bindTileEntitySpecialRenderer(Ljava/lang/Class;Lnet/minecraft/client/renderer/tileentity/TileEntitySpecialRenderer;)V"))
	private void waystone_extras$preventTESRRegistry(Class<TileEntity> tileEntityClass, TileEntitySpecialRenderer<TileEntity> specialRenderer) {
		//register it as usual
		if (!WaystoneJsonRenderer.isEnabled()) {
	        ClientRegistry.bindTileEntitySpecialRenderer(tileEntityClass, specialRenderer);
		}
	}
}