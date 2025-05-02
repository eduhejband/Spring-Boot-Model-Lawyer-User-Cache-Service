package com.example.servico_documentos.service;

import com.example.servico_documentos.model.Documento;
import com.example.servico_documentos.repository.DocumentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentoService {
    private final DocumentoRepository documentoRepository;
    private final StorageService storageService;
    private final RedisCacheService redisCacheService;

    public DocumentoService(DocumentoRepository documentoRepository,
                            StorageService storageService,
                            RedisCacheService redisCacheService) {
        this.documentoRepository = documentoRepository;
        this.storageService = storageService;
        this.redisCacheService = redisCacheService;
    }

    public Documento armazenarDocumento(Long clienteId, MultipartFile arquivo, String categoria) {
        try {
            String nomeArquivo = arquivo.getOriginalFilename();
            String objectName = storageService.uploadFile(arquivo, nomeArquivo);

            Documento documento = new Documento();
            documento.setClienteId(clienteId);
            documento.setNome(nomeArquivo);
            documento.setCategoria(categoria);
            documento.setDataUpload(LocalDateTime.now());
            documento.setUrlArquivo(objectName);
            documento.setContentType(arquivo.getContentType());
            documento.setTamanho(arquivo.getSize());

            redisCacheService.cacheDocumento("documento:" + documento.getId(), documento);
            return documentoRepository.save(documento);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao armazenar documento", e);
        }
    }

    public byte[] recuperarDocumento(String documentoId) {
        Documento documento = redisCacheService.getCachedDocumento("documento:" + documentoId);

        if (documento == null) {
            documento = documentoRepository.findById(documentoId)
                    .orElseThrow(() -> new RuntimeException("Documento não encontrado"));
            redisCacheService.cacheDocumento("documento:" + documentoId, documento);
        }

        try {
            return storageService.downloadFile(documento.getUrlArquivo());
        } catch (IOException e) {
            throw new RuntimeException("Falha ao baixar documento", e);
        }
    }

    public void deletarDocumento(String documentoId) {
        Documento documento = documentoRepository.findById(documentoId)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado"));

        try {
            storageService.deleteFile(documento.getUrlArquivo());
            documentoRepository.delete(documento);
            redisCacheService.removeCachedDocumento("documento:" + documentoId);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao deletar documento", e);
        }
    }

    public List<Documento> listarDocumentosPorCliente(Long clienteId) {
        List<Documento> documentos = redisCacheService.getCachedDocumentosPorCliente(clienteId);
        if (documentos == null) {
            documentos = documentoRepository.findByClienteId(clienteId);
            redisCacheService.cacheDocumentosPorCliente(clienteId, documentos);
        }
        return documentos;
    }

    public List<Documento> listarDocumentosPorClienteECategoria(Long clienteId, String categoria) {
        String cacheKey = "cliente:" + clienteId + ":categoria:" + categoria;
        List<Documento> documentos = redisCacheService.getCachedDocumentosPorClienteECategoria(cacheKey);
        if (documentos == null) {
            documentos = documentoRepository.findByClienteIdAndCategoria(clienteId, categoria);
            redisCacheService.cacheDocumentosPorClienteECategoria(cacheKey, documentos);
        }
        return documentos;
    }
}