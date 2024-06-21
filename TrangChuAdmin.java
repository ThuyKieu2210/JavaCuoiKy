package Controler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import View.TrangChuView;


public class TrangChuAdmin implements ActionListener {

//	private TrangChuView qlView;
	private String ac;

	public TrangChuAdmin(TrangChuView qlView) {
		super();
//		this.qlView = qlView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ac = e.getActionCommand();
		
		if (ac.equals("QUẢN LÝ SẢN PHẨM") ) {
//			qlView.xemThongTin();
		}
		else if (ac.equals("QUẢN LÝ KHO")) {
//			qlView.NhapKho();
		}
		else if (ac.equals("BÁN HÀNG")) {
//			qlView.BanSach();
		}	
		else if (ac.equals("THỐNG KÊ")) {
//			qlView.ThongKe();
		}	
		
		
	}

}
