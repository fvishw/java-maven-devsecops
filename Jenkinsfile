pipeline{
    agent {
        label 'docker-trivy'
    }
    stages{
        stage('Trivy Files Scan'){
            steps{
                sh 'trivy fs --exit-code 1 --severity HIGH,CRITICAL .'
            }
        }
    }
}