package services;

import java.util.List;
import model.Equation;

public interface EquationPreparer {
    List<Equation> prepareEquationsFor(int base);
}
