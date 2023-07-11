import cv2
from datetime import datetime
import numpy as np
import os
from cv2.data import haarcascades

# cascade_file = os.path.join(haarcascades)
cascade_file = "/home/pi/.local/lib/python3.9/site-packages/cv2/data/haarcascade_frontalface_alt.xml"
cascade = cv2.CascadeClassifier(cascade_file)


def square_line(frame):
    # image_file = "./data/face1.jpg" # ./data/face2.jpg
    nparr = np.frombuffer(frame, np.uint8)
    image = cv2.imdecode(nparr, cv2.IMREAD_COLOR)
    # image = frame.copy()
    image_gs = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

    start = datetime.now()
    fname = start.strftime(f'./media/%Y%m%d_%H%M%S.jpg')

    face_list = cascade.detectMultiScale(image_gs, scaleFactor=1.1, minNeighbors=1, minSize=(150,150))

    if len(face_list) > 0:
        print(face_list)
        color = (0, 0, 255)

        for face in face_list:
            x,y,w,h = face
            cv2.rectangle(image, (x,y), (x+w, y+h), color, thickness=8)

        cv2.imwrite(fname, image)
        print('fname: ', fname)

    else:
        print('사람이 아니에요')
        fname = '인간아님'

    return fname





