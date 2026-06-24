package ru.otus;

public class OutputData {
    private String status;
    private String message;
    private int total_sum;
    private boolean discount_applied;

    public OutputData(String status, String message, int total_sum, boolean discount_applied) {
        this.status = status;
        this.message = message;
        this.total_sum = total_sum;
        this.discount_applied = discount_applied;
    }

    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public int getTotal_sum() { return total_sum; }
    public boolean isDiscount_applied() { return discount_applied; }
}
