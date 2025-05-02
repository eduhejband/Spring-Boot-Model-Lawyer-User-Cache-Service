package com.example.servico_documentos.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import java.io.Serializable;
import java.time.LocalDateTime;

@RedisHash("Documento")
public class Documento implements Serializable {
    @Id
    private String id;
    private Long clienteId;
    private String nome;
    private String categoria;
    private LocalDateTime dataUpload;
    private String urlArquivo;
    private String contentType;
    private Long tamanho;

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public LocalDateTime getDataUpload() { return dataUpload; }
    public void setDataUpload(LocalDateTime dataUpload) { this.dataUpload = dataUpload; }
    public String getUrlArquivo() { return urlArquivo; }
    public void setUrlArquivo(String urlArquivo) { this.urlArquivo = urlArquivo; }
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public Long getTamanho() { return tamanho; }
    public void setTamanho(Long tamanho) { this.tamanho = tamanho; }
}