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
            //tag(ORES).addTag(ITags.Blocks.ASNIUM_ORE);
            //tag(ITags.Blocks.RESOURCE_NODES).add(ModBlocks.IRON_RESOURCE_NODE.get().get());

            //tag(BlockTags.MINEABLE_WITH_PICKAXE).addTag(ITags.Blocks.ASNIUM_ORE);

            // All of our blocks are mineable with a pickaxe
//            for (var block : Registry.BLOCK) {
//                if (Registry.BLOCK.getKey(block).getNamespace().equals(MinersDream.MOD_ID)) {
//                    tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block);
//                }
//            }

            // However the uraninite ores require at least an iron pickaxe
            //tag(BlockTags.NEEDS_IRON_TOOL).addTag(ITags.Blocks.ASNIUM_ORE);
        }
    }

    public static class Items extends ItemTagsProvider {
        public Items(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, String modId, ExistingFileHelper existingFileHelper) {
            super(dataGenerator, blockTagProvider, modId, existingFileHelper);
        }

        @Override
        protected void addTags() {

            //tag(Tags.Items.ORES).addTag(ITags.Items.ASNIUM_ORE);

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