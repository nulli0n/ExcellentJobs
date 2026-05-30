package su.nightexpress.excellentjobs.grind.modifier.impl;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.api.grind.GrindContext;
import su.nightexpress.excellentjobs.api.grind.GrindModifier;
import su.nightexpress.excellentjobs.api.grind.GrindObjectiveProperty;
import su.nightexpress.excellentjobs.api.grind.GrindReward;
import su.nightexpress.excellentjobs.grind.context.impl.BrewingGrindContext;
import su.nightexpress.excellentjobs.grind.modifier.codec.BrewingModifierCodec;
import su.nightexpress.excellentjobs.util.Calc;

@NullMarked
public class BrewingGrindModifier implements GrindModifier {

    public static final BrewingModifierCodec CODEC = new BrewingModifierCodec();

    // TODO Bonus for potion type (Splash, Lingering)

    private final double amountBonus;
    private final double autoBrewPenalty;
    private final double manualBrewBonus;

    public BrewingGrindModifier() {
        this(25, -80, 50);
    }

    public BrewingGrindModifier(double amountBonus, double autoBrewPenalty, double manualBrewBonus) {
        this.amountBonus = amountBonus;
        this.autoBrewPenalty = autoBrewPenalty;
        this.manualBrewBonus = manualBrewBonus;
    }

    @Override
    public void modify(GrindReward reward, GrindContext context) {
        if (!(context instanceof BrewingGrindContext brewingContext)) return;

        boolean isAutomated = brewingContext.isAutomated();
        int potionsAmount = brewingContext.getPotionsAmount();

        double modeBonus = Calc.toMult(isAutomated ? this.autoBrewPenalty : this.manualBrewBonus);
        double potionBonus = Calc.toMult(this.amountBonus * potionsAmount);

        reward.modifyIfPresent(GrindObjectiveProperty.XP, xp -> xp * modeBonus * potionBonus);
        reward.modifyIfPresent(GrindObjectiveProperty.INCOME, income -> income * modeBonus * potionBonus);
    }

    public double getAmountBonus() {
        return this.amountBonus;
    }

    public double getAutoBrewPenalty() {
        return this.autoBrewPenalty;
    }

    public double getManualBrewBonus() {
        return this.manualBrewBonus;
    }
}
