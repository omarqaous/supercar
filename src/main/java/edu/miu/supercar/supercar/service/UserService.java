package edu.miu.supercar.supercar.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.miu.supercar.supercar.model.Role;
import edu.miu.supercar.supercar.model.User;
import edu.miu.supercar.supercar.repository.RoleRepository;
import edu.miu.supercar.supercar.repository.UserRepository;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        Optional<User> existingUserOptional = userRepository.findById(user.getId());
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            // Update relevant fields (e.g., username, password, email)
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(user.getPassword()); // Consider password hashing
            existingUser.setEmail(user.getEmail());
            existingUser.setRoles(user.getRoles()); // Assuming roles are already set in the user object
            existingUser.setBookings(user.getBookings());
            return userRepository.save(existingUser);
        } else {
            throw new RuntimeException("User not found with id: " + user.getId());
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Method to add a role to a user
    public User addRoleToUser(Long userId, Long roleId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<Role> roleOptional = roleRepository.findById(roleId);
            if (roleOptional.isPresent()) {
                user.getRoles().add(roleOptional.get());
                return userRepository.save(user);
            } else {
                throw new RuntimeException("Role not found with id: " + roleId);
            }
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }

    public User removeRoleFromUser(Long userId, Long roleId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<Role> roleOptional = roleRepository.findById(roleId);
            if (roleOptional.isPresent()) {
                Set<Role> userRoles = user.getRoles();
                userRoles.remove(roleOptional.get());
                user.setRoles(userRoles);
                return userRepository.save(user);
            } else {
                throw new RuntimeException("Role not found with id: " + roleId);
            }
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }
}