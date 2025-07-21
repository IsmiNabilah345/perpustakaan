package perpustakaan;

public class relasibuku {
    private int id;
    private String nama_buku;

    public relasibuku(int id, String nama_buku) {
        this.id = id;
        this.nama_buku = nama_buku;
    }

    public int getId() {
        return id;
    }

    public String getNama_buku() {
        return nama_buku;
    }

    @Override
    public String toString() {
        return nama_buku;
    }
}
