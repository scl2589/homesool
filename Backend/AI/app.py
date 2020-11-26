# -*- coding: utf-8 -*-
import Emotion_Recognition
import cv2
import numpy

from flask import Flask, request

from flask_cors import CORS
import ssl

app = Flask(__name__)
CORS(app)
er_model = Emotion_Recognition


@app.route('/')
def index():
    text = 'HOMESOOL AI SERVER : ON'
    return text


@app.route('/emotion', methods=['POST'])
def emotion():

    filestr = request.files['files'].read()

    # convert string data to numpy array
    npimg = numpy.fromstring(filestr, numpy.uint8)
    # convert numpy array to image
    img = cv2.imdecode(npimg, cv2.IMREAD_COLOR)

    emotion_dict = er_model.emotion_recognition(img)

    if len(emotion_dict) > 0:
        smile = float(emotion_dict['Happy'])

        if smile > 50:
            return "smile", 200
        else:
            return "pass", 200

    else:
        return "no", 200


if __name__ == '__main__':
    ssl_context = ssl.SSLContext(ssl.PROTOCOL_TLS)
    cert = "/etc/letsencrypt/live/k3a503.p.ssafy.io/cert.pem"
    pkey = "/etc/letsencrypt/live/k3a503.p.ssafy.io/privkey.pem"

    ssl_context.load_cert_chain(certfile=cert, keyfile=pkey)
    app.run(
        host="0.0.0.0",
        ssl_context=(cert, pkey)
    )
