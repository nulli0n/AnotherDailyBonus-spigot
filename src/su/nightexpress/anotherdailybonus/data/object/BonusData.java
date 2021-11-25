package su.nightexpress.anotherdailybonus.data.object;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.manager.IPlaceholder;
import su.nexmedia.engine.utils.TimeUT;
import su.nightexpress.anotherdailybonus.AnotherDailyBonus;

import java.util.function.UnaryOperator;

public class BonusData implements IPlaceholder {

    private int rewardCount;
    private long timeUntilNextReward;
    private long timeUntilResetReward;

    public static final String PLACEHOLDER_EXPIRE_IN = "%user_bonus_expire_in%";
    public static final String PLACEHOLDER_UPCOMING_IN = "%user_bonus_upcoming_in%";
    public static final String PLACEHOLDER_AVAILABLE   = "%user_bonus_available%";

    @Override
    @NotNull
    public UnaryOperator<String> replacePlaceholders() {
        long reset = this.getTimeUntilResetReward();
        long next = this.getTimeUntilNextReward();

        return str -> str
            .replace(PLACEHOLDER_EXPIRE_IN, TimeUT.formatTimeLeft(reset == 0 ? System.currentTimeMillis() : reset))
            .replace(PLACEHOLDER_UPCOMING_IN, TimeUT.formatTimeLeft(next == 0 ? System.currentTimeMillis() : next))
            .replace(PLACEHOLDER_AVAILABLE, AnotherDailyBonus.getInstance().lang().getBool(this.isReady()))
            ;
    }

    public BonusData() {
        this(0, 0, 0);
    }

    public BonusData(int rewardCount, long timeUntilNextReward, long timeUntilResetReward) {
        this.setRewardCount(rewardCount);
        this.setTimeUntilNextReward(timeUntilNextReward);
        this.setTimeUntilResetReward(timeUntilResetReward);
    }

    public int getRewardCount() {
        return rewardCount;
    }

    public void setRewardCount(int rewardCount) {
        this.rewardCount = rewardCount;
    }

    public long getTimeUntilNextReward() {
        return timeUntilNextReward;
    }

    public void setTimeUntilNextReward(long timeUntilNextReward) {
        this.timeUntilNextReward = timeUntilNextReward;
    }

    public long getTimeUntilResetReward() {
        return timeUntilResetReward;
    }

    public void setTimeUntilResetReward(long timeUntilResetReward) {
        this.timeUntilResetReward = timeUntilResetReward;
    }

    public boolean isReady() {
        long now = System.currentTimeMillis();
        long reset = this.getTimeUntilResetReward();

        return now >= this.getTimeUntilNextReward() && (reset <= 0 || now < reset);
    }

    public boolean isExpired() {
        return this.getTimeUntilResetReward() > 0 && System.currentTimeMillis() >= this.getTimeUntilResetReward();
    }

    public void validate() {
        if (this.isExpired()) {
            this.reset();
        }
    }

    public void reset() {
        this.rewardCount = 0;
        this.timeUntilNextReward = 0;
        this.timeUntilResetReward = 0;
    }
}
