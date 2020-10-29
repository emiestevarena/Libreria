/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.Libreria2.Servicios;
import com.libreria.Libreria2.Repositorios.EditorialR;
import com.libreria.Libreria2.Exception.ServiceException;
import com.libreria.Libreria2.Entidades.Editorial;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import javax.transaction.Transactional;
/**
 *
 * @author emiliano
 */
@Service
public class EditorialS {
    
    @Autowired
    private EditorialR editorialR;
    
    @Transactional
    public void alta(String nombre) throws ServiceException{
        validar(nombre);
        Editorial e = new Editorial();
        e.setNombre(nombre);
        editorialR.save(e);
        e=null;
    }
    
    @Transactional
    public void baja(String id) throws ServiceException{
        Optional<Editorial> editorial = editorialR.findById(id);
        if(editorial.isPresent()){
            Editorial e = editorial.get();
            editorialR.delete(e);
            e=null;
        }
    }
    
    @Transactional
    public void modificacion(String id, String nombre) throws ServiceException{ 
        validar(nombre);
        Optional<Editorial> editorial = editorialR.findById(id);
        if(editorial.isPresent()){
        Editorial e = editorial.get();
        e.setNombre(nombre);
        editorialR.save(e);
        e=null;}
        else{
            throw new ServiceException("Autor inexistente");
        }
    }
    
    private void validar(String nombre) throws ServiceException{
        if(nombre == null || nombre.isEmpty()){
            throw new ServiceException("nombre vacío");
        }
    }
    
    public List<Editorial> consulta(){
        return editorialR.findAll();
    }
}
