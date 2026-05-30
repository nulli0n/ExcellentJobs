package su.nightexpress.excellentjobs.grind.modifier.impl;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.api.grind.GrindContext;
import su.nightexpress.excellentjobs.api.grind.GrindModifier;
import su.nightexpress.excellentjobs.api.grind.GrindObjectiveProperty;
import su.nightexpress.excellentjobs.api.grind.GrindReward;
import su.nightexpress.excellentjobs.grind.context.impl.EnchantingGrindContext;
import su.nightexpress.excellentjobs.grind.modifier.codec.EnchantingModifierCodec;
import su.nightexpress.excellentjobs.util.Calc;

@NullMarked
public class EnchantingGrindModifier implements GrindModifier {

    public static final EnchantingModifierCodec CODEC = new EnchantingModifierCodec();

    private final double levelBonus;

    public EnchantingGrindModifier() {
        this(25D);
    }

    // TODO Only level 3+ ? prevent abusing with wooden items and level 1

    public EnchantingGrindModifier(double levelBonus) {
        this.levelBonus = levelBonus;
    }

    @Override
    public void modify(GrindReward reward, GrindContext context) {
        if (!(context instanceof EnchantingGrindContext enchantingContext)) return;

        double modeBonus = Calc.toMult(this.levelBonus * enchantingContext.getEnchantmentLevel());

        reward.modifyIfPresent(GrindObjectiveProperty.XP, xp -> xp * modeBonus);
        reward.modifyIfPresent(GrindObjectiveProperty.INCOME, income -> income * modeBonus);
    }

    public double getLevelBonus() {
        return this.levelBonus;
    }
}
