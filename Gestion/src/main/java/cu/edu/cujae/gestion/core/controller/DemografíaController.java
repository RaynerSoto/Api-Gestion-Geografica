package cu.edu.cujae.gestion.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cu.edu.cujae.gestion.core.dto.DemografiaDto;
import cu.edu.cujae.gestion.core.dto.TokenDto;
import cu.edu.cujae.gestion.core.dto.UsuarioDto;
import cu.edu.cujae.gestion.core.feignclient.TokenServiceInterfaces;
import cu.edu.cujae.gestion.core.mapper.Empleado;
import cu.edu.cujae.gestion.core.mapper.Entidad;
import cu.edu.cujae.gestion.core.mapper.Municipio;
import cu.edu.cujae.gestion.core.mapper.Provincia;
import cu.edu.cujae.gestion.core.services.EmpleadoServiceInterfaces;
import cu.edu.cujae.gestion.core.services.EntidadServicesInterfaces;
import cu.edu.cujae.gestion.core.services.MunicipioServicesInterfaces;
import cu.edu.cujae.gestion.core.services.ProvinciaServiceInterfaces;
import cu.edu.cujae.gestion.core.utils.IpUtils;
import cu.edu.cujae.gestion.core.utils.RegistroUtils;
import cu.edu.cujae.gestion.core.utils.TokenUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
//Funciona
@RestController
@RequestMapping("/api/v1/gestion/demografia")
public class DemografíaController {
    private final EntidadServicesInterfaces entidadServices;
    private final EmpleadoServiceInterfaces empleadoServices;
    private final ProvinciaServiceInterfaces provinciaServices;
    private final MunicipioServicesInterfaces municipioServices;
    private final RegistroUtils registroUtils;
    private final TokenServiceInterfaces tokenService;
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public DemografíaController(EntidadServicesInterfaces entidadServices, EmpleadoServiceInterfaces empleadoServices, ProvinciaServiceInterfaces provinciaServices, MunicipioServicesInterfaces municipioServices, RegistroUtils registroUtils, TokenServiceInterfaces tokenService) {
        this.entidadServices = entidadServices;
        this.empleadoServices = empleadoServices;
        this.provinciaServices = provinciaServices;
        this.municipioServices = municipioServices;
        this.registroUtils = registroUtils;
        this.tokenService = tokenService;
    }

    @GetMapping("/interProvincial")
    @Operation(summary = "Movimientos demográficos interprovinciales",
            description = "Permite obtener todo el listado de los movimientos demográficos interprovinciales",
            security = { @SecurityRequirement(name = "bearer-key") })
    @PreAuthorize("hasAnyRole('Super Administrador','Administrador','Gestor')")
    public ResponseEntity<?> movilidadPoblacionalInterProvincial(HttpServletRequest request){
        String actividad = "Obtener una tabla con la movilidad interprovincial";
        TokenDto tokenDto = TokenUtils.getTokenDto(request);
        try{
           List<Provincia> provinciaList = provinciaServices.listadoProvincia();
           List<DemografiaDto> listado = new ArrayList<>();
           for (Provincia provincia : provinciaList) {
               for (Provincia provincia1 : provinciaList) {
                   DemografiaDto demografiaDto = new DemografiaDto();
                   demografiaDto.setOrigen(provincia.getNombre());
                   demografiaDto.setDestino(provincia1.getNombre());
                   for (Entidad entidad : entidadServices.listarEntidad()){
                       if(entidad.getProvincia().getNombre().equalsIgnoreCase(demografiaDto.getDestino())){
                           for (Empleado empleado : entidad.getPersonal()){
                               if (empleado.getProvincia().getNombre().equalsIgnoreCase(demografiaDto.getOrigen())){
                                   demografiaDto.setCantidad(demografiaDto.getCantidad()+1);
                               }
                           }
                       }
                   }
                   listado.add(demografiaDto);
               }
           }
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad, IpUtils.hostIpV4Http(request),"Aceptado",null);
            return ResponseEntity.ok(listado);
       } catch (Exception e) {
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,IpUtils.hostIpV4Http(request),"Rechazado",e.getMessage());
            throw new RuntimeException("No se ha podido obtener el reporte demográfico, compruebe su conexiòn a la base de datos o contacto con el servicio tècnico");
       }
    }

    @GetMapping("/intermunicipal/{provincia}")
    @Operation(summary = "Movimientos demográficos intermunicipales dentro de una provincia",
            description = "Permite obtener todo el listado de los movimientos demográficos interprovinciales",
            security = { @SecurityRequirement(name = "bearer-key") })
    @PreAuthorize("hasAnyRole('Super Administrador','Administrador','Gestor')")
    public ResponseEntity<?> movilidadPoblacionalInterMunicipal(@PathVariable String provincia, HttpServletRequest request){
        String actividad = "Obtener una tabla con la movilidad intermunicipal";
        TokenDto tokenDto = TokenUtils.getTokenDto(request);
        try{
            Provincia prov = provinciaServices.buscarProvinciaPorNombre(provincia).get();
            List<DemografiaDto> listado = new ArrayList<>();
            for(Municipio municipio : prov.getListadoMunicipios()){
                for(Municipio municipio1: prov.getListadoMunicipios()){
                    DemografiaDto demografiaDto = new DemografiaDto();
                    demografiaDto.setOrigen(municipio.getNombre());
                    demografiaDto.setDestino(municipio1.getNombre());
                    for(Entidad entidad: municipio1.getEntidades()){
                        for (Empleado empleado : entidad.getPersonal()){
                            if (empleado.getMunicipio().getNombre().equalsIgnoreCase(demografiaDto.getOrigen())){
                                demografiaDto.setCantidad(demografiaDto.getCantidad()+1);
                                if (demografiaDto.getCantidad() != 0){
                                    System.out.println(demografiaDto);
                                }
                            }
                        }
                    }
                    listado.add(demografiaDto);
                }
            }
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad, IpUtils.hostIpV4Http(request),"Aceptado",null);
            return ResponseEntity.ok(listado);
        } catch (Exception e) {
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,IpUtils.hostIpV4Http(request),"Rechazado",e.getMessage());
            throw new RuntimeException("No se ha podido obtener el reporte demográfico, compruebe su conexiòn a la base de datos o contacto con el servicio tècnico");
        }
    }
}
