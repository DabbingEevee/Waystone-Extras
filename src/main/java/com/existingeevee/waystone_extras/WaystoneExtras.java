package com.existingeevee.waystone_extras;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = WaystoneExtras.MODID)
public class WaystoneExtras {
    public static final String MODID = "waystone_extras";

    public WaystoneExtras() {
    	//we need this EARLY. 
    	//otherwise stuff in preinit wont be affected
    	ConfigHandler.init(); 
    }
    
    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        System.out.println("Hello world!");
    }
}
