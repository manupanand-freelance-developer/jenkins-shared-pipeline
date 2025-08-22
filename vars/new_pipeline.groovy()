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
            }
        }

        stage('Deploying to Firebase') {
            echo "Deploying to production..."
        }
    }