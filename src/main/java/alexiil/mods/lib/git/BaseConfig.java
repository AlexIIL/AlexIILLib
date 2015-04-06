package alexiil.mods.lib.git;

import java.util.Collections;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import alexiil.mods.lib.AlexIILMod;
import alexiil.mods.lib.LangUtils;

public abstract class BaseConfig extends GuiScreen {
    private GitHubUserScrollingList contributors;
    private CommitScrollingList commits;
    public final AlexIILMod mod;

    public BaseConfig(GuiScreen screen, AlexIILMod mod) {
        fontRendererObj = Minecraft.getMinecraft().fontRendererObj;
        this.mod = mod;
        setupGui();
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height) {
        super.setWorldAndResolution(mc, width, height);
        setupGui();
    }

    protected void setupGui() {
        int width = 0;

        contributors = new GitHubUserScrollingList(this, width + 40, this.height, 40, this.height - 40, 10);
        for (GitHubUser c : mod.getContributors())
            contributors.userList.add(c);

        commits = new CommitScrollingList(this, this.width - width - 80, this.height, 40, this.height - 40, width + 60);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground(0);
        if (mod.connectExternally.getBoolean()) {
            commits.drawScreen(mouseX, mouseY, partialTicks);
            contributors.drawScreen(mouseX, mouseY, partialTicks);
            drawString(fontRendererObj, LangUtils.format("alexiilutils.gui.contributors"), 8, 30, 0xFFFFFF);
            String text = LangUtils.format("alexiilutils.gui.commits");
            drawString(fontRendererObj, text, this.width - fontRendererObj.getStringWidth(text) - 10, 30, 0xFFFFFF);
        }
        else {
            String text = LangUtils.format("alexiilutils.gui.connectExternallyDisabled");
            int textWidth = fontRendererObj.getStringWidth(text);
            drawHoveringText(Collections.singletonList(text), (this.width - textWidth) / 2, this.height / 2);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public FontRenderer getFontRenderer() {
        return fontRendererObj;
    }
}
