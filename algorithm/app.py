from flask import Flask
from extension import cors
from config import settings
from backend.views.generate_normal import gn
from backend.views.anomaly_inject import ai

app = Flask(__name__)

# 注册蓝图
app.register_blueprint(gn, url_prefix='/generateNormal')
app.register_blueprint(ai, url_prefix='/anomalyInject')

app.config.from_object(settings)
# db.init_app(app)
cors.init_app(app)


@app.route('/')
def hello_world():
    return 'hello world'


if __name__ == '__main__':
    app.run(debug=True)
