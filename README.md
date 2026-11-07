# X200s 清爽助手（普通 APK 版）

目标设备：vivo X200s / OriginOS 6  
包名：`com.x200s.cleanassistant`  
版本：1.0.0

## 功能
- 7 项系统广告与推荐设置检查清单
- 尝试打开 Android 标准隐私、通知或系统设置页面
- 本地保存完成状态
- 无网络权限、无 Root、无 Shizuku、无 ADB 依赖
- 不会自动停用、卸载或修改系统应用

## 重要限制
普通第三方 APK 无法直接修改 vivo 系统应用的私有设置，也无法保证能跳转到 OriginOS 的每个专有设置子页面。因此部分按钮只会打开系统设置首页，需要用户手动查找对应项目。不同版本菜单名称可能不同。

“已检查”仅表示用户完成了检查，不代表系统广告已全部关闭。

## 云端编译
本仓库配置了 GitHub Actions。进入 Actions，选择 Android APK Build，点击 Run workflow。构建成功后打开对应运行记录，在 Artifacts 下载 `x200s-clean-assistant-debug-apk`，解压得到 APK。

## 使用 Android Studio 构建
1. 安装 Android Studio（建议稳定版）和 Android SDK Platform 35。
2. 打开本项目文件夹。
3. 等待 Gradle Sync 完成。
4. 选择 Build > Build Bundle(s) / APK(s) > Build APK(s)。
5. APK 通常输出在 `app/build/outputs/apk/debug/app-debug.apk`。

本项目不包含 Gradle Wrapper；GitHub Actions 使用 Gradle 8.9 在云端构建。