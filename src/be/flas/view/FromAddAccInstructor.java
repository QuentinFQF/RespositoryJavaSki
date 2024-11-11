


/*package be.flas.view;

import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import be.flas.model.Accreditation;
import be.flas.model.Instructor;

import java.awt.Color;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class FromAddAccInstructor extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JComboBox<String> comboInstructor;
	private JComboBox<String> comboAccreditation;

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FromAddAccInstructor frame = new FromAddAccInstructor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public FromAddAccInstructor() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 838, 551);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 128, 128));
		panel.setBounds(288, 61, 350, 406);
		contentPane.add(panel);
		panel.setLayout(null);
		
		comboInstructor = new JComboBox<>();
		comboInstructor.setBounds(38, 141, 286, 21);
		panel.add(comboInstructor);
		
		comboAccreditation = new JComboBox<>();
		comboAccreditation.setBounds(41, 227, 283, 21);
		panel.add(comboAccreditation);
		listInstructors();
		//listAccreditations();
	}
	private void listInstructors() {
        
        try {
            List<Instructor> instructors = Instructor.getAllInstructorsWithAAndLT();
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
    private void listAccreditations() {
        
        try {
            List<Accreditation> accreditations = Accreditation.selectAccDiffIns();
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

            for (Accreditation accreditation : accreditations) {
                //String displayText = instructor.getName() + " " + instructor.getFirstName();
                String displayText = accreditation.getName() + " " + accreditation.getSport()+ " "+accreditation.getAgeCategory();

                model.addElement(displayText);
            }

            comboAccreditation.setModel(model);
            System.out.println("Instructeurs mis à jour : " + accreditations);
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour des instructeurs : " + e.getMessage());
        }
    }
}*/

package be.flas.view;

import java.awt.EventQueue;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import be.flas.model.Accreditation;
import be.flas.model.Booking;
import be.flas.model.Instructor;
import be.flas.model.Lesson;
import be.flas.model.LessonType;
import be.flas.model.Period;
import be.flas.model.Skier;

import java.awt.Color;
import javax.swing.JButton;

public class FromAddAccInstructor extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JComboBox<String> comboInstructor;
    private JComboBox<String> comboAccreditation;
    
    // HashMap pour stocker l'ID des instructeurs associés à leur affichage
    private Map<String, Integer> instructorIdMap = new HashMap<>();
    private Map<String, Integer> accreditationIdMap = new HashMap<>();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FromAddAccInstructor frame = new FromAddAccInstructor();
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
    public FromAddAccInstructor() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 838, 551);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 128, 128));
        panel.setBounds(236, 57, 350, 406);
        contentPane.add(panel);
        panel.setLayout(null);
        
        comboInstructor = new JComboBox<>();
        comboInstructor.setBounds(38, 141, 286, 21);
        panel.add(comboInstructor);
        
        comboAccreditation = new JComboBox<>();
        comboAccreditation.setBounds(41, 227, 283, 21);
        panel.add(comboAccreditation);
        
        JButton AddAccIns = new JButton("ajouter accréditation");
        AddAccIns.setBounds(161, 341, 163, 21);
        panel.add(AddAccIns);

        // Initialiser la liste des instructeurs
        listInstructors();
        
        // Ajouter un écouteur d'événements pour détecter le changement de sélection
        comboInstructor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Récupérer l'ID de l'instructeur sélectionné
                Integer selectedInstructorId = getSelectedInstructorId();
                System.out.println(selectedInstructorId);
                
                // Si un instructeur est sélectionné, mettre à jour la liste des accréditations
                if (selectedInstructorId != null) {
                    listAccreditations(selectedInstructorId);
                }
            }
        });
        
        
        AddAccIns.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            	
            	Integer selectedInstructorId = getSelectedInstructorId();
            	Integer selectedAccreditationId = getSelectedAccreditationId();
            	System.out.println( selectedInstructorId );
            	System.out.println(selectedAccreditationId);
            	if (selectedInstructorId != null && selectedAccreditationId != null) {
       	    	 Instructor i=new Instructor();
                    // Appel de la méthode pour sauvegarder la relation entre l'instructeur et l'accréditation
                    //i.saveAccIns(instructorId, accreditationId);
                    System.out.println("ajoute avec success");
       	        //saveAccIns(instructorId, accreditationId);
       	    } else {
       	        JOptionPane.showMessageDialog(null, "Veuillez sélectionner un instructeur et une accréditation.");
       	    }


               
            }
        });

        
        
        
        
        
        
        
        
        
        
    }

    private void listInstructors() {
        try {
            List<Instructor> instructors = Instructor.getAllInstructorsWithAAndLT();
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

            // Vider la map avant de la remplir
            instructorIdMap.clear();

            for (Instructor instructor : instructors) {
                // Texte à afficher dans le comboBox
                String displayText = instructor.getName() + " " + instructor.getFirstName() + " (" + instructor.getPseudo() + ")";
                
                // Ajouter le texte d'affichage au modèle
                model.addElement(displayText);

                // Associer l'affichage à l'ID de l'instructeur dans la HashMap
                instructorIdMap.put(displayText, instructor.getPersonId());
            }

            comboInstructor.setModel(model);
            System.out.println("Instructeurs mis à jour : " + instructors);
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour des instructeurs : " + e.getMessage());
        }
    }

    // Méthode pour récupérer l'ID de l'instructeur sélectionné
    public Integer getSelectedInstructorId() {
        String selectedItem = (String) comboInstructor.getSelectedItem();
        if (selectedItem != null && instructorIdMap.containsKey(selectedItem)) {
            return instructorIdMap.get(selectedItem);
        }
        return null; // Aucun instructeur sélectionné ou item invalide
    }
    public Integer getSelectedAccreditationId() {
        String selectedItem = (String) comboAccreditation.getSelectedItem();
        if (selectedItem != null && accreditationIdMap.containsKey(selectedItem)) {
            return accreditationIdMap.get(selectedItem);
        }
        return null; 
    }

    
    private void listAccreditations(int instructorId) {
        try {
            // Passer l'ID de l'instructeur pour récupérer les accréditations correspondantes
            List<Accreditation> accreditations = Accreditation.selectAccDiffIns(instructorId);
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

            // Vider la Map avant de la remplir
            accreditationIdMap.clear();

            for (Accreditation accreditation : accreditations) {
                // Texte à afficher pour chaque accréditation
                String displayText = accreditation.getName() + " " + accreditation.getSport() + " " + accreditation.getAgeCategory();

                // Ajouter l'accréditation au modèle du ComboBox
                model.addElement(displayText);

                // Associer l'affichage à l'ID de l'accréditation dans la HashMap
                accreditationIdMap.put(displayText, accreditation.getId());
            }

            // Mettre à jour le modèle du ComboBox
            comboAccreditation.setModel(model);
            System.out.println("Accréditations mises à jour : " + accreditations);
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour des accréditations : " + e.getMessage());
        }
    }

}


