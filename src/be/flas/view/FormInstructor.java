package be.flas.view;

import java.awt.EventQueue;

import java.time.LocalDate;
import java.time.ZoneId;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.Date;
import java.util.List;



import be.flas.model.Accreditation;
import be.flas.model.Instructor;

public class FormInstructor extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField TxNom;
    private JTextField txPrenom;
    private JTextField txPseudo;
    private JComboBox<Accreditation> comboAccreditation;
    
    
  
    

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FormInstructor frame = new FormInstructor();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public FormInstructor() {
        setTitle("Formulaire moniteur");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 799, 546);
        
        
        
        

        
        
       

        contentPane = new JPanel();
        contentPane.setBorder(new TitledBorder(null, "Inscription moniteur", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 128, 128));
        panel.setBounds(255, 149, 333, 235);
        contentPane.add(panel);
        panel.setLayout(null);

        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setBounds(123, 119, 70, 21);
        panel.add(dateChooser);

        JLabel lblNewLabel_3 = new JLabel("Date de naissance");
        lblNewLabel_3.setBounds(28, 119, 85, 13);
        panel.add(lblNewLabel_3);

        comboAccreditation = new JComboBox<>();
        comboAccreditation.setBounds(123, 30, 200, 21);
        panel.add(comboAccreditation);

        fillAccreditationComboBox();

        JButton btnNewButton = new JButton("Créer");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nom = TxNom.getText();
                String prenom = txPrenom.getText();
                String pseudo = txPseudo.getText();
                
                if (nom == null || nom.trim().isEmpty() || !nom.matches("[a-zA-Z]{1,50}")) {
                    JOptionPane.showMessageDialog(FormInstructor.this, "Erreur : le nom doit contenir uniquement des lettres et être de 50 caractères maximum.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (prenom == null || prenom.trim().isEmpty() || !prenom.matches("[a-zA-Z]{1,50}")) {
                    JOptionPane.showMessageDialog(FormInstructor.this, "Erreur : le prénom doit contenir uniquement des lettres et être de 50 caractères maximum.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (pseudo == null || pseudo.trim().isEmpty() || !pseudo.matches("[a-zA-Z]{1,50}")) {
                    JOptionPane.showMessageDialog(FormInstructor.this, "Erreur : le pseudo doit contenir uniquement des lettres et être de 50 caractères maximum.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                
                Date date = dateChooser.getDate();
		        if (date == null) {
		            System.out.println("Erreur : la date de naissance ne peut pas être vide.");
		            JOptionPane.showMessageDialog(null, "Erreur : la date de naissance ne peut pas être vide.");
		            return;
		        }

		        LocalDate dob = dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		        
		        if (dob.isAfter(LocalDate.now())) {
		            System.out.println("Erreur : la date de naissance doit être dans le passé.");
		            JOptionPane.showMessageDialog(null, "Erreur : la date de naissance doit être dans le passé.");
		            return;
		        }
		        

               
                Accreditation selected = (Accreditation) comboAccreditation.getSelectedItem();
                if (selected == null) 
                { 
                	JOptionPane.showMessageDialog(FormInstructor.this, "Veuillez sélectionner une accréditation.", "Erreur", JOptionPane.ERROR_MESSAGE); 
                	return; 
                }
                System.out.println(selected.getId());

                Accreditation accreditation = new Accreditation(selected.getId());
                Instructor instructor = new Instructor(nom, prenom, dob, accreditation, pseudo);

                System.out.println("Accréditation sélectionnée: " + selected);
                System.out.println(instructor.toString());
                
                
                boolean success = instructor.save();
                
                if (success) {
                	JOptionPane.showMessageDialog(null, "ins et acc réussi !");
                } else {
                	JOptionPane.showMessageDialog(null, "echec ins et acc");
                }
            }
        });
        btnNewButton.setBounds(238, 204, 85, 21);
        panel.add(btnNewButton);

        txPseudo = new JTextField();
        txPseudo.setBounds(123, 149, 96, 19);
        panel.add(txPseudo);
        txPseudo.setColumns(10);

        JLabel lblNewLabel_2 = new JLabel("Pseudo");
        lblNewLabel_2.setBounds(28, 152, 45, 13);
        panel.add(lblNewLabel_2);

        txPrenom = new JTextField();
        txPrenom.setBounds(123, 90, 96, 19);
        panel.add(txPrenom);
        txPrenom.setColumns(10);

        TxNom = new JTextField();
        TxNom.setBounds(123, 61, 96, 19);
        panel.add(TxNom);
        TxNom.setColumns(10);

        JLabel lblNewLabel = new JLabel("Nom");
        lblNewLabel.setBounds(28, 64, 45, 13);
        panel.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Prénom");
        lblNewLabel_1.setBounds(28, 93, 45, 13);
        panel.add(lblNewLabel_1);

        JButton btnNewButton_1 = new JButton("Retour");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    FormStart frame = new FormStart();
                    frame.setVisible(true);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        });
        btnNewButton_1.setBounds(10, 204, 85, 21);
        panel.add(btnNewButton_1);

        JLabel lblNewLabel_4 = new JLabel("Inscription moniteur");
        lblNewLabel_4.setFont(new Font("Times New Roman", Font.BOLD, 30));
        lblNewLabel_4.setBackground(new Color(255, 255, 255));
        lblNewLabel_4.setBounds(255, 50, 286, 53);
        contentPane.add(lblNewLabel_4);
    }

    
    private void fillAccreditationComboBox() {
        System.out.println("Début du remplissage du JComboBox avec les accréditations.");
        try {
           
            List<Accreditation> accreditations = Accreditation.getAll();

            
            DefaultComboBoxModel<Accreditation> model = new DefaultComboBoxModel<>();

            
            for (Accreditation accreditation : accreditations) {
                model.addElement(accreditation);
            }

            
            comboAccreditation.setModel(model);
            
            System.out.println("Accréditations chargées avec succès : " + accreditations);
        } catch (Exception e) {
            System.err.println("Erreur lors du remplissage du JComboBox : " + e.getMessage());
            JOptionPane.showMessageDialog(this, 
                "Erreur lors du chargement des accréditations : " + e.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    

    


    
}
