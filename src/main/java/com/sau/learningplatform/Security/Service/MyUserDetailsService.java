package com.sau.learningplatform.Security.Service;

import com.sau.learningplatform.Entity.User;
import com.sau.learningplatform.Repository.UserRepository;
import com.sau.learningplatform.Security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

         username=username.toUpperCase();
         Optional<User> user=userRepository.findByNumber(username);

         if (user.isEmpty()){
             log.warn("User not found!");
             throw new UsernameNotFoundException("User not found!");
         }

         return new UserPrincipal(user.get());
    }
}
