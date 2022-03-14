package es.uji.ei1027.clubesportiu.services;

import es.uji.ei1027.clubesportiu.model.Nadador;
import java.util.List;
import java.util.Map;

public interface ClassificacioService {
    public Map<String, List<Nadador>> getClassificationByCountry(String prova);

    public Map<String, List<Nadador>> getNadadorsPerProva(String prova);
}
