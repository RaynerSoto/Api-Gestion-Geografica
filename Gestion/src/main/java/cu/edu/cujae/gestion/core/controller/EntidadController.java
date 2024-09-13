package cu.edu.cujae.gestion.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cu.edu.cujae.gestion.core.dto.EntidadDto;
import cu.edu.cujae.gestion.core.dto.TokenDto;
import cu.edu.cujae.gestion.core.dto.UsuarioDto;
import cu.edu.cujae.gestion.core.feignclient.TokenServiceInterfaces;
import cu.edu.cujae.gestion.core.mapper.Empleado;
import cu.edu.cujae.gestion.core.utils.IpUtils;
import cu.edu.cujae.gestion.core.utils.RegistroUtils;
import cu.edu.cujae.gestion.core.utils.TokenUtils;
import cu.edu.cujae.gestion.core.services.servicesImpl.RegistroService;
import cu.edu.cujae.gestion.core.services.EntidadServicesInterfaces;
import cu.edu.cujae.gestion.core.services.MunicipioServicesInterfaces;
import cu.edu.cujae.gestion.core.services.ProvinciaServiceInterfaces;
import cu.edu.cujae.gestion.core.services.servicesIntern.EntidadServicesIntern;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Totalmente probada
@RestController
@RequestMapping(value = "/api/v1/gestion/entidad",name = "Controller para las entidades")
@Tag(name = "Controllador de la entidad"
        ,description = "Es el responsable de controllar todo lo referente con las entidades del sistema")
@SecurityRequirement(name = "bearer-key")
public class EntidadController {

    private final EntidadServicesInterfaces entidadServices;
    private final ProvinciaServiceInterfaces provinciaServices;
    private final MunicipioServicesInterfaces municipioServices;
    private final RegistroService registroService;
    private final RegistroUtils registroUtils;
    private final TokenServiceInterfaces tokenService;
    private final EntidadServicesIntern entidadServicesIntern;
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public EntidadController(EntidadServicesInterfaces entidadServices, ProvinciaServiceInterfaces provinciaServices, MunicipioServicesInterfaces municipioServices, RegistroService registroService, RegistroUtils registroUtils, TokenServiceInterfaces tokenService, EntidadServicesIntern entidadServicesIntern) {
        this.entidadServices = entidadServices;
        this.provinciaServices = provinciaServices;
        this.municipioServices = municipioServices;
        this.registroService = registroService;
        this.registroUtils = registroUtils;
        this.tokenService = tokenService;
        this.entidadServicesIntern = entidadServicesIntern;
    }

    @GetMapping("/")
    @Operation(description = "Permite listar todas las entidades del sistema",
    summary = "Listar Entidades",security = { @SecurityRequirement(name = "bearer-key") })
    @PreAuthorize(value = "hasAnyRole('Super Administrador','Administrador','Gestor')")
    public ResponseEntity<?> listadoEntidades(HttpServletRequest request){
        String actividad = "Listar todas las entidades del sistema";
        TokenDto tokenDto = TokenUtils.getTokenDto(request);
        try {
            List<EntidadDto> entidades = entidadServicesIntern.obtenerListadoEntidadDto();
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad, IpUtils.hostIpV4Http(request),"Aceptado",null);
            return ResponseEntity.ok(entidades);
        }catch (Exception e){
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,IpUtils.hostIpV4Http(request),"Rechazado",e.getMessage());
            return ResponseEntity.badRequest().body("No se ha podido obtener el listado de entidades, compruebe su conexiòn a la base de datos o contacto con el servicio tècnico");
        }
    }

    @PostMapping("/")
    @Operation(summary = "Insertar Entidad",
    description = "Permite insertar una entidad",security = { @SecurityRequirement(name = "bearer-key") })
    @PreAuthorize(value = "hasAnyRole('Super Administrador','Administrador','Gestor')")
    public ResponseEntity<?> insertarEntidad(@RequestBody EntidadDto entidad,HttpServletRequest request){
        String actividad = "Insertar una entidad en el sistema";
        TokenDto tokenDto = TokenUtils.getTokenDto(request);
        try {
            entidadServicesIntern.insertarEntidad(entidad);
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,IpUtils.hostIpV4Http(request),"Aceptado",null);
            return ResponseEntity.ok("Entidad insertada");
        }catch (Exception e){
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,IpUtils.hostIpV4Http(request),"Rechazado",e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar entidad",
    description = "Permite modificar una entidad a través de su ID",security = { @SecurityRequirement(name = "bearer-key") })
    @PreAuthorize(value = "hasAnyRole('Super Administrador','Administrador','Gestor')")
    public ResponseEntity<?> modificarEntidad(@RequestBody EntidadDto entidad,@PathVariable Long id,HttpServletRequest request){
        String actividad = "Modificar una entidad a travès de su ID";
        TokenDto tokenDto = TokenUtils.getTokenDto(request);
        try {
            entidadServicesIntern.modificarEntidad(entidad,id);
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,IpUtils.hostIpV4Http(request),"Aceptado",null);
            return ResponseEntity.ok("Entidad modificada con éxito");
        }catch (Exception e){
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,IpUtils.hostIpV4Http(request),"Rechazado",e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar entidad",
    description = "Permite eliminar entidades a través de su ID",security = { @SecurityRequirement(name = "bearer-key") })
    @PreAuthorize(value = "hasAnyRole('Super Administrador','Administrador','Gestor')")
    public ResponseEntity<?> eliminarEntidad(@PathVariable Long id,HttpServletRequest request){
        String actividad = "Eliminar una entidad del sistema por su ID";
        TokenDto tokenDto = TokenUtils.getTokenDto(request);
        try {
            entidadServices.eliminarEntidad(id);
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,IpUtils.hostIpV4Http(request),"Aceptado",null);
            return ResponseEntity.ok("Entidad eliminada con éxito");
        }catch (Exception e){
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,IpUtils.hostIpV4Http(request),"Rechazado",e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
