# -*- coding: utf-8 -*-
import Emotion_Recognition
import cv2
import numpy

from flask import Flask, request

from flask_cors import CORS
import ssl

app = Flask(__name__)
# app.config.from_pyfile('config.py')
CORS(app)
er_model = Emotion_Recognition


@app.route('/')
def index():
    text = 'Welcome to EngKi AI-MODEL-SERVER'
    return text


@app.route('/emotion', methods=['POST'])
def emotion():

    filestr = request.files['files'].read()

    emotion_data = {}
    # convert string data to numpy array
    npimg = numpy.fromstring(filestr, numpy.uint8)
    # convert numpy array to image
    img = cv2.imdecode(npimg, cv2.IMREAD_COLOR)

    emotion_dict = er_model.emotion_recognition(img)

    # 감정 인식 성공
    if len(emotion_dict) > 0:
        smile = float(emotion_dict['Happy'])
        # print(smile)
        # 웃는다는 기준 수치 정해야할듯
        if smile > 20:
            return "smile", 200
        else:
            return "pass", 200
    # 감정 인식 실패
    else:
        return 400


if __name__ == '__main__':
    ssl_context = ssl.SSLContext(ssl.PROTOCOL_TLS)
    cert = "/etc/letsencrypt/live/k3a503.p.ssafy.io/cert.pem"
    pkey = "/etc/letsencrypt/live/k3a503.p.ssafy.io/privkey.pem"
    # cert = "C:\k3a503.p.ssafy.io\cert1.pem"
    # pkey = "C:\k3a503.p.ssafy.io\privkey1.pem"
    ssl_context.load_cert_chain(certfile=cert, keyfile=pkey)
    app.run(
        host="0.0.0.0",
        debug=True,
        ssl_context=(cert, pkey)
        # ssl_context=ssl_context
    )
