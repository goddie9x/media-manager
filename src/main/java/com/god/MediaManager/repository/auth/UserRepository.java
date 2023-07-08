package com.god.MediaManager.repository.auth;

import com.god.MediaManager.model.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username) throws UsernameNotFoundException;
    List<User> findAllByFullName(String fullName)throws UsernameNotFoundException;
}
