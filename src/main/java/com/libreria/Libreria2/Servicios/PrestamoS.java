/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.Libreria2.Servicios;
import com.libreria.Libreria2.Repositorios.PrestamoR;
import com.libreria.Libreria2.Exception.ServiceException;
import com.libreria.Libreria2.Entidades.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;
import java.util.List;
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
       p=null;
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
       Date d = new Date();
       if(entrega==null||entrega.before(d)) throw new ServiceException("Fecha de entrega inválida");
       if(devolucion==null||devolucion.before(d)) throw new ServiceException("Fecha de devolución inválida");
       d=null;
       if(multa==null||multa<0) throw new ServiceException("Multa inválida");
       if(libros==null||libros.isEmpty()) throw new ServiceException("Libros inválidos");
       if(clientes==null||clientes.isEmpty()) throw new ServiceException("Clientes inválidos");
   }
}
