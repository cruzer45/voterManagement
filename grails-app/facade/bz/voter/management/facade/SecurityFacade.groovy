package bz.voter.management.facade

import org.zkoss.zk.grails.*

import bz.voter.management.*

class SecurityFacade {

	def sessionFactory

   
	/**
	Change a user's password.
	@param Map with the parameters required to change 
	a password:
		<ul>
			<li>oldPassword</li>
			<li>newPassword</li>
			<li>repeatNewPassword</li>
			<li>secUserId</li>
		</ul>
	@return SecUser
	**/
   	public SecUser changePassword(params){
   		SecUser user = SecUser.get(params.secUserId)

   	} 


   	/**
   	Save a user. If user does not exist, create it.
   	@param Map
   	@return SecUser
   	**/
   	public SecUser saveUser(params){
   		SecUser secUser //= SecUser.get(params.secUserId?.toLong()) ?: new SecUser()

   		//flushSession()
   		SecUser.withTransaction{status->
   			secUser = SecUser.get(params.secUserId?.toLong()) ?: new SecUser()

   			secUser.username = params.username ?: secUser.username
   			secUser.password = params.password ?: secUser.password
   			secUser.enabled = params.enabled ?: secUser.enabled
   			secUser.accountExpired = params.accountExpired ?: secUser.accountExpired
   			secUser.accountLocked = params.accountLocked ?: secUser.accountLocked
   			secUser.passwordExpired = params.passwordExpired ?: secUser.passwordExpired

   			secUser.validate()

   			if(secUser.hasErrors()){
   				for(error in secUser.errors){
   					log.error error
   				}
   			}else{
   				secUser.save(flush:true)

   				params.roles.each{authority,assignRole->
   					if(assignRole){
   						setRoleOnUser(authority,secUser)	
   					}else{
   						removeRoleFromUser(authority,secUser)
   					}
   					
   				}
   			}
   		}

   		
   		//flushSession()
   		return secUser
   	}


   	private void setRoleOnUser(String authority, SecUser secUser ){
   		SecRole secRole = SecRole.findByAuthority(authority)

   		if(secRole && (!secUser.hasRole(secRole)))
   		{
   			SecUserSecRole.create(secUser,secRole)
   		}   		
   	}

   	private removeRoleFromUser(String authority, SecUser secUser){
   		SecRole secRole = SecRole.findByAuthority(authority)
   		if(secRole && !secUser.hasRole(secRole)){
   			SecUserSecRole.remove(secUser,secRole)
   		}   		
   	}


   	def flushSession(){
   		sessionFactory?.getCurrentSession()?.flush()
   		sessionFactory?.getCurrentSession()?.clear()
   	}

   
}
