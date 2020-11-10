/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.Libreria2.Servicios;

import com.libreria.Libreria2.Repositorios.ClienteR;
import com.libreria.Libreria2.Exception.ServiceException;
import com.libreria.Libreria2.Entidades.Cliente;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author emiliano
 */
@Service
public class ClienteS implements UserDetailsService {

    @Autowired
    private ClienteR clienteR;

    @Transactional
    public void alta(Long documento, String nombre, String apellido, String domicilio, String telefono, String password, String username,Boolean admin) throws ServiceException {
        verificar(documento, nombre, apellido, domicilio, telefono, password, username);
        Optional<Cliente> cliente = clienteR.buscarPorUser(username);
        if (!cliente.isPresent()) {
            String claveEncriptada = new BCryptPasswordEncoder().encode(password);
            Cliente c = new Cliente();
            c.setDocumento(documento);
            c.setNombre(nombre);
            c.setApellido(apellido);
            c.setDomicilio(domicilio);
            c.setTelefono(telefono);
            c.setPassword(claveEncriptada);
            c.setUsername(username);
            c.setAdmin(admin);
            clienteR.save(c);
            c = null;
        } else {
            throw new ServiceException("Inserte otro usuario");
        }

    }

    @Transactional
    public void modificacion(Long id, Long documento, String nombre, String apellido, String domicilio, String telefono, String password, String username) throws ServiceException {
        verificar(documento, nombre, apellido, domicilio, telefono, password, username);
        Optional<Cliente> cliente = clienteR.findById(id);
        if (cliente.isPresent()) {
            String claveEncriptada = new BCryptPasswordEncoder().encode(password);
            Cliente c = cliente.get();
            c.setNombre(nombre);
            c.setApellido(apellido);
            c.setDomicilio(domicilio);
            c.setTelefono(telefono);
            c.setDocumento(documento);
            c.setUsername(username);
            c.setPassword(claveEncriptada);
            clienteR.save(c);
            c = null;
            cliente = null;
        } else {
            throw new ServiceException("Id inexistente");
        }
    }

    @Transactional
    public void baja(Long id) throws ServiceException {
        Optional<Cliente> cliente = clienteR.findById(id);
        if (cliente.isPresent()) {
            Cliente c = cliente.get();
            clienteR.delete(c);
            c = null;
            cliente = null;
        } else {
            throw new ServiceException("Id inexistente");
        }
    }

    private void verificar(Long id, String nombre, String apellido, String domicilio, String telefono, String password, String username) throws ServiceException {
        if (id == null || id < 0) {
            throw new ServiceException("Id inválido");
        }
        if (nombre == null || nombre.isEmpty()) {
            throw new ServiceException("nombre inválido");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new ServiceException("apellido inválido");
        }
        if (domicilio == null || domicilio.isEmpty()) {
            throw new ServiceException("domicilio inválido");
        }
        if (telefono == null || telefono.isEmpty()) {
            throw new ServiceException("teléfono inválido");
        }
        if (password == null || password.isEmpty()) {
            throw new ServiceException("contraseña inválida");
        }
        if (username == null || username.isEmpty()) {
            throw new ServiceException("usuario inválido");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        try {
            Cliente c = clienteR.clientePorUser(username);
            
            System.out.println(c.toString());
            
            if (c != null) {
                List<GrantedAuthority> permisos = new ArrayList<>();
                GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_USUARIO_REGISTRADO");
                permisos.add(p1);
                if(c.getAdmin()){
                    GrantedAuthority p2 = new SimpleGrantedAuthority("ROLE_ADMIN");
                    permisos.add(p2);
                } 
                ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
                HttpSession session = attr.getRequest().getSession(true);
                session.setAttribute("clientesession", c);
                User us = new User(c.getUsername(), c.getPassword(), permisos);
                return us;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

    }

    @Transactional
    public Cliente getCliente(Long id) {
        Optional<Cliente> cliente = clienteR.findById(id);
        if (cliente.isPresent()) {
            return cliente.get();
        } else {
            return null;
        }
    }
    
    public List<Cliente> consulta(){
        return clienteR.findAll();
    }
    
    public String count(){
        Long i = clienteR.count();
        return i.toString();
    }
}
