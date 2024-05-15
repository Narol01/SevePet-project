package ait.cohort34.accounting.service;

import ait.cohort34.accounting.dao.UserAccountRepository;
import ait.cohort34.accounting.model.UserAccount;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    final private UserAccountRepository repository;

    public UserService(UserAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount user = repository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException(username));

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }
}
