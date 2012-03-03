grails.servlet.version = "2.5"
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        inherits true
        grailsPlugins()
        grailsHome()
        grailsCentral()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenLocal()
        mavenCentral()
        mavenRepo "http://snapshots.repository.codehaus.org"
        mavenRepo "http://repository.codehaus.org"
        mavenRepo "http://download.java.net/maven/2/"
        mavenRepo "http://repository.jboss.com/maven2/"
        mavenRepo "http://zkgrails.googlecode.com/svn/repo/"
        mavenRepo "http://mavensync.zkoss.org/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.13'   

        runtime 'postgresql:postgresql:8.4-701.jdbc4'
        runtime "org.grails.plugins:hibernate:$grailsVersion"
        runtime "org.grails.plugins:jquery:1.7.1"
        runtime "org.grails.plugins:resources:1.1.6"
       
        compile "org.grails.plugins:export:1.1"
        compile "org.grails.plugins:spring-security-core:1.2.7.2"
        compile "org.grails.plugins:mail:1.0"
        compile "org.grails.plugins:rest:0.7" 
        compile "org.grails.plugins:zk:2.0.0.M5"

        build "org.grails.plugins:tomcat:$grailsVersion"   
        
    }

    plugins{
         compile ":database-migration:1.0"

    }
}
