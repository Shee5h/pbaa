package lt.vtmc.pbaa.services;

import lt.vtmc.pbaa.models.ExpensesCategory;
import lt.vtmc.pbaa.payload.requests.ExpensesCategoryInsertRequest;
import lt.vtmc.pbaa.payload.requests.ExpensesCategoryUpdateRequest;
import lt.vtmc.pbaa.payload.responses.ExpensesCategoryResponse;
import lt.vtmc.pbaa.repositories.ExpensesCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpensesCategoryService {
    private final ExpensesCategoryRepository expensesCategoryRepository;

    @Autowired
    public ExpensesCategoryService(ExpensesCategoryRepository expensesCategoryRepository) {
        this.expensesCategoryRepository = expensesCategoryRepository;
    }

    public List<ExpensesCategory> getAllExpensesCategories() {
        return expensesCategoryRepository.findAll();
    }

    public ExpensesCategoryResponse deleteExpensesCategory(Long id) {
        Optional<ExpensesCategory> deletingExpensesCategory = expensesCategoryRepository.findById(id);
        if (deletingExpensesCategory.isEmpty()) {
            throw new RuntimeException("Category does not exist");
        }
        expensesCategoryRepository.deleteById(id);
        return null;
    }

    public ExpensesCategoryResponse saveExpensesCategory(ExpensesCategoryInsertRequest expensesCategoryInsertRequest) {
        String categoryName = expensesCategoryInsertRequest.getName();
        String expenseCategoryName = categoryName.substring(0, 1).toUpperCase() + categoryName.substring(1);
        Optional<ExpensesCategory> expensesCategory = expensesCategoryRepository.findByName(expenseCategoryName);
        if (expensesCategory.isPresent()) {
            throw new RuntimeException("Category already exists");
        }
        ExpensesCategory newExpensesCategory = new ExpensesCategory(expenseCategoryName);
        expensesCategoryRepository.save(newExpensesCategory);
        return new ExpensesCategoryResponse(newExpensesCategory.getId(), newExpensesCategory.getName());
    }

    public ExpensesCategoryResponse updateExpensesCategory(ExpensesCategoryUpdateRequest expensesCategoryUpdateRequest) {
        ExpensesCategory expensesCategory = expensesCategoryRepository.getById(expensesCategoryUpdateRequest.getId());
        if (expensesCategory == null) {
            throw new RuntimeException("Category does not exist");
        }
        String categoryName = expensesCategoryUpdateRequest.getName();
        String expenseCategoryName = categoryName.substring(0, 1).toUpperCase() + categoryName.substring(1);
        Optional<ExpensesCategory> expensesCategory2 = expensesCategoryRepository.findByName(expenseCategoryName);
        if (expensesCategory2.isPresent()) {
            throw new RuntimeException("Category already exists");
        }
        expensesCategory.setName(expenseCategoryName);
        expensesCategoryRepository.save(expensesCategory);
        return new ExpensesCategoryResponse(expensesCategory.getId(), expensesCategory.getName());
    }

}
