package lt.vtmc.pbaa.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lt.vtmc.pbaa.models.ERole;
import lt.vtmc.pbaa.models.Income;
import lt.vtmc.pbaa.models.Role;
import lt.vtmc.pbaa.models.User;
import lt.vtmc.pbaa.payload.requests.SignupRequest;
import lt.vtmc.pbaa.payload.responses.MessageResponse;
import lt.vtmc.pbaa.repositories.RoleRepository;
import lt.vtmc.pbaa.repositories.UserRepository;
import lt.vtmc.pbaa.services.UserService;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

	private final UserService userService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	PasswordEncoder encoder;
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/{email}")
	public ResponseEntity<Optional<User>> getUserByEmail(@PathVariable String email) {
		return ResponseEntity.ok().body(userService.getUserByEmail(email));
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ADMIN')")
	public void deleteUser(@PathVariable Long id) {
		userService.deleteUserById(id);
	}
	
	@PutMapping("/{id}")
//	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ADMIN')")
	public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @Valid @RequestBody SignupRequest signUpRequest) {
//		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//			return ResponseEntity
//					.badRequest()
//					.body(new MessageResponse("Error: Email is already in use!"));
//		}
		
		User user = userRepository.findById(id).orElseThrow();
	
//		Set<Role> roles = new HashSet<>();
//		roles.add((Role) signUpRequest.getRole());
		
		user.setUsername(signUpRequest.getUsername());
		user.setEmail(signUpRequest.getEmail());
		
		if (signUpRequest.getPassword() != null) {
			user.setPassword(encoder.encode(signUpRequest.getPassword()));
		}
		
		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();
		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);
					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);
					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<User>> getAllUsers() {
		return ResponseEntity.ok().body(this.userRepository.findAll());
	}
	@GetMapping("/allPage")
	public ResponseEntity<Page<User>> getAllUsersPage(@RequestParam("offset") int offset, @RequestParam("pageSize") int pageSize) {
		return ResponseEntity.ok().body(this.userRepository.findAll(PageRequest.of(offset, pageSize)));
	}
}
