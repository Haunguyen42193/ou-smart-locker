pipeline {
    agent any
    tools {
        maven 'Maven3.9.6'
        jdk 'jdk17'
    }
    stages {
        stage('Build Maven') {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Haunguyen42193/ou-smart-locker']])
                bat 'mvn -v'
                bat 'mvn clean install'
            }
        }
        stage('Run Test') {
            steps {
                bat 'mvn test'
            }
        }
        stage('Build Image') {
            steps {
                script {
                    bat 'docker build -t haunguyen42195/ou-smart-locker .'
                }
            }
        }
        stage('Push Image To DockerHub') {
            steps {
                script {

                    withCredentials([string(credentialsId: 'dockerhub-pwd', variable: 'dockerhubpwd')]) {
                    bat "docker login -u haunguyen42195@gmail.com -p ${dockerhubpwd}"
                    }

                    bat 'docker push haunguyen42195/ou-smart-locker'
                }
            }
        }
    }
}