package be.flas.view;

import java.awt.EventQueue;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import be.flas.model.Instructor;
import be.flas.model.Skier;

public class FormDeleteInstructor extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tfDeleteSkier;
	private JTable tableSkier;
	private DefaultTableModel tableModel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FormDeleteInstructor frame = new FormDeleteInstructor();
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
	public FormDeleteInstructor() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 835, 548);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 128, 128));
		panel.setBounds(198, 10, 429, 473);
		contentPane.add(panel);
		panel.setLayout(null);

		JButton btnSearchInstructor = new JButton("Rechercher instructor");
		btnSearchInstructor.setBounds(144, 154, 150, 21);
		panel.add(btnSearchInstructor);

		tfDeleteSkier = new JTextField();
		tfDeleteSkier.setBounds(144, 126, 150, 19);
		panel.add(tfDeleteSkier);
		tfDeleteSkier.setColumns(10);

		// Ajouter une barre de défilement et une table
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 250, 381, 150);
		panel.add(scrollPane);

		// Initialiser le modèle de la table avec les colonnes
		tableModel = new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Nom", "Prénom", "Pseudo", "Date Naissance" });
		tableSkier = new JTable(tableModel);
		scrollPane.setViewportView(tableSkier);
		
		JButton btnDeleteButton = new JButton("supprimer");
		// ActionListener pour le bouton Supprimer
		btnDeleteButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        // Vérifier qu'une ligne est sélectionnée
		        int selectedRow = tableSkier.getSelectedRow();

		        if (selectedRow == -1) { // Aucune ligne n'est sélectionnée
		            JOptionPane.showMessageDialog(contentPane, "Veuillez sélectionner un instructor dans le tableau pour le supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
		            return;
		        }

		        // Récupérer l'ID du skieur sélectionné dans la table
		        int insId = (int) tableModel.getValueAt(selectedRow, 0);

		        // Afficher une confirmation avant la suppression
		        int confirm = JOptionPane.showConfirmDialog(contentPane, "Êtes-vous sûr de vouloir supprimer ce instructor ?", "Confirmation", JOptionPane.YES_NO_OPTION);
		        
		        if (confirm == JOptionPane.YES_OPTION) {
		            // Appel à la méthode de suppression (à implémenter)
		        	Instructor s=new Instructor(insId);
		            boolean isDeleted = s.delete();

		            if (isDeleted) {
		                JOptionPane.showMessageDialog(contentPane, "Le instrcuteur a été supprimé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
		                // Supprimer la ligne du tableau
		                tableModel.removeRow(selectedRow);
		            } else {
		                JOptionPane.showMessageDialog(contentPane, "Erreur lors de la suppression du instrcuteur.", "Erreur", JOptionPane.ERROR_MESSAGE);
		            }
		        }
		    }
		});

		btnDeleteButton.setBounds(334, 442, 85, 21);
		panel.add(btnDeleteButton);

		// ActionListener pour le bouton Rechercher
		btnSearchInstructor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String pseudo = tfDeleteSkier.getText().trim();

				if (pseudo.isEmpty()) {
					JOptionPane.showMessageDialog(contentPane, "Veuillez entrer un pseudo.", "Erreur", JOptionPane.ERROR_MESSAGE);
					return;
				}

				// Recherche du skieur par pseudo
				Instructor instructor = Instructor.getInstructorByPseudo(pseudo);

				// Nettoyer les anciennes données du modèle de table
				tableModel.setRowCount(0);

				if (instructor != null) {
					// Ajouter le skieur trouvé dans le tableau
					tableModel.addRow(new Object[] {
							instructor.getPersonId(),
							instructor.getName(),
							instructor.getFirstName(),
							instructor.getPseudo(),
							instructor.getDateOfBirth()
					});
				} else {
					JOptionPane.showMessageDialog(contentPane, "Aucun instructor trouvé avec ce pseudo.", "Résultat", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
	}
}
