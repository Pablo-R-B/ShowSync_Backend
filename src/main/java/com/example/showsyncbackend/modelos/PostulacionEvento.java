package com.example.showsyncbackend.modelos;

import com.example.showsyncbackend.enumerados.EstadoPostulacion;
import com.example.showsyncbackend.enumerados.TipoSolicitud;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="postulaciones_eventos", schema = "showsync", catalog = "postgres")
/*Este modelo sirve para las solicitudes de colaboración entre un promotor y un artista
 * y para las postulaciones de artistas a un evento**/
public class PostulacionEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Eventos evento;

    @ManyToOne
    @JoinColumn(name = "artista_id", nullable = false)
    private Artistas artista;

    @Column(name = "estado")
    @Enumerated(EnumType.STRING)
    private EstadoPostulacion estadoPostulacion;

    @Column(name = "tipo_postulacion")
    @Enumerated(EnumType.STRING)
    private TipoSolicitud tipoSolicitud;
    //Diferencia entre postulacion (de artista a evento) u oferta (de promotor a artista)

    @Column(name = "fecha_postulacion", nullable = false, updatable = false)
    private LocalDate fechaPostulacion = LocalDate.now();

    @Column(name = "fecha_respuesta")
    private LocalDate fechaRespuesta;

}
