package br.com.maddytec.cliente.resource;

import br.com.maddytec.cliente.entities.Cliente;
import br.com.maddytec.cliente.request.ClienteRequest;
import br.com.maddytec.cliente.response.ClienteResponse;
import br.com.maddytec.cliente.service.ClienteService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "cliente")
public class ClienteResource {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    public ResponseEntity<ClienteResponse> save(@RequestBody ClienteRequest clienteRequest) {
        Cliente cliente = new Cliente();
        BeanUtils.copyProperties(clienteRequest, cliente);
        clienteService.save(cliente);

        ClienteResponse clienteResponse = new ClienteResponse();
        BeanUtils.copyProperties(cliente, clienteResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(clienteResponse);

    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR', 'CONSULTA')")
    public ResponseEntity<List<ClienteResponse>> findAll() {
        List<Cliente> listaCliente = clienteService.findAll();

        List<ClienteResponse> listaClienteResponse = new ArrayList<>();
        ModelMapper mapper = new ModelMapper();
        listaCliente.forEach(cliente -> listaClienteResponse.add(mapper.map(cliente, ClienteResponse.class)));

        return ResponseEntity.ok(listaClienteResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> findById(@PathVariable(name = "id") Long id) {
        Cliente cliente = null;
        try {
            cliente = clienteService.findById(id);

            ClienteResponse clienteResponse = new ClienteResponse();
            BeanUtils.copyProperties(cliente, clienteResponse);

            return ResponseEntity.ok().body(clienteResponse);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ClienteResponse.builder().error(e.getMessage()).build());
        }
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'CONSULTA')")
    public ResponseEntity<ClienteResponse> findByEmail(@PathVariable(name = "email") String email) {
        try {
            Cliente cliente = clienteService.findByEmail(email);

            ClienteResponse clienteResponse = new ClienteResponse();
            BeanUtils.copyProperties(cliente, clienteResponse);

            return ResponseEntity.ok().body(clienteResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ClienteResponse.builder().error(e.getMessage()).build());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR')")
    public ResponseEntity<ClienteResponse> update(@PathVariable(name = "id") Long id, @RequestBody ClienteRequest clienteRequest) {
        try {
            Cliente cliente = clienteService.findById(id);
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setSkipNullEnabled(true);
            modelMapper.map(clienteRequest, cliente);
            cliente.setId(id);
            clienteService.save(cliente);

            ClienteResponse clienteResponse = new ClienteResponse();
            BeanUtils.copyProperties(cliente, clienteResponse);

            return ResponseEntity.status(HttpStatus.OK).body(clienteResponse);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ClienteResponse.builder().error(e.getMessage()).build());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    public ResponseEntity<?> deleteById(@PathVariable(name = "id") Long id) {
        try {
            clienteService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(ClienteResponse.builder().error("Cliente não existe ou invalido: " + id).build());
        }
    }

}