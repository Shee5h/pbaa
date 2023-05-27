package lt.vtmc.pbaa.services;

import lt.vtmc.pbaa.models.Expense;
import lt.vtmc.pbaa.models.ExpensesCategory;
import lt.vtmc.pbaa.models.Income;
import lt.vtmc.pbaa.models.User;
import lt.vtmc.pbaa.payload.requests.ExpenseInsertRequest;
import lt.vtmc.pbaa.payload.requests.ExpenseUpdateRequest;
import lt.vtmc.pbaa.payload.responses.ExpenseResponse;
import lt.vtmc.pbaa.repositories.ExpenseRepository;
import lt.vtmc.pbaa.repositories.ExpensesCategoryRepository;
import lt.vtmc.pbaa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final ExpensesCategoryRepository expensesCategoryRepository;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository, ExpensesCategoryRepository expensesCategoryRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.expensesCategoryRepository = expensesCategoryRepository;
    }

    public ExpenseResponse saveExpense(ExpenseInsertRequest expenseInsertRequest) {
        String currentPrincipalEmail = getCurrentPrincipalEmail();
        User user = userRepository.findByEmail(currentPrincipalEmail).orElse(null);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ExpensesCategory expenseCategory = expensesCategoryRepository.getById(expenseInsertRequest.getCategoryId());
        String expenseName = expenseInsertRequest.getExpenseName();
        Expense expense = new Expense(
                user,
                expenseCategory,
                expenseName.substring(0, 1).toUpperCase() + expenseName.substring(1),
                LocalDate.parse(expenseInsertRequest.getDate(), formatter),
                BigDecimal.valueOf(Double.parseDouble(expenseInsertRequest.getAmount())));
        expenseRepository.save(expense);
        return new ExpenseResponse(
                expense.getId(),
                expenseInsertRequest.getExpenseName(),
                expenseInsertRequest.getCategoryId(),
                expenseInsertRequest.getDate(),
                expenseInsertRequest.getAmount());
    }

    public ExpenseResponse updateExpense(ExpenseUpdateRequest expenseUpdateRequest) {
        String currentPrincipalEmail = getCurrentPrincipalEmail();
        Optional<User> user = userRepository.findByEmail(currentPrincipalEmail);
        if (user.isEmpty()) {
            throw new RuntimeException("User does not exist");
        }
        List<Expense> userExpenses = getAllExpenseByUser(user.get().getId());
        Expense updatingExpense = expenseRepository.getById(expenseUpdateRequest.getExpenseId());
        if (!userExpenses.contains(updatingExpense)) {
            throw new RuntimeException("User has not this income");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String expenseName = expenseUpdateRequest.getExpenseName();
        String updatingExpenseName = expenseName.substring(0, 1).toUpperCase() + expenseName.substring(1);
        updatingExpense.setExpenseName(updatingExpenseName);
        updatingExpense.setExpensesCategory(expensesCategoryRepository.getById(expenseUpdateRequest.getCategoryId()));
        updatingExpense.setDate(LocalDate.parse(expenseUpdateRequest.getDate(), formatter));
        updatingExpense.setAmount(BigDecimal.valueOf(Double.parseDouble(expenseUpdateRequest.getAmount())));
        expenseRepository.save(updatingExpense);
        return new ExpenseResponse(
                updatingExpense.getId(),
                updatingExpenseName,
                expenseUpdateRequest.getCategoryId(),
                expenseUpdateRequest.getDate(),
                expenseUpdateRequest.getAmount());
    }

    public ExpenseResponse deleteExpense(Long id) {
        String currentPrincipalEmail = getCurrentPrincipalEmail();
        Optional<User> user = userRepository.findByEmail(currentPrincipalEmail);
        if (user.isEmpty()) {
            throw new RuntimeException("User does not exist");
        }
        List<Expense> userExpenses = getAllExpenseByUser(user.get().getId());
        Expense deletingExpense = expenseRepository.getById(id);
        if (!userExpenses.contains(deletingExpense)) {
            throw new RuntimeException("User has not this expense");
        }
        expenseRepository.delete(deletingExpense);
        return null;
    }

    private String getCurrentPrincipalEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public List<Expense> getAllExpenseByUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        return expenseRepository.findByUser(user);
    }
    
    public Page<Expense> getAllExpenseByUserPage(int offset, int pageSize){
    	String currentPrincipalEmail = getCurrentPrincipalEmail();
    	User user1 = userRepository.findByEmail(currentPrincipalEmail).orElse(null);
    	Optional<User> user = Optional.of(user1);
    	
    	List<Expense> expense = expenseRepository.findByUser(user);
   	 Collections.sort(expense, new Comparator() {

   	        public int compare(Object o1, Object o2) {
   	        	//comapre by date and then id in descending order
   	        	LocalDate x1 = ((Expense) o1).getDate();
   	        	LocalDate x2 = ((Expense) o2).getDate();
   	            int sComp = x2.compareTo(x1);

   	            if (sComp != 0) {
   	               return sComp;
   	            } 

   	            Long y1 = ((Expense) o1).getId();
   	            Long y2 = ((Expense) o2).getId();
   	            return y2.compareTo(y1);
   	    }});
   	 Pageable pageable = PageRequest.of(offset, pageSize);
   	 final int startp = (int)pageable.getOffset();
    	final int endp = Math.min((startp + pageable.getPageSize()), expense.size());
    	final Page<Expense> page = new PageImpl<>(expense.subList(startp, endp), pageable, expense.size());
    	
    	return page;
    }
    //
    public Page<Expense> findByUserAndDateAndExpensesCategory(String date1, String date2, String category, int offset, int pageSize){
    	String currentPrincipalEmail = getCurrentPrincipalEmail();
    	User user1 = userRepository.findByEmail(currentPrincipalEmail).orElse(null);
    	Optional<User> user = Optional.of(user1);
    	
    	
    	LocalDate start = LocalDate.parse(date1);
    	LocalDate end = LocalDate.parse(date2);
    	
    	Page<Expense> page = expenseRepository.findByUserAndDateBetweenAndExpensesCategory(user, start, end, expensesCategoryRepository.findByName(category), PageRequest.of(offset, pageSize));

    	
		return page;
	}
    public Page<Expense> findByUserAndExpensesCategory(String category, int offset, int pageSize){
    	String currentPrincipalEmail = getCurrentPrincipalEmail();
    	User user1 = userRepository.findByEmail(currentPrincipalEmail).orElse(null);
    	Optional<User> user = Optional.of(user1);
    	
    	
    	Page<Expense> page = expenseRepository.findByUserAndExpensesCategory(user, expensesCategoryRepository.findByName(category), PageRequest.of(offset, pageSize));

    	
		return page;
	}
}
