package cu.edu.cujae.gestion.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cu.edu.cujae.gestion.core.dto.TokenDto;
import cu.edu.cujae.gestion.core.dto.UsuarioDto;
import cu.edu.cujae.gestion.core.dto.abstractas.General;
import cu.edu.cujae.gestion.core.dto.EntidadDto;
import cu.edu.cujae.gestion.core.dto.ErrorDto;
import cu.edu.cujae.gestion.core.dto.empleadoDtos.EmpleadoDtoInsert;
import cu.edu.cujae.gestion.core.feignclient.TokenServiceInterfaces;
import cu.edu.cujae.gestion.core.services.servicesImpl.RegistroService;
import cu.edu.cujae.gestion.core.services.servicesIntern.EntidadServicesIntern;
import cu.edu.cujae.gestion.core.services.servicesIntern.ExcelServicesIntern;
import cu.edu.cujae.gestion.core.services.servicesIntern.FileServicesIntern;
import cu.edu.cujae.gestion.core.services.servicesIntern.EmpleadoServicesIntern;
import cu.edu.cujae.gestion.core.utils.IpUtils;
import cu.edu.cujae.gestion.core.utils.RegistroUtils;
import cu.edu.cujae.gestion.core.utils.TokenUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;

// Excel comprobado
@RestController
@RequestMapping("/api/v1/gestion/excel")
@Tag(name = "Controlador del trabajo con el Excel"
        ,description = "Permite todo el trabajo relacionado con el Excel")
@SecurityRequirement(name = "bearer-key")
public class ExcelController {

    private final FileServicesIntern fileServicesIntern;
    private final ExcelServicesIntern excelServicesIntern;
    private final EntidadServicesIntern entidadServicesIntern;
    private final EmpleadoServicesIntern empleadoServicesIntern;
    private final RegistroService registroService;
    private final RegistroUtils registroUtils;
    private final TokenServiceInterfaces tokenService;
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public ExcelController(FileServicesIntern fileServicesIntern, ExcelServicesIntern excelServicesIntern, EntidadServicesIntern entidadServicesIntern, EmpleadoServicesIntern empleadoServicesIntern, RegistroService registroService, RegistroUtils registroUtils, TokenServiceInterfaces tokenService) {
        this.fileServicesIntern = fileServicesIntern;
        this.excelServicesIntern = excelServicesIntern;
        this.entidadServicesIntern = entidadServicesIntern;
        this.empleadoServicesIntern = empleadoServicesIntern;
        this.registroService = registroService;
        this.registroUtils = registroUtils;
        this.tokenService = tokenService;
    }

    @PostMapping("/")
    @Operation(
            summary = "Extraer e insertar datos de un Excel",
            description = "Permite obtener todos los datos que encontramos en un Excel",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @ApiResponse(responseCode = "200", description = "Fichero recibido",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class)))
    @PreAuthorize(value = "hasAnyRole('Super Administrador','Administrador','Gestor')")
    public ResponseEntity<?> cargarUnExcel(@Parameter(description = "File to be uploaded", required = true)
                                               @RequestParam("file") @NotNull MultipartFile file, HttpServletRequest request) {
        String actividad = "Extraer datos del Excel";
        TokenDto tokenDto = TokenUtils.getTokenDto(request);
        try {
            if (file.isEmpty()) {
                throw new Exception("Archivo vacío o no recibido");
            } else {
                File fichero;
                try {
                    fichero = fileServicesIntern.convertMultipartFileToFile(file);
                } catch (Exception e) {
                    throw new RuntimeException("No es una archivo que se pueda procesar");
                }
                Workbook libro;
                try{
                    libro = fileServicesIntern.construccion_libro(fichero);
                } catch (Exception e) {
                    throw new RuntimeException("No es una archivo que se pueda convertir a Excel");
                }
                ArrayList<Sheet> hojas = fileServicesIntern.listado_hojas(libro);
                fichero.deleteOnExit();
                if (hojas.size() == 2) {
                    ArrayList<General> datos = new ArrayList<>();
                    ArrayList<ErrorDto> datosIncorrectos = new ArrayList<>();
                    for (Sheet hoja : hojas) {
                        int cantidad_filas = hoja.getLastRowNum() + 1;
                        int cantida_columnas;
                        try {
                            cantida_columnas = hoja.getRow(0).getLastCellNum();
                        } catch (Exception e2) {
                            cantida_columnas = 0;
                        }
                        if (cantidad_filas != 0 && cantida_columnas != 0) {
                            if (excelServicesIntern.isEntidadSheet(hoja)) {
                                datos.addAll(entidadServicesIntern.extraer_entidades(hoja));
                            } else if (excelServicesIntern.isPersonalSheet(hoja)) {
                                datos.addAll(empleadoServicesIntern.extraer_personas(hoja));
                            }
                        }
                    }
                    for (General elemento : datos) {
                        try {
                            if (elemento instanceof EntidadDto) {
                                entidadServicesIntern.insertarEntidad((EntidadDto) elemento);
                            } else {
                                empleadoServicesIntern.insertarEmpleadoConTrabajo((EmpleadoDtoInsert) elemento);
                            }

                        } catch (Exception e) {
                            datosIncorrectos.add(new ErrorDto(elemento, e.getMessage()));
                            System.out.println(e.getMessage());
                        }
                    }
                    if (datosIncorrectos.isEmpty()) {
                        registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(), actividad, IpUtils.hostIpV4Http(request), "Aceptado", null);
                        return ResponseEntity.ok("Datos analizados y procesados con éxito");
                    } else {
                        registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(), actividad, IpUtils.hostIpV4Http(request), "Aceptado", "No se insertaron una cantidad de: " + datosIncorrectos.size() + " registros");
                        return ResponseEntity.badRequest().body(datosIncorrectos);
                    }
                } else {
                    throw new Exception("Libro con una cantidad de hojas no computables");
                }
            }
        }catch(Exception e){
            registroUtils.insertarRegistro(mapper.convertValue(tokenService.tokenExists(tokenDto).getBody(), UsuarioDto.class).getUsername(), actividad, IpUtils.hostIpV4Http(request), "Rechazado", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
