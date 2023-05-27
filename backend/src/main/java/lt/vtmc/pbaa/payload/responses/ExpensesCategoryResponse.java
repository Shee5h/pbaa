package lt.vtmc.pbaa.payload.responses;

public class ExpensesCategoryResponse {

    private Long id;

    private String name;

    public ExpensesCategoryResponse() {
    }

    public ExpensesCategoryResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ExpensesCategoryResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
