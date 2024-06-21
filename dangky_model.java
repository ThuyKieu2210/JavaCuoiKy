package Model;

public class dangky_model {
	private String TenTaiKhoan;
	private String nhapMk;
	private String nhapLaiMK;
	public dangky_model(String tenTaiKhoan, String nhapMk, String nhapLaiMK) {
		super();
		TenTaiKhoan = tenTaiKhoan;
		this.nhapMk = nhapMk;
		this.nhapLaiMK = nhapLaiMK;
	}
	public String getTenTaiKhoan() {
		return TenTaiKhoan;
	}
	public void setTenTaiKhoan(String tenTaiKhoan) {
		TenTaiKhoan = tenTaiKhoan;
	}
	public String getNhapMk() {
		return nhapMk;
	}
	public void setNhapMk(String nhapMk) {
		this.nhapMk = nhapMk;
	}
	public String getNhapLaiMK() {
		return nhapLaiMK;
	}
	public void setNhapLaiMK(String nhapLaiMK) {
		this.nhapLaiMK = nhapLaiMK;
	}
	

}
