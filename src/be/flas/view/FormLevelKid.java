package be.flas.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import be.flas.model.Accreditation;
import be.flas.model.Booking;
import be.flas.model.Instructor;
import be.flas.model.Lesson;
import be.flas.model.Skier;

import java.awt.Color;
import javax.swing.JComboBox;

public class FormLevelKid extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FormLevelKid frame = new FormLevelKid();
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
	public FormLevelKid() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 753, 543);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 128, 128));
		panel.setBounds(246, 59, 251, 409);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(74, 162, 120, 21);
		panel.add(comboBox);
		int instructorId = 1; // Remplacez par un ID existant dans votre base
        Instructor instructor = Instructor.find(1);
        
        




        
        //int idlesson=instructor.getLessonId(selectedInstructorId,selectedLessonTypeId,timeSlot,"Collectif",minMax[0],minMax[1]);

        // Vérification des résultats
        if (instructor != null) {
            System.out.println("Instructor trouvé :");
            System.out.println("Nom : " + instructor.getName());
            System.out.println("Prénom : " + instructor.getFirstName());
            System.out.println("Pseudo : " + instructor.getPseudo());
            System.out.println("Date de naissance : " + instructor.getDateOfBirth());

            // Afficher ses accréditations
            System.out.println("Accréditations :");
            for (Accreditation acc : instructor.getAccreditations()) {
                System.out.println("- " + acc.getName());
            }

            // Afficher ses leçons
            System.out.println("Leçons :");
            for (Lesson lesson : instructor.getLessons()) {
                System.out.println("- Leçon ID : " + lesson.getId());
                System.out.println("  Type : " + lesson.getLessonType().getLevel());
                System.out.println("  Min. réservations : " + lesson.getMinBookings());
                System.out.println("  Max. réservations : " + lesson.getMaxBookings());
            }

            // Afficher les réservations
            System.out.println("Réservations :");
            for (Booking booking : instructor.getBookings()) {
                System.out.println("- Réservation ID : " + booking.getId());
                System.out.println("  Skieur : " + booking.getSkier().getName());
                System.out.println("  Période : " + booking.getPeriod().getStartDate() + " - " + booking.getPeriod().getEndDate());
            }
        } else {
            System.out.println("Aucun instructeur trouvé avec l'ID " + instructorId);
        }

        // Fermer la connexion
        int skierId = 2;
    	Skier skier = Skier.find(skierId);

    	if (skier != null) {
    	    System.out.println("Détails du skieur :");
    	    System.out.println("Nom : " + skier.getName());
    	    System.out.println("Prénom : " + skier.getFirstName());
    	    System.out.println("Pseudo : " + skier.getPseudo());
    	    System.out.println("Assurance : " + (skier.isAssurance() ? "Oui" : "Non"));

    	    System.out.println("Réservations :");
    	    for (Booking booking : skier.getBookings()) {
    	        System.out.println("- Booking ID: " + booking.getId());

    	        // Vérifier si la réservation a une leçon
    	        if (booking.getLesson() != null) {
    	            Lesson lesson = booking.getLesson();
    	            System.out.println("  Type de leçon : " + lesson.getCourseType());

    	            // Vérifier si c'est un cours particulier
    	            if (lesson.getDate() != null) {
    	                System.out.println("  Cours particulier, Date : " + lesson.getDate());
    	            } else {
    	                // Si c'est un cours collectif, vérifier et afficher le créneau horaire
    	                if (lesson.getDayPart() != null) {
    	                    System.out.println("  Cours collectif, créneau horaire : " + lesson.getDayPart());
    	                } else {
    	                    System.out.println("  Cours collectif, créneau horaire non défini");
    	                }
    	            }

    	            // Vérifier si la réservation a une période associée
    	            if (booking.getPeriod() != null) {
    	                System.out.println("  Période : " + booking.getPeriod().getStartDate() + " - " + booking.getPeriod().getEndDate());
    	                System.out.println("- Period ID: " + booking.getPeriod().getId());
    	            }
    	        } else if (booking.getPeriod() != null) {
    	            // Si la réservation n'a pas de leçon, c'est probablement une réservation pour une période
    	            System.out.println("  Période : " + booking.getPeriod().getStartDate() + " - " + booking.getPeriod().getEndDate());
    	            System.out.println("- Period ID: " + booking.getPeriod().getId());
    	        }
    	    }
    	} else {
    	    System.out.println("Aucun skieur trouvé avec cet ID.");
    	}


        
    } 
	
	

	
	
 
	
}
