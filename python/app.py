import os
import tempfile
from flask import Flask, request, jsonify
from vosk import Model, KaldiRecognizer
import wave
import json

app = Flask(__name__)
model = Model("vosk-model-small-fr-0.22")  # Assure-toi que ce chemin est correct

@app.route("/transcribe", methods=["POST"])
def transcribe():
    if "audio" not in request.files:
        return jsonify({"error": "Aucun fichier audio trouvé"}), 400

    audio_file = request.files["audio"]
    with tempfile.NamedTemporaryFile(delete=False, suffix=".wav") as tmp_file:
        # Sauvegarder le fichier dans un fichier temporaire
        audio_file.save(tmp_file.name)

        # Traiter le fichier
        try:
            with wave.open(tmp_file.name, "rb") as wf:
                if wf.getnchannels() != 1 or wf.getsampwidth() != 2 or wf.getframerate() != 16000:
                    return jsonify({"error": "Le fichier doit être WAV mono, 16-bit, 16000 Hz"}), 400

                rec = KaldiRecognizer(model, wf.getframerate())
                results = []
                while True:
                    data = wf.readframes(4000)
                    if len(data) == 0:
                        break
                    if rec.AcceptWaveform(data):
                        results.append(json.loads(rec.Result())["text"])
                results.append(json.loads(rec.FinalResult())["text"])

                return jsonify({"text": " ".join(results)})
        finally:
            try:
                os.remove(tmp_file.name)  # Supprimer le fichier après traitement
            except PermissionError:
                print(f"Erreur lors de la suppression du fichier {tmp_file.name}")
            
if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5001)
