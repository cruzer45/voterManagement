dataSource {
    pooled = true
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
    jdbc.batch_size = 100
}
// environment specific settings
environments {
    development {
        dataSource {
		  	driverClassName = "org.postgresql.Driver"
			username = "voter_management"
			password = ".,password.\$"
            //dbCreate = "create-drop" // one of 'create', 'create-drop','update'
            url = "jdbc:postgresql://127.0.0.1:5432/albert-division"
        }
    }
    test {
        dataSource {
		  	driverClassName = "org.postgresql.Driver"
			username = "voter_management"
			password = ".,password.\$"
            dbCreate = "create-drop"
            url = "jdbc:postgresql://127.0.0.1:5432/voter-management-test"
        }
    }
    production {
        dataSource {
    		driverClassName = "org.postgresql.Driver"
    		username = "voter_management"
    		password = ".,password.\$"
            //dbCreate = "update"
			dialect = 'org.hibernate.dialect.PostgreSQLDialect'
            //url = "jdbc:postgresql://127.0.0.1:5432/voter-management"
            url = "jdbc:postgresql://127.0.0.1:5432/voter-management"
        }
    }
}
