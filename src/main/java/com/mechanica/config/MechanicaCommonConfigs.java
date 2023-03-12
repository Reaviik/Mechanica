package com.mechanica.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.lwjgl.system.CallbackI;

public class MechanicaCommonConfigs {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> NETHERITE_DROP_CHANCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> QUARTZ_DROP_CHANCE;

    static {
        BUILDER.push("Configs for Mechanica");

        NETHERITE_DROP_CHANCE = BUILDER.comment("Quantidade de netherite no primeiro tier (Escalonavel)")
                .define("Netherite Chance", 1);
        QUARTZ_DROP_CHANCE = BUILDER.comment("Quantidade de quartz no primeiro tier (Escalonavel)")
                .defineInRange("Quartz Chance", 2,1,5);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
