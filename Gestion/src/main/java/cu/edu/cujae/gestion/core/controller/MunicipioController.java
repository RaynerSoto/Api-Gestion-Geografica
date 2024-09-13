package cu.edu.cujae.gestion.core.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import cu.edu.cujae.gestion.core.dto.MunicipioDto;
import cu.edu.cujae.gestion.core.dto.TokenDto;
import cu.edu.cujae.gestion.core.dto.UsuarioDto;
import cu.edu.cujae.gestion.core.feignclient.TokenServiceInterfaces;
import cu.edu.cujae.gestion.core.utils.IpUtils;
import cu.edu.cujae.gestion.core.utils.RegistroUtils;
import cu.edu.cujae.gestion.core.utils.TokenUtils;
import cu.edu.cujae.gestion.core.services.MunicipioServicesInterfaces;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
// Municipio funciona bien
@RestController
@RequestMapping("/api/v1/gestion/municipio")
@Tag(name = "Controlador de municipios",
        description = "Permite hacer todas las operaciones de los municipios")
@SecurityRequirement(name = "bearer-key")
public class MunicipioController {

    private final MunicipioServicesInterfaces municipioServices;
    private final RegistroUtils registroUtils;
    private final TokenServiceInterfaces tokenService;
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public MunicipioController(MunicipioServicesInterfaces municipioServices, RegistroUtils registroUtils, TokenServiceInterfaces tokenService) {
        this.municipioServices = municipioServices;
        this.registroUtils = registroUtils;
        this.tokenService = tokenService;
    }

    @GetMapping("/")
    @Operation(summary = "Listado de municipios",
    description = "Permite obtener el listado de todos los municipios del sistema",security = { @SecurityRequirement(name = "bearer-key") })
    @PreAuthorize(value = "hasAnyRole('Super Administrador','Administrador','Gestor')")
    public ResponseEntity<?> getAllMunicipios(HttpServletRequest request) {
        String actividad = "Listar todos todos los municipios del sistema";
        TokenDto tokenDto = TokenUtils.getTokenDto(request);
        try {
            List<MunicipioDto> municipioDtos = municipioServices.listadoMunicipios().stream()
                    .map(MunicipioDto::new)
                    .sorted(Comparator.comparing(MunicipioDto::getUuid))
                    .toList();
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad, IpUtils.hostIpV4Http(request),"Aceptado",null);
            return ResponseEntity.ok(municipioDtos);
        }catch (Exception e){
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(),actividad,IpUtils.hostIpV4Http(request),"Rechazado",e.getMessage());
            return ResponseEntity.badRequest().body("No se ha podido obtener el listado de municipios, compruebe su conexiòn a la base de datos o contacto con el servicio tècnico");
        }
    }
}
