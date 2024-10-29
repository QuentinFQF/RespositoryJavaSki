package be.flas.view;

import java.awt.EventQueue;



import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import com.toedter.calendar.JDateChooser;

import be.flas.dao.DAOInstructor;
import be.flas.dao.DAOSkier;
import be.flas.model.Skier;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;

public class FormSkier extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField Prenom;
	private JTextField Nom;
	private JTextField Pseudo;
	private DAOSkier daoSkier = new DAOSkier();
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
		
		
		
		
		JCheckBox Assurance = new JCheckBox("");
		Assurance.setBounds(191, 191, 27, 21);
		panel.add(Assurance);
		
		JLabel lblNewLabel_4 = new JLabel("Assurance");
		lblNewLabel_4.setBounds(73, 195, 45, 13);
		panel.add(lblNewLabel_4);
		
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
				
                String nom = Nom.getText();
                String prenom = Prenom.getText();
                String pseudo = Pseudo.getText();
                LocalDate dob=dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                boolean assuranceSelected = Assurance.isSelected();


                

              

                System.out.println("date stest : "+dob);
                Skier i=new Skier(nom,prenom,dob,pseudo,assuranceSelected);
                System.out.print(i.toString());  
              
                boolean success = daoSkier.create(i);
                boolean success2=daoSkier.testConnection();
                
                if (success) {
                    
                    System.out.println("skier ajouté avec succès !");
                } else {
                    
                    System.out.println("Erreur lors de l'ajout de skier.");
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
