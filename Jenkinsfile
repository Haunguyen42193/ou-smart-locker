pipeline {
    agent any
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
                    sh 'systemctl status docker'
                    sh 'docker build -t haunguyen42195/ou-smart-locker .'
                }
            }
        }
        stage('Push Image To DockerHub') {
            steps {
                script {

                    withCredentials([string(credentialsId: 'dockerhub-pwd', variable: 'dockerhubpwd')]) {
                    sh "docker login -u haunguyen42195@gmail.com -p ${dockerhubpwd}"
                    }

                    sh 'docker push haunguyen42195/ou-smart-locker'
                }
            }
        }
    }
}