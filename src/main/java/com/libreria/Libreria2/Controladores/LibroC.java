/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.Libreria2.Controladores;

import com.libreria.Libreria2.Entidades.*;
import com.libreria.Libreria2.Exception.ServiceException;
import com.libreria.Libreria2.Servicios.*;
import java.util.ArrayList;
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
public class LibroC {
    
    @Autowired
    private AutorS autorS;
    
    @Autowired
    private EditorialS editorialS;
    
    @Autowired
    private LibroS libroS;
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/libros")
    public String autores(@RequestParam(required=false) String erroralta,
                          @RequestParam(required=false) String altaok,
                          ModelMap modelo){
        if(erroralta!=null){modelo.put("erroralta","error en el alta");}
        List<Autor> autores = autorS.consultar();
        modelo.put("autores",autores);
        List<Editorial> editoriales = editorialS.consulta();
        modelo.put("editoriales",editoriales);
        List<Libro> libros = libroS.consulta();
        modelo.put("libros",libros);
        return "libros.html";
    }
    
    @PostMapping("/alta_libro")
    public String alta(@RequestParam(required=true) String nombre,
                       @RequestParam(required=true) List<String> autores,
                       @RequestParam(required=true) List<String> editoriales,
                       @RequestParam(required=true) String year,
                       @RequestParam(required=true) String prestados,
                       @RequestParam(required=true) String disponibles,
                        ModelMap modelo){
        List<Autor> autoresOK = new ArrayList<>();
        for(String id: autores){
            Autor a = autorS.get(id);
            autoresOK.add(a);
        }
        List<Editorial> editorialesOK = new ArrayList<>();
        for(String id: editoriales){
            Editorial e = editorialS.get(id);
            editorialesOK.add(e);
        }
        Integer y = Integer.parseInt(year);
        Integer p = Integer.parseInt(prestados);
        Integer d = Integer.parseInt(disponibles);
        try{
        libroS.alta(nombre, y, p, d, autoresOK, editorialesOK);
        }catch(ServiceException e){
            modelo.put("error",e.getMessage());
        }
        return "redirect:/libros";
    }
    
    @PostMapping("/baja_libro")
    public String baja(@RequestParam(required=true) String id,
                       ModelMap modelo){
        try{
            Long idOK = Long.parseLong(id);
            libroS.baja(idOK);
        }catch(ServiceException e){
            modelo.put("error",e.getMessage());
        }
        return "redirect/libros";
    }
}
