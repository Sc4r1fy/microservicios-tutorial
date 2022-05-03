package com.api.rest.usuarioservice.service.controlador;

import com.api.rest.usuarioservice.service.entidades.Usuario;
import com.api.rest.usuarioservice.service.modelos.Carro;
import com.api.rest.usuarioservice.service.modelos.Moto;
import com.api.rest.usuarioservice.service.servicio.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios(){
        List<Usuario> usuarios = usuarioService.getAll();
        if(usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable("id") int id){
        Usuario usuario = usuarioService.getUsuarioById(id);
        if(usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity<Usuario> guardarUsuario(@RequestBody Usuario usuario){
        Usuario nuevoUsuario = usuarioService.save(usuario);
        return ResponseEntity.ok(nuevoUsuario);
    }

    // (UMPA LUMPA)
    @GetMapping("/carros/{usuarioId}")
    public ResponseEntity<List<Carro>> listarCarros(@PathVariable("usuarioId") int id){
        Usuario usuario = usuarioService.getUsuarioById(id);
        if(usuario == null) {
            return ResponseEntity.notFound().build();
        }

        List<Carro> carros = usuarioService.getCarros(id);
        return ResponseEntity.ok(carros);
    }

    @GetMapping("/motos/{usuarioId}")
    public ResponseEntity<List<Moto>> listarMotos(@PathVariable("usuarioId") int id){
        Usuario usuario = usuarioService.getUsuarioById(id);
        if(usuario == null) {
            return ResponseEntity.notFound().build();
        }

        List<Moto> motos = usuarioService.getMotos(id);
        return ResponseEntity.ok(motos);
    }


    //metodos feign client

    @PostMapping("/carro/{usuarioId}")
    public ResponseEntity<Carro> guardarCarro(@PathVariable("usuarioId") int usuarioId , @RequestBody Carro carro){

        // el metodo saveCarro me va a persistir el carro nuevo en la BD y va a devolver el carro pero con el usuario seteado.
        // la ruta es : Objectcontroller --> ObjectService --> FeigClientObject
        Carro nuevoCarro = usuarioService.saveCarro(usuarioId , carro);
        return ResponseEntity.ok( nuevoCarro);

    }


    @PostMapping("/moto/{usuarioId}")
    public ResponseEntity<Moto> guardarMoto(@PathVariable("usuarioId") int usuarioId , @RequestBody Moto moto){
        return ResponseEntity.ok( usuarioService.guardarMoto(moto, usuarioId) );

    }


    @GetMapping("/todos/{usuarioId}")
    public ResponseEntity< Map< String , Object>> listarTodosLosVehiculos(@PathVariable("usuarioId") int usuarioId){
        return ResponseEntity.ok( usuarioService.getUsuarioAndVehiculo(usuarioId) );
    }


    }
