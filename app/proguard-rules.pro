# Aturan ProGuard khusus project ini
# Room Database - menjaga entity dan DAO agar tidak dihapus saat minify
-keep class com.pesantrenqu.maos.data.** { *; }
