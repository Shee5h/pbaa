package lt.vtmc.pbaa.services;


import java.util.Optional;
import org.springframework.stereotype.Service;

import lt.vtmc.pbaa.models.User;
import lt.vtmc.pbaa.repositories.UserRepository;


@Service
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public void deleteUserById(Long id) {
		userRepository.deleteById(id);
	}
	
	
}
