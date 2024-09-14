package cu.edu.cujae.gestion.core.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import cu.edu.cujae.gestion.core.dto.TokenDto;
import cu.edu.cujae.gestion.core.dto.UsuarioDto;
import cu.edu.cujae.gestion.core.dto.empleadoDtos.EmpleadoDto;
import cu.edu.cujae.gestion.core.dto.empleadoDtos.EmpleadoDtoInsert;
import cu.edu.cujae.gestion.core.dto.empleadoDtos.EmpleadoDtoRegular;
import cu.edu.cujae.gestion.core.feignclient.TokenServiceInterfaces;
import cu.edu.cujae.gestion.core.mapper.Empleado;
import cu.edu.cujae.gestion.core.mapper.Entidad;
import cu.edu.cujae.gestion.core.utils.IpUtils;
import cu.edu.cujae.gestion.core.utils.RegistroUtils;
import cu.edu.cujae.gestion.core.utils.TokenUtils;
import cu.edu.cujae.gestion.core.services.servicesImpl.RegistroService;
import cu.edu.cujae.gestion.core.services.EmpleadoServiceInterfaces;
import cu.edu.cujae.gestion.core.services.EntidadServicesInterfaces;
import cu.edu.cujae.gestion.core.services.MunicipioServicesInterfaces;
import cu.edu.cujae.gestion.core.services.ProvinciaServiceInterfaces;
import cu.edu.cujae.gestion.core.services.servicesIntern.EmpleadoServicesIntern;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gestion/empleado")
@Tag(name = "Controlador de empleados",
        description = "Controlador encargardo de todo lo referente con los empleados del sistema")
@SecurityRequirement(name = "bearer-key")
public class EmpleadoController {

    private final EmpleadoServiceInterfaces empleadoService;
    private final MunicipioServicesInterfaces municipioService;
    private final ProvinciaServiceInterfaces provinciaService;
    private final EntidadServicesInterfaces entidadService;
    private final RegistroService registroService;
    private final RegistroUtils registroUtils;
    private final TokenServiceInterfaces tokenService;
    private final EmpleadoServicesIntern empleadoServicesIntern;
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public EmpleadoController(EmpleadoServiceInterfaces empleadoService, MunicipioServicesInterfaces municipioService, ProvinciaServiceInterfaces provinciaService, EntidadServicesInterfaces entidadService, RegistroService registroService, RegistroUtils registroUtils, TokenServiceInterfaces tokenService, EmpleadoServicesIntern empleadoServicesIntern) {
        this.empleadoService = empleadoService;
        this.municipioService = municipioService;
        this.provinciaService = provinciaService;
        this.entidadService = entidadService;
        this.registroService = registroService;
        this.registroUtils = registroUtils;
        this.tokenService = tokenService;
        this.empleadoServicesIntern = empleadoServicesIntern;
    }

    @GetMapping("/")
    @Operation(summary = "Listado de empleados",
            description = "Permite listar todos los empleados del sistema junto con sus centros laborales",
            security = { @SecurityRequirement(name = "bearer-key") })
    @PreAuthorize(value = "hasAnyRole('Super Administrador','Administrador','Gestor')")
    public ResponseEntity<?> listarEmpleados(HttpServletRequest request){
        String actividad = "Listado de empleados del sistema junto con sus centros laborales";
        TokenDto tokenDto = TokenUtils.getTokenDto(request);
        try {
            List<EmpleadoDtoRegular> empleados = empleadoServicesIntern.listadoEmpleadoDtoRegular();
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,request.getRemoteHost(),"Aceptado",null);
            return ResponseEntity.ok(empleados);
        }catch (Exception e){
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,request.getRemoteHost(),"Rechazado",e.getMessage());
            return ResponseEntity.badRequest().body("No se puede acceder a la lista de empleados del sistema. Código de error: "+e.getMessage());
        }
    }

    @PostMapping("/")
    @Operation(summary = "Insertar empleado sin trabajo",
    description = "Permite insertar un empleado que no está afiliado a un centro laboral",
            security = { @SecurityRequirement(name = "bearer-key") })
    @PreAuthorize(value = "hasAnyRole('Super Administrador','Administrador','Gestor')")
    public ResponseEntity<?> insertarEmpleadoSinCompañia(@RequestBody EmpleadoDto empleadoDto,HttpServletRequest request){
        String actividad = "Insertar un empleado que no está afiliado a un centro laboral";
        TokenDto tokenDto = TokenUtils.getTokenDto(request);
        try {
            empleadoServicesIntern.insertarEmpleadoSinTrabajo(empleadoDto);
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad, IpUtils.hostIpV4Http(request),"Aceptado",null);
            return ResponseEntity.ok("Usuario insertado con éxito");
        }catch (Exception e){
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,IpUtils.hostIpV4Http(request),"Rechazado",e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/compannia")
    @Operation(summary = "Insertar empleado con trabajo",
    description = "Permite insertar un empleado que está afiliado a un centro laboral",
            security = { @SecurityRequirement(name = "bearer-key") })
    @PreAuthorize(value = "hasAnyRole('Super Administrador','Administrador','Gestor')")
    public ResponseEntity<?> insertarEmpleadoConCompañia(@RequestBody EmpleadoDtoInsert empleadoDto,HttpServletRequest request){
        String actividad = "Insertar un empleado que está afiliado a un centro laboral";
        TokenDto tokenDto = TokenUtils.getTokenDto(request);
        try {
            empleadoServicesIntern.insertarEmpleadoConTrabajo(empleadoDto);
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,IpUtils.hostIpV4Http(request),"Aceptado",null);
            return ResponseEntity.ok("Usuario insertado con éxito");
        }catch (Exception e){
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,IpUtils.hostIpV4Http(request),"Rechazado",e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/ci/{ci}")
    @Operation(summary = "Obtener empleado por CI",
    description = "Permite obtener un empleado a raíz de su carnet de identidad",security = { @SecurityRequirement(name = "bearer-key") })
    @PreAuthorize(value = "hasAnyRole('Super Administrador','Administrador','Gestor')")
    public ResponseEntity<?> obtenerEmpleadosPorCi(@PathVariable String ci,HttpServletRequest request){
        String actividad = "Obtener un empleado a raìz del CI";
        TokenDto tokenDto = TokenUtils.getTokenDto(request);
        try {
            EmpleadoDtoRegular empleado = new EmpleadoDtoRegular(empleadoService.obtenerEmpleadoXCi(ci).get());
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,IpUtils.hostIpV4Http(request),"Aceptado",null);
            return ResponseEntity.ok(empleado);
        }catch (Exception e){
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,IpUtils.hostIpV4Http(request),"Rechazado",e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PatchMapping("/id/{id}")
    @Operation(summary = "Obtener empleado por ID",
            description = "Permite obtener un empleado a raíz de su ID",
            security = { @SecurityRequirement(name = "bearer-key") })
    @PreAuthorize(value = "hasAnyRole('Super Administrador','Administrador','Gestor')")
    public ResponseEntity<?> obtenerEmpleadosPorId(@PathVariable Long id,HttpServletRequest request){
        String actividad = "Obtener un empleado a raìz de su ID";
        TokenDto tokenDto = TokenUtils.getTokenDto(request);
        try {
            EmpleadoDtoRegular empleado = new EmpleadoDtoRegular(empleadoService.obtenerEmpleadoXId(id).get());
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,IpUtils.hostIpV4Http(request),"Aceptado",null);
            return ResponseEntity.ok(empleado);
        }catch (Exception e){
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,IpUtils.hostIpV4Http(request),"Rechazado",e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar empleado por ID",
            description = "Permite modificar un empleado a raíz de su ID",security = { @SecurityRequirement(name = "bearer-key") })
    @PreAuthorize(value = "hasAnyRole('Super Administrador','Administrador','Gestor')")
    public ResponseEntity<?> modificarEmpleadoXId(@PathVariable Long id,@RequestBody EmpleadoDto empleadoDto,HttpServletRequest request){
        String actividad = "Modificar un empleado a raìz de su ID";
        TokenDto tokenDto = TokenUtils.getTokenDto(request);
        try {
            //Modificar empleado
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,IpUtils.hostIpV4Http(request),"Aceptado",null);
            return ResponseEntity.ok("Usuario insertado con éxito");
        }catch (Exception e){
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,IpUtils.hostIpV4Http(request),"Rechazado",e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/id/{id}")
    @Operation(summary = "Eliminar empleado por ID",
            description = "Permite eliminar un empleado a raíz de su ID",security = { @SecurityRequirement(name = "bearer-key") })
    @PreAuthorize(value = "hasAnyRole('Super Administrador','Administrador','Gestor')")
    public ResponseEntity<?> eliminarEmpleadoXId(@PathVariable Long id,HttpServletRequest request){
        String actividad = "Eliminar un empleado a raíz de su ID";
        TokenDto tokenDto = TokenUtils.getTokenDto(request);
        try {
            Empleado empleado = empleadoService.obtenerEmpleadoXId(id).get();
            List<Entidad> entidads = empleado.getEntidades();
            for (Entidad entidad: entidads){
                for (int contador = 0; contador<entidad.getPersonal().size();contador++){
                    if (entidad.getPersonal().get(contador).getUuid() == id)
                        entidad.getPersonal().remove(contador);
                }
                entidadService.modificarEntidad(entidad);
            }
            empleadoService.eliminarEmpleado(id);
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,IpUtils.hostIpV4Http(request),"Aceptado",null);
            return ResponseEntity.ok("Empleado eliminado");
        }catch (Exception e){
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,IpUtils.hostIpV4Http(request),"Rechazado",e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/ci/{ci}")
    @Operation(summary = "Eliminar empleado por CI",
            description = "Permite eliminar un empleado a raíz de su carnet de identidad",security = { @SecurityRequirement(name = "bearer-key") })
    @PreAuthorize(value = "hasAnyRole('Super Administrador','Administrador','Gestor')")
    public ResponseEntity<?> eliminarEmpleadoXCi(@PathVariable String ci,HttpServletRequest request){
        String actividad = "Eliminar un empleado a raìz de su carnet de identidad";
        TokenDto tokenDto = TokenUtils.getTokenDto(request);
        try {
            Empleado empleado = empleadoService.obtenerEmpleadoXCi(ci).get();
            List<Entidad> entidads = empleado.getEntidades();
            for (Entidad entidad: entidads){
                for (int contador = 0; contador<entidad.getPersonal().size();contador++){
                    if (entidad.getPersonal().get(contador).getCi().equals(ci))
                        entidad.getPersonal().remove(contador);
                }
                entidadService.modificarEntidad(entidad);
            }
            empleadoService.eliminarEmpleado(ci);
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,IpUtils.hostIpV4Http(request),"Aceptado",null);
            return ResponseEntity.ok("Empleado eliminado");
        }catch (Exception e){
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,IpUtils.hostIpV4Http(request),"Rechazado",e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/desacoplar/{id1}/{id2}")
    @Operation(summary = "Desacoplar empleado de la entidad",
            description = "Desacoplar empleado de la entidad por ambos ID",security = { @SecurityRequirement(name = "bearer-key") })
    @PreAuthorize(value = "hasAnyRole('Super Administrador','Administrador','Gestor')")
    public ResponseEntity<?> desacomplarEmpleado(@PathVariable Long id1,@PathVariable Long id2,HttpServletRequest request){
        String actividad = "Desacoplar empleado de la entidad";
        TokenDto tokenDto = TokenUtils.getTokenDto(request);
        try {
            Entidad entidads = entidadService.obtenerEntidadID(id2).get();
            for (Empleado empleado: entidads.getPersonal()){
                System.out.println(empleado.getUuid());
                if (empleado.getUuid() == id1){
                    entidads.getPersonal().remove(empleado);
                    break;
                }
            }
            entidadService.modificarEntidad(entidads);
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,IpUtils.hostIpV4Http(request),"Aceptado",null);
            return ResponseEntity.ok("Usuario desacoplado");
        }catch (Exception e){
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,IpUtils.hostIpV4Http(request),"Rechazado",e.getMessage());
            return ResponseEntity.badRequest().body("No ha sido posible desacoplar el usuario");
        }
    }
}
