package su.nightexpress.excellentjobs.contract.bar;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.api.grind.bar.BarElement;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class ContractBarElement implements BarElement {

    private final String format;

    private double points;

    public ContractBarElement(String format, double points) {
        this.format = format;
        this.points = points;
    }

    @Override
    public String getId() {
        return "contract_points";
    }

    @Override
    public int getPosition() {
        return 2;
    }

    @Override
    public void merge(BarElement from) {
        if (from instanceof ContractBarElement element) {
            this.points += element.points;
        }
    }

    @Override
    public String render() {
        PlaceholderContext context = PlaceholderContext.builder()
            .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(this.points))
            .build();

        return context.apply(this.format);
    }

    @Override
    public boolean shouldRender() {
        return this.points != 0;
    }
}
