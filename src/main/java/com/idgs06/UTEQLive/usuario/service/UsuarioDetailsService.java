package com.idgs06.UTEQLive.usuario.service;

import com.idgs06.UTEQLive.usuario.entity.Rol;
import com.idgs06.UTEQLive.usuario.entity.Usuario;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.idgs06.UTEQLive.usuario.repository.IUsuarioRepository;

@Service("userDetailsService")
public class UsuarioDetailsService implements UserDetailsService {

    @Autowired
    private IUsuarioRepository repo;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = repo.findByCorreoAndEnabled(username, true);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Rol rol : user.getAuthorities()) {
            authorities.add(new SimpleGrantedAuthority(rol.getAuthority()));
        }
        return new User(user.getCorreo(), user.getPassword(), user.isEnabled(), true, true, true, authorities);
    }

}