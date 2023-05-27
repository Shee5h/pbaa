package lt.vtmc.pbaa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import lt.vtmc.pbaa.models.Income;
import lt.vtmc.pbaa.models.User;

@CrossOrigin
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	Boolean existsByEmail(String email);

	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);
	
	void deleteById(Long id);
	
	Optional<User> findById(Long id);

	List<User> findAll();
	
	Page<User> findAll(Pageable pageable);

}
