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
        stage('Deploy on server') {
            steps {
                script {
                    sshagent(['VPS-pwd']) {
                        sh 'ssh root@14.225.253.41'
                        sh 'docker ps'
                        sh 'docker stop smartlocker'
                        sh 'docker rm smartlocker'
                        sh 'docker pull haunguyen42195/ou-smart-locker'
                        sh 'docker run -d -p 8081:8081 --name smartlocker --restart unless-stopped -e "TZ=Asia/Ho_Chi_Minh" haunguyen42195/ou-smart-locker'
                        sh 'docker ps'
                    }
                }
            }
        }
    }
}