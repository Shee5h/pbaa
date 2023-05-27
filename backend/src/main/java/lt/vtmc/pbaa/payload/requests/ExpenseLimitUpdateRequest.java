package lt.vtmc.pbaa.payload.requests;

import javax.validation.constraints.NotBlank;

public class ExpenseLimitUpdateRequest {

    @NotBlank
    private Long id;

    @NotBlank
    private Long categoryId;

    @NotBlank
    private String limit;

    public ExpenseLimitUpdateRequest() {
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getLimit() {
        return limit;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ExpenseLimitUpdateRequest{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", limit='" + limit + '\'' +
                '}';
    }
}
