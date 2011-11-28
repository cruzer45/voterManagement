package bz.voter.management

import org.apache.commons.lang.builder.HashCodeBuilder

class VoterElection implements Serializable{

	Voter voter
	Election election
	boolean voted
	String pickupTime
	Date voteTime


    static constraints = {
	 	pickupTime(nullable:true)
		voteTime(nullable: true)
    }


	 boolean equals(other){
	 	if(!(other instanceof VoterElection)){
			return false
		}

		other.voter?.id == voter?.id &&
			other.election?.id == election?.id
	 }


	 int hashCode(){
	 	def builder = new HashCodeBuilder()
		if (voter) builder.append(voter.id)
		if(election) builder.append(election.id)
		if(pickupTime) builder.append(pickupTime)
		if(voteTime) builder.append(voteTime)
		builder.toHashCode()
	 }


	 static VoterElection get(long voterId, electionId){
	 	find "from VoterElection where voter.id=:voterId and election.id=:electionId",
			[voterId: voterId, electionId: electionId]
	 }


	 static VoterElection create(Voter voter, Election election, boolean flush=false){
	 	new VoterElection(voter: voter, election: election, voted: false).save(flush:flush,insert:true)
	 }


	 static VoterElection update(Voter voter, Election election, boolean voted, 
	 	Date pickupTime,Date voteTime, flush=false){
	 	def instance = VoterElection.get(voter.id,election.id)
		if(instance){
			instance.voted = voted
			instance.pickupTime = pickupTime
			instance.pickupTime = voteTime
			instance.save(flush:flush)
		}

		return instance
	 }


	 static mapping = {
	 	id composite: ['voter','election']
		version false
	 }

	/**
	Counts the votes casted at a Division, grouped by poll station, 
	affiliation and division.
	@param election is the election for which we want count votes.
	@param division is the division for which we want to count votes
	@return list of results: [voteCounts, poll_station, affiliation ]
	**/
	 static countVotesByPollStationAndAffiliation(Election election,Division division){
	 	if(election instanceof Election){
	 		def query = "select count(v.id) as voteCounts, " +
				"p.pollNumber as poll_station, " +
				"aff.name as party "+
				"from VoterElection ve"+
				" inner join ve.voter as v" +
				" inner join v.affiliation as aff" +
				" inner join v.pollStation as p" +
				" where ve.election = :election and ve.voted = true " +
				" and p.division = :division " +
				" group by p.pollNumber, aff.name"

			def results = VoterElection.executeQuery(query,
				[election: election, division: division])
		}
	 }
}
