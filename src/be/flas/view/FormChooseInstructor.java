
package be.flas.view;

import java.awt.EventQueue;
import java.sql.Connection;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import be.flas.connection.DatabaseConnection;
import be.flas.dao.DAOInstructor;
import be.flas.dao.DAOLessonType;
import be.flas.model.Instructor;

import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormChooseInstructor extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private DAOInstructor daoInstructor;
    private DAOLessonType daoLessonType;
    private Connection sharedConnection;
    private JComboBox<String> comboInstructor;
    private JComboBox<String> comboLessonType;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FormChooseInstructor frame = new FormChooseInstructor();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public FormChooseInstructor() {
        sharedConnection = DatabaseConnection.getInstance().getConnection();
        daoInstructor = new DAOInstructor(sharedConnection);
        daoLessonType = new DAOLessonType(sharedConnection);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 757, 550);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 128, 128));
        panel.setBounds(213, 10, 462, 474);
        contentPane.add(panel);
        panel.setLayout(null);

        comboInstructor = new JComboBox<>();
        comboInstructor.setBounds(149, 149, 161, 21);
        panel.add(comboInstructor);

        comboLessonType = new JComboBox<>();
        comboLessonType.setBounds(32, 115, 410, 21);
        panel.add(comboLessonType);

        JButton btnNewButton = new JButton("Choisir");
        btnNewButton.setBounds(189, 443, 85, 21);
        panel.add(btnNewButton);

        fillLessonTypeComboBox();
        
        // Ajouter un ActionListener pour détecter les changements dans comboLessonType
        comboLessonType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedLessonType = (String) comboLessonType.getSelectedItem();
                String[] parts = selectedLessonType.split(" - ");
                
                // Vérifiez que le tableau contient suffisamment d'éléments avant d'accéder aux indices
                if (parts.length >= 4) {
                    String level = parts[0];
                    String category = parts[2];
                    String targetAudience = parts[3];
                    
                    updateInstructorsForLessonType(/*level,*/ category, targetAudience);
                    System.out.println("cat "+category);
                    System.out.println("age "+targetAudience);
                } else {
                    System.err.println("Format inattendu pour le type de leçon sélectionné : " + selectedLessonType);
                }
            }
        });
    }

    // Met à jour le comboInstructor en fonction des paramètres extraits de comboLessonType
    private void updateInstructorsForLessonType(/*String level,*/ String category, String targetAudience) {
        System.out.println("Mise à jour des instructeurs pour le type de leçon : "/* + level */+ ", " + category + ", " + targetAudience);
        try {
            // Appel à daoInstructor pour récupérer les instructeurs par type de leçon
            List<Instructor> instructors = daoInstructor.getInstructorsByLessonType(/*level,*/ category, targetAudience);
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

            for (Instructor instructor : instructors) {
                String displayText = instructor.getName() + " " + instructor.getFirstName();
                model.addElement(displayText);
            }

            comboInstructor.setModel(model);
            System.out.println("Instructeurs mis à jour : " + instructors);
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour des instructeurs : " + e.getMessage());
        }
    }

    // Remplir comboLessonType avec tous les LessonTypes disponibles
    private void fillLessonTypeComboBox() {
        System.out.println("Début du remplissage du JComboBox avec les LessonTypes.");
        try {
            List<String> lessonTypes = daoLessonType.selectLessonType();
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(lessonTypes.toArray(new String[0]));
            comboLessonType.setModel(model);
            System.out.println("LessonTypes chargés : " + lessonTypes);
        } catch (Exception e) {
            System.err.println("Erreur lors du remplissage du JComboBox : " + e.getMessage());
        }
    }
}


