package kontroler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import entiteti.Osoblje;
import entiteti.Sobarica;
import funkcionalnosti.SobaricaFunkcija;
import hotel.HotelListePodataka;

public class SobaricaFunkcionalnosti {
    private Osoblje sobarica;
    private DefaultTableModel tableModel;
    private JTable table;

    public SobaricaFunkcionalnosti(Osoblje sobarica, DefaultTableModel tableModel, JTable table) {
        this.setSobarica(sobarica);
        this.tableModel = tableModel;
        this.table = table;
    }

    public ActionListener getRemoveButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                	int id = (int) tableModel.getValueAt(selectedRow, 0);
                    tableModel.removeRow(selectedRow);
                    SobaricaFunkcija sobar = new SobaricaFunkcija();
                    Sobarica sobaricaFunkcija = null;
                    for (Sobarica sobaricaHotela : HotelListePodataka.getInstance().getListaSobarica()) {
						if (sobaricaHotela.getKorisnickoIme() == sobarica.getKorisnickoIme()) {
							sobaricaFunkcija = sobaricaHotela;
							break;
						}
                    }
                    sobar.ociscenaSoba(sobaricaFunkcija, id);
                }
            }
        };
    }

	public Osoblje getSobarica() {
		return sobarica;
	}

	public void setSobarica(Osoblje sobarica) {
		this.sobarica = sobarica;
	}
}