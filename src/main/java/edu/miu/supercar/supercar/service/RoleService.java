package edu.miu.supercar.supercar.service;

import java.util.List;
import java.util.Optional;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.miu.supercar.supercar.model.Role;
import edu.miu.supercar.supercar.repository.RoleRepository;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public Role updateRole(Role role) {
        Optional<Role> existingRoleOptional = roleRepository.findById(role.getId());
        if (existingRoleOptional.isPresent()) {
            // Update existing role if found
            Role existingRole = existingRoleOptional.get();
            existingRole.setName(role.getName()); // Update other fields as needed
            return roleRepository.save(existingRole);
        } else {
            // Throw an exception or handle the case where the role to update is not found
            throw new RuntimeException("Role not found with id: " + role.getId());
        }
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
