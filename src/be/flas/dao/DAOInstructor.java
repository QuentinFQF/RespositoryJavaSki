package be.flas.dao;

import be.flas.interfaces.IDaoClasse;
import be.flas.model.Instructor;

public class DAOInstructor implements IDaoClasse<Instructor>{

	
	@Override
    public boolean create(Instructor instructor) {
        // Ici, vous pouvez ajouter le code pour enregistrer l'instructeur dans la base de données
        // Pour l'instant, nous retournons simplement true comme exemple.
        System.out.println("Instructeur créé: " + instructor);
        return true;
    }
}