package be.flas.view;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import be.flas.connection.DatabaseConnection;
import be.flas.dao.DAOBooking;
import be.flas.dao.DAOInstructor;
import be.flas.dao.DAOLesson;
import be.flas.dao.DAOLessonType;
import be.flas.dao.DAOPeriod;
import be.flas.dao.DAOSkier;
import be.flas.model.Booking;
import be.flas.model.Instructor;
import be.flas.model.Lesson;
import be.flas.model.LessonType;
import be.flas.model.Period;
import be.flas.model.Skier;

public class FormInscriptionParticulier extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private DAOInstructor daoInstructor;
    private DAOLessonType daoLessonType;
    private DAOSkier daoSkier;
    private DAOLesson daoLesson;
    private DAOPeriod daoPeriod;
    
    private DAOBooking daoBooking;
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
                    FormInscriptionParticulier frame = new FormInscriptionParticulier();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public FormInscriptionParticulier() {
        sharedConnection = DatabaseConnection.getInstance().getConnection();
        daoInstructor = new DAOInstructor(sharedConnection);
        daoLessonType = new DAOLessonType(sharedConnection);
        daoSkier = new DAOSkier(sharedConnection);
        daoLesson = new DAOLesson(sharedConnection);
        daoPeriod = new DAOPeriod(sharedConnection);
        daoBooking = new DAOBooking(sharedConnection);

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

        /*comboSemaine = new JComboBox<>();  // Création du JComboBox pour les semaines
        comboSemaine.setBounds(32, 100, 410, 21);
        panel.add(comboSemaine);*/

        JButton btnChoisir = new JButton("Choisir");
        btnChoisir.setBounds(189, 443, 85, 21);
        panel.add(btnChoisir);
        
        JRadioButton RadioButton1 = new JRadioButton("1 heure");
        RadioButton1.setBounds(145, 291, 103, 21);
        panel.add(RadioButton1);
        
        JRadioButton RadioButton2 = new JRadioButton("2 heures");
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
        //fillSemaineComboBox();  // Appeler pour remplir le JComboBox avec les semaines

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

        
        
        
        btnChoisir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String semaineChoisie = (String) comboPeriod.getSelectedItem();
                String instructorChoisi = (String) comboInstructor.getSelectedItem();
                String lessonTypeChoisi = (String) comboLessonType.getSelectedItem();
                String skierChoisi = (String) comboSkier.getSelectedItem();


                //String timeSlot = RadioButton1.isSelected() ? "1 heure" : "2 heures";
                String timeSlot = null;
                if (RadioButton1.isSelected()) {
                    timeSlot = "1 heure";
                } else if (RadioButton2.isSelected()) {
                    timeSlot = "2 heures";
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner une durée pour la leçon (1 heure ou 2 heures).", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Vérification que toutes les sélections sont faites
                if (semaineChoisie == null || semaineChoisie.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner une période valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (instructorChoisi == null || instructorChoisi.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un instructeur.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (lessonTypeChoisi == null || lessonTypeChoisi.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un type de leçon.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (skierChoisi == null || skierChoisi.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un skieur.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Récupération de l'ID du skieur
                int idK = -1;
                if (skierChoisi != null) {
                    String pseudoK = skierChoisi.substring(skierChoisi.lastIndexOf("(") + 1, skierChoisi.lastIndexOf(")")).trim();
                    idK = daoSkier.getSkierIdByName(pseudoK);
                    System.out.println("id skier : " + idK);
                }

                // Récupération de l'ID de l'instructeur
                int idI = -1;
                if (instructorChoisi != null) {
                    String pseudoI = instructorChoisi.substring(instructorChoisi.lastIndexOf("(") + 1, instructorChoisi.lastIndexOf(")")).trim();
                    idI = daoInstructor.getInstructorIdByName(pseudoI);
                    System.out.println("ID instructeur : " + idI);
                }

                // Récupération de l'ID du type de leçon
                int idL = -1;
                String[] selectedItem2 = lessonTypeChoisi.split(" - ");
                if (selectedItem2.length >= 4) {
                    String niveau = selectedItem2[0];
                    String categorie = selectedItem2[1];
                    String publicCible = selectedItem2[2];
                    String autreInfo = selectedItem2[3];

                    idL = daoLessonType.getLessonTypeIdByName(niveau, Double.parseDouble(categorie), publicCible, autreInfo);
                    System.out.println("ID du type de leçon sélectionné : " + idL);
                } else {
                    System.err.println("Format inattendu pour le type de leçon sélectionné : " + lessonTypeChoisi);
                }

                // Récupération des dates de la période choisie
                LocalDate startDate = null;
                LocalDate endDate = null;
                if (semaineChoisie != null) {
                    String[] parts = semaineChoisie.split(" ");
                    if (parts.length >= 2) {
                        try {
                            startDate = LocalDate.parse(parts[0].trim());
                            endDate = LocalDate.parse(parts[1].trim());
                        } catch (DateTimeParseException ex) {
                            System.err.println("Erreur lors du parsing des dates : " + ex.getMessage());
                        }
                    } else {
                        System.err.println("Format inattendu pour les dates de la période : " + semaineChoisie);
                    }
                } else {
                    System.err.println("Aucune période valide sélectionnée.");
                }

                // Récupération de l'ID de la période
                int idP = daoPeriod.getPeriodIdBy(startDate, endDate);
                if (idP != -1) {
                    System.out.println("ID de la période : " + idP);
                } else {
                    System.err.println("Aucune période trouvée pour les dates sélectionnées.");
                }

                // Vérifier si les IDs sont valides avant de créer la leçon ou la réservation
                if (idI != -1 && idL != -1 && startDate != null && endDate != null && idP != -1) {
                    // Appel à la méthode pour créer la leçon
                	System.out.println("nbr heures : "+timeSlot);
                	/*Lesson lesson=new Lesson();
                	int[] minMax;
                	minMax = lesson.getMinAndMaxBooking(selectedItem2[3], timeSlot);
                    int idLesson=daoLesson.createLesson(idI, idL,timeSlot,"Particulier",minMax[0],minMax[1]); 

                    
                	System.out.println("cat ea : "+selectedItem2[3]);
                	System.out.println("idlesson"+idLesson);
                	
                	LocalDate dateBooking = LocalDate.now();
                	Period p=new Period(idP);
                	Skier s=new Skier(idK);
                	Instructor i=new Instructor(idI);
                	Lesson l=new Lesson(idLesson);
                	//LocalDate date= java.sql.Date.valueOf(dateBooking);
                	
                	Booking b=new Booking(dateBooking,s,p,l,i);
                	
                    
                    //daoBooking.createBooking(idLesson,idK, idI , idP);
                	daoBooking.create(b);*/
                   
                    //daoBooking.createBooking(idLesson,idK, idI , idP); // Assurez-vous que la méthode createBooking existe

                    // Message de confirmation
                	
                	Lesson lesson=new Lesson();
                	int[] minMax;
                	minMax = lesson.getMinAndMaxBooking(selectedItem2[3], timeSlot);
                    //int idLesson=daoLesson.createLesson(idI, idL/*, selectedItem2[3]*/,timeSlot,"Collectif",minMax[0],minMax[1]); 

                    
                	//System.out.println("cat ea : "+selectedItem2[3]);
                	//System.out.println("idlesson"+idLesson);
                	LocalDate dateBooking = LocalDate.now();
                	Period p=new Period(idP);
                	Skier s=new Skier(idK);
                	Instructor i=new Instructor(idI);
                	LessonType lt=new LessonType(idL);
                	
                	Lesson lesson2=new Lesson(minMax[0],minMax[1],i,lt,timeSlot,"Particulier");
                	int idLesson=daoLesson.getLessonId(idI,idL,timeSlot,"Particulier",minMax[0],minMax[1]);
                	System.out.println("id lesson : "+idLesson);
                	
                	if(idLesson==-1) {
                		//create
                		
                		daoLesson.create(lesson2);
                		int idLessonCreate=daoLesson.getLessonId(idI,idL,timeSlot,"Collectif",minMax[0],minMax[1]);
                		
                		Lesson l=new Lesson(idLessonCreate);
                    	Booking b=new Booking(dateBooking,s,p,l,i);
                        
                    	daoBooking.create(b);
                    	JOptionPane.showMessageDialog(null, "Réservation créée avec succès!");
                		
                	}else {
                		//update
                		Lesson l=new Lesson(idLesson);
                		daoLesson.update(l);
                		JOptionPane.showMessageDialog(null, "lesson mit à jour!");
                	}
                	
                	
                	
                } else {
                    System.err.println("Impossible de créer la leçon ou la réservation, certains IDs sont invalides.");
                }
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
    /*private void fillSemaineComboBox() {
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
    }*/
}