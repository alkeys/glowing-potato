package entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Getter
@Setter
@Entity
@Table(name = "historial_medico")
public class HistorialMedico {
    @Id
    @Column(name = "id_historial", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_mascota")
    @JsonBackReference("mascota-historiales")
    private Mascota idMascota;

    @NotNull
    @ColumnDefault("CURRENT_DATE")
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "diagnostico", length = Integer.MAX_VALUE)
    private String diagnostico;

    @Column(name = "tratamiento", length = Integer.MAX_VALUE)
    private String tratamiento;

    @Column(name = "observaciones", length = Integer.MAX_VALUE)
    private String observaciones;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "id_veterinario")
    @JsonBackReference("usuario-historiales")
    private Usuario idVeterinario;



}