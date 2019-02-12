package com.creditsaison.accesscontrol.Role;

import com.creditsaison.accesscontrol.User.UserRepository;
import com.creditsaison.accesscontrol.Utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RoleController {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/roles")
    public Page<Role> getAllRoles(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    @PostMapping("/roles")
    public Role createRole(@Valid @RequestBody Role role) {
        System.out.println("Came to add a new role.");
        return roleRepository.save(role);
    }

    @PutMapping("/roles/{roleId}")
    public Role updateRole(@PathVariable Long roleID,
                           @Valid @RequestBody Role roleRequest) {
        return roleRepository.findById(roleID)
                .map(role -> {
                    role.setName(roleRequest.getName());
                    return roleRepository.save(role);
                }).orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + roleID));
    }

    @DeleteMapping("/roles/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable Long roleId) {
        return roleRepository.findById(roleId)
                .map(role -> {
                    roleRepository.delete(role);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + roleId));
    }

    @GetMapping("/users/{userId}/roles")
    public List<Role> getRoleByUserId(@PathVariable Long userId) {
        return roleRepository.findByUsersId(userId);
    }

    @PostMapping("/users/{userId}/roles")
    public Role addRoleToUser(@PathVariable Long userId,
                            @Valid @RequestBody Role role) {

        return userRepository.findById(userId)
                .map(user -> {
                    role.addUserToRole(user);
                    return roleRepository.save(role);
                }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    @PutMapping("/users/{userId}/roles/{roleId}")
    public Role updateRoleWithUser(@PathVariable Long userId,
                               @PathVariable Long roleId,
                               @Valid @RequestBody Role roleRequest) {
        if(!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }

        return roleRepository.findById(roleId)
                .map(role -> {
                    role.setName(roleRequest.getName());
                    return roleRepository.save(role);
                }).orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + roleId));
    }

    @DeleteMapping("/users/{userId}/roles/{roleId}")
    public ResponseEntity<?> deleteRoleWithUser(@PathVariable Long userId,
                                          @PathVariable Long roleId) {
        if(!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }

        return roleRepository.findById(roleId)
                .map(role -> {
                    roleRepository.delete(role);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + roleId));

    }

}
