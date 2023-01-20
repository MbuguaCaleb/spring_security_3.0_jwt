package com.codewithcaleb.security.user;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder  //Helps me build my Objects using the design builder class
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
// Postgres has a User Table by Default //We are renaming our table to avoid collision
//Once i build my class i want it to inherit the Spring Security Properties
public class User implements UserDetails {

    //MySQL does not work with sequences...instead it works with tables
    @Id //unique identifier of the User Class
    //@GeneratedValue(strategy = GenerationType.AUTO) //helps me autogenerate my id...every time //default value is strategy auto
    // id is null it will be auto_incremented
    @GeneratedValue
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;


    @Enumerated(EnumType.STRING)
    private Role role;

    //return a list of Roles
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    //I will use my username as the email
    //this sets my claim
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
