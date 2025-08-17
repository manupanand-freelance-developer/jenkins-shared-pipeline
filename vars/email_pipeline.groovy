def call() {
    node {
        pipeline {
            agent any
        
            stages {
                stage('Build') {
                    steps {
                        sh 'echo "Hello Build Log"'
                        sh 'ls -l'
                    }
                }
            }
        
            post {
                always {
                    script {
                        echo "Attempting to send email..."
                        echo "Build Number: ${env.BUILD_NUMBER}"
                        echo "Build Status: ${currentBuild.currentResult}"
                        
                        try {
                            emailext(
                                subject: "Build #${env.BUILD_NUMBER} - ${currentBuild.currentResult}",
                                body: '''Hello,

Here are the logs:

${BUILD_LOG, maxLines=100}

Build URL: ${BUILD_URL}
Job Name: ${JOB_NAME}
Build Number: ${BUILD_NUMBER}
Build Status: ${BUILD_STATUS}
''',
                                to: 'manupanand@outlook.com',
                                mimeType: 'text/plain',
                                attachLog: true,
                                compressLog: true
                            )
                            echo "Email sent successfully!"
                        } catch (Exception e) {
                            echo "Failed to send email: ${e.message}"
                            echo "Stack trace: ${e.printStackTrace()}"
                        }
                    }
                }
            }
        }
    }
}