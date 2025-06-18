# Use_Android_phone_detect_traffic_sign
# 🚦 Traffic Sign Detection using YOLOv8 + Android

本專案結合 YOLOv8 模型與 Android Camera App，實現交通號誌即時辨識功能。模型可在 Google Colab 上測試，並已轉換為 TensorFlow Lite 格式供 Android App 使用。

---

### 🎥 實測影片

- [📺 道路實測1](https://youtu.be/aLm5FB8fbxg?si=0OlG5yID60spQ9gS) 
- [📺 道路實測2](https://youtu.be/dlAm7eZzP6I) 

---

### 📂 訓練資料集下載

👉 **資料集下載連結**：[Google Drive 資料集](https://drive.google.com/drive/folders/143J2hKB27mz3tHu9Ck2z0dlCs7eyeJKn)  

資料集已經整理為 YOLO 格式，包含以下類別：

- 各類交通號誌圖片
- YOLO 格式標註（`.txt`）
---

### 🚀 在 Google Colab 上測試 YOLOv8 模型

## ✅ 步驟範例
# Step 1：掛載 Google 雲端硬碟
from google.colab import drive
drive.mount('/content/drive')

# Step 2：安裝 YOLOv8 套件
!pip install ultralytics

# Step 3：切換到模型所在資料夾
%cd /content/drive/MyDrive/你的資料夾名稱  # ← 請修改為實際路徑

# Step 4：載入模型並進行推論
from ultralytics import YOLO

model = YOLO("best.pt")  # 載入訓練好的模型
results = model.predict(source="test.jpg", conf=0.3, save=True)  # 預測圖像


# 📂 預測結果輸出位置：
預設會儲存在 runs/detect/predict/ 資料夾中

可透過以下指令顯示圖片：
from IPython.display import Image
Image(filename='runs/detect/predict/test.jpg')

---

## 📱 在 Android Studio 使用 YOLOv8 模型
本專案的 Android App 是參考 AarohiSingla/Object-Detection-Android-App 並加以修改，使其能夠讀取 YOLOv8 TFLite 模型 進行即時交通號誌辨識。
使用方法參考上一行連結之Github即可。
