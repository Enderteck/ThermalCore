package cofh.thermal.lib.compat.jei;

import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.lib.util.recipes.ThermalFuel;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static cofh.lib.util.helpers.StringHelper.getTextComponent;

public abstract class ThermalFuelCategory<T extends ThermalFuel> implements IRecipeCategory<T> {

    protected final int DURATION_X = 70;
    protected final int DURATION_Y = 24;

    protected final int ENERGY_X = 106;
    protected final int ENERGY_Y = 10;

    protected final RecipeType<T> type;
    protected IDrawable background;
    protected IDrawable icon;
    protected Component name;

    protected IDrawableStatic energyBackground;
    protected IDrawableStatic durationBackground;

    protected IDrawableAnimated energy;
    protected IDrawableAnimated duration;

    public ThermalFuelCategory(IGuiHelper guiHelper, ItemStack icon, RecipeType<T> type) {

        this(guiHelper, icon, type, true);
    }

    public ThermalFuelCategory(IGuiHelper guiHelper, ItemStack icon, RecipeType<T> type, boolean drawEnergy) {

        this.type = type;
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, icon);

        if (drawEnergy) {
            energyBackground = Drawables.getDrawables(guiHelper).getEnergyEmpty();
            energy = guiHelper.createAnimatedDrawable(Drawables.getDrawables(guiHelper).getEnergyFill(), 400, IDrawableAnimated.StartDirection.BOTTOM, false);
        }
    }

    // region IRecipeCategory
    @Override
    public Component getTitle() {

        return name;
    }

    @Override
    public IDrawable getBackground() {

        return background;
    }

    @Override
    public IDrawable getIcon() {

        return icon;
    }

    @Override
    public void draw(T recipe, IRecipeSlotsView recipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {

        if (energyBackground != null) {
            energyBackground.draw(matrixStack, ENERGY_X, ENERGY_Y);
        }
        if (energy != null) {
            energy.draw(matrixStack, ENERGY_X, ENERGY_Y);
        }

        if (durationBackground != null) {
            durationBackground.draw(matrixStack, DURATION_X, DURATION_Y);
        }
        if (duration != null) {
            duration.draw(matrixStack, DURATION_X, DURATION_Y);
        }
    }

    @Override
    public List<Component> getTooltipStrings(T recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {

        List<Component> tooltip = new ArrayList<>();

        if (energy != null && mouseX > ENERGY_X && mouseX < ENERGY_X + energy.getWidth() - 1 && mouseY > ENERGY_Y && mouseY < ENERGY_Y + energy.getHeight() - 1) {
            tooltip.add(getTextComponent("info.cofh.energy").append(": " + StringHelper.format(recipe.getEnergy()) + " RF"));
        }
        return tooltip;
    }
    // endregion
}