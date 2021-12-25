from flask import Flask, render_template, request, redirect, session, flash, send_from_directory
from flask.helpers import url_for
import os.path

app = Flask(__name__)
app.config['UPLOADS_DIR'] = 'files'

def get_file_name(path):
    path = path.replace('\\', '/')
    head, tail = os.path.split(path)
    return tail or os.path.basename(head)

@app.route("/")
def index():
    return "hello world"

@app.route('/<string:path>', methods=['GET'])
def file_get(path):
    return send_from_directory(app.config['UPLOADS_DIR'], get_file_name(path))

app.run(host="0.0.0.0", port=8080)


