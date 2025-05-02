// src/main/java/com/example/servico_documentos/messaging/events/ClienteCriadoEvent.java
package com.example.servico_documentos.messaging.events;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ClienteCriadoEvent(
        Long id,
        String nome,
        String email,
        Long advogadoId,
        LocalDateTime createdAt
) implements Serializable {}