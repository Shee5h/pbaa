package lt.vtmc.pbaa.services;

import lt.vtmc.pbaa.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final ExpenseService expenseService;
    private final ExpenseLimitService expenseLimitService;
    private final IncomeService incomeService;
    private final ExpensesCategoryService expensesCategoryService;

    @Autowired
    public StatisticsService(ExpenseService expenseService, ExpenseLimitService expenseLimitService, IncomeService incomeService, ExpensesCategoryService expensesCategoryService) {
        this.expenseService = expenseService;
        this.expenseLimitService = expenseLimitService;
        this.incomeService = incomeService;
        this.expensesCategoryService = expensesCategoryService;
    }


    public List<ExpenseUnitStatistic> getAllExpenseStatisticByUser(Long id) {
        List<Expense> allExpenses = expenseService.getAllExpenseByUser(id);
        List<Expense> thisMonthExpenses = allExpenses.stream().filter(expense -> !expense.getDate().isBefore(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1))).collect(Collectors.toList());

        List<ExpenseLimit> userLimits = expenseLimitService.getAllExpenseLimitsByUser(id);
        List<ExpensesCategory> allExpensesCategories = expensesCategoryService.getAllExpensesCategories();
        List<ExpenseUnitStatistic> expenseUnitStatisticList = new ArrayList<>();
        for (ExpensesCategory category : allExpensesCategories) {
            BigDecimal sumOfExpenseCategory = thisMonthExpenses.stream().filter(expense -> expense.getExpensesCategory().equals(category)).map(expense -> expense.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
            Optional<ExpenseLimit> searchingLimit = userLimits.stream().filter(expenseLimit -> expenseLimit.getExpensesCategory().equals(category)).findFirst();
            BigDecimal limitAmount = BigDecimal.ZERO;
            if (searchingLimit.isPresent()) {
                limitAmount = searchingLimit.get().getAmount();
            }
             expenseUnitStatisticList.add(new ExpenseUnitStatistic(category, sumOfExpenseCategory, limitAmount));
        }
    return expenseUnitStatisticList;
    }

    public Map<String, BigDecimal> getAllIncomeStatisticByUser(Long id) {
        List<Income> allIncomes = incomeService.getAllIncomeByUser(id);
        List<Income> thisMonthIncomes = allIncomes.stream().filter(expense -> !expense.getDate().isBefore(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1))).collect(Collectors.toList());
        return thisMonthIncomes.stream().collect(
                Collectors.toMap(
                        income -> income.getIncomeName(),
                        income -> income.getAmount(),
                        BigDecimal::add
                )
        );
    }

}
