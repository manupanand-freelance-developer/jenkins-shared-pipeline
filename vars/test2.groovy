def call() {
    node {
        echo "DEBUG: currentBuild = ${currentBuild}"
        echo "DEBUG: env.BRANCH_NAME = ${env.BRANCH_NAME}"
    }
}
