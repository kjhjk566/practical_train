
# Vue 项目 README

## 项目介绍

这是一个基于 Vue.js 构建的 Web 项目，使用 Yarn 作为包管理工具。本指南将帮助您在 macOS 和 Windows 上配置开发环境，并介绍如何启动项目。

## 环境要求

- Node.js (版本 12.x 或更高)
- Yarn (版本 1.x 或更高)
- Vue CLI (全局安装)

## 在 macOS 上配置开发环境

1. **安装 Homebrew（如果尚未安装）**:

    打开终端并输入以下命令安装 Homebrew:
    
    ```bash
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
    ```

2. **安装 Node.js 和 Yarn**:

    使用 Homebrew 安装 Node.js 和 Yarn:
    
    ```bash
    brew install node
    brew install yarn
    ```

3. **安装 Vue CLI**:

    全局安装 Vue CLI:
    
    ```bash
    yarn global add @vue/cli
    ```

4. **克隆项目仓库**:

    使用以下命令克隆项目仓库:
    
    ```bash
    git clone <your-repository-url>
    cd <your-project-directory>
    ```

5. **安装依赖**:

    在项目目录下运行以下命令安装依赖:
    
    ```bash
    yarn install
    ```

## 在 Windows 上配置开发环境

1. **安装 Node.js**:

    从 [Node.js 官方网站](https://nodejs.org/) 下载并安装适用于 Windows 的 Node.js。

2. **安装 Yarn**:

    从 [Yarn 官方网站](https://classic.yarnpkg.com/en/docs/install#windows-stable) 下载并安装 Yarn。

3. **安装 Vue CLI**:

    打开命令提示符（或 PowerShell），然后运行以下命令全局安装 Vue CLI:
    
    ```bash
    yarn global add @vue/cli
    ```

4. **克隆项目仓库**:

    使用以下命令克隆项目仓库:
    
    ```bash
    git clone <your-repository-url>
    cd <your-project-directory>
    ```

5. **安装依赖**:

    在项目目录下运行以下命令安装依赖:
    
    ```bash
    yarn install
    ```

## 启动项目

1. **开发模式下运行项目**:

    在项目目录下运行以下命令启动开发服务器:
    
    ```bash
    yarn serve
    ```

    服务器启动后，您可以在浏览器中访问 `http://localhost:8080` 查看项目。

2. **构建项目**:

    如果您准备将项目部署到生产环境，可以运行以下命令构建项目:
    
    ```bash
    yarn build
    ```

    构建输出将存储在 `dist/` 目录下。

## 结语

按照上述步骤，您应该能够在 macOS 或 Windows 上成功配置开发环境并启动 Vue 项目。如有任何问题，请参考官方文档或联系项目维护者。
