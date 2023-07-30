package com.refinedmods.refinedstorage2.platform.common.screen.grid;

import com.refinedmods.refinedstorage2.platform.common.containermenu.grid.AbstractGridContainerMenu;
import com.refinedmods.refinedstorage2.platform.common.screen.widget.AbstractSideButtonWidget;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import static com.refinedmods.refinedstorage2.platform.common.util.IdentifierUtil.createTranslation;

public class SynchronizationSideButtonWidget extends AbstractSideButtonWidget {
    private static final MutableComponent TITLE = createTranslation("gui", "grid.synchronizer");

    private final AbstractGridContainerMenu menu;

    public SynchronizationSideButtonWidget(final AbstractGridContainerMenu menu) {
        super(createPressAction(menu));
        this.menu = menu;
    }

    private static OnPress createPressAction(final AbstractGridContainerMenu menu) {
        return btn -> menu.toggleSynchronizer();
    }

    @Override
    protected ResourceLocation getTextureIdentifier() {
        return menu.getSynchronizer().getTextureIdentifier();
    }

    @Override
    protected MutableComponent getTitle() {
        return TITLE;
    }

    @Override
    protected MutableComponent getSubText() {
        return menu.getSynchronizer().getTitle();
    }

    @Override
    protected int getXTexture() {
        return menu.getSynchronizer().getXTexture();
    }

    @Override
    protected int getYTexture() {
        return menu.getSynchronizer().getYTexture();
    }
}
