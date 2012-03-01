package bz.voter.management.facade

import org.zkoss.zk.grails.*

import bz.voter.management.*
import bz.voter.management.spring.SpringUtil

class SecurityFacade {

	def sessionFactory
   def springSecurityService = SpringUtil.getBean('springSecurityService')

   
	/**
	Change a user's password.
	@param Map with the parameters required to change 
	a password:
		<ul>
			<li>id</li>
         <li>password</li>
		</ul>
	@return SecUser
	**/
   	public SecUser changePassword(params){
   		SecUser user //= SecUser.get(params.id)

         if(params.id){
            SecUser.withTransaction{status->
               user = SecUser.get(params.id)
               user.password = params.password
               user.validate()
               if(user.hasErrors()){
                  for(error in user){
                     log.error error
                  }
               }else{
                  user.save()
               }
            }
         }

         return user

   	}

      public String validatePasswords(def id, String currentPassword, String newPassword, String verifyPassword){

         def errorStr = "" 
         SecUser.withSession{session->

            SecUser user = SecUser.get(id)

            if(user){
               if(!springSecurityService.encodePassword(currentPassword).equals(user.password)){
                  errorStr += "Current password is incorrect!"
               }

               if(!newPassword.equals(verifyPassword)){
                  errorStr += "The new passwords do not match!" 
               }
        
            }else{
               errorStr = "Invalid User !"
            }
         }

         //sessionFactory.getCurrentSession().clear()

         return errorStr
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
