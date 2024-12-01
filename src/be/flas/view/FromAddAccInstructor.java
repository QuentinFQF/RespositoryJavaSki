




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


import be.flas.model.Accreditation;

import be.flas.model.Instructor;


import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;

public class FromAddAccInstructor extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JComboBox<String> comboInstructor;
    private JComboBox<String> comboAccreditation;
    
   
    private Map<String, Integer> instructorIdMap = new HashMap<>();
    private Map<String, Integer> accreditationIdMap = new HashMap<>();
    private JButton btnNewButton;

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
        
        btnNewButton = new JButton("Retour");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
                try {
                    
                    FormStart frame = new FormStart();
                    frame.setVisible(true);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
			}
		});
		
        btnNewButton.setBounds(40, 341, 85, 21);
        panel.add(btnNewButton);
        
        JLabel lblNewLabel = new JLabel("Moniteurs");
        lblNewLabel.setBounds(38, 110, 72, 13);
        panel.add(lblNewLabel);
        
        JLabel lblNewLabel_1 = new JLabel("Accréditations");
        lblNewLabel_1.setBounds(38, 206, 87, 13);
        panel.add(lblNewLabel_1);

       
        listInstructors();
        
    
        comboInstructor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              
                Integer selectedInstructorId = getSelectedInstructorId();
                System.out.println(selectedInstructorId);
                
              
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
       	    	 i.saveAccIns(selectedInstructorId, selectedAccreditationId);
                    
                    System.out.println("ajoute avec success");
       	        
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

            
            instructorIdMap.clear();

            for (Instructor instructor : instructors) {
               
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
    public Integer getSelectedAccreditationId() {
        String selectedItem = (String) comboAccreditation.getSelectedItem();
        if (selectedItem != null && accreditationIdMap.containsKey(selectedItem)) {
            return accreditationIdMap.get(selectedItem);
        }
        return null; 
    }

    
    private void listAccreditations(int instructorId) {
        try {
           
            List<Accreditation> accreditations = Accreditation.selectAccDiffIns(instructorId);
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

            
            accreditationIdMap.clear();

            for (Accreditation accreditation : accreditations) {
               
                String displayText = accreditation.getName() + " " + accreditation.getSport() + " " + accreditation.getAgeCategory();

             
                model.addElement(displayText);

         
                accreditationIdMap.put(displayText, accreditation.getId());
            }

           
            comboAccreditation.setModel(model);
            System.out.println("Accréditations mises à jour : " + accreditations);
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour des accréditations : " + e.getMessage());
        }
    }
}


