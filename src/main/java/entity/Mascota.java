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

import java.math.BigDecimal;
import java.util.List;
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Getter
@Setter
@Entity
@Table(name = "mascotas")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mascota", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Size(max = 50)
    @NotNull
    @Column(name = "especie", nullable = false, length = 50)
    private String especie;

    @Size(max = 50)
    @Column(name = "raza", length = 50)
    private String raza;

    @Column(name = "edad")
    private Integer edad;

    @Size(max = 10)
    @Column(name = "sexo", length = 10)
    private String sexo;

    @Size(max = 30)
    @Column(name = "color", length = 30)
    private String color;

    @Column(name = "peso", precision = 5, scale = 2)
    private BigDecimal peso;

    @Column(name = "observaciones", length = Integer.MAX_VALUE)
    private String observaciones;


    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_propietario")
    @JsonBackReference("propietario-mascotas")
    private Propietario idPropietario;


    @OneToMany(mappedBy = "idMascota", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "mascota-citas") // 👈 Este lado controla la serialización
    private List<Cita> citas;



    @OneToMany(mappedBy = "idMascota", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "mascota-vacunas")
    private List<Vacuna> vacunas;

    @OneToMany(mappedBy = "idMascota", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "mascota-historiales")
    private List<HistorialMedico> historialMedico;
}
