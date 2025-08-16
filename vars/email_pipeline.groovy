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
                        mimeType: 'text/plain'
                    )
                }
            }
        }
    }
}