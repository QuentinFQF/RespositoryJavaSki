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
import be.flas.dao.DAOSkier;
import be.flas.dao.DAOAccreditation; // Import de la classe DAOAccreditation
import be.flas.model.Accreditation; // Import du modèle Accreditation
import be.flas.model.Instructor;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import com.toedter.calendar.JDateChooser;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.util.List;

public class FormInstructor extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField TxNom;
    private JTextField txPrenom;
    private JTextField txPseudo;
    private DAOInstructor daoInstructor = new DAOInstructor();
    private DAOAccreditation daoAccreditation = new DAOAccreditation(); // Instance de DAOAccreditation
    private JComboBox<String> comboAccreditation; // JComboBox pour afficher les accréditations
    private DAOSkier daoSkier = new DAOSkier();

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
        
        // Date chooser
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setBounds(123, 119, 70, 21);
        panel.add(dateChooser);
        
        JLabel lblNewLabel_3 = new JLabel("Date de naissance");
        lblNewLabel_3.setBounds(28, 119, 85, 13);
        panel.add(lblNewLabel_3);
        
        // JComboBox pour accréditations
        comboAccreditation = new JComboBox<>();
        comboAccreditation.setBounds(123, 30, 153, 21); // Position et taille du JComboBox
        panel.add(comboAccreditation);

        // Remplissage du JComboBox avec les noms d'accréditation
        fillAccreditationComboBox();

        JButton btnNewButton = new JButton("Créer");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nom = TxNom.getText();
                String prenom = txPrenom.getText();
                String pseudo = txPseudo.getText();
                LocalDate dob = dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                // Récupération de l'accréditation sélectionnée
                String selectedAccreditation = (String) comboAccreditation.getSelectedItem();
                Accreditation a=new Accreditation(selectedAccreditation);
                System.out.println("Accréditation sélectionnée: " + selectedAccreditation);

                Instructor i = new Instructor(nom, prenom, dob,a, pseudo);
               
                System.out.print(i.toString());  
                //boolean success = daoInstructor.create(i);
                System.out.println("Test de connexion à la base de données...");

                boolean success=daoSkier.testConnection();
         
                
                //int idA=daoAccreditation.selectId(selectedAccreditation);
                //System.out.println("id a :"+idA);
                //int test=daoInstructor.insertInstructor(i);
                //int success = daoInstructor.create(i,idA);
                //boolean success2 = daoInstructor.create(i);
                
                //System.out.println(success);
                
                /*if (success>-1) {
                    System.out.println("Instructeur ajouté avec succès !");
                } else {
                    System.out.println("Erreur lors de l'ajout de l'instructeur.");
                }*/
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

    // Méthode pour remplir le JComboBox avec les accréditations
    /*private void fillAccreditationComboBox() {
        List<String> accreditationNames = daoAccreditation.selectNames(); // Récupération des noms d'accréditation
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(accreditationNames.toArray(new String[0]));
        comboAccreditation.setModel(model); // Affectation du modèle au JComboBox
    }*/
    private void fillAccreditationComboBox() {
        System.out.println("Début du remplissage du JComboBox avec les accréditations.");
        try {
            List<String> accreditationNames = daoAccreditation.selectNames();
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(accreditationNames.toArray(new String[0]));
            comboAccreditation.setModel(model);
            System.out.println("Accréditations chargées : " + accreditationNames);
        } catch (Exception e) {
            System.err.println("Erreur lors du remplissage du JComboBox : " + e.getMessage());
        }
    }

}
