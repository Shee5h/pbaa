package lt.vtmc.pbaa.controllers;

import lt.vtmc.pbaa.models.ExpenseLimit;
import lt.vtmc.pbaa.payload.requests.ExpenseLimitInsertRequest;
import lt.vtmc.pbaa.payload.requests.ExpenseLimitUpdateRequest;
import lt.vtmc.pbaa.payload.requests.ExpenseUpdateRequest;
import lt.vtmc.pbaa.payload.responses.ExpenseLimitResponse;
import lt.vtmc.pbaa.payload.responses.ExpenseResponse;
import lt.vtmc.pbaa.services.ExpenseLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/limits")
public class ExpensesLimitsController {

    private final ExpenseLimitService expenseLimitService;

    @Autowired
    public ExpensesLimitsController(ExpenseLimitService expenseLimitService ) {
        this.expenseLimitService = expenseLimitService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseLimitResponse insertExpenseLimit(@RequestBody ExpenseLimitInsertRequest expenseLimitInsertRequest) {
        return this.expenseLimitService.saveExpenseLimit(expenseLimitInsertRequest);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ExpenseLimitResponse updateExpenseLimit(@RequestBody ExpenseLimitUpdateRequest expenseLimitUpdateRequest) {
        return this.expenseLimitService.updateExpenseLimit(expenseLimitUpdateRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ExpenseLimitResponse deleteExpenseLimit(@PathVariable Long id) {
        return this.expenseLimitService.deleteExpenseLimit(id);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<ExpenseLimit>> getAllExpensesLimitsByUserId(@PathVariable Long id) {
        return ResponseEntity.ok().body(this.expenseLimitService.getAllExpenseLimitsByUser(id));
    }

    @GetMapping("/user")
    public ResponseEntity<Page<ExpenseLimit>> getAllExpensesLimitsByUserIdPage(@RequestParam("offset") int offset, @RequestParam("pageSize") int pageSize) {
        return ResponseEntity.ok().body(this.expenseLimitService.getAllExpenseLimitsByUserPage(offset, pageSize));
    }
}
