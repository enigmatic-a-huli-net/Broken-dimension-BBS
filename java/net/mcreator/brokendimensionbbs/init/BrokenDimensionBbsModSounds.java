/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.brokendimensionbbs.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

import net.mcreator.brokendimensionbbs.BrokenDimensionBbsMod;

public class BrokenDimensionBbsModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, BrokenDimensionBbsMod.MODID);
	public static final DeferredHolder<SoundEvent, SoundEvent> DIMENSION_TRAVEL = REGISTRY.register("dimension_travel", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("broken_dimension_bbs", "dimension_travel")));
	public static final DeferredHolder<SoundEvent, SoundEvent> DIMENSION_PORTAL = REGISTRY.register("dimension_portal", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("broken_dimension_bbs", "dimension_portal")));
	public static final DeferredHolder<SoundEvent, SoundEvent> DIMENSION_TRIGGER1 = REGISTRY.register("dimension_trigger1",
			() -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("broken_dimension_bbs", "dimension_trigger1")));
	public static final DeferredHolder<SoundEvent, SoundEvent> DIMENSION_TRIGGER2 = REGISTRY.register("dimension_trigger2",
			() -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("broken_dimension_bbs", "dimension_trigger2")));
	public static final DeferredHolder<SoundEvent, SoundEvent> DIMENSION_TRIGGER = REGISTRY.register("dimension_trigger", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("broken_dimension_bbs", "dimension_trigger")));
}