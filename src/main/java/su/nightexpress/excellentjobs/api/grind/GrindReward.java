package su.nightexpress.excellentjobs.api.grind;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class GrindReward {

    private final Map<GrindObjectiveProperty, Double> properties;

    public GrindReward() {
        this.properties = new EnumMap<>(GrindObjectiveProperty.class);
    }

    public boolean has(GrindObjectiveProperty property) {
        return this.properties.containsKey(property);
    }

    public GrindReward put(GrindObjectiveProperty property, double value) {
        this.properties.put(property, value);
        return this;
    }

    public double get(GrindObjectiveProperty property) {
        return this.properties.getOrDefault(property, 0D);
    }

    public void remove(GrindObjectiveProperty property) {
        this.properties.remove(property);
    }

    public void modifyIfPresent(GrindObjectiveProperty property, DoubleUnaryOperator operator) {
        if (this.has(property)) {
            this.modify(property, operator);
        }
    }

    public void modify(GrindObjectiveProperty property, DoubleUnaryOperator operator) {
        double value = this.get(property);
        this.put(property, operator.applyAsDouble(value));
    }
}
