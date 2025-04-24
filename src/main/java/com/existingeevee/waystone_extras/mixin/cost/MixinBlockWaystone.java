package com.existingeevee.waystone_extras.mixin.cost;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.existingeevee.waystone_extras.WaystoneExtrasConfig;
import com.existingeevee.waystone_extras.features.WaystoneItemCost;

import net.blay09.mods.waystones.WaystoneConfig;
import net.blay09.mods.waystones.block.BlockWaystone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

@Mixin(value = BlockWaystone.class, remap = false)
public class MixinBlockWaystone {

	@Inject(method = { 
			"onBlockActivated(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;Lnet/minecraft/util/EnumFacing;FFF)Z", 
			"func_180639_a(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;Lnet/minecraft/util/EnumFacing;FFF)Z" }, 
			at = @At(value = "HEAD"), cancellable = true, remap = false)
	private void waystone_extras$preventUsageWithoutKey(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ, CallbackInfoReturnable<Boolean> ci) {
        if (player.isSneaking() && (player.capabilities.isCreativeMode || !WaystoneConfig.general.creativeModeOnly)) {
        	return; 
        }

		if (WaystoneItemCost.isEnabled() && !WaystoneExtrasConfig.ItemCost.teleportationKeyItem.trim().isEmpty()) {
			if (!WaystoneItemCost.doesPlayerHaveKey(player, !world.isRemote)) {
                player.sendStatusMessage(new TextComponentTranslation("waystone_extras:missingKey"), true);
				ci.setReturnValue(true);
			}
		}
	}
}
