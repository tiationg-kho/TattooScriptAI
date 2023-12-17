from flask import Flask, request, jsonify
from datetime import datetime, timedelta
import os
import jwt
from flask_cors import CORS
from dotenv import load_dotenv
from model import db, User


app = Flask(__name__)
load_dotenv()
frontend_url = os.environ.get("FRONTEND_URL")
cors = CORS(
    app,
    resources={r"/api/*": {"origins": frontend_url}},
    supports_credentials=True
)
secret_key = os.environ.get("SECRET_KEY")
database_url = os.environ.get("DATABASE_URL")
app.config['SQLALCHEMY_DATABASE_URI'] = database_url
db.init_app(app)


with app.app_context():
    db.create_all()


@app.route("/")
def home():
    return "Hello, World!"


@app.route("/api/v1/health", methods=["GET"])
def health_check():
    return jsonify({"status": "healthy",
                    "message": "Service is up and running"}), 200


@app.route("/api/v1/login", methods=["POST"])
def login():
    credential = request.json.get("credential")
    payload = jwt.decode(credential, options={"verify_signature": False})
    email = payload.get("email")
    expiration_time = datetime.utcnow() + timedelta(minutes=1)
    new_payload = {"iss": "TattooScriptAI",
                   "email": email,
                   "exp": expiration_time}
    new_token = jwt.encode(new_payload, secret_key, algorithm="HS256")

    user = User.get_by_email(email)
    if not user:
        User.create(email)

    return jsonify({"token": new_token, "email": email})


@app.route("/api/v1/login", methods=["GET"])
def login_check():
    try:
        auth = request.headers.get("Authorization")
        if not auth:
            return jsonify({"message": "Token is missing!"}), 401
        token = auth[7:]
        data = jwt.decode(token, secret_key, algorithms=["HS256"])
        return jsonify({"message": "Token is valid", "data": data})
    except jwt.ExpiredSignatureError:
        return jsonify({"message": "Token has expired!"}), 401
    except jwt.InvalidTokenError:
        return jsonify({"message": "Invalid token!"}), 401


@app.route("/api/v1/credit", methods=["GET"])
def credit_check():
    try:
        auth = request.headers.get("Authorization")
        if not auth:
            raise ValueError("Token is missing!")
        token = auth[7:]
        data = jwt.decode(token, secret_key, algorithms=["HS256"])
        user = User.get_by_email(data['email'])
        return jsonify({"data": user.credit})
    except Exception as e:
        return jsonify({"message": str(e)}), 500


@app.route("/api/v1/credit/deduct", methods=["PATCH"])
def credit_deduct():
    try:
        auth = request.headers.get("Authorization")
        if not auth:
            raise ValueError("Token is missing!")
        token = auth[7:]
        data = jwt.decode(token, secret_key, algorithms=["HS256"])
        user = User.get_by_email(data['email'])
        if user and user.credit > 0:
            user.credit -= 1
            db.session.commit()
            return jsonify({"data": user.credit})
        else:
            raise ValueError("Invalid user and credit!")
    except Exception as e:
        return jsonify({"message": str(e)}), 400


if __name__ == "__main__":
    app.run(debug=True)
