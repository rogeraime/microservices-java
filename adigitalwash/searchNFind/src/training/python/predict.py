from keras.preprocessing.image import img_to_array
import cv2
import keras
import numpy as np
from imutils import paths


model = keras.models.load_model("CNN_20.h5")
data_path = sorted(list(paths.list_images("dataset/test/unknown")))
for img_name in data_path:
    print(img_name)
    image = cv2.imread(img_name)
    image = cv2.resize(image, (224, 224))
    image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    image = img_to_array(image)
    image = np.array([image], dtype="float") / 255.0
    color, category = model.predict(image)
    print("Schwarz:" + "{:10.2f}".format(color[0][0]*100) + '%')
    print("Blau:\t" + "{:10.2f}".format(color[0][1]*100)+ '%')
    print("Rot:\t" + "{:10.2f}".format(color[0][2]*100)+ '%')
    print("Kleid:\t" + "{:10.2f}".format(category[0][0]*100)+ '%')
    print("Hose:\t" + "{:10.2f}".format(category[0][1]*100)+ '%')
    print("T-Shirt:" + "{:10.2f}".format(category[0][2]*100)+ '%')