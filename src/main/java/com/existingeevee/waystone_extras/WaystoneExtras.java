package com.existingeevee.waystone_extras;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = WaystoneExtras.MODID)
public class WaystoneExtras {
    public static final String MODID = "waystone_extras";

    @GameRegistry.ObjectHolder("waystones:waystone")
    public static final Block blockWaystone = null;
    
    public WaystoneExtras() {
    	//we need this EARLY. 
    	//otherwise stuff in preinit wont be affected
    	WaystoneExtrasConfig.init(); 
    }
    
    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        System.out.println("Hello world, hello waystone improvements!");
    }
}
