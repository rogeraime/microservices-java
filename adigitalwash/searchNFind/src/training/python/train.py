from ImageTaggingCNN import ImageTaggingCNN
from sklearn.preprocessing import LabelBinarizer
from sklearn.model_selection import train_test_split
from tensorflow.keras.preprocessing.image import img_to_array
from tensorflow.keras.optimizers import Adam
from imutils import paths
import numpy as np
import random
import cv2
import os

class Train:
    """Klasse zum trainieren des neuronalen Netzes"""
    # Wesentliche Einstellungen zum Training
    EPOCHS = 40
    LEARNINGRATE = 0.001
    BATCHSIZE = 32
    INPUT_SHAPE = (224, 224, 3)
    PATH = 'dataset'
    # Definition der Loss Function, die fuer den jeweiligen Output verwendet werden soll
    LOSSES = {
	    "category_output": "categorical_crossentropy",
	    "color_output": "categorical_crossentropy",
    }
    # Die Gewichtung(wie Wichtig eine gute Klassifizierung des Tags ist) des jeweiligen Outputs
    LOSS_WEIGHTS = {"category_output": 1.0, "color_output": 1.0}
    def initialize_model(self):
        """Initialisierung des Models"""
        print("[INFO] initializing model...")
        self.model = ImageTaggingCNN.build(self.INPUT_SHAPE)
        print("[INFO] compiling model...")
        optimizer = Adam(lr=self.LEARNINGRATE, decay=self.LEARNINGRATE / self.EPOCHS)
        self.model.compile(optimizer=optimizer, loss=self.LOSSES, loss_weights=self.LOSS_WEIGHTS, metrics=["accuracy"])
        self.model.summary()

    def load_and_preprocess_images(self):
        """Laedt die Bilder, skaliert, normalisiert und speichert RGB Werte und Label in die jeweiligen Arrays"""
        print('[INFO] loading images...')
        data_path = sorted(list(paths.list_images(self.PATH)))
        random.seed(54)
        random.shuffle(data_path)
        data = []
        labels_categories = []
        labels_colors = []
    
        for data_folder_path in data_path:
            image = cv2.imread(data_folder_path)
            image = cv2.resize(image, (self.INPUT_SHAPE[0], self.INPUT_SHAPE[1]))
            image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
            image = img_to_array(image)
            data.append(image)
            (color, category) = data_folder_path.split(os.path.sep)[-2].split("_")
            labels_categories.append(category)
            labels_colors.append(color)
        self.data = np.array(data, dtype="float") / 255.0
        print("[INFO] data matrix: {} images ({:.2f}MB)".format(
	    len(data_path), self.data.nbytes / (1024 * 1000.0)))
        labels_categories = np.array(labels_categories)
        labels_colors = np.array(labels_colors)

        print("[INFO] binarizing labels...")
        LB_category = LabelBinarizer()
        LB_color = LabelBinarizer()
        self.labels_categories = LB_category.fit_transform(labels_categories)
        self.labels_colors = LB_color.fit_transform(labels_colors)
        
        split = train_test_split(self.data, self.labels_categories, self.labels_colors, test_size=0.2, random_state=54)
        (self.x_train, self.x_test, self.y_train_category, self.y_test_category, self.y_train_color, self.y_test_color) = split

    def train(self):
        self.model.fit(self.x_train,
            {"category_output": self.y_train_category, "color_output": self.y_train_color},
	        epochs=self.EPOCHS, batch_size=self.BATCHSIZE)
        scores = self.model.evaluate(self.x_test,
           {"category_output": self.y_test_category, "color_output": self.y_test_color},
	        verbose=2)
        for i in range(5):
            print("%s: %.2f%%" % (self.model.metrics_names[i], scores[i]*100))
        self.model.save("CNN_" + str(self.EPOCHS) + "E_IMGTAGCNN_V2_batchNormAxis=1_inputbatchnormaxis=3_withoutDownsamplingPadding.h5")

    def setEpoch(self, epoch):
        self.EPOCHS = epoch
        self.initialize_model()

    def __init__(self, epoch = EPOCHS):
        self.setEpoch(epoch)
        self.load_and_preprocess_images()
        self.train()

Train(1)