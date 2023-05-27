package lt.vtmc.pbaa.repositories;

import lt.vtmc.pbaa.models.Income;
import lt.vtmc.pbaa.models.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

	List<Income> findByUser(Optional<User> user);
	Page<Income> findByUser(Optional<User> user, Pageable pageable);
	List<Income> findByUser(Optional<User> user, Sort by);
	Page<Income> findByUserAndDateBetween(Optional<User> user, LocalDate start,LocalDate end, Pageable pageable);
	List<Income> findByUserAndDateBetween(Optional<User> user, LocalDate start,LocalDate end);
}
