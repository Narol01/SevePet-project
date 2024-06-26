package ait.cohort34.accounting.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserAccount implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String login;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "userAccount")
    private PhotoUser avatar;
    private String password;
    private String fullName;
    private String email;
    private String website;
    private String phone;
    private String telegram;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public UserAccount(String login, PhotoUser avatar, String password, String fullName, String email, String website, String phone, String telegram) {
        this.login = login;
        this.avatar = avatar;
        this.password = password;
        this.fullName=fullName;
        this.email = email;
        this.website = website;
        this.phone = phone;
        this.telegram = telegram;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return this.login;
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
