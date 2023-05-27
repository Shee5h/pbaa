package lt.vtmc.pbaa.repositories;

import lt.vtmc.pbaa.models.ExpenseLimit;
import lt.vtmc.pbaa.models.ExpensesCategory;
import lt.vtmc.pbaa.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpensesLimitsRepository extends JpaRepository<ExpenseLimit, Long> {

    List<ExpenseLimit> findByUser(Optional<User> user);

}
