package be.flas.view;

import java.awt.EventQueue;




import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import com.toedter.calendar.JDateChooser;


import be.flas.model.Skier;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class FormSkier extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField Prenom;
	private JTextField Nom;
	private JTextField Pseudo;

    
	
	
	private JDateChooser dateChooser;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FormSkier frame = new FormSkier();
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
	public FormSkier() {
		
        
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 795, 539);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 128, 128));
		panel.setBounds(201, 91, 383, 325);
		contentPane.add(panel);
		panel.setLayout(null);
		
		Prenom = new JTextField();
		Prenom.setBounds(191, 98, 96, 19);
		panel.add(Prenom);
		Prenom.setColumns(10);
		
		Nom = new JTextField();
		Nom.setBounds(191, 69, 96, 19);
		panel.add(Nom);
		Nom.setColumns(10);
		
		dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("yyyy-MM-dd");
		dateChooser.setBounds(191, 127, 96, 19);
		panel.add(dateChooser);
		
		JLabel lblNewLabel = new JLabel("Nom");
		lblNewLabel.setBounds(73, 72, 45, 13);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Prénom");
		lblNewLabel_1.setBounds(73, 97, 45, 19);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Date de naissance");
		lblNewLabel_2.setBounds(73, 126, 89, 26);
		panel.add(lblNewLabel_2);
		
		Pseudo = new JTextField();
		Pseudo.setBounds(191, 156, 96, 19);
		panel.add(Pseudo);
		Pseudo.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Pseudo");
		lblNewLabel_3.setBounds(73, 159, 45, 13);
		panel.add(lblNewLabel_3);
		
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
		btnNewButton_1.setBounds(10, 294, 85, 21);
		panel.add(btnNewButton_1);
		
		JButton btnNewButton = new JButton("Créer");
		btnNewButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        String nom = Nom.getText().trim();
		        String prenom = Prenom.getText().trim();
		        String pseudo = Pseudo.getText().trim();
		
		        

		      
		        if (nom.isEmpty()) {
		            System.out.println("Erreur : le champ 'Nom' ne peut pas être vide.");
		            JOptionPane.showMessageDialog(null, "Erreur : le champ 'Nom' ne peut pas être vide.");
		            return;
		        }
		        if (prenom.isEmpty()) {
		            System.out.println("Erreur : le champ 'Prénom' ne peut pas être vide.");
		            JOptionPane.showMessageDialog(null, "Erreur : le champ 'Prénom' ne peut pas être vide.");
		            return;
		        }
		        if (pseudo.isEmpty()) {
		            System.out.println("Erreur : le champ 'Pseudo' ne peut pas être vide.");
		            JOptionPane.showMessageDialog(null, "Erreur : le champ 'Pseudo' ne peut pas être vide.");
		            return;
		        }

		        
		        if (!nom.matches("[a-zA-Z]{1,50}")) {
		            System.out.println("Erreur : le nom doit contenir uniquement des lettres et être de 50 caractères maximum.");
		            JOptionPane.showMessageDialog(null, "Erreur : le nom doit contenir uniquement des lettres et être de 50 caractères maximum.");
		            return;
		        }
		        if (!prenom.matches("[a-zA-Z]{1,50}")) {
		            System.out.println("Erreur : le prénom doit contenir uniquement des lettres et être de 50 caractères maximum.");
		            JOptionPane.showMessageDialog(null, "Erreur : le prénom doit contenir uniquement des lettres et être de 50 caractères maximum.");
		            return;
		        }
		        if (!pseudo.matches("[a-zA-Z]{1,50}")) {
		            System.out.println("Erreur : le pseudo doit contenir uniquement des lettres et être de 50 caractères maximum.");
		            JOptionPane.showMessageDialog(null, "Erreur : le pseudo doit contenir uniquement des lettres et être de 50 caractères maximum.");
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


		      
		        Skier i = new Skier(nom, prenom, dob, pseudo);
		        System.out.print(i.toString());

		       
		        boolean success = i.save();

		        if (success) {
		            System.out.println("Skieur ajouté avec succès !");
		            JOptionPane.showMessageDialog(null, "Skieur ajouté avec succès !");
		        } else {
		            System.out.println("Erreur lors de l'ajout du skieur.");
		            JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout du skieur.");
		        }

		        
		        
		    }
		});
		btnNewButton.setBounds(288, 294, 85, 21);
		panel.add(btnNewButton);
		
		JLabel lblNewLabel_4_1 = new JLabel("Inscription skier");
		lblNewLabel_4_1.setFont(new Font("Times New Roman", Font.BOLD, 30));
		lblNewLabel_4_1.setBackground(Color.WHITE);
		lblNewLabel_4_1.setBounds(255, 28, 286, 53);
		contentPane.add(lblNewLabel_4_1);
	}
}
