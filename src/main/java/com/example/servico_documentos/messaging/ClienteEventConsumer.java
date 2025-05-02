package com.example.servico_documentos.messaging;

import com.example.servico_documentos.config.RabbitMQConfig;
import com.example.servico_documentos.messaging.events.ClienteCriadoEvent;
import com.example.servico_documentos.model.Documento;
import com.example.servico_documentos.repository.DocumentoRepository;
import com.example.servico_documentos.service.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class ClienteEventConsumer {
    private static final Logger logger = LoggerFactory.getLogger(ClienteEventConsumer.class);

    private final DocumentoRepository documentoRepository;
    private final RedisCacheService cacheService;

    private static final List<String> CATEGORIAS_PADRAO = Arrays.asList(
            "Contrato",
            "Documento Pessoal",
            "Procuração"
    );

    public ClienteEventConsumer(DocumentoRepository documentoRepository,
                                RedisCacheService cacheService) {
        this.documentoRepository = documentoRepository;
        this.cacheService = cacheService;
    }

    @RabbitListener(queues = RabbitMQConfig.CLIENTE_QUEUE)
    public void handleClienteCriado(ClienteCriadoEvent event) {
        logger.info("Processando evento de cliente criado: ID {}", event.id());

        try {
            // 1. Criar documento inicial
            Documento doc = new Documento();
            doc.setClienteId(event.id());
            doc.setNome("Termo de Adesão");
            doc.setCategoria("Contrato");
            doc.setDataUpload(LocalDateTime.now());
            doc.setUrlArquivo("system://modelos/termo-adesao");
            doc.setContentType("application/pdf");
            doc.setTamanho(0L);

            documentoRepository.save(doc);

            // 2. Cache no Redis
            cacheService.cacheDocumento("documento:" + doc.getId(), doc);
            cacheService.cacheClientCategories(event.id(), CATEGORIAS_PADRAO);

            logger.info("Estrutura de documentos criada para cliente ID {}", event.id());
        } catch (Exception e) {
            logger.error("Erro ao processar evento de cliente criado: ID {}", event.id(), e);
        }
    }
}