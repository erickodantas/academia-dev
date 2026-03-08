package academia_dev;

public interface SubscriptionPlan {
    boolean canEnroll(int currentActiveEnrollments);
}