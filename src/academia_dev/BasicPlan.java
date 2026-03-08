package academia_dev;

public class BasicPlan implements SubscriptionPlan {
    @Override
    public boolean canEnroll(int currentActiveEnrollments) {
        return currentActiveEnrollments < 3;
    }
}