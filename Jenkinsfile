node {
    stage('Preparation') { // for display purposes
        checkout scm
    }
    stage('Build') {
        withMaven(
                jdk: "JDK 7",
                maven: "Maven 3.3",
                mavenSettingsConfig: "devauto-settings.xml",
                mavenLocalRepo: ".repository") {

            // Run the maven build
            if (isUnix()) {
                // Check that the POM is sorted
                sh "mvn -B -V sortpom:sort"
                sh "git diff --stat --exit-code || (echo '[ERROR] Build changed some Git files' 1>&2 ; exit 1)"
                sh "mvn -B -V -U clean install"
            } else {
                bat(/mvn -B -V -U clean install/)
            }
        }
    }
    stage('Results') {
        archive 'target/*.jar'
        warnings canComputeNew: false,
                canResolveRelativePaths: false,
                consoleParsers: [[parserName: 'Maven'],
                                 [parserName: 'Java Compiler (javac)'],
                                 [parserName: 'JavaDoc Tool']],
                defaultEncoding: '',
                excludePattern: '',
                failedTotalAll: '0',
                healthy: '0',
                includePattern: '',
                messagesPattern: 'Picked up JAVA_TOOL_OPTIONS',
                unHealthy: '1',
                unstableTotalAll: '0'

        checkstyle canComputeNew: false,
                canRunOnFailed: true,
                defaultEncoding: '',
                failedTotalAll: '0',
                healthy: '0',
                pattern: '',
                shouldDetectModules: true,
                unHealthy: '1',
                unstableTotalAll: '0'

        pmd canComputeNew: false,
                defaultEncoding: '',
                failedTotalAll: '0',
                healthy: '0',
                pattern: '',
                shouldDetectModules: true,
                unHealthy: '1',
                unstableTotalAll: '0'

// withMaven covers findbugs
//        findbugs canComputeNew: false,
//                defaultEncoding: '',
//                excludePattern: '',
//                failedTotalAll: '0',
//                healthy: '0',
//                includePattern: '',
//                pattern: '',
//                shouldDetectModules: true,
//                unHealthy: '1',
//                unstableTotalAll: '0'

        hipchatSend color: 'YELLOW',
                credentialId: 'hipchat',
                message: '${env.JOB_NAME} ${env.BUILD_NUMBER} status: ${currentBuild.result}',
                room: 'kam-test',
                v2enabled: true


    }
}
// vim: set et sts=4 sw=4 ts=4 ft=groovy :
