package perpustakaan;

public class relasimember {
	private int id;
	private String nama;
	private String kode;

	public relasimember(int id, String nama, String kode) {
		this.id = id;
		this.nama = nama;
		this.kode = kode;
	}

	public int getId() {
		return id;
	}

	public String getNama() {
		return nama;
	}

	public String getKode() {
		return kode;
	}

	@Override
	public String toString() {
		return nama + " - " + kode;
	}
}
