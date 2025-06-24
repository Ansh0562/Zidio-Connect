package com.zidioproject.repository;

import com.zidioproject.entity.Adminprofile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdminProfileRepository extends JpaRepository<Adminprofile, Long> {
    Optional<Adminprofile> findByUser_Email(String email);
    Optional<Adminprofile> findByUser_Username(String username);
}