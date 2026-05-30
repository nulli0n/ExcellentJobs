package su.nightexpress.excellentjobs.grind.bar;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.api.grind.bar.BarElement;
import su.nightexpress.nightcore.bridge.currency.Currency;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class GrindBarElement implements BarElement {

    private final String   id;
    private final Currency currency;
    private final String   format;

    private double amount;

    public GrindBarElement(Currency currency, double amount, String format) {
        this.id = "currency_" + currency.getInternalId();
        this.currency = currency;
        this.amount = amount;
        this.format = format;
    }

    @Override
    public boolean shouldRender() {
        return this.amount != 0D;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public int getPosition() {
        return 0;
    }

    @Override
    public void merge(BarElement from) {
        if (from instanceof GrindBarElement element) {
            this.amount += element.amount;
        }
    }

    @Override
    public String render() {
        PlaceholderContext context = PlaceholderContext.builder()
            .with(CommonPlaceholders.GENERIC_AMOUNT, () -> this.currency.format(this.amount))
            .build();

        return context.apply(this.format);
    }
}
