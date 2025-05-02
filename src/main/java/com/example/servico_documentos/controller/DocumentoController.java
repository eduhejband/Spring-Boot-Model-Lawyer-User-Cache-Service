package com.example.servico_documentos.controller;

import com.example.servico_documentos.model.Documento;
import com.example.servico_documentos.service.DocumentoService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/documentos")
public class DocumentoController {

    private final DocumentoService documentoService;

    public DocumentoController(DocumentoService documentoService) {
        this.documentoService = documentoService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Documento> uploadDocumento(
            @RequestParam Long clienteId,
            @RequestParam MultipartFile arquivo,
            @RequestParam String categoria) {

        Documento doc = documentoService.armazenarDocumento(clienteId, arquivo, categoria);
        return ResponseEntity.ok(doc);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> downloadDocumento(@PathVariable String id) {
        byte[] conteudo = documentoService.recuperarDocumento(id); // Agora o método existe
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(conteudo);
    }
}