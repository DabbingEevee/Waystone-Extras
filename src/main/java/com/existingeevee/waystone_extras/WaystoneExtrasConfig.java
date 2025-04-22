package com.existingeevee.waystone_extras;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class WaystoneExtrasConfig {

	public static class JsonModelRenderer {
		public static boolean useJsonModelRenderer = false;
	}

	private static void initConfig(File file) {
		Configuration config = new Configuration(file);
		String category;
		
		category = "Use Json Rendering";
		config.addCustomCategoryComment(category, "Enabling this feature will override the hardcoded tileentity renderer of the waystone with a standard json-based model. Note that enabling this will cause dynamic rendering features (such as the mossy variant and the text only appearing after knowing about it) to not render.");
		JsonModelRenderer.useJsonModelRenderer = config.getBoolean("Enable Json Renderer", category, false, "Set to \"true\" if you wish to use the json-based rendering for waystones.");
		
		category = "Item-based Cost";
		config.addCustomCategoryComment(category, "Enabling this feature will override the hardcoded XP-based cost system in vanilla waystones.");
		
		config.save();
	}

	protected static void init() {
		File configFolder = new File(".", "config");
		configFolder.mkdirs();
		initConfig(new File(configFolder.getPath(), WaystoneExtras.MODID + ".cfg"));
	}
}
