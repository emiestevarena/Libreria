/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.Libreria2.Servicios;
import com.libreria.Libreria2.Repositorios.ClienteR;
import com.libreria.Libreria2.Exception.ServiceException;
import com.libreria.Libreria2.Entidades.Cliente;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import javax.transaction.Transactional;
/**
 *
 * @author emiliano
 */
@Service
public class ClienteS {
    @Autowired
    private ClienteR clienteR;
    
    
    @Transactional
    public void alta(Long id,String nombre,String apellido,String domicilio,String telefono) throws ServiceException{
        verificar(id,nombre,apellido,domicilio,telefono);
        Cliente c = new Cliente();
        c.setId(id);
        c.setNombre(nombre);
        c.setApellido(apellido);
        c.setDomicilio(domicilio);
        c.setTelefono(telefono);
        clienteR.save(c);
        c=null;
    }
    
    @Transactional
    public void modificacion(Long id,String nombre,String apellido,String domicilio,String telefono) throws ServiceException{
        verificar(id,nombre,apellido,domicilio,telefono);
        Optional<Cliente> cliente = clienteR.findById(id);
        if(cliente.isPresent()){
            Cliente c = cliente.get();
            c.setNombre(nombre);
            c.setApellido(apellido);
            c.setDomicilio(domicilio);
            c.setTelefono(telefono);
            clienteR.save(c);
            c=null;
            cliente=null;
        }else{
            throw new ServiceException("Id inexistente");
        }
    }
    
    @Transactional
    public void baja(Long id) throws ServiceException{
        Optional<Cliente> cliente = clienteR.findById(id);
        if(cliente.isPresent()){
            Cliente c = cliente.get();
            clienteR.delete(c);
            c=null;
            cliente=null;
        }else{
            throw new ServiceException("Id inexistente");
        }
    }
    
    private void verificar(Long id,String nombre,String apellido,String domicilio,String telefono) throws ServiceException{
        if(id==null||id<0) throw new ServiceException("Id inválido");
        if(nombre==null||nombre.isEmpty()) throw new ServiceException("nombre inválido");
        if(apellido==null||apellido.isEmpty()) throw new ServiceException("apellido inválido");
        if(domicilio==null||domicilio.isEmpty()) throw new ServiceException("domicilio inválido");
        if(telefono==null||telefono.isEmpty()) throw new ServiceException("teléfono inválido");
    }
}
