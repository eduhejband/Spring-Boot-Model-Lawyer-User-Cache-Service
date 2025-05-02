package com.example.servico_documentos.repository;

import com.example.servico_documentos.model.Documento;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DocumentoRepository extends CrudRepository<Documento, String> {
    List<Documento> findByClienteId(Long clienteId);
    List<Documento> findByClienteIdAndCategoria(Long clienteId, String categoria);
}