package su.nightexpress.excellentjobs.grind.context.impl;

import su.nightexpress.excellentjobs.api.grind.GrindContext;

public class CookingGrindContext extends GrindContext {

    private final boolean automated;

    public CookingGrindContext(int amount, boolean automated) {
        super(null, amount);
        this.automated = automated;
    }

    public boolean isAutomated() {
        return this.automated;
    }
}
