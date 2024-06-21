package Model;

import java.io.Serializable;

public class NhapKho implements Serializable  {
    private static final long serialVersionUID = 1L;
	private String maSachNhapKho;
    private String ngayNhap;
    private int soLuongNhap;
    private String tenSachNhap;
    private int updateValue;
	public int getUpdateValue() {
		return updateValue;
	}
	public void setUpdateValue(int updateValue) {
		this.updateValue = updateValue;
	}
	public String getMaNhapKho() {
		return maSachNhapKho;
	}
	public void setMaNhapKho(String maNhapKho) {
		this.maSachNhapKho = maNhapKho;
	}
	public String getNgayNhap() {
		return ngayNhap;
	}
	public void setNgayNhap(String ngayNhap) {
		this.ngayNhap = ngayNhap;
	}
	public int getSoLuongNhap() {
		return soLuongNhap;
	}
	public void setSoLuongNhap(int soLuongNhap) {
		this.soLuongNhap = soLuongNhap;
	}
	public String getTenSachNhap() {
		return tenSachNhap;
	}
	public void setTenSachNhap(String tenSachNhap) {
		this.tenSachNhap = tenSachNhap;
	}
	public NhapKho(String maNhapKho, String ngayNhap, int soLuongNhap, String tenSachNhap) {
		this.maSachNhapKho = maNhapKho;
		this.ngayNhap = ngayNhap;
		this.soLuongNhap = soLuongNhap;
		this.tenSachNhap = tenSachNhap;
	}
	public NhapKho(String maNhapKho, String ngayNhap, int soLuongNhap, String tenSachNhap, int soLuongNhapCu) {
		this.setUpdateValue(soLuongNhap-soLuongNhapCu);
		this.maSachNhapKho = maNhapKho;
		this.ngayNhap = ngayNhap;
		this.soLuongNhap = soLuongNhap;
		this.tenSachNhap = tenSachNhap;
	}
    
}
