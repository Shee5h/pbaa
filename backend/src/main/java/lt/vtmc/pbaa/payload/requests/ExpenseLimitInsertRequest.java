package lt.vtmc.pbaa.payload.requests;

import javax.validation.constraints.NotBlank;

public class ExpenseLimitInsertRequest {

    @NotBlank
    private Long categoryId;

    @NotBlank
    private String limit;

    public ExpenseLimitInsertRequest() {
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getLimit() {
        return limit;
    }

    @Override
    public String toString() {
        return "ExpenseInsertRequest{" +
                ", categoryId='" + categoryId + '\'' +
                ", limit='" + limit + '\'' +
                '}';
    }
}
