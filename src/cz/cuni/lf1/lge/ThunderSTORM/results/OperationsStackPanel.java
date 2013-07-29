package cz.cuni.lf1.lge.ThunderSTORM.results;

import com.sun.org.apache.bcel.internal.generic.LASTORE;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OperationsStackPanel extends JPanel {

  private List<LabelWithCheckbox> stack;

  public OperationsStackPanel() {
    stack = new ArrayList<LabelWithCheckbox>();
    this.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
  }

  public void addOperation(Operation op) {
    //if (stack.get(WIDTH)){ TODO: remove last if unchecked
    
    //add arrow
    JLabel arrow = null;
    if (!stack.isEmpty()) {
      arrow = new JLabel("\u2192");
      add(arrow);
    }

    LabelWithCheckbox opLabel = new LabelWithCheckbox(op, arrow);
    add(opLabel);
    stack.add(opLabel);
    disableNextToLastCheckbox();
    revalidate();
  }

  public Operation getLastOperation() {
    return stack.isEmpty() ? null : stack.get(stack.size() - 1).getOperation();
  }

  public void removeAllOperations() {
    stack.clear();
    removeAll();
    repaint();
  }

  public Operation removeLastOperation() {
    int opCount = stack.size();
    if (opCount > 0) {
      LabelWithCheckbox last = stack.remove(opCount - 1);
      if (last.arrow != null) {
        remove(last.arrow);   //remove preceding arrow
      }
      remove(last);
      revalidate();
      return last.getOperation();
    }
    return null;
  }

  public void disableNextToLastCheckbox() {
    int opCount = stack.size();
    if (opCount > 1) {
      LabelWithCheckbox nextToLast = stack.get(opCount - 2);
      nextToLast.removeCheckbox();
    }
  }

  private class LabelWithCheckbox extends JPanel {

    JCheckBox chb;
    JLabel lab;
    JLabel arrow;
    Operation op;

    public LabelWithCheckbox(final Operation op, JLabel arrow) {
      this.op = op;
      setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
      setBorder(javax.swing.BorderFactory.createEtchedBorder());
      //label
      lab = new JLabel(op.getName());
      lab.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      lab.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          op.clicked();
        }
      });
      add(lab);
      //checkbox
      if (op.isUndoAble()) {
        chb = new JCheckBox();
        chb.setBorder(null);
        chb.setSelected(true);
        chb.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            if (chb.isSelected()) {
              op.redo();
            } else {
              op.undo();
            }
          }
        });
        add(chb);
      }
    }

    public void removeCheckbox() {
      if (chb != null) {
        remove(chb);
        chb = null;
        revalidate();
      }
    }

    public boolean isChecked() {
      return chb == null ? false : chb.isSelected();
    }

    public Operation getOperation() {
      return op;
    }
  }

  public static abstract class Operation {

    protected abstract String getName();

    protected boolean isUndoAble() {
      return false;
    }

    protected void clicked() {
    }

    protected void undo() {
    }

    protected void redo() {
    }
  }
}
