/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.Libreria2.Controladores;

import com.libreria.Libreria2.Entidades.*;
import com.libreria.Libreria2.Exception.ServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.libreria.Libreria2.Servicios.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.ModelMap;
import org.springframework.ui.ModelMap;
/**
 *
 * @author emiliano
 */
@Controller
@RequestMapping("/")
public class Controlador {
    
    @Autowired
    private ClienteS clienteS;
    
    @Autowired
    private LibroS libroS;
    
    @GetMapping("/")
    public String index(ModelMap modelo){
        List<Libro> libros = libroS.consulta();
        modelo.put("libros",libros);
        String count = clienteS.count();
        modelo.put("usuarios", count);
        return "index.html";
    }
    
  
    @GetMapping("/login")
    public String login(@RequestParam(required=false) String error, @RequestParam(required=false) String logout, ModelMap modelo){
        if (error!=null && !error.isEmpty()){ modelo.put("error","usuario o contraseña erróneos");}
        if (logout!=null&& !logout.isEmpty()){ modelo.put("logout","has cerrado la sesión");}
        return "login.html";
    }
    
    @GetMapping("/registro")
    public String registro(){
        return "registro.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/inicio")
    public String inicio(){
        return "inicio.html";
    }
    
    @PostMapping("/registrar")
    public String registrar(ModelMap modelo,
                          @RequestParam(required=true) String DNI,
                          @RequestParam(required=true) String nombre,
                          @RequestParam(required=true) String apellido,
                          @RequestParam(required=true) String domicilio, 
                          @RequestParam(required=true) String telefono,
                          @RequestParam(required=true) String password,
                          @RequestParam(required=true) String username)
                          throws ServiceException{
        try{
        clienteS.alta(Long.parseLong(DNI), nombre, apellido, domicilio, telefono,password,username);
        }catch(ServiceException e){
            modelo.put("error",e.getMessage());
            return "redirect:/registro";
        }
        return "redirect:/";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/admin")
    public String admin(){
        return "admin.html";
    }
    
}
