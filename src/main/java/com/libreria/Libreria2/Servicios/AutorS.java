/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.Libreria2.Servicios;

import com.libreria.Libreria2.Repositorios.AutorR;
import com.libreria.Libreria2.Exception.ServiceException;
import com.libreria.Libreria2.Entidades.Autor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import javax.transaction.Transactional;

/**
 *
 * @author emiliano
 */
@Service
public class AutorS{
    @Autowired
    private AutorR autorR;
    
    @Transactional
    public void alta(String nombre) throws ServiceException{
        validar(nombre);
        Autor a = new Autor();
        a.setNombre(nombre);
        autorR.save(a);
        a=null;
    }
    
    @Transactional
    public void baja(String id) throws ServiceException{
        Optional<Autor> autor = autorR.findById(id);
        if(autor.isPresent()){
            Autor a = autor.get();
            autorR.delete(a);
            a=null;
        }
    }
    
    @Transactional
    public void modificacion(String id, String nombre) throws ServiceException{ 
        validar(nombre);
        Optional<Autor> autor = autorR.findById(id);
        if(autor.isPresent()){
        Autor a = autor.get();
        a.setNombre(nombre);
        autorR.save(a);
        a=null;}
        else{
            throw new ServiceException("Autor inexistente");
        }
    }
    
    private void validar(String nombre) throws ServiceException{
        if(nombre == null || nombre.isEmpty()){
            throw new ServiceException("nombre vacío");
        }
    }
}
