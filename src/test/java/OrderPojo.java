import java.util.List;
public class OrderPojo {
    private boolean success;
    private List<OrderPojoOrders> orders;
    private int total;
    private int totalToday;

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<OrderPojoOrders> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderPojoOrders> orders) {
        this.orders = orders;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalToday() {
        return totalToday;
    }

    public void setTotalToday(int totalToday) {
        this.totalToday = totalToday;
    }
}
