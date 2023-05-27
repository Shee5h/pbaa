package lt.vtmc.pbaa.repositories;

import lt.vtmc.pbaa.models.ExpensesCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ExpensesCategoryRepository extends JpaRepository<ExpensesCategory, Long> {

    Optional<ExpensesCategory> findByName(String name);

}
