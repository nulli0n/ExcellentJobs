package su.nightexpress.excellentjobs.grind.modifier.impl;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.api.grind.GrindContext;
import su.nightexpress.excellentjobs.api.grind.GrindModifier;
import su.nightexpress.excellentjobs.api.grind.GrindObjectiveProperty;
import su.nightexpress.excellentjobs.api.grind.GrindReward;
import su.nightexpress.excellentjobs.grind.context.impl.CookingGrindContext;
import su.nightexpress.excellentjobs.grind.modifier.codec.CookingModifierCodec;
import su.nightexpress.excellentjobs.util.Calc;

@NullMarked
public class CookingGrindModifier implements GrindModifier {

    public static final CookingModifierCodec CODEC = new CookingModifierCodec();

    private final double autoSmeltBonus;
    private final double manualSmeltBonus;

    public CookingGrindModifier() {
        this(-80D, 50D);
    }

    public CookingGrindModifier(double autoSmeltBonus, double manualSmeltBonus) {
        this.autoSmeltBonus = autoSmeltBonus;
        this.manualSmeltBonus = manualSmeltBonus;
    }

    @Override
    public void modify(GrindReward reward, GrindContext context) {
        if (!(context instanceof CookingGrindContext cookingContext)) return;

        double modeBonus = Calc.toMult(cookingContext.isAutomated() ? this.autoSmeltBonus : this.manualSmeltBonus);

        reward.modifyIfPresent(GrindObjectiveProperty.XP, xp -> xp * modeBonus);
        reward.modifyIfPresent(GrindObjectiveProperty.INCOME, income -> income * modeBonus);
    }


    public double getAutoSmeltBonus() {
        return this.autoSmeltBonus;
    }

    public double getManualSmeltBonus() {
        return this.manualSmeltBonus;
    }
}
