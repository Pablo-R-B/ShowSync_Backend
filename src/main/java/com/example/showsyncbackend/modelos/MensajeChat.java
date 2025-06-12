package com.example.showsyncbackend.modelos;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant; // ¡CRÍTICO! CAMBIO AQUÍ: Usamos Instant
// Asegúrate de que NO haya 'import java.time.LocalDateTime;' aquí

@Entity
@Table(name = "mensajes_chat")
@Data // Lombok generará automáticamente getters y setters para todas las propiedades
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MensajeChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Considera FetchType.LAZY si no siempre necesitas la conversación cargada
    @JoinColumn(name = "id_conversacion", nullable = false)
    private Conversacion conversacion;

    @Column(name = "id_remitente") // Quitamos nullable = false si no siempre se proporciona este ID desde el frontend
    private Long remitenteId;

    @Column(name = "nombre_remitente", nullable = false) // ¡CRÍTICO! Esta columna debe existir en la DB
    private String remitente;

    @Column(name = "rol_remitente", nullable = false) // ¡CRÍTICO! Esta columna debe existir en la DB
    private String tipo;

    @Column(columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "imagen_remitente_url")
    private String imagenRemitenteUrl;

    @Column(name = "fecha_envio", nullable = false)
    private Instant fechaEnvio; // ¡CRÍTICO! CAMBIO AQUÍ: Ahora es Instant
}
