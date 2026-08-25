package com.transito.sistema.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration; 

@Configuration 
public class OpenApiConfig { 
    
    @Bean 
    public OpenAPI sistemaInfracoesOpenAPI() { 
        return new OpenAPI()
                .info(new Info() 
                        .title("Sistema de Gestão de Infrações de Trânsito")
                        .description( 
                            "API REST para gerenciamento de condutores, veículos, " +   "tipos de infração e infrações de trânsito." 
                        ) 
                        .version("1.0.0")); 
    } 
}