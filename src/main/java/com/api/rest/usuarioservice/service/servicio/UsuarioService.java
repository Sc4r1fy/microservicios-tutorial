package com.api.rest.usuarioservice.service.servicio;

import com.api.rest.usuarioservice.service.entidades.Usuario;
import com.api.rest.usuarioservice.service.feignClients.CarroFeignClient;
import com.api.rest.usuarioservice.service.feignClients.MotoFeignClient;
import com.api.rest.usuarioservice.service.modelos.Carro;
import com.api.rest.usuarioservice.service.modelos.Moto;
import com.api.rest.usuarioservice.service.repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsuarioService {

     /*
     VAMOS A COMUNICAR LAS APIS USANDO REST TEMPLATE :
    para implementar arquitectura de micro servicios simplemente hay que agregar los paquetes : configuracion y modelos.
    1) en configuracion va una clase llamada RestTemplateConfig.
    2) en la clase models pondremos las entidades principales de los otros proyectos con sus atributos.
    3) en UsuarioService (esta clase) pondremos los metodos que nos interesan usando restTemplate para comunicarnos via
    URL con las demas APIS
     */



    // inyectando dependencia importante para que funcionen en conjunto los microservicios con restTemplate
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;


    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    public Usuario getUsuarioById(int id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario save(Usuario usuario) {
        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        return nuevoUsuario;
    }

    // metodos restTemplate :

    // este metodo va a obtener los carros de un usuario mediante la API de "carro-service".
    // este metodo nos ayudara a obtenerlos y devolverlos mediante el metodo listarCarros en UsuarioController (UMPA LUMPA)
    public List<Carro> getCarros(int usuarioId) {
        List<Carro> carros = restTemplate.getForObject("http://localhost:8002/carro/usuario/" + usuarioId, List.class);
        return carros;
    }

    public List<Moto> getMotos(int usuarioId) {
        List<Moto> motos = restTemplate.getForObject("http://localhost:8003/moto/usuario/" + usuarioId, List.class);
        return motos;
    }

// metodos feign client :

    // primero hay que inyectar FeignClient :

    @Autowired
    private CarroFeignClient carroFeignClient;

    @Autowired
    private MotoFeignClient motoFeignClient;


    public Carro saveCarro(int usuarioId , Carro carro){
        carro.setUsuarioId(usuarioId);
        Carro nuevoCarro = carroFeignClient.save(carro);
        return nuevoCarro;
    }

    public List<Carro> listarCarros(int usuarioId){
        return carroFeignClient.listarCarros(usuarioId);
    }


    public Moto guardarMoto(Moto moto , int usuarioId){
        moto.setUsuarioId(usuarioId);
        return motoFeignClient.saveMoto(moto);
    }

    // en el siguiente diccionario devolvere al usuario, sus motos y carros.
    public Map<String, Object> getUsuarioAndVehiculo(int usuarioId){

        Map<String, Object> result = new HashMap<>();

        // primero consulto si el usuario existe, en caso de que NO , devuelvo un diccionario que dé como mensaje eso.

        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(null);

        if( usuario == null){
            result.put("Mensaje" , "el usuario con id " + usuarioId +" no existe jeje");
            return result;
        }

        // ahora inserto en el dicc los carros, si no tiene alguno, lo doy como mensaje.

        List<Carro> carros = carroFeignClient.listarCarros(usuarioId);

        if( carros.isEmpty() ){
            result.put("Carros" , "El usuario con id " + usuarioId + " no tiene carros");
        }

        result.put( "Carros" , carros);

        // ahora inserto en el dicc un par clave/valor donde el valor sera la lista de Motos

        List<Moto> motos = motoFeignClient.listarMotos(usuarioId);

        if( motos.isEmpty() ){
            result.put( "Motos" , "Este usuario no tiene motos :( ");
        }

        result.put( "Motos" , motos);

        return result;

    }


}
