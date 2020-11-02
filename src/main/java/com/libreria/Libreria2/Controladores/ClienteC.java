/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.Libreria2.Controladores;

import com.libreria.Libreria2.Entidades.*;
import com.libreria.Libreria2.Exception.ServiceException;
import com.libreria.Libreria2.Servicios.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author emiliano
 */
@Controller
@RequestMapping("/")
public class ClienteC {
    
    @Autowired
    private ClienteS clienteS;
    
    @Autowired
    private PrestamoS prestamoS;
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/clientes")
    public String clientes(ModelMap modelo){
        List<Cliente> clientes = clienteS.consulta();
        modelo.put("clientes",clientes);        
        return "clientes.html";
    }
    
    @PostMapping("/alta_cliente")
    public String alta(ModelMap modelo,
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
        }
        return "redirect:/clientes";
    }
            
}
