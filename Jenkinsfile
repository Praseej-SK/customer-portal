pipeline {

    agent any

    environment {
        IMAGE_NAME = "customer-portal"
        CONTAINER_NAME = "customer-portal-test"
        APP_PORT = "8080"
        HOST_PORT = "8081"
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out source code from Git...'

                git branch: 'main',
                    credentialsId: 'github-push-credentials',
                    url: 'https://github.com/Praseej-SK/customer-portal.git'
            }
        }

        stage('Build') {
            steps {
                echo 'Building Customer Portal application...'

                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                echo 'Running automated tests...'

                bat 'mvn test'
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building Docker image...'

                bat "docker build -t %IMAGE_NAME%:build-%BUILD_NUMBER% ."
            }
        }

        stage('Container Verification') {
            steps {
                echo 'Starting temporary container...'

                bat "docker run -d --name %CONTAINER_NAME% -p %HOST_PORT%:%APP_PORT% %IMAGE_NAME%:build-%BUILD_NUMBER%"

                echo 'Waiting for application to start...'

                bat 'timeout /t 5 /nobreak'

                echo 'Verifying application health endpoint...'

                bat 'curl --fail http://localhost:8081/health'
            }
        }

        stage('Cleanup') {
            steps {
                echo 'Stopping temporary container...'

                bat 'docker stop %CONTAINER_NAME%'

                echo 'Removing temporary container...'

                bat 'docker rm %CONTAINER_NAME%'
            }
        }
    }

    post {
        success {
            echo "Pipeline completed successfully. Docker image: customer-portal:build-%BUILD_NUMBER%"
        }

        failure {
            echo 'Pipeline failed.'
        }
    }
}