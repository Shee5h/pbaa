package lt.vtmc.pbaa.services;

import lt.vtmc.pbaa.models.Expense;
import lt.vtmc.pbaa.models.ExpenseLimit;
import lt.vtmc.pbaa.models.ExpensesCategory;
import lt.vtmc.pbaa.models.User;
import lt.vtmc.pbaa.payload.requests.ExpenseLimitInsertRequest;
import lt.vtmc.pbaa.payload.requests.ExpenseLimitUpdateRequest;
import lt.vtmc.pbaa.payload.requests.ExpenseUpdateRequest;
import lt.vtmc.pbaa.payload.responses.ExpenseLimitResponse;
import lt.vtmc.pbaa.payload.responses.ExpenseResponse;
import lt.vtmc.pbaa.repositories.ExpensesCategoryRepository;
import lt.vtmc.pbaa.repositories.ExpensesLimitsRepository;
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
public class ExpenseLimitService {
    private final ExpensesLimitsRepository expensesLimitsRepository;
    private final UserRepository userRepository;
    private final ExpensesCategoryRepository expensesCategoryRepository;

    @Autowired
    public ExpenseLimitService(ExpensesLimitsRepository expensesLimitsRepository, UserRepository userRepository, ExpensesCategoryRepository expensesCategoryRepository) {
        this.expensesLimitsRepository = expensesLimitsRepository;
        this.userRepository = userRepository;
        this.expensesCategoryRepository = expensesCategoryRepository;
    }

    public ExpenseLimitResponse saveExpenseLimit(ExpenseLimitInsertRequest expenseLimitInsertRequest) {
        ExpensesCategory expenseCategoryForNewInsertingLimit = expensesCategoryRepository.getById(expenseLimitInsertRequest.getCategoryId());
        String currentPrincipalEmail = getCurrentPrincipalEmail();
        Optional<User> user = userRepository.findByEmail(currentPrincipalEmail);
        List<ExpenseLimit> expenseLimitList = expensesLimitsRepository.findByUser(user);

        Optional<ExpenseLimit> existingLimit = expenseLimitList.stream().filter(expenseLimit -> expenseLimit.getExpensesCategory().equals(expenseCategoryForNewInsertingLimit)).findFirst();
        if (existingLimit.isPresent()) {
            throw new RuntimeException("Limit with this expense category already exists");
        }
        ExpenseLimit expenseLimit = new ExpenseLimit(
                user.get(),
                expenseCategoryForNewInsertingLimit,
                BigDecimal.valueOf(Double.parseDouble(expenseLimitInsertRequest.getLimit())));
        expensesLimitsRepository.save(expenseLimit);
        return new ExpenseLimitResponse(
                expenseLimit.getId(),
                expenseLimitInsertRequest.getCategoryId(),
                expenseLimitInsertRequest.getLimit());
    }

    public ExpenseLimitResponse updateExpenseLimit(ExpenseLimitUpdateRequest expenseLimitUpdateRequest) {
        String currentPrincipalEmail = getCurrentPrincipalEmail();
        Optional<User> user = userRepository.findByEmail(currentPrincipalEmail);
        if (user.isEmpty()) {
            throw new RuntimeException("User does not exist");
        }
        List<ExpenseLimit> expenseLimitList = getAllExpenseLimitsByUser(user.get().getId());
        ExpenseLimit updatingExpenseLimit = expensesLimitsRepository.getById(expenseLimitUpdateRequest.getId());
        if (!expenseLimitList.contains(updatingExpenseLimit)) {
            throw new RuntimeException("User has not this expense limit");
        }
        ExpensesCategory expenseCategoryForUpdatingLimit = expensesCategoryRepository.getById(expenseLimitUpdateRequest.getCategoryId());
        Optional<ExpenseLimit> existingLimit = expenseLimitList.stream().filter(expenseLimit -> expenseLimit.getExpensesCategory().equals(expenseCategoryForUpdatingLimit)).findFirst();
        if (existingLimit.isPresent()) {
            if (!updatingExpenseLimit.getExpensesCategory().getId().equals(expenseCategoryForUpdatingLimit.getId())) {
                throw new RuntimeException("Limit with this expense category already exists");
            }
        }
        updatingExpenseLimit.setExpensesCategory(expenseCategoryForUpdatingLimit);
        updatingExpenseLimit.setAmount(BigDecimal.valueOf(Double.parseDouble(expenseLimitUpdateRequest.getLimit())));
        expensesLimitsRepository.save(updatingExpenseLimit);
        return new ExpenseLimitResponse(
                updatingExpenseLimit.getId(),
                expenseLimitUpdateRequest.getCategoryId(),
                expenseLimitUpdateRequest.getLimit());
    }

    public ExpenseLimitResponse deleteExpenseLimit(Long id) {
        String currentPrincipalEmail = getCurrentPrincipalEmail();
        Optional<User> user = userRepository.findByEmail(currentPrincipalEmail);
        if (user.isEmpty()) {
            throw new RuntimeException("User does not exist");
        }
        List<ExpenseLimit> userExpenses = getAllExpenseLimitsByUser(user.get().getId());
        ExpenseLimit deletingExpenseLimit = expensesLimitsRepository.getById(id);
        if (!userExpenses.contains(deletingExpenseLimit)) {
            throw new RuntimeException("User has not this expense limit");
        }
        expensesLimitsRepository.delete(deletingExpenseLimit);
        return null;
    }

    private String getCurrentPrincipalEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public List<ExpenseLimit> getAllExpenseLimitsByUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        return expensesLimitsRepository.findByUser(user);
    }

    public Page<ExpenseLimit> getAllExpenseLimitsByUserPage(int offset, int pageSize){
        String currentPrincipalEmail = getCurrentPrincipalEmail();
        User user1 = userRepository.findByEmail(currentPrincipalEmail).orElse(null);
        Optional<User> user = Optional.of(user1);
        List<ExpenseLimit> expenseLimitslist = expensesLimitsRepository.findByUser(user);

        Collections.sort(expenseLimitslist, new Comparator() {

            public int compare(Object o1, Object o2) {
                Long y1 = ((ExpenseLimit) o1).getId();
                Long y2 = ((ExpenseLimit) o2).getId();
                return y2.compareTo(y1);
            }});
        Pageable pageable = PageRequest.of(offset, pageSize);
        final int startp = (int)pageable.getOffset();
        final int endp = Math.min((startp + pageable.getPageSize()), expenseLimitslist.size());
        final Page<ExpenseLimit> page = new PageImpl<>(expenseLimitslist.subList(startp, endp), pageable, expenseLimitslist.size());

        return page;
    }
}
