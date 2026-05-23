/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.brokendimensionbbs.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import net.mcreator.brokendimensionbbs.BrokenDimensionBbsMod;

@EventBusSubscriber
public class BrokenDimensionBbsModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BrokenDimensionBbsMod.MODID);
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BROKENDIMENSIONBBS = REGISTRY.register("brokendimensionbbs",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.broken_dimension_bbs.brokendimensionbbs")).icon(() -> new ItemStack(BrokenDimensionBbsModBlocks.PORTAL_BEACON.get())).displayItems((parameters, tabData) -> {
				tabData.accept(BrokenDimensionBbsModBlocks.PORTAL_BEACON.get().asItem());
				tabData.accept(BrokenDimensionBbsModBlocks.DIMENSIONPORTALCATALIZATOR.get().asItem());
			}).build());

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey() == CreativeModeTabs.OP_BLOCKS) {
			if (tabData.hasPermissions()) {
				tabData.accept(BrokenDimensionBbsModBlocks.BROKENDIMENSIONPORTAL.get().asItem());
			}
		}
	}
}