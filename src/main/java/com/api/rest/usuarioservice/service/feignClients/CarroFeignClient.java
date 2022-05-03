package com.api.rest.usuarioservice.service.feignClients;

import com.api.rest.usuarioservice.service.modelos.Carro;
import com.api.rest.usuarioservice.service.modelos.Moto;
import feign.Client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient( name = "carro-service" , url = "http://localhost:8002")
@RequestMapping("/carro")
public interface CarroFeignClient {

    @PostMapping
    Carro save(@RequestBody Carro carro);


    @GetMapping("/usuario/{usuarioId}")
    List<Carro> listarCarros(@PathVariable("usuarioId") int usuarioId);


}
