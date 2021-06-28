package br.com.maddytec.cliente.service;

import br.com.maddytec.cliente.entities.Cliente;
import br.com.maddytec.cliente.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente save(Cliente cliente) {

        return clienteRepository.save(cliente);
    }

    public List<Cliente> findAll() {

        return clienteRepository.findAll();
    }

    public void deleteById(Long id) {

        clienteRepository.deleteById(id);
    }

    public Cliente findById(Long id) throws Exception {
        Optional<Cliente> optional = clienteRepository.findById(id);
        return optional.orElseThrow(() -> new Exception("Não existe cliente com id: " + id));
    }

    public Cliente findByEmail(String email) throws Exception {
        Optional<Cliente> optional = clienteRepository.findByEmail(email);
        return optional.orElseThrow(() -> new Exception("Não existe cliente com email: " + email));
    }

}