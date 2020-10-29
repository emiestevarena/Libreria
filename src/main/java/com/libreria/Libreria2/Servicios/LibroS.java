/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.Libreria2.Servicios;
import com.libreria.Libreria2.Repositorios.LibroR;
import com.libreria.Libreria2.Exception.ServiceException;
import com.libreria.Libreria2.Entidades.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
/**
 *
 * @author emiliano
 */
@Service
public class LibroS {
    @Autowired
    private LibroR libroR;
    
    @Transactional
    public void alta(String title,Integer year,Integer borrowed,Integer available,List<Autor> autores,List<Editorial>editoriales) throws ServiceException{
        verificar(title,year,borrowed,available,autores,editoriales);
        Libro l = new Libro();
        l.setTitle(title);
        l.setYear(year);
        l.setBorrowed(borrowed);
        l.setAvailable(available);
        l.setAutores(autores);
        l.setEditoriales(editoriales);
        libroR.save(l);
        l=null;
    }
    
    @Transactional
    public void modificacion(Long id,String title,Integer year,Integer borrowed,Integer available,List<Autor> autores,List<Editorial>editoriales)throws ServiceException{
        verificar(title,year,borrowed,available,autores,editoriales);
        Optional<Libro> libro = libroR.findById(id);
        if(libro.isPresent()){
            Libro l = libro.get();
            l.setTitle(title);
            l.setYear(year);
            l.setBorrowed(borrowed);
            l.setAvailable(available);
            l.setAutores(autores);
            l.setEditoriales(editoriales);
            libroR.save(l);
            l=null;
            libro=null;
        }else{
            throw new ServiceException("Id inexistente");
        }
    }
    
    @Transactional
    public void baja(Long id) throws ServiceException{
        Optional<Libro> libro = libroR.findById(id);
        if(libro.isPresent()){
            Libro l = libro.get();
            libroR.delete(l);
            l=null;
            libro=null;
        }else{
            throw new ServiceException("Id inexistente");
        }
    }
    
    private void verificar(String title,Integer year,Integer borrowed,Integer available,List<Autor> autores,List<Editorial>editoriales) throws ServiceException{
        if(title==null||title.isEmpty()) throw new ServiceException("título vacío");
        if(autores==null||autores.isEmpty()) throw new ServiceException("autores vacío");
        if(editoriales==null||editoriales.isEmpty()) throw new ServiceException("editoriales vacío");
        if(year==null||year<0) throw new ServiceException("año vacío");
        if(borrowed==null||borrowed<0) throw new ServiceException("ejemplares prestados vacío");
        if(available==null||available<0) throw new ServiceException("ejemplares disponibles vacío");
    }
    
    public List<Libro> consulta(){
        return libroR.findAll();
    }
}
