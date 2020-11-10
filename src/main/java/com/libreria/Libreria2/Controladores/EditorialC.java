/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.Libreria2.Controladores;

import com.libreria.Libreria2.Entidades.Editorial;
import com.libreria.Libreria2.Exception.ServiceException;
import com.libreria.Libreria2.Servicios.EditorialS;
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
public class EditorialC {
    
    @Autowired
    private EditorialS editorialS;
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/editoriales")
    public String editoriales(@RequestParam(required=false) String erroralta,
                                ModelMap modelo){
        if(erroralta!=null){modelo.put("erroralta","error en el alta");}
        List<Editorial> editoriales = editorialS.consulta();
        modelo.put("editoriales",editoriales);
        return "editoriales.html";
    }
    
    @PostMapping("/alta_editorial")
    public String alta(@RequestParam(required=true) String nombre,
                        ModelMap modelo){
        try{
            editorialS.alta(nombre);
        }catch(ServiceException e){
            modelo.put("error",e.getMessage());
        }
        return "redirect:/editoriales";
    }
    
    @PostMapping("/modificar_editorial")
    public String modificar(@RequestParam(required=true) String nombreviejo,
                            @RequestParam(required=true) String nombrenuevo,
                                    ModelMap modelo){
        try{
            editorialS.modificacion(nombreviejo, nombrenuevo);
        }catch(ServiceException e){
            modelo.put("error_modificacion",e.getMessage());
        }
        return "redirect:/editoriales";
    }
    
    @PostMapping("/baja_editorial")
    public String baja(@RequestParam(required=true) String nombreviejo,
                                    ModelMap modelo){
        try{
            editorialS.baja(nombreviejo);
        }catch(ServiceException e){
            modelo.put("error_baja",e.getMessage());
        }
        return "redirect:/editoriales";
    }
}
