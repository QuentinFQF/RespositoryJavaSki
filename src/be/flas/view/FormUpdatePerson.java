package be.flas.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import be.flas.model.Skier;
import be.flas.model.Instructor;
import com.toedter.calendar.JDateChooser;

public class FormUpdatePerson extends JFrame {

    private JPanel contentPane;
    private JTextField tfSearchPerson, tfPseudo, tfFirstName, tfLastName;
    private JTable tablePerson;
    private DefaultTableModel tableModel;
    private JButton btnUpdatePerson;
    private Skier skier = null;
    private Instructor instructor = null;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                FormUpdatePerson frame = new FormUpdatePerson();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    
    public FormUpdatePerson() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 835, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 128, 128));
        panel.setBounds(20, 10, 780, 540);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblSearch = new JLabel("Pseudo Skier/Instructor :");
        lblSearch.setBounds(20, 20, 200, 20);
        panel.add(lblSearch);

        tfSearchPerson = new JTextField();
        tfSearchPerson.setBounds(220, 20, 150, 20);
        panel.add(tfSearchPerson);

        JButton btnSearchPerson = new JButton("Rechercher");
        btnSearchPerson.setBounds(400, 20, 150, 20);
        panel.add(btnSearchPerson);

        JLabel lblPseudo = new JLabel("Pseudo :");
        lblPseudo.setBounds(20, 60, 150, 20);
        panel.add(lblPseudo);

        tfPseudo = new JTextField();
        tfPseudo.setBounds(220, 60, 150, 20);
        panel.add(tfPseudo);

        JLabel lblFirstName = new JLabel("Prénom :");
        lblFirstName.setBounds(20, 100, 150, 20);
        panel.add(lblFirstName);

        tfFirstName = new JTextField();
        tfFirstName.setBounds(220, 100, 150, 20);
        panel.add(tfFirstName);

        JLabel lblLastName = new JLabel("Nom :");
        lblLastName.setBounds(20, 140, 150, 20);
        panel.add(lblLastName);

        tfLastName = new JTextField();
        tfLastName.setBounds(220, 140, 150, 20);
        panel.add(tfLastName);

        JButton btnUpdatePerson = new JButton("Modifier");
        btnUpdatePerson.setBounds(400, 140, 150, 20);
        panel.add(btnUpdatePerson);

        // Création de JDateChooser pour la date de naissance (pour les Skier et Instructor)
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setBounds(220, 181, 150, 20); // Positionner le JDateChooser
        panel.add(dateChooser);
        //dateChooser.setVisible(false); // Masquer le composant par défaut

        // ActionListener pour le bouton "Rechercher"
        btnSearchPerson.addActionListener(e -> {
            String input = tfSearchPerson.getText().trim();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(contentPane, "Veuillez entrer un pseudo.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            skier = Skier.getSkierByPseudo(input);
            instructor = Instructor.getInstructorByPseudo(input);

            if (skier != null) {
                tfPseudo.setText(skier.getPseudo());
                tfFirstName.setText(skier.getFirstName());
                tfLastName.setText(skier.getName());
                //dateChooser.setVisible(true); 
                LocalDate dateSkier = skier.getDateOfBirth();

                if (dateSkier != null) {
                    // Convertir LocalDate en java.util.Date
                    Date date = Date.from(dateSkier.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    
                    // Passer la date au JDateChooser
                    dateChooser.setDate(date);
                }
                instructor = null; // On s'assure qu'il ne s'agit pas d'un instructeur
            } else if (instructor != null) {
                tfPseudo.setText(instructor.getPseudo());
                tfFirstName.setText(instructor.getFirstName());
                tfLastName.setText(instructor.getName());
               // dateChooser.setVisible(true); // Afficher la date pour l'instructeur
                // Si une date existe déjà, la remplir dans le JDateChooser
                /*if (instructor.getDateOfBirth() != null) {
                    dateChooser.setDate(instructor.getDateOfBirth());
                }*/
                LocalDate dateInstructor = instructor.getDateOfBirth();

                if (dateInstructor != null) {
                    // Convertir LocalDate en java.util.Date
                    Date date = Date.from(dateInstructor.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    
                    // Passer la date au JDateChooser
                    dateChooser.setDate(date);
                }
                skier = null; // On s'assure qu'il ne s'agit pas d'un skieur
            } else {
                JOptionPane.showMessageDialog(contentPane, "Aucun skieur ou instructeur trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ActionListener pour le bouton "Modifier"
        btnUpdatePerson.addActionListener(e -> {
            if (skier == null && instructor == null) {
                JOptionPane.showMessageDialog(contentPane, "Aucun skieur ou instructeur sélectionné.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String pseudo = tfPseudo.getText().trim();
            String firstName = tfFirstName.getText().trim();
            String lastName = tfLastName.getText().trim();

            // Vérifications des champs vides et des formats
            if (pseudo.isEmpty()) {
                JOptionPane.showMessageDialog(contentPane, "Erreur : le champ 'Pseudo' ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (firstName.isEmpty()) {
                JOptionPane.showMessageDialog(contentPane, "Erreur : le champ 'Prénom' ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (lastName.isEmpty()) {
                JOptionPane.showMessageDialog(contentPane, "Erreur : le champ 'Nom' ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Vérifications des formats des champs
            if (!pseudo.matches("[a-zA-Z]{1,50}")) {
                JOptionPane.showMessageDialog(contentPane, "Erreur : le pseudo doit contenir uniquement des lettres et être de 50 caractères maximum.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!firstName.matches("[a-zA-Z]{1,50}")) {
                JOptionPane.showMessageDialog(contentPane, "Erreur : le prénom doit contenir uniquement des lettres et être de 50 caractères maximum.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!lastName.matches("[a-zA-Z]{1,50}")) {
                JOptionPane.showMessageDialog(contentPane, "Erreur : le nom doit contenir uniquement des lettres et être de 50 caractères maximum.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Récupérer la date sélectionnée
            java.util.Date selectedDate = dateChooser.getDate();

            // Vérification si la date est valide
            if (selectedDate == null) {
                JOptionPane.showMessageDialog(contentPane, "Erreur : la date de naissance doit être sélectionnée.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Vérification que la date n'est pas dans le futur
            java.util.Date currentDate = new java.util.Date();
            if (selectedDate.after(currentDate)) {
                JOptionPane.showMessageDialog(contentPane, "Erreur : la date de naissance ne peut pas être dans le futur.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Conversion de la date en java.sql.Date pour la base de données
            java.sql.Date birthDate = new java.sql.Date(selectedDate.getTime());

            if (skier != null) {
                skier.setPseudo(pseudo);
                skier.setFirstName(firstName);
                skier.setName(lastName);
                skier.setDateOfBirth(birthDate.toLocalDate()); // Mettre à jour la date de naissance

                if (skier.update()) {
                    JOptionPane.showMessageDialog(contentPane, "Le skieur a été modifié avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Erreur lors de la modification du skieur.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else if (instructor != null) {
                instructor.setPseudo(pseudo);
                instructor.setFirstName(firstName);
                instructor.setName(lastName);
                instructor.setDateOfBirth(birthDate.toLocalDate()); // Mettre à jour la date de naissance

                if (instructor.update()) {
                    JOptionPane.showMessageDialog(contentPane, "L'instructeur a été modifié avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Erreur lors de la modification de l'instructeur.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }


}


