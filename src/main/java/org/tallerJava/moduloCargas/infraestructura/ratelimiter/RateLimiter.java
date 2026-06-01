package org.tallerJava.moduloCargas.infraestructura.ratelimiter;

import java.time.Duration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RateLimiter {

    private Bucket bucket;
    private boolean activo;

    @PostConstruct
    public void inicializar() {
        activo = true;

        Bandwidth bucketConf = Bandwidth.builder()
                .capacity(10)
                .refillGreedy(5, Duration.ofSeconds(1))
                .build();

        bucket = Bucket.builder().addLimit(bucketConf).build();
    }

    public boolean consumir() {
        boolean result = bucket.tryConsume(1);
        System.out.println("Tokens restantes: " + bucket.getAvailableTokens());
        return result;
    }

}