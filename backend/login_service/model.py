from flask_sqlalchemy import SQLAlchemy
from datetime import datetime

db = SQLAlchemy()


class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    email = db.Column(db.String(120), unique=True, nullable=False)
    credit = db.Column(db.Integer, default=3)
    create_date = db.Column(db.DateTime, default=datetime.utcnow)

    def __repr__(self):
        return f"<User {self.email}>"

    @classmethod
    def create(cls, email):
        new_user = cls(email=email)
        db.session.add(new_user)
        db.session.commit()
        return new_user

    @classmethod
    def get_by_email(cls, email):
        return cls.query.filter_by(email=email).first()
