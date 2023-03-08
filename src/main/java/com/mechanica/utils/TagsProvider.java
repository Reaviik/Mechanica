package com.mechanica.utils;

import com.mechanica.item.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class TagsProvider {
    public static class Blocks extends BlockTagsProvider {
        public Blocks(DataGenerator generatorIn, String modId, ExistingFileHelper existingFileHelper) {
            super(generatorIn, modId, existingFileHelper);
        }

        @Override
        protected void addTags() {
            //tag(ITags.Blocks.CONVEYOR_BELT).get());
        }
    }

    public static class Items extends ItemTagsProvider {
        public Items(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, String modId, ExistingFileHelper existingFileHelper) {
            super(dataGenerator, blockTagProvider, modId, existingFileHelper);
        }

        @Override
        protected void addTags() {

            tag(ITags.Items.MATRIX)
                    .add(ModItems.STARLUME_MATRIX.get().asItem())
                    .add(ModItems.SUNDUST_MATRIX.get().asItem())
                    .add(ModItems.MOONSHADOW_MATRIX.get().asItem())
                    .add(ModItems.EMBERGLOW_MATRIX.get().asItem())
                    .add(ModItems.AQUAMIST_MATRIX.get().asItem())
                    .add(ModItems.EARTHSONG_MATRIX.get().asItem());
        }
    }
}

