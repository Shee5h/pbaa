package lt.vtmc.pbaa.controllers;

import lt.vtmc.pbaa.models.Income;
import lt.vtmc.pbaa.payload.requests.IncomeInsertRequest;
import lt.vtmc.pbaa.payload.requests.IncomeUpdateRequest;
import lt.vtmc.pbaa.payload.responses.IncomeResponse;
import lt.vtmc.pbaa.services.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/income")
public class IncomeController {
	
	private final IncomeService incomeService;

	@Autowired
	public IncomeController(IncomeService incomeService) {
		this.incomeService = incomeService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public IncomeResponse insertIncome(@RequestBody IncomeInsertRequest incomeInsertRequest) {
		return this.incomeService.saveIncome(incomeInsertRequest);
	}

	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public IncomeResponse updateIncome(@RequestBody IncomeUpdateRequest incomeUpdateRequest) {
		return this.incomeService.updateIncome(incomeUpdateRequest);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public IncomeResponse deleteIncome(@PathVariable String id) {
		return this.incomeService.deleteIncome(id);
	}

	@GetMapping("/user/{id}")
	public ResponseEntity<List<Income>> getAllIncomeByUserId(@PathVariable Long id) {
		return ResponseEntity.ok().body(this.incomeService.getAllIncomeByUser(id));
	}
	
	@GetMapping("/user")// /api/income/user?offset=0&pageSize=3 to test
	public ResponseEntity<Page<Income>> getAllIncomeByUserIdPage(@RequestParam("offset") int offset, @RequestParam("pageSize") int pageSize) {
		return ResponseEntity.ok().body(this.incomeService.getIncomeWithPagination(offset, pageSize));
	}

	@GetMapping("/userDate")// /api/income/userDate?field=date to test
	public ResponseEntity<List<Income>> getAllIncomeByUserIdMonth(@RequestParam("date") String date) {
		return ResponseEntity.ok().body(this.incomeService.findByUserByDate(date));
	}
	@GetMapping("/userDate2")// /api/income/userDate2?field=date to test
	public ResponseEntity<Page<Income>> getAllIncomeByUserIdAndMonth(@RequestParam("date") String date, @RequestParam("offset") int offset, @RequestParam("pageSize") int pageSize) {
		return ResponseEntity.ok().body(this.incomeService.findByUserAndDate(date, offset, pageSize));
	}
	
}
