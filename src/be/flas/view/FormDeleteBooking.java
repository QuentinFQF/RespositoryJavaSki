/*package be.flas.view;

import java.awt.EventQueue;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import be.flas.model.Booking;
import be.flas.model.Skier;

public class FormDeleteBooking extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tfDeleteSkier;
    private JTable tableSkier;
    private DefaultTableModel tableModel;

    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FormDeleteBooking frame = new FormDeleteBooking();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    
    public FormDeleteBooking() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 835, 548);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 128, 128));
        panel.setBounds(198, 10, 429, 473);
        contentPane.add(panel);
        panel.setLayout(null);

        JButton btnSearchBooking = new JButton("Rechercher booking");
        btnSearchBooking.setBounds(144, 154, 150, 21);
        panel.add(btnSearchBooking);

        tfDeleteSkier = new JTextField();
        tfDeleteSkier.setBounds(144, 126, 150, 19);
        panel.add(tfDeleteSkier);
        tfDeleteSkier.setColumns(10);

        // Ajouter une barre de défilement et une table
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 250, 381, 150);
        panel.add(scrollPane);

        // Initialiser le modèle de la table avec les colonnes
        tableModel = new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Nom", "Prénom", "Pseudo", "Date Naissance" });
        tableSkier = new JTable(tableModel);
        scrollPane.setViewportView(tableSkier);
        
        JButton btnDeleteButton = new JButton("supprimer");
        // ActionListener pour le bouton Supprimer
        btnDeleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Vérifier qu'une ligne est sélectionnée
                int selectedRow = tableSkier.getSelectedRow();

                if (selectedRow == -1) { // Aucune ligne n'est sélectionnée
                    JOptionPane.showMessageDialog(contentPane, "Veuillez sélectionner un booking dans le tableau pour le supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Récupérer l'ID du skieur sélectionné dans la table
                int insId = (int) tableModel.getValueAt(selectedRow, 0);

                // Afficher une confirmation avant la suppression
                int confirm = JOptionPane.showConfirmDialog(contentPane, "Êtes-vous sûr de vouloir supprimer ce booking ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    // Appel à la méthode de suppression (à implémenter)
                    Booking s = new Booking(insId);
                    boolean isDeleted = s.delete();

                    if (isDeleted) {
                        JOptionPane.showMessageDialog(contentPane, "Le booking a été supprimé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                        // Supprimer la ligne du tableau
                        tableModel.removeRow(selectedRow);
                    } else {
                        JOptionPane.showMessageDialog(contentPane, "Erreur lors de la suppression du booking.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        btnDeleteButton.setBounds(334, 442, 85, 21);
        panel.add(btnDeleteButton);

        // ActionListener pour le bouton Rechercher
        btnSearchBooking.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String pseudo = tfDeleteSkier.getText().trim();

                if (pseudo.isEmpty()) {
                    JOptionPane.showMessageDialog(contentPane, "Veuillez entrer un pseudo.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Recherche du skieur par pseudo
                Booking booking = Booking.getBookingByPseudo(pseudo);

                // Nettoyer les anciennes données du modèle de table
                tableModel.setRowCount(0);

                if (booking != null) {
                    // Ajouter le skieur trouvé dans le tableau
                    tableModel.addRow(new Object[] {
                            
                    });
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Aucun booking trouvé avec ce pseudo.", "Résultat", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }
}*/

package be.flas.view;

import java.awt.EventQueue;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import be.flas.model.Booking;
import be.flas.model.Skier;
import be.flas.model.Instructor;

public class FormDeleteBooking extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tfDeleteSkier;
    private JTable tableSkier;
    private DefaultTableModel tableModel;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FormDeleteBooking frame = new FormDeleteBooking();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public FormDeleteBooking() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 835, 548);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 128, 128));
        panel.setBounds(0, 10, 811, 473);
        contentPane.add(panel);
        panel.setLayout(null);

        JButton btnSearchBooking = new JButton("Rechercher booking");
        btnSearchBooking.setBounds(144, 154, 150, 21);
        panel.add(btnSearchBooking);

        tfDeleteSkier = new JTextField();
        tfDeleteSkier.setBounds(144, 126, 150, 19);
        panel.add(tfDeleteSkier);
        tfDeleteSkier.setColumns(10);

        // Ajouter une barre de défilement et une table
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 250, 791, 150);
        panel.add(scrollPane);

        // Initialiser le modèle de la table avec les colonnes
        tableModel = new DefaultTableModel(new Object[][] {},  new String[] { 
                "ID Booking", "Nom Skieur", "Prénom Skieur",
                "Nom Instructeur", "Prénom Instructeur", 
                "Sport", "Catégorie d'âge", "Prix", "Niveau",
                "Moment journée", "Heure début", "Heure fin","Debut","Fin"
            });
        tableSkier = new JTable(tableModel);
        scrollPane.setViewportView(tableSkier);

        JButton btnDeleteButton = new JButton("supprimer");
        // ActionListener pour le bouton Supprimer
        btnDeleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Vérifier qu'une ligne est sélectionnée
                int selectedRow = tableSkier.getSelectedRow();

                if (selectedRow == -1) { // Aucune ligne n'est sélectionnée
                    JOptionPane.showMessageDialog(contentPane, "Veuillez sélectionner un booking dans le tableau pour le supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Récupérer l'ID du booking sélectionné dans la table
                int bookingId = (int) tableModel.getValueAt(selectedRow, 0);

                // Afficher une confirmation avant la suppression
                int confirm = JOptionPane.showConfirmDialog(contentPane, "Êtes-vous sûr de vouloir supprimer ce booking ?", "Confirmation", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    // Appel à la méthode de suppression du booking (méthode à implémenter dans la classe Booking)
                	Booking s=new Booking(bookingId);
                	System.out.println(bookingId);
                    boolean isDeleted = s.delete();

                    if (isDeleted) {
                        JOptionPane.showMessageDialog(contentPane, "Le booking a été supprimé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                        // Supprimer la ligne du tableau
                        tableModel.removeRow(selectedRow);
                    } else {
                        JOptionPane.showMessageDialog(contentPane, "Erreur lors de la suppression du booking.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        btnDeleteButton.setBounds(421, 442, 85, 21);
        panel.add(btnDeleteButton);

        // ActionListener pour le bouton Rechercher
        btnSearchBooking.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	String input = tfDeleteSkier.getText().trim();
            	System.out.println(input);

                if (input.isEmpty()) {
                    JOptionPane.showMessageDialog(contentPane, "Veuillez entrer un ID de skieur ou d'instructeur.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Tentons de convertir l'input en Integer (ID)
                try {
                    //Integer id = Integer.valueOf(input);

                    // Ici, on recherche les bookings en fonction de l'ID
                    List<Booking> bookings = null;

                    // Si l'ID correspond à un Skier
                    bookings = Booking.getBookingsBySkierOrInstructorId(input, null);  // Recherche par SkierId

                    if (bookings.isEmpty()) {
                        // Si aucun booking n'est trouvé pour un skieur, on cherche par InstructorId
                        bookings = Booking.getBookingsBySkierOrInstructorId(null, input); // Recherche par InstructorId
                    } // Recherche par SkierId uniquement

                    // Nettoyer les anciennes données du modèle de table
                    tableModel.setRowCount(0);

                    // Affichage des résultats dans la table
                    for (Booking booking : bookings) {
                        tableModel.addRow(new Object[] {
                            booking.getId(),
                            booking.getSkier().getName(),
                            booking.getSkier().getFirstName(),
                            booking.getInstructor().getName(),
                            booking.getInstructor().getFirstName(),
                            booking.getLesson().getLessonType().getSport(),
                            booking.getLesson().getLessonType().getAgeCategory(),
                            booking.getLesson().getLessonType().getPrice(),
                            booking.getLesson().getLessonType().getLevel(),
                            booking.getLesson().getDayPart(),
                            booking.getLesson().getStart(),
                            booking.getLesson().getEnd(),
                            booking.getPeriod().getStartDate(),
                            booking.getPeriod().getEndDate()
                           
                            
                        });
                      
                    }
                    

                    if (bookings.isEmpty()) {
                        JOptionPane.showMessageDialog(contentPane, "Aucun booking trouvé pour ce skieur/instructeur.", "Résultat", JOptionPane.INFORMATION_MESSAGE);
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(contentPane, "L'ID doit être un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}

