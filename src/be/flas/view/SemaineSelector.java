package be.flas.view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

public class SemaineSelector extends JFrame {

    private JComboBox<String> semaineComboBox;
    private JButton validerButton;

    public SemaineSelector() {
        // Titre de la fenêtre
        setTitle("Sélecteur de Semaine");
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Créer le JComboBox pour les semaines
        semaineComboBox = new JComboBox<>();
        remplirComboBox();

        // Créer un bouton de validation
        validerButton = new JButton("Valider");
        validerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String semaineChoisie = (String) semaineComboBox.getSelectedItem();
                JOptionPane.showMessageDialog(null, "Vous avez choisi : " + semaineChoisie);
            }
        });

        // Ajouter les composants à la fenêtre
        add(semaineComboBox);
        add(validerButton);
    }

    private void remplirComboBox() {
        // Remplir le JComboBox avec les numéros de semaine et leurs dates
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));

        for (int i = 1; i <= 52; i++) {
            // Début de la semaine (lundi)
            calendar.set(Calendar.WEEK_OF_YEAR, i);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            String debutSemaine = String.format("%1$td/%1$tm/%1$tY", calendar);

            // Fin de la semaine (dimanche)
            calendar.add(Calendar.DAY_OF_WEEK, 6); // Ajouter 6 jours pour obtenir la fin de la semaine
            String finSemaine = String.format("%1$td/%1$tm/%1$tY", calendar);

            // Ajouter l'élément au JComboBox
            semaineComboBox.addItem("Semaine " + i + ": " + debutSemaine + " - " + finSemaine);

            // Réinitialiser le calendrier pour le début de la semaine suivante
            calendar.add(Calendar.DAY_OF_WEEK, -6); // Revenir à lundi
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SemaineSelector app = new SemaineSelector();
                app.setVisible(true);
            }
        });
    }
}
