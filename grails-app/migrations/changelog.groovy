databaseChangeLog = {

	changeSet(author: "rguerra (generated)", id: "1323274351757-1") {
		createTable(tableName: "address") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "addressPK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "house_number", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "municipality_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "street", type: "varchar(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-2") {
		createTable(tableName: "affiliation") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "affiliationPK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "varchar(255)") {
				constraints(nullable: "false", unique: "true")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-3") {
		createTable(tableName: "district") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "districtPK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "code", type: "varchar(255)") {
				constraints(nullable: "false", unique: "true")
			}

			column(name: "name", type: "varchar(255)") {
				constraints(nullable: "false", unique: "true")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-4") {
		createTable(tableName: "division") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "divisionPK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "varchar(255)") {
				constraints(nullable: "false", unique: "true")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-5") {
		createTable(tableName: "election") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "electionPK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "election_date", type: "timestamp")

			column(name: "election_type_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "year", type: "int4") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-6") {
		createTable(tableName: "election_type") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "election_typePK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "code", type: "varchar(255)") {
				constraints(nullable: "false", unique: "true")
			}

			column(name: "name", type: "varchar(255)") {
				constraints(nullable: "false", unique: "true")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-7") {
		createTable(tableName: "ethnicity") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "ethnicityPK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "varchar(255)") {
				constraints(nullable: "false", unique: "true")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-8") {
		createTable(tableName: "identification_type") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "identificatioPK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "varchar(255)") {
				constraints(nullable: "false", unique: "true")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-9") {
		createTable(tableName: "municipality") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "municipalityPK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "district_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "varchar(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-10") {
		createTable(tableName: "person") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "personPK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "address_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "birth_date", type: "timestamp") {
				constraints(nullable: "false")
			}

			column(name: "cell_phone", type: "varchar(255)")

			column(name: "comments", type: "varchar(255)")

			column(name: "ethnicity_id", type: "int8")

			column(name: "first_name", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "home_phone", type: "varchar(255)")

			column(name: "last_name", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "middle_name", type: "varchar(255)")

			column(name: "sex_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "work_phone", type: "varchar(255)")
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-11") {
		createTable(tableName: "pledge") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "pledgePK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "varchar(255)") {
				constraints(nullable: "false", unique: "true")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-12") {
		createTable(tableName: "poll_station") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "poll_stationPK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "division_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "poll_number", type: "varchar(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-13") {
		createTable(tableName: "registration_code") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "registration_PK")
			}

			column(name: "date_created", type: "timestamp") {
				constraints(nullable: "false")
			}

			column(name: "token", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "username", type: "varchar(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-14") {
		createTable(tableName: "sec_role") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "sec_rolePK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "authority", type: "varchar(255)") {
				constraints(nullable: "false", unique: "true")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-15") {
		createTable(tableName: "sec_user") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "sec_userPK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "account_expired", type: "bool") {
				constraints(nullable: "false")
			}

			column(name: "account_locked", type: "bool") {
				constraints(nullable: "false")
			}

			column(name: "enabled", type: "bool") {
				constraints(nullable: "false")
			}

			column(name: "password", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "password_expired", type: "bool") {
				constraints(nullable: "false")
			}

			column(name: "username", type: "varchar(255)") {
				constraints(nullable: "false", unique: "true")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-16") {
		createTable(tableName: "sec_user_sec_role") {
			column(name: "sec_role_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "sec_user_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-17") {
		createTable(tableName: "sex") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "sexPK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "code", type: "varchar(255)") {
				constraints(nullable: "false", unique: "true")
			}

			column(name: "name", type: "varchar(255)") {
				constraints(nullable: "false", unique: "true")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-18") {
		createTable(tableName: "voter") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "voterPK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "affiliation_id", type: "int8")

			column(name: "identification_type_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "person_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "pledge_id", type: "int8")

			column(name: "poll_station_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "registration_date", type: "timestamp") {
				constraints(nullable: "false")
			}

			column(name: "registration_number", type: "varchar(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-19") {
		createTable(tableName: "voter_election") {
			column(name: "voter_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "election_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "pickup_time", type: "varchar(255)")

			column(name: "vote_time", type: "timestamp")

			column(name: "voted", type: "bool") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-20") {
		addPrimaryKey(columnNames: "sec_role_id, sec_user_id", constraintName: "sec_user_sec_PK", tableName: "sec_user_sec_role")
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-21") {
		addPrimaryKey(columnNames: "voter_id, election_id", constraintName: "voter_electioPK", tableName: "voter_election")
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-22") {
		createIndex(indexName: "name_unique_1323274351541", tableName: "affiliation", unique: "true") {
			column(name: "name")
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-23") {
		createIndex(indexName: "code_unique_1323274351551", tableName: "district", unique: "true") {
			column(name: "code")
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-24") {
		createIndex(indexName: "name_unique_1323274351552", tableName: "district", unique: "true") {
			column(name: "name")
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-25") {
		createIndex(indexName: "name_unique_1323274351555", tableName: "division", unique: "true") {
			column(name: "name")
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-26") {
		createIndex(indexName: "code_unique_1323274351569", tableName: "election_type", unique: "true") {
			column(name: "code")
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-27") {
		createIndex(indexName: "name_unique_1323274351570", tableName: "election_type", unique: "true") {
			column(name: "name")
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-28") {
		createIndex(indexName: "name_unique_1323274351572", tableName: "ethnicity", unique: "true") {
			column(name: "name")
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-29") {
		createIndex(indexName: "name_unique_1323274351574", tableName: "identification_type", unique: "true") {
			column(name: "name")
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-30") {
		createIndex(indexName: "name_unique_1323274351589", tableName: "pledge", unique: "true") {
			column(name: "name")
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-31") {
		createIndex(indexName: "authority_unique_1323274351597", tableName: "sec_role", unique: "true") {
			column(name: "authority")
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-32") {
		createIndex(indexName: "username_unique_1323274351602", tableName: "sec_user", unique: "true") {
			column(name: "username")
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-33") {
		createIndex(indexName: "code_unique_1323274351611", tableName: "sex", unique: "true") {
			column(name: "code")
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-34") {
		createIndex(indexName: "name_unique_1323274351612", tableName: "sex", unique: "true") {
			column(name: "name")
		}
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-35") {
		addForeignKeyConstraint(baseColumnNames: "municipality_id", baseTableName: "address", constraintName: "FKBB979BF4ADB9B3E9", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "municipality", referencesUniqueColumn: "false")
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-36") {
		addForeignKeyConstraint(baseColumnNames: "election_type_id", baseTableName: "election", constraintName: "FKFEFA9419A5BB38BC", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "election_type", referencesUniqueColumn: "false")
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-37") {
		addForeignKeyConstraint(baseColumnNames: "district_id", baseTableName: "municipality", constraintName: "FKBDF4447CB252B229", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "district", referencesUniqueColumn: "false")
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-38") {
		addForeignKeyConstraint(baseColumnNames: "address_id", baseTableName: "person", constraintName: "FKC4E39B5568FE482B", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "address", referencesUniqueColumn: "false")
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-39") {
		addForeignKeyConstraint(baseColumnNames: "ethnicity_id", baseTableName: "person", constraintName: "FKC4E39B55ED0D800B", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "ethnicity", referencesUniqueColumn: "false")
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-40") {
		addForeignKeyConstraint(baseColumnNames: "sex_id", baseTableName: "person", constraintName: "FKC4E39B55E7B33B6B", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "sex", referencesUniqueColumn: "false")
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-41") {
		addForeignKeyConstraint(baseColumnNames: "division_id", baseTableName: "poll_station", constraintName: "FKA43810B43283A9C9", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "division", referencesUniqueColumn: "false")
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-42") {
		addForeignKeyConstraint(baseColumnNames: "sec_role_id", baseTableName: "sec_user_sec_role", constraintName: "FK6630E2A4BA602EE", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "sec_role", referencesUniqueColumn: "false")
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-43") {
		addForeignKeyConstraint(baseColumnNames: "sec_user_id", baseTableName: "sec_user_sec_role", constraintName: "FK6630E2AF0D0C6CE", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "sec_user", referencesUniqueColumn: "false")
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-44") {
		addForeignKeyConstraint(baseColumnNames: "affiliation_id", baseTableName: "voter", constraintName: "FK6B30AC81B2E362B", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "affiliation", referencesUniqueColumn: "false")
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-45") {
		addForeignKeyConstraint(baseColumnNames: "identification_type_id", baseTableName: "voter", constraintName: "FK6B30AC865E7EA6", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "identification_type", referencesUniqueColumn: "false")
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-46") {
		addForeignKeyConstraint(baseColumnNames: "person_id", baseTableName: "voter", constraintName: "FK6B30AC85598C889", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "person", referencesUniqueColumn: "false")
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-47") {
		addForeignKeyConstraint(baseColumnNames: "pledge_id", baseTableName: "voter", constraintName: "FK6B30AC8635EFE09", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "pledge", referencesUniqueColumn: "false")
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-48") {
		addForeignKeyConstraint(baseColumnNames: "poll_station_id", baseTableName: "voter", constraintName: "FK6B30AC8B010BB6C", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "poll_station", referencesUniqueColumn: "false")
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-49") {
		addForeignKeyConstraint(baseColumnNames: "election_id", baseTableName: "voter_election", constraintName: "FK8ED85BB07A95E249", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "election", referencesUniqueColumn: "false")
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-50") {
		addForeignKeyConstraint(baseColumnNames: "voter_id", baseTableName: "voter_election", constraintName: "FK8ED85BB0A27EFEB", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "voter", referencesUniqueColumn: "false")
	}

	changeSet(author: "rguerra (generated)", id: "1323274351757-51") {
		createSequence(sequenceName: "hibernate_sequence")
	}

	include file: 'changelog-0.1.2.groovy'

	include file: 'changelog-0.1.2.groovy'

	include file: 'changelog-1.0.1.groovy'
}
