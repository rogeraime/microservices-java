from keras.models import Model
from keras.layers.normalization import BatchNormalization
from keras.layers.convolutional import Conv2D
from keras.layers.convolutional import MaxPooling2D
from keras.layers.convolutional import AveragePooling2D
from keras.layers.core import Activation
from keras.layers.core import Dense
from keras.layers import Flatten
from keras.layers import Input
from keras.layers import Add
from keras.layers import ZeroPadding2D

class ImageTaggingCNN:
    """ Factory Klasse zum erstellen eines Residual CNN """
   
    def _input_conv_layer(self, input, num_of_kernel):    
        """ Erzeugt ein Convolution Layer mit einem 7 x 7 Kernel einem Stride von 2 gefolgt von einem Max-Pooling

        :tensor|list input: Die Tensoren aus vorherigen Layer  
        :int num_of_kernel: Die Anzahl der Kernel (Filter)    
        """
        x = ZeroPadding2D(
            padding=3)(input)
        x = Conv2D(
            filters=num_of_kernel,
            kernel_size=7,
            strides=2,
            padding='valid',
            kernel_initializer='VarianceScaling')(x)
        x = BatchNormalization(
            axis=3)(x)
        x = Activation('relu')(x)
        return MaxPooling2D(
            pool_size=2,
            padding='same')(x)

    def _residual_block(self, input, num_of_kernel):
        """ Erzeugt ein residual Block mit Identity Shortcut
            (Conv2, Batchnorm., ReLU, Conv2, Batchnorm., Add, RELU)

        :tensor|list input: Die Tensoren aus vorherigen Layer
        :int num_of_kernel: Die Anzahl der Kernel (Filter)
        """ 
        x = Conv2D(
            filters=num_of_kernel,
            kernel_size=3,
            kernel_initializer='VarianceScaling',
            padding='same')(input)
        x = BatchNormalization(axis=1)(x)
        x = Activation('relu')(x)
        x = Conv2D(
            filters=num_of_kernel,
            kernel_size=3,
            kernel_initializer='VarianceScaling',
            padding='same')(x)
        x = BatchNormalization(axis=1)(x)
        x = Add()([x, input])
        return Activation('relu')(x)

    def _downsampling_layer(self, input, num_of_kernel):
        """ Ein Convolution Layer mit 3 x 3 Kernel und 2px Stride  

        :tensor|list input: Die Tensoren aus vorherigen Layer
        :int num_of_kernel: Die Anzahl der Kernel (Filter)
        """
        return Conv2D(
            filters=num_of_kernel,
            kernel_size=3,
            strides=2,
            kernel_initializer='VarianceScaling')(input)

    def _classifier_block(self, input, name_of_output, num_of_output_neurons, num_of_neurons=1024):
        """ Erzeugt 3 FC Layer mit ReLU in den ersten 2 und Softmax Activation im Letzten Layer

        :tensor|list input: Die Tensoren aus vorherigen Layer
        :string name_of_output: Name des Output Layers
        :int num_of_output_neurons: Die Anzahl der zu Klassifizierenden Klassen
        :int num_of_neurons: Die Anzahl der Neuronen der hidden Layer
        """
        x = Dense(
            units=num_of_neurons,
            activation='relu')(input)
        x = Dense(
            units=num_of_neurons,
            activation='relu')(x)
        return Dense(
            units=num_of_output_neurons,
            activation='softmax',
            name=name_of_output)(x)

    @staticmethod
    def build(input_shape):
        """ Baut das CNN zusammen
        
        :3Tupel input_shape: Das Format des Input Tensors
        :rtype: Model
        """
        imgCNN = ImageTaggingCNN()
        input_layer = Input(shape=input_shape)
        x = imgCNN._input_conv_layer(input_layer, 32)
        x = imgCNN._residual_block(x, 32)
        x = imgCNN._residual_block(x, 32)
        x = imgCNN._downsampling_layer(x, 64)
        x = imgCNN._residual_block(x, 64)
        x = imgCNN._residual_block(x, 64)
        x = imgCNN._downsampling_layer(x, 128)
        x = imgCNN._residual_block(x, 128)
        x = imgCNN._residual_block(x, 128)
        x = imgCNN._downsampling_layer(x, 256)
        x = imgCNN._residual_block(x, 256)
        x = imgCNN._residual_block(x, 256)
        x = AveragePooling2D(
            pool_size=2,
            padding='same')(x)
        x = Flatten()(x)
        color_output = imgCNN._classifier_block(x, 'color_output', 3)
        category_output = imgCNN._classifier_block(x, 'category_output', 3)
        return Model(
			inputs=input_layer,
			outputs=[color_output, category_output],
			name="imageTaggingCNN")
