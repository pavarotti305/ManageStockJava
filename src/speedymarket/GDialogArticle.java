/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package speedymarket;

import com.j256.ormlite.stmt.QueryBuilder;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author 10067
 */
public class GDialogArticle extends javax.swing.JDialog {

    private DefaultComboBoxModel tvaComboModel;
    private DefaultComboBoxModel marqueComboModel;
    private DefaultComboBoxModel categorieComboModel;
    private DefaultComboBoxModel sousCategorieComboModel;
    /**
     * Determine si on est en modification ou pas
     */
    private boolean modification;
    /**
     * article recuperé dans la base de données si modification et vierge si ajout
     */
    private Article article;

    /**
     * A return status code - returned if Cancel button has been pressed
     */
    public static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    public static final int RET_OK = 1;

    /**
     * Creates new form DialogArticle
     */
    public GDialogArticle(java.awt.Frame parent, boolean modal, Integer id) throws SQLException {
        super(parent, modal);
        //Si un id est notifié on est en modification sinon ajout
        modification = (id != null);
        initComponents();

        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });
        //icone de la fenetre
        this.setIconImage(new ImageIcon(getClass().getResource("img/iconL.png")).getImage());
        
        //récuperation des modèles des comboBox
        tvaComboModel = (DefaultComboBoxModel) jComboBoxTVA.getModel();
        marqueComboModel = (DefaultComboBoxModel) jComboBoxMarque.getModel();
        categorieComboModel = (DefaultComboBoxModel) jComboBoxCategorie.getModel();
        sousCategorieComboModel = (DefaultComboBoxModel) jComboBoxSousCategorie.getModel();
        majCombo();
        
        if (modification) {
            //on recupere l'article avec l'id
            article = SingletonData.getSingletonData().getArticleDAO().queryForId(id);
            jTextFieldLibelle.setText(article.getLibelle());
            //on boucle sur le comboBox jusqu'a trouver la marque de l'article
            for (int i = 0; i < marqueComboModel.getSize(); i++) {
                if (((Marque) marqueComboModel.getElementAt(i)).getCode() == article.getMarque().getCode()) {
                    jComboBoxMarque.setSelectedIndex(i);
                    break;
                }
            }
            jTextPaneDescriptionCourte.setText(article.getDescriptionCourte());
            jTextPaneDescriptionLongue.setText(article.getDescriptionLongue());
            jFormattedTextFieldQuantite.setText(article.getQuantite().toString());
            //on remplace le point par la virgule
            jFormattedTextFieldPrixHT.setText(article.getPrixHT().toString().replace(".", ","));
            //on boucle sur le comboBox jusqu'a trouver la TVA correspondante
            for (int i = 0; i < tvaComboModel.getSize(); i++) {
                if (((TVA) tvaComboModel.getElementAt(i)).getCode() == article.getTVA().getCode()) {
                    jComboBoxTVA.setSelectedIndex(i);
                    break;
                }
            }
            jCheckBoxVisible.setSelected(article.isVisibilite());
            //on recupere la categorie de l 'article ORM ne fait pas les jointures
            article.setCategorie(SingletonData.getSingletonData().getCategorieDAO().queryForId(article.getCategorie().getCode()));
            //si la categorie de l'article n'a pas de categorie parente c'est une categorie principale 
            //donc on met a jours la selection de la comboBox categorie
            if (article.getCategorie().getCategorieParente() == null) {
                for (int i = 0; i < categorieComboModel.getSize(); i++) {
                    if (((Categorie) categorieComboModel.getElementAt(i)).getCode() == article.getCategorie().getCode()) {
                        jComboBoxCategorie.setSelectedIndex(i);
                        break;
                    }
                }
                //sinon on met à jours les deux comboBox
            } else {
                for (int i = 0; i < categorieComboModel.getSize(); i++) {
                    if (((Categorie) categorieComboModel.getElementAt(i)).getCode() == article.getCategorie().getCategorieParente().getCode()) {
                        jComboBoxCategorie.setSelectedIndex(i);
                        break;
                    }
                }
                for (int i = 0; i < sousCategorieComboModel.getSize(); i++) {
                    if (((Categorie) sousCategorieComboModel.getElementAt(i)).getCode() == article.getCategorie().getCode()) {
                        jComboBoxSousCategorie.setSelectedIndex(i);
                        break;
                    }
                }
            }
            //si on est pas en modification on crée un nouvelle article
        } else {
            article = new Article();
            jComboBoxCategorie.setSelectedIndex(0);
            jComboBoxMarque.setSelectedIndex(0);
            jComboBoxTVA.setSelectedIndex(0);
        }

    }
/**
 * Mise à jour des comboBox à partir de la base de données
 */
    private void majCombo() {
        
        tvaComboModel.removeAllElements();
        marqueComboModel.removeAllElements();
        categorieComboModel.removeAllElements();

        try {
            for (TVA tva : SingletonData.getSingletonData().getTvaDAO()) {
                tvaComboModel.addElement(tva);
            }

            for (Marque marque : SingletonData.getSingletonData().getMarqueDAO()) {
                marqueComboModel.addElement(marque);
            }

            List<Categorie> listCategorie = SingletonData.getSingletonData().getCategorieDAO().queryBuilder().where()
                    .isNull(Categorie.CATEGORIEPARENTE_FIELD_NAME).query();

            for (Categorie categorie : listCategorie) {
                categorieComboModel.addElement(categorie);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GDialogArticle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the return status of this dialog - one of RET_OK or RET_CANCEL
     */
    public int getReturnStatus() {
        return returnStatus;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabelLibelle = new javax.swing.JLabel();
        jTextFieldLibelle = new javax.swing.JTextField();
        jLabelDescCourte = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPaneDescriptionCourte = new javax.swing.JTextPane();
        jLabelDescLongue = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPaneDescriptionLongue = new javax.swing.JTextPane();
        jPanel2 = new javax.swing.JPanel();
        jFormattedTextFieldQuantite = new javax.swing.JFormattedTextField();
        jLabelQuantite = new javax.swing.JLabel();
        jFormattedTextFieldPrixHT = new javax.swing.JFormattedTextField();
        jLabelPrixHT = new javax.swing.JLabel();
        jLabelTVA = new javax.swing.JLabel();
        jComboBoxTVA = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jCheckBoxVisible = new javax.swing.JCheckBox();
        jLabelCategorie = new javax.swing.JLabel();
        jLabelVisible = new javax.swing.JLabel();
        jLabelMarque = new javax.swing.JLabel();
        jComboBoxMarque = new javax.swing.JComboBox();
        jComboBoxCategorie = new javax.swing.JComboBox();
        jComboBoxSousCategorie = new javax.swing.JComboBox();
        jLabelSousCategorie = new javax.swing.JLabel();
        jLabelImage = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        okButton.setText("Enregistrer");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Fermer");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jLabelLibelle.setText("Libellé :");

        jLabelDescCourte.setText("Description courte :");

        jScrollPane1.setViewportView(jTextPaneDescriptionCourte);

        jLabelDescLongue.setText("Description longue :");

        jScrollPane2.setViewportView(jTextPaneDescriptionLongue);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabelDescLongue)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 707, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelDescCourte)
                            .addComponent(jLabelLibelle))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldLibelle)
                            .addComponent(jScrollPane1))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelLibelle)
                    .addComponent(jTextFieldLibelle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelDescCourte)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabelDescLongue)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jFormattedTextFieldQuantite.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        jLabelQuantite.setText("Quantité :");

        jFormattedTextFieldPrixHT.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

        jLabelPrixHT.setText("Prix HT :");

        jLabelTVA.setText("TVA :");

        jComboBoxTVA.setModel(new DefaultComboBoxModel());

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTVA)
                    .addComponent(jLabelQuantite)
                    .addComponent(jLabelPrixHT))
                .addGap(56, 56, 56)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jFormattedTextFieldQuantite)
                    .addComponent(jComboBoxTVA, 0, 367, Short.MAX_VALUE)
                    .addComponent(jFormattedTextFieldPrixHT))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelPrixHT)
                    .addComponent(jFormattedTextFieldPrixHT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTVA)
                    .addComponent(jComboBoxTVA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelQuantite)
                    .addComponent(jFormattedTextFieldQuantite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jLabelCategorie.setText("Categorie :");

        jLabelVisible.setText("Visible :");

        jLabelMarque.setText("Marque :");

        jComboBoxMarque.setModel(new DefaultComboBoxModel());

        jComboBoxCategorie.setModel(new DefaultComboBoxModel());
        jComboBoxCategorie.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxCategorieItemStateChanged(evt);
            }
        });
        jComboBoxCategorie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxCategorieActionPerformed(evt);
            }
        });

        jComboBoxSousCategorie.setModel(new DefaultComboBoxModel());

        jLabelSousCategorie.setText("Sous-categorie :");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabelVisible))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelCategorie))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelMarque)))
                .addGap(4, 4, 4)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jCheckBoxVisible)
                        .addGap(7, 213, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxCategorie, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBoxMarque, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelSousCategorie)
                .addGap(18, 18, 18)
                .addComponent(jComboBoxSousCategorie, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelVisible)
                    .addComponent(jCheckBoxVisible))
                .addGap(34, 34, 34)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCategorie)
                    .addComponent(jComboBoxCategorie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelSousCategorie)
                    .addComponent(jComboBoxSousCategorie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelMarque)
                    .addComponent(jComboBoxMarque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46))
        );

        jLabelImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/speedymarket/img/logoSM.png"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Back Office Speedy Market");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cancelButton))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabelImage)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabelImage))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton))
                .addContainerGap())
        );

        getRootPane().setDefaultButton(okButton);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        if (jTextFieldLibelle.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le libellé est obligatoire");
            return;
        }
        if (jFormattedTextFieldPrixHT.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le prix est obligatoire");
            return;
        }
        if (jFormattedTextFieldQuantite.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La quantité est obligatoire");
            return;
        }
            try {
            //on ajoute dans l'article les données saisie
            article.setLibelle(jTextFieldLibelle.getText());
            article.setDescriptionCourte(jTextPaneDescriptionCourte.getText());
            article.setDescriptionLongue(jTextPaneDescriptionLongue.getText());
            article.setMarque((Marque) marqueComboModel.getSelectedItem());
            article.setPrixHT(Double.parseDouble(jFormattedTextFieldPrixHT.getText().replace(",", ".")));
            article.setQuantite(Integer.parseInt(jFormattedTextFieldQuantite.getText()));
            article.setVisibilite(jCheckBoxVisible.isSelected());
            if (jComboBoxSousCategorie.getSelectedIndex() == -1) {
                article.setCategorie((Categorie) categorieComboModel.getSelectedItem());
            } else {
                article.setCategorie((Categorie) sousCategorieComboModel.getSelectedItem());
            }
            article.setTVA((TVA) tvaComboModel.getSelectedItem());
               
                if (modification){
                     SingletonData.getSingletonData().getArticleDAO().update(article);
                JOptionPane.showMessageDialog(this, "L'article a bien été modifié !");
                }else{
                SingletonData.getSingletonData().getArticleDAO().create(article);
                JOptionPane.showMessageDialog(this, "L'article a bien été inseré !");
                //on vide les champs pour un ajout
                jTextFieldLibelle.setText("");
                jTextPaneDescriptionCourte.setText("");
                jTextPaneDescriptionLongue.setText("");
                jFormattedTextFieldPrixHT.setText("");
                jFormattedTextFieldQuantite.setText("");
                jCheckBoxVisible.setSelected(false);
                jComboBoxMarque.setSelectedIndex(0);
                jComboBoxCategorie.setSelectedIndex(0);
                jComboBoxTVA.setSelectedIndex(0);
                }
            } catch (SQLException ex) {
                Logger.getLogger(GDialogArticle.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void jComboBoxCategorieItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxCategorieItemStateChanged

        Categorie parente = (Categorie) categorieComboModel.getSelectedItem();
        //Precaution ceci ne devrait jamais arriver MAIS si aucune categorie n'est selectionnée on ne fait rien
        if (parente == null) {            
            return;
        }
        
        List<Categorie> listCategorie;
        try {
            //preparation de la requete
            QueryBuilder qb = SingletonData.getSingletonData().getCategorieDAO().queryBuilder();
            qb.where().eq(Categorie.CATEGORIEPARENTE_FIELD_NAME, parente.getCode());
            qb.orderBy(Categorie.LIBELLE_CATEGORIE_FIELD_NAME, true);
            //execution de la requete
            listCategorie = qb.query();
            // on met a jour la comboBox sous categorie
            sousCategorieComboModel.removeAllElements();
            for (Categorie categorie : listCategorie) {
                sousCategorieComboModel.addElement(categorie);

            }
        } catch (SQLException ex) {
            Logger.getLogger(GDialogArticle.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jComboBoxCategorieItemStateChanged

    private void jComboBoxCategorieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxCategorieActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxCategorieActionPerformed

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GDialogArticle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GDialogArticle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GDialogArticle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GDialogArticle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                GDialogArticle dialog;
                try {
                    dialog = new GDialogArticle(new javax.swing.JFrame(), true, null);
                    dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent e) {
                            System.exit(0);
                        }
                    });
                    dialog.setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(GDialogArticle.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JCheckBox jCheckBoxVisible;
    private javax.swing.JComboBox jComboBoxCategorie;
    private javax.swing.JComboBox jComboBoxMarque;
    private javax.swing.JComboBox jComboBoxSousCategorie;
    private javax.swing.JComboBox jComboBoxTVA;
    private javax.swing.JFormattedTextField jFormattedTextFieldPrixHT;
    private javax.swing.JFormattedTextField jFormattedTextFieldQuantite;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelCategorie;
    private javax.swing.JLabel jLabelDescCourte;
    private javax.swing.JLabel jLabelDescLongue;
    private javax.swing.JLabel jLabelImage;
    private javax.swing.JLabel jLabelLibelle;
    private javax.swing.JLabel jLabelMarque;
    private javax.swing.JLabel jLabelPrixHT;
    private javax.swing.JLabel jLabelQuantite;
    private javax.swing.JLabel jLabelSousCategorie;
    private javax.swing.JLabel jLabelTVA;
    private javax.swing.JLabel jLabelVisible;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextFieldLibelle;
    private javax.swing.JTextPane jTextPaneDescriptionCourte;
    private javax.swing.JTextPane jTextPaneDescriptionLongue;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables

    private int returnStatus = RET_CANCEL;
}
