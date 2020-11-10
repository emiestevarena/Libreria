/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.Libreria2.Controladores;

import com.libreria.Libreria2.Entidades.Autor;
import com.libreria.Libreria2.Exception.ServiceException;
import com.libreria.Libreria2.Servicios.AutorS;
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
public class AutorC {
    
    @Autowired
    private AutorS autorS;
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/autores")
    public String autores(@RequestParam(required=false) String erroralta,
                          @RequestParam(required=false) String altaok,
                          ModelMap modelo){
        if(erroralta!=null){modelo.put("erroralta","error en el alta");}
        List<Autor> autores = autorS.consultar();
        modelo.put("autores",autores);
        return "autores.html";
    }
    
    @PostMapping("/alta_autor")
    public String alta(@RequestParam(required=true) String nombre,
                        ModelMap modelo){
        try{
            autorS.alta(nombre);
        }catch(ServiceException e){
            modelo.put("erroralta", e.getMessage());
        }
        return "redirect:/autores";
    }
            
    @PostMapping("/modificar_autor")
    public String modificar(@RequestParam(required=true) String nombreviejo,
                            @RequestParam(required=true) String nombrenuevo,
                                    ModelMap modelo){
        try{
            autorS.modificacion(nombreviejo, nombrenuevo);
        }catch(ServiceException e){
            modelo.put("error_modificacion",e.getMessage());
        }
        return "redirect:/autores";
    }
    
    @PostMapping("/baja_autor")
    public String baja(@RequestParam(required=true) String nombreviejo,
                                    ModelMap modelo){
        try{
            autorS.baja(nombreviejo);
        }catch(ServiceException e){
            modelo.put("error_baja",e.getMessage());
        }
        return "redirect:/autores";
    }
    
}
