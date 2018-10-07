node('linux && ansible') {

    tools {
        maven 'maven -3.5.4'
        jdk 'java1.8.0'
    }


    stage ('Initialize') {
        steps {
            sh '''
                echo "PATH = ${PATH}"
                echo "M2_HOME = ${M2_HOME}"
            '''
        }
    }

    stage ('Build') {
        steps {
            sh 'mvn -Dmaven.test.failure.ignore=true install'
        }
    }
}