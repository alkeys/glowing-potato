package entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "vacunas")
public class Vacuna {
    @Id
    @Column(name = "id_vacuna", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "tipo_vacuna", nullable = false, length = 100)
    private String tipoVacuna;

    @Size(max = 50)
    @Column(name = "lote", length = 50)
    private String lote;

    @NotNull
    @Column(name = "fecha_aplicacion", nullable = false)
    private LocalDate fechaAplicacion;

    @Column(name = "proxima_dosis")
    private LocalDate proximaDosis;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_mascota")
    @JsonBackReference("mascota-vacunas")
    private Mascota idMascota;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "id_veterinario")
    @JsonManagedReference("usuario-vacunas") // ✅ corregido
    private Usuario idVeterinario;




}