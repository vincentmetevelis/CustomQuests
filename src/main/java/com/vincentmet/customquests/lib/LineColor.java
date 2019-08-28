package com.vincentmet.customquests.lib;

import net.minecraft.util.ResourceLocation;

public enum LineColor {
    BLACK(0, Ref.IMAGE_COLOR_BLACK),
    RED(1, Ref.IMAGE_COLOR_RED),
    YELLOW(2, Ref.IMAGE_COLOR_YELLOW),
    GREEN(3, Ref.IMAGE_COLOR_GREEN),
    WHITE(4, Ref.IMAGE_COLOR_WHITE);

    private int id;
    private ResourceLocation resourceLocation;

    LineColor(int id, ResourceLocation resourceLocation){
        this.id = id;
        this.resourceLocation = resourceLocation;
    }

    public int getId() {
        return id;
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }
}
