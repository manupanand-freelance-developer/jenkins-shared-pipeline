def call() {
    node {
        try {
        def branch = env.BRANCH_NAME
        
        stage('Checkout') {
            checkout scm
            echo "Branch: ${branch}"
        }
        stage('Setup and Install Dependencies'){
            echo "Installing tools and dependencies"
            nodejs(nodeJSInstallationName: 'NodeJS') {
                dir('firebase/hello-world-app') {
                sh 'npm install -g firebase-tools'
                sh 'npm install'  // This will now work in the correct directory
                }
                //sh 'npm install -g firebase-tools'
                //sh 'npm install'  // Install local dependencies including Angular CLI
            }
        }


        stage(' Firebase Test') {
            echo "Building feature branch..."
            // Use NodeJS plugin installation
            // Option 1: Using configured NodeJS tool
           nodejs(nodeJSInstallationName: 'NodeJS') {
                //sh 'npm install -g firebase-tools'
                //sh 'npm --version'
                // Add your build commands here
                //sh 'npm install'
                //sh 'npm run build'
                //sh 'npm test'
            }
            
            // Option 2: Alternative - if NodeJS tool doesn't work, use direct commands
            //* 
            //sh '''
            //    export PATH="/usr/bin:$PATH"
            //    node --version
            //    npm --version
            //    npm install
            //    npm run build
            //    npm test
            //'''
            //*/
        }
        stage('Firebase build production'){
            nodejs(nodeJSInstallationName: 'NodeJS') {
                dir('firebase/hello-world-app') {
                    sh 'npx ng build --configuration=production'  // Use npx for local CLI
                }
            }
        }
        
        stage('Deployment Approval') {
            echo "Waiting for admindev approval..."
            script {
                try {
                    timeout(time: 60, unit: 'MINUTES') {
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
            nodejs(nodeJSInstallationName: 'NodeJS') {
            dir('firebase/hello-world-app') {
                sh '''
                    # Install Firebase CLI if not already installed
                    #npm install -g firebase-tools
                        
                    # Deploy to Firebase (using service account or login) 
                    firebase deploy --project zeta-flare-449207-r0 --token "$FIREBASE"
                '''
            }
            }
        }
        } finally {
            // This always runs, even if build fails
            echo "Running final cleanup..."
            cleanWs()
        }
    }
}