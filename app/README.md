<div align="center">

<!-- GANTI DENGAN URL LOGO APLIKASIMU -->
<img src="app/src/main/res/drawable/logo.png" alt="Logo MyKiroku" width="150" height="150">
<h1 align="center">MyKiroku</h1>

<p align="center">
Asisten produktivitas personal untuk menetapkan, melacak, dan mencapai tujuanmu melalui sesi fokus yang terukur.
<br><br><br>
</p>

</div>

---

## 🧭 Tentang Proyek

**MyKiroku** adalah aplikasi mobile berbasis Android yang dirancang untuk membantu pelajar, mahasiswa, dan profesional meningkatkan produktivitas mereka.  
Berbeda dari aplikasi *to-do list* biasa, **MyKiroku** berfokus pada sistem **Goal Tracking** yang komprehensif.

Pengguna dapat:
- Menetapkan tujuan (goals) spesifik,
- Melacak waktu yang dihabiskan untuk setiap tujuan menggunakan stopwatch sesi fokus, dan
- Memvisualisasikan kemajuan mereka melalui dasbor statistik yang informatif.

Aplikasi ini dibangun sepenuhnya secara **native** dengan **Java** dan **XML**, serta menerapkan prinsip **arsitektur modern Android** agar kode tetap bersih, terstruktur, dan mudah dikembangkan.

---

## ✨ Fitur Utama

### 🎯 Manajemen Goal yang Detail
- Buat goal baru dengan **judul, target waktu harian (jam/menit), dan kode warna.**
- Atur **periode aktif** goal (tanggal mulai dan selesai) atau pilih *No Limit* untuk tujuan berkelanjutan.
- Tentukan **hari aktif** untuk setiap goal (misalnya, hanya aktif di hari kerja).

### ⏱️ Sesi Fokus (Stopwatch)
- Mulai sesi fokus untuk goal tertentu langsung dari halaman utama.
- Lacak waktu berjalan dengan stopwatch yang bisa di-*pause* dan di-*resume.*
- Simpan setiap sesi yang selesai agar tercatat dalam statistik.

### 📝 Editor Catatan Terintegrasi
- Tulis catatan selama sesi fokus berlangsung untuk mencatat ide atau rangkuman.
- Dilengkapi fitur format teks dasar seperti **Bold** dan *Italic.*

### 📊 Dasbor Statistik Visual
- Lihat rangkuman produktivitas dengan filter **Harian, Mingguan, dan Bulanan.**
- Tampilkan total waktu fokus, jumlah sesi, dan persentase pencapaian untuk setiap goal.
- Analisis performa dengan **Bar Chart** interaktif (menggunakan MPAndroidChart).

---

## 🛠️ Teknologi yang Digunakan

| Komponen | Teknologi |
|-----------|------------|
| **Bahasa** | Java |
| **UI** | XML dengan Material Design 3 |
| **Arsitektur** | MVVM (Model–View–ViewModel) |
| **Navigasi** | Android Navigation Component (Single-Activity Architecture) |
| **Penyimpanan Data** | Sementara di memori *(akan ditingkatkan ke Room Database)* |

### 🔧 MVVM Detail
- **ViewModel**: Mengelola logika bisnis dan data UI.
- **LiveData**: Membuat UI reaktif terhadap perubahan data.
- **Repository**: Bertindak sebagai *single source of truth* untuk data aplikasi.

### 📦 Dependensi Utama
- `Core Splashscreen` → Splash screen bawaan Android untuk transisi halus.
- `Material Components` → Komponen UI modern dan responsif.
- `MPAndroidChart` → Visualisasi data dalam bentuk grafik.

---

## 🚀 Rencana Pengembangan Kedepannya

- [ ] **Room Database**: Menyimpan data secara lokal dan permanen.
- [ ] **WorkManager untuk Timer**: Menjalankan stopwatch di background.
- [ ] **Sinkronisasi Cloud**: Menyimpan data lintas perangkat.
- [ ] **Fitur Sosial**: Tambahkan leaderboard dan grup belajar untuk motivasi.
- [ ] **Widget Homescreen**: Memulai sesi fokus langsung dari layar utama.

---

## 👥 Kontributor

| Nama | NIM |
|------|-----|
| Rendy Sahrasad | 2210511132 |
| Aliyah Irsya Azzahri | 2210511144 |
| Annisa Dwi Aprilia | 2210511148 |
| Rachva Aryanjaya | 2210511152 |

---