package com.api.rest.usuarioservice.service.repositorios;

import com.api.rest.usuarioservice.service.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {
}
