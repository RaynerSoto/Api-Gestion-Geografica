package cu.edu.cujae.logs.core.servicesInterfaces;

import cu.edu.cujae.logs.core.mapping.Privilegio;

import java.util.List;

public interface PrivilegioServiceInterfaces {
    public void iniciar();

    public List<Privilegio> listarPrivilegios();
}