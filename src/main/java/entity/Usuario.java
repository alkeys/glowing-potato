package entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false)
    private Integer id;



    @Size(max = 100)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Size(max = 100)
    @NotNull
    @Column(name = "correo", nullable = false, length = 100)
    private String correo;

    @Size(max = 255)
    @NotNull
    @Column(name = "contrasena", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE) // para permitir la escritura de la contraseña
    private String contrasena;

    @Size(max = 20)
    @NotNull
    @Column(name = "rol", nullable = false, length = 20)
    private String rol;

    // Relaciones: Veterinario con citas, historial y vacunas

    @OneToMany(mappedBy = "idVeterinario")
    @JsonBackReference(value = "usuario-citas")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Cita> citas;

    @OneToMany(mappedBy = "idVeterinario")
    @JsonBackReference("usuario-historiales")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<HistorialMedico> historiales;

    @OneToMany(mappedBy = "idVeterinario")
    @JsonBackReference("usuario-vacunas") // ✅ corregido
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Vacuna> vacunas;


}
