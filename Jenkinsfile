pipeline{
    agent {
        label 'docker-trivy'
    }
    tools{
        maven 'maven3'
    }
    environment {
        SONAR_IP = '172.31.11.67'
        ECR_REGISTRY = '389352669036.dkr.ecr.ap-south-1.amazonaws.com'

    }
    stages{
        stage('Trivy Files Scan'){
            steps{
                sh 'trivy fs --exit-code 1 --severity HIGH,CRITICAL .'
            }
        }
        stage('Build and SonarQube Scan'){
            steps{
                withCredentials([string(credentialsId: 'sonarqube-token', variable: 'SONAR_TOKEN')]) {
                    
                    sh '''
                        mvn clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121:sonar \
                        -Dsonar.projectKey=maven-app \
                        -Dsonar.host.url="http://${SONAR_IP}:9000" \
                        -Dsonar.token="${SONAR_TOKEN}" \
                        -Dsonar.qualitygate.wait=true
                    ''' 
                }
            }
        }
        stage('ECR login'){
            steps{
                sh 'aws ecr get-login-password --region ap-south-1 | docker login --username AWS --password-stdin ${ECR_REGISTRY}'
            }
        }
    }
}