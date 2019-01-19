def URL= 'https://gitlab.com/cmb-avatar/code-generator.git'
def Branch= '*/master'
def PackageName= 'code-generator'
def DockerRegistry= '955065381857.dkr.ecr.cn-north-1.amazonaws.com.cn'
def Image= "${DockerRegistry}/${PackageName}"
pipeline {
    agent {
        label "master"
    }
    triggers {
        pollSCM('* * * * *')
    }
    stages {
        stage('Preparation') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: "${Branch}"]], userRemoteConfigs: [[ url: "${URL}", credentialsId:"gitlibUsername"]]])
            }
        }
        stage('Build') {
            steps {
                sh './gradlew clean build -x test'
            }
        }
        stage('Build Docker Image') {
            steps {
                sh """
                sudo docker build -t ${Image}:${BUILD_NUMBER} .
                """
            }
        }
        stage('Push and Remove Image') {
            steps {
                sh """
                sudo \$(aws ecr get-login --no-include-email --region cn-north-1)
                sudo docker push ${Image}:${BUILD_NUMBER}
                sudo docker rmi ${Image}:${BUILD_NUMBER}
                """
            }
        }
        stage('Restart Docker Container') {
            steps {
                sh """
                sudo ansible-playbook  -i ansible/inventory.ini -e image=${Image} -e tag=${BUILD_NUMBER} ansible/deploy.yml
                """
            }
        }
    }
}