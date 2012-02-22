// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

//Default properties are stored in this location.
//Required property is division and should be declared as : division = DivisionName
//When app boots up, a division with this name will be created if it doesn't exist.
//Actions will only be allowed in this division, even if other divisions exist in the database.
grails.config.locations = ["file:/usr/local/${appName}0.properties"]
// if(System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text/plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      excel: "application/vnd.ms-excel",
                      pdf: "application/pdf",
                      csv: 'text/csv',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// whether to install the java.util.logging bridge for sl4j. Disable for AppEngine!
grails.logging.jul.usebridge = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

files.dir='/usr/local/files/'
importvoters.serverURL = "http://localhost:9090/import-voters/api/"

// set per-environment serverURL stem for creating absolute links
environments {
    production {
        grails.serverURL = "http://74.207.233.149:8080/${appName}"
    }
    development {
        grails.serverURL = "http://localhost:8080/${appName}"
    }
    test {
        grails.serverURL = "http://localhost:8080/${appName}"
    }

}

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    appenders {
        console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')

        environments{
            production{
                rollingFile name:'rollingFile', maxFileSize:102400, maxBackupIndex: 5, file:'/usr/local/tomcat/logs/voterManagement.log',
                    layout:pattern(conversionPattern:'%d %-5p %c{2} %x - %m%n')
            }
        }

    }

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'

    warn   'org.mortbay.log'
}

// Added by the Spring Security Core plugin:
//grails.plugins.springsecurity.userLookup.userDomainClassName = 'bz.voter.management.SecUser'
//grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'bz.voter.management.UserProfile'
//grails.plugins.springsecurity.authority.className = 'bz.voter.management.Profile'

// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'bz.voter.management.SecUser'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'bz.voter.management.SecUserSecRole'
grails.plugins.springsecurity.authority.className = 'bz.voter.management.SecRole'
grails.plugins.springsecurity.filterChain.chainMap = [
	'/zkau/upload': 'anonymousAuthenticationFilter',
	'/**': 'JOINED_FILTERS',
]

// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'bz.voter.management.SecUser'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'bz.voter.management.SecUserSecRole'
grails.plugins.springsecurity.authority.className = 'bz.voter.management.SecRole'
