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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		TxNom = new JTextField();
		TxNom.setBounds(193, 39, 96, 19);
		contentPane.add(TxNom);
		TxNom.setColumns(10);
		
		txPrenom = new JTextField();
		txPrenom.setBounds(193, 68, 96, 19);
		contentPane.add(txPrenom);
		txPrenom.setColumns(10);
		
		txPseudo = new JTextField();
		txPseudo.setBounds(193, 97, 96, 19);
		contentPane.add(txPseudo);
		txPseudo.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Nom");
		lblNewLabel.setBounds(165, 42, 45, 13);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Prénom");
		lblNewLabel_1.setBounds(146, 68, 45, 13);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Pseudo");
		lblNewLabel_2.setBounds(146, 100, 45, 13);
		contentPane.add(lblNewLabel_2);
		
		JButton btnNewButton = new JButton("Ajouter");
		btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Récupérer les valeurs des champs de texte
                String nom = TxNom.getText();
                String prenom = txPrenom.getText();
                String pseudo = txPseudo.getText();

                // Afficher les valeurs dans la console (ou les utiliser autrement)
                /*System.out.println("Nom: " + nom);
                System.out.println("Prénom: " + prenom);
                System.out.println("Pseudo: " + pseudo);*/

                Instructor i=new Instructor(nom,prenom,pseudo);
                System.out.print(i.toString());  
                // Ici, vous pouvez ajouter le code pour enregistrer ces données ou les traiter comme vous le souhaitez
                boolean success = daoInstructor.create(i);
                
                if (success) {
                    // Afficher un message de succès
                    System.out.println("Instructeur ajouté avec succès !");
                } else {
                    // Afficher un message d'erreur
                    System.out.println("Erreur lors de l'ajout de l'instructeur.");
                }
                FormBooking instructorFrame = new FormBooking();
                instructorFrame.setVisible(true);
            }
        });
		btnNewButton.setBounds(204, 154, 85, 21);
		contentPane.add(btnNewButton);
		
		
		
	}
}
