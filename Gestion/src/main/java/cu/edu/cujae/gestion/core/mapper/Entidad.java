package cu.edu.cujae.gestion.core.mapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cu.edu.cujae.gestion.core.dto.EntidadDto;
import cu.edu.cujae.gestion.core.utils.Validacion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "entidades")
public class Entidad {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "entidad_seq")
    @SequenceGenerator(name = "entidad_seq", sequenceName = "entidad_sequence",initialValue = 1,allocationSize = 1)
    @NotNull(message = "El id de la entidad no puede ser nulo")
    @Column(name = "identidad", nullable = false, updatable = false, unique = true)
    private Long uuid;

    @NotBlank(message = "El nombre no puede estar vacío o estar compuesto solamente por espacios")
    @NotNull(message = "El nombre de la entidad no puede ser null")
    @Size(min = 3,max = 1000,message = "El nombre debe tener entre 3 y 1000 caracteres")
    @Column(name = "nombre",nullable = false, unique = true,length = 100)
    private String nombre;

    @Column(name = "entidadmadre",length = 100)
    private String entidadMadre;

    @NotNull(message = "El municipio no puede ser nulo")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idmunicipio",nullable = false)
    private Municipio municipio;

    @NotNull(message = "La provincia no puede ser nulo")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idprovincia",nullable = false)
    private Provincia provincia;

    @NotBlank(message = "La calle principal de la entidad no puede estar vacío o estar compuesto solamente por espacios")
    @NotNull(message = "La calle principal de la entidad no puede ser null")
    @Column(name = "direccion",nullable = false)
    private String direccion;

    @Column(name = "numero")
    private String numeroCasa;

    @Column(name = "localidad")
    private String localidad;

    @Column(name = "horario_entrada")
    @Temporal(TemporalType.TIME)
    private Time horario_entrada;

    @Column(name = "horario_salida")
    @Temporal(TemporalType.TIME)
    private Time horario_salida;

    @Column(name = "horario_propuesto_entrada")
    @Temporal(TemporalType.TIME)
    private Time horario_propuesto_entrada;

    @Column(name = "horario_propuesto_salida")
    @Temporal(TemporalType.TIME)
    private Time horario_propuesto_salida;

    @Column(name = "datosAdicionales")
    private String datos;

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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.DETACH,CascadeType.REMOVE},fetch = FetchType.LAZY)
    @JoinTable(name = "entidadpersonal"
            ,joinColumns = @JoinColumn(name = "identidad")
            ,inverseJoinColumns = @JoinColumn(name = "idpersonal")
            ,uniqueConstraints = @UniqueConstraint(columnNames = {"identidad", "idpersonal"}))
    @JsonIgnore
    private List<Empleado> personal = new ArrayList<>();

    public Entidad(EntidadDto entidadDto, Municipio municipio, Provincia provincia){
        this.uuid = entidadDto.getUuid();
        this.nombre = entidadDto.getNombre();
        this.entidadMadre = entidadDto.getEntidadMadre();
        this.provincia = provincia;
        this.municipio = municipio;
        this.direccion = entidadDto.getDireccion();
        this.numeroCasa = entidadDto.getNumeroCasa();
        this.localidad = entidadDto.getLocalidad();
        this.horario_entrada = entidadDto.getHorario_entrada();
        this.horario_salida = entidadDto.getHorario_salida();
        this.horario_propuesto_salida = entidadDto.getHorario_propuesto_salida();
        this.horario_propuesto_entrada = entidadDto.getHorario_propuesto_entrada();
        this.datos = entidadDto.getDatos();
    }

    @PrePersist
    public void prePersist(){
        Validacion.validarUnsupportedOperationException(this);
    }
}
