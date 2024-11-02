
/*package be.flas.view;

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
                    
                    updateInstructorsForLessonType(category, targetAudience);
                    System.out.println("cat "+category);
                    System.out.println("age "+targetAudience);
                } else {
                    System.err.println("Format inattendu pour le type de leçon sélectionné : " + selectedLessonType);
                }
            }
        });
    }

    // Met à jour le comboInstructor en fonction des paramètres extraits de comboLessonType
    private void updateInstructorsForLessonType(String category, String targetAudience) {
        System.out.println("Mise à jour des instructeurs pour le type de leçon : " + category + ", " + targetAudience);
        try {
            // Appel à daoInstructor pour récupérer les instructeurs par type de leçon
            List<Instructor> instructors = daoInstructor.getInstructorsByLessonType( category, targetAudience);
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
}*/
//------------------------------------
/*
package be.flas.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Calendar;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import be.flas.connection.DatabaseConnection;
import be.flas.dao.DAOInstructor;
import be.flas.dao.DAOLessonType;
import be.flas.model.Instructor;

public class FormChooseInstructor extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private DAOInstructor daoInstructor;
    private DAOLessonType daoLessonType;
    private Connection sharedConnection;
    private JComboBox<String> comboInstructor;
    private JComboBox<String> comboLessonType;
    private JComboBox<String> comboSemaine; 
    private ButtonGroup buttonGroup;

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
        comboInstructor.setBounds(149, 200, 161, 21);
        panel.add(comboInstructor);

        comboLessonType = new JComboBox<>();
        comboLessonType.setBounds(32, 150, 410, 21);
        panel.add(comboLessonType);

        comboSemaine = new JComboBox<>();  // Création du JComboBox pour les semaines
        comboSemaine.setBounds(32, 100, 410, 21);
        panel.add(comboSemaine);

        JButton btnChoisir = new JButton("Choisir");
        btnChoisir.setBounds(189, 443, 85, 21);
        panel.add(btnChoisir);
        
        JRadioButton RadioButton1 = new JRadioButton("Matin");
        RadioButton1.setBounds(145, 291, 103, 21);
        panel.add(RadioButton1);
        
        JRadioButton RadioButton2 = new JRadioButton("Après-midi");
        RadioButton2.setBounds(145, 322, 103, 21);
        panel.add(RadioButton2);
        
        buttonGroup = new ButtonGroup();
        buttonGroup.add(RadioButton1);
        buttonGroup.add(RadioButton2);

        fillLessonTypeComboBox();
        fillSemaineComboBox();  // Appeler pour remplir le JComboBox avec les semaines

        // Ajout d'un ActionListener pour comboLessonType
        comboLessonType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedLessonType = (String) comboLessonType.getSelectedItem();
                String[] parts = selectedLessonType.split(" - ");

                if (parts.length >= 3) {
                    String extractedCategory = parts[2];
                    String extractedTargetAudience = parts[3];
                    updateInstructorsForLessonType(extractedCategory, extractedTargetAudience);
                } else {
                    System.err.println("Format inattendu pour le type de leçon sélectionné : " + selectedLessonType);
                }
            }
        });

        // ActionListener pour bouton Choisir, qui montre la semaine choisie
        btnChoisir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String semaineChoisie = (String) comboSemaine.getSelectedItem();
                JOptionPane.showMessageDialog(null, "Vous avez choisi : " + semaineChoisie);
            }
        });
    }

    private void updateInstructorsForLessonType(String category, String targetAudience) {
        System.out.println("Mise à jour des instructeurs pour le LessonType : " + category + ", " + targetAudience);
        try {
            List<Instructor> instructors = daoInstructor.getInstructorsByLessonType(category, targetAudience);
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

    
    private void fillSemaineComboBox() {
        Calendar calendar = Calendar.getInstance();
        
        // Début de la saison : samedi 6 décembre 2024
        calendar.set(2024, Calendar.DECEMBER, 8);

        // Fin de la saison : samedi 3 mai 2025
        Calendar endSeason = Calendar.getInstance();
        endSeason.set(2025, Calendar.MAY, 3);

        int weekNumber = 1;  // Numérotation des semaines

        // Remplissage du JComboBox pour chaque semaine entre le début et la fin de la saison
        while (!calendar.after(endSeason)) {
            // Début de la semaine (samedi)
            String debutSemaine = String.format("%1$td/%1$tm/%1$tY", calendar);

            // Fin de la semaine (vendredi suivant)
            calendar.add(Calendar.DAY_OF_WEEK, 5);
            String finSemaine = String.format("%1$td/%1$tm/%1$tY", calendar);

            // Ajouter la semaine au JComboBox
            comboSemaine.addItem("Semaine " + weekNumber + ": " + debutSemaine + " - " + finSemaine);

            // Avancer au samedi suivant pour la prochaine semaine
            calendar.add(Calendar.DAY_OF_WEEK, 2);
            weekNumber++;
        }
    }
}*/

package be.flas.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Calendar;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import be.flas.connection.DatabaseConnection;
import be.flas.dao.DAOInstructor;
import be.flas.dao.DAOLesson;
import be.flas.dao.DAOLessonType;
import be.flas.dao.DAOPeriod;
import be.flas.dao.DAOSkier;
import be.flas.model.Instructor;
import be.flas.model.Period;
import be.flas.model.Skier;

public class FormChooseInstructor extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private DAOInstructor daoInstructor;
    private DAOLessonType daoLessonType;
    private DAOSkier daoSkier;
    private DAOLesson daoLesson;
    private DAOPeriod daoPeriod;
    private Connection sharedConnection;
    private JComboBox<String> comboInstructor;
    private JComboBox<String> comboLessonType;
    private JComboBox<String> comboSemaine; 
    private JComboBox<String> comboSkier;
    private JComboBox<String> comboPeriod;
    private ButtonGroup buttonGroup; // Sélecteur de semaine
    private JComboBox comboBox;
    private JComboBox comboBox_1;

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
        daoSkier = new DAOSkier(sharedConnection);
        daoLesson = new DAOLesson(sharedConnection);
        daoPeriod = new DAOPeriod(sharedConnection);

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
        comboInstructor.setBounds(149, 200, 161, 21);
        panel.add(comboInstructor);

        comboLessonType = new JComboBox<>();
        comboLessonType.setBounds(32, 150, 410, 21);
        panel.add(comboLessonType);

        comboSemaine = new JComboBox<>();  // Création du JComboBox pour les semaines
        comboSemaine.setBounds(32, 100, 410, 21);
        panel.add(comboSemaine);

        JButton btnChoisir = new JButton("Choisir");
        btnChoisir.setBounds(189, 443, 85, 21);
        panel.add(btnChoisir);
        
        JRadioButton RadioButton1 = new JRadioButton("Matin");
        RadioButton1.setBounds(145, 291, 103, 21);
        panel.add(RadioButton1);
        
        JRadioButton RadioButton2 = new JRadioButton("Après-midi");
        RadioButton2.setBounds(145, 322, 103, 21);
        panel.add(RadioButton2);
        
        buttonGroup = new ButtonGroup();
        buttonGroup.add(RadioButton1);
        buttonGroup.add(RadioButton2);
        
        
        
        comboSkier = new JComboBox<>();
        comboSkier.setBounds(32, 63, 286, 21);
        panel.add(comboSkier);
        
        comboPeriod = new JComboBox();
        comboPeriod.setBounds(32, 32, 329, 21);
        panel.add(comboPeriod);

        fillPeriodComboBox();
        fillSkierComboBox();
        fillLessonTypeComboBox();
        fillSemaineComboBox();  // Appeler pour remplir le JComboBox avec les semaines

        // Ajout d'un ActionListener pour comboLessonType
        comboLessonType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedLessonType = (String) comboLessonType.getSelectedItem();
                String[] parts = selectedLessonType.split(" - ");

                if (parts.length >= 3) {
                    String extractedCategory = parts[2];
                    String extractedTargetAudience = parts[3];
                    updateInstructorsForLessonType(extractedCategory, extractedTargetAudience);
                } else {
                    System.err.println("Format inattendu pour le type de leçon sélectionné : " + selectedLessonType);
                }
            }
        });

        // ActionListener pour bouton Choisir, qui montre toutes les sélections
        btnChoisir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String semaineChoisie = (String) comboPeriod.getSelectedItem();
                String instructorChoisi = (String) comboInstructor.getSelectedItem();
                String lessonTypeChoisi = (String) comboLessonType.getSelectedItem();
                String skierChoisi = (String) comboSkier.getSelectedItem();
                if (skierChoisi != null) {
                    // Extraire le pseudo de l'affichage (par exemple, si l'affichage est "Nom Prénom (Pseudo)")
                    String pseudo = skierChoisi.substring(skierChoisi.lastIndexOf("(") + 1, skierChoisi.lastIndexOf(")")).trim();
                    int idI=daoSkier.getSkierIdByName(pseudo);
                    System.out.println("id skier : "+ idI);
                    
                }
                
                String selectedTime = RadioButton1.isSelected() ? "Matin" : "Après-midi";

                // Afficher toutes les sélections
                String message = String.format("Vous avez choisi :\nSemaine: %s\nInstructeur: %s\nType de leçon: %s\nMoment de la journée: %s skier : %s",
                                                semaineChoisie, instructorChoisi, lessonTypeChoisi, selectedTime,skierChoisi);
                
                
                
             // Exemple d'utilisation lorsque vous avez besoin de récupérer l'ID
                String selectedItem = (String) comboInstructor.getSelectedItem();
                if (selectedItem != null) {
                    // Extraire le pseudo de l'affichage (par exemple, si l'affichage est "Nom Prénom (Pseudo)")
                    String pseudo = selectedItem.substring(selectedItem.lastIndexOf("(") + 1, selectedItem.lastIndexOf(")")).trim();
                    int idI=daoInstructor.getInstructorIdByName(pseudo);
                    System.out.println(idI);
                    
                }
             // Extraction des informations sélectionnées
                String lessonTypeChoisi2 = (String) comboLessonType.getSelectedItem();
                String[] selectedItem2 = lessonTypeChoisi.split(" - "); // Supposons que vous avez un format "niveau - catégorie - public cible - autre info"

                // Assurez-vous que le tableau contient suffisamment d'éléments avant d'accéder aux indices
                if (selectedItem2.length >= 4) {
                    String niveau = selectedItem2[0]; // Par exemple, le niveau
                    String categorie = selectedItem2[1]; // Catégorie
                    String publicCible = selectedItem2[2]; // Public cible
                    String autreInfo = selectedItem2[3]; // Autres informations

                    // Appelez la méthode pour récupérer l'ID du type de leçon
                    int idL = daoLessonType.getLessonTypeIdByName(niveau, Double.parseDouble(categorie), publicCible, autreInfo);
                    System.out.println("ID du type de leçon sélectionné: " + idL);
                } else {
                    System.err.println("Format inattendu pour le type de leçon sélectionné : " + lessonTypeChoisi);
                }
                

                //daoLesson.createLesson(instructorChoisi, ALLBITS, null, null, ABORT);
                
                JOptionPane.showMessageDialog(null, message);
            }
        });
    }

    private void updateInstructorsForLessonType(String category, String targetAudience) {
        System.out.println("Mise à jour des instructeurs pour le LessonType : " + category + ", " + targetAudience);
        try {
            List<Instructor> instructors = daoInstructor.getInstructorsByLessonType(category, targetAudience);
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

            for (Instructor instructor : instructors) {
                //String displayText = instructor.getName() + " " + instructor.getFirstName();
                String displayText = instructor.getName() + " " + instructor.getFirstName() + " (" + instructor.getPseudo() + ")";

                model.addElement(displayText);
            }

            comboInstructor.setModel(model);
            System.out.println("Instructeurs mis à jour : " + instructors);
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour des instructeurs : " + e.getMessage());
        }
    }

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
    private void fillSkierComboBox() {
    	System.out.println("skier chargé ");
        try {
            List<Skier> skiers = daoSkier.getAllSkiers();
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

            for (Skier skier : skiers) {
                //String displayText = instructor.getName() + " " + instructor.getFirstName();
                String displayText = skier.getName() + " " + skier.getFirstName() + " (" + skier.getPseudo() + ")";

                model.addElement(displayText);
            }

            comboSkier.setModel(model);
            System.out.println("Instructeurs mis à jour : " + skiers);
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour des instructeurs : " + e.getMessage());
        }
    }

    private void fillPeriodComboBox() {
    	System.out.println("period chargé ");
        try {
            List<Period> periods = daoPeriod.getAllPeriods();
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

            for (Period period : periods) {
                //String displayText = instructor.getName() + " " + instructor.getFirstName();
                String displayText = period.getStartDate() + " " + period.getEndDate() + " (" + period.isVacation() + ")";

                model.addElement(displayText);
            }

            comboPeriod.setModel(model);
            System.out.println("period mis à jour : " +periods);
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour des period : " + e.getMessage());
        }
    }
    private void fillSemaineComboBox() {
        Calendar calendar = Calendar.getInstance();
        
        // Début de la saison : samedi 6 décembre 2024
        calendar.set(2024, Calendar.DECEMBER, 6);

        // Fin de la saison : samedi 3 mai 2025
        Calendar endSeason = Calendar.getInstance();
        endSeason.set(2025, Calendar.MAY, 3);

        int weekNumber = 1;  // Numérotation des semaines

        // Remplissage du JComboBox pour chaque semaine entre le début et la fin de la saison
        while (!calendar.after(endSeason)) {
            // Début de la semaine (samedi)
            String debutSemaine = String.format("%1$td/%1$tm/%1$tY", calendar);

            // Fin de la semaine (vendredi suivant)
            calendar.add(Calendar.DAY_OF_WEEK, 5);
            String finSemaine = String.format("%1$td/%1$tm/%1$tY", calendar);

            // Ajouter la semaine au JComboBox
            comboSemaine.addItem("Semaine " + weekNumber + ": " + debutSemaine + " - " + finSemaine);

            // Avancer au samedi suivant pour la prochaine semaine
            calendar.add(Calendar.DAY_OF_WEEK, 2);
            weekNumber++;
        }
    }
}




