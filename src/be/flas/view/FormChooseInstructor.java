
package be.flas.view;

import java.awt.*;




import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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



public class FormChooseInstructor extends JFrame {

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
    private Map<String, Integer> lessonTypeIdMap = new HashMap<>();
    private Map<String, Integer> skierIdMap = new HashMap<>();
    private Map<String, Integer> instructorIdMap = new HashMap<>();
    private Map<String, Integer> periodIdMap = new HashMap<>();

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
        //fillSemaineComboBox();  // Appeler pour remplir le JComboBox avec les semaines

        // Ajout d'un ActionListener pour comboLessonType
        comboLessonType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedLessonType = (String) comboLessonType.getSelectedItem();
                Integer selectedLessonTypeId = getSelectedLessonTypeId();
                String[] parts = selectedLessonType.split(" - ");

                System.out.println(selectedLessonTypeId);
                
                updateInstructorsForLessonType(selectedLessonTypeId);
            }
        });

        
        
        
        btnChoisir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String semaineChoisie = (String) comboPeriod.getSelectedItem();
                String instructorChoisi = (String) comboInstructor.getSelectedItem();
                String lessonTypeChoisi = (String) comboLessonType.getSelectedItem();
                String skierChoisi = (String) comboSkier.getSelectedItem();
                
                Integer selectedSkierId = getSelectedSkierId();
                Integer selectedLessonTypeId = getSelectedLessonTypeId();
                Integer selectedInstructorId = getSelectedInstructorId();
                Integer selectedPeriodId = getSelectedPeriodId();

                System.out.println("skier id "+selectedSkierId);
                System.out.println("lessontype id "+selectedLessonTypeId);
                System.out.println("ins id "+selectedInstructorId);
                System.out.println("period id "+selectedPeriodId);

                //String timeSlot = RadioButton1.isSelected() ? "Matin" : "Après-midi";
             // Vérification de la validité des sélections
                if (selectedPeriodId == null ) {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner une période valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (selectedInstructorId == null) {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un instructeur.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (selectedLessonTypeId == null) {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un type de leçon.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (selectedSkierId == null) {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un skieur.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Vérification de la sélection des boutons radio pour le créneau horaire
                String timeSlot = null;
                int[] tabTime = new int[2]; 
                if (RadioButton1.isSelected()) {
                    timeSlot = "Matin";
                    tabTime[0]=9;
                    tabTime[1]=12;
                } else if (RadioButton2.isSelected()) {
                    timeSlot = "Après-midi";
                    tabTime[0]=14;
                    tabTime[1]=17;
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un créneau horaire (Matin ou Après-midi).", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                System.out.println("timesslot "+timeSlot);

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

               

                // Vérifier si les IDs sont valides avant de créer la leçon ou la réservation
                if (selectedSkierId != -1 && selectedLessonTypeId != -1 && startDate != null && endDate != null && selectedPeriodId != -1) {
                    
                	Lesson lesson=new Lesson();
                
                	LessonType lessonType=LessonType.getLesson(selectedLessonTypeId);
                	int[] minMax;
                	System.out.println("age lt : "+lessonType.getAccreditation().getAgeCategory());
                	
                	minMax = lesson.getMinAndMaxBooking(lessonType.getAccreditation().getAgeCategory(), timeSlot);
                	int[] timeRange = lesson.getStartAndEndTime(timeSlot);
                	System.out.println("time : "+timeRange[0]+timeRange[1]);
                	System.out.println(minMax[0]+ " "+minMax[1]);
                	System.out.println(lessonType.getAccreditation().getAgeCategory());
                  
                	LocalDate dateBooking = LocalDate.now();
                	Period p=new Period(selectedPeriodId);
                	Skier s=new Skier(selectedSkierId);
                	Instructor i=new Instructor(selectedInstructorId);
                	LessonType lt=new LessonType(selectedLessonTypeId);
                	
                	Lesson lesson2=new Lesson(minMax[0],minMax[1],i,lt,timeSlot,"Collectif",0);
                	
                	System.out.println("min "+lesson2.getMinBookings());
                	System.out.println("max "+lesson2.getMaxBookings());
                	
                	Instructor instructor = Instructor.find(selectedInstructorId);
                	Skier skier = Skier.find(selectedSkierId);

                	
                	int idLesson = instructor.getLessonId(selectedLessonTypeId, timeSlot, "Collectif", minMax[0], minMax[1],selectedPeriodId);

                    if (skier.isSkierInLesson(selectedSkierId,selectedPeriodId,timeSlot)) {
                    	JOptionPane.showMessageDialog(null, "Le skieur est déjà inscrit à cette period", "Erreur d'inscription", JOptionPane.ERROR_MESSAGE);
                    	return;
                	}
                    if (instructor.isLessonComplete(idLesson)) {
        	            JOptionPane.showMessageDialog(null, "La leçon est complète. Pour choisir cette leçon, veuillez prendre un autre moniteur.", "Leçon pleine", JOptionPane.WARNING_MESSAGE);
        	            return;
        	        }
                    if (instructor.isAvailables(selectedPeriodId, timeSlot)){
                    	JOptionPane.showMessageDialog(null, "L'instructeur n'est pas disponible à ce créneau horaire.", "Indisponibilité", JOptionPane.ERROR_MESSAGE);
                    	return;
                    }
                    
                	
                	           
    	            if (idLesson == -1) {
    	        
    	            	Lesson lesson3=new Lesson(minMax[0],minMax[1],i,lt,timeSlot,"Collectif",0,idLesson,timeRange[0],timeRange[1]);
                		Booking booking = new Booking(dateBooking, s, p, lesson3, i);

                		//booking.save();
                    	JOptionPane.showMessageDialog(null, "Réservation créée avec succès!");
    	            } else {
    	                
    	                Lesson existingLesson = new Lesson(idLesson);
    	                //existingLesson.update();
    	                JOptionPane.showMessageDialog(null, "Leçon mise à jour!", "Mise à jour", JOptionPane.INFORMATION_MESSAGE);
    	            }
                	        
                	    
                	

                	
                    
                }else {
                	JOptionPane.showMessageDialog(null, "Instructeur indisponible", "Disponibilité", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }
        


    private void updateInstructorsForLessonType(int id) {
        //System.out.println("Mise à jour des instructeurs pour le LessonType : " + category + ", " + targetAudience);
        try {
            //List<Instructor> instructors = daoInstructor.getInstructorsByLessonType(category, targetAudience);
        	List<Instructor> instructors = Instructor.getAllInstructorsWithAAndLTWithId(id);
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

            for (Instructor instructor : instructors) {
                //String displayText = instructor.getName() + " " + instructor.getFirstName();
                String displayText = instructor.getName() + " " + instructor.getFirstName() + " (" + instructor.getPseudo() + ")";

                model.addElement(displayText);
                instructorIdMap.put(displayText, instructor.getPersonId());
            }

            comboInstructor.setModel(model);
            System.out.println("Instructeurs mis à jour : " + instructors);
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour des instructeurs : " + e.getMessage());
        }
    }
    public Integer getSelectedInstructorId() {
        String selectedItem = (String) comboInstructor.getSelectedItem();
        if (selectedItem != null && instructorIdMap.containsKey(selectedItem)) {
            return instructorIdMap.get(selectedItem);
        }
        return null; 
    }

    private void fillLessonTypeComboBox() {
        System.out.println("Début du remplissage du JComboBox avec les LessonTypes.");
        try {
            //List<String> lessonTypes = daoLessonType.selectLessonType();
        	List<LessonType> lessonTypes = LessonType.getAll();
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            for (LessonType lessonType : lessonTypes) {
                //String displayText = instructor.getName() + " " + instructor.getFirstName();
                String displayText = lessonType.getLevel() + " " + lessonType.getPrice() + " " + lessonType.getAccreditation().getSport() + " "+lessonType.getAccreditation().getAgeCategory() ;

                model.addElement(displayText);
                lessonTypeIdMap.put(displayText, lessonType.getId());
            }
            comboLessonType.setModel(model);
           
            System.out.println("LessonTypes chargés : " + lessonTypes);
        } catch (Exception e) {
            System.err.println("Erreur lors du remplissage du JComboBox : " + e.getMessage());
        }
    }
    public Integer getSelectedLessonTypeId() {
        String selectedItem = (String) comboLessonType.getSelectedItem();
        if (selectedItem != null && lessonTypeIdMap.containsKey(selectedItem)) {
            return lessonTypeIdMap.get(selectedItem);
        }
        return null; 
    }
    private void fillSkierComboBox() {
    	System.out.println("skier chargé ");
        try {
         
        	List<Skier> skiers = Skier.getAll();
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

            for (Skier skier : skiers) {
            
                String displayText = skier.getName() + " " + skier.getFirstName() + " (" + skier.getPseudo() + ")";

                model.addElement(displayText);
                skierIdMap.put(displayText, skier.getPersonId());
            }

            comboSkier.setModel(model);
            System.out.println("Instructeurs mis à jour : " + skiers);
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour des instructeurs : " + e.getMessage());
        }
    }
    public Integer getSelectedSkierId() {
        String selectedItem = (String) comboSkier.getSelectedItem();
        if (selectedItem != null && skierIdMap.containsKey(selectedItem)) {
            return skierIdMap.get(selectedItem);
        }
        return null; 
    }

    private void fillPeriodComboBox() {
    	System.out.println("period chargé ");
        try {
           
        	List<Period> periods = Period.getAll();
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

            for (Period period : periods) {
             
                String displayText = period.getStartDate() + " " + period.getEndDate() + " (" + period.isVacation() + ")";

                model.addElement(displayText);
                periodIdMap.put(displayText, period.getId());
            }

            comboPeriod.setModel(model);
            System.out.println("period mis à jour : " +periods);
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour des period : " + e.getMessage());
        }
    }
    public Integer getSelectedPeriodId() {
        String selectedItem = (String) comboPeriod.getSelectedItem();
        if (selectedItem != null && periodIdMap.containsKey(selectedItem)) {
            return periodIdMap.get(selectedItem);
        }
        return null; 
    }
    
}





