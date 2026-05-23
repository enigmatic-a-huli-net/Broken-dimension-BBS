/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.brokendimensionbbs.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;

import net.minecraft.world.level.block.Block;

import net.mcreator.brokendimensionbbs.block.PortalBeaconBlock;
import net.mcreator.brokendimensionbbs.block.DimensionportalcatalizatorBlock;
import net.mcreator.brokendimensionbbs.block.BrokendimensionportalBlock;
import net.mcreator.brokendimensionbbs.BrokenDimensionBbsMod;

public class BrokenDimensionBbsModBlocks {
	public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(BrokenDimensionBbsMod.MODID);
	public static final DeferredBlock<Block> BROKENDIMENSIONPORTAL;
	public static final DeferredBlock<Block> PORTAL_BEACON;
	public static final DeferredBlock<Block> DIMENSIONPORTALCATALIZATOR;
	static {
		BROKENDIMENSIONPORTAL = REGISTRY.register("brokendimensionportal", BrokendimensionportalBlock::new);
		PORTAL_BEACON = REGISTRY.register("portal_beacon", PortalBeaconBlock::new);
		DIMENSIONPORTALCATALIZATOR = REGISTRY.register("dimensionportalcatalizator", DimensionportalcatalizatorBlock::new);
	}
	// Start of user code block custom blocks
	// End of user code block custom blocks
}