# backend

> powererd by flask

## 0. 运行环境配置

1. 创建并运行虚拟环境

   ```shell
   cd backend
   virtualenv flask_env
   .\flask_env\Scripts\activate
   deactivate
   ```
2. 需要安装以下依赖

   ```python==3.11.5
   flask==2.2.3
   flask-script==2.0.6
   pymysql==1.0.3
   dbutils==1.3
   flask-sqlalchemy==3.0.3
   ```

   > pip install -r requirements.txt（后续添加依赖需手动写入 requirements 文件）
   >
3. 在config文件夹下创建与settings.py同级的localsettings.py文件，文件应包括数据库的配置，以下为范例

   ```python
   DB_HOST = "127.0.0.1"
   DB_USER = "nku_software_test"
   DB_PWD = "Test_pw123"
   DB_NAME = "db"
   DIALECT = 'mysql'
   DRIVER = 'pymysql'
   DB_PORT = 3306
   SECRET_KEY = "azaaaafg"
   ```
   本项目的数据存在本地，相对路径为 "../frontend/static/synthetic"

## 1. 运行步骤

1. 进入backend目录
   > cd backend
   >
2. 运行项目
   > python app.py
   >


