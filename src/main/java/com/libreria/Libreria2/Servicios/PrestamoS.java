/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.Libreria2.Servicios;
import com.libreria.Libreria2.Repositorios.PrestamoR;
import com.libreria.Libreria2.Exception.ServiceException;
import com.libreria.Libreria2.Entidades.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.transaction.Transactional;
/**
 *
 * @author emiliano
 */
@Service
public class PrestamoS {
   @Autowired
   private PrestamoR prestamoR;
   
   @Transactional
   public void alta(Date entrega,Date devolucion,Double multa,List<Libro> libros,List<Cliente> clientes) throws ServiceException{
       verificar(entrega,devolucion,multa,libros,clientes);
       Prestamo p = new Prestamo();
       p.setEntrega(entrega);
       p.setDevolucion(devolucion);
       p.setMulta(multa);
       p.setClientes(clientes);
       p.setLibros(libros);
       prestamoR.save(p);
   }
   
   @Transactional
   public void modificacion(String id,Date entrega,Date devolucion,Double multa,List<Libro> libros,List<Cliente> clientes) throws ServiceException{
       verificar(entrega,devolucion,multa,libros,clientes);
       Optional<Prestamo> prestamo= prestamoR.findById(id);
       if(prestamo.isPresent()){
           Prestamo p = prestamo.get();
           p.setEntrega(entrega);
           p.setDevolucion(devolucion);
           p.setMulta(multa);
           p.setClientes(clientes);
           p.setLibros(libros);
           prestamoR.save(p);
           p=null;
           prestamo=null;
       }else{
           throw new ServiceException("Id inválido");
       }
   }
   
   @Transactional
   public void baja(String id) throws ServiceException{
       Optional<Prestamo> prestamo= prestamoR.findById(id);
       if(prestamo.isPresent()){
           Prestamo p = prestamo.get();
           prestamoR.delete(p);
           p=null;
           prestamo=null;
       }else{
           throw new ServiceException("Id inválido");
       }
   }
   
   private void verificar(Date entrega,Date devolucion,Double multa,List<Libro> libros,List<Cliente> clientes) throws ServiceException{
       if(entrega==null) throw new ServiceException("Fecha de entrega inválida");
       if(devolucion==null||devolucion.before(entrega)) throw new ServiceException("Fecha de devolución inválida");
       if(multa==null||multa<0) throw new ServiceException("Multa inválida");
       if(libros==null||libros.isEmpty()) throw new ServiceException("Libros inválidos");
       if(clientes==null||clientes.isEmpty()) throw new ServiceException("Clientes inválidos");
   }
   
   public List<Prestamo> consulta(){
       List<Prestamo> iterate = prestamoR.findAll();
       Date d = new Date();
       for(Prestamo p: iterate){
           if(d.after(p.getDevolucion())){
               long fechaInicialMs = d.getTime();
               long fechaFinalMs = p.getDevolucion().getTime();
               long diferencia = fechaFinalMs - fechaInicialMs;
               double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
               p.setMulta(-dias*3.50);
               prestamoR.save(p);
           }
       }
       return prestamoR.findAll();
   }
   
   public Prestamo getPrestamo(String id){
       Prestamo p = prestamoR.getOne(id);
       Date d = new Date();
       if(d.after(p.getDevolucion())){
               long fechaInicialMs = d.getTime();
               long fechaFinalMs = p.getDevolucion().getTime();
               long diferencia = fechaFinalMs - fechaInicialMs;
               double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
               p.setMulta(dias*3.50);
               prestamoR.save(p);
           }
       return prestamoR.getOne(id);
   }
   
   public List<Prestamo> consultaPorId(Long id){
       List<Prestamo> a = this.consulta();
       List<Prestamo> b = new ArrayList();
       for(Prestamo p : a){
           for(Cliente c: p.getClientes()){
               if(Objects.equals(id, c.getId())){
                   b.add(p);
               }
           }
       }
       return b;
   }
}
