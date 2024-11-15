package cu.edu.cujae.core.clases;

import cu.edu.cujae.logs.core.utils.enums.SexoEnums;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.Before;
import org.junit.Test;
import java.util.Set;

import org.springframework.boot.test.context.SpringBootTest;

import static junit.framework.TestCase.fail;

@SpringBootTest
public class sexoTest {

    private jakarta.validation.Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void crear_sexo(){
        SexoEnums sexoEnums = SexoEnums.MASCULINO;
        Set<ConstraintViolation<SexoEnums>> violations = validator.validate(sexoEnums);
        if (violations.size() == 0){
            System.out.println("Prueba con éxito");
        }else {
            for (ConstraintViolation con :violations ){
                System.out.println(con.getMessage());
            }
            fail("La prueba ha fallado en la creación del sexo");
        }
    }

    @Test
    public void crear_sexo2(){
        SexoEnums sex = SexoEnums.FEMININO;
        Set<ConstraintViolation<SexoEnums>> violations = validator.validate(sex);
        if (violations.size() == 0){
            System.out.println("Prueba con éxito");
        }else {
            for (ConstraintViolation con :violations ){
                System.out.println(con.getMessage());
            }
            fail("La prueba ha fallado en la creación del sexo");
        }
    }
}