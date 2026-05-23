package com.ews.ews_system.Service;

import com.ews.ews_system.Model.Admin;
import com.ews.ews_system.Model.EWSApplication;
import com.ews.ews_system.Repository.AdminRepository;
import com.ews.ews_system.Repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ApplicationRepository applicationRepository;

    public Admin login(String email , String password){
        Admin admin = adminRepository.findByEmail(email);
        if (admin != null && admin.getPassword().equals(password)){
            return admin;
        }
        return null;
    }

    public List<EWSApplication> getApprodeApplication(){
        return applicationRepository.findByStatus("Approved");
    }

    public List<EWSApplication> getRejectApplication(){
        return applicationRepository.findByStatus("Rejected");
    }

}
