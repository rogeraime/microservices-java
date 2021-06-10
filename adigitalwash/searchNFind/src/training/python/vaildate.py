from sklearn.preprocessing import LabelBinarizer
from sklearn.model_selection import train_test_split
from keras.preprocessing.image import img_to_array
from imutils import paths
import numpy as np
import random
import cv2
import os
import keras

model = keras.models.load_model("CNN_20E_batchNormAxis=1_withoutDownsamplingPadding.pb")
print('[INFO] loading images...')
data_path = sorted(list(paths.list_images("dataset")))
random.seed(54)
random.shuffle(data_path)
data = []
labels_categories = []
labels_colors = []

for data_folder_path in data_path:
    image = cv2.imread(data_folder_path)
    image = cv2.resize(image, (224, 224))
    image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    image = img_to_array(image)
    data.append(image)
    (color, category) = data_folder_path.split(os.path.sep)[-2].split("_")
    labels_categories.append(category)
    labels_colors.append(color)
data = np.array(data, dtype="float") / 255.0
print("[INFO] data matrix: {} images ({:.2f}MB)".format(
len(data_path), data.nbytes / (1024 * 1000.0)))
labels_categories = np.array(labels_categories)
labels_colors = np.array(labels_colors)

print("[INFO] binarizing labels...")
LB_category = LabelBinarizer()
LB_color = LabelBinarizer()
labels_categories = LB_category.fit_transform(labels_categories)
labels_colors = LB_color.fit_transform(labels_colors)

split = train_test_split(data, labels_categories, labels_colors, test_size=0.2, random_state=54)
(x_train, x_test, y_train_category, y_test_category, y_train_color, y_test_color) = split

scores = model.evaluate(x_test, {"category_output": y_test_category, "color_output": y_test_color})
print("%s: %.2f%%" % (model.metrics_names[0], scores[0]*100))
print("%s: %.2f%%" % (model.metrics_names[1], scores[1]*100))
print("%s: %.2f%%" % (model.metrics_names[2], scores[2]*100))
for score in scores:
    print(score)
for metric in model.metrics_names:
    print(metric)
