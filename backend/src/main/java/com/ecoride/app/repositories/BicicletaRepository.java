package com.ecoride.app.repositories;

import com.ecoride.app.model.Bicicleta;
import com.ecoride.app.model.EstadoBicicleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BicicletaRepository extends JpaRepository<Bicicleta, Long> {
    
    // Cumple con el requerimiento de la Semana 6: JPQL con parámetros nombrados
    @Query("SELECT b FROM Bicicleta b WHERE b.estado = :estado")
    List<Bicicleta> buscarPorEstadoParametrizado(@Param("estado") EstadoBicicleta estado);
}