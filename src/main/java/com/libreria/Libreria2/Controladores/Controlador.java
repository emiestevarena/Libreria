/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.Libreria2.Controladores;

import com.libreria.Libreria2.Entidades.*;
import com.libreria.Libreria2.Exception.Fecha;
import com.libreria.Libreria2.Exception.ServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.libreria.Libreria2.Servicios.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
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
    
    @Autowired
    private AutorS autorS;
    
    @Autowired
    private PrestamoS prestamoS;
    
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
    public String inicio(ModelMap modelo,@RequestParam(required=false) String error, HttpSession session){
        if(error!=null&&!error.isEmpty()){modelo.put("error",error);}
        List<Libro> libros = libroS.consulta();
        modelo.put("libros",libros);
        List<Autor> autores = autorS.consultar();
        modelo.put("autores",autores);
        Cliente c = (Cliente) session.getAttribute("clientesession"); 
        List<Prestamo> prestamos = prestamoS.consultaPorId(c.getId());
        modelo.put("prestamos", prestamos);
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
    
    @PostMapping("/prestamo")
    public String alta(ModelMap modelo,
                       @RequestParam(required=true) String clienteId,
                       @RequestParam(required=true) List<String> librosOK,
                       @RequestParam(required=true) String devolucion) throws ServiceException{
        try{
        
            Date ent = new Date();
            Date dev = Fecha.parseFechaGuiones(devolucion);
            List<Libro> libros = new ArrayList();
            for(String i : librosOK){
                Long id = Long.parseLong(i);
                Libro l = libroS.get(id);
                l.setAvailable(l.getAvailable()-1);
                l.setBorrowed(l.getBorrowed()+1);
                libroS.modificacion(l.getId(), l.getTitle(), l.getYear(), l.getAvailable(), l.getBorrowed(), l.getAutores(), l.getEditoriales());
                libros.add(l);
            }
            
            List<Cliente> clientes = new ArrayList();
            Long id = Long.parseLong(clienteId);
            clientes.add(clienteS.getCliente(id));
            
        
            prestamoS.alta(ent, dev, 0.0, libros, clientes);
        }catch(ServiceException ex){
            modelo.put("error", ex.getMessage());
        }
        return "redirect:/inicio";
    }
    
    @PostMapping("/extension")
    public String extensiones(ModelMap modelo,
                            @RequestParam(required=true) String id,
                            @RequestParam(required=false) String devolucion) throws ServiceException{
        try{
            Prestamo p = prestamoS.getPrestamo(id);
            if(devolucion!=null&&!devolucion.isEmpty())p.setDevolucion(Fecha.parseFechaGuiones(devolucion));
            prestamoS.modificacion(id, p.getEntrega(), p.getDevolucion(), p.getMulta(), p.getLibros(), p.getClientes());
        }catch(ServiceException ex){
            modelo.put("error",ex.getMessage());
        }
        return "redirect:/inicio";
    }
    
    @PostMapping("/devoluciones")
    public String devoluciones(ModelMap modelo,
                       @RequestParam(required=true) String id) throws ServiceException{
        try{

            Prestamo p = prestamoS.getPrestamo(id);

            for(Libro l : p.getLibros()){
                l.setAvailable(l.getAvailable()+1);
                l.setBorrowed(l.getBorrowed()-1);
                libroS.modificacion(l.getId(), l.getTitle(), l.getYear(), l.getAvailable(), l.getBorrowed(), l.getAutores(), l.getEditoriales());
            }

            prestamoS.baja(id);
        }catch(ServiceException ex){
            modelo.put("error", ex.getMessage());
        }
        return "redirect:/inicio";
    }
    
    
    
}
