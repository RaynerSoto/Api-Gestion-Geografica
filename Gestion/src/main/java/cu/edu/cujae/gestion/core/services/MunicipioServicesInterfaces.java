package cu.edu.cujae.gestion.core.services;

import cu.edu.cujae.gestion.core.mapper.Municipio;

import java.util.List;
import java.util.Optional;

public interface MunicipioServicesInterfaces {
    List<Municipio> listadoMunicipios();

    Optional<Municipio> obtenerMunicipioNombre(String nombre) throws Exception;

    boolean isMuncipioinProvincia(String provincia,String municipio) throws Exception;
}
