package org.parabot.dragonblader141.dbsmith;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DBSmithUI extends JFrame {

    private JButton btnOk;
    private JButton btnCancel;
    private JComboBox<DBSmithConstants.ItemType> itemSelection;
    private JComboBox<DBSmithConstants.OreType> oreTypeSelection;
    private JCheckBox useAutoProgression;
    private DBSmithInstance state;

    public DBSmithUI(DBSmithInstance state) {
        this.state = state;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        btnOk = new JButton("Ok");
        btnCancel = new JButton("Cancel");
        itemSelection = new JComboBox<>(DBSmithConstants.ItemType.values());
        oreTypeSelection = new JComboBox<>(DBSmithConstants.OreType.values());
        useAutoProgression = new JCheckBox("Auto Progression");
        setTitle("DBSmith 1.0");

        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOkPressed(e);
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancelPressed(e);
            }
        });


        add(oreTypeSelection);
        add(itemSelection);
        add(useAutoProgression);
        add(btnOk);
        add(btnCancel);

        setLayout(new FlowLayout());

        setSize(200, 150);
        setLocationRelativeTo(null);
    }

    private void onOkPressed(ActionEvent e) {
        this.state.setAutoProgression(useAutoProgression.isSelected());
        DBSmithConstants.OreType oreType = (DBSmithConstants.OreType) this.oreTypeSelection.getSelectedItem();
        this.state.setOreType(oreType);
        DBSmithConstants.ItemType itemType = (DBSmithConstants.ItemType) this.itemSelection.getSelectedItem();
        this.state.setItemType(itemType);

        this.state.setOptionsSet(true);
        this.setVisible(false);
    }

    private void onCancelPressed(ActionEvent e) {
        this.state.setOptionsSet(false);

        this.setVisible(false);
    }

    public static void main(String[] args) {
        final DBSmithInstance state = new DBSmithInstance();
        try {
            EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    new DBSmithUI(state).setVisible(true);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
