pipeline {
    agent {
        label 'agent2'
    }
    stages {
        stage('Checkout') {
            steps {
                script {
                    def branchName = 'aziz'
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
                    sh 'sudo docker build -t azizmk12/skistationrepo:latest -f Dockerfile .'
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
                    sh "sudo docker push azizmk12/skistationrepo:latest"
            }
        }
 
        
         stage('Start Containers') {
           steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    sh 'sudo docker-compose up -d'
                }
        }
    }
     
       
       
} } 
