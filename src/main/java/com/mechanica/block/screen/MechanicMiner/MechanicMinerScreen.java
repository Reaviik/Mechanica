package com.mechanica.block.screen.MechanicMiner;

import com.mechanica.Mechanica;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.slf4j.Logger;

public class MechanicMinerScreen extends AbstractContainerScreen<MechanicMinerMenu> {

    //O caminho até a textura da GUI é setado aqui
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Mechanica.MOD_ID, "textures/gui/mechanic_miner_gui.png");
    //Define a Scream
    public MechanicMinerScreen(MechanicMinerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }
    private static final Logger LOGGER = LogUtils.getLogger();
    //Renderiza a GUI na posião x, y, com base no tanahdo da imagem e da tela
    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        //Posições definir o x 0 e y 0 da GUI
        this.imageHeight = 226;
        int x = (width - imageWidth) / 2;
        int y = (height - (imageHeight + 58)) / 2;
        //Renderiza a GUI
        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);
    }

    //Ainda não sei, pergunta pro player
    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pPoseStack, pMouseX, pMouseY);
    }
    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, this.title, +19.0F, -54.0F, 16777215);
        this.font.draw(matrixStack, this.playerInventoryTitle, 8.0F, (float) (this.imageHeight - 152), 16777215);
    }
}
