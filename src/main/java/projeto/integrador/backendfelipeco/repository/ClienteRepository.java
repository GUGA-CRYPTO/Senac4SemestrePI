package projeto.integrador.backendfelipeco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projeto.integrador.backendfelipeco.Entidades.Cliente;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
}

