package be.flas.dao;

import be.flas.interfaces.IDaoClasse;
import be.flas.model.Instructor;
import be.flas.model.Skier;

public class DAOSkier implements IDaoClasse<Skier> {

	@Override
    public boolean create(Skier skier) {
        // Ici, vous pouvez ajouter le code pour enregistrer l'instructeur dans la base de données
        // Pour l'instant, nous retournons simplement true comme exemple.
        System.out.println("skier créé: " + skier);
        return true;
    }
}
