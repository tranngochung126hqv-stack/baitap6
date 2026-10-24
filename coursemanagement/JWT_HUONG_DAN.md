# Chức năng đăng nhập và phân quyền bằng JWT

## 1. Tài khoản demo
- USER: `user` / `user123`
- ADMIN: `admin` / `admin123`

Đây là tài khoản minh họa cho bài tập. Trong dự án triển khai thực tế, cần xác thực với bảng người dùng trong cơ sở dữ liệu và lưu mật khẩu dạng băm.

## 2. Chạy chương trình
1. Mở thư mục `coursemanagement` trong VS Code.
2. Đảm bảo SQL Server và database của dự án đã được cấu hình đúng trong `src/main/resources/application.properties`.
3. Mở Terminal tại thư mục dự án và chạy `mvnw.cmd spring-boot:run` trên Windows (cần Maven Wrapper tải được Maven và kết nối Internet).
4. Truy cập `http://localhost:8080/login.html`.

## 3. Luồng hoạt động
1. Người dùng nhập tài khoản/mật khẩu và bấm Đăng nhập.
2. Trình duyệt gửi `POST /api/auth/login` với JSON `{ "username": "user", "password": "user123" }`.
3. Server xác thực, tạo JWT chứa `sub` (tài khoản), `role`, thời điểm phát hành và hết hạn, rồi trả token về client.
4. Client gửi token ở header `Authorization: Bearer <JWT>` khi gọi API tiếp theo.
5. Server kiểm tra chữ ký và hạn token, đọc role rồi quyết định cho phép hoặc từ chối.

## 4. API kiểm tra phân quyền
- `POST /api/auth/login`: đăng nhập, không cần token.
- `GET /api/user/profile`: cần JWT có role USER hoặc ADMIN.
- `GET /api/admin/dashboard`: chỉ JWT có role ADMIN mới được phép.

Kết quả thường gặp:
- `200 OK`: authorized / được phép.
- `401 Unauthorized`: thiếu JWT hoặc JWT không hợp lệ/hết hạn, hoặc sai thông tin đăng nhập.
- `403 Forbidden`: đã đăng nhập nhưng role không đủ quyền (ví dụ USER gọi API ADMIN).

Trang `login.html` giữ JWT trong bộ nhớ JavaScript của trang và gửi token cho các API. Làm mới trang sẽ mất token, người dùng cần đăng nhập lại.
