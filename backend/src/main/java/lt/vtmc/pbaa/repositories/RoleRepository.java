package lt.vtmc.pbaa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import lt.vtmc.pbaa.models.ERole;
import lt.vtmc.pbaa.models.Role;

@CrossOrigin
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(ERole name);
}
