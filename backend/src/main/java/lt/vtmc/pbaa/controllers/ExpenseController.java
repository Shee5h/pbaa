package lt.vtmc.pbaa.controllers;

import lt.vtmc.pbaa.models.Expense;
import lt.vtmc.pbaa.models.Income;
import lt.vtmc.pbaa.payload.requests.ExpenseInsertRequest;
import lt.vtmc.pbaa.payload.requests.ExpenseUpdateRequest;
import lt.vtmc.pbaa.payload.responses.ExpenseResponse;
import lt.vtmc.pbaa.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/expense")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService ) {
        this.expenseService = expenseService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseResponse insertExpense(@RequestBody ExpenseInsertRequest expenseInsertRequest) {
        return this.expenseService.saveExpense(expenseInsertRequest);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ExpenseResponse updateExpense(@RequestBody ExpenseUpdateRequest expenseUpdateRequest) {
        return this.expenseService.updateExpense(expenseUpdateRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ExpenseResponse deleteExpense(@PathVariable Long id) {
        return this.expenseService.deleteExpense(id);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Expense>> getAllExpensesByUserId(@PathVariable Long id) {
        return ResponseEntity.ok().body(this.expenseService.getAllExpenseByUser(id));
    }
    @GetMapping("/user")// /api/expense/user?offset=0&pageSize=3 to test
	public ResponseEntity<Page<Expense>> getAllIncomeByUserIdPage(@RequestParam("offset") int offset, @RequestParam("pageSize") int pageSize) {
		return ResponseEntity.ok().body(this.expenseService.getAllExpenseByUserPage(offset, pageSize));
	}
	@GetMapping("/userDate")// /api/expense/userDate2?date1=date1&date2=date2&category=category&offset=0&pageSize=3  to test
	public ResponseEntity<Page<Expense>> getAllExpenseByUserIdAndMonth(@RequestParam("date1") String date1, @RequestParam("date2") String date2,  @RequestParam("category") String category, @RequestParam("offset") int offset, @RequestParam("pageSize") int pageSize) {
		return ResponseEntity.ok().body(this.expenseService.findByUserAndDateAndExpensesCategory(date1, date2, category, offset, pageSize));
	}
	@GetMapping("/userCategory")// /api/expense/userDate2?date1=date1&date2=date2&category=category&offset=0&pageSize=3  to test
	public ResponseEntity<Page<Expense>> getAllExpenseByUserIdAndMonth(String category, @RequestParam("offset") int offset, @RequestParam("pageSize") int pageSize) {
		return ResponseEntity.ok().body(this.expenseService.findByUserAndExpensesCategory(category, offset, pageSize));
	}
}
