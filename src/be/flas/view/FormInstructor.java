package be.flas.view;

import java.awt.EventQueue;

import java.time.LocalDate;
import java.time.ZoneId;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import be.flas.dao.DAOInstructor;
import be.flas.model.Instructor;

import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import com.toedter.calendar.JDateChooser;
import javax.swing.border.TitledBorder;
import java.awt.Font;

public class FormInstructor extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField TxNom;
	private JTextField txPrenom;
	private JTextField txPseudo;
	private DAOInstructor daoInstructor = new DAOInstructor();

	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the frame.
	 */
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
		panel.setBounds(255, 149, 286, 235);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JDateChooser dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("yyyy-MM-dd");
		dateChooser.setBounds(123, 119, 70, 21);
		panel.add(dateChooser);
		
		JLabel lblNewLabel_3 = new JLabel("Date de naissance");
		lblNewLabel_3.setBounds(28, 119, 85, 13);
		panel.add(lblNewLabel_3);
		
		JButton btnNewButton = new JButton("Créer");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
                String nom = TxNom.getText();
                String prenom = txPrenom.getText();
                String pseudo = txPseudo.getText();
                LocalDate dob=dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

             
                

                Instructor i=new Instructor(nom,prenom,dob,pseudo);
                System.out.print(i.toString());  
         
                boolean success = daoInstructor.create(i);
                
                if (success) {
                    
                    System.out.println("Instructeur ajouté avec succès !");
                } else {
                    
                    System.out.println("Erreur lors de l'ajout de l'instructeur.");
                }
                
			}
		});
		btnNewButton.setBounds(191, 204, 85, 21);
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
}
