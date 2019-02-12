package com.creditsaison.accesscontrol.User;

import com.creditsaison.accesscontrol.Role.RoleRepository;
import com.creditsaison.accesscontrol.Utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/users")
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/users/{userId}")
    public User updateUser(@PathVariable Long userId,
                                   @Valid @RequestBody User userRequest) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setUsername(userRequest.getUsername());
                    user.setPassword(userRequest.getPassword());
                    return userRepository.save(user);
                }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    userRepository.delete(user);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }



    @GetMapping("/roles/{roleId}/users")
    public List<User> getUsersByRoleId(@PathVariable Long roleId) {
        return userRepository.findByRolesId(roleId);
    }

    @PostMapping("/roles/{roleId}/user")
    public User addUserToRole(@PathVariable Long roleId,
                        @Valid @RequestBody User user) {

        return roleRepository.findById(roleId)
                .map(role -> {
                    user.addRoleToUser(role);
                    return userRepository.save(user);
                }).orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + roleId));
    }

    @PutMapping("/roles/{roleId}/users/{userId}")
    public User updateUserWithRole(@PathVariable Long roleId,
                           @PathVariable Long userId,
                           @Valid @RequestBody User userRequest) {
        if(!roleRepository.existsById(roleId)) {
            throw new ResourceNotFoundException("Role not found with id " + roleId);
        }

        return userRepository.findById(userId)
                .map(user -> {
                    user.setUsername(userRequest.getUsername());
                    user.setPassword(userRequest.getPassword());
                    return userRepository.save(user);
                }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    @DeleteMapping("/roles/{roleId}/users/{userId}")
    public ResponseEntity<?> deleteUserWithRole(@PathVariable Long roleId,
                                        @PathVariable Long userId) {
        if(!roleRepository.existsById(roleId)) {
            throw new ResourceNotFoundException("Role not found with id " + roleId);
        }

        return userRepository.findById(userId)
                .map(user -> {
                    userRepository.delete(user);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

    }
}
