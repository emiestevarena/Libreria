/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.Libreria2.Repositorios;

import com.libreria.Libreria2.Entidades.Cliente;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
/**
 *
 * @author emiliano
 */
@Repository
public interface ClienteR extends JpaRepository<Cliente,Long>{
    
    @Query("Select c from Cliente c where c.username like :username")
    public Cliente clientePorUser(@Param("username") String username);
    
    @Query("Select c from Cliente c where c.username like :username")
    public Optional<Cliente> buscarPorUser(@Param("username") String username);
}
