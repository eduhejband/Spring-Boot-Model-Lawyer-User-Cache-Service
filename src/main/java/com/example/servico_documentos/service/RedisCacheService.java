package com.example.servico_documentos.service;

import com.example.servico_documentos.model.Documento;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheService {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheDocumento(String key, Object documento) {
        redisTemplate.opsForValue().set(key, documento, 1, TimeUnit.HOURS);
    }

    public <T> T getCachedDocumento(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    public void removeCachedDocumento(String key) {
        redisTemplate.delete(key);
    }

    public void cacheClientCategories(Long clienteId, List<String> categorias) {
        redisTemplate.opsForValue().set(
                "cliente:" + clienteId + ":categorias",
                categorias,
                24,
                TimeUnit.HOURS
        );
    }

    public List<String> getCachedClientCategories(Long clienteId) {
        return (List<String>) redisTemplate.opsForValue().get("cliente:" + clienteId + ":categorias");
    }

    public void cacheDocumentosPorCliente(Long clienteId, List<Documento> documentos) {
        redisTemplate.opsForValue().set(
                "cliente:" + clienteId + ":documentos",
                documentos,
                1,
                TimeUnit.HOURS
        );
    }

    public List<Documento> getCachedDocumentosPorCliente(Long clienteId) {
        return (List<Documento>) redisTemplate.opsForValue().get("cliente:" + clienteId + ":documentos");
    }

    public void cacheDocumentosPorClienteECategoria(String cacheKey, List<Documento> documentos) {
        redisTemplate.opsForValue().set(
                cacheKey,
                documentos,
                1,
                TimeUnit.HOURS
        );
    }

    public List<Documento> getCachedDocumentosPorClienteECategoria(String cacheKey) {
        return (List<Documento>) redisTemplate.opsForValue().get(cacheKey);
    }
}