/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.Libreria2.Controladores;

import com.libreria.Libreria2.Entidades.*;
import com.libreria.Libreria2.Exception.Fecha;
import com.libreria.Libreria2.Exception.ServiceException;
import com.libreria.Libreria2.Servicios.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
public class PrestamoC {
 
    
    @Autowired
    private PrestamoS prestamoS;
    
    @Autowired
    private ClienteS clienteS;
    
    @Autowired
    private LibroS libroS;
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/prestamos")
    public String clientes(ModelMap modelo,@RequestParam(required=false) String error){
        List<Prestamo> prestamos = prestamoS.consulta();
        List<Cliente> clientes = clienteS.consulta();
        List<Libro> libros = libroS.consulta();
        modelo.put("clientes",clientes);
        modelo.put("prestamos",prestamos);
        modelo.put("libros",libros);
        if (error!=null && !error.isEmpty()){modelo.put("error",error);}
        return "prestamos.html";
    }
    
    @PostMapping("/alta_prestamo")
    public String alta(ModelMap modelo,
                       @RequestParam(required=true) List<String> clientesOK,
                       @RequestParam(required=true) List<String> librosOK,
                       @RequestParam(required=true) String entrega,
                       @RequestParam(required=true) String devolucion) throws ServiceException{
        try{
        
            
            Date ent = Fecha.parseFechaGuiones(entrega);
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
            for (String i : clientesOK){
                Long id = Long.parseLong(i);
                clientes.add(clienteS.getCliente(id));
            }
        
            prestamoS.alta(ent, dev, 0.0, libros, clientes);
        }catch(ServiceException ex){
            modelo.put("error", ex.getMessage());
        }
        return "redirect:/prestamos";
    }
    
    @PostMapping("/modificar_prestamo")
    public String modificar(ModelMap modelo,
                            @RequestParam(required=true) String id,
                            @RequestParam(required=false) String entrega,
                            @RequestParam(required=false) String devolucion,
                            @RequestParam(required=false) String multa,
                            @RequestParam(required=false) List<String> clientes) throws ServiceException{
        try{
            Prestamo p = prestamoS.getPrestamo(id);
            if(entrega!=null&&!entrega.isEmpty())p.setEntrega(Fecha.parseFechaGuiones(entrega));
            if(devolucion!=null&&!devolucion.isEmpty())p.setDevolucion(Fecha.parseFechaGuiones(devolucion));
            if(multa!=null&&!multa.isEmpty())p.setMulta(Double.parseDouble(multa));
            if(clientes!=null&&!clientes.isEmpty()){
                List<Cliente> clientesOK = new ArrayList();
                for (String i : clientes){
                    Long id2 = Long.parseLong(i);
                    clientesOK.add(clienteS.getCliente(id2));
                }
                p.setClientes(clientesOK);
            }
            prestamoS.modificacion(id, p.getEntrega(), p.getDevolucion(), p.getMulta(), p.getLibros(), p.getClientes());
        }catch(ServiceException ex){
            modelo.put("error",ex.getMessage());
        }
        return "redirect:/prestamos";
    }
    
    @PostMapping("/baja_prestamo")
    public String baja(ModelMap modelo,
                       @RequestParam(required=true) String id) throws ServiceException{
        try{

            Prestamo p = prestamoS.getPrestamo(id);

            for(Libro l : p.getLibros()){
                l.setAvailable(l.getAvailable()+1);
                l.setBorrowed(l.getBorrowed()-1);
                libroS.modificacion(l.getId(), l.getTitle(), l.getYear(), l.getAvailable(), l.getBorrowed(), l.getAutores(), l.getEditoriales());
            }

            prestamoS.baja(id);
        }catch(Exception ex){
            modelo.put("error", ex.getMessage());
        }
        return "redirect:/prestamos";
    }
}
