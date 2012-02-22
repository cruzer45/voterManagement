package bz.voter.management.facade

import org.zkoss.zkgrails.*

import bz.voter.management.Activity
import bz.voter.management.ActivityType

class ActivityFacade {

    Activity selected


    public List<ActivityType> getActivityTypes(){
        ActivityType.list([sort:'name'])
    }

    
    /**
    Retrieves an activity.
    @param Long : activity id.
    @return Activity instance.
    **/
    def getActivity(id){
        Activity.get(id.toLong())
    }


    
    /**
    Saves an ActivityType of a given name. If the ActivityType with that name
    already exists, then that instance is returned. Otherwise a new instance is created
    with the name provided.
    @param String name of the ActivityType
    @return ActivityType
    **/
    def saveActivity(activityName){
        def activityType
        ActivityType.withTransaction{status->
            activityType = (ActivityType.findByName(activityName)) ?: new ActivityType(name: activityName).save()
        }

        return activityType
    }

}
