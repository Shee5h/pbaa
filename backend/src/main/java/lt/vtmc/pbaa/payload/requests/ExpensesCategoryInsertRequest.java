package lt.vtmc.pbaa.payload.requests;

import javax.validation.constraints.NotBlank;

public class ExpensesCategoryInsertRequest {

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ExpensesCategoryInsertRequest{" +
                "name='" + name + '\'' +
                '}';
    }
}
