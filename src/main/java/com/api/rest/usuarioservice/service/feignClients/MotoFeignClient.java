package com.api.rest.usuarioservice.service.feignClients;

import com.api.rest.usuarioservice.service.modelos.Moto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//nombre del microservicio y url del micro servicio
@FeignClient( name = "moto-service" , url = "http://localhost:8003")
@RequestMapping("/moto")
public interface MotoFeignClient {



    @PostMapping
    Moto saveMoto(@RequestBody Moto moto);

    @GetMapping("/usuario/{usuarioId}")
    List<Moto> listarMotos(@PathVariable("usuarioId") int usuarioId);




}
