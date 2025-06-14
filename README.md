# Deadline Notification

## Mô tả
Ứng dụng Deadline Notification giúp người dùng quản lý và nhận thông báo về các deadline. Ứng dụng sử dụng các service, repository, adapter và các thành phần khác để xử lý logic nghiệp vụ và giao diện người dùng.

## Cấu trúc thư mục
- **app/src/main/java/com/example/notificationdeadline/**: Chứa mã nguồn chính của ứng dụng.
  - **ui/**: Chứa các Activity và các thành phần giao diện người dùng.
  - **service/**: Chứa các service xử lý logic nghiệp vụ.
  - **repository/**: Chứa các lớp repository để tương tác với dữ liệu.
  - **Adapter/**: Chứa các adapter cho RecyclerView và các thành phần UI khác.
  - **notification/**: Chứa các lớp liên quan đến thông báo.
  - **receiver/**: Chứa các BroadcastReceiver để nhận sự kiện hệ thống.
  - **mapper/**: Chứa các lớp chuyển đổi dữ liệu.
  - **dto/**: Chứa các lớp Data Transfer Object (DTO) cho request và response.
  - **logic/**: Chứa các lớp xử lý logic nghiệp vụ.
  - **data/**: Chứa các lớp quản lý dữ liệu và database.

## Các thành phần chính
- **Activity**: `activity_main.java`, `SplashActivity.java`, và các Activity khác trong thư mục `ui/`.
- **Service**: `NotificationService.java`, `SettingService.java`, `TaskService.java`, `UserService.java`, `NotificationHistoryService.java`.
- **Repository**: `NotificationRepository.java`, `SettingRepository.java`, `TaskRepository.java`, `UserRepository.java`, `NotificationHistoryRepository.java`.
- **Adapter**: `TaskAdapter.java`, `DeadlineAdapter.java`, `FilterButtonAdapter.java`, `NotificationHistoryAdapter.java`, `PrioritySpinnerAdapter.java`.
- **Notification**: `ScanDeadlineWorker.java`, `NotificationScheduler.java`, `DeadlineNotifier.java`, `DeadlineWorker.java`.
- **Receiver**: `BootReceiver.java`, `FixedTimeReceiver.java`.
- **Mapper**: `UserMapper.java`, `NotificationMapper.java`, `SettingMapper.java`.
- **DTO**: Các lớp trong thư mục `dto/request/` và `dto/response/`.
- **Logic**: `DeadlineEvaluator.java`.
- **Data**: `AppDatabase.java`, các lớp DAO, entity và relation trong thư mục `data/`.

## Hướng dẫn cài đặt
1. Clone dự án từ repository.
2. Mở dự án trong Android Studio.
3. Chạy ứng dụng trên thiết bị hoặc máy ảo.

## Yêu cầu
- Android Studio 4.0 trở lên.
- JDK 8 trở lên.
- Android SDK 21 trở lên.

## Liên hệ
Nếu bạn có bất kỳ câu hỏi hoặc góp ý nào, vui lòng liên hệ qua email: [your-email@example.com](mailto:your-email@example.com).