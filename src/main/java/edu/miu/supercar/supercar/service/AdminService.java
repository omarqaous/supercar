package edu.miu.supercar.supercar.service;

import java.util.List;
import java.util.Optional;
import edu.miu.supercar.supercar.model.Admin;
import edu.miu.supercar.supercar.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public Admin createAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Optional<Admin> getAdminById(Long id) {
        return adminRepository.findById(id);
    }

    public Admin getAdminByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    public Admin updateAdmin(Long id, Admin updatedAdmin) {
        Optional<Admin> existingAdminOptional = adminRepository.findById(id);

        if (existingAdminOptional.isPresent()) {
            Admin existingAdmin = existingAdminOptional.get();
            existingAdmin.setUsername(updatedAdmin.getUsername());
            existingAdmin.setPassword(updatedAdmin.getPassword());
            existingAdmin.setEmail(updatedAdmin.getEmail());
            return adminRepository.save(existingAdmin);
        } else {
            throw new RuntimeException("Admin not found with id: " + id);
        }
    }

    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }
}