import keras

model = keras.models.load_model("CNN_20E_batchNormAxis=1_withoutDownsamplingPadding.pb")
model.save("CNN_20.h5")