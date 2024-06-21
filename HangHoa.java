package Model;

import java.io.Serializable;

public class HangHoa implements Serializable{
	private static final long serialVersionUID = 1L;
	private String maSach;
	private String tenSach;
	private String TacGia;
	private String theLoai;
	private double GiaSach;
	private int soLuongTonKho;
	
	private int soLuongBan;
	public HangHoa() {
		
	}
	
	public String getMaSach() {
		return maSach;
	}
	public void setMaSach(String maSach) {
		this.maSach = maSach;
	}
	public String getTenSach() {
		return tenSach;
	}
	public void setTenSach(String tenSach) {
		this.tenSach = tenSach;
	}
	public String getTacGia() {
		return TacGia;
	}
	public void setTacGia(String tacGia) {
		TacGia = tacGia;
	}
	public String getTheLoai() {
		return theLoai;
	}
	public void setTheLoai(String theLoai) {
		this.theLoai = theLoai;
	}
	public double getGiaSach() {
		return GiaSach;
	}
	public void setGiaSach(double giaSach) {
		GiaSach = giaSach;
	}
	public int getSoLuongTonKho() {
		return soLuongTonKho;
	}
	public void setSoLuongTonKho(int soLuongTonKho) {
		this.soLuongTonKho = soLuongTonKho;
	}
	public int getSoLuongBan() {
        return soLuongBan;
    }

    public void setSoLuongBan(int soLuongBan) {
        this.soLuongBan = soLuongBan;
    }
	public HangHoa(String maSach, String tenSach, String tacGia, String theLoai, double giaSach,
			int soLuongTonKho) {
		super();
		
		this.maSach = maSach;
		this.tenSach = tenSach;
		this.TacGia = tacGia;
		this.theLoai = theLoai;
		this.GiaSach = giaSach;
		this.soLuongTonKho = soLuongTonKho;
	}
	


	
	
}
