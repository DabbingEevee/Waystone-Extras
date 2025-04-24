package com.existingeevee.waystone_extras.mixin.cost;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.existingeevee.waystone_extras.WaystoneExtrasConfig;
import com.existingeevee.waystone_extras.features.WaystoneItemCost;

import net.blay09.mods.waystones.WaystoneManager;
import net.blay09.mods.waystones.block.TileWaystone;
import net.blay09.mods.waystones.network.handler.HandlerTeleportToWaystone;
import net.blay09.mods.waystones.network.message.MessageTeleportToWaystone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

@Mixin(HandlerTeleportToWaystone.class)
public class MixinHandlerTeleportToWaystone {

	@Inject(method = "lambda$onMessage$0(Lnet/minecraftforge/fml/common/network/simpleimpl/MessageContext;Lnet/blay09/mods/waystones/network/message/MessageTeleportToWaystone;)V", at = @At("HEAD"), cancellable = true)
	private static void waystone_extras$preventButtonPressNoItem(MessageContext ctx, MessageTeleportToWaystone message,  CallbackInfo ci) {
        EntityPlayer player = ctx.getServerHandler().player;
        TileWaystone waystone = WaystoneManager.getWaystoneInWorld(message.getWaystone());
        
		String cfg = WaystoneExtrasConfig.ItemCost.teleportationCostItem;
		if (cfg.trim().isEmpty() || player.capabilities.isCreativeMode)
			return;

		int dist = (int) Math.sqrt(player.getDistanceSqToCenter(waystone.getPos()));
		int itemCost = (int) (WaystoneExtrasConfig.ItemCost.teleportationCostCoefficient * dist);

		if (itemCost <= 0)
			return;

		if (!WaystoneItemCost.doesPlayerHaveTPItem(player, true, itemCost)) {
			ci.cancel();
		}
	}
}
