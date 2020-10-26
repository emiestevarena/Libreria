/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.Libreria2.Controladores;

import com.libreria.Libreria2.Exception.ServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.libreria.Libreria2.Servicios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    
    @GetMapping("/")
    public String index(){
        return "index.html";
    }
    
    @GetMapping("/login")
    public String login(@RequestParam(required=false) String error, ModelMap modelo){
        if (error!=null) modelo.put("error","usuario o contraseña erróneos");
        return "login.html";
    }
    
    @GetMapping("/registro")
    public String registro(){
        return "registro.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/Inicio")
    public String inicio(){
        return "inicio.html";
    }
    
    @PostMapping("/registrar")
    public String registrar(ModelMap modelo,
                          @RequestParam(required=true) String DNI,
                          @RequestParam(required=true) String nombre,
                          @RequestParam(required=true) String apellido,
                          @RequestParam(required=true) String domicilio, 
                          @RequestParam(required=true) String telefono)
                          throws ServiceException{
        try{
        clienteS.alta(Long.parseLong(DNI), nombre, apellido, domicilio, telefono);
        }catch(ServiceException e){
            modelo.put("error",e.getMessage());
            return "redirect:/registro";
        }
        return "redirect:/";
    }
}
