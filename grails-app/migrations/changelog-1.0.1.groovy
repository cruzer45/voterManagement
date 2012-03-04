databaseChangeLog = {

	changeSet(author: "rguerra (generated)", id: "1330780520081-1") {
		createTable(tableName: "activity") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "activityPK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "activity_date", type: "timestamp") {
				constraints(nullable: "false")
			}

			column(name: "activity_type_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "notes", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "voter_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-2") {
		createTable(tableName: "activity_type") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "activity_typePK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "varchar(255)") {
				constraints(nullable: "false", unique: "true")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-3") {
		createTable(tableName: "address_type") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "address_typePK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "varchar(255)") {
				constraints(nullable: "false", unique: "true")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-4") {
		createTable(tableName: "dependent") {
			column(name: "voter_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "person_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "relation_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-5") {
		createTable(tableName: "relation") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "relationPK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "varchar(255)") {
				constraints(nullable: "false", unique: "true")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-6") {
		createTable(tableName: "zone") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "zonePK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "captain_id", type: "int8") {
				constraints(unique: "true")
			}

			column(name: "name", type: "varchar(255)") {
				constraints(nullable: "false", unique: "true")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-7") {
		addColumn(tableName: "address") {
			column(name: "address_type_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-8") {
		addColumn(tableName: "address") {
			column(name: "person_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-9") {
		addColumn(tableName: "address") {
			column(name: "phone_number1", type: "varchar(255)")
		}
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-10") {
		addColumn(tableName: "address") {
			column(name: "phone_number2", type: "varchar(255)")
		}
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-11") {
		addColumn(tableName: "address") {
			column(name: "phone_number3", type: "varchar(255)")
		}
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-12") {
		addColumn(tableName: "division") {
			column(name: "district_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-13") {
		addColumn(tableName: "election") {
			column(name: "complete", type: "bool") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-14") {
		addColumn(tableName: "person") {
			column(name: "email_address", type: "varchar(255)")
		}
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-15") {
		addColumn(tableName: "voter") {
			column(name: "zone_id", type: "int8")
		}
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-16") {
		addNotNullConstraint(columnDataType: "bool", columnName: "alive", tableName: "person")
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-17") {
		addNotNullConstraint(columnDataType: "varchar(1)", columnName: "code", tableName: "pledge")
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-18") {
		addNotNullConstraint(columnDataType: "int8", columnName: "pledge_id", tableName: "voter_election")
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-19") {
		addPrimaryKey(columnNames: "voter_id, person_id", constraintName: "dependentPK", tableName: "dependent")
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-20") {
		dropForeignKeyConstraint(baseTableName: "person", baseTableSchemaName: "public", constraintName: "fkc4e39b5568fe482b")
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-21") {
		createIndex(indexName: "name_unique_1330780518831", tableName: "activity_type", unique: "true") {
			column(name: "name")
		}
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-22") {
		createIndex(indexName: "name_unique_1330780518835", tableName: "address_type", unique: "true") {
			column(name: "name")
		}
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-23") {
		createIndex(indexName: "name_unique_1330780518851", tableName: "relation", unique: "true") {
			column(name: "name")
		}
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-24") {
		createIndex(indexName: "captain_id_unique_1330780518861", tableName: "zone", unique: "true") {
			column(name: "captain_id")
		}
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-25") {
		createIndex(indexName: "name_unique_1330780518861", tableName: "zone", unique: "true") {
			column(name: "name")
		}
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-26") {
		addForeignKeyConstraint(baseColumnNames: "activity_type_id", baseTableName: "activity", constraintName: "FK9D4BF30F458E35A8", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "activity_type", referencesUniqueColumn: "false")
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-27") {
		addForeignKeyConstraint(baseColumnNames: "voter_id", baseTableName: "activity", constraintName: "FK9D4BF30FA27EFEB", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "voter", referencesUniqueColumn: "false")
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-28") {
		addForeignKeyConstraint(baseColumnNames: "address_type_id", baseTableName: "address", constraintName: "FKBB979BF4F26C5EB4", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "address_type", referencesUniqueColumn: "false")
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-29") {
		addForeignKeyConstraint(baseColumnNames: "person_id", baseTableName: "address", constraintName: "FKBB979BF45598C889", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "person", referencesUniqueColumn: "false")
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-30") {
		addForeignKeyConstraint(baseColumnNames: "person_id", baseTableName: "dependent", constraintName: "FKBDE28AFF5598C889", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "person", referencesUniqueColumn: "false")
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-31") {
		addForeignKeyConstraint(baseColumnNames: "relation_id", baseTableName: "dependent", constraintName: "FKBDE28AFF7A46F369", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "relation", referencesUniqueColumn: "false")
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-32") {
		addForeignKeyConstraint(baseColumnNames: "voter_id", baseTableName: "dependent", constraintName: "FKBDE28AFFA27EFEB", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "voter", referencesUniqueColumn: "false")
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-33") {
		addForeignKeyConstraint(baseColumnNames: "district_id", baseTableName: "division", constraintName: "FK15BD30ADB252B229", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "district", referencesUniqueColumn: "false")
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-34") {
		addForeignKeyConstraint(baseColumnNames: "zone_id", baseTableName: "voter", constraintName: "FK6B30AC8918944E9", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "zone", referencesUniqueColumn: "false")
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-35") {
		addForeignKeyConstraint(baseColumnNames: "captain_id", baseTableName: "zone", constraintName: "FK3923AC297F520F", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "voter", referencesUniqueColumn: "false")
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-36") {
		dropColumn(columnName: "address_id", tableName: "person")
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-37") {
		dropColumn(columnName: "cell_phone", tableName: "person")
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-38") {
		dropColumn(columnName: "comments", tableName: "person")
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-39") {
		dropColumn(columnName: "home_phone", tableName: "person")
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-40") {
		dropColumn(columnName: "work_phone", tableName: "person")
	}

	changeSet(author: "rguerra (generated)", id: "1330780520081-41") {
		dropTable(tableName: "registration_code")
	}
}
