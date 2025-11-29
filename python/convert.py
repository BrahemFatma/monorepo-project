from flask import Flask, request, jsonify
import subprocess
import os
from flask_cors import CORS

app = Flask(__name__)
CORS(app)
UPLOAD_FOLDER = './uploads'
os.makedirs(UPLOAD_FOLDER, exist_ok=True)

@app.route('/convert', methods=['POST'])
def convert_audio():
    file = request.files['file']
    input_path = os.path.join(UPLOAD_FOLDER, file.filename)
    output_path = os.path.join(UPLOAD_FOLDER, 'converted.wav')
    file.save(input_path)

    command = [
    'C:/ffmpeg/ffmpeg-7.1.1-essentials_build/bin/ffmpeg.exe',
    '-y',  # <-- ajoute cette ligne
    '-i', input_path,
    '-ac', '1',
    '-ar', '16000',
    '-sample_fmt', 's16',
    output_path
]

    subprocess.run(command, check=True)

    return jsonify({'message': 'Conversion réussie', 'output': output_path})

if __name__ == '__main__':
    app.run(debug=True)
