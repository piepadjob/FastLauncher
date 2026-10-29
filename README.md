# FastLauncher

Launcher Android tối giản, nhẹ (~20-30MB RAM), thiết kế cho Redmi 8 và các thiết bị Android tầm trung.

## Tính năng
- Màn hình home hiển thị tất cả app dạng grid
- App drawer với thanh search tìm kiếm nhanh
- Hiển thị wallpaper hệ thống
- Không có bloat, không chạy service ngầm
- Animation mượt khi mở/đóng drawer

## Cách build APK qua GitHub Actions

### Bước 1: Tạo repo trên GitHub
1. Truy cập https://github.com/new
2. Đặt tên repo: `FastLauncher`
3. Chọn **Public** hoặc **Private**
4. Nhấn **Create repository**

### Bước 2: Đẩy code lên GitHub

Mở terminal tại thư mục `FastLauncher` trên máy tính:

```bash
cd "C:\Users\ASUS ROG\Desktop\FastLauncher"
git init
git add .
git commit -m "Initial commit: FastLauncher"
git branch -M main
git remote add origin https://github.com/TEN_CUA_BAN/FastLauncher.git
git push -u origin main
```

> Thay `TEN_CUA_BAN` bằng username GitHub của bạn.

### Bước 3: Chờ GitHub Actions build

1. Vào repo trên GitHub → tab **Actions**
2. Chờ workflow **Build FastLauncher APK** chạy xong (~3-5 phút)
3. Nhấn vào run → kéo xuống phần **Artifacts**
4. Download `FastLauncher-debug.apk`

### Bước 4: Cài APK qua ADB

```bash
"C:\Users\ASUS ROG\Desktop\platform-tools\adb.exe" install -r FastLauncher-debug.apk
```

### Bước 5: Đặt làm launcher mặc định

Sau khi cài, nhấn nút Home trên điện thoại → chọn **FastLauncher** → **Always**.

---

## Cấu trúc project

```
FastLauncher/
├── app/
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/fastlauncher/
│       │   ├── MainActivity.java       # Màn hình home
│       │   ├── AppDrawerActivity.java  # Drawer tất cả app
│       │   ├── AppAdapter.java         # RecyclerView adapter
│       │   └── AppInfo.java            # Model data
│       └── res/
│           ├── layout/                 # XML layouts
│           ├── values/                 # Colors, strings, themes
│           ├── drawable/               # Backgrounds
│           └── anim/                   # Animations
├── .github/workflows/build-apk.yml    # GitHub Actions
└── build.gradle
```

## Khôi phục MIUI Launcher

Nếu muốn quay lại MIUI Launcher:

```bash
"C:\Users\ASUS ROG\Desktop\platform-tools\adb.exe" shell pm enable --user 0 com.miui.home
```
