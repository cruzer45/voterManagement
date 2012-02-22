// Place your Spring DSL code here

import org.springframework.jdbc.core.JdbcTemplate
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.apache.commons.dbcp.BasicDataSource

beans = {
	addressService(bz.voter.management.AddressService)
    //springSecurityService(grails.plugins.springsecurity.SpringSecurityService)
    //utilsFacade(bz.voter.management.facade.UtilsFacade)
	voterService(bz.voter.management.VoterService)
	divisionVotersPagingListModel(org.zkoss.zklargelivelist.model.DivisionVotersPagingListModel){
		voterService = ref("voterService")
	}

    jdbcTemplate(JdbcTemplate){
        dataSource = ref('dataSource')
    }

    /*
    namedParameterJdbcTemplate(NamedParameterJdbcTemplate){
        dataSource = ref('dataSource')
    }
    */
}
