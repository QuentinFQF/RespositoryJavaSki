package be.flas.view;

import java.awt.EventQueue;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class FormStart extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FormStart frame = new FormStart();
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
	public FormStart() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 752, 544);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 128, 128));
		panel.setBounds(222, 111, 244, 323);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JButton btnNewButton = new JButton("Formulaire skier");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                try {
                    
                    FormSkier frame = new FormSkier();
                    frame.setVisible(true);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
			}
		});
		btnNewButton.setBounds(49, 30, 150, 21);
		panel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Formulaire moniteur");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                try {
                    
                    FormInstructor frame = new FormInstructor();
                    frame.setVisible(true);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
			}
		});
		btnNewButton_1.setBounds(49, 61, 150, 21);
		panel.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Formulaire colectif");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                try {
                    
                    FormChooseInstructor frame = new FormChooseInstructor();
                    frame.setVisible(true);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
			}
		});
		btnNewButton_2.setBounds(49, 92, 150, 21);
		panel.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("update personne");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                try {
                    
                    FormUpdatePerson frame = new FormUpdatePerson();
                    frame.setVisible(true);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
			}
		});
		btnNewButton_3.setBounds(49, 123, 150, 21);
		panel.add(btnNewButton_3);
		
		JButton btnNewButton_5 = new JButton("delete skier");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                try {
                    
                    FormDeleteSkier frame = new FormDeleteSkier();
                    frame.setVisible(true);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
			}
		});
		btnNewButton_5.setBounds(49, 185, 150, 21);
		panel.add(btnNewButton_5);
		
		JButton btnNewButton_6 = new JButton("delete inscructor");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                try {
                    
                    FormDeleteInstructor frame = new FormDeleteInstructor();
                    frame.setVisible(true);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
			}
		});
		btnNewButton_6.setBounds(49, 216, 150, 21);
		panel.add(btnNewButton_6);
		
		JButton btnNewButton_7 = new JButton("Formulaire particulier");
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                try {
                    
                    FormInscriptionParticulier frame = new FormInscriptionParticulier();
                    frame.setVisible(true);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
			}
		});
		btnNewButton_7.setBounds(49, 247, 150, 21);
		panel.add(btnNewButton_7);
		
		JButton btnNewButton_8 = new JButton("ajouter accreditation");
		btnNewButton_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                try {
                    
                    FromAddAccInstructor frame = new FromAddAccInstructor ();
                    frame.setVisible(true);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
			}
		});
		btnNewButton_8.setBounds(47, 275, 152, 21);
		panel.add(btnNewButton_8);
		
		JLabel lblNewLabel = new JLabel("Accueil");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
		lblNewLabel.setBounds(292, 46, 106, 27);
		contentPane.add(lblNewLabel);
		
		
		
		
	}
}
