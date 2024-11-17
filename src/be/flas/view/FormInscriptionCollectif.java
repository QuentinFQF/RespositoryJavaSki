package be.flas.view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Color;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import com.toedter.calendar.JDateChooser;

import be.flas.connection.DatabaseConnection;
import be.flas.dao.DAOInstructor;
import be.flas.dao.DAOLessonType;
import be.flas.model.LessonType;

import javax.swing.JComboBox;
import com.toedter.calendar.JMonthChooser;
import com.toedter.components.JSpinField;
import com.toedter.components.JLocaleChooser;
import com.toedter.calendar.JDayChooser;

public class FormInscriptionCollectif extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private DAOLessonType daoLessonType;
	private Connection sharedConnection;
	private JComboBox<String> comboLessonType;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FormInscriptionCollectif frame = new FormInscriptionCollectif();
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
	public FormInscriptionCollectif() {
		sharedConnection = DatabaseConnection.getInstance().getConnection();
		daoLessonType = new DAOLessonType(sharedConnection);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 753, 545);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 128, 128));
		panel.setBounds(143, 33, 466, 443);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JButton btnNewButton = new JButton("Retour");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//AddInscription();
                try {
                    
                    FormStart frame = new FormStart();
                    frame.setVisible(true);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
			}
		});
		btnNewButton.setBounds(10, 412, 85, 21);
		panel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Inscrire");
		btnNewButton_1.setBounds(132, 412, 85, 21);
		panel.add(btnNewButton_1);
		
		JDateChooser dateChooser = new JDateChooser();
		dateChooser.setBounds(147, 164, 70, 19);
		panel.add(dateChooser);
		
		comboLessonType = new JComboBox();
		comboLessonType.setBounds(32, 115, 410, 21);
		panel.add(comboLessonType);
		
		JLabel lblNewLabel_1 = new JLabel("Level");
		lblNewLabel_1.setBounds(48, 92, 45, 13);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Catégorie d'age");
		lblNewLabel_2.setBounds(132, 92, 45, 13);
		panel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Sport");
		lblNewLabel_3.setBounds(222, 92, 45, 13);
		panel.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("Prix");
		lblNewLabel_4.setBounds(340, 92, 45, 13);
		panel.add(lblNewLabel_4);
		
		JSpinField spinField = new JSpinField();
		spinField.setBounds(133, 292, 30, 19);
		panel.add(spinField);
		
		
		
		
		
		JLabel lblNewLabel = new JLabel("Inscription");
		lblNewLabel.setBounds(353, 10, 47, 13);
		contentPane.add(lblNewLabel);
		//fillAccreditationComboBox();
		
		
	}
	/*private void fillAccreditationComboBox() {
        System.out.println("Début du remplissage du JComboBox avec les lessontype.");
        try {
            List<LessonType> accreditationNames = daoLessonType.selectLessonType();
            DefaultComboBoxModel<LessonType> model = new DefaultComboBoxModel<>(accreditationNames.toArray();
            comboLessonType.setModel(model);
            System.out.println("Accréditations chargées : " + accreditationNames);
        } catch (Exception e) {
            System.err.println("Erreur lors du remplissage du JComboBox : " + e.getMessage());
        }
    }*/
}
