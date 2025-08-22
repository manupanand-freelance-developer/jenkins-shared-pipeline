def call() {
    node {
        def branch = env.BRANCH_NAME
        
        stage('Checkout') {
            checkout scm
            echo "Branch: ${branch}"
        }
        
        stage('Build & Firebase Test') {
            echo "Building feature branch..."
            // Use NodeJS plugin installation
            nodejs(nodeJSInstallationName: 'NodeJS_16') {
                sh 'node --version'
                sh 'npm --version'
                // Add your build commands here
                sh 'npm install'
                sh 'npm run build'
                sh 'npm test'
            }
        }
        
        stage('Deployment Approval') {
            echo "Waiting for admindev approval..."
            script {
                try {
                    timeout(time: 10, unit: 'MINUTES') {
                        input message: 'Deploy to Firebase?', 
                              ok: 'Deploy',
                              submitter: 'admindev',
                              submitterParameter: 'APPROVER',
                              parameters: [
                                  choice(name: 'ENVIRONMENT', 
                                         choices: ['production', 'staging', 'development'], 
                                         description: 'Select deployment environment'),
                                  text(name: 'DEPLOYMENT_NOTES', 
                                       description: 'Add any deployment notes (optional)', 
                                       defaultValue: '')
                              ]
                    }
                    echo "Deployment approved by: ${env.APPROVER}"
                    echo "Deploying to: ${env.ENVIRONMENT}"
                    if (env.DEPLOYMENT_NOTES) {
                        echo "Deployment notes: ${env.DEPLOYMENT_NOTES}"
                    }
                } catch (err) {
                    echo "Deployment approval timeout or rejected"
                    currentBuild.result = 'ABORTED'
                    error("Deployment cancelled - no approval received")
                }
            }
        }
        
        stage('Deploying to Firebase') {
            echo "Deploying to production..."
            // Add your Firebase deployment commands here
            //nodejs(nodeJSInstallationName: 'NodeJS_16') {
            //   sh '''
            //      # Install Firebase CLI if not already installed
            //      npm install -g firebase-tools
            //        
            //        # Deploy to Firebase (using service account or login)
            //        firebase deploy --project your-project-id
            //    '''
            //}
        }
    }
}