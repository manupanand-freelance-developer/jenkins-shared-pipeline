def call() {
    node {
        // Null-safe branch detection
        def branch = env.BRANCH_NAME ?: ""
        def app    = env.appType

        // Safe trigger user detection
        def userCause = currentBuild?.rawBuild?.getCauses()
                        ?.find { cause -> cause.hasProperty('userId') }
        def user = userCause?.userId ?: "SYSTEM"

        if (branch.startsWith("feature/")) {
            if (app == "nodejs") {
                stage('Checkout') {
                    checkout scm
                    echo "Branch: ${branch}"
                    echo "Triggered by: ${user}"
                }

                stage('Unit test case') {
                    echo "Running Unit test case"
                    echo "npm test"
                }
                stage('Integration Test') {
                    echo "Running Integration test case"
                    echo "npm run integration-test"
                }
                stage('Regression Test') {
                    echo "Running regression test case"
                    echo "npm run regression-test"
                }
                stage('Deploy to Test Environment') {
                    echo "Deploying to test environment..."
                    echo "firebase deploy --project test"
                }
            }

        } else if (branch == "main" || env.TAG_NAME) {
            if (app == "nodejs") {
                stage('Build') {
                    echo "Building production code..."
                }

                stage('Approval') {
                    input message: "Approve deployment to PROD?",
                          submitter: 'admindev,admininfra'
                }

                stage('Deploy to Production') {
                    echo "Deploying to production..."
                    echo "firebase deploy --project production"
                }
            }

        } else {
            echo "No matching deployment rule for this branch."
        }
    }
}
