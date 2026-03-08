package academia_dev;

public class PremiumPlan implements SubscriptionPlan {
    @Override
    public boolean canEnroll(int currentActiveEnrollments) {
        return true;
    }
}