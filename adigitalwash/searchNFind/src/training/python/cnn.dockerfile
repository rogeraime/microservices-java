FROM tensorflow/tensorflow
VOLUME ${PWD}:/CNN
RUN ["pip install", "--upgrade", "pip"]
RUN ["pip install", "sklearn"]
RUN ["pip install", "keras"]
RUN ["pip install", "imutils"]
RUN ["pip install", "opencv-python-headless"]