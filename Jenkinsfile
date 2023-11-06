def emailTemplate = """
Hello,

This is a Jenkins pipeline notification for build #${currentBuild.number}:

Status: ${currentBuild.result}
Pipeline URL: ${env.BUILD_URL}

Please review the details and take any necessary actions.

Thank you,
Your Jenkins Server
"""
pipeline {
    agent {
        label 'agent1'
    }
   

    stages {
        stage('Checkout') {
            steps {
                script {
                    def branchName = 'ahmed'
                    def gitCredentialId = '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'

                    catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                        checkout([$class: 'GitSCM', 
                            branches: [[name: branchName]], 
                            doGenerateSubmoduleConfigurations: false, 
                            extensions: [[$class: 'CleanBeforeCheckout'], [$class: 'CheckoutOption', timeout: 60], [$class: 'CloneOption', depth: 0, noTags: false, reference: '', shallow: true]], 
                            submoduleCfg: [], 
                            userRemoteConfigs: [[credentialsId: gitCredentialId, url: 'https://github.com/azizmakri/Ski-Station-Project']]
                        ])
                    }
                }
            }
        }

        stage('Checking Docker Version') {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    sh "sudo docker version"
                }
            }
        }
        stage('Unit Testing') {
            steps {
                sh 'mvn test'
            }
        }
        stage('SRC Analysis Testing') {
            steps {
                
                script {
                withSonarQubeEnv('sonarqube-10.2.1'){
                    sh 'mvn sonar:sonar'
                        }
                    }
                
            }
        }
         stage('Build Artifact') {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    sh 'mvn package -DskipTests'
                }
            }
        }
         stage('Build Docker image'){
            steps {
                    sh 'sudo docker build -t bouha881/dockerrepo:latest -f DockerFile .'
            }
        }

         stage('Deploy Artifact to Nexus') {
            steps {
                
                    sh 'mvn deploy -DskipTests=true'
                
            }
        }
         stage('Login to DockerHub') {
            steps {
                script {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                    // Log in to Docker Hub
                    sh "echo $DOCKERHUB_PASSWORD | sudo docker login -u $DOCKERHUB_USERNAME --password-stdin"
                }
            }
        }
    }
 
         stage('Deploy Image to DockerHub') {
            steps {
                // Pushing the image to Docker Hub
                    sh "sudo docker push bouha881/dockerrepo:latest"
            }
        }
        
         stage('Start Containers') {
           steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    sh 'sudo docker-compose up -d'
                }
        }
    }
    
    


    
} 
    post {
         
        always {
            // Send an email notification using the template
            emailext(
                subject: "Pipeline ${currentBuild.result}: ${currentBuild.fullDisplayName}",
                body: emailTemplate, // Use the emailTemplate variable
                to: 'ahmed.jebri@esprit.tn', // Replace with the desired email address
                attachLog: true,
                recipientProviders: [[$class: 'CulpritsRecipientProvider']]
            )
        }
    }
}
