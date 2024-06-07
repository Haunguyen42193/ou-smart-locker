pipeline {
    agent any
    environment {
        TWILIO_AUTH_TOKEN = credentials('twilio-auth-token')
        DB_PASSWORD = credentials('db-password')
        SERVER_HOST = credentials('server-host')
        KEYSTORE_PASS = credentials('keystore-pass')
        MAIL_PASS = credentials('mail-pass')
        JWT_SECRET = credentials('jwt-secret')
    }
    tools {
        maven 'Maven3.9.6'
    }
    stages {
        stage('Build Maven') {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Haunguyen42193/ou-smart-locker']])
                sh 'mvn -v'
                sh 'mvn clean install'
            }
        }
        stage('Run Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Build Image') {
            steps {
                script {
                   
                    sh 'sudo docker run hello-world'
                    sh 'sudo docker build -t haunguyen42195/ou-smart-locker .'
                }
            }
        }
        stage('Push Image To DockerHub') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'dockerhub-pwd', variable: 'dockerhubpwd')]) {
                        sh "sudo docker login -u haunguyen42195@gmail.com -p ${dockerhubpwd}"
                    }
                    sh 'sudo docker push haunguyen42195/ou-smart-locker'
                }
            }
        }
        stage('Deploy on server') {
            steps {
                script {
                    sh '''
                        sudo docker ps
                        sudo docker stop smartlocker
                        sudo docker rm smartlocker
                        sudo docker pull haunguyen42195/ou-smart-locker
                        sudo apt install docker-compose
                        ls
                        sudo docker-compose -f /root/docker-compose.yml up -d
                        sudo docker ps
                    '''
                }
            }
        }
    }
}
