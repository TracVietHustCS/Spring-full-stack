//package org.project1.shopweb.configure;
//
//import lombok.RequiredArgsConstructor;
//import org.project1.shopweb.Model.Role;
//import org.project1.shopweb.Model.User;
//import org.project1.shopweb.Repository.UserRepository;
//import org.project1.shopweb.exception.NotFoundException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class ShopWebUserDetailService implements UserDetailsService {
//    private final UserRepository userRepository;
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User existUser = userRepository.findByPhoneNumber(username)
//                .orElseThrow(() -> new NotFoundException("User detail not found for the number" + username));
//
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        Role role = existUser.getRole();
//        authorities.add(new SimpleGrantedAuthority(role.getName()));
//        return new org.springframework.security.core
//                .userdetails.User(existUser.getPhoneNumber(),existUser.getPassword(),authorities);
//    }
//}
