package cu.edu.cujae.gestion.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cu.edu.cujae.gestion.core.dto.AreaSaludDto;
import cu.edu.cujae.gestion.core.dto.TokenDto;
import cu.edu.cujae.gestion.core.dto.UsuarioDto;
import cu.edu.cujae.gestion.core.feignclient.TokenServiceInterfaces;
import cu.edu.cujae.gestion.core.utils.IpUtils;
import cu.edu.cujae.gestion.core.utils.RegistroUtils;
import cu.edu.cujae.gestion.core.utils.TokenUtils;
import cu.edu.cujae.gestion.core.services.servicesImpl.RegistroService;
import cu.edu.cujae.gestion.core.services.AreaSaludServicesInterfaces;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
// Area de salud ya funciona
@RestController
@RequestMapping(path = "/api/v1/gestion/areaSalud")
@Tag(name = "Controlador del área de salud"
        ,description = "Permite hacer todas las operaciones relacionadas con el área de salud")
@SecurityRequirement(name = "bearer-key")
public class AreaSaludController {

    private final AreaSaludServicesInterfaces areaSaludServices;
    private final RegistroService registroService;
    private final RegistroUtils registroUtils;
    private final TokenServiceInterfaces tokenService;
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public AreaSaludController(AreaSaludServicesInterfaces areaSaludServices, RegistroService registroService, RegistroUtils registroUtils,TokenServiceInterfaces tokenService) {
        this.areaSaludServices = areaSaludServices;
        this.registroService = registroService;
        this.registroUtils = registroUtils;
        this.tokenService = tokenService;
    }

    @GetMapping("/")
    @Operation(summary = "Listado de áreas de salud",
            description = "Permite obtener todo el listado de las áreas de salud",
            security = { @SecurityRequirement(name = "bearer-key") })
    @PreAuthorize(value = "hasAnyRole('Super Administrador','Administrador','Gestor')")
    public ResponseEntity<?> getAreaSalud(HttpServletRequest request) {
        String actividad = "Obtener todo el listado de las áreas de salud";
        TokenDto tokenDto = TokenUtils.getTokenDto(request);
        try{
            List<AreaSaludDto> areaSaludDtos = areaSaludServices.getAreaSalud().stream()
                    .map(AreaSaludDto::new)
                    .sorted(Comparator.comparing(AreaSaludDto::getUuid))
                    .toList();
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad, IpUtils.hostIpV4Http(request),"Aceptado",null);
            return ResponseEntity.ok(areaSaludDtos);
        }catch (Exception e){
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,IpUtils.hostIpV4Http(request),"Rechazado",e.getMessage());
            return ResponseEntity.badRequest().body("No se ha podido obtener el listado de àreas de salud, compruebe su conexiòn a la base de datos o contacto con el servicio tècnico");
        }
    }
}
