package com.existingeevee.waystone_extras;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class WaystoneExtrasConfig {

	public static class JsonModelRenderer {
		public static boolean useJsonModelRenderer;
	}
	
	public static class ItemCost {
		public static boolean useItemCost;
		
		public static String teleportationCostItem;
		public static float teleportationCostCoefficient;
		public static float teleportationMaxCost;
		public static boolean teleportationCostDurability;
		
		public static String teleportationKeyItem;
		public static int teleportationKeyCostCount;
		public static boolean teleportationKeyCostDurability;
		public static boolean teleportationKeyInBaubles;
	}

	private static void initConfig(File file) {
		Configuration config = new Configuration(file);
		String category;
		
		category = "Use Json Rendering";
		config.addCustomCategoryComment(category, "Enabling this feature will override the hardcoded tileentity renderer of the waystone with a standard json-based model. Note that enabling this will cause dynamic rendering features (such as the mossy variant and the text only appearing after knowing about it) to not render.");
		JsonModelRenderer.useJsonModelRenderer = config.getBoolean("Enable Json Renderer", category, false, "Set to \"true\" if you wish to use the json-based rendering for waystones.");
		
		category = "Item-Based Cost";
		config.addCustomCategoryComment(category, "Enabling this feature will add an item-based cost system to waystones.");
		ItemCost.useItemCost = config.getBoolean("Enable Item-Based Cost", category, false, "Set to \"true\" if you wish to use the item-based teleportation cost for waystones.");
		ItemCost.teleportationCostItem = config.getString("Teleportation Cost Item", category, "", "The item that is consumed or damaged to actually teleport. Note that if \"Teleportation Cost Uses Durability\" is enabled, the meta value must be a wildcard or issues will happen. Format is \"modid:item|meta\", with * being used for a wildcard meta value.");
		ItemCost.teleportationCostCoefficient = config.getFloat("Teleportation Cost Coefficient", category, 1f, 0, Float.MAX_VALUE, "The cost multiplier per block for the amount of item(s) consumed or damaged. Setting this to zero will cause the block to require the item only.");
		ItemCost.teleportationMaxCost = config.getInt("Teleportation Max Cost", category, 32, 0, Integer.MAX_VALUE, "The maximum cost for the amount of item(s) that are consumed or damaged. Setting this to zero will disable this feature entirely.");
		ItemCost.teleportationCostDurability = config.getBoolean("Teleportation Cost Uses Durability", category, false, "Set to \"true\" if you wish to for the teleportation cost to consume durability instead of conusuming whole items.");

		ItemCost.teleportationKeyItem = config.getString("Teleportation Key Item", category, "", "The item that is consumed or damaged to open the teleportation GUI. Note that if \"Teleportation Key Cost Uses Durability\" is enabled, the meta value must be a wildcard or issues will happen. Format is \"modid:item|meta\", with * being used for a wildcard meta value.");
		ItemCost.teleportationKeyCostCount = config.getInt("Teleportation Key Cost", category, 1, 0, Integer.MAX_VALUE, "The cost for the amount of item(s) consumed or damaged. Setting this to zero will cause the block to require the item only.");
		ItemCost.teleportationKeyCostDurability = config.getBoolean("Teleportation Key Cost Uses Durability", category, false, "Set to \"true\" if you wish to for the teleportation key cost to consume durability instead of conusuming whole items.");
		ItemCost.teleportationKeyInBaubles = config.getBoolean("Teleportation Key Bauble", category, false, "Set to \"true\" if you wish to for the teleportation key to be worn in a baubles slot to function.");
		
		config.save();
	}

	protected static void init() {
		File configFolder = new File(".", "config");
		configFolder.mkdirs();
		initConfig(new File(configFolder.getPath(), WaystoneExtras.MODID + ".cfg"));
	}
}
