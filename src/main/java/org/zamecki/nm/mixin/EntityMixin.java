package org.zamecki.nm.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zamecki.nm.utils.INaturalEntityData;

@Mixin(Entity.class)
public abstract class EntityMixin implements INaturalEntityData {
    @Unique
    private NbtCompound naturalData = null;

    @Override
    public NbtCompound naturalMobs$getNaturalData() {
        if (naturalData == null) {
            naturalData = new NbtCompound();
        }
        return naturalData;
    }
    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> callbackInfo) {
        if (naturalData != null) {
            nbt.put("NaturalData", naturalData);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
        if (nbt.contains("NaturalData")) {
            naturalData = nbt.getCompound("NaturalData");
        }
    }
}
