package com.cubrid.cubridmanager.core.common.model;

/**
 * cubrid_shard.conf configuration file parameters constants
 * 
 * @author Tobi
 * 
 * @version 1.0
 * @date 2012-12-4
 */
public class CubridShardConfParaConstants {

	// shard parameter
	public final static String SHARD_SECTION = "[shard]";
	public final static String SHARD_SECTION_NAME = "shard";
	// shard general
	public final static String MASTER_SHM_ID = "MASTER_SHM_ID";
	public final static String ADMIN_LOG_FILE = "ADMIN_LOG_FILE";

	// shard broker
	public final static String SERVICE = "SERVICE";
	public final static String BROKER_PORT = "BROKER_PORT";
	public final static String MIN_NUM_APPL_SERVER = "MIN_NUM_APPL_SERVER";
	public final static String MAX_NUM_APPL_SERVER = "MAX_NUM_APPL_SERVER";
	public final static String APPL_SERVER_SHM_ID = "APPL_SERVER_SHM_ID";
	public final static String LOG_DIR = "LOG_DIR";
	public final static String ERROR_LOG_DIR = "ERROR_LOG_DIR";
	public final static String SQL_LOG = "SQL_LOG";
	public final static String TIME_TO_KILL = "TIME_TO_KILL";
	public final static String SESSION_TIMEOUT = "SESSION_TIMEOUT";
	public final static String KEEP_CONNECTION = "KEEP_CONNECTION";
	public final static String MAX_PREPARED_STMT_COUNT = "MAX_PREPARED_STMT_COUNT";
	public final static String SHARD_DB_NAME = "SHARD_DB_NAME";
	public final static String SHARD_DB_USER = "SHARD_DB_USER";
	public final static String SHARD_DB_PASSWORD = "SHARD_DB_PASSWORD";
	public final static String NUM_PROXY_MIN = "NUM_PROXY_MIN";
	public final static String NUM_PROXY_MAX = "NUM_PROXY_MAX";
	public final static String PROXY_LOG_FILE = "PROXY_LOG_FILE";
	public final static String PROXY_LOG = "PROXY_LOG";
	public final static String MAX_CLIENT = "MAX_CLIENT";
	public final static String METADATA_SHM_ID = "METADATA_SHM_ID";
	public final static String SHARD_CONNECTION_FILE = "SHARD_CONNECTION_FILE";
	public final static String SHARD_KEY_FILE = "SHARD_KEY_FILE";

	public final static String PARAMETER_TYPE_BROKER_GENERAL = "general";
	public final static String PARAMETER_TYPE_BROKER_COMMON = "common";
	public final static String PARAMETER_TYPE_BROKER_ADVANCE = "advance";

	// shard broker parameter
	public static String[][] shardBrokerParameters = {
			{ MASTER_SHM_ID, "int(1024~65535)", "45001", PARAMETER_TYPE_BROKER_GENERAL },
			{ ADMIN_LOG_FILE, "string", "log/broker/cubrid_broker.log", PARAMETER_TYPE_BROKER_GENERAL },

			{ SERVICE, "string(ON|OFF)", "ON", PARAMETER_TYPE_BROKER_COMMON },
			{ BROKER_PORT, "int(1024~65535)", "45011", PARAMETER_TYPE_BROKER_COMMON },
			{ SHARD_DB_NAME, "string", "shard1", PARAMETER_TYPE_BROKER_COMMON },
			{ SHARD_DB_USER, "string", "shard", PARAMETER_TYPE_BROKER_COMMON },
			{ SHARD_DB_PASSWORD, "string", "shard123", PARAMETER_TYPE_BROKER_COMMON },
			{ SHARD_CONNECTION_FILE, "string", "shard_connection.txt", PARAMETER_TYPE_BROKER_COMMON },
			{ SHARD_KEY_FILE, "string", "shard_key.txt", PARAMETER_TYPE_BROKER_COMMON },
			{ APPL_SERVER_SHM_ID, "int(1024~65535)", "45011", PARAMETER_TYPE_BROKER_COMMON },
			{ METADATA_SHM_ID, "int(1024~65535)", "45092", PARAMETER_TYPE_BROKER_COMMON },

			{ MIN_NUM_APPL_SERVER, "int", "5", PARAMETER_TYPE_BROKER_ADVANCE },
			{ MAX_NUM_APPL_SERVER, "int", "40", PARAMETER_TYPE_BROKER_ADVANCE },
			{ LOG_DIR, "string", "log/broker/sql_log", PARAMETER_TYPE_BROKER_ADVANCE },
			{ ERROR_LOG_DIR, "string", "log/broker/error_log", PARAMETER_TYPE_BROKER_ADVANCE },
			{ SQL_LOG, "string(ON|OFF|ERROR|NOTICE|TIMEOUT)", "ON", PARAMETER_TYPE_BROKER_ADVANCE },
			{ TIME_TO_KILL, "int", "120", PARAMETER_TYPE_BROKER_ADVANCE },
			{ SESSION_TIMEOUT, "int", "300", PARAMETER_TYPE_BROKER_ADVANCE },
			{ KEEP_CONNECTION, "string(ON|OFF|AUTO)", "AUTO", PARAMETER_TYPE_BROKER_ADVANCE },
			{ MAX_PREPARED_STMT_COUNT, "int", "1024", PARAMETER_TYPE_BROKER_ADVANCE },
			{ NUM_PROXY_MIN, "int", "1", PARAMETER_TYPE_BROKER_ADVANCE },
			{ NUM_PROXY_MAX, "int", "1", PARAMETER_TYPE_BROKER_ADVANCE },
			{ PROXY_LOG_FILE, "string", "log/broker/proxy_log", PARAMETER_TYPE_BROKER_ADVANCE },
			{ PROXY_LOG, "string(ALL|ON|SHARD| SCHEDULE|NOTICE|TIMEOUT| ERROR|NONE|OFF)", "ALL",
					PARAMETER_TYPE_BROKER_ADVANCE },// TODO
			// (ALL|ON|SHARD|SCHEDULE|NOTICE|TIMEOUT|ERROR|NONE|OFF)
			{ MAX_CLIENT, "int", "10", PARAMETER_TYPE_BROKER_ADVANCE }

	};

	/**
	 * Get the static field brokerParameters
	 * 
	 * @return String[][]
	 */
	public static String[][] getShardParameters() {

		return shardBrokerParameters;
	}
}
