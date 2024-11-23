/*package be.flas.view;

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
    private Map<String, Integer> lessonTypeIdMap = new HashMap<>();
    private Map<String, Integer> skierIdMap = new HashMap<>();
    private Map<String, Integer> instructorIdMap = new HashMap<>();
    private Map<String, Integer> periodIdMap = new HashMap<>();

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
        //fillLessonTypeComboBox();
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
                int nbrHours=0;
                if (RadioButton1.isSelected()) {
                    timeSlot = "1 heure";
                    nbrHours=1;
                } else if (RadioButton2.isSelected()) {
                    timeSlot = "2 heures";
                    nbrHours=2;
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
                	
                   
                    //daoBooking.createBooking(idLesson,idK, idI , idP); // Assurez-vous que la méthode createBooking existe

                    // Message de confirmation
                	
                	Lesson lesson=new Lesson();
                	int[] minMax;
                	minMax = lesson.getMinAndMaxBooking(selectedItem2[3], timeSlot);
                    //int idLesson=daoLesson.createLesson(idI, idL,timeSlot,"Collectif",minMax[0],minMax[1]); 

                    
                	//System.out.println("cat ea : "+selectedItem2[3]);
                	//System.out.println("idlesson"+idLesson);
                	LocalDate dateBooking = LocalDate.now();
                	Period p=new Period(idP);
                	Skier s=new Skier(idK);
                	Instructor i=new Instructor(idI);
                	LessonType lt=new LessonType(idL);
                	
                	Lesson lesson2=new Lesson(minMax[0],minMax[1],i,lt,timeSlot,"Particulier",nbrHours);
                	int idLesson=daoLesson.getLessonId(idI,idL,timeSlot,"Particulier",minMax[0],minMax[1]);
                	System.out.println("id lesson : "+idLesson);
                	
                	//daoInstructor.isInstructorAvailable(idI, idP, timeSlot);
                	//System.out.println("est dispo : "+ daoInstructor.isInstructorAvailable(idI, idP, timeSlot));
                	if(i!=null) {
                		JOptionPane.showMessageDialog(null, "lesson pleine pour choisir cette lesson veuillez prendre un autre moniteur");
                	}else if(idLesson==-1 ) {
                		//create
                		
                		daoLesson.create(lesson2);
                		int idLessonCreate=daoLesson.getLessonId(idI,idL,timeSlot,"Particulier",minMax[0],minMax[1]);
                		
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
    
}*/


package be.flas.view;

import java.awt.*;




import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private Map<String, Integer> lessonTypeIdMap = new HashMap<>();
    private Map<String, Integer> skierIdMap = new HashMap<>();
    private Map<String, Integer> instructorIdMap = new HashMap<>();
    private Map<String, Integer> periodIdMap = new HashMap<>();

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
        JLabel labelPrix = new JLabel("Prix total : ");
        labelPrix.setFont(new Font("Arial", Font.BOLD, 14));
        labelPrix.setBounds(32, 370, 400, 21); 
        panel.add(labelPrix);

        JButton btnCalculPrix = new JButton("Calculer le Prix");
        btnCalculPrix.setBounds(32, 400, 150, 21);
        panel.add(btnCalculPrix);

        //fillPeriodComboBox();
        fillPeriodComboBoxWithSpecificDates();
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
                
                
                Integer selectedSkierId = getSelectedSkierId();
                Integer selectedLessonTypeId = getSelectedLessonTypeId();
                Integer selectedInstructorId = getSelectedInstructorId();
                Integer selectedPeriodId = getSelectedPeriodId();
                String dateChoose = (String) comboPeriod.getSelectedItem();

                System.out.println("skier id "+selectedSkierId);
                System.out.println("lessontype id "+selectedLessonTypeId);
                System.out.println("ins id "+selectedInstructorId);
                System.out.println("period id "+selectedPeriodId);
                System.out.println("date choisie "+dateChoose);

                if (dateChoose == null ) {
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

              
                String timeSlot = null;
                int[] tabTime = new int[2]; 
                if (RadioButton1.isSelected()) {
                    timeSlot = "1 heure";
                    tabTime[0]=12;
                    tabTime[1]=13;
                } else if (RadioButton2.isSelected()) {
                    timeSlot = "2 heures";
                    tabTime[0]=12;
                    tabTime[1]=14;
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un créneau horaire (Matin ou Après-midi).", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
             

                
                

               

       
                if (selectedSkierId != -1 && selectedLessonTypeId != -1 && dateChoose != null) {
                    
                	Lesson lesson=new Lesson();
                
                	LessonType lessonType=LessonType.getLesson(selectedLessonTypeId);
                	int[] minMax;
                	System.out.println("age lt : "+lessonType.getAccreditation().getAgeCategory());
                	
                	minMax = lesson.getMinAndMaxBooking(lessonType.getAccreditation().getAgeCategory(), timeSlot);
                	
                	System.out.println(minMax[0]+ " "+minMax[1]);
                	System.out.println(lessonType.getAccreditation().getAgeCategory());
                  
                	LocalDate dateBooking = LocalDate.now();
                	Period p=new Period(22);
                	Skier s=new Skier(selectedSkierId);
                	Instructor i=new Instructor(selectedInstructorId);
                	LessonType lt=new LessonType(selectedLessonTypeId);
                	
                	Lesson lesson2=new Lesson(minMax[0],minMax[1],i,lt,timeSlot,"Particulier",0);
                	
                	System.out.println("min "+lesson2.getMinBookings());
                	System.out.println("max "+lesson2.getMaxBookings());
                	
                 
                	
                	
                	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            		// Convertir la chaîne en LocalDate en utilisant le formatter
            		LocalDate parsedDate = LocalDate.parse(dateChoose, formatter);
                	
                	//int idLesson=Lesson.getLessonId(selectedInstructorId,selectedLessonTypeId,timeSlot,"Particulier",minMax[0],minMax[1]);
                	//System.out.println("id lesson : "+idLesson);
                	Instructor instructor = Instructor.find(selectedInstructorId);
                	Skier skier = Skier.find(selectedSkierId);

                	
                	
                	
                	int idLesson = instructor.getLessonIdForDate(selectedLessonTypeId, timeSlot, "Particulier", minMax[0], minMax[1],parsedDate);
                	
                	boolean b= p.isDateInVacationPeriod(parsedDate);
                	System.out.println("vac "+b);
                	System.out.println(" pda "+parsedDate+" db "+ dateBooking);
                	LocalDate today = LocalDate.of(2024, 12, 17);
                	boolean dateIsOk=p.isBookingAllowed(parsedDate, today/*dateBooking*/,b);
                	System.out.println("ok ? "+dateIsOk);
                	
                	
                	//System.out.println(p.isBookingAllowed(bookingDate, today, isVacation));
                	/*if(!dateIsOk) {
                		JOptionPane.showMessageDialog(null, "impossible de faire la reservation maintenant", "Erreur d'inscription", JOptionPane.ERROR_MESSAGE);
                    	return;
                	}*/
                	
                	if (skier.isSkierInLesson(selectedSkierId,parsedDate)) {
                    	JOptionPane.showMessageDialog(null, "Le skieur est déjà inscrit à cette period", "Erreur d'inscription", JOptionPane.ERROR_MESSAGE);
                    	return;
                	}
                    if (instructor.isLessonComplete(idLesson)) {
        	            JOptionPane.showMessageDialog(null, "La leçon est complète. Pour choisir cette leçon, veuillez prendre un autre moniteur.", "Leçon pleine", JOptionPane.WARNING_MESSAGE);
        	            return;
        	        }
                    if (instructor.isAvailableForDate(parsedDate, timeSlot)){
                    	JOptionPane.showMessageDialog(null, "L'instructeur n'est pas disponible à ce créneau horaire.", "Indisponibilité", JOptionPane.ERROR_MESSAGE);
                    	return;
                    }

                	
                	           
    	            if (idLesson == -1) {
    	        
    	            	//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                		// Convertir la chaîne en LocalDate en utilisant le formatter
                		//LocalDate parsedDate = LocalDate.parse(dateChoose, formatter);

                		int tarifId=lesson.getDurationInHours(timeSlot);
                		// Vous pouvez maintenant utiliser parsedDate dans la création de votre Lesson
                		Lesson lesson3 = new Lesson(minMax[0], minMax[1], i, lt, timeSlot, "Particulier", tarifId, idLesson, tabTime[0], tabTime[1], parsedDate);

                		// Créer la réservation
                		Booking booking = new Booking(dateBooking, s, p, lesson3, i);
                		//booking.save();
                		if(booking.isDateBeforeToday()) {
                			JOptionPane.showMessageDialog(null, "vous ne pouvez pas reserver pour une date expiré");
                			return;
                		}
                		
                		JOptionPane.showMessageDialog(null, "Réservation créée avec succès!");
    	            } else {
    	                
    	            	//update
                		Lesson l=new Lesson(idLesson);
                		//l.update();
                	
                		JOptionPane.showMessageDialog(null, "lesson mit à jour!");
    	            }
                	        
                	    
                	} else {
             
                	    JOptionPane.showMessageDialog(null, "L'instructeur n'est pas disponible à ce créneau horaire.", "Indisponibilité", JOptionPane.ERROR_MESSAGE);
                	}
                	
                	
                	
                    
            }
            
        });
        
        btnCalculPrix.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                	//boolean isChecked = ckAssurance.isSelected();
                	Integer selectedSkierId = getSelectedSkierId();
                    Integer selectedLessonTypeId = getSelectedLessonTypeId();
                    Integer selectedInstructorId = getSelectedInstructorId();
                    Integer selectedPeriodId = getSelectedPeriodId();
                    String dateChoose = (String) comboPeriod.getSelectedItem();
                	String timeSlot = null;
                	int[] tabTime = new int[2]; 
                    if (RadioButton1.isSelected()) {
                        timeSlot = "1 heure";
                        tabTime[0]=12;
                        tabTime[1]=13;
                    } else if (RadioButton2.isSelected()) {
                        timeSlot = "2 heures";
                        tabTime[0]=12;
                        tabTime[1]=14;
                    } else {
                        JOptionPane.showMessageDialog(null, "Veuillez sélectionner un créneau horaire (Matin ou Après-midi).", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                	
                	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            		// Convertir la chaîne en LocalDate en utilisant le formatter
            		LocalDate parsedDate = LocalDate.parse(dateChoose, formatter);
                	
                	if (selectedSkierId == null || selectedLessonTypeId == null || selectedInstructorId == null) {
                        JOptionPane.showMessageDialog(FormInscriptionParticulier.this, 
                            "Veuillez remplir tous les champs obligatoires.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                	Lesson lesson=new Lesson();
                    
                	LessonType lessonType=LessonType.getLesson(selectedLessonTypeId);
                	int[] minMax;
                	//System.out.println("age lt : "+lessonType.getAccreditation().getAgeCategory());
                	
                	minMax = lesson.getMinAndMaxBooking(lessonType.getAccreditation().getAgeCategory(), timeSlot);
                	//int[] timeRange = lesson.getStartAndEndTime(timeSlot);
                	
                	LocalDate dateBooking = LocalDate.now();
                	
                	Skier s=new Skier(selectedSkierId);
                	Period p=new Period(22);
                	Instructor i=new Instructor(selectedInstructorId);
                	LessonType lt=new LessonType(selectedLessonTypeId);
                	Instructor instructor = Instructor.find(selectedInstructorId);
                	int idLesson = instructor.getLessonIdForDate(selectedLessonTypeId, timeSlot, "Particulier", minMax[0], minMax[1],parsedDate);
                	
                	int tarifId=lesson.getDurationInHours(timeSlot);
            		
            		Lesson lesson3 = new Lesson(minMax[0], minMax[1], i, lt, timeSlot, "Particulier", tarifId, idLesson, tabTime[0], tabTime[1], parsedDate);

            		
            		Booking booking = new Booking(dateBooking, s,p, lesson3, i);
                	
            		System.out.println(booking.getLesson().getTarifId());
                	
                    double prixTotal = booking.calculatePrice(null,null);

                    labelPrix.setText("Prix total : " + prixTotal + " €");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(FormInscriptionParticulier.this, 
                        "Erreur lors du calcul du prix : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
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

   
    public Integer getSelectedPeriodId() {
        String selectedItem = (String) comboPeriod.getSelectedItem();
        if (selectedItem != null && periodIdMap.containsKey(selectedItem)) {
            return periodIdMap.get(selectedItem);
        }
        return null; 
    }
    
    /*private void fillPeriodComboBoxWithSpecificDates() {
        System.out.println("Chargement des dates spécifiques...");
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        LocalDate startDate = LocalDate.of(2024, 12, 6);
        LocalDate endDate = LocalDate.of(2025, 5, 3);

        // Parcours des dates par incréments journaliers
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            // Vérifiez si le jour est un samedi (DayOfWeek.SATURDAY)
            if (currentDate.getDayOfWeek() != java.time.DayOfWeek.SATURDAY) {
                model.addElement(currentDate.toString());
            }
            currentDate = currentDate.plusDays(1);
        }

        comboPeriod.setModel(model);
        System.out.println("Périodes chargées entre " + startDate + " et " + endDate + " sans les samedis");
    }*/
    private void fillPeriodComboBoxWithSpecificDates() {
        System.out.println("Chargement des dates spécifiques...");
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        // Date de début et date de fin
        LocalDate startDate = LocalDate.of(2024, 12, 6);
        LocalDate endDate = LocalDate.of(2025, 5, 3);

        // Créer un formatter pour le format 'dd-MM-yyyy'
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Parcours des dates par incréments journaliers
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            // Vérifiez si le jour est un samedi (DayOfWeek.SATURDAY)
            if (currentDate.getDayOfWeek() != java.time.DayOfWeek.SATURDAY) {
                // Formatage de la date dans le format 'dd-MM-yyyy'
                String formattedDate = currentDate.format(formatter);
                model.addElement(formattedDate);  // Ajouter la date formatée au comboBox
            }
            currentDate = currentDate.plusDays(1);  // Passer au jour suivant
        }

        // Mettre à jour le modèle du comboBox avec les dates formatées
        comboPeriod.setModel(model);
        System.out.println("Périodes chargées entre " + startDate + " et " + endDate + " sans les samedis");
    }


    
}





