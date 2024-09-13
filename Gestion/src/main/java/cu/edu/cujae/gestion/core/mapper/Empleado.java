package cu.edu.cujae.gestion.core.mapper;

import cu.edu.cujae.gestion.core.dto.empleadoDtos.EmpleadoDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.Mergeable;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "personal")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "empleado_seq")
    @SequenceGenerator(name = "empleado_seq", sequenceName = "empleado_sequence",initialValue = 1,allocationSize = 1)
    @Column(name = "idpersonal", nullable = false, updatable = false, unique = true)
    private Long uuid;

    @NotBlank(message = "El nombre no puede estar vacío o estar compuesto solamente por espacios")
    @NotNull(message = "El nombre de la entidad no puede ser null")
    @Size(min = 3,max = 100,message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(name = "nombre",nullable = false,length = 100)
    private String nombre;

    @NotBlank(message = "El carnet de identidad no puede estar vacío o estar compuesto solamente por espacios")
    @NotNull(message = "El carnet de la entidad no puede ser null")
    @Size(min = 11,max = 11,message = "El carnet debe tener 11 caracteres")
    @Column(name = "ci",nullable = false, unique = true, length = 11)
    @Pattern(regexp = "^[0-9]{2}([0]?[0-9]|[1]?[0-2])([0-2]?[0-9]|[3]?[0-1])\\d{5}$"
            ,message = "No se cumple los valores del carnet de identidad")
    private String ci;

    @NotNull(message = "El municipio no puede ser nulo")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idmunicipio")
    public Municipio municipio;

    @NotNull(message = "La provincia no puede ser nulo")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idprovincia")
    public Provincia provincia;

    @NotBlank(message = "La calle principal del empleado no puede estar vacío o estar compuesto solamente por espacios")
    @NotNull(message = "La calle principal del empleado no puede ser null")
    @Column(name = "direccion",nullable = false)
    public String direccion;

    @Column(name = "datosAdicionales")
    private String datos;

    @Column(name = "numero")
    private String numero;

    @Column(name = "localidad")
    private String localidad;

    @Column(name = "zona_transporte")
    private Long zona_transporte;

    @Column(name = "areaSalud")
    private Long areaSalud;

    @Column(name = "distcentroasalud")
    private Double distcentroasalud;

    @Column(name = "distcentrozonat")
    private Double distcentrozonat;

    @Column(name = "distcentromunic")
    private Double distcentromunic;

    @Column(name = "distcentroprov")
    private Double distcentroprov;

    @Column(name = "geolocalizacion",columnDefinition = "geometry")
    private Geometry geometry;

    @ManyToMany(mappedBy = "personal",cascade = {CascadeType.MERGE,CascadeType.REFRESH,CascadeType.DETACH,CascadeType.REMOVE})
    private List<Entidad> entidades;

    public Empleado(EmpleadoDto empleadoDto, Municipio municipio, Provincia provincia){
        this.uuid = empleadoDto.getUuid();
        this.nombre = empleadoDto.getNombre();
        this.ci = empleadoDto.getCi();
        this.municipio = municipio;
        this.provincia = provincia;
        this.direccion = empleadoDto.getDireccion();
        this.datos = empleadoDto.getDatos();
    }

    public Empleado(EmpleadoDto empleadoDto, Municipio municipio, Provincia provincia,Entidad entidad){
        this.uuid = empleadoDto.getUuid();
        this.nombre = empleadoDto.getNombre();
        this.ci = empleadoDto.getCi();
        this.municipio = municipio;
        this.provincia = provincia;
        this.direccion = empleadoDto.getDireccion();
        this.datos = empleadoDto.getDatos();
        this.entidades = List.of(entidad);
    }

    @PreRemove
    public void preRemove(){
        this.getEntidades().clear();
    }
}
