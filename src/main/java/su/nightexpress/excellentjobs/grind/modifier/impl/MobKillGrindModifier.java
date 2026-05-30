package su.nightexpress.excellentjobs.grind.modifier.impl;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.api.grind.GrindContext;
import su.nightexpress.excellentjobs.api.grind.GrindModifier;
import su.nightexpress.excellentjobs.api.grind.GrindObjectiveProperty;
import su.nightexpress.excellentjobs.api.grind.GrindReward;
import su.nightexpress.excellentjobs.grind.context.impl.KillingGrindContext;
import su.nightexpress.excellentjobs.grind.modifier.codec.MobKillModifierCodec;
import su.nightexpress.excellentjobs.util.Calc;

@NullMarked
public class MobKillGrindModifier implements GrindModifier {

    public static final MobKillModifierCodec CODEC = new MobKillModifierCodec();

    private final double spawnerMobBonus;

    public MobKillGrindModifier() {
        this(-90D);
    }

    public MobKillGrindModifier(double spawnerMobBonus) {
        this.spawnerMobBonus = spawnerMobBonus;
    }

    @Override
    public void modify(GrindReward reward, GrindContext context) {
        if (context instanceof KillingGrindContext killContext && killContext.isSpawnerMob()) {
            double modeBonus = Calc.toMult(this.spawnerMobBonus);
            reward.modifyIfPresent(GrindObjectiveProperty.XP, xp -> xp * modeBonus);
            reward.modifyIfPresent(GrindObjectiveProperty.INCOME, income -> income * modeBonus);
            reward.modifyIfPresent(GrindObjectiveProperty.CONTRACT_POINTS, points -> points * modeBonus);
        }
    }

    public double getSpawnerMobBonus() {
        return this.spawnerMobBonus;
    }
}
