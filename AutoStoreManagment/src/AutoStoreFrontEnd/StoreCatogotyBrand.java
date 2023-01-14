/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AutoStoreFrontEnd;

import DatabaseConnection.SQLOracleConnection;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Naeem ur Rahman
 */
public class StoreCatogotyBrand extends javax.swing.JFrame {

    /**
     * Creates new form StoreEmployee
     */
    String user;
    private SQLOracleConnection sql = new SQLOracleConnection();
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    private String CatagoryTitle;
    private String BrandTitle;
    private int CatagoryId;
    private int BrandId;

    public StoreCatogotyBrand() {
        initComponents();
        txtUser.setText("Naeem Ur Rahman Sajid");
        tableBrand_update();
        tableCatagory_update();
        resetValues();
    }

    public StoreCatogotyBrand(String user) {
        initComponents();
        this.user = user;
        txtUser.setText(user);
        tableBrand_update();
        tableCatagory_update();
        resetValues();
        txtCatagoryTitle.grabFocus();
    }

    private void tableCatagorySelect() {
        DefaultTableModel d1 = (DefaultTableModel) tableCatagory.getModel();
        int selectIndex = tableCatagory.getSelectedRow();
        txtCatagoryId.setText(d1.getValueAt(selectIndex, 0).toString());
        txtCatagoryTitle.setText(d1.getValueAt(selectIndex, 1).toString());
        txtCatagoryTitle.grabFocus();
    }

    private void tableBrandSelect() {
        DefaultTableModel d1 = (DefaultTableModel) tableBrand.getModel();
        int selectIndex = tableBrand.getSelectedRow();
        txtBrandId.setText(d1.getValueAt(selectIndex, 0).toString());
        txtBrandTitle.setText(d1.getValueAt(selectIndex, 1).toString());
        txtBrandId.grabFocus();
    }

    private void tableCatagory_update() {
        int c;
        try {
            con = sql.databaseConnection();
            pst = con.prepareStatement("select * from StoreCatagory");
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsd = (ResultSetMetaData) rs.getMetaData();
            c = rsd.getColumnCount();
            DefaultTableModel d = (DefaultTableModel) tableCatagory.getModel();
            d.setRowCount(0);
            while (rs.next()) {
                Vector v2 = new Vector();
                for (int i = 1; i <= c; i++) {
                    v2.add(rs.getInt("Id"));
                    v2.add(rs.getString("CTitle"));
                }
                d.addRow(v2);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    private void tableBrand_update() {
        int c;
        try {
            con = sql.databaseConnection();
            pst = con.prepareStatement("select * from StoreBrand");
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsd = (ResultSetMetaData) rs.getMetaData();
            c = rsd.getColumnCount();
            DefaultTableModel d = (DefaultTableModel) tableBrand.getModel();
            d.setRowCount(0);
            while (rs.next()) {
                Vector v2 = new Vector();
                for (int i = 1; i <= c; i++) {
                    v2.add(rs.getInt("Id"));
                    v2.add(rs.getString("BTitle"));
                }
                d.addRow(v2);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    private void selectCatagoryId() {
        CatagoryId = 0;
        int c;
        try {
            con = sql.databaseConnection();
            pst = con.prepareStatement("Select id from storeCatagory");
            rs = pst.executeQuery();
            while (rs.next()) {
                c = rs.getInt("Id");
                if (c > CatagoryId) {
                    CatagoryId = c;
                }
            }
            CatagoryId = CatagoryId + 1;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    private void selectBrandId() {
        BrandId = 0;
        int c;
        try {
            con = sql.databaseConnection();
            pst = con.prepareStatement("Select id from storeBrand");
            rs = pst.executeQuery();
            while (rs.next()) {
                c = rs.getInt("Id");
                if (c > BrandId) {
                    BrandId = c;
                }
            }
            BrandId = BrandId + 1;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    private boolean checkCatagoryValues() {
        if (txtCatagoryTitle.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(StoreCatogotyBrand.this, "Enter Title", null, JOptionPane.ERROR_MESSAGE);
            txtCatagoryTitle.grabFocus();
            return false;
        }
        return true;
    }

    private boolean checkBrandValues() {
        if (txtBrandTitle.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(StoreCatogotyBrand.this, "Enter Title", null, JOptionPane.ERROR_MESSAGE);
            txtBrandTitle.grabFocus();
            return false;
        }
        return true;
    }

    private void resetValues() {
        txtBrandId.setText("");
        txtBrandTitle.setText("");
        txtCatagoryId.setText("");
        txtCatagoryTitle.setText("");
    }

    private void insertCatagory() {
        try {
            con = sql.databaseConnection();
            selectCatagoryId();
            CatagoryTitle = txtCatagoryTitle.getText().toUpperCase();
            pst = con.prepareStatement("Insert into storeCatagory values(?,?)");
            pst.setInt(1, CatagoryId);
            pst.setString(2, CatagoryTitle);
            rs = pst.executeQuery();
            JOptionPane.showMessageDialog(this, "Catagory Added");
            tableCatagory_update();
            resetValues();
            txtCatagoryTitle.grabFocus();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
            JOptionPane.showMessageDialog(this, "Catagory not Added", "Error", JOptionPane.ERROR_MESSAGE);
            txtCatagoryTitle.grabFocus();
        }
    }

    private void insertBrand() {
        try {
            con = sql.databaseConnection();
            selectBrandId();
            BrandTitle = txtBrandTitle.getText().toUpperCase();
            pst = con.prepareStatement("Insert into storeBrand values(?,?)");
            pst.setInt(1, BrandId);
            pst.setString(2, BrandTitle);
            rs = pst.executeQuery();
            JOptionPane.showMessageDialog(this, "Brand Added");
            tableBrand_update();
            resetValues();
            txtBrandTitle.grabFocus();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
            JOptionPane.showMessageDialog(this, "Brand not Added", "Error", JOptionPane.ERROR_MESSAGE);
            txtBrandTitle.grabFocus();
        }
    }

    private void deleteProduct_catagory(int catagoryid) {
        try {
            con = sql.databaseConnection();
            pst = con.prepareStatement("delete from StoreProduct where catid = ?");

            pst.setInt(1, catagoryid);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Products Deleted....!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
            JOptionPane.showMessageDialog(this, "Products not Deleted", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCatagory() {
        DefaultTableModel d1 = (DefaultTableModel) tableCatagory.getModel();
        int selectIndex = tableCatagory.getSelectedRow();

        int Id = Integer.parseInt(d1.getValueAt(selectIndex, 0).toString());

        int ch = JOptionPane.showConfirmDialog(null, "Do you Want to delete Catagory ?", "Warning", JOptionPane.YES_NO_OPTION);
        if (ch == JOptionPane.YES_OPTION) {

            try {
                con = sql.databaseConnection();
                pst = con.prepareStatement("delete from StoreCatagory where Id= ?");
                pst.setInt(1, Id);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(null, "Catagory Deleted....!");
                tableCatagory_update();
                resetValues();
                txtCatagoryTitle.grabFocus();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex);
                JOptionPane.showMessageDialog(this, "Catagory not Deleted", "Error", JOptionPane.ERROR_MESSAGE);
                txtCatagoryTitle.grabFocus();
            }
        }
    }

    private void deleteProduct_brand(int Brandid) {
        try {
            con = sql.databaseConnection();
            pst = con.prepareStatement("delete from StoreProduct where bid = ?");

            pst.setInt(1, Brandid);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Products Deleted....!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
            JOptionPane.showMessageDialog(this, "Products not Deleted", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBrand() {
        DefaultTableModel d1 = (DefaultTableModel) tableBrand.getModel();
        int selectIndex = tableBrand.getSelectedRow();

        int Id = Integer.parseInt(d1.getValueAt(selectIndex, 0).toString());

        int ch = JOptionPane.showConfirmDialog(null, "Do you Want to delete Brand ?", "Warning", JOptionPane.YES_NO_OPTION);
        if (ch == JOptionPane.YES_OPTION) {

            try {
                con = sql.databaseConnection();
                pst = con.prepareStatement("delete from StoreBrand where Id= ?");
                pst.setInt(1, Id);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(null, "Brand Deleted....!");
                tableBrand_update();
                resetValues();
                txtBrandTitle.grabFocus();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex);
                JOptionPane.showMessageDialog(this, "Brand not Deleted", "Error", JOptionPane.ERROR_MESSAGE);
                txtBrandTitle.grabFocus();
            }
        }
    }
        private void editCatagory(){
        try {
            tableCatagorySelect();
            CatagoryId = Integer.parseInt(txtCatagoryId.getText().trim());
            CatagoryTitle = txtCatagoryTitle.getText();
            con = sql.databaseConnection();
            pst = con.prepareStatement("update storeCatagory set Ctitle = '"+CatagoryTitle+"' where id = '"+CatagoryId+"'");
            pst.executeUpdate();
            con.close();
            JOptionPane.showMessageDialog(null, "Catagory Updated....!");
            resetValues();
            tableCatagory_update();
            txtCatagoryTitle.grabFocus();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }
        private void editBrand(){
        try {
            tableBrandSelect();
            BrandId = Integer.parseInt(txtBrandId.getText().trim());
            BrandTitle = txtBrandTitle.getText();
            con = sql.databaseConnection();
            pst = con.prepareStatement("update storebrand set Btitle = ? where id = ?");
            pst.setString(1, BrandTitle);
            pst.setInt(2,BrandId);
            
            JOptionPane.showMessageDialog(null, "Brand Updated....!");
            con.close();
           tableCatagory_update();
            resetValues();
            txtBrandTitle.grabFocus();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtUser = new javax.swing.JLabel();
        panelSwictch = new javax.swing.JPanel();
        labelProducts = new javax.swing.JLabel();
        labelEmployee = new javax.swing.JLabel();
        labelLoginusers = new javax.swing.JLabel();
        labelOrder = new javax.swing.JLabel();
        labelCatagory = new javax.swing.JLabel();
        labelBrand = new javax.swing.JLabel();
        labelSalesRecord = new javax.swing.JLabel();
        PanelCatagory = new javax.swing.JPanel();
        panelTable = new javax.swing.JScrollPane();
        tableCatagory = new javax.swing.JTable();
        PanelBrand = new javax.swing.JPanel();
        panelTable1 = new javax.swing.JScrollPane();
        tableBrand = new javax.swing.JTable();
        txtBrandId = new javax.swing.JTextField();
        txtBrandTitle = new javax.swing.JTextField();
        txtCatagoryId = new javax.swing.JTextField();
        txtCatagoryTitle = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnAddBrand = new javax.swing.JButton();
        btnEditBrand = new javax.swing.JButton();
        btnDeleteBrand = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        btnAddCatagory = new javax.swing.JButton();
        btnEditCatagory = new javax.swing.JButton();
        btnDeleteCatagory = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtUser.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtUser.setForeground(new java.awt.Color(51, 51, 51));
        txtUser.setText("Name");

        panelSwictch.setBackground(new java.awt.Color(0, 102, 102));
        panelSwictch.setForeground(new java.awt.Color(0, 102, 102));

        labelProducts.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        labelProducts.setForeground(new java.awt.Color(255, 255, 255));
        labelProducts.setText("PRODUCTS");
        labelProducts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelProductsMouseClicked(evt);
            }
        });

        labelEmployee.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        labelEmployee.setForeground(new java.awt.Color(255, 255, 255));
        labelEmployee.setText("EMPLOYEE ");
        labelEmployee.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelEmployeeMouseClicked(evt);
            }
        });

        labelLoginusers.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        labelLoginusers.setForeground(new java.awt.Color(255, 255, 255));
        labelLoginusers.setText("LOGIN USER");
        labelLoginusers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelLoginusersMouseClicked(evt);
            }
        });

        labelOrder.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        labelOrder.setForeground(new java.awt.Color(255, 255, 255));
        labelOrder.setText("ORDER");
        labelOrder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelOrderMouseClicked(evt);
            }
        });

        labelCatagory.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        labelCatagory.setForeground(new java.awt.Color(255, 255, 255));
        labelCatagory.setText("CATAGORY");
        labelCatagory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelCatagoryMouseClicked(evt);
            }
        });

        labelBrand.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        labelBrand.setForeground(new java.awt.Color(255, 255, 255));
        labelBrand.setText("BRAND");
        labelBrand.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelBrandMouseClicked(evt);
            }
        });

        labelSalesRecord.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        labelSalesRecord.setForeground(new java.awt.Color(255, 255, 255));
        labelSalesRecord.setText("SALES RECORD");
        labelSalesRecord.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelSalesRecordMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelSwictchLayout = new javax.swing.GroupLayout(panelSwictch);
        panelSwictch.setLayout(panelSwictchLayout);
        panelSwictchLayout.setHorizontalGroup(
            panelSwictchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSwictchLayout.createSequentialGroup()
                .addGroup(panelSwictchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSwictchLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(labelSalesRecord))
                    .addGroup(panelSwictchLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(panelSwictchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelSwictchLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(labelBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(labelEmployee)))
                    .addGroup(panelSwictchLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(panelSwictchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelLoginusers)
                            .addComponent(labelCatagory, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(40, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSwictchLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(labelOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );
        panelSwictchLayout.setVerticalGroup(
            panelSwictchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSwictchLayout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(labelEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(labelLoginusers, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(labelOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelCatagory, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelSalesRecord, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tableCatagory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "TITLE"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tableCatagory.getTableHeader().setReorderingAllowed(false);
        tableCatagory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableCatagoryMouseClicked(evt);
            }
        });
        panelTable.setViewportView(tableCatagory);

        javax.swing.GroupLayout PanelCatagoryLayout = new javax.swing.GroupLayout(PanelCatagory);
        PanelCatagory.setLayout(PanelCatagoryLayout);
        PanelCatagoryLayout.setHorizontalGroup(
            PanelCatagoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCatagoryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelTable, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
                .addContainerGap())
        );
        PanelCatagoryLayout.setVerticalGroup(
            PanelCatagoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCatagoryLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelTable, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tableBrand.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "TITLE"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tableBrand.getTableHeader().setReorderingAllowed(false);
        tableBrand.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableBrandMouseClicked(evt);
            }
        });
        panelTable1.setViewportView(tableBrand);

        javax.swing.GroupLayout PanelBrandLayout = new javax.swing.GroupLayout(PanelBrand);
        PanelBrand.setLayout(PanelBrandLayout);
        PanelBrandLayout.setHorizontalGroup(
            PanelBrandLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBrandLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelTable1, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                .addContainerGap())
        );
        PanelBrandLayout.setVerticalGroup(
            PanelBrandLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBrandLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        txtBrandId.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        txtBrandTitle.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtBrandTitle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBrandTitleKeyPressed(evt);
            }
        });

        txtCatagoryId.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        txtCatagoryTitle.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtCatagoryTitle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCatagoryTitleKeyPressed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("ID:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("TITLE:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("ID:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("TITLE:");

        btnAddBrand.setText("Add");
        btnAddBrand.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddBrandMouseClicked(evt);
            }
        });
        btnAddBrand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddBrandActionPerformed(evt);
            }
        });
        btnAddBrand.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnAddBrandKeyPressed(evt);
            }
        });

        btnEditBrand.setText("Edit");
        btnEditBrand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditBrandActionPerformed(evt);
            }
        });

        btnDeleteBrand.setText("Delete");
        btnDeleteBrand.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDeleteBrandMouseClicked(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Viner Hand ITC", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 102, 102));
        jLabel7.setText("CATAGORY");

        btnAddCatagory.setText("Add");
        btnAddCatagory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddCatagoryMouseClicked(evt);
            }
        });
        btnAddCatagory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnAddCatagoryKeyPressed(evt);
            }
        });

        btnEditCatagory.setText("Edit");
        btnEditCatagory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditCatagoryActionPerformed(evt);
            }
        });

        btnDeleteCatagory.setText("Delete");
        btnDeleteCatagory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDeleteCatagoryMouseClicked(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Viner Hand ITC", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 102, 102));
        jLabel8.setText("BRAND");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(panelSwictch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(68, 68, 68)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnAddCatagory)
                                .addGap(18, 18, 18)
                                .addComponent(btnEditCatagory)
                                .addGap(18, 18, 18)
                                .addComponent(btnDeleteCatagory)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAddBrand)
                                .addGap(18, 18, 18)
                                .addComponent(btnEditBrand)
                                .addGap(18, 18, 18)
                                .addComponent(btnDeleteBrand)
                                .addGap(53, 53, 53))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtCatagoryTitle)
                                    .addComponent(txtCatagoryId, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addGap(63, 63, 63)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtBrandId)
                                    .addComponent(txtBrandTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(36, 36, 36))))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(106, 106, 106))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(PanelCatagory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)
                                .addComponent(PanelBrand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(175, 175, 175)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addGap(151, 151, 151))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelSwictch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7))
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCatagoryId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(txtCatagoryTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBrandId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBrandTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddBrand)
                    .addComponent(btnEditBrand)
                    .addComponent(btnDeleteBrand)
                    .addComponent(btnAddCatagory)
                    .addComponent(btnEditCatagory)
                    .addComponent(btnDeleteCatagory))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelCatagory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PanelBrand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void labelLoginusersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelLoginusersMouseClicked
        // TODO add your handling code here:
        new StoreLoginUsers(user).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_labelLoginusersMouseClicked

    private void labelEmployeeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelEmployeeMouseClicked
        // TODO add your handling code here:
        new StoreEmployee(user).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_labelEmployeeMouseClicked

    private void labelProductsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelProductsMouseClicked
        // TODO add your handling code here:
        new StoreProduct(user).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_labelProductsMouseClicked

    private void labelSalesRecordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelSalesRecordMouseClicked
        // TODO add your handling code here:
        new StoreSalesRecord(user).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_labelSalesRecordMouseClicked

    private void labelOrderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelOrderMouseClicked
        // TODO add your handling code here:
        new StoreOrder(user).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_labelOrderMouseClicked

    private void labelCatagoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelCatagoryMouseClicked
        // TODO add your handling code here:
        new StoreCatogotyBrand(user).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_labelCatagoryMouseClicked

    private void labelBrandMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelBrandMouseClicked
        // TODO add your handling code here:
        new StoreCatogotyBrand(user).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_labelBrandMouseClicked

    private void tableCatagoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCatagoryMouseClicked
        // TODO add your handling code here:
        tableCatagorySelect();
    }//GEN-LAST:event_tableCatagoryMouseClicked

    private void tableBrandMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableBrandMouseClicked
        // TODO add your handling code here:
        tableBrandSelect();
    }//GEN-LAST:event_tableBrandMouseClicked

    private void btnAddCatagoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddCatagoryMouseClicked
        // TODO add your handling code here:
        if (checkCatagoryValues()) {
            insertCatagory();
        }
    }//GEN-LAST:event_btnAddCatagoryMouseClicked

    private void btnAddBrandMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddBrandMouseClicked
        // TODO add your handling code here:
        if (checkBrandValues()) {
            insertBrand();
        }
    }//GEN-LAST:event_btnAddBrandMouseClicked

    private void btnDeleteCatagoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteCatagoryMouseClicked
        // TODO add your handling code here:
        if (txtCatagoryId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(StoreCatogotyBrand.this, "Enter Catagory Id", null, JOptionPane.ERROR_MESSAGE);
            txtCatagoryId.grabFocus();
        }
        else{
            deleteCatagory();
        }
    }//GEN-LAST:event_btnDeleteCatagoryMouseClicked

    private void btnDeleteBrandMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteBrandMouseClicked
        // TODO add your handling code here:
        if (txtBrandId.getText().trim().isEmpty()) {
             JOptionPane.showMessageDialog(StoreCatogotyBrand.this, "Enter Brand Id", null, JOptionPane.ERROR_MESSAGE);
            txtBrandId.grabFocus();
        } else {
          deleteBrand();
        }
    }//GEN-LAST:event_btnDeleteBrandMouseClicked

    private void txtCatagoryTitleKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCatagoryTitleKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (checkCatagoryValues()) {
                btnAddCatagory.grabFocus();
            }
        }
    }//GEN-LAST:event_txtCatagoryTitleKeyPressed

    private void txtBrandTitleKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBrandTitleKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (checkBrandValues()) {
                btnAddBrand.grabFocus();
            }
        }
    }//GEN-LAST:event_txtBrandTitleKeyPressed

    private void btnAddCatagoryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAddCatagoryKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (checkCatagoryValues()) {
                insertCatagory();
            }
        }
    }//GEN-LAST:event_btnAddCatagoryKeyPressed

    private void btnAddBrandKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAddBrandKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (checkBrandValues()) {
                insertBrand();
            }
        }
    }//GEN-LAST:event_btnAddBrandKeyPressed

    private void btnAddBrandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddBrandActionPerformed
        // TODO add your handling code here:
              if (checkBrandValues()) {
            insertBrand();
        }
         
    }//GEN-LAST:event_btnAddBrandActionPerformed

    private void btnEditCatagoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditCatagoryActionPerformed
        // TODO add your handling code here:
        editCatagory();
    }//GEN-LAST:event_btnEditCatagoryActionPerformed

    private void btnEditBrandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditBrandActionPerformed
        // TODO add your handling code here:
        editBrand();
    }//GEN-LAST:event_btnEditBrandActionPerformed

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
            java.util.logging.Logger.getLogger(StoreCatogotyBrand.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StoreCatogotyBrand.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StoreCatogotyBrand.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StoreCatogotyBrand.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StoreCatogotyBrand().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelBrand;
    private javax.swing.JPanel PanelCatagory;
    private javax.swing.JButton btnAddBrand;
    private javax.swing.JButton btnAddCatagory;
    private javax.swing.JButton btnDeleteBrand;
    private javax.swing.JButton btnDeleteCatagory;
    private javax.swing.JButton btnEditBrand;
    private javax.swing.JButton btnEditCatagory;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel labelBrand;
    private javax.swing.JLabel labelCatagory;
    private javax.swing.JLabel labelEmployee;
    private javax.swing.JLabel labelLoginusers;
    private javax.swing.JLabel labelOrder;
    private javax.swing.JLabel labelProducts;
    private javax.swing.JLabel labelSalesRecord;
    private javax.swing.JPanel panelSwictch;
    private javax.swing.JScrollPane panelTable;
    private javax.swing.JScrollPane panelTable1;
    private javax.swing.JTable tableBrand;
    private javax.swing.JTable tableCatagory;
    private javax.swing.JTextField txtBrandId;
    private javax.swing.JTextField txtBrandTitle;
    private javax.swing.JTextField txtCatagoryId;
    private javax.swing.JTextField txtCatagoryTitle;
    private javax.swing.JLabel txtUser;
    // End of variables declaration//GEN-END:variables
}
