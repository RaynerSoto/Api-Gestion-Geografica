package cu.edu.cujae.gestion.core.services.servicesImpl;

import cu.edu.cujae.gestion.core.mapper.AreaSalud;
import cu.edu.cujae.gestion.core.repository.AreaSaludRepository;
import cu.edu.cujae.gestion.core.services.AreaSaludServicesInterfaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaSaludService implements AreaSaludServicesInterfaces {
    private final AreaSaludRepository areaSaludRepository;

    @Autowired
    public AreaSaludService(AreaSaludRepository areaSaludRepository) {
        this.areaSaludRepository = areaSaludRepository;
    }

    @Override
    public List<AreaSalud> getAreaSalud() {
        return areaSaludRepository.findAll();
    }
}
