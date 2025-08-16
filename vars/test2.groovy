def call() {
    node {
        echo "DEBUG: currentBuild = ${currentBuild}"
        echo "DEBUG: env.BRANCH_NAME = ${env.BRANCH_NAME}"
        echo "DEBUG: env.appType=${env.appType}"
    }
}
