package org.zamecki.nm.mixin;

import net.minecraft.entity.passive.AnimalEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zamecki.nm.utils.INaturalEntityData;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin implements INaturalEntityData {
    @Unique
    public boolean getAnimalSex() {
        return naturalMobs$getNaturalData().getBoolean("sex");
    }

    @Inject(method = "canBreedWith", at = @At("HEAD"), cancellable = true)
    private void injectCanBreedWith(AnimalEntity other, CallbackInfoReturnable<Boolean> callbackInfo) {
        INaturalEntityData otherNaturalData = (INaturalEntityData) other;
        if (getAnimalSex() == otherNaturalData.naturalMobs$getNaturalData().getBoolean("sex")) {
            callbackInfo.setReturnValue(false);
        }
    }

}
