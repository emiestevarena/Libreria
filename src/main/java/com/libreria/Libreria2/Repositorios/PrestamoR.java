/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.Libreria2.Repositorios;

import com.libreria.Libreria2.Entidades.Prestamo;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
/**
 *
 * @author emiliano
 */
@Repository
public interface PrestamoR extends JpaRepository<Prestamo,String> {
    
}
