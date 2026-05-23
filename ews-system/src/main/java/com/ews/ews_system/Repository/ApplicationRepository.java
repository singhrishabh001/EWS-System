package com.ews.ews_system.Repository;

import com.ews.ews_system.Model.EWSApplication;
import com.ews.ews_system.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<EWSApplication,Long> {
    List<EWSApplication> findByStatus(String status);
    List<EWSApplication> findByUser(User user);
}
