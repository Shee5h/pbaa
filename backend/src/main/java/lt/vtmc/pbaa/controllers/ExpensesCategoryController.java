package lt.vtmc.pbaa.controllers;

import lt.vtmc.pbaa.models.ExpensesCategory;
import lt.vtmc.pbaa.payload.requests.ExpensesCategoryInsertRequest;
import lt.vtmc.pbaa.payload.requests.ExpensesCategoryUpdateRequest;
import lt.vtmc.pbaa.payload.responses.ExpensesCategoryResponse;
import lt.vtmc.pbaa.services.ExpensesCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/categories")
public class ExpensesCategoryController {

    private final ExpensesCategoryService expensesCategoryService;

    @Autowired
    public ExpensesCategoryController(ExpensesCategoryService expensesCategoryService) {
        this.expensesCategoryService = expensesCategoryService;
    }

    @PostMapping
//    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ExpensesCategoryResponse insertExpensesCategory(@RequestBody ExpensesCategoryInsertRequest expensesCategoryInsertRequest) {
        return this.expensesCategoryService.saveExpensesCategory(expensesCategoryInsertRequest);
    }

    @PutMapping
//    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ExpensesCategoryResponse updateExpensesCategory(@RequestBody ExpensesCategoryUpdateRequest expensesCategoryUpdateRequest) {
        return this.expensesCategoryService.updateExpensesCategory(expensesCategoryUpdateRequest);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ExpensesCategoryResponse deleteExpensesCategory(@PathVariable Long id) {
        return this.expensesCategoryService.deleteExpensesCategory(id);
    }

    @GetMapping
    public ResponseEntity<List<ExpensesCategory>> getAllExpensesCategories() {
        return ResponseEntity.ok().body(this.expensesCategoryService.getAllExpensesCategories());
    }


}
