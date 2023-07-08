package com.god.MediaManager.model.auth;

import com.god.MediaManager.Enum.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Users")
public class User implements UserDetails, CredentialsContainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String fullName;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String phoneNumber;

    @Column(columnDefinition = "boolean default false")
    private boolean isAdmin;

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (isAdmin) {
            authorities.add(() -> Role.ROLE_ADMIN.name());
        } else {
            authorities.add(() -> Role.ROLE_CUSTOMER.name());
        }
        return authorities;
    }

    @Override
    public void eraseCredentials() {

    }

    public void importUserData(User otherUser) {
        this.fullName = otherUser.getFullName();
        this.email = otherUser.getEmail();
        this.phoneNumber = otherUser.getPhoneNumber();
    }
}
