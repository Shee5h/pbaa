package lt.vtmc.pbaa.payload.responses;

public class ExpenseLimitResponse {

    private Long limitId;

    private Long categoryId;

    private String limit;

    public ExpenseLimitResponse() {
    }

    public ExpenseLimitResponse(Long limitId, Long categoryId, String limit) {
        this.limitId = limitId;
        this.categoryId = categoryId;
        this.limit = limit;
    }

    public Long getLimitId() {
        return limitId;
    }

    public void setLimitId(Long limitId) {
        this.limitId = limitId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "ExpenseResponse{" +
                "expenseId='" + limitId + '\'' +
                ", expenseCategory='" + categoryId + '\'' +
                ", limit='" + limit + '\'' +
                '}';
    }
}
