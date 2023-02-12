package orgsid.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
//Methode 1 : Statique
//    @Bean
//    RouteLocator routeLocator(RouteLocatorBuilder builder){
//        return builder.routes()
//                .route((r)->r.path("/etudiants/**").uri("http://localhost:8084/"))
//                .route((r)->r.path("/modules/**").uri("http://localhost:8084/"))
//                .build();
//    }


//    Methode 2 : Statique
//@Bean
//RouteLocator routeLocator(RouteLocatorBuilder builder){
//    return builder.routes()
//            .route((r)->r.path("/etudiants/**").uri("lb://INSCRIPTION-PEDAGOGIQUE-SERVICE").id("r1"))
//            .route((r)->r.path("/modules/**").uri("lb://MODULE-SERVICE").id("r2"))
//            .build();
//}
//Methode 3 : Dynamique
    @Bean
    DiscoveryClientRouteDefinitionLocator definitionLocator(
            ReactiveDiscoveryClient rdc,
            DiscoveryLocatorProperties properties){
        return new DiscoveryClientRouteDefinitionLocator(rdc,properties);
    }

}
