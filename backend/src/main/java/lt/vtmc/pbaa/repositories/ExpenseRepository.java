package lt.vtmc.pbaa.repositories;

import lt.vtmc.pbaa.models.Expense;
import lt.vtmc.pbaa.models.ExpensesCategory;
import lt.vtmc.pbaa.models.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUser(Optional<User> user);

	Page<Expense> findByUserAndDateBetween(Optional<User> user, LocalDate start,LocalDate end, Pageable pageable);
	Page<Expense> findByUserAndDateBetweenAndExpensesCategory(Optional<User> user, LocalDate start,LocalDate end, Optional<ExpensesCategory> category, Pageable pageable);
	Page<Expense> findByUserAndExpensesCategory(Optional<User> user, Optional<ExpensesCategory> category, Pageable pageable);
}
